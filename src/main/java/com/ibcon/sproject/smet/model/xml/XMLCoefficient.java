package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class XMLCoefficient {

    private boolean allVidRabs = true;
    private boolean allChapters = true;

    private ArrayList<String> vrsLink = new ArrayList<>();
    private ArrayList<String> chaptersLink = new ArrayList<>();

    private String formula;
    private String formulaValue;

    private boolean disabled;

    private boolean PzAll;      //Распространять коэффициент к прямым затратам на все статьи
    private boolean EmAll;      //Распространять коэффициент к экспл. машин на зарплату машинистов
    private boolean SeparEm;    //Начислять коэффициент к чистой ЭМ в виде К*(ЭМ-ЗПМ)
    private boolean EmQty;      //Применять коэффициент не только к стоимости, но и к расходу машин и механизмов
    private boolean MatQty;     //Применять коэффициент не только к стоимости, но и к расходу материалов
    private boolean OzpTz;      //Применять коэффициент не только к зарплате, но и к трудозатратам рабочих
    private boolean ZpmTz;      //Применять коэффициент не только к зарплате машинистов, но и к трудозатратам машинистов

    private boolean inPos;      //Признак применения коэф-та в позиции сметы
    private String kind = "Multiply";

    private Double pz;
    private Double oz;
    private Double em;
    private Double zm;
    private Double mt;

    HashMap<String, Double> values = new HashMap<>();

    /** Конструктор для глобальных коэффициентов*/
    public XMLCoefficient(Element k, Element coeffsElement) {
        this.chaptersLink = new ArrayList<>();

        //Разбор параметров коэффициента
        if (k.getAttribute("Options") != null) {
            String opt = k.getAttributeValue("Options");
            inPos = opt.contains("InPos");
            disabled = opt.contains("Disabled");

            PzAll = opt.contains("PzAll");
            EmAll = opt.contains("EmAll");
            OzpTz = opt.contains("OzpTz");
            ZpmTz = opt.contains("ZpmTz");
            SeparEm = opt.contains("SeparEm");
            EmQty = opt.contains("EmQty");
            MatQty = opt.contains("MatQty");
        }

        //Разбор коэффициента с формулой
        if (k.getAttribute("Kind") != null
                && k.getAttributeValue("Kind").equals("Formula")
                && k.getAttribute("Formula") != null
                && !k.getAttributeValue("Formula").equals("")) {

            String formula = k.getAttributeValue("Formula").replace(" ", "").replace(",", ".");
            String value = (formula.contains("=")) ? formula.substring(0, formula.indexOf("=")) : formula;
            kind = "Formula";
            this.formula = formula;
            this.formulaValue = value;
        }

        //Коэффициент с заданными видами работ
        if (k.getAttribute("AllVidRabs") != null && k.getAttributeValue("AllVidRabs").equals("No")) {
            this.allVidRabs = false;
            this.vrsLink = new ArrayList<>();

            //Ссылки по видам работ
            if (k.getAttribute("VrsLinks") != null) {
                ArrayList<String> links = new ArrayList<>(Arrays.asList(k.getAttributeValue("VrsLinks").replace("(", "").replace(")", "").replace(" ", "").split(",")));
                for (String link : links) {
                    this.vrsLink.add(link);
                }
            }

            //Ссылки по группам видов работ
            if (k.getAttribute("VrGroupsLinks") != null) {
                ArrayList<String> VrGroups = new ArrayList<>(Arrays.asList(k.getAttributeValue("VrGroupsLinks").replace("(", "").replace(")", "").replace(" ", "").split(",")));
                Element VidRabCatalog = coeffsElement.getParentElement().getChild("VidRab_Catalog");

                for (Element catChild : VidRabCatalog.getChildren()) {
                    if (catChild.getAttribute("Type") == null || !catChild.getAttributeValue("Type").equals("Виды работ 2001г")) continue;

                    for (Element XMLVidRabGroup : catChild.getChildren()) {
                        if (XMLVidRabGroup != null && XMLVidRabGroup.getAttribute("ID") != null && VrGroups.contains(XMLVidRabGroup.getAttributeValue("ID"))) {

                            for (Element XMLVidRab : XMLVidRabGroup.getChildren()) {
                                if (XMLVidRab.getAttribute("ID") != null)
                                    this.vrsLink.add(XMLVidRab.getAttributeValue("ID"));
                            }
                        }
                    }
                }
            }
        }

        //region Коэффициент с заданными разделами
        if (k.getAttribute("AllChapters") != null && k.getAttributeValue("AllChapters").equals("No")) {
            allChapters = false;
            chaptersLink = new ArrayList<>();

            if (k.getAttribute("ChaptersLinks") != null) {
                String[] links = k.getAttributeValue("ChaptersLinks").replace("(", "").replace(")", "").replace(" ", "").split(",");
                Collections.addAll(chaptersLink, links);
            }
        }
        //endregion

        if (k.getAttribute("Value_PZ") != null && !k.getAttributeValue("Value_PZ").equals("")) {
            this.pz = Double.parseDouble(k.getAttributeValue("Value_PZ").replace(",", "."));
        }

        if (k.getAttribute("Value_EM") != null && !k.getAttributeValue("Value_EM").equals("")) {
            this.em = Double.parseDouble(k.getAttributeValue("Value_EM").replace(",", "."));
        }

        if (k.getAttribute("Value_OZ") != null && !k.getAttributeValue("Value_OZ").equals("")) {
            this.oz = Double.parseDouble(k.getAttributeValue("Value_OZ").replace(",", "."));
        }

        if (k.getAttribute("Value_MT") != null && !k.getAttributeValue("Value_MT").equals("")) {
            this.mt = Double.parseDouble(k.getAttributeValue("Value_MT").replace(",", "."));
        }

        if (k.getAttribute("Value_ZM") != null && !k.getAttributeValue("Value_ZM").equals("")) {
            this.zm = Double.parseDouble(k.getAttributeValue("Value_ZM").replace(",", "."));
        }
    }

    /** Конструктор для локальных коэффициентов*/
    public XMLCoefficient(Element k) {
        //region Разбор параметров коэффициента
        if (k.getAttribute("Options") != null) {
            String opt = k.getAttributeValue("Options");
            inPos = opt.contains("InPos");
            disabled = opt.contains("Disabled");

            PzAll = opt.contains("PzAll");
            EmAll = opt.contains("EmAll");
            OzpTz = opt.contains("OzpTz");
            ZpmTz = opt.contains("ZpmTz");
            SeparEm = opt.contains("SeparEm");
            EmQty = opt.contains("EmQty");
            MatQty = opt.contains("MatQty");
        }
        //endregion

        if (k.getAttribute("Value_PZ") != null && !k.getAttributeValue("Value_PZ").equals("")) {
            this.pz = Double.parseDouble(k.getAttributeValue("Value_PZ").replace(",", "."));
        }

        if (k.getAttribute("Value_EM") != null && !k.getAttributeValue("Value_EM").equals("")) {
            this.em = Double.parseDouble(k.getAttributeValue("Value_EM").replace(",", "."));
        }

        if (k.getAttribute("Value_OZ") != null && !k.getAttributeValue("Value_OZ").equals("")) {
            this.oz = Double.parseDouble(k.getAttributeValue("Value_OZ").replace(",", "."));
        }

        if (k.getAttribute("Value_MT") != null && !k.getAttributeValue("Value_MT").equals("")) {
            this.mt = Double.parseDouble(k.getAttributeValue("Value_MT").replace(",", "."));
        }

        if (k.getAttribute("Value_ZM") != null && !k.getAttributeValue("Value_ZM").equals("")) {
            this.zm = Double.parseDouble(k.getAttributeValue("Value_ZM").replace(",", "."));
        }
    }

    public boolean isAllVidRabs() {
        return allVidRabs;
    }

    public boolean isAllChapters() {
        return allChapters;
    }

    public ArrayList<String> getVrsLink() {
        return vrsLink;
    }

    public ArrayList<String> getChaptersLink() {
        return chaptersLink;
    }

    public String getFormula() {
        return formula;
    }

    public String getFormulaValue() {
        return formulaValue;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isPzAll() {
        return PzAll;
    }

    public boolean isEmAll() {
        return EmAll;
    }

    public boolean isSeparEm() {
        return SeparEm;
    }

    public boolean isEmQty() {
        return EmQty;
    }

    public boolean isMatQty() {
        return MatQty;
    }

    public boolean isOzpTz() {
        return OzpTz;
    }

    public boolean isZpmTz() {
        return ZpmTz;
    }

    public boolean isInPos() {
        return inPos;
    }

    public String getKind() {
        return kind;
    }

    public Double getPz() {
        return pz;
    }

    public Double getOz() {
        return oz;
    }

    public Double getEm() {
        return em;
    }

    public Double getZm() {
        return zm;
    }

    public Double getMt() {
        return mt;
    }

    public HashMap<String, Double> getValues() {
        return values;
    }
}
