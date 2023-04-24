package CodiceFiscale.person;


import CodiceFiscale.error.InvalidDateException;
import CodiceFiscale.error.InvalidNameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Calendar;

public class PersonTest {

    @Test
    public void shouldCreatePerson(){
        new Person("Mario","Rossi",
                Sex.M,"Milano",2003,12,25);
    }

    @Test
    public void shouldFailToCreatePersonBornInTheFutureByAYear(){
        int currentYear = Year.now().getValue();
        Assertions.assertThrows(InvalidDateException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", currentYear+1,1,1);
        });
    }

    @Test
    public void shouldFailToCreatePersonBornInTheFutureByAMonth(){
        int currentYear = Year.now().getValue();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Assertions.assertThrows(InvalidDateException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", currentYear,currentMonth+1,1);
        });
    }

    @Test
    public void shouldFailToCreatePersonBornInTheFutureByADay(){
        int currentYear = Year.now().getValue();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Assertions.assertThrows(InvalidDateException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", currentYear,currentMonth,currentDay+1);
        });
    }

    @Test
    public void shouldFailToCreatePersonWithInvalidMonth(){
        Assertions.assertThrows(InvalidDateException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", 2003,13,25);
        });
    }

    //Tests invalid day for the only month with 28 days
    @Test
    public void shouldFailToCreatePersonWithInvalidDayForFebruary(){
        Assertions.assertThrows(InvalidDateException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", 2003,2,29);
        });
    }

    @Test
    public void shouldSucceedToCreatePerson29FebruaryInLeapYear(){
        new Person("Mario", "Rossi",
                    Sex.M, "Milano", 2020,2,29);

    }

    //Tests invalid day for a month with 31 days
    @Test
    public void shouldFailToCreatePersonWithInvalidDayForJanuary(){
        Assertions.assertThrows(InvalidDateException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", 2003,1,32);
        });
    }

    //Tests invalid day for a month with 30 days
    @Test
    public void shouldFailToCreatePersonWithInvalidDayForNovember(){
        Assertions.assertThrows(InvalidDateException.class,()->{
            new Person("Mario", "Rossi",
                    Sex.M, "Milano", 2003,11,31);
        });
    }

    @Test
    public void shouldFailToCreatePersonWithInvalidCharactersInName(){
        Assertions.assertThrows(InvalidNameException.class,()->{
            new Person("Mario7", "Rossi",
                    Sex.M, "Milano", 2003,11,30);
        });
    }

    @Test
    public void shouldFailToCreatePersonWithInvalidCharactersInSurname(){
        Assertions.assertThrows(InvalidNameException.class,()->{
            new Person("Mario", "Rossi\n",
                    Sex.M, "Milano", 2003,11,30);
        });
    }

    @Test
    public void shouldFailToCreatePersonWithOnlyOneVocalAndNoConsonants(){
        Assertions.assertThrows(InvalidNameException.class,()->{
            new Person("a", "Rossi",
                    Sex.M, "Milano", 2003,11,30);
        });
    }

    @Test
    public void shouldFailToCreatePersonWithOnlyOneConsonantAndNoVocals(){
        Assertions.assertThrows(InvalidNameException.class,()->{
            new Person("c", "Rossi",
                    Sex.M, "Milano", 2003,11,30);
        });
    }

    @Test
    public void shouldSucceedToCreatePersonWithOnlyOneConsonantAndOneVocal(){
        new Person("ca", "Rossi", Sex.M,
                "Milano", 2003,11,30);
    }

    @Test
    public void shouldSucceedToCreatePersonWithOnlyTwoVocals(){
        new Person("aa", "Rossi", Sex.M,
                "Milano", 2003,11,30);
    }
}
