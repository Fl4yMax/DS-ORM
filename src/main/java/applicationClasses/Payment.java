package applicationClasses;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Payment {
    int id;
    Date paymentDate;
    float amount;
    Person person;

    public Payment(Date paymentDate, int amount, Person person) {
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.person = person;
    }

    public Payment(int id, Date paymentDate, int amount, Person person) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.person = person;
    }
}
