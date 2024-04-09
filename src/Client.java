import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
  public static void main(String[] args) throws UnknownHostException, IOException {
    Socket socket = new Socket("localhost", 8080);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    Scanner scanner = new Scanner(System.in);
  }
}