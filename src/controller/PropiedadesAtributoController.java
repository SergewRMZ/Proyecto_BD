package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PropiedadesAtributoController implements Initializable {

    @FXML VBox propiedadesAtributo;
    
    @FXML public TextField nombreAtributo;
    @FXML public RadioButton PK;
    @FXML public RadioButton FK;
    @FXML public  RadioButton atributoCompuesto;
    @FXML public RadioButton atributoSimple;
    @FXML public  Button btnAceptar;
    
    
    
    public boolean atributoCreado =  false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        propiedadesAtributo.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                guardarValores();
            }
        });
        
    }    

    // Evento asociado al botón aceptar
    public void guardarValores () {
        String nombre = this.nombreAtributo.getText();
        boolean isPrimaryKey =  this.PK.isSelected();
        boolean isForeignKey = this.FK.isSelected();
        boolean isCompuesto = this.atributoCompuesto.isSelected();
        boolean isSimple = this.atributoSimple.isSelected();
        
        /* Excepción lanzada en caso de que el usuario no asigne ningún nombre al atributo creado */
        if (nombre.isEmpty()) {
            // Construimos la ventana del error 
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle ("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Error: Ingrese un nombre al atributo y seleccione el tipo de atributo");
            alert.showAndWait();
        } 
        
        /* Excepción lanzada en caso de que el usuario no seleccione si el atrbuto es compuesto o simple */
        else if (!this.atributoCompuesto.isSelected() && !this.atributoSimple.isSelected()) {
         // Construimos la ventana del error 
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle ("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Error: Necesita indicar si el atributo es compuesto o simple");
            alert.showAndWait();
        }
        
        
        
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Información");
            alert.setContentText("Atributo creado correctamente");
            alert.showAndWait();
            atributoCreado = true; // Para verificar que el atributo se creó correctamente
            
            Stage stage = (Stage) propiedadesAtributo.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML
    public void isPrimary (ActionEvent event) {
        if (this.FK.isSelected() == true) {
            this.FK.setSelected(false); 
        }
    }
    
    @FXML
    public void isForeign (ActionEvent event) {
        if (this.PK.isSelected() == true) {
            this.PK.setSelected(false);
        }
    }
    
    public void isSimple (ActionEvent event) {
        if (this.atributoCompuesto.isSelected() == true) {
            this.atributoCompuesto.setSelected(false);
        }
    }
    
    @FXML
    public void isCompuesto (ActionEvent event) {
        if (this.atributoSimple.isSelected() == true) {
            this.atributoSimple.setSelected(false);
        }
    }  
    
    public boolean atributoCreado () {
        return atributoCreado;
    }
}
