package DAO;

import java.sql.PreparedStatement;

import Connection.ConnectDB;
import model.Image;

public class ImageDAO {
  public void uploadImage(Image image) {
    String sql = "INSERT INTO images (img, name_image) VALUES (?, ?)";

    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = ConnectDB.getConnection().prepareStatement(sql);
      preparedStatement.setBytes(1, image.getImage());
      preparedStatement.setString(2, image.getName());
      preparedStatement.executeUpdate();
      System.out.println("Imagem inserida no banco de dados!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
