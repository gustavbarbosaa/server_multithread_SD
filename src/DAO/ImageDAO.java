package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

  public BufferedImage downloadImage(int imageId) {
    String sql = "SELECT img FROM images WHERE id_image = ?";

    try (PreparedStatement preparedStatement = ConnectDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setInt(1, imageId);
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

  public boolean deleteImage(int imageID) {
    String sql = "DELETE FROM images WHERE id_image = ?";

    try (PreparedStatement preparedStatement = ConnectDB.getConnection().prepareStatement(sql)) {
      preparedStatement.setInt(1, imageID);
      int rowsAffected = preparedStatement.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public Map<Integer, String> getAllImages() {

    Map<Integer, String> imageMap = new HashMap<Integer, String>();

    ArrayList<String> imageNames = new ArrayList<>();
    String sql = "SELECT id_image, name_image FROM images";

    try(PreparedStatement preparedStatement = ConnectDB.getConnection().prepareStatement(sql)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
          int id = resultSet.getInt("id_image");
          String name = resultSet.getString("name_image");
          imageMap.put(id, name);
          imageNames.add(name);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return imageMap;
  }
}
