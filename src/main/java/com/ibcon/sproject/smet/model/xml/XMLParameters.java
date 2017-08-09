package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class XMLParameters {

    private Boolean roundSum = false;

    private boolean tempRound;                      //Промежуточное округление до копеек при расчете единичной стоимости позиций
    private Boolean roundNPBase = false;
    private Boolean addZatr = false;
    private Boolean globalNR = false;
    private Boolean globalSP = false;
    private String basePrices, baseCalcVrs;
    private int matDigits, tzDigits, posKDigits;

    public XMLParameters(Element param) {

        basePrices = param.getAttributeValue("BasePrices");
        baseCalcVrs = param.getAttributeValue("BaseCalcVrs");

        Attribute attrOptions = param.getAttribute("Options");

        if (attrOptions != null) {
            roundSum = attrOptions.getValue().contains("RoundPos");
            tempRound = attrOptions.getValue().contains("TempRound");
            roundNPBase = attrOptions.getValue().contains("RoundNPBase");
            addZatr = attrOptions.getValue().contains("AddZatr");
            globalNR = attrOptions.getValue().contains("CommonNakl");
            globalSP = attrOptions.getValue().contains("CommonPlan");
            if (param.getAttribute("TzDigits") != null) tzDigits = Integer.valueOf(param.getAttributeValue("TzDigits"));
            if (param.getAttribute("MatDigits") != null) matDigits = Integer.valueOf(param.getAttributeValue("MatDigits"));
        }

        if (param.getAttribute("PosKDigits") != null) posKDigits = Integer.valueOf(param.getAttributeValue("PosKDigits"));
    }

    public Boolean getRoundSum() {
        return roundSum;
    }

    public boolean isTempRound() {
        return tempRound;
    }

    public Boolean getRoundNPBase() {
        return roundNPBase;
    }

    public Boolean getAddZatr() {
        return addZatr;
    }

    public Boolean getGlobalNR() {
        return globalNR;
    }

    public Boolean getGlobalSP() {
        return globalSP;
    }

    public String getBasePrices() {
        return basePrices;
    }

    public String getBaseCalcVrs() {
        return baseCalcVrs;
    }

    public int getMatDigits() {
        return matDigits;
    }

    public int getTzDigits() {
        return tzDigits;
    }

    public int getPosKDigits() {
        return posKDigits;
    }
}
