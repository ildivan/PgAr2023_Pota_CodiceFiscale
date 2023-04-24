package CodiceFiscale.person;

import CodiceFiscale.error.InvalidDateException;
import CodiceFiscale.error.InvalidNameException;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;

/**
 * Represents a person, contains all data to work with personal fiscal codes.
 */
public class Person{

    private final String name;
    private final String surname;
    private final Sex sex;
    private final String cityOfBirth;
    private final int yearOfBirth;
    private final int monthOfBirth;
    private final int dayOfBirth;

    /**
     * @param name         Name of the person.
     * @param surname      Surname of the person.
     * @param sex          Can be male (Sex.M) or female (Sex.F)
     * @param cityOfBirth  String containing the city of birth of the person.
     * @param yearOfBirth  Year of birth of the person.
     * @param monthOfBirth Month of birth of the person.
     * @param dayOfBirth   Day of birth of the person.
     */
    public Person(String name, String surname, Sex sex,
                  String cityOfBirth, int yearOfBirth,
                  int monthOfBirth, int dayOfBirth){
        checkName(name);
        checkName(surname);
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.cityOfBirth = cityOfBirth;

        checkDateValidity(yearOfBirth, monthOfBirth, dayOfBirth);
        this.yearOfBirth = yearOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.dayOfBirth = dayOfBirth;

    }

    private void checkName(String name){
        //Checks that the name is only letters and at least 2 letters long.
        boolean onlyLetters = name.matches("[A-Za-z][A-Za-z]+");
        //Checks that there are at least two vocals in the name.
        boolean atLeastTwoVocals = name.matches(".*[AEIOUaeiou].*[AEIOUaeiou].*");
        //Checks that there are at least a vocal and a consonant.
        boolean containsVocalAndConsonant =
                name.matches("((.*[AEIOUaeiou].*[^AEIOUaeiou].*)|(.*[^AEIOUaeiou].*[AEIOUaeiou].*))");

        boolean isValid = onlyLetters && (atLeastTwoVocals || containsVocalAndConsonant);

        if(!isValid){
            throw new InvalidNameException(String.format("Name not valid: %s",name));
        }
    }

    private void checkDateValidity(int year, int month, int day) {
        LocalDate today = LocalDate.now();

        checkYear(year,today);
        checkMonth(month, year,today);
        checkDay(day, month, year,today);
    }

    private void checkYear(int year, LocalDate today) {
        int currentYear = today.getYear();

        if (year > currentYear) {
            throw new InvalidDateException("Year of birth not valid.");
        }
    }

    private void checkMonth(int month, int year, LocalDate today) {
        boolean isMonthValid;

        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        if (year == currentYear) {
            isMonthValid = month <= currentMonth;
        }else{
            isMonthValid = month <= 12;
        }

        if (!isMonthValid) {
            throw new InvalidDateException("Month of birth not valid.");
        }
    }

    private void checkDay(int day, int month, int year, LocalDate today) {
        boolean isDayValid;

        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        int currentDay = today.getDayOfMonth();

        if (year == currentYear && month == currentMonth) {
            isDayValid = day <= currentDay;
        }else{
            YearMonth yearMonthObject = YearMonth.of(year, month);
            int daysInMonth = yearMonthObject.lengthOfMonth();
            isDayValid = day <= daysInMonth;
        }

        if (!isDayValid) {
            throw new InvalidDateException("Day of birth not valid.");
        }
    }


    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Sex getSex() {
        return sex;
    }

    public String getCityOfBirth() {
        return cityOfBirth;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public int getMonthOfBirth() {
        return monthOfBirth;
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }
}
