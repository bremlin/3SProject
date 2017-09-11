package com.ibcon.sproject.smet.model.xml;

import bsh.EvalError;
import bsh.Interpreter;
import com.ibcon.sproject.smet.model.objects.*;
import com.ibcon.sproject.smet.model.objects.smr.SmrWork;
import com.ibcon.sproject.smet.model.utilits.CheckValue;
import org.jdom2.Element;

import java.math.BigDecimal;
import java.util.*;

public class XMLSmr {

    private XmlSmet xmlSmet;
    private Element smrElement;
    private Norm localNorm;

    private HashMap<String, String> smrMasks;
    private HashMap<String, Double> smrValues;

    private HashMap<String, Double> smrCosts, smrEdits;
    private List<String> baseVals = Arrays.asList("PZ", "ZM", "EM", "OZ", "MT");
    private boolean pzSync = true;
    private ArrayList<SmetResourceAssignment> resourceAssignments;

    private XMLCoefficients localK;
    private HashMap<String, Double> levSum;

    private boolean isNoRegK;

    private SmrNode smrNode;
    private ChapterNode chapterNode;

    private SmetResourceCollection resourceCollection;

    public XMLSmr(XmlSmet xmlSmet, Element smrElement, ChapterNode chapter) {
            this.xmlSmet = xmlSmet;
            this.smrElement = smrElement;
            this.chapterNode = chapter;
            this.smrNode = xmlSmet.getSmrNode();

            this.resourceAssignments = new ArrayList<>();
            this.smrCosts = new HashMap<>();
            this.smrValues = new HashMap<>();
            this.smrEdits = new HashMap<>();
            this.smrMasks = new HashMap<>();

            //todo заполнить коллекцию
            resourceCollection = new SmetResourceCollection();

            parseLocalNorm();
            parseSmrValues();
            parseWorks();
            smrNode.updateValues(smrValues, smrCosts, smrMasks, resourceAssignments);
    }

    private void parseLocalNorm() {
        smrNode.setLocalNorm(smrElement.getAttribute("Options") != null && smrElement.getAttributeValue("Options").contains("CustNaclCurr"));
        if (smrElement.getChild("CustomNPCurr") != null) {
            localNorm = new Norm(smrElement.getChild("CustomNPCurr"));
        }
        this.pzSync = smrElement.getAttribute("PzSync") != null && !smrElement.getAttributeValue("PzSync").equals
                ("") && !smrElement.getAttributeValue("PzSync").equals("No");

        this.isNoRegK = smrElement.getAttribute("Options") != null && smrElement.getAttributeValue("Options").contains("NoRegK");
    }

    //region Расчет и парсинг показателей расценки
    private void parseSmrValues() {
        localK = null;

        parseResourceAssignments();
        parseLocalK();

        parseQuantity();
        parseValues();

        if (!xmlSmet.getIndexes().getIndexMode().equals("None")) applyIndexes();
        if (xmlSmet.getTerZoneK() != null && !smrNode.isEqui()) applyTerZoneK();
        if (localK != null) applyLocalCoefficients();

        sumValues();

        //region Округление стоимостей расценки до двух разрядов
        for (String key : smrValues.keySet()) {
            if (key.equals("TZR") || key.equals("TZM") || key.equals("quantity")) continue;
            BigDecimal a = new BigDecimal(String.valueOf(smrValues.get(key))).setScale(xmlSmet.getParameters().getRoundSum() ? 0 : 2, BigDecimal.ROUND_HALF_UP);
            smrValues.put(key, a.doubleValue());
        }
        //endregion

        if (xmlSmet.getRegionalK() != 1 && !isNoRegK) applyRegionalK();
        applyGlobalCoefficients();

        smrValues.put("SUM", smrValues.get("PZ"));
        applyNrSp(smrElement);

        //TODO Чмошный костыль для КИНЕФа
        if (smrNode.getVidRabId() == 10087 && smrValues.containsKey("PZ") && !smrValues.containsKey("OZ") &&
                !smrValues.containsKey("MT") && !smrValues.containsKey("EM") && !smrValues.containsKey("ZM")) {
            smrValues.put("MT", smrValues.get("PZ"));
        }

        if (smrNode.isDisabled()) return;

        xmlSmet.getSmet().setPz((xmlSmet.getSmet().getPz() != null ? xmlSmet.getSmet().getPz() : 0) +
                (smrValues.containsKey("PZ") ? smrValues.get("PZ") : 0));
        if (smrValues.containsKey("OZ")) xmlSmet.getSmet().setOz(xmlSmet.getSmet().getOz() + smrValues.get("OZ"));
        if (smrValues.containsKey("TZR")) xmlSmet.getSmet().setTzr(xmlSmet.getSmet().getTzr() + smrValues.get("TZR"));
        if (smrValues.containsKey("EM")) xmlSmet.getSmet().setEm(xmlSmet.getSmet().getEm() + smrValues.get("EM"));
        if (smrValues.containsKey("ZM")) xmlSmet.getSmet().setZm(xmlSmet.getSmet().getZm() + smrValues.get("ZM"));
        if (smrValues.containsKey("TZM")) xmlSmet.getSmet().setTzm(xmlSmet.getSmet().getTzm() + smrValues.get("TZM"));
        if (smrValues.containsKey("MT")) xmlSmet.getSmet().setMt(xmlSmet.getSmet().getMt() + smrValues.get("MT"));

        xmlSmet.getSmet().setNr(xmlSmet.getSmet().getNr() + (smrValues.get("NR")));
        xmlSmet.getSmet().setSp(xmlSmet.getSmet().getSp() + (smrValues.get("SP")));
        xmlSmet.getSmet().setSum(xmlSmet.getSmet().getPz() + xmlSmet.getSmet().getNr() + xmlSmet.getSmet().getSp());
    }
    //endregion


    //Расчет и парсинг назначений ресурсов
    private void parseResourceAssignments() {
        if (smrElement.getChild("Resources") == null || smrElement.getChild("Resources").getChildren().isEmpty()) return;

        Element rsrcs = smrElement.getChild("Resources");
        for (Element rsrcElement : rsrcs.getChildren() ) {

            SmetResource rsrc = parseResource(rsrcElement);
            if (rsrc == null) continue;
            SmetResourceAssignment smetRa = new SmetResourceAssignment(
                    rsrc,
                    rsrcElement,
                    xmlSmet.getParameters());

            resourceAssignments.add(smetRa);

            XMLCoefficients kRA = null;
            if (rsrcElement.getChild("Koefficients") != null) kRA = new XMLCoefficients(rsrcElement.getChild("Koefficients"));
            if (kRA!= null
                    && (rsrcElement.getAttribute("Attribs") == null
                    || (rsrcElement.getAttribute("Attribs") != null
                    && !rsrcElement.getAttributeValue("Attribs").contains("Deleted")))) smetRa.applyCoeff(kRA);

            for (String key : smetRa.getCosts().keySet()) {
                if (key.equals("TZR") || key.equals("TZM")) {
                    smrCosts.put(key, smrCosts.containsKey(key)? smrCosts.get(key) + smetRa.getCosts().get(key)
                            : smetRa.getCosts().get(key));
                }

                if (rsrcElement.getAttribute("Attribs") != null
                        && rsrcElement.getAttributeValue("Attribs").contains("Deleted")
                        && (rsrcElement.getAttribute("Options") == null || !rsrcElement.getAttributeValue("Options").contains("NotCount"))) {
                    smrEdits.put(key, smrEdits.containsKey(key)
                            ? smrEdits.get(key) - (smetRa.getCosts().get(key))
                            : smetRa.getCosts().get(key)* (-1));

                } else if ((rsrcElement.getAttribute("Attribs") != null
                        && rsrcElement.getAttributeValue("Attribs").contains("Replaced"))
                        || (rsrcElement.getAttribute("Attribs") != null
                        && rsrcElement.getAttributeValue("Attribs").contains("Added"))
                        || (rsrcElement.getAttribute("Options") != null && rsrcElement.getAttributeValue("Options").contains("NotCount")
                        && rsrcElement.getAttributeValue("Options").contains("CountInPlace"))) {

                    smrEdits.put(key, smrEdits.containsKey(key)
                            ? smrEdits.get(key) + (smetRa.getCosts().get(key))
                            : smetRa.getCosts().get(key));

                } else if (smetRa.raEdits != null && smetRa.raEdits.containsKey(key)) {

                    smrEdits.put(key, smrEdits.containsKey(key)
                            ? smrEdits.get(key)  + (smetRa.raEdits.get(key))
                            : smetRa.raEdits.get(key));

                }
            }

            if (rsrcElement.getAttribute("Attribs") != null
                    && smetRa.getAttrib().contains("Replaced")
                    && rsrcElement.getChild("Replaced") != null
                    && ((rsrcElement.getChild("Replaced").getAttribute("Options") != null
                    && !rsrcElement.getAttributeValue("Options").contains("NotCount")
                    && !rsrcElement.getChild("Replaced").getAttributeValue("Options").contains("CountInPlace"))
                    || rsrcElement.getChild("Replaced").getAttribute("Options") == null)) {

                Element tempEl = rsrcElement.getChild("Replaced");
                SmetResource rsrcDeleted = parseResource(tempEl);

                SmetResourceAssignment smetRaDeleted = new SmetResourceAssignment(
                        rsrcDeleted,
                        tempEl,
                        xmlSmet.getParameters());

                for (String key : smetRaDeleted.getCosts().keySet()) {
                    smrEdits.put(key, smrEdits.containsKey(key)
                            ? smrEdits.get(key) - smetRaDeleted.getCosts().get(key)
                            : smetRaDeleted.getCosts().get(key) * (-1));
                }
            }
        }
    }

    private void parseLocalK() {
        localK = null;

        //Разбираем коэффициенты сметной строки
        if (smrElement.getChild("Koefficients") != null
                && !smrElement.getChild("Koefficients").getChildren().isEmpty()) {
            localK = new XMLCoefficients(smrElement.getChild("Koefficients"));
        }

        //Если групповые коэффициенты не заданы, возвращаемся
        if (xmlSmet.getGlobalK() == null) return;

        for (int l : xmlSmet.getGlobalK().getLevels()) {
            for (XMLCoefficient k : xmlSmet.getGlobalK().getCoeffs().get(l)) {
                if (!k.isInPos()) continue;

                //Проверяем не попадает ли коэффициент в список отмененных
                Boolean excluded = false;
                if (smrElement.getChild("ExcludedKfs") != null
                        && smrElement.getChild("ExcludedKfs").getAttribute("Value") != null) {

                    String excludedKfs = smrElement.getChild("ExcludedKfs").getAttributeValue("Value").replace("(", "").replace(")", "").replace(" ", "");
                    for (String kStr : excludedKfs.split(",")) excluded = xmlSmet.getGlobalK().getCoeffList().get(Integer.parseInt(kStr)).equals(k);
                }
                if (excluded) continue;

                if (isApplyK(k)) {
                    if (localK == null) localK = new XMLCoefficients();
                    if (!localK.getLevels().contains(l)) {
                        localK.getLevels().add(l);
                        localK.getCoeffs().put(l, new ArrayList<XMLCoefficient>());
                    }
                    localK.getCoeffs().get(l).add(k);
                }
            }
        }
        if (localK != null) localK.sortLevels();
    }

    private void parseWorks() {
        if (smrElement.getChild("WorksList") == null || smrElement.getChild("WorksList").getChildren().isEmpty() ) return;

        for (Element w : smrElement.getChild("WorksList").getChildren()) {
            if (!w.getName().equals("Work") || w.getAttribute("Caption") == null
                    || w.getAttributeValue("Caption").equals("")) continue;
            smrNode.getWorks().add(new SmrWork(w.getAttributeValue("Caption")));
        }
    }

    private void parseQuantity() {
        double quantity = 0.0;
        if (smrElement.getChild("Quantity") != null
                && smrElement.getChild("Quantity").getAttribute("Result") != null)
            quantity = Double.parseDouble(smrElement.getChild("Quantity").getAttributeValue("Result").replace(",", "."));
        else if (smrElement.getAttribute("Quantity") != null) {
            if (CheckValue.isDouble(smrElement.getAttributeValue("Quantity").replace(",", "."))) {
                quantity = Double.parseDouble(smrElement.getAttributeValue("Quantity").replace(",", "."));

            } else {
                if (xmlSmet.getIdentifierValues().containsKey(smrElement.getAttributeValue("Quantity"))) {
                    quantity = xmlSmet.getIdentifierValues().get(smrElement.getAttributeValue("Quantity"));
                } else {
                    String stringQuantity = smrElement.getAttributeValue("Quantity").toLowerCase();
                    ArrayList<String> identList = new ArrayList<>(xmlSmet.getIdentifierValues().keySet());

                    Collections.sort(identList, new Comparator<String>() {
                        @Override
                        public int compare(String string1, String string2) {
                            if (string1.length() > string2.length()) {
                                return -1;
                            } else if (string1.length() < string2.length()) {
                                return 1;
                            }
                            return 0;
                        }
                    });

                    for (String i : identList) {
                        if (stringQuantity.contains(i)) {
                            stringQuantity = stringQuantity.replace(i, xmlSmet.getIdentifierValues().get(i).toString());
                        }
                    }
                    try {
                        Interpreter interpreter = new Interpreter();
                        stringQuantity = stringQuantity.replace(",", ".");
                        int rounding = -1;
                        stringQuantity = stringQuantity.replace(" ", "");
                        String toRound = stringQuantity.substring(stringQuantity.indexOf(";") + 1);
                        if (toRound.contains(")")) toRound = stringQuantity.substring(stringQuantity.indexOf(";")+1, stringQuantity.indexOf(";")+2);

                        if (stringQuantity.contains("окр(")) {
                            stringQuantity = stringQuantity.replace("окр(", "");
                            rounding = Integer.valueOf(toRound);
                            stringQuantity = stringQuantity.substring(0, stringQuantity.indexOf(";"));
                        } else if (stringQuantity.contains("округл(")) {
                            stringQuantity = stringQuantity.replace("округл(", "");
                            rounding = Integer.valueOf(toRound);
                            stringQuantity = stringQuantity.substring(0, stringQuantity.indexOf(";"));
                        }
                        interpreter.eval("quantity=" + "0.1*10*" + stringQuantity);
                        BigDecimal bdQuantity = new BigDecimal(interpreter.get("quantity").toString());
                        if (rounding > -1) bdQuantity = bdQuantity.setScale(rounding, BigDecimal.ROUND_HALF_UP);
                        quantity = bdQuantity.doubleValue();
                    } catch (EvalError evalError) {
                        System.out.println("bad Formula2: " + smrElement.getAttributeValue("Quantity").replace(",", "."));
                    }
                }
            }
        }
        smrValues.put("quantity", quantity);
    }

    private void parseValues() {
        for (String val : baseVals) {
            String stringValue;

            if (smrElement.getAttribute("BaseCalcMode") == null || !smrElement.getAttributeValue("BaseCalcMode").equals("ForceRes")) {

                if (smrElement.getChild("PriceBase") != null){
                    Element elementPrice = smrElement.getChild("PriceBase");

                    if (elementPrice.getAttribute(val) != null && !elementPrice.getAttributeValue(val).equals("")) {
                        stringValue = smrElement.getChild("PriceBase").getAttributeValue(val).replace(",", ".");
                        smrCosts.put(val, Double.parseDouble(stringValue));
                    }

                } else if ((!smrCosts.containsKey(val) || smrCosts.get(val) == 0)
                        && smrElement.getChild("PriceCurr") != null) {
                    Element elementPrice = smrElement.getChild("PriceCurr");
                    if (elementPrice.getAttribute(val) != null && !elementPrice.getAttributeValue(val).equals("")) {
                        stringValue = smrElement.getChild("PriceCurr").getAttributeValue(val).replace(",", ".");
                        smrCosts.put(val, Double.parseDouble(stringValue));
                        if (!val.equals("PZ") && !val.equals("ZM") && pzSync)
                            smrCosts.put("PZ", (smrCosts.containsKey("PZ") ? smrCosts.get("PZ") : 0.0) + smrCosts.get(val));
                    }
                }
            } else if (!smrCosts.containsKey(val)) {
                smrCosts.put(val, 0.0);
            }
        }

        smrCosts.put("EMR",
                ((smrCosts.containsKey("EM")) ? smrCosts.get("EM") : 0) - ((smrCosts.containsKey("ZM")) ? smrCosts.get("ZM") : 0));

        smrValues.put("TZR", smrCosts.containsKey("TZR") ? smrCosts.get("TZR") : 0.0);
        smrValues.put("TZM", smrCosts.containsKey("TZM") ? smrCosts.get("TZM") : 0.0);

        for (String val : baseVals) {
            if (smrEdits.containsKey(val)) {
                smrCosts.put(val, (smrCosts.containsKey(val) ? smrCosts.get(val) : 0) + (smrEdits.get(val)));
                if (val.equals("ZM") && smrEdits.containsKey("EM")) continue;
                if (smrCosts.containsKey("PZ")) smrCosts.put("PZ", smrCosts.get("PZ") + (smrEdits.get(val)));
            }
        }

        if ((smrElement.getAttribute("BaseCalcMode") != null
                && smrElement.getAttributeValue("BaseCalcMode").equals("ForceRes")) || pzSync) {
            double sum = smrCosts.containsKey("OZ") ? smrCosts.get("OZ") : 0;
            sum = sum + (smrCosts.containsKey("MT") ? smrCosts.get("MT") : 0);
            sum = sum + (smrCosts.containsKey("EM") ? smrCosts.get("EM") : smrCosts.containsKey("ZM") ? smrCosts.get
                    ("ZM") : 0);
            if (sum > 0 && !smrCosts.containsKey("PZ")) smrCosts.put("PZ", sum);
        }

        //Округление стоимостей расценки до двух разрядов
        for (String key : smrCosts.keySet()) {
            if (key.equals("TZR") || key.equals("TZM")) continue;
            BigDecimal a = new BigDecimal(String.valueOf(smrCosts.get(key))).setScale(2, BigDecimal.ROUND_HALF_UP);
            smrCosts.put(key, a.doubleValue());
        }

        if (smrEdits.containsKey("EMR")) smrCosts.put("EMR", smrCosts.get("EMR")+(smrEdits.get("EMR")));
    }

    private void applyIndexes() {
//      Для применения индексов к текущим ценам
        if (smrElement.getAttribute("Index8401Code") == null || smrElement.getAttributeValue("Index8401Code").equals("")) return;


        String indexCode = "";
        if (smrElement.getAttribute("Index8401Code") != null && !smrElement.getAttributeValue("Index8401Code").equals("")) {
            indexCode = smrElement.getAttributeValue("Index8401Code");
        }
        if (xmlSmet.getIndexes().getIndexesCode().containsKey(indexCode)) {

            XMLIndex i = xmlSmet.getIndexes().getIndexesCode().get(indexCode);
            for (String key : i.getValues().keySet()) {
                if (!smrValues.containsKey(key)) continue;
                Double calc = smrValues.get(key) * (i.getValues().get(key)-1);
                calc = new BigDecimal(String.valueOf(calc)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                smrValues.put(key, (smrValues.containsKey(key) ? smrValues.get(key) : 0) + calc);

                if (!key.equals("ZM"))
                    smrValues.put("PZ", (smrValues.containsKey("PZ") ? smrValues.get("PZ") : 0) + calc);

                if (key.equals("ZM") && !i.getValues().keySet().contains("EM")) {
                    smrValues.put("EM", smrValues.get("EM") + calc);
                    smrValues.put("PZ", smrValues.get("PZ") + calc);
                }
            }
        }
    }

    //Применение территориальной поправки
    private void applyTerZoneK() {
        XMLCoefficient coeff = xmlSmet.getTerZoneK();

        //Применение коэффициента ПЗ
        if (coeff.getPz() != null && smrCosts.containsKey("PZ")) {
            smrCosts.put("PZ", smrCosts.get("PZ")*coeff.getPz());
        }
        //Применение коэффициента ОЗ
        if (coeff.getOz() != null || (coeff.isPzAll() && coeff.getPz() != null)) {

            double k = 1;
            if (coeff.getOz() != null) k = coeff.getOz();
            if (coeff.isPzAll() && coeff.getPz() != null) k = k * coeff.getPz();

            if (smrCosts.containsKey("OZ")) {
                Double calc = smrCosts.get("OZ")*(k-1);
                smrCosts.put("OZ", smrCosts.get("OZ") + calc);
                if (coeff.getPz() == null || (coeff.isPzAll() && !smrCosts.containsKey("PZ"))) {
                    smrCosts.put("PZ", ((smrCosts.containsKey("PZ")) ? smrCosts.get("PZ") : 0) + calc);
                }
                if (coeff.getPz() != null && coeff.getOz() != null && coeff.isPzAll() && smrCosts.containsKey("PZ")) {
                    smrCosts.put("PZ", smrCosts.get("PZ") + calc - smrCosts.get("OZ")*(coeff.getPz() - 1));
                }
            }
            if (coeff.isOzpTz() && smrCosts.containsKey("TZR")) smrCosts.put("TZR", smrCosts.get("TZR")*k);

            for (SmetResourceAssignment ra : resourceAssignments) {
                if (!ra.getType().equals("T")) continue;
                ra.getCosts().put("OZ", (ra.getCosts().containsKey("OZ") ? ra.getCosts().get("OZ") : 0) * k);
                if (coeff.isOzpTz()) ra.getCosts().put("TZR", (ra.getCosts().containsKey("TZR") ? ra.getCosts().get
                        ("TZR") : 0) * k);
            }
        }
        //Применение коэффициента ЭМ
        if (coeff.getEm() != null || (coeff.isPzAll() && coeff.getPz() != null)) {

            double k = 1;
            if (coeff.getEm() != null) k = coeff.getEm();
            if (coeff.isPzAll() && coeff.getPz() != null) k = k * coeff.getPz();

            if (smrCosts.containsKey("EM")) {
                Double calc;
                if (coeff.isSeparEm() && !coeff.isEmAll() && smrCosts.containsKey("ZM")){
                    calc = (smrCosts.get("EM") - smrCosts.get("ZM"))*(k - 1);
                } else {
                    calc = smrCosts.get("EM")*(k - 1);
                }
                smrCosts.put("EM", smrCosts.get("EM") + calc);

                if (coeff.getPz() == null || (coeff.isPzAll() && !smrCosts.containsKey("PZ"))) {
                    smrCosts.put("PZ", ((smrCosts.containsKey("PZ")) ? smrCosts.get("PZ") : 0) + calc);
                }
            }

            for (SmetResourceAssignment ra : resourceAssignments) {
                if (!ra.getType().equals("Mech")) continue;
                Double calc;
                if (coeff.isSeparEm() && !coeff.isEmAll()){
                    calc = ((ra.getCosts().containsKey("EM") ? ra.getCosts().get("EM") : 0) - (ra.getCosts()
                            .containsKey("ZM") ? ra.getCosts().get("ZM") : 0))*(k-1);
                } else {
                    calc = (ra.getCosts().containsKey("EM") ? ra.getCosts().get("EM") : 0)*(k-1);
                }
                ra.getCosts().put("EM", (ra.getCosts().containsKey("EM") ? ra.getCosts().get("EM") : 0) + calc);
            }
        }
        //Применение коэффициента ЗМ
        if (coeff.getZm() != null
                || (coeff.isEmAll() && coeff.getEm() != null)
                || (coeff.isPzAll() && coeff.getPz() != null)) {

            double k = 1;
            if (coeff.getZm() != null) k = coeff.getZm();
            if (coeff.isEmAll() && coeff.getEm() != null) k = k * coeff.getEm();
            if (coeff.isPzAll() && coeff.getPz() != null) k = k * coeff.getPz();

            if (smrCosts.containsKey("ZM")) {
                Double calc = smrCosts.get("ZM") * (k - 1);
                smrCosts.put("ZM", smrCosts.get("ZM") + calc);
                if ((coeff.getEm() == null && coeff.getZm() != null && !coeff.isPzAll()) || (coeff.isEmAll() &&
                        !smrCosts.containsKey("EM"))) {
                    smrCosts.put("EM", ((smrCosts.containsKey("EM")) ? smrCosts.get("EM") : 0) + calc);
                    if (coeff.getPz() == null || (coeff.isPzAll() && !smrCosts.containsKey("PZ"))) {
                        smrCosts.put("PZ", ((smrCosts.containsKey("PZ")) ? smrCosts.get("PZ") : 0) + calc);
                    }
                }
            }
            if (coeff.isZpmTz() && smrCosts.containsKey("TZM")) smrCosts.put("TZM", smrCosts.get("TZM") * k);

            for (SmetResourceAssignment ra : resourceAssignments) {
                if (coeff.isZpmTz() && ra.getType().equals("Tzm")) {
                    ra.getCosts().put("TZM", (ra.getCosts().containsKey("TZM") ? ra.getCosts().get("TZM") : 0) * k);
                } else if (ra.getType().equals("Mech")) {
                    ra.getCosts().put("ZM", (ra.getCosts().containsKey("ZM") ? ra.getCosts().get("ZM") : 0) * k);
                }
            }
        }
        //Применение коэффициента МАТ
        if (coeff.getMt() != null || (coeff.isPzAll() && coeff.getPz() != null)) {

            double k = 1;
            if (coeff.getMt() != null) k = coeff.getMt();
            if (coeff.isPzAll() && coeff.getPz() != null) k = k * coeff.getPz();

            if (smrCosts.containsKey("MT")) {
                Double calc = smrCosts.get("MT")*(k-1);
                smrCosts.put("MT", smrCosts.get("MT") + calc);
                if (coeff.getPz() == null || (coeff.isPzAll() && !smrCosts.containsKey("PZ"))) {
                    smrCosts.put("PZ", ((smrCosts.containsKey("PZ")) ? smrCosts.get("PZ") : 0) + calc);
                }
            }
            for (SmetResourceAssignment ra : resourceAssignments) {
                if (!ra.getType().equals("Mat")) continue;
                ra.getCosts().put("MT", (ra.getCosts().containsKey("MT") ? ra.getCosts().get("MT") : 0) * k);
                if (coeff.isMatQty()) ra.setQuantity(ra.getQuantity() * k);
            }
        }

        smrCosts.put("EMR",
                (smrCosts.containsKey("EM") ? smrCosts.get("EM") : 0)
                        - (smrCosts.containsKey("ZM") ? smrCosts.get("ZM") : 0));

        for (SmetResourceAssignment smRa : resourceAssignments) {
            if (!smRa.getCosts().containsKey("EM")) continue;
            smRa.getCosts().put("EM", smRa.getCosts().get("ZM")+(smRa.getCosts().get("EMR")));
        }
    }

    //Применение группы локальных коэффициентов
    private void applyLocalCoefficients() {

        if (localK == null) return;

        for (int lev : localK.getLevels()) {

            levSum = new HashMap<>();
            for (SmetResourceAssignment smRa : resourceAssignments) {
                smRa.setLevelSum(new HashMap<String, Double>());
            }

            Element chapter = smrElement.getParentElement();

            for (XMLCoefficient coeff : localK.getCoeffs().get(lev)) {

                if (coeff.isDisabled()) continue;

                //Расчет показателей по формулам
                if (coeff.getKind().equals("Formula")) {

                    Interpreter interpreter = new Interpreter();
                    Double eval = 0.0;

                    String rawFormula = coeff.getFormula().substring(coeff.getFormula().indexOf("=")+1);

                    if (smrNode.isEqui() && CheckValue.isDouble(rawFormula.replace(",","."))) continue;

                    if (smrNode.isEqui() && rawFormula.contains("МАТ")) {
                        String s = /*rawFormula.contains("ПЗ") ? "ПЗ" : */"МАТ";
                        String r = rawFormula.substring(rawFormula.indexOf(s) - 1, rawFormula.indexOf(s) + s.length());
                        rawFormula = rawFormula.replace(r, "");
                    }

                    if (rawFormula.contains("ПЗ")) {
                        if (smrCosts.keySet().contains("PZ")) {
                            rawFormula=rawFormula.replace("ПЗ", smrCosts.get("PZ").toString());
                        } else {
                            rawFormula=rawFormula.replace("ПЗ", "0");
                        }
                    }

                    if (rawFormula.contains("ОЗП")) {
                        if (smrCosts.keySet().contains("OZ")) {
                            rawFormula=rawFormula.replace("ОЗП", smrCosts.get("OZ").toString());
                        } else {
                            rawFormula=rawFormula.replace("ОЗП", "0");
                        }
                    }

                    if (rawFormula.contains("ЭМ")) {
                        if (smrCosts.keySet().contains("EM")) {
                            rawFormula = rawFormula.replace("ЭМ", smrCosts.get("EM").toString());
                        } else {
                            rawFormula = rawFormula.replace("ЭМ", "0");
                        }
                    }

                    if (rawFormula.contains("МАТ")) {
                        if (smrCosts.keySet().contains("MT")) {
                            rawFormula = rawFormula.replace("МАТ", smrCosts.get("MT").toString());
                        } else {
                            rawFormula = rawFormula.replace("МАТ", "0");
                        }
                    }

                    String value = coeff.getFormulaValue();
                    String formula = value + "=" + rawFormula.replace("/", "*1.0/");
                    int rounding = -1;

                    //region Если формула не вычисляется пробуем подставить переменные
                    if (!isFormulaOk(formula, value)) {
                        rawFormula = rawFormula.toLowerCase();
                        ArrayList<String> identList = new ArrayList<>(xmlSmet.getIdentifierValues().keySet());
                        Collections.sort(identList, new Comparator<String>() {
                            @Override
                            public int compare(String string1, String string2) {
                                if (string1.length() > string2.length()) {
                                    return -1;
                                } else if (string1.length() < string2.length()) {
                                    return 1;
                                }
                                return 0;
                            }
                        });

                        for (String i : identList) {
                            if (!rawFormula.contains(i)) continue;
                            rawFormula = rawFormula.replace(i, xmlSmet.getIdentifierValues().get(i).toString());
                        }

                        rawFormula = rawFormula.replace(",", ".");

                        if (rawFormula.contains("окр(")) {
                            rawFormula = rawFormula.replace("окр(", "");
                            rounding = Integer.valueOf(rawFormula.substring(rawFormula.indexOf(";")+1));
                            rawFormula = rawFormula.substring(0, rawFormula.indexOf(";"));
                        }
                    }

                    try {
                        formula = value + "=" + rawFormula.replace("/", "*1.0/");
                        interpreter.eval(formula);
                        BigDecimal bdQuantity = new BigDecimal(interpreter.get(value).toString());
                        if (rounding > -1) bdQuantity = bdQuantity.setScale(rounding, BigDecimal.ROUND_HALF_UP);
                        eval = bdQuantity.doubleValue();
                    } catch (EvalError evalError) {
                        System.out.println("bad Formula : " + formula);
                    }

                    value = value.replace("ПЗ", "PZ").replace("ОЗП", "OZ").replace("ЭМ", "EM").replace("ЗПМ", "ZM").replace("МАТ", "MT");

                    if (xmlSmet.getParameters().isTempRound()) {
                        eval = new BigDecimal(String.valueOf(eval)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }

                    levSum.put(value, (levSum.containsKey(value) ? levSum.get(value) : 0) + eval);
                    if (value.equals("ZM")) levSum.put("EM", (levSum.containsKey("EM") ? levSum.get("EM") : 0) + eval);
                    if (!value.equals("PZ")) levSum.put("PZ", (levSum.containsKey("PZ") ? levSum.get("PZ") : 0) + eval);

                    continue;

                }

                //Расчет по коэффициентам
                if ((coeff.isAllChapters() && coeff.isAllVidRabs())
                        || (!coeff.isAllChapters() && chapter.getName().equals("Chapter") && chapter.getAttribute("SysID") != null
                        && coeff.getChaptersLink().contains(chapter.getAttributeValue("SysID")))
                        || (!coeff.isAllVidRabs() && smrElement.getAttribute("Vr2001")!=null && coeff.getVrsLink().contains(smrElement.getAttributeValue("Vr2001")))) {

                    applyGroupK(coeff, smrCosts);
                }

                if (xmlSmet.getParameters().isTempRound()) {
                    for (String k : levSum.keySet()) {
                        BigDecimal a = new BigDecimal(String.valueOf(levSum.get(k))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        levSum.put(k, a.doubleValue());
                    }
                }
            }
            //Суммирование показателей по уровням
            for (String key : levSum.keySet()) {
                BigDecimal a = new BigDecimal(String.valueOf((smrCosts.containsKey(key) ? smrCosts.get(key) : 0) +
                        levSum.get(key)));
                if (xmlSmet.getParameters().isTempRound()) a = a.setScale(2, BigDecimal.ROUND_HALF_UP);
                smrCosts.put(key, a.doubleValue());

                for (SmetResourceAssignment smRa : resourceAssignments) {
                    if (!smRa.getCosts().containsKey(key)) continue;
                    smRa.getCosts().put(key, smRa.getCosts().get(key) + (smRa.getLevelSum().containsKey(key) ? smRa.getLevelSum().get(key) : 0));
                }
            }
        }

        for (String k : smrCosts.keySet()) {
            smrCosts.put(k, new BigDecimal(String.valueOf(smrCosts.get(k))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

        smrCosts.put("EMR",
                (smrCosts.containsKey("EM") ? smrCosts.get("EM") : 0)
                        - (smrCosts.containsKey("ZM") ? smrCosts.get("ZM") : 0));

        for (SmetResourceAssignment smRa : resourceAssignments) {
            smRa.setLevelSum(null);
            if (!smRa.getCosts().containsKey("EM")) continue;
            smRa.getCosts().put("EM", smRa.getCosts().get("ZM")+(smRa.getCosts().get("EMR")));
        }
    }

    //Суммирование показателей
    private void sumValues() {
        for (String key : smrCosts.keySet()) {
            smrValues.put(key, smrCosts.get(key) * (smrValues.get("quantity")));
        }

        if (!smrValues.containsKey("PZ") && pzSync) {
            smrValues.put("PZ", (smrValues.containsKey("OZ") ? smrValues.get("OZ") : 0)
                    + (smrValues.containsKey("EM") ? smrValues.get("EM") : 0)
                    + (smrValues.containsKey("MT") ? smrValues.get("MT") : 0));
        }

        smrValues.put("EMR", (smrCosts.containsKey("EM") ? smrCosts.get("EM") : 0)-(smrValues.containsKey("ZM") ? smrValues.get("ZM") : 0));
    }

    //Применение группы глобальных коэффициентов
    private void applyGlobalCoefficients() {

        if (xmlSmet.getGlobalK() == null) return;

        for (int lev : xmlSmet.getGlobalK().getLevels()) {

            levSum = new HashMap<>();
            for (SmetResourceAssignment smRa : resourceAssignments) {
                smRa.setLevelSum(new HashMap<String, Double>());
            }

            for (XMLCoefficient koeff : xmlSmet.getGlobalK().getCoeffs().get(lev)) {
                if (isExcludedK(koeff) || koeff.isInPos() || !isApplyK(koeff) || koeff.isDisabled()) continue;
                //Расчет показателей по формулам
                if (koeff.getKind().equals("Formula")) {

                    Interpreter interpreter = new Interpreter();
                    Double eval = 0.0;

                    String rawFormula = koeff.getFormula().substring(koeff.getFormula().indexOf("=")+1);

                    if (smrNode.isEqui() && CheckValue.isDouble(rawFormula.replace(",","."))) continue;

                    if (smrNode.isEqui() && rawFormula.contains("МАТ")) {
                        String s = "МАТ";
                        String r = rawFormula.substring(rawFormula.indexOf(s) - 1, rawFormula.indexOf(s) + s.length());
                        rawFormula = rawFormula.replace(r, "");
                    }

                    if (rawFormula.contains("ПЗ")) {
                        if (smrValues.keySet().contains("PZ")) {
                            rawFormula=rawFormula.replace("ПЗ", smrValues.get("PZ").toString());
                        } else {
                            rawFormula=rawFormula.replace("ПЗ", "0");
                        }
                    }

                    if (rawFormula.contains("ОЗП")) {
                        if (smrValues.keySet().contains("OZ")) {
                            rawFormula=rawFormula.replace("ОЗП", smrValues.get("OZ").toString());
                        } else {
                            rawFormula=rawFormula.replace("ОЗП", "0");
                        }
                    }

                    if (rawFormula.contains("ЭМ")) {
                        if (smrValues.keySet().contains("EM")) {
                            rawFormula = rawFormula.replace("ЭМ", smrValues.get("EM").toString());
                        } else {
                            rawFormula = rawFormula.replace("ЭМ", "0");
                        }
                    }

                    if (rawFormula.contains("МАТ")) {
                        if (smrValues.keySet().contains("MT")) {
                            rawFormula = rawFormula.replace("МАТ", smrValues.get("MT").toString());
                        } else {
                            rawFormula = rawFormula.replace("МАТ", "0");
                        }
                    }

                    String value = koeff.getFormulaValue();
                    String formula = value + "=" + rawFormula.replace("/", "*1.0/");
                    int rounding = -1;

                    //Если формула не вычисляется пробуем подставить переменные
                    if (!isFormulaOk(formula, value)) {
                        rawFormula = rawFormula.toLowerCase();
                        ArrayList<String> identList = new ArrayList<>(xmlSmet.getIdentifierValues().keySet());
                        Collections.sort(identList, new Comparator<String>() {
                            @Override
                            public int compare(String string1, String string2) {
                                if (string1.length() > string2.length()) {
                                    return -1;
                                } else if (string1.length() < string2.length()) {
                                    return 1;
                                }
                                return 0;
                            }
                        });

                        for (String i : identList) {
                            if (!rawFormula.contains(i)) continue;
                            rawFormula = rawFormula.replace(i, xmlSmet.getIdentifierValues().get(i).toString());
                        }

                        rawFormula = rawFormula.replace(",", ".");

                        if (rawFormula.contains("окр(")) {
                            rawFormula = rawFormula.replace("окр(", "");
                            rounding = Integer.valueOf(rawFormula.substring(rawFormula.indexOf(";")+1));
                            rawFormula = rawFormula.substring(0, rawFormula.indexOf(";"));
                        }
                    }

                    try {
                        formula = value + "=" + rawFormula.replace("/", "*1.0/");
                        interpreter.eval(formula);
                        BigDecimal bdQuantity = new BigDecimal(interpreter.get(value).toString());
                        if (rounding > -1) bdQuantity = bdQuantity.setScale(rounding, BigDecimal.ROUND_HALF_UP);
                        eval = bdQuantity.doubleValue();
                    } catch (EvalError evalError) {
                        System.out.println("bad Formula : " + formula);
                    }

                    value = value.replace("ПЗ", "PZ").replace("ОЗП", "OZ").replace("ЭМ", "EM").replace("ЗПМ", "ZM").replace("МАТ", "MT");

                    if (xmlSmet.getParameters().isTempRound()) {
                        eval = new BigDecimal(String.valueOf(eval)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }

                    levSum.put(value, (levSum.containsKey(value) ? levSum.get(value) : 0) + eval);
                    if (value.equals("ZM")) levSum.put("EM", (levSum.containsKey("EM") ? levSum.get("EM") : 0) + eval);
                    if (!value.equals("PZ")) levSum.put("PZ", (levSum.containsKey("PZ") ? levSum.get("PZ") : 0) + eval);

                } else {
                    applyGroupK(koeff, smrValues);
                }

            }

            for (String key : levSum.keySet()) {
                BigDecimal a = new BigDecimal(smrValues.containsKey(key) ? smrValues.get(key) : 0.0).add(new
                        BigDecimal(levSum.get(key))).setScale(xmlSmet.getParameters().getRoundSum() ? 0 : 2, BigDecimal.ROUND_HALF_UP);
                smrValues.put(key, a.doubleValue());

                for (SmetResourceAssignment smRa : resourceAssignments) {
                    if (!smRa.getCosts().containsKey(key)) continue;
                    smRa.getCosts().put(key, ((smRa.getCosts().containsKey(key)) ? smRa.getCosts().get(key) : 0)
                            + ((smRa.getLevelSum().containsKey(key)) ? smRa.getLevelSum().get(key) : 0));
                }
            }
        }

        smrValues.put("EMR", (smrValues.containsKey("EM") ? smrValues.get("EM") : 0)
                - (smrValues.containsKey("ZM") ? smrValues.get("ZM") : 0));

        for (SmetResourceAssignment smRa : resourceAssignments) {
            smRa.setLevelSum(null);
            if (!smRa.getCosts().containsKey("EM")) continue;
            Double ZMCost = (smRa.getCosts().containsKey("ZM") ? smRa.getCosts().get("ZM") : 0.00);
            Double EMRCost = (smRa.getCosts().containsKey("EMR") ? smRa.getCosts().get("EMR") : 0.00);
            smRa.getCosts().put("EM", ZMCost + EMRCost);
        }
    }

    //Применение регионального коэффициента
    private void applyRegionalK() {

        List<String> keys = Arrays.asList("ZM", "OZ");
        for (String key : keys) {

            if (!smrValues.containsKey(key)) continue;

            Double calc = new BigDecimal(smrValues.get(key) * (xmlSmet.getRegionalK() - 1)).setScale(xmlSmet.getParameters().getRoundSum() ? 0 : 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            smrValues.put(key, smrValues.get(key) + calc);

            if (key.equals("ZM") && smrValues.containsKey("EM")) {
                smrValues.put("EM", smrValues.get("EM")+calc);
            }
            smrValues.put("PZ", smrValues.containsKey("PZ") ? smrValues.get("PZ") + calc  : calc);

            for (SmetResourceAssignment smRa : resourceAssignments) {

                if (!smRa.getCosts().containsKey(key)) continue;
                smRa.getCosts().put(key, smRa.getCosts().get(key) * xmlSmet.getRegionalK());
                if (key.equals("ZM")) {
                    Double keyCost = (smRa.getCosts().containsKey(key) ? smRa.getCosts().get(key) : 0.00);
                    Double EMRCost = (smRa.getCosts().containsKey("EMR") ? smRa.getCosts().get("EMR") : 0.00);
                    smRa.getCosts().put("EM", keyCost + EMRCost);
                }
            }
        }
    }

    private void applyNrSp(Element smrElement) {

        smrValues.put("NR", 0.0);
        smrValues.put("SP", 0.0);

        //region Инициализация общего норматива
        Norm globNorm = xmlSmet.getNrsp().getGlobalNorm();
        //endregion

        //region Инициализация норматива по видам работ
        XMLVidRab vr = null;
        if (smrElement.getAttribute("Vr2001")!=null && !smrElement.getAttributeValue("Vr2001").equals("")) {

            Integer smrVr = Integer.parseInt(smrElement.getAttributeValue("Vr2001"));
            for (XMLVidRabGroup vrg : xmlSmet.getVrgs().values()) {
                if (!vrg.containsKey(smrVr)) continue;
                vr = vrg.get(smrVr);
                break;

            }

        }
        //endregion

        String maskNR = "";
        String maskSP = "";
        Double NR = 0.0;
        Double SP = 0.0;

        if (smrNode.isLocalNorm()) {
            if (localNorm != null && localNorm.getNr() != null) {
                maskNR = localNorm.getNrMask();
                NR = localNorm.getNr();
            }
        } else if (xmlSmet.getNrsp().isGlobalNR()) {
            maskNR = globNorm.getNrMask();
            NR = globNorm.getNr();
        } else if ( vr!= null ){
            maskNR = vr.getNaclMask();
            NR = vr.getNacl();
        }

        if (maskNR != null && !maskNR.equals("") && NR != null && NR != 0) {
            smrCosts.put("NR", NR);
            smrMasks.put("NR", maskNR);

            if (xmlSmet.getNrsp().getNRK() != null && xmlSmet.getNrsp().getNRK() != 0) smrCosts.put("NRK", xmlSmet.getNrsp().getNRK());
            else if (vr.getNaclK() != null) smrCosts.put("NRK", vr.getNaclK());
            else if (smrElement.getAttribute("NKB") != null) smrCosts.put("NRK", Double.parseDouble(smrElement.getAttributeValue("NKB").replace(",", ".")));

            applyNorm("NR");
        }
        if (smrNode.isLocalNorm()) {
            if (localNorm != null && localNorm.getSp() != null) {
                maskSP = localNorm.getSpMask();
                SP = localNorm.getSp();
            }
        } else if (xmlSmet.getNrsp().isGlobalSP()) {
            maskSP = globNorm.getSpMask();
            SP = globNorm.getSp();
        } else if ( vr!= null ){
            maskSP = vr.getPlanMask();
            SP = vr.getPlan();
        }

        if (maskSP != null &&!maskSP.equals("") && SP != null && SP != 0) {
            smrCosts.put("SP", SP);
            smrMasks.put("SP", maskSP);

            if (xmlSmet.getNrsp().getSPK() != null && xmlSmet.getNrsp().getSPK() != 0) smrCosts.put("SPK", xmlSmet.getNrsp().getSPK());
            else if (vr.getPlanK() != null) smrCosts.put("SPK", vr.getPlanK());
            else if (smrElement.getAttribute("PNB") != null) smrCosts.put("SPK", Double.parseDouble(smrElement.getAttributeValue("PNB").replace(",",".")));
            applyNorm("SP");
        }
    }

    //Парсинг ресурсов
    private SmetResource parseResource(Element rsrcElement) {

        SmetResource rsrc = null;
        String name = rsrcElement.getName();

        if (rsrcElement.getName().equals("Replaced")) name = rsrcElement.getParentElement().getName();

        switch (name) {
            case "Tzr":
                if (resourceCollection.containsRes("T")) {
                    rsrc = resourceCollection.get("T");
                } else {
                    rsrc = new SmetResource("Затраты труда рабочих", "T", "T", "чел.час");
                }
                break;
            case "Tzm":
                if (resourceCollection.containsRes("Tzm")) {
                    rsrc = resourceCollection.get("Tzm");
                } else {
                    rsrc = new SmetResource("Затраты труда машинистов", "Tzm", "Tzm", "чел.час");
                }
                break;
            case "Mch": {

                String rsrcCode = rsrcElement.getAttributeValue("Code");
                if (rsrcCode == null) break;

                if (resourceCollection.containsRes(rsrcCode)) {
                    rsrc = resourceCollection.get(rsrcCode);
                } else {
                    rsrc = new SmetResource(rsrcElement.getAttributeValue("Caption"), rsrcCode,"Mech","маш.-ч");
                }
                break;
            }
            case "Mat": {
                String rsrcCode = rsrcElement.getAttributeValue("Code");
                if (rsrcCode == null) break;

                if (resourceCollection.containsRes(rsrcCode)) {
                    rsrc = resourceCollection.get(rsrcCode);
                } else {
                    rsrc = new SmetResource(rsrcElement.getAttributeValue("Caption"), rsrcCode, "Mat", rsrcElement.getAttributeValue("Units"));
                }
                break;
            }
        }
        return rsrc;
    }

    private boolean isApplyK(XMLCoefficient k) {
        boolean apply = false;

        if ((k.isAllChapters() && k.isAllVidRabs())) {
            apply = true;
        } else if (!k.isAllChapters()
                && k.isAllVidRabs()
                && k.getChaptersLink().contains(chapterNode.getSysId())) {
            apply = true;
        } else if (!k.isAllVidRabs()
                && k.isAllChapters()
                && smrElement.getAttribute("Vr2001")!=null
                && k.getVrsLink().contains(smrElement.getAttributeValue("Vr2001"))) {
            apply = true;
        } else if (!k.isAllChapters()
                && k.getChaptersLink() != null
                && k.getChaptersLink().contains(chapterNode.getSysId())
                && !k.isAllVidRabs()
                && smrElement.getAttribute("Vr2001")!=null
                && k.getVrsLink().contains(smrElement.getAttributeValue("Vr2001"))) {
            apply = true;
        }
        return apply;
    }

    private boolean isFormulaOk(String formula, String value) {
        Interpreter interpreter = new Interpreter();
        try {
            interpreter.eval(formula);
            Double.parseDouble(interpreter.get(value).toString());
            return true;
        } catch (EvalError evalError) {
            return false;
        }
    }

    //Применение группового коэффициента
    private void applyGroupK(XMLCoefficient koeff, HashMap<String, Double> smrValues) {
        //Применение коэффициента ПЗ
        if (koeff.getPz() != null && smrValues.containsKey("PZ")) {
            levSum.put("PZ", ((levSum.containsKey("PZ")) ? levSum.get("PZ") : 0) + smrValues.get("PZ")  * (koeff.getPz() - 1));
        }

        //Применение коэффициента ОЗ
        if (koeff.getOz() != null || (koeff.isPzAll() && koeff.getPz() != null)) {

            double k = 1;
            if (koeff.getOz() != null) k = koeff.getOz();
            if (koeff.isPzAll() && koeff.getPz() != null) k = k * koeff.getPz();

            if (smrValues.containsKey("OZ")) {
                Double calc = smrValues.get("OZ") * (k - 1);
                levSum.put("OZ", ((levSum.containsKey("OZ")) ? levSum.get("OZ") : 0) + calc);
                if (koeff.getPz() == null || (koeff.isPzAll() && !smrValues.containsKey("PZ"))) {
                    levSum.put("PZ", ((levSum.containsKey("PZ")) ? levSum.get("PZ") : 0) + calc);
                }
                if (koeff.getPz() != null  && koeff.getOz() != null && koeff.isPzAll() && levSum.containsKey("PZ")) {
                    levSum.put("PZ", levSum.get("PZ") + calc - smrValues.get("OZ")*(koeff.getPz() - 1));
                }
            }
            if (koeff.isOzpTz() && smrValues.containsKey("TZR")) levSum.put("TZR", smrValues.get("TZR") * (k - 1));

            for (SmetResourceAssignment ra : resourceAssignments) {
                if (!ra.getType().equals("T")) continue;
                double raCalc = (ra.getCosts().containsKey("OZ") ? ra.getCosts().get("OZ") : 0) * (k - 1);
                ra.getLevelSum().put("OZ", (ra.getLevelSum().containsKey("OZ") ? ra.getLevelSum().get("OZ") : 0) + raCalc);
                if (koeff.isOzpTz()) {
                    double tzCalc = (ra.getCosts().containsKey("TZR") ? ra.getCosts().get("TZR") : 0) * (k - 1);
                    ra.getLevelSum().put("TZR", (ra.getLevelSum().containsKey("TZR") ? ra.getLevelSum().get("TZR") : 0) + tzCalc);
                }
            }
        }

        //Применение коэффициента ЭМ
        if (koeff.getEm() != null || (koeff.isPzAll() && koeff.getPz() != null)) {

            double k = 1;
            if (koeff.getEm() != null) k = koeff.getEm();
            if (koeff.isPzAll() && koeff.getPz() != null) k = k * koeff.getPz();

            if (smrValues.containsKey("EM")) {
                Double calc;
                if (koeff.isSeparEm() && !koeff.isEmAll() && smrValues.containsKey("ZM")){
                    if (koeff.getEm() != null && koeff.getZm() != null && koeff.isSeparEm()) {
                        calc = smrValues.get("EM")*(k - 1);
                    } else {
                        calc = (smrValues.get("EM") - smrValues.get("ZM"))*(k - 1);
                    }
                } else {
                    calc = smrValues.get("EM")*(k - 1);
                }

                levSum.put("EM", ((levSum.containsKey("EM")) ? levSum.get("EM") : 0) + calc);

                if (koeff.getPz() == null || (koeff.isPzAll() && !smrValues.containsKey("PZ"))) {
                    levSum.put("PZ", ((levSum.containsKey("PZ")) ? levSum.get("PZ") : 0) + calc);
                }
            }

            for (SmetResourceAssignment ra : resourceAssignments) {
                if (!ra.getType().equals("Mech")) continue;
                Double calc;
                if (koeff.isSeparEm()){
                    calc = ((ra.getCosts().containsKey("EM") ? ra.getCosts().get("EM") : 0) - (ra.getCosts()
                            .containsKey("ZM") ? ra.getCosts().get("ZM") : 0))*(k - 1);
                } else {
                    calc = (ra.getCosts().containsKey("EM") ? ra.getCosts().get("EM") : 0)*(k - 1);
                }
                ra.getCosts().put("EM", ((ra.getCosts().containsKey("EM")) ? ra.getCosts().get("EM") : 0) + calc);
            }
        }

        //Применение коэффициента ЗМ
        if (koeff.getZm() != null || (koeff.isEmAll() && (koeff.getEm() != null || (koeff.isPzAll() && koeff.getPz() != null)))) {

            double k = 1;
            if (koeff.getZm() != null) k = koeff.getZm();
            if (koeff.isEmAll() && koeff.getEm() != null) k = k * koeff.getEm();
            if (koeff.isEmAll() && koeff.isPzAll() && koeff.getPz() != null) k = k * koeff.getPz();

            if (smrValues.containsKey("ZM")) {
                Double calc = smrValues.get("ZM")*(k-1);
                levSum.put("ZM", ((levSum.containsKey("ZM")) ? levSum.get("ZM") : 0) + calc);

                if ((koeff.getEm() == null && !koeff.isPzAll() )|| (koeff.isEmAll() && !smrValues.containsKey("EM"))
                        || (koeff.getEm() == null && koeff.getPz() == null && koeff.isPzAll())
                        || (koeff.getPz() == null && koeff.isPzAll() && koeff.getEm() != null && koeff.isSeparEm() && koeff.getZm() != null)) {
                    levSum.put("EM", ((levSum.containsKey("EM")) ? levSum.get("EM") : 0) + calc);
                    if (koeff.getPz() == null || (koeff.isPzAll() && !smrValues.containsKey("PZ"))) {
                        levSum.put("PZ", ((levSum.containsKey("PZ")) ? levSum.get("PZ") : 0) + calc);
                    }
                }
            }

            if (koeff.isZpmTz() && smrValues.containsKey("TZM")) {
                levSum.put("TZM", ((levSum.containsKey("TZM")) ? levSum.get("TZM") : 0) + smrValues.get("TZM") * (k - 1));
            }
            for (SmetResourceAssignment ra : resourceAssignments) {
                if (koeff.isZpmTz() && ra.getType().equals("Tzm")) {
                    double tzmCalc = (ra.getCosts().containsKey("TZM") ? ra.getCosts().get("TZM") : 0) * (k -1);
                    ra.getLevelSum().put("TZM", ((ra.getLevelSum().containsKey("TZM")) ? ra.getLevelSum().get("TZM") :
                            0) + tzmCalc);
                } else if (ra.getType().equals("Mech")) {
                    double mechCalc = ((ra.getCosts().containsKey("ZM")) ? ra.getCosts().get("ZM") : 0) * (k -1);
                    ra.getLevelSum().put("ZM", (ra.getLevelSum().containsKey("ZM") ? ra.getLevelSum().get("ZM") :
                            0) + mechCalc);
                }
            }
        }

        //Применение коэффициента МАТ
        if (koeff.getMt() != null && !smrNode.isEqui() || (koeff.isPzAll() && koeff.getPz() != null)) {

            double k = 1;
            if (koeff.getMt() != null) k = koeff.getMt();
            if (koeff.isPzAll() && koeff.getPz() != null) k = k * koeff.getPz();

            if (smrValues.containsKey("MT")) {
                Double calc = smrValues.get("MT")*(k - 1);
                levSum.put("MT", ((levSum.containsKey("MT")) ? levSum.get("MT") : 0) + calc);
                if (koeff.getPz() == null || (koeff.isPzAll() && !smrValues.containsKey("PZ"))) {
                    levSum.put("PZ", ((levSum.containsKey("PZ")) ? levSum.get("PZ") : 0) + calc);
                }
                if (koeff.getPz() != null && koeff.isPzAll() && levSum.containsKey("PZ")) {
                    levSum.put("PZ", levSum.get("PZ") + calc - smrValues.get("MT")*(koeff.getPz() - 1));
                }
            }
            for (SmetResourceAssignment ra : resourceAssignments) {
                if (!ra.getType().equals("Mat")) continue;
                double raCalc = (ra.getCosts().containsKey("MT") ? ra.getCosts().get("MT") : 0)
                        * (k - 1);
                ra.getLevelSum().put("MT", (ra.getLevelSum().containsKey("MT") ? ra.getLevelSum().get("MT") :
                        0) + raCalc);
                if (koeff.isMatQty()) ra.setQuantity(ra.getQuantity() * k);
            }
        }
    }

    private boolean isExcludedK(XMLCoefficient koeff) {
        Boolean excluded = false;
        if (smrElement.getChild("ExcludedKfs") != null
                && smrElement.getChild("ExcludedKfs").getAttribute("Value") != null) {

            String excludedKfs = smrElement.getChild("ExcludedKfs").getAttributeValue("Value").replace("(", "").replace(")", "").replace(" ", "");
            for (String k : excludedKfs.split(",")) {
                if (xmlSmet.getGlobalK().getCoeffList().get(Integer.parseInt(k)).equals(koeff)) excluded = true;
            }
        }
        return excluded;
    }

    private void applyNorm(String v) {
        Double sum = 0.0;
        Double commonK = 1.0;

        if (smrCosts.containsKey(v+"K")) commonK = smrCosts.get(v + "K");

        for (String a : smrMasks.get(v).split(",")) {
            if (a.equals("")) continue;
            sum += (smrValues.containsKey(a)) ? smrValues.get(a) : 0;
        }

        double k = new BigDecimal(String.valueOf(smrCosts.get(v) * commonK)).setScale(xmlSmet.getParameters().getRoundNPBase() ? 2 : 6, BigDecimal.ROUND_HALF_UP).doubleValue();
        Double calc = new BigDecimal(String.valueOf(sum * k)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        if (xmlSmet.getParameters().getRoundSum()) calc = new BigDecimal(String.valueOf(calc)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        smrValues.put(v, (smrValues.containsKey(v) ? smrValues.get(v) : 0) + calc);
        smrValues.put("SUM", ((smrValues.get("SUM") != null) ? smrValues.get("SUM") : 0) + calc);
    }
}
