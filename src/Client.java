import java.io.*;
import java.net.*;
import java.util.*;

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
  }

  public static void sendResponseServer(PrintWriter out, Scanner scanner) {
    System.out.println("Resposta para o servidor: ");
    String response = scanner.nextLine();
    out.println(response);
  }
}