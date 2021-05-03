import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    private  Socket socket = null;
    private int serverPort = 8189;
    private List<ClientHendler> clients;


    public Server(int serverPort) {
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

    public synchronized void subscribe(ClientHendler clientHendler)  {
        clients.add(clientHendler);
        broadcastMessage("Клиент с ником: " + clientHendler.getUserName() + " подключился\n");
        broadcastClientList();

    }

    public synchronized void unsubscribe(ClientHendler clientHendler)  {
        clients.remove(clientHendler);
        broadcastMessage("Клиент с ником: " + clientHendler.getUserName() + " oтключился\n");
        broadcastClientList();

    }

    public synchronized void broadcastMessage(String message)  {
       for(ClientHendler clientHendler: clients){
           clientHendler.sendMessage(message);
       }
    }

    public synchronized boolean isUserOnline(String nickName){
        for(ClientHendler clientHendler: clients){
            if(clientHendler.getUserName().equals(nickName)){
                return true;
            }
        }
        return false;
    }

  public synchronized void sendPrivatMassage(ClientHendler sender, String receiverUserName, String message) {
    for(ClientHendler client: clients){
        if(client.getUserName().equals(receiverUserName)){
            client.sendMessage("От: " + sender.getUserName() + " Сообщение: " + message);
            sender.sendMessage("Пользователю: " + receiverUserName +" Сообщение: " + message);
            return;
        }
    }
     sender.sendMessage("невозможно отправить сообщение пользователю");
  }

  private  void broadcastClientList()  {
        StringBuilder stringBuilder = new StringBuilder("/clients_list ");
        for(ClientHendler client: clients){
            stringBuilder.append(client.getUserName()).append(" ");
        }
        stringBuilder.setLength(stringBuilder.length() -1);
       String clientList = stringBuilder.toString();
       for(ClientHendler clientHendler: clients){
           clientHendler.sendMessage(clientList);
       }

  }

}
