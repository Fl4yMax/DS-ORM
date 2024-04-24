package applicationClasses;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class Work {
    int id;
    int toMake;
    int made;
    int hoursWorked;
    boolean active;
    Date assignDate;
    Date finishDate;
    Person person;
    Product product;

    public Work(Person person, Product product, int toMake, int hoursWorked, Date finishDate) {
        this.person = person;
        this.product = product;
        this.toMake = toMake;
        this.hoursWorked = hoursWorked;
        this.finishDate = finishDate;
    }

    public Work(Person person, Product product, int toMake, int made, int hoursWorked, Date assignDate, Date finishDate, boolean active) {
        this.toMake = toMake;
        this.made = made;
        this.hoursWorked = hoursWorked;
        this.assignDate = assignDate;
        this.finishDate = finishDate;
        this.active = active;
        this.person = person;
        this.product = product;
    }

    public Work(int id, Person person, Product product, int toMake, int made, int hoursWorked, Date assignDate, Date finishDate, boolean active) {
        this.id = id;
        this.toMake = toMake;
        this.made = made;
        this.hoursWorked = hoursWorked;
        this.assignDate = assignDate;
        this.finishDate = finishDate;
        this.active = active;
        this.person = person;
        this.product = product;
    }
}
