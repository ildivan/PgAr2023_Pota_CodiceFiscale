package CodiceFiscale.person;


import CodiceFiscale.error.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PersonTest {

    static public LocalDate today = LocalDate.now();

    @Test
    public void shouldCreatePerson(){
        new Person("Mario","Rossi",
                Sex.M,"Milano","2003-12-25");
    }

    @Test
    public void shouldFailToCreatePersonBornInTheFutureByAYear(){
        int currentYear = today.getYear();
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", String.format("%d-1-1",currentYear+1));
        });
    }

    @Test
    public void shouldFailToCreatePersonBornInTheFutureByAMonth(){
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", String.format("%d-%d-1",currentYear,currentMonth+1));
        });
    }

    @Test
    public void shouldFailToCreatePersonBornInTheFutureByADay(){
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        int currentDay = today.getDayOfMonth();
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", String.format("%d-%d-%d",currentYear,currentMonth,currentDay+1));
        });
    }

    @Test
    public void shouldFailToCreatePersonWithInvalidMonth(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", "2003-13-25");
        });
    }

    //Tests invalid day for the only month with 28 days
    @Test
    public void shouldFailToCreatePersonWithInvalidDayForFebruary(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", "2003-2-29");
        });
    }

    @Test
    public void shouldSucceedToCreatePerson29FebruaryInLeapYear(){
        new Person("Mario", "Rossi",
                    Sex.M, "Milano", "2020-02-29");

    }

    //Tests invalid day for a month with 31 days
    @Test
    public void shouldFailToCreatePersonWithInvalidDayForJanuary(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", "2003-01-32");
        });
    }

    //Tests invalid day for a month with 30 days
    @Test
    public void shouldFailToCreatePersonWithInvalidDayForNovember(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", "2003-11-31");
        });
    }

    @Test
    public void shouldFailToCreatePersonWithInvalidCharactersInName(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario7", "Rossi",
                    Sex.M, "Milano", "2003-11-30");
        });
    }

    @Test
    public void shouldFailToCreatePersonWithInvalidCharactersInSurname(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("Mario", "Rossi\n",
                    Sex.M, "Milano", "2003-11-30");
        });
    }

    @Test
    public void shouldFailToCreatePersonWithOnlyOneVocalAndNoConsonants(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("a", "Rossi",
                    Sex.M, "Milano", "2003-11-30");
        });
    }

    @Test
    public void shouldFailToCreatePersonWithOnlyOneConsonantAndNoVocals(){
        Assertions.assertThrows(InvalidInputException.class,()->{
            new Person("c", "Rossi",
                    Sex.M, "Milano", "2003-11-30");
        });
    }

    @Test
    public void shouldSucceedToCreatePersonWithOnlyOneConsonantAndOneVocal(){
        new Person("ca", "Rossi", Sex.M,
                "Milano", "2003-11-30");
    }

    @Test
    public void shouldSucceedToCreatePersonWithOnlyTwoVocals(){
        new Person("aa", "Rossi", Sex.M,
                "Milano", "2003-11-30");
    }
}
