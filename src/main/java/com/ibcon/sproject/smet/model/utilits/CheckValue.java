package com.ibcon.sproject.smet.model.utilits;

public class CheckValue {

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s.replace(",", "."));
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean isInteger(String s) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}
