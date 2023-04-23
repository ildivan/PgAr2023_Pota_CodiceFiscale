package CodiceFiscale.person;

import CodiceFiscale.error.InvalidDateException;

import java.time.Year;
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
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.cityOfBirth = cityOfBirth;

        checkDateValidity(yearOfBirth, monthOfBirth, dayOfBirth);
        this.yearOfBirth = yearOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.dayOfBirth = dayOfBirth;

    }

    private void checkDateValidity(int year, int month, int day) {
        checkYear(year);
        checkMonth(month, year);
        checkDay(day, month, year);
    }

    private void checkYear(int year) {
        int currentYear = Year.now().getValue();
        if (year > currentYear) throw new InvalidDateException("Year of birth not valid.");
    }

    private void checkMonth(int month, int year) {
        boolean isMonthValid = month <= 12;
        int currentYear = Year.now().getValue();
        if (year == currentYear) {
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            isMonthValid = month <= currentMonth;
        }
        if (!isMonthValid) throw new InvalidDateException("Month of birth not valid.");
    }

    private void checkDay(int day, int month, int year) {
        int daysInTheMonth;
        switch (month) {
            case 1, 3, 5, 7, 8 -> daysInTheMonth = 31;
            case 4, 6, 9, 10, 11, 12 -> daysInTheMonth = 30;
            default -> daysInTheMonth = 28;
        }
        boolean isDayValid = day <= daysInTheMonth;
        int currentYear = Year.now().getValue();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (year == currentYear && month == currentMonth) {
            isDayValid = day <= currentDay;
        }

        if (!isDayValid) throw new InvalidDateException("Day of birth not valid.");
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
