package dataMapper;

import applicationClasses.*;
import dataMapper.IMapper.IDataMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonMapper implements IDataMapper<Person> {

    private static PersonMapper instance;

    private static final String UPDATE_PHOTO = "UPDATE Person SET image = ? WHERE Person_ID = ?";
    //COMPLETE IT
    //P006
    private static final String SHOW_TASKS = "SELECT * FROM Person p " +
                                            "JOIN Work w ON p.Person_ID = w.personID " +
                                            "JOIN Product pr On w.productID = pr.Product_ID " +
                                            "WHERE p.Person_ID = ? AND p.role = 'worker'";
    //P004
    private static final String SHOW_ACTIVE = "SELECT * FROM Person p " +
            "JOIN Account a ON p.Person_ID = a.personID " +
            "WHERE a.active = 'T'";
    //P004
    private static final String SHOW_INACTIVE = "SELECT * FROM Person p " +
            "JOIN Account a ON p.Person_ID = a.personID " +
            "WHERE a.active = 'F'";

    //P003
    private static final String SHOW_CURRENT_TASK = "SELECT w.productID, Work_ID, toMake, made, hoursWorked, assignDate, finishDate FROM Person p JOIN Work w ON p.Person_ID = w.personID " +
                                                    "JOIN Product pr ON w.productID = pr.Product_ID WHERE p.Person_ID = ? AND w.active = 'T'";
    //P007
    private static final String SEARCH_BY_FILTER = "SELECT * FROM Person WHERE ";
    //P008
    private static final String SET_INACTIVE = "UPDATE ACCOUNT SET active = 'F' WHERE personID = ?";
    //PO08
    private static final String SET_ACTIVE = "UPDATE ACCOUNT SET active = 'F' WHERE personID = ?";


    private PersonMapper() {}

    public static PersonMapper getInstance() {
        if (instance == null) {
            instance = new PersonMapper();
        }
        return instance;
    }

    @Override
    public Person find(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        Person person = null;
        String sql = "SELECT * FROM Person Where Person_ID = ?";

        try {
            connection = Database.getConnection();
            if (connection != null) {
                statement = connection.prepareStatement(sql);
                statement.setInt(1, id);
                rs = statement.executeQuery();

                if (rs.next()) {
                    person = evalResultSet(rs);
                }
            }

            if(person == null) {
                System.out.println("Couldnt find person with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return person;
    }

    //P006
    @Override
    public boolean create(Person person) {
        String asCompany = person.isAsCompany() ? "T" : "F";
        Company company = person.getCompany();
        Object cmp = null;
        if(company != null){
            cmp = company.getId();
        }
        String sql = "INSERT INTO Person (login, firstName, lastName, image, companyID, addressID, asCompany, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql,
                     person.getLogin(), person.getFirstName(), person.getLastName(),
                     person.getImage(),
                     cmp , person.getAddress().getId(), asCompany,
                     person.getRole())) {

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                Database.setLastGeneratedID(person, conn, "Person", "Person_ID");
                System.out.println("Person has been created");
            }
            else{
                System.out.println("Something went wrong in Person CREATE");
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Person person) {
        String asCompany = person.isAsCompany() ? "T" : "F";
        Company company = person.getCompany();
        Object companyInt = null;
        if(company == null){
            asCompany = "F";
        } else {
            companyInt = company.getId();
        }
        String sql = "UPDATE Person SET login = ?, firstName = ?, lastName = ?, image = ?, " +
                "companyID = ?, addressID = ?, asCompany = ?, role = ? WHERE Person_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql,
                     person.getLogin(), person.getFirstName(), person.getLastName(), person.getImage(),
                     companyInt, person.getAddress().getId(), asCompany,
                     person.getRole(), person.getId())) {

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Person has been updated");
            }
            else{
                System.out.println("Something went wrong in Person UPDATE");
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Person person) {
        String sql = "DELETE FROM Person WHERE Person_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, person.getId())) {

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Person has been deleted");
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

    public boolean updatePhoto(Blob photo, Person person) {
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, UPDATE_PHOTO, photo ,person.getId())) {

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Photo has been updated");
            }
            else{
                System.out.println("Something went wrong in UPDATE_PHOTO");
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Work> showTasks(Person person){
        List<Work> tasks = new ArrayList<>();
        ResultSet rs = null;
        try (Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, SHOW_TASKS, person.getId());) {

            rs = statement.executeQuery();
            while(rs.next()) {
                Product product = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getInt("count"),
                        ProductMapper.getInstance().find(rs.getInt("productID"))
                );

                Work work = new Work(
                        rs.getInt("Work_ID"),
                        person,
                        product,
                        rs.getInt("toMake"),
                        rs.getInt("made"),
                        rs.getInt("hoursWorked"),
                        rs.getDate("assignDate"),
                        rs.getDate("finishDate"),
                        rs.getString("active").equals("T")
                );
                tasks.add(work);
            }
            if(tasks.isEmpty()){
                System.out.println("Found zero tasks");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }



    public Work showCurrentTask(Person person){
        Work work = null;
        ResultSet rs = null;
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, SHOW_CURRENT_TASK, person.getId());) {

            rs = statement.executeQuery();
            if(rs.next()) {
                Product product = ProductMapper.getInstance().find(rs.getInt(1));

                work = new Work(
                        rs.getInt("Work_ID"),
                        person,
                        product,
                        rs.getInt("toMake"),
                        rs.getInt("made"),
                        rs.getInt("hoursWorked"),
                        rs.getDate("assignDate"),
                        rs.getDate("finishDate"),
                        true
                        );
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
        return work;
    }

    public List<Person> searchPersonel(String filter, String value){
        //value = "'" + value + "'";
        String sql = SEARCH_BY_FILTER;
        ResultSet rs = null;
        List<Person> personel = new ArrayList<>();
        switch (filter.toLowerCase()) {
            case "firstname" -> sql = sql + "firstName = ?";
            case "lastname" -> sql = sql + "lastName = ?";
            case "login" -> sql = sql + "login = ?";
            case "role" -> sql = sql + "role = ?";
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, value);) {

            rs = statement.executeQuery();
            while(rs.next()) {
                personel.add(evalResultSet(rs));
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
        return personel;
    }

    private Person evalResultSet(ResultSet rs) throws SQLException{
        int id = rs.getInt("Person_ID");
        int companyID = rs.getInt("companyID");
        int addressID = rs.getInt("addressID");
        String login = rs.getString("login");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        Blob image = rs.getBlob("image");
        boolean asComp = rs.getBoolean("asCompany");
        String role = rs.getString("role");

        Company company = CompanyMapper.getInstance().find(companyID);
        Address address = AddressMapper.getInstance().find(addressID);

        return new Person(id, login, firstName, lastName, image, company, address, asComp, role);
    }

    public boolean setPersonActiveInactive(boolean active, Person person) {
        String sql;
        if(active){
            sql = SET_ACTIVE;
        }
        else{
            sql = SET_INACTIVE;
        }
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, sql, person.getId());) {

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Person active status updated");
            }
            else{
                System.out.println("Something went wrong in SETTING_PERSON_ACTIVE");
            }

            return rowsAffected > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Person> showPersonel(boolean active) {
        String sql;
        ResultSet rs = null;
        List<Person> personel = new ArrayList<>();
        if(active){
            sql = SHOW_ACTIVE;
        }
        else{
            sql = SHOW_INACTIVE;
        }
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, sql);) {

            rs = statement.executeQuery();
            while(rs.next()) {
                Person person = evalResultSet(rs);

                personel.add(person);
            }
            if(personel.isEmpty()){
                System.out.println("Something went wrong in showin PERSONEL");
            }

        } catch(SQLException e) {
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
        return personel;
    }

}
