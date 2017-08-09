package com.ibcon.sproject.smet.model.xml;

import com.ibcon.sproject.smet.model.objects.Norm;
import com.ibcon.sproject.smet.model.objects.smr.SmrMask;
import org.jdom2.Element;

import java.util.HashMap;

public class NRSP {

    private boolean globalSP, globalNR;
    private Norm globalNorm;
    private HashMap<String, Norm> norms;
    private double NRK;
    private double SPK;

    public NRSP(Element parameters) {
        if (parameters.getChild("CommonNK") != null
                && parameters.getChild("CommonNK").getAttributeValue("CommonK") != null
                && parameters.getChild("CommonNK").getAttributeValue("ActiveItems") != null
                && parameters.getChild("CommonNK").getAttributeValue("ActiveItems").contains("Ck"))
            this.NRK = Double.parseDouble(parameters.getChild("CommonNK").getAttributeValue
                    ("CommonK").replace(",", "."));

        if (parameters.getChild("CommonNK") != null
                && parameters.getChild("CommonNK").getAttributeValue("MethodKB") != null
                && parameters.getChild("CommonNK").getAttributeValue("ActiveItems") != null
                && parameters.getChild("CommonNK").getAttributeValue("ActiveItems").contains("Mk"))
            this.NRK = Double.parseDouble(parameters.getChild("CommonNK").getAttributeValue("MethodKB").replace(",", "."));


        if (parameters.getChild("CommonPK") != null
                && parameters.getChild("CommonPK").getAttributeValue("CommonK") != null
                && parameters.getChild("CommonPK").getAttributeValue("ActiveItems") != null
                && parameters.getChild("CommonPK").getAttributeValue("ActiveItems").contains("Ck"))
            this.SPK = Double.parseDouble(parameters.getChild("CommonPK").getAttributeValue("CommonK").replace(",", "."));

        if (parameters.getChild("CommonPK") != null
                && parameters.getChild("CommonPK").getAttributeValue("MethodKB") != null
                && parameters.getChild("CommonPK").getAttributeValue("ActiveItems") != null
                && parameters.getChild("CommonPK").getAttributeValue("ActiveItems").contains("Mk"))
            this.SPK = Double.parseDouble(parameters.getChild("CommonPK").getAttributeValue
                    ("MethodKB").replace(",", "."));

        //Расчет НР по общему нормативу
        if (globalNR) {
            this.globalNorm = new Norm("Общий норматив", Double.parseDouble(parameters.getChild("CommonNPCurr")
                    .getAttributeValue("Nacl").replace(",", ".")) / 100,
                    SmrMask.getMask(parameters.getChild("CommonNPCurr").getAttribute("NaclMask")));
        }

        //Расчет СП по общему нормативу
        if (globalSP) {
            if (globalNorm == null) globalNorm = new Norm("Общий норматив");
            globalNorm.setSp(Double.parseDouble(parameters.getChild("CommonNPCurr")
                    .getAttributeValue("Plan").replace(",", ".")) / 100);
            globalNorm.setSpMask(SmrMask.getMask(parameters.getChild("CommonNPCurr").getAttribute("PlanMask")));
        }
    }

    public boolean isGlobalSP() {
        return globalSP;
    }

    public boolean isGlobalNR() {
        return globalNR;
    }

    public Norm getGlobalNorm() {
        return globalNorm;
    }

    public HashMap<String, Norm> getNorms() {
        return norms;
    }

    public Double getNRK() {
        return NRK;
    }

    public Double getSPK() {
        return SPK;
    }
}
