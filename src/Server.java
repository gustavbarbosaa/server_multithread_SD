import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import DAO.ImageDAO;
import model.Image;

public class Server implements Runnable {
  public Socket client;

  public Server(Socket cli) {
    this.client = cli;
  }

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(8080)) {
      while (true) {
        Socket socket = serverSocket.accept();
        Server server = new Server(socket);
        Thread thread = new Thread(server);
        thread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());

    try (PrintWriter out = new PrintWriter(client.getOutputStream(), true);
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

          sendMessageClient(out);

          String msg = in.readLine();

          if (msg.equals("1")) {
            msg = "1 - Download";
            out.println(msg);
          } else if (msg.equals("2")) {
            msg = "2 - Upload";
            out.println(msg);
          } else if (msg.equals("3")) {
            msg = "3 - Delete";
            out.println(msg);
          }

          System.out.println("Cliente -> " + msg);

          verifyResponse(msg, client);
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new UnsupportedOperationException("Unimplemented method 'run'");
  }

  private void sendMessageClient(PrintWriter out) {
    out.println("1 - Download");
    out.println("2 - Upload");
    out.println("3 - Delete");
    out.println("Escolha uma das opcoes acima: ");
    out.println("FIM");
  }

  private void verifyResponse(String msg, Socket client) {
  
  
  
    switch (msg) {
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
  }

  private void uploadServer(Socket socket) {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      String imageName = in.readLine();

      if (imageName != null && !imageName.isEmpty()) {
        BufferedImage image = ImageIO.read(socket.getInputStream());

        if (image != null) {
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          ImageIO.write(image, "png", byteArrayOutputStream);
          byte[] imageData = byteArrayOutputStream.toByteArray();

          ImageDAO imageDAO = new ImageDAO();
          imageDAO.uploadImage(new Image(imageName, imageData));

          File path = new File("imagem_" + System.currentTimeMillis() + ".png");
          ImageIO.write(image, "png", path);
          System.out.println("Imagem salva como: " + path.getAbsolutePath());
      } else {
          System.out.println("Imagem recebida é nula ou inválida.");
      }
      } else {
        System.out.println("Nome da imagem recebido é nulo ou vazio!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

