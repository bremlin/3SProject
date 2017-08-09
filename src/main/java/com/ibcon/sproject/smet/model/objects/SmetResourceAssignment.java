package com.ibcon.sproject.smet.model.objects;

import bsh.EvalError;
import bsh.Interpreter;
import com.ibcon.sproject.smet.model.xml.XMLCoefficient;
import com.ibcon.sproject.smet.model.xml.XMLCoefficients;
import com.ibcon.sproject.smet.model.xml.XMLParameters;
import org.jdom2.Element;

import java.util.HashMap;

public class SmetResourceAssignment {

    private int id;
    private int resourceId;
    private int smrId;
    private int smetId;
    private int titulId;
    private String name;
    private String code;
    private String type;
    private String units;
    private String attrib = "";
    public HashMap<String, Double> raEdits;
    private Double quantity = 0.0;

    private Double em = 0.0;
    private Double zm = 0.0;
    private Double oz = 0.0;
    private Double mt = 0.0;
    private Double tzr = 0.0;
    private Double tzm = 0.0;

    private HashMap <String, Double> costs, levelSum;

    //region Конструктор для парсинга XML
    public SmetResourceAssignment(SmetResource rsrc, Element rsrcElement, XMLParameters params) {

        Double value = 0.0;

        if (rsrcElement.getAttribute("Quantity") != null ) quantity = Double.parseDouble(rsrcElement.getAttributeValue("Quantity").replace(",", "."));

        if (rsrcElement.getChild("PriceBase") != null
                && rsrcElement.getChild("PriceBase").getAttribute("Value")!= null
                && !rsrcElement.getChild("PriceBase").getAttributeValue("Value").equals(""))
            value = Double.parseDouble(rsrcElement.getChild("PriceBase").getAttributeValue("Value").replace(",", "."));

        if (rsrcElement.getChild("PriceBase") != null
                && rsrcElement.getChild("PriceBase").getAttribute("ZM") != null
                && !rsrcElement.getChild("PriceBase").getAttributeValue("ZM").equals(""))
            zm = Double.parseDouble(rsrcElement.getChild("PriceBase").getAttributeValue("ZM").replace(",","."));

        if (rsrcElement.getAttribute("Attribs") != null)
            attrib = rsrcElement.getAttributeValue("Attribs").replace("SnipSet", "").replace(" ", "");

        this.costs = new HashMap<>();

        switch (rsrc.getType()) {
            case "T":
                this.oz = value * quantity;
                this.costs.put("OZ", this.oz);
                this.costs.put("TZR", quantity);
                break;

            case "Tzm":
                this.costs.put("TZM", quantity);
                this.zm = value * quantity;
                this.costs.put("ZM", this.zm);
                break;

            case "Mech":
                this.em = value * quantity;
                this.zm = zm * quantity;
                this.costs.put("EM", this.em);
                this.costs.put("ZM", this.zm);
                this.costs.put("EMR", this.em - this.zm);
                break;

            case "Mat":
                this.mt = value * quantity;
                this.costs.put("MT", this.mt);
                break;

            default:
                return;
        }

        this.resourceId = rsrc.getId();
        this.name = rsrc.getName();
        this.code = rsrc.getCode();
        this.type = rsrc.getType();
        this.units = rsrc.getUnits();
    }

    //region Применение коэфф-та при парсинге XML
    public void applyCoeff(XMLCoefficients coeffs) {

        if (coeffs == null) return;
        raEdits = new HashMap<>();

        for (int lev : coeffs.getLevels()) {

            HashMap<String, Double> levSum = new HashMap<>();
            for (XMLCoefficient coeff : coeffs.getCoeffs().get(lev)) {

                //region Расчет показателей по формулам
                if (coeff.getKind().equals("Formula")) {

                    Interpreter interpreter = new Interpreter();
                    Double eval = 0.0;

                    String rawFormula = coeff.getFormula().substring(coeff.getFormula().indexOf("=")+1);

                    if (costs.keySet().contains("PZ") && rawFormula.contains("ПЗ")) rawFormula = rawFormula.replace("ПЗ", costs.get("PZ").toString());
                    if (costs.keySet().contains("OZ") && rawFormula.contains("ОЗП")) rawFormula = rawFormula.replace("ОЗП", costs.get("OZ").toString());
                    if (costs.keySet().contains("EM") && rawFormula.contains("ЭМ")) rawFormula = rawFormula.replace("ЭМ", costs.get("EM").toString());
                    if (costs.keySet().contains("MT") && rawFormula.contains("МАТ")) rawFormula = rawFormula.replace("МАТ", costs.get("MT").toString());

                    String value = coeff.getFormulaValue();
                    String formula = value + "=" + rawFormula;

                    try {
                        interpreter.eval(formula);
                        eval = Double.parseDouble(interpreter.get(value).toString());
                    } catch (EvalError evalError) {
                        evalError.printStackTrace();
                    }

                    value = value.replace("ПЗ", "PZ").replace("ОЗП", "OZ").replace("ЭМ", "EM").replace("ЗПМ", "ZM").replace("МАТ", "MT");

                    levSum.put(value, (levSum.containsKey(value) ? levSum.get(value) : 0) + eval);
                    if (value.equals("ZM")) levSum.put("EM", (levSum.containsKey("EM") ? levSum.get("EM") : 0) + eval);
                    if (!value.equals("PZ")) levSum.put("PZ", (levSum.containsKey("PZ") ? levSum.get("PZ"): 0) + eval);

                    continue;

                }
                //endregion

                //region Расчет по коэффициентам
                for (String key : coeff.getValues().keySet()) {

                    if (!costs.containsKey(key)) {
                        System.out.println("Ошибка расчет коэффициента для ресурса: " + key );
                    } else {

                        Double calc = costs.get(key) * (coeff.getValues().get(key) - 1);

                        if (key.equals("EM") && coeff.isSeparEm())
                            calc = (costs.get("EM") - costs.get("ZM") * (coeff.getValues().get("EM")-1));

                        levSum.put(key, (levSum.containsKey(key) ? levSum.get(key) : 0) + calc);

                        if (key.equals("ZM")
                                && (!coeff.getValues().keySet().contains("EM") || coeff.isSeparEm()))
                            levSum.put("EM", (levSum.containsKey("EM") ? levSum.get("EM") : 0) + calc);
                    }

                }
                //endregion

            }

            //region Суммирование показателей по уровням
            for (String key : levSum.keySet()) {
                raEdits.put(key, (raEdits.containsKey(key) ? raEdits.get(key) : 0) + levSum.get(key));
                costs.put(key, (costs.containsKey(key) ? costs.get(key) : 0) + levSum.get(key));
            }
            //endregion

        }

        costs.put("EMR",
                (costs.containsKey("EM") ? costs.get("EM") : 0) - (costs.containsKey("ZM") ? costs.get("ZM") : 0));

    }

    public int getId() {
        return id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getSmrId() {
        return smrId;
    }

    public int getSmetId() {
        return smetId;
    }

    public int getTitulId() {
        return titulId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getUnits() {
        return units;
    }

    public String getAttrib() {
        return attrib;
    }

    public HashMap<String, Double> getRaEdits() {
        return raEdits;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getEm() {
        return em;
    }

    public Double getZm() {
        return zm;
    }

    public Double getOz() {
        return oz;
    }

    public Double getMt() {
        return mt;
    }

    public Double getTzr() {
        return tzr;
    }

    public Double getTzm() {
        return tzm;
    }

    public HashMap<String, Double> getCosts() {
        return costs;
    }

    public HashMap<String, Double> getLevelSum() {
        return levelSum;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setEm(Double em) {
        this.em = em;
    }

    public void setZm(Double zm) {
        this.zm = zm;
    }

    public void setOz(Double oz) {
        this.oz = oz;
    }

    public void setMt(Double mt) {
        this.mt = mt;
    }

    public void setTzr(Double tzr) {
        this.tzr = tzr;
    }

    public void setTzm(Double tzm) {
        this.tzm = tzm;
    }

    public void setLevelSum(HashMap<String, Double> levelSum) {
        this.levelSum = levelSum;
    }
}
