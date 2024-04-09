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
    throw new UnsupportedOperationException("Unimplemented method 'run'");
  }
}
