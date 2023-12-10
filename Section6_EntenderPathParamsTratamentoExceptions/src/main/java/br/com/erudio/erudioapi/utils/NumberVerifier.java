package br.com.erudio.erudioapi.utils;

public class NumberVerifier {

    public static boolean isZero(String strNumber) {
        return NumberConverter.toDouble(strNumber).equals(0D);
    }

    public static boolean isNumeric(String strNumber) {
        if (strNumber == null) {
            return false;
        }
        String number = strNumber.replaceAll(",",".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
