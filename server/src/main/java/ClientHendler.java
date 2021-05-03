import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHendler {
    private Server server;
    private Socket socket = null;
    private DataInputStream in;
    private DataOutputStream out;
    private String userName;

    public ClientHendler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (true) {
                    String msg = in.readUTF();
                    if(msg.startsWith("/login ")){
                        String userNameFromLogin = msg.split("\\s")[1];
                        if(server.isNickBusy(userNameFromLogin)){
                            sendMessage("/login_filed");
                            continue;
                        }

                        sendMessage("login_ok " + userName);
                        server.subscribe(this);
                        break;
                    }
                }

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

    public String getUserName() {
        return userName;
    }
}