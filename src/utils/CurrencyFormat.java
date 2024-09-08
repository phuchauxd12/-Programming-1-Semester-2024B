package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormat {
    public static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    public static String format(double amount) {
        return currencyFormat.format(amount);
    }
}
