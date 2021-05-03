import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private  Socket socket = null;
    private int serverPort = 8189;
    private List<ClientHendler> clients;


    public Server(int serverPort ) {
        this.serverPort = serverPort;
        this.clients = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Сервер запущен, ожидаем подключения " + serverPort);
            while (true){
                System.out.println("Ожидаем нового клиента");
                socket = serverSocket.accept();
                new ClientHendler(socket, this);
                System.out.println("Клиент поключился");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientHendler clientHendler){
        clients.add(clientHendler);

    }

    public void unsubscribe(ClientHendler clientHendler){
        clients.remove(clientHendler);

    }

    public void broadcastMessage(String message) throws IOException {
       for(ClientHendler clientHendler: clients){
           clientHendler.sendMessage(message);
       }
    }

    public boolean isNickBusy(String nickName){
        for(ClientHendler clientHendler: clients){
            if(clientHendler.getUserName().equals(nickName)){
                return true;
            }
        }
        return false;
    }

}
