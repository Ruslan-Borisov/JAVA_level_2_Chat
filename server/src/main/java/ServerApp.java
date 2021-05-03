import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerApp {
    private static final int SERVER_PORT = 8189;



    public static void main(String[] args) {
        new Server(8189);



    }
}
