package CodiceFiscale.fiscalcode;

import java.util.ArrayList;
import java.util.Map;

public class FiscalCodeChecker {
    private final Map<String, String> cityCodes;

    public FiscalCodeChecker(Map<String, String> cityCodes) {
        this.cityCodes = cityCodes;
    }

    public boolean isValid(String fiscalCode){
        return isFormatValid(fiscalCode) && areNameAndSurnameValid(fiscalCode)
                && checkMonth(fiscalCode) && checkDay(fiscalCode)
                && checkCityCode(fiscalCode) && checkControlCharacter(fiscalCode);
    }



    //Returns true if the letters and the digits are in the right place, also checks for length.
    private boolean isFormatValid(String fiscalCode){
        return fiscalCode.matches("[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]");
    }

    private boolean areNameAndSurnameValid(String fiscalCode) {
        String surname = fiscalCode.substring(0,3);
        String name = fiscalCode.substring(3,6);
        return checkName(name) && checkName(surname);
    }

    private boolean checkName(String threeDigitsCode){
        //Checks cases without trailing X at the end.
        String withoutXsRegex = "([^AEIOUX]{3}|[^AEIOUX]{2}[AEIOU]|[^AEIOUX][AEIOU]{2}|[AEIOU]{3})";
        //Checks cases with trailing X at the end.
        String withXsRegex = "([^AEIOUX][AEIOU]X|[AEIOU][AEIOU]X)";
        return threeDigitsCode.matches(withoutXsRegex) || threeDigitsCode.matches(withXsRegex);
    }

    private boolean checkMonth(String fiscalCode){
        //Checks if the ninth character is one of the allowed characters for the month.
        return fiscalCode.matches(".{8}[ABCDEHLMPRST].{7}");
    }

    private boolean checkDay(String fiscalCode) {
        int day = Integer.parseInt(fiscalCode.substring(9,11));
        int adjustedDay;
        if(day >= 41) {
            adjustedDay = day - 40;
        }else{
            adjustedDay = day;
        }
        Character month = fiscalCode.charAt(8);
        return isDayInBounds(day) && doesDayExistsInMonth(adjustedDay,month);
    }

    private boolean isDayInBounds(int day) {
        return (day >= 1 && day <= 31) || (day >= 41 && day <= 71 );
    }

    private boolean doesDayExistsInMonth(int day, Character month) {
        int daysInTheMonth;
        switch (month) {
            case 'A', 'C', 'E', 'L', 'M' -> daysInTheMonth = 31;
            case 'D', 'H', 'P', 'R', 'S', 'T' -> daysInTheMonth = 30;
            case 'B' -> daysInTheMonth = 28;
            default -> daysInTheMonth = 0;
        }

        return day <= daysInTheMonth;
    }

    private boolean checkCityCode(String fiscalCode) {
        String city = fiscalCode.substring(11,15);
        return cityCodes.containsValue(city);
    }

    private boolean checkControlCharacter(String fiscalCode) {
        char calculatedControlChar = calculateControlCharacter(fiscalCode.substring(0,fiscalCode.length()-1));
        char realControlChar = (char)fiscalCode.charAt(15);
        return calculatedControlChar == realControlChar;
    }

    protected Character calculateControlCharacter(String fiscalCode) {
        ArrayList<Integer> numericFiscalCode = convertFiscalCodeToNumeric(fiscalCode);
        int sum = numericFiscalCode.stream().reduce(0, Integer::sum);
        int numericControlChar = sum%26;
        return (char) (numericControlChar + (int)'A');
    }

    private ArrayList<Integer> convertFiscalCodeToNumeric(String fiscalCode) {
        ArrayList<Integer> numericFiscalCode = new ArrayList<>();

        for (int i = 0; i < fiscalCode.length(); i++) {
            int toAdd;
            if((i+1)%2 == 0){
                toAdd = convertEvenOrderCharacter(fiscalCode.charAt(i));
            }else{
                toAdd = convertOddOrderCharacter(fiscalCode.charAt(i));
            }
            numericFiscalCode.add(toAdd);
        }

        return numericFiscalCode;
    }

    private int convertEvenOrderCharacter(char c) {
        if(Character.isDigit(c)){
            return Character.getNumericValue(c);
        }
        return (int)c - (int)'A';
    }

    private int convertOddOrderCharacter(char c) {
        int value;
        switch (c) {
            case 'A', '0' -> value = 1;
            case 'B', '1' -> value = 0;
            case 'C', '2' -> value = 5;
            case 'D', '3' -> value = 7;
            case 'E', '4' -> value = 9;
            case 'F', '5' -> value = 13;
            case 'G', '6' -> value = 15;
            case 'H', '7' -> value = 17;
            case 'I', '8' -> value = 19;
            case 'J', '9' -> value = 21;
            case 'K' -> value = 2;
            case 'L' -> value = 4;
            case 'M' -> value = 18;
            case 'N' -> value = 20;
            case 'O' -> value = 11;
            case 'P' -> value = 3;
            case 'Q' -> value = 6;
            case 'R' -> value = 8;
            case 'S' -> value = 12;
            case 'T' -> value = 14;
            case 'U' -> value = 16;
            case 'V' -> value = 10;
            case 'W' -> value = 22;
            case 'X' -> value = 25;
            case 'Y' -> value = 24;
            case 'Z' -> value = 23;
            default -> value = -1;
        }
        return value;
    }

}
