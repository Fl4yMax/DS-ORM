package dataMapper;

import applicationClasses.Company;
import applicationClasses.Person;
import dataMapper.IMapper.IDataMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyMapper implements IDataMapper<Company> {

    private static final String CREATE_COMPANY = "INSERT INTO Company(name, cin, tin) "+
                                                "VALUES(?, ?, ?)";

    private static final String SHOW_INFORMATION = "SELECT * FROM Company c JOIN Person p ON c.Company_ID = p.companyID WHERE c.Company_ID = ?";

    private static final String LINK_COMPANY = "UPDATE Person SET companyID = ? WHERE Person_ID = ?";
    private static CompanyMapper instance;

    private CompanyMapper() {}

    public static CompanyMapper getInstance() {
        if (instance == null) {
            instance = new CompanyMapper();
        }
        return instance;
    }

    @Override
    public Company find(int id) {
        Company company = null;
        ResultSet rs = null;
        try (Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, SHOW_INFORMATION, id);){

            rs = statement.executeQuery();
            if(rs.next()) {
                company = new Company(
                        rs.getInt("Company_ID"),
                        rs.getString("name"),
                        rs.getString("cin"),
                        rs.getString("tin"));
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
        return company;
    }

    @Override
    public boolean create(Company company) {
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, CREATE_COMPANY, company.getName(), company.getCin(), company.getTin());
             ) {

            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0) {
                System.out.println("Company has been created");
                Database.setLastGeneratedID(company, conn, "Company", "Company_ID");
            }
            else {
                System.out.println("Something went wrong");
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean linkCompanyToPerson(Company company, Person person) {
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, LINK_COMPANY, company.getId(), person.getId())) {

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Company: " + company.getName() + " has been linked to: " + person.getFirstName() + " " + person.getLastName() + " succesfully");
            } else {
                System.out.println("Could not link the company");
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean update(Company obj) {
        return false;
    }

    @Override
    public boolean delete(Company obj) {
        return false;
    }

}
