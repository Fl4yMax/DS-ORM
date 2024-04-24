package applicationClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    int id;
    int salary;
    int rank;
    int bonus;
    String password;
    boolean active;
    String bankNumber;
    String bankForeNumber;
    String accBankNumber;
    Person person;

    public Account(int salary, int rank, int bonus, String password, boolean active, String bankNumber, String bankForeNumber, String accBankNumber, Person person) {
        this.salary = salary;
        this.rank = rank;
        this.bonus = bonus;
        this.password = password;
        this.active = active;
        this.bankNumber = bankNumber;
        this.bankForeNumber = bankForeNumber;
        this.accBankNumber = accBankNumber;
        this.person = person;
    }

    public Account(int id, int salary, int rank, int bonus, String password, boolean active, String bankNumber, String bankForeNumber, String accBankNumber, Person person) {
        this.id = id;
        this.salary = salary;
        this.rank = rank;
        this.bonus = bonus;
        this.password = password;
        this.active = active;
        this.bankNumber = bankNumber;
        this.bankForeNumber = bankForeNumber;
        this.accBankNumber = accBankNumber;
        this.person = person;
    }
}
