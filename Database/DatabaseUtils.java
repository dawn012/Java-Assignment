package Database;

import java.sql.*;

public class DatabaseUtils {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/cinema";
    private static final String DB_UNAME = "root";
    private static final String DB_PSD = "{M8246+qpzmS}";

    // 不能创建对象
    private DatabaseUtils() {
    }

    private static Connection getConnection() throws SQLException {
        // 获取连接
        return DriverManager.getConnection(DB_URL, DB_UNAME, DB_PSD);
    }

    public static ResultSet selectQuery(String selectedThing, String tableName, String idColumn, Object... params) throws SQLException {
        try {
            Connection conn = getConnection();

            String sql;
            PreparedStatement stmt;

            if (params == null) {
                // Select statement without ID
                sql = "SELECT " + selectedThing + " FROM " + tableName;
                stmt = conn.prepareStatement(sql);
            }
            else {
                // Select statement by ID
                sql = "SELECT " + selectedThing + " FROM " + tableName + " WHERE " + idColumn;
                stmt = conn.prepareStatement(sql);
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);  // 预处理语句中的参数索引从 1 开始，所以在循环中使用 i + 1 来为每个参数设置对应的索引
                }
            }

            return stmt.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing SELECT query: " + e.getMessage());
        }
    }

    public static ResultSet select(String sql, Object... params) throws SQLException {
        try {
            Connection conn = getConnection();

            PreparedStatement stmt;

            if (params == null) {
                // Select statement without ID
                stmt = conn.prepareStatement(sql);
            }
            else {
                // Select statement by ID
                stmt = conn.prepareStatement(sql);
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);  // 预处理语句中的参数索引从 1 开始，所以在循环中使用 i + 1 来为每个参数设置对应的索引
                }
            }

            return stmt.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing SELECT query: " + e.getMessage());
        }
    }

    public static int insertQuery(String sql, Object... params) throws SQLException {
        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);  // 预处理语句中的参数索引从 1 开始，所以在循环中使用 i + 1 来为每个参数设置对应的索引
            }
            return stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing INSERT query: " + e.getMessage());
        }
    }

    public static int updateQuery(String sql, Object... params) throws SQLException {
        try {
            Connection conn = getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);  // 预处理语句中的参数索引从 1 开始，所以在循环中使用 i + 1 来为每个参数设置对应的索引
            }
            return stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing UPDATE query: " + e.getMessage());
        }
    }

    public static int delectQuery(String tableName, String statusColumnName, String idColumn, Object... params) throws SQLException {
        try {
            Connection conn = getConnection();

            String sql = "UPDATE " + tableName + " SET " + statusColumnName + " = 0 WHERE " + idColumn + " = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);  // 预处理语句中的参数索引从 1 开始，所以在循环中使用 i + 1 来为每个参数设置对应的索引
            }
            return stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing DELETE query: " + e.getMessage());
        }
    }
}