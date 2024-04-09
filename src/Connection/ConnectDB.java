package Connection;

import java.sql.*;

public class ConnectDB {
  private static final String url = "jdbc:mysql://172.18.0.2:3306/images";
  private static final String user = "root";
  private static final String password = "Guga1957!";

  private static Connection connect;

  public static Connection getConnection() throws SQLException {
    if (connect == null) {
      connect = DriverManager.getConnection(url, user, password);
      return connect;
    } else {
      return connect;
    }
  }
}
