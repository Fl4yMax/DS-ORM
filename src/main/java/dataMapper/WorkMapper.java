package dataMapper;

import applicationClasses.Work;
import dataMapper.IMapper.IDataMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        return null;
    }

    @Override
    public boolean create(Work work) {
        String sql = "INSERT INTO Work (personID, productID, toMake, finishDate) VALUES (?, ?, ?, ?)";
        String act = work.isActive() ? "T" : "F";
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = Database.prepareStatement(conn, sql, work.getPerson().getId(), work.getProduct().getId(), work.getToMake(), work.getFinishDate());
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
    public boolean update(Work obj) {
        return false;
    }

    @Override
    public boolean delete(Work obj) {
        return false;
    }
}
