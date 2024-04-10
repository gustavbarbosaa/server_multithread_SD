import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

import DAO.ImageDAO;

public class Client {
  public static void main(String[] args) throws UnknownHostException, IOException {
    Socket socket = new Socket("localhost", 8080);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    Scanner scanner = new Scanner(System.in);

    String msg;

    while ((msg = in.readLine()) != null) {
      if (msg.equals("FIM")) {
        break;
      }
      System.out.println("Servidor -> " + msg);
    }

    sendResponseServer(out, scanner);

    String choice = in.readLine();

    switch (choice) {
      case "1 - Download":
          System.out.println("Imagens disponíveis:");
          while (!(msg = in.readLine()).equals("FIM")) {
              System.out.println(msg);
          }
          System.out.print("Digite o ID da imagem a ser baixada: ");
          int idImage = scanner.nextInt();

          out.println(idImage);

          downloadImageClient(socket, idImage);
          break;
      case "2 - Upload":
          System.out.print("Digite o nome da imagem a ser carregada: ");
          String nameToUpload = scanner.nextLine();
          uploadImageClient(socket, nameToUpload);
          break;
      case "3 - Delete":
          System.out.println("Imagens disponíveis:");
          while (!(msg = in.readLine()).equals("FIM")) {
              System.out.println(msg);
          }

          System.out.print("Digite o ID da imagem a ser excluída: ");
          String stringImageID = scanner.nextLine();
          int imageID = Integer.parseInt(stringImageID);

          out.println(imageID);

          deleteImageClient(socket, imageID);
          break;
      default:
          System.out.println("Opção inválida.");
  }
    socket.close();
  }

  public static void sendResponseServer(PrintWriter out, Scanner scanner) {
    System.out.println("Resposta para o servidor: ");
    String response = scanner.nextLine();
    out.println(response);
  }

  public static void uploadImageClient(Socket socket, String nameImage) {
    try {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(nameImage);

        BufferedImage image = ImageIO.read(new File("src/" + nameImage + ".png"));
        ByteArrayOutputStream arrayImage = new ByteArrayOutputStream();
        ImageIO.write(image, "png", arrayImage);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(arrayImage.toByteArray());
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public static void downloadImageClient(Socket socket, int idImage) {
    try {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(idImage);

        DataInputStream in = new DataInputStream(socket.getInputStream());

        int imageSize = in.readInt();

        if (imageSize != -1) {
            byte[] imageData = new byte[imageSize];
            in.readFully(imageData);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

            File path = null;
            ImageDAO imageDAO = new ImageDAO();
            Map<Integer, String> images = imageDAO.getAllImages();
      
            for (Map.Entry<Integer, String> entry : images.entrySet()) {
              if (idImage == entry.getKey()) {
                path = new File(entry.getValue());
              }
            }

            ImageIO.write(image, "png", path);
            System.out.println("Imagem salva como: " + path.getAbsolutePath());
        } else {
            System.out.println("Imagem não encontrada.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public static void deleteImageClient(Socket socket, int idImage) {
    try {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println(idImage); 

        String response = in.readLine();
        System.out.println(response);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}