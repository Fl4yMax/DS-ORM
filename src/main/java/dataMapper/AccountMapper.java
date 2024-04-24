package dataMapper;

import applicationClasses.Account;
import applicationClasses.Company;
import applicationClasses.Person;
import dataMapper.IMapper.IDataMapper;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountMapper implements IDataMapper<Account> {

    private static final String SHOW_HOURS_WORKED = "SELECT SUM(hoursWorked) as worked FROM account a JOIN person p ON a.personID = p.Person_ID "+
                                                    "JOIN Work w ON p.Person_ID = w.personID WHERE Person_ID = ? AND assignDate > ? AND assignDate < ?";

    private static AccountMapper instance;

    private AccountMapper() {}

    public static AccountMapper getInstance() {
        if (instance == null) {
            instance = new AccountMapper();
        }
        return instance;
    }

    @Override
    public Account find(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Account account = null;

        try {
            connection = Database.getConnection();
            if(connection != null) {
                statement = connection.prepareStatement("SELECT * FROM Account WHERE Account_ID = ?");
                statement.setInt(1, id);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                        int salary = resultSet.getInt("salary");
                        int rank = resultSet.getInt("rank");
                        int bonus = resultSet.getInt("bonus");
                        String password = resultSet.getString("password");
                        String active = resultSet.getString("active");
                        String bank = resultSet.getString("bankNumber");
                        String foreNumber = resultSet.getString("bankForeNumber");
                        String accNumber = resultSet.getString("accBankNumber");
                        int pID = resultSet.getInt("personID");

                        Person person = PersonMapper.getInstance().find(pID);
                        boolean act = active.equals("T");

                        account = new Account(id, salary, rank, bonus, password, act, bank, foreNumber, accNumber, person);
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

        return account;
    }

    @Override
    public boolean create(Account account) {
        String sql = "INSERT INTO Account (salary, rank, bonus, password, bankNumber, bankForeNumber, accBankNumber, personID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = IDataMapper.Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, account.getSalary(), account.getRank(), account.getBonus(), account.getPassword(), account.getBankNumber(), account.getBankForeNumber(), account.getAccBankNumber(), account.getPerson().getId())){

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                Database.setLastGeneratedID(account, conn, "Account", "Account_ID");
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

    @Override
    public boolean update(Account account) {
        String sql = "UPDATE Account SET salary = ?, rank = ?, bonus = ?, bankNumber = ?, bankForeNumber = ?, accBankNumber = ? WHERE Account_ID = ?";
        try (Connection conn = IDataMapper.Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, account.getSalary(), account.getRank(), account.getBonus(), account.getBankNumber(), account.getBankForeNumber(), account.getAccBankNumber(), account.getId())) {

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Account has been updated");
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

    @Override
    public boolean delete(Account account) {
        String sql = "DELETE FROM Account WHERE Account_ID = ?";
        try (Connection conn = IDataMapper.Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, account.getId()))  {

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Account has been deleted");
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

    public void showLeaderboard(String from, String to){
        List<Person> leaderboard = new ArrayList<>();
        ResultSet rs = null;
        try (Connection conn = Database.getConnection();
             CallableStatement statement = conn.prepareCall("{? = call ShowLeaderboards(?, ?)}");) {

            statement.setDate(2, java.sql.Date.valueOf(from));
            statement.setDate(3, java.sql.Date.valueOf(to));

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            rs = (ResultSet) statement.getObject(1);
            if(rs == null){
                System.out.println("Found nothing");
            }

            //rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                System.out.println("NAME: " + rs.getString("firstName") + " " + rs.getString("lastName") + " RANK: " + rs.getInt("currentRank") + ".");
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

    public void showHoursWorked(Person person, String from, String to) {
        ResultSet rs = null;
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, SHOW_HOURS_WORKED, person.getId(), java.sql.Date.valueOf(from), java.sql.Date.valueOf(to))){

            rs = statement.executeQuery();
            if(rs.next()){
                System.out.println("Hours worked from: " + from + " to: " + to + " = " + rs.getInt("worked") + "hours");
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
    }
}
