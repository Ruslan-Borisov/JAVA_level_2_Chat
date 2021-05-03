import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final int CLINT_PORT = 8189;
    private static final String CLINT_ADDR = "localhost";
    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;

    @FXML
    TextField msgField;

    @FXML
    TextArea msgArea;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    msgArea.appendText("" + msg);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });dataThread.start();
    }

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
}
