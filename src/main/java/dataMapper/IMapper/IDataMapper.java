package dataMapper.IMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;

public interface IDataMapper<T>{
    T find(int id);
    boolean create(T obj);
    boolean update(T obj);
    boolean delete(T obj);

    class Database
    {
        public static Connection getConnection(){
            try {
                String url = "jdbc:oracle:thin:@dbsys.cs.vsb.cz:1521:oracle";
                String usr = "paz0042";
                String pass = "ohsf71ykOfCmWDsA";
                return DriverManager.getConnection(url, usr, pass);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException {
            if (conn == null) {
                throw new SQLException("Connection is null");
            }
            PreparedStatement statement;
            if (sql.trim().toLowerCase().startsWith("update")) {
                statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                statement = conn.prepareStatement(sql);
            }

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement;
        }

        public static void setLastGeneratedID(Object obj, Connection conn, String tableName, String idColumnName) {
            ResultSet rs = null;
            try(PreparedStatement statement = Database.prepareStatement(conn, "SELECT MAX(" + idColumnName + ") FROM " + tableName)) {

                rs = statement.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    Method setIdMethod = obj.getClass().getMethod("setId", int.class);
                    setIdMethod.invoke(obj, id);
                    if (id != 0) {
                        System.out.println("Generated ID: " + id);
                    } else {
                        System.out.println("Something went wrong");
                    }
                }
            } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            } finally {
                try {
                    if(rs != null) {
                        rs.close();
                    }
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }
}