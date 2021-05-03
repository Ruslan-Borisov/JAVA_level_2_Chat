import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Controller  {

    private  final int CLINT_PORT = 8189;
    private  final String CLINT_ADDR = "localhost";
    private  String userName;
    private  Socket socket ;
    private  DataInputStream in;
    private  DataOutputStream out;

    @FXML
    TextField msgField, userNameField;

    @FXML
    TextArea msgArea;

    @FXML
    HBox loginPanel, msgPanel;

    @FXML
    ListView<String> clientList;

    public void sendMsg(ActionEvent actionEvent) {
        String msg = msgField.getText() + '\n';
        try {
                out.writeUTF(msg);
                System.out.println(msg);
                msgField.clear();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void login(ActionEvent actionEvent) {
        if(socket == null||socket.isClosed()){
            connect();
        }
        if(userNameField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Имя пользователя не может быть пустым", ButtonType.OK);
            alert.showAndWait();
        }
        try {
            out.writeUTF("/login " + userNameField.getText());
            System.out.println(userNameField.getText());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void  setUserName(String userName){
        this.userName = userName;
        if(userName !=null){
            loginPanel.setVisible(false);
            loginPanel.setManaged(false);
            msgPanel.setVisible(true);
            msgPanel.setManaged(true);
        }else {
            loginPanel.setVisible(true);
            loginPanel.setManaged(true);
            msgPanel.setVisible(false);
            msgPanel.setManaged(false);
        }
    }

    public void connect(){
        try {
            socket = new Socket( CLINT_ADDR,CLINT_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Thread dataThread = new Thread(()->{
            try {
                while(true){
                    String msg = in.readUTF();
                        if(msg.startsWith("/login_ok ")){
                            setUserName(msg.split("\\s")[1]);
                            break;
                        }
                    if(msg.startsWith("/login_filed ")){
                        String cause = msg.split("\\s",2)[1];
                        msgArea.appendText(cause + '\n');
                    }
                }

                while (true){
                    String msg = in.readUTF();
                    if(msg.startsWith("/")){
                        executeCommand(msg);
                       continue;
                    }
                    msgArea.appendText(msg);
                }

            } catch (IOException exception) {
                exception.printStackTrace();
            }finally {
                disconnect();
            }
        });dataThread.start();
    }
    private void executeCommand(String cmd){

        if(cmd.startsWith("/clients_list ")){
            String[] token = cmd.split("\\s");
            Platform.runLater(() ->{
                clientList.getItems().clear();
                for(int i=1; i<token.length; i++){
                    clientList.getItems().add(token[i]);
                }
            });
        }
    }

    private void disconnect() {

        setUserName(null);
        if(socket !=null){
            try {
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    }

}
