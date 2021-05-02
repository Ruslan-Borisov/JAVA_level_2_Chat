import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("Вошли в start");
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        System.out.println("Вошли указали путь к sample.fxml");
        primaryStage.setTitle("Hello World");
        System.out.println("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        System.out.println("Размер экран");
        primaryStage.show();
        System.out.println("Активировали окно");

    }


    public static void main(String[] args) {

        launch(args);
    }
}
