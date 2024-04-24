package dataMapper;

import applicationClasses.Work;
import dataMapper.IMapper.IDataMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkMapper implements IDataMapper<Work> {

    private static WorkMapper instance;

    private WorkMapper() {}

    public static WorkMapper getInstance() {
        if (instance == null) {
            instance = new WorkMapper();
        }
        return instance;
    }

    @Override
    public Work find(int id) {
        ResultSet rs = null;
        Work w = null;
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, "SELECT * FROM WORK WHERE Work_ID = ?", id)) {

            rs = statement.executeQuery();
            if(rs.next()) {
                w = new Work(
                        id,
                        PersonMapper.getInstance().find(rs.getInt("personID")),
                        ProductMapper.getInstance().find(rs.getInt("productID")),
                        rs.getInt("toMake"),
                        rs.getInt("made"),
                        rs.getInt("hoursWorked"),
                        rs.getDate("assignDate"),
                        rs.getDate("finishDate"),
                        rs.getString("active").equals("T") ? true : false
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return w;
    }

    @Override
    public boolean create(Work work) {
        String sql = "INSERT INTO Work (personID, productID, toMake, hoursWorked, finishDate) VALUES (?, ?, ?, ?, ?)";
        String act = work.isActive() ? "T" : "F";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, work.getPerson().getId(), work.getProduct().getId(), work.getToMake(), work.getHoursWorked(), work.getFinishDate());
        ) {

            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0) {
                System.out.println("Work has been created");
                Database.setLastGeneratedID(work, conn, "Work", "Work_ID");
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

    @Override
    public boolean update(Work work) {
        try(Connection conn = Database.getConnection();
            PreparedStatement statement = Database.prepareStatement(conn, "UPDATE WORK SET made = ? WHERE Work_ID = ?", work.getMade(), work.getId())){

            int affectedRows = statement.executeUpdate();

            if(affectedRows > 0) {
                System.out.println("WORK HAS BEEN UPDATED");
            }
            else {
                System.out.println("SOMETHING WENT WRONG WHEN UPDATING WORK");
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Work obj) {
        return false;
    }
}
