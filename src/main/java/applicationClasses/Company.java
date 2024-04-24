package applicationClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Company {
    int id;
    private String name;
    private String cin;
    private String tin;

    public Company(String name, String cin, String tin) {
        this.name = name;
        this.cin = cin;
        this.tin = tin;
    }

    public Company(int id, String name, String cin, String tin) {
        this.id = id;
        this.name = name;
        this.cin = cin;
        this.tin = tin;
    }
}
