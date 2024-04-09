import java.io.*;
import java.net.*;

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
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {

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
}
