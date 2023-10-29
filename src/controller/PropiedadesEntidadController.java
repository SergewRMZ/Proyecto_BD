package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PropiedadesEntidadController implements Initializable {
    @FXML public AnchorPane propiedades;
    @FXML public Label nombre;
    @FXML public TextField campoNombre;
    @FXML public Button btnCrear;
    public boolean creado = false;
    public String nombreFigura; // Este valor seá modificado por la clase, almacena si es una entidad o una relación
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCrear.setOnAction(e -> guardarValores()); // Agregamos el evento a btnCrear
        propiedades.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                guardarValores();
            }
        });
    }    
    
    public void guardarValores () {
        String nombre = campoNombre.getText();
        
        // En caso de que el campo de texto esté vacío
        
        if (nombre.isEmpty()) {
            // Construimos la ventana del error 
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle ("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Error: Ingrese un nombre en el campo texto");
            alert.showAndWait();
        }
        
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Información");
            alert.setContentText(nombreFigura + " creada correctamente");
            alert.showAndWait();
            creado = true; // Para verificar que el atributo se creó correctamente
            
            Stage stage = (Stage) propiedades.getScene().getWindow();
            stage.close();
        }
    }
}
