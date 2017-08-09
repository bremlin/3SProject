package com.ibcon.sproject.smet.model.xml;

import bsh.EvalError;
import bsh.Interpreter;
import com.ibcon.sproject.smet.model.objects.ChapterNode;
import com.ibcon.sproject.smet.model.objects.SmetNode;
import com.ibcon.sproject.smet.model.objects.SmetTreeNode;
import com.ibcon.sproject.smet.model.objects.SmrNode;
import com.ibcon.sproject.smet.model.utilits.CheckValue;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by s_shmakov on 07.07.2017.
 */
public class XmlSmet {
    private String name;
    private Element rootElement;
    private XMLRegionInfo regionInfo;
    private XMLParameters parameters;
    private XMLIndexes indexes;
    private NRSP nrsp;
    private XMLVidRabGroups vrgs;

    private double regionalK;
    private XMLCoefficient terZoneK;
    private XMLCoefficients globalK;

    private SmetNode smet;

    private int chapNum = 0;

    private HashMap<String, Double> identifierValues;

    private SmrNode smrNode;

    public XmlSmet(File file, int epsId) throws JDOMException, IOException {

        SAXBuilder builder = new SAXBuilder();
        Document xmlDocument = builder.build(file);
        rootElement = xmlDocument.getRootElement();

        getHeader(epsId);
        getNRSPCalc();
        getGroupCoeff();
        getStructure();
        System.out.println("xmlSmet");
    }

    private void getHeader(int epsId) {
        if (rootElement.getChild("RegionInfo") != null) {
            this.regionInfo = new XMLRegionInfo(rootElement.getChild("RegionInfo"));
        }
        if (rootElement.getChild("Parameters") != null) {
            this.parameters = new XMLParameters(rootElement.getChild("Parameters"));
        }
        if (rootElement.getChild("Indexes") != null) {
            this.indexes = new XMLIndexes(rootElement.getChild("Indexes"));
        }

        smet = new SmetNode(name, rootElement, regionInfo, parameters, epsId);
    }

    private void getNRSPCalc() {
        this.nrsp = new NRSP(rootElement.getChild("Parameters"));

        if (rootElement.getChild("VidRab_Catalog") != null &&
                rootElement.getChild("VidRab_Catalog").getChild("Vids_Rab") != null &&
                rootElement.getChild("VidRab_Catalog").getChild("Vids_Rab").getChildren().size() > 0) {
            this.vrgs = new XMLVidRabGroups(rootElement);
        } else {
            System.out.println("nrsp error");
        }
    }

    private void getGroupCoeff() {
        if (rootElement.getChild("RegionalK") != null
                && rootElement.getChild("RegionalK").getAttribute("Value") != null ) this.regionalK = Double.parseDouble
                (rootElement.getChild("RegionalK").getAttributeValue("Value").replace(",", "."));
        else this.regionalK = 1.0;

        if (rootElement.getChild("TerZoneK") != null &&
                (rootElement.getChild("TerZoneK").getAttribute("Value_OZ") != null
                        || rootElement.getChild("TerZoneK").getAttribute("Value_EM") != null
                        || rootElement.getChild("TerZoneK").getAttribute("Value_ZM") != null
                        || rootElement.getChild("TerZoneK").getAttribute("Value_MT") != null
                        || rootElement.getChild("TerZoneK").getAttribute("Value_PZ") != null) ) terZoneK = new XMLCoefficient(rootElement.getChild("TerZoneK"));

        if (rootElement.getChild("Koefficients") != null
                && !rootElement.getChild("Koefficients").getChildren().isEmpty())
            globalK = new XMLCoefficients(rootElement.getChild("Koefficients"));
    }

    private void getStructure() {
        if (rootElement.getChild("Chapters") == null || rootElement.getChild("Chapters").getChildren().size() < 1) {
            System.out.println("XmlSmet-getStructure-error");
            return;
        }

        parseIdentifierValues();

        for (Element chapterElement: rootElement.getChild("Chapters").getChildren()) {
            ChapterNode curChapter = new ChapterNode(chapterElement, chapNum++);
            smet.add(curChapter);

            HeaderNode curHeader;
            SmetTreeNode parent = curChapter;

            for (Element child : chapterElement.getChildren()) {
                if (child.getName().equals("Header")) {
                    curHeader = new HeaderNode(child.getAttributeValue("Caption"));
                    curChapter.add(curHeader);
                    parent = curHeader;
                } else if (child.getName().equals("Position")) {
                    if (child.getAttribute("Number") != null && !child.getAttributeValue("Number").equals("")) {
                        smrNode = new SmrNode(child, vrgs);
                        new XMLSmr(this, child, curChapter);
                        if (parent.getChildren() == null || parent.getChildren().isEmpty()) {
                            //todo а вот эту сомнительную вещь вообще проверить нужно
//                            parent.add(MainFrame.ALL_SMET.get("smrhead"));
                        }
                        parent.add(smrNode);
                        smet.addSmetRow(smrNode);
                    }
                }
            }
        }
    }

    private void parseIdentifierValues() {
        identifierValues = new HashMap<>();

        for (Element chapterElement: rootElement.getChild("Chapters").getChildren()) {
            for (Element child : chapterElement.getChildren()) {
                if (child.getName().equals("Position") && child.getAttribute("Identifier") != null
                        && !child.getAttributeValue("Identifier").equals("")) {
                    parseIdentifierQuantity(child);
                }
            }
        }
    }

    private void parseIdentifierQuantity(Element smrElement) {
        double quantity = 0.0;
        if (smrElement.getChild("Quantity") != null
                && smrElement.getChild("Quantity").getAttribute("Result") != null)
            quantity = Double.parseDouble(smrElement.getChild("Quantity").getAttributeValue("Result").replace(",", "."));
        else if (smrElement.getAttribute("Quantity") != null) {
            if (CheckValue.isDouble(smrElement.getAttributeValue("Quantity").replace(",", "."))) {
                quantity = Double.parseDouble(smrElement.getAttributeValue("Quantity").replace(",", "."));
            } else {
                try {
                    Interpreter interpreter = new Interpreter();
                    String stringQuantity = smrElement.getAttributeValue("Quantity").replace(",", ".");
                    int rounding = -1;

                    if (stringQuantity.contains("окр(")) {
                        stringQuantity = stringQuantity.replace("окр(", "");
                        rounding = Integer.valueOf(stringQuantity.substring(stringQuantity.indexOf(";")+1));
                        stringQuantity = stringQuantity.substring(0, stringQuantity.indexOf(";"));
                    }
                    interpreter.eval("quantity=" + "0.1*10*" + stringQuantity);
                    BigDecimal bdQuantity = new BigDecimal(interpreter.get("quantity").toString());
                    if (rounding > -1) bdQuantity = bdQuantity.setScale(rounding, BigDecimal.ROUND_HALF_UP);
                    quantity = bdQuantity.doubleValue();
                } catch (EvalError evalError) {
                    System.out.println("bad Formula 1: " + smrElement.getAttributeValue("Quantity").replace(",", "."));
                }
            }
        }
        identifierValues.put(smrElement.getAttributeValue("Identifier").toLowerCase(), quantity);
    }

    public String getName() {
        return name;
    }

    public Element getRootElement() {
        return rootElement;
    }

    public XMLRegionInfo getRegionInfo() {
        return regionInfo;
    }

    public XMLParameters getParameters() {
        return parameters;
    }

    public XMLIndexes getIndexes() {
        return indexes;
    }

    public NRSP getNrsp() {
        return nrsp;
    }

    public XMLVidRabGroups getVrgs() {
        return vrgs;
    }

    public double getRegionalK() {
        return regionalK;
    }

    public XMLCoefficient getTerZoneK() {
        return terZoneK;
    }

    public XMLCoefficients getGlobalK() {
        return globalK;
    }

    public SmetNode getSmet() {
        return smet;
    }

    public int getChapNum() {
        return chapNum;
    }

    public HashMap<String, Double> getIdentifierValues() {
        return identifierValues;
    }

    public SmrNode getSmrNode() {
        return smrNode;
    }


}
