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
}