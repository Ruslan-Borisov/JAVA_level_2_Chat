import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Controller  {

    private static final int CLINT_PORT = 8189;
    private static final String CLINT_ADDR = "localhost";
    private  String userName;
    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;

    @FXML
    TextField msgField, userNameField;

    @FXML
    TextArea msgArea;

    @FXML
    HBox loginPanel, msgPanel;




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
                    if(msg.startsWith("/")){
                        if(msg.startsWith("/Login_ok ")){
                            setUserName(msg.split("\\s")[1]);
                        }
                        continue;
                    }
                    msgArea.appendText("" + msg);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }finally {
                disconnect();
            }
        });dataThread.start();

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
