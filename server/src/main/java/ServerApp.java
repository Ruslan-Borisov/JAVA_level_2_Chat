import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerApp {
    private static final int SERVER_PORT = 8189;
    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;


    public static void main(String[] args) {


        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Сервер запущен, ожидаем подключения...");
            socket = serverSocket.accept();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Клиент подключился");
            String msg;
            while (true) {
                msg = in.readUTF();
                out.writeUTF(msg);
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
