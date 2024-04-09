import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

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
        System.out.println("download");
        break;
      case "2 - Upload":
        System.out.println("upload");
        break;
      case "3 - Delete":
        System.out.println("delete");
        break;
      default:
        System.out.println("opcao invalida");
        break;
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

        System.out.println("Imagem enviada para o servidor com o nome: " + nameImage);
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public static void downloadImageClient(Socket socket, String nameImage) {
    try {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(nameImage);

        DataInputStream in = new DataInputStream(socket.getInputStream());

        int imageSize = in.readInt();

        if (imageSize != -1) {
            byte[] imageData = new byte[imageSize];
            in.readFully(imageData);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            File path = new File(nameImage + ".png");
            ImageIO.write(image, "png", path);
            System.out.println("Imagem salva como: " + path.getAbsolutePath());
        } else {
            System.out.println("Imagem não encontrada.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}