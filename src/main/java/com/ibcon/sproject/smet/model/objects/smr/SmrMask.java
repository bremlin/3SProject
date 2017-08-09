package com.ibcon.sproject.smet.model.objects.smr;

import org.jdom2.Attribute;

import java.util.HashSet;

public class SmrMask {

    public static String getMask(Attribute attr) {
        HashSet<String> maskHash = new HashSet<>();
        String mask = "";
        String a = attr.getValue();

        if (a.contains("ПЗ")) {
            maskHash.add("OZ");
            maskHash.add("ZM");
            maskHash.add("EM");
            maskHash.add("MT");
        }

        if (a.contains("ЭМ")) {
            maskHash.add("ZM");
            maskHash.add("EM");
        }

        if (a.contains("ФОТ")) {
            maskHash.add("OZ");
            maskHash.add("ZM");
        }

        if (a.contains("ОЗП")) maskHash.add("OZ");

        if (a.contains("ЗПМ")) maskHash.add("ZM");

        if (a.contains("МАТ")) maskHash.add("MT");

        for (String k : maskHash) {
            mask = mask + (!mask.equals("") ? "," : "" )+ k;
        }

        return mask;
    }
}
