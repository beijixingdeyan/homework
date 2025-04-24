import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // 数据库连接URL，用户名和密码
    private static final String URL = "jdbc:mysql://localhost:3306/xsgl";
    private static final String USER = "root";
    private static final String PASSWORD = "qweasd123456";

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

