package diagrambuilder;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BuilderDiagramER extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargamos el archivo FXMLgetClass().getResource("interfazGrafica.fxml"));
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/interfaz.fxml"));
        primaryStage.setTitle("Diagramador");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}