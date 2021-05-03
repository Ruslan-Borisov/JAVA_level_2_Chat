import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHendler {
    private Server server;
    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;

    public ClientHendler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (true) {
                    String msg = in.readUTF();
                    out.writeUTF(msg);
                    server.broadcastMessage(msg);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }finally {
                disconnect();
            }
        }).start();

    }

    private void disconnect() {
        server.unsubscribe(this);

        if(in !=null){
            try {
                in.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if(out !=null){
            try {
                out.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        if(socket !=null){
            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    }
    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

}