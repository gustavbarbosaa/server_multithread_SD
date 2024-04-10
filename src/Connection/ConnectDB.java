package Connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConnectDB {
  private static Connection connect;

  public static Connection getConnection() throws SQLException {
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream("config.properties")) {
      properties.load(fis);
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String url = properties.getProperty("database.url");
    String user = properties.getProperty("database.user");
    String password = properties.getProperty("database.password");

    if (connect == null) {
      connect = DriverManager.getConnection(url, user, password);
      return connect;
    } else {
      return connect;
    }
  }
}
