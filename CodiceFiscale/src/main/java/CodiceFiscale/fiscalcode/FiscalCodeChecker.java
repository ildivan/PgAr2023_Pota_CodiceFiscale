package CodiceFiscale.fiscalcode;

import java.util.Map;

public class FiscalCodeChecker {
    private final Map<String, String> cityCodes;

    public FiscalCodeChecker(Map<String, String> cityCodes) {
        this.cityCodes = cityCodes;
    }

    public boolean isValid(String fiscalCode){
        return isFormatValid(fiscalCode) && areNameAndSurnameValid(fiscalCode)
                && checkMonth(fiscalCode) && checkDay(fiscalCode)
                && checkCityCode(fiscalCode);
    }

    //Returns true if the letters and the digits are in the right place, also checks for length.
    private boolean isFormatValid(String fiscalCode){
        return fiscalCode.matches("[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]");
    }

    private boolean areNameAndSurnameValid(String fiscalCode) {
        String name = fiscalCode.substring(0,3);
        String surname = fiscalCode.substring(3,6);
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
        String month = fiscalCode.substring(8,9);
        return isDayInBounds(day) && doesDayExistsInMonth(adjustedDay,month);
    }

    private boolean isDayInBounds(int day) {
        return (day >= 1 && day <= 31) || (day >= 41 && day <= 71 );
    }

    private boolean doesDayExistsInMonth(int day, String month) {
        int daysInTheMonth;
        switch (month) {
            case "A", "C", "E", "L", "M" -> daysInTheMonth = 31;
            case "D", "H", "P", "R", "S", "T" -> daysInTheMonth = 30;
            case "B" -> daysInTheMonth = 28;
            default -> daysInTheMonth = 0;
        }

        return day <= daysInTheMonth;
    }

    private boolean checkCityCode(String fiscalCode) {
        String city = fiscalCode.substring(12,15);
        return cityCodes.containsValue(city);
    }

}
