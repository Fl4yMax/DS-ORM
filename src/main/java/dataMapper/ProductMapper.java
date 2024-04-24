package dataMapper;

import applicationClasses.Product;
import dataMapper.IMapper.IDataMapper;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class ProductMapper implements IDataMapper<Product> {

    private static final String SHOW_ALL_SUBPRODUCTS = "";

    private static ProductMapper instance;

    private ProductMapper() {}

    public static ProductMapper getInstance() {
        if (instance == null) {
            instance = new ProductMapper();
        }
        return instance;
    }

    @Override
    public Product find(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Product product = null;

        try {
            connection = Database.getConnection();
            if (connection != null) {
                statement = connection.prepareStatement("SELECT * FROM Product WHERE Product_ID = ?");
                statement.setInt(1, id);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int pId = resultSet.getInt("Product_ID");
                    String name = resultSet.getString("name");
                    float price = resultSet.getFloat("price");
                    int count = resultSet.getInt("count");
                    int ppId = resultSet.getInt("productID");

                    Product p = this.find(ppId);

                    product = new Product(pId, name, price, count, p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return product;
    }

    @Override
    public boolean create(Product product) {
        String sql = "INSERT INTO Product (name, price, productID) VALUES (?, ?, ?)";
        Product subproduct = product.getProduct();
        Object subID = null;
        if(subproduct != null){
            subID = subproduct.getId();
        }
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, product.getName(), product.getPrice(), subID)) {

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                Database.setLastGeneratedID(product, conn, "Product", "Product_ID");
                System.out.println("Product has been created");
            } else {
                System.out.println("Failed to create product");
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE Product SET name = ?, price = ?, productID = ? WHERE Product_ID = ?";
        Product subproduct = product.getProduct();
        Object subID = null;
        if(subproduct != null){
            subID = subproduct.getId();
        }
        try (Connection conn = IDataMapper.Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, product.getName(), product.getPrice(), subID, product.getId())) {

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        String sql = "DELETE FROM Product WHERE Product_ID = ?";
        try (Connection conn = IDataMapper.Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, product.getId())) {

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Product has been deleted");
            }
            else{
                System.out.println("Something went wrong");
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void showProductWithItsSubproducts(Product product, String name){
        ResultSet rs = null;
        try(Connection conn = Database.getConnection();
            CallableStatement statement = conn.prepareCall("{? = call getProductWithSubproducts(?, ?)}");) {

            if(product != null) {
                statement.setInt(2, product.getId());
            } else {
                statement.setInt(2, NULL);
            }

            statement.setString(3, name);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            rs = (ResultSet) statement.getObject(1);
            if(rs == null){
                System.out.println("Found nothing");
            }

            while (rs.next()) {
                System.out.println("Product and subproducts are: NAME: " + rs.getString("name") + " PRICE: " + rs.getString("price") + " COUNT: " + rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void copyProductWithSubproducts(Product product, String name){
        try(Connection conn = Database.getConnection();
        CallableStatement statement = conn.prepareCall("{call copyProductWithSubproducts(?, ?)}")) {

            statement.setInt(1, product.getId());
            statement.setString(2, name);

            statement.execute();

            System.out.println("Product copied... Created new product " + name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setAsSubproduct(Product product, Product subProduct){
        return false;
    }

}
