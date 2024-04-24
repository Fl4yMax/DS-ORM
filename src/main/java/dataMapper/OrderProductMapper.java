package dataMapper;

import applicationClasses.Order;
import applicationClasses.OrderProduct;
import applicationClasses.Product;
import dataMapper.IMapper.IDataMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderProductMapper implements IDataMapper<OrderProduct> {

    private static OrderProductMapper instance;

    private static String SHOW_PRODUCTS_OF_THE_ORDER = "SELECT * FROM OrderProduct op " +
                                                        "JOIN \"Order\" o ON op.orderID = o.Order_ID " +
                                                        "JOIN Product p ON op.productID = p.Product_ID "+
                                                        "WHERE Order_ID = ?";

    private static String ADD_PRODUCT = "INSERT INTO OrderProduct(count, orderID, productID)"+
                                        "VALUES (?,?,?)";

    private OrderProductMapper() {}

    public static OrderProductMapper getInstance() {
        if (instance == null) {
            instance = new OrderProductMapper();
        }
        return instance;
    }

    @Override
    public OrderProduct find(int id) {
        return null;
    }

    @Override
    public boolean create(OrderProduct orderProduct) {
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, ADD_PRODUCT, orderProduct.getCount(), orderProduct.getOrder().getId(), orderProduct.getProduct().getId())) {

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Product succesfully added to the order");
            }
            else {
                System.out.println("Something went wrong when adding the product");
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OrderProduct obj) {
        return false;
    }

    @Override
    public boolean delete(OrderProduct orderProduct) {
        String sql = "DELETE FROM ORDERPRODUCT WHERE order_ID = ?";
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, sql, orderProduct.getOrder().getId())) {

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Product was removed from the order");
            }
            else {
                System.out.println("Something went wrong when adding the product");
            }
            return rowsAffected > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
        }
    }

    public List<OrderProduct> showOrderProducts(Order order){
        ResultSet rs = null;
        List<OrderProduct> products = new ArrayList<>();
        try(Connection conn = Database.getConnection();
        PreparedStatement statement = Database.prepareStatement(conn, SHOW_PRODUCTS_OF_THE_ORDER, order.getId())) {

            rs = statement.executeQuery();
            while (rs.next()) {
                OrderProduct product = new OrderProduct(
                        rs.getInt("count"),
                        rs.getInt("sent"),
                        order,
                        new Product(rs.getString("name"),
                                rs.getFloat("price"),
                                rs.getInt("count"),
                                ProductMapper.getInstance().find(rs.getInt("productID"))
                        )
                );
                products.add(product);
            }

            if(products.isEmpty()) {
                System.out.println("Found nothing");
            }
            else {
                System.out.println(products.size() + " products has been found in the order");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return products;
    }
}
