package pharmacy.utils;

public class StringUtil {
    public static String capitalizeWords(String str) {
        String[] words = str.split("\\s+"); 
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            capitalized.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }

        return capitalized.toString().trim(); 
    }
}
