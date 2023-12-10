package br.com.erudio.erudioapi.utils;

public class NumberConverter {

    public static Double toDouble(String strNumber) {
        if (strNumber == null) {
            return 0D;
        }
        String number = strNumber.replaceAll(",",".");
        if (NumberVerifier.isNumeric(strNumber)) {
            return Double.parseDouble(number);
        }
        return 0D;
    }
}
