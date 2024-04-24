package applicationClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProduct {
    Order order;
    Product product;
    int count;
    int sent;

    public OrderProduct(int count, Order order, Product product) {
        this.order = order;
        this.product = product;
        this.count = count;
    }

    public OrderProduct(int count, int sent, Order order, Product product) {
        this.order = order;
        this.product = product;
        this.count = count;
        this.sent = sent;
    }
}
