package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class XMLCoefficients {

    private ArrayList<Integer> levels = new ArrayList<>();
    private ArrayList<XMLCoefficient> coeffList = new ArrayList<>();
    private HashMap<Integer, ArrayList<XMLCoefficient>> coeffs = new HashMap<>();

    public XMLCoefficients() {
    }

    public XMLCoefficients(Element coefficients) {
        for (Element k : coefficients.getChildren()) {

            if (k.getAttribute("Options") == null
                    || (k.getAttribute("Options") != null &&
                    !k.getAttributeValue("Options").contains("Base"))) continue;

            int lev = 0;
            if (k.getAttribute("Level") != null) lev = Integer.valueOf(k.getAttributeValue("Level"));
            if (!levels.contains(lev)) levels.add(lev);

            ArrayList<XMLCoefficient> ks;
            if (!coeffs.containsKey(lev)) {
                ks = new ArrayList<>();
                coeffs.put(lev, ks);
            } else {
                ks = coeffs.get(lev);
            }

            XMLCoefficient coeff = new XMLCoefficient(k, coefficients);
            ks.add(coeff);
            coeffList.add(coeff);
        }
        sortLevels();
    }

    //todo проверить сортировку
    public void sortLevels() {
        Collections.sort(levels, Integer::compareTo);
    }

    public ArrayList<Integer> getLevels() {
        return levels;
    }

    public ArrayList<XMLCoefficient> getCoeffList() {
        return coeffList;
    }

    public HashMap<Integer, ArrayList<XMLCoefficient>> getCoeffs() {
        return coeffs;
    }
}
