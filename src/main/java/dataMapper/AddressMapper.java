package dataMapper;

import applicationClasses.Address;
import dataMapper.IMapper.IDataMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AddressMapper implements IDataMapper<Address> {

    private static AddressMapper instance;

    private AddressMapper() {}

    public static AddressMapper getInstance() {
        if (instance == null) {
            instance = new AddressMapper();
        }
        return instance;
    }

    @Override
    public Address find(int id) {
        ResultSet rs = null;
        String sql = "SELECT * FROM Address WHERE Address_ID = ?";
        Address address = null;
        try (Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, sql);) {

            statement.setInt(1, id);
            rs = statement.executeQuery();
            if(rs.next()) {
                address = new Address(
                        id,
                        rs.getString("district"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("city"),
                        rs.getString("postalCode"),
                        rs.getString("street"),
                        rs.getInt("houseNumber"),
                        rs.getString("country"));
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
        return address;
    }

    @Override
    public boolean create(Address address) {
        String sql = "INSERT INTO Address (district, phone, email, city, postalCode, street, houseNumber, country) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql,
                     address.getDistrict(), address.getPhone(), address.getEmail(), address.getCity(), address.getPostalCode(),
                     address.getStreet(), address.getHouseNumber(), address.getCountry())) {

            int rowsInserted = statement.executeUpdate();

            if(rowsInserted > 0){
                Database.setLastGeneratedID(address, conn, "Address", "Address_ID");
                System.out.println("Account has been created");
            } else {
                System.out.println("Something went wrong");
            }

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean update(Address obj) {
        return false;

    }

    @Override
    public boolean delete(Address obj) {
        return false;
    }


}
