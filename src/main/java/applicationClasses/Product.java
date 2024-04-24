package applicationClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    int id;
    String name;
    float price;
    int count;
    Product product;

    public Product(String name, float price, int count, Product product) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.product = product;
    }

    public Product(int id, String name, float price, int count, Product product) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.product = product;
    }
}
