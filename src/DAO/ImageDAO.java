package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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

  public BufferedImage downloadImage(String imageName) {
    String sql = "SELECT img FROM images WHERE name_image = ?";

    try (PreparedStatement preparedStatement = ConnectDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setString(1, imageName);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        InputStream inputStream = resultSet.getBinaryStream("img");
        return ImageIO.read(inputStream);
      }
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public boolean deleteImage(String imageName) {
    String sql = "DELETE FROM images WHERE name_image = ?";

    try (PreparedStatement preparedStatement = ConnectDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setString(1, imageName);
      int rowsAffected = preparedStatement.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
