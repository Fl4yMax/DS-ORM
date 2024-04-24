package applicationClasses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Order {
    int id;
    Person person;
    boolean readyToExport;
    Date orderDate;
    Date exportDate;

    public Order(Person person) {
        this.person = person;
    }

    public Order(int id, Date orderDate, boolean readyToExport, Date exportDate, Person person) {
        this.id = id;
        this.person = person;
        this.readyToExport = readyToExport;
        this.orderDate = orderDate;
        this.exportDate = exportDate;
    }
}
