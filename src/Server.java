import java.io.*;
import java.net.*;
import java.util.Map;
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

  private void sendImageNames(PrintWriter out) {
    ImageDAO imageDAO = new ImageDAO();
    Map<Integer, String> images = imageDAO.getAllImages();

    for (Map.Entry<Integer, String> entry : images.entrySet()) {
      Integer key = entry.getKey();
      String value = entry.getValue();
      out.println("ID: " + key + ", NOME: " + value);
    }
    out.println("FIM");
}

  private void verifyResponse(String msg, Socket client) {
    switch (msg) {
      case "1 - Download":
          try {
            sendImageNames(new PrintWriter(client.getOutputStream(), true));

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String stringImageID = in.readLine();

            int imageId = Integer.parseInt(stringImageID);

            System.out.println(imageId);

            downloadServer(client, imageId);
            break;
        } catch (IOException e) {
            e.printStackTrace();
        }
      case "2 - Upload":
        uploadServer(client);
        break;
      case "3 - Delete":
        try {
          sendImageNames(new PrintWriter(client.getOutputStream(), true));
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          String stringImageID = in.readLine();
          int imageID = Integer.parseInt(stringImageID);
          deleteServer(client, imageID);
          break;
        } catch (IOException e) {
            e.printStackTrace();
        }
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
          imageDAO.uploadImage(new Image("imagem_" + System.currentTimeMillis(), imageData));
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

  private void downloadServer(Socket socket, int imageId) {
    try {
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());

      ImageDAO imageDAO = new ImageDAO();
      BufferedImage image = imageDAO.downloadImage(imageId);

      if (image != null) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] imageData = byteArrayOutputStream.toByteArray();
        
        out.writeInt(imageData.length);
        out.write(imageData);
        
        System.out.println("Imagem enviada para o cliente: " + imageId);
      } else {
        out.writeInt(-1);
        System.out.println("Imagem não encontrada.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void deleteServer(Socket socket, int imageID) {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String stringImageID = in.readLine();
      imageID = Integer.parseInt(stringImageID);

      System.out.println("ID da imagem: " + imageID);

      File imageFile = null;
      String imagePath = "";
      ImageDAO imageDAO = new ImageDAO();
      Map<Integer, String> images = imageDAO.getAllImages();
  
      for (Map.Entry<Integer, String> entry : images.entrySet()) {
        if (imageID == entry.getKey()) {
          imagePath = "src\\assets\\" + entry.getValue() + ".png";
        }
      }
      System.out.println(imagePath);
      imageFile = new File(imagePath);
      System.out.println("Criado o imageFile");

      if (imageFile.exists()) {
        boolean deleted = imageFile.delete();
        if (deleted) {
            out.println("Imagem de id " + imageID + " deletada com sucesso.");
            if (imageDAO.deleteImage(imageID)) {
                System.out.println("Imagem de id " + imageID + " deletada do banco de dados.");
            } else {
                System.out.println("Erro ao deletar a imagem " + imageID + " do banco de dados.");
            }
        } else {
            out.println("Falha ao deletar a imagem " + imageID + ".");
        }
      } else {
        out.println("A imagem " + imageID + " não existe.");
    }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

