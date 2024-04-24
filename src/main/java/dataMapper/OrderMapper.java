package dataMapper;

import applicationClasses.Order;
import applicationClasses.OrderProduct;
import applicationClasses.Person;
import applicationClasses.Product;
import dataMapper.IMapper.IDataMapper;

import javax.swing.plaf.LabelUI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper implements IDataMapper<Order> {

    //Ord001
    private static final String SHOW_ACTIVE = "SELECT * FROM \"Order\" WHERE exportDate IS NULL";
    //Ord002
    private static final String SHOW_EXPORTED = "SELECT * FROM \"Order\" WHERE exportDate IS NOT NULL";
    //Ord003
    private static final String SHOW_CUSTOMER_INFO = "SELECT * FROM \"Order\" WHERE personID = ?";
    private static final String SEARCH_ORDER_BY_FILTER = "SELECT * FROM \"Order\" WHERE ";
    private static final String VERIFY_QUALITY = "UPDATE \"Order\" SET readyToExport = 'T' WHERE Order_ID = ?";
    private static OrderMapper instance;

    private OrderMapper() {}

    public static OrderMapper getInstance() {
        if (instance == null) {
            instance = new OrderMapper();
        }
        return instance;
    }

    @Override
    public Order find(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Order order = null;

        try {
            connection = Database.getConnection();

            if (connection != null) {
                String sql = "SELECT * FROM \"Order\" WHERE Order_ID = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, id);

                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                            int ordID = resultSet.getInt("Order_ID");
                            Date orderDate = resultSet.getDate("orderDate");
                            boolean readyToExport = resultSet.getBoolean("readyToExport");
                            Date exportDate = resultSet.getDate("exportDate");
                            int pID = resultSet.getInt("personID");

                            Person person = PersonMapper.getInstance().find(pID);

                            order = new Order(ordID, orderDate, readyToExport, exportDate, person);
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

        return order;
    }

    @Override
    public boolean create(Order order) {
        String sql = "INSERT INTO \"Order\" (personID) VALUES (?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, order.getPerson().getId())) {

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                Database.setLastGeneratedID(order, conn, "\"Order\"", "Order_ID");
                System.out.println("Order has been created");
            } else {
                System.out.println("Failed to update order");
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Order order) {

        String sql = "UPDATE \"Order\" SET orderDate = ?, readyToExport = ?, exportDate = ?, personID = ? WHERE Order_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql,
                     order.getOrderDate(), order.isReadyToExport(), order.getExportDate(), order.getPerson().getId(), order.getId())) {

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order has been updated");
            } else {
                System.out.println("Failed to update order");
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Order order) {

        String sql = "DELETE FROM \"Order\" WHERE Order_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, order.getId())) {

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Order has been deleted");
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

    public List<Order> showOrders(boolean active) {
        String sql;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();
        if(active) {
            sql = SHOW_ACTIVE;
        }
        else {
            sql = SHOW_EXPORTED;
        }
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, sql);) {

            rs = statement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("Order_ID");
                int pID = rs.getInt("personID");
                Date orderDate = rs.getDate("orderDate");
                Date exportDate = rs.getDate("exportDate");

                Person person = PersonMapper.getInstance().find(pID);

                Order o = new Order(id, orderDate, active, exportDate, person);

                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    public Person showCustomerInfo(int orderID){
        return find(orderID).getPerson();
    }

    public boolean verityQuality(Order order){
        try (Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, VERIFY_QUALITY, order.getId());){

            int numberOfRowsAffected = statement.executeUpdate();

            if(numberOfRowsAffected > 0){
                System.out.println("Order ID: " + order.getId() + " quality has been verified, Ready to export");
            }
            else{
                System.out.println("Something went wrong");
            }
            return numberOfRowsAffected > 0;

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> searchOrderByFilter(String filter, Object value) {
        String sql = SEARCH_ORDER_BY_FILTER;
        List<Order> orders = new ArrayList<>();
        ResultSet rs = null;
        switch (filter.toLowerCase()) {
            case "person" -> sql = sql + "personID = ?";
            case "ready" -> sql = sql + "readyToExport = ?";
            case "orderdate" -> sql = sql + "orderDate = ?";
            case "exportdate" -> {
                if (value == null) {
                    sql = sql + "exportDate IS NULL";
                } else {
                    sql = sql + "exportDate = ?";
                }
            }
        }
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, sql, value);) {

            rs = statement.executeQuery();
            if(rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                        rs.getDate("orderDate"),
                        rs.getString("readyToExport").equals("T"),
                        rs.getDate("exportDate"),
                        PersonMapper.getInstance().find(rs.getInt("personID"))
                );
                orders.add(order);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    public boolean exportOrder(Order order){
        try (Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, "UPDATE \"Order\" SET exportDate = SYSDATE WHERE Order_ID = ?", order.getId())) {

            int affectedRows = statement.executeUpdate();

            if(affectedRows > 0){
                System.out.println("Export date has been set");
            }
            else{
                System.out.println("Something went with setting the exportDate");
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}