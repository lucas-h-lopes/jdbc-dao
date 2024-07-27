package util;

public class TextColor {
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[31m";
    private static final String GREEN = "\033[32m";
    private static final String YELLOW = "\033[33m";
    private static final String BLUE = "\033[34m";

    public static String formatToRed(String text){
        return RED + text + RESET;
    }

    public static String formatToGreen(String text){
        return GREEN + text + RESET;
    }

    public static String formatToYellow(String text){
        return YELLOW + text + RESET;
    }

    public static String formatToBlue(String text){
        return BLUE + text + RESET;
    }
}
