/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dell
 */
public class ViewCardinalidadController implements Initializable {
    @FXML public AnchorPane contenedor;
    @FXML public Label nombreEnt1;
    @FXML public Label nombreEnt2;
    @FXML public RadioButton unoEnt1;
    @FXML public RadioButton unoEnt2;
    @FXML public RadioButton muchosEnt1;
    @FXML public RadioButton muchosEnt2;
    @FXML public Button btnAceptar;
    
    // Valores booleanos para almacenar la cardinalidad elegida
    public boolean isUnoEnt1;
    public boolean isMuchosEnt1;
    public boolean isUnoEnt2;
    public boolean isMuchosEnt2;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Se agregan los eventos
        unoEnt1.setOnAction(event -> isUnoEnt1());
        unoEnt2.setOnAction(event -> isUnoEnt2());
        muchosEnt1.setOnAction(event -> isMuchosEnt1());
        muchosEnt2.setOnAction(event -> isMuchosEnt2());
        
        btnAceptar.setOnAction (event -> guardarValores());
    }  
    
    public void guardarValores () {
        // En caso de que el usuario no seleccione ninguna opción.
        if (!unoEnt1.isSelected() && !unoEnt2.isSelected() && !muchosEnt1.isSelected() && !muchosEnt2.isSelected()) {
            mostrarError("Error al establecer la cardinalidad", "Necesita especificar la cardinalidad de la relación");
        }
        
        else if ((!unoEnt1.isSelected() && !muchosEnt1.isSelected()) || (!unoEnt2.isSelected() && !muchosEnt2.isSelected())) {
            mostrarError("Error al establecer la cardinalidad", "Necesita especificar la cardinalidad de ambas entidades");
        }
        
        else {
            this.isUnoEnt1 = this.unoEnt1.isSelected();
            this.isUnoEnt2 = this.unoEnt2.isSelected();
            this.isMuchosEnt1 = this.muchosEnt1.isSelected();
            this.isMuchosEnt2 = this.muchosEnt2.isSelected();
            Stage stage = (Stage) contenedor.getScene().getWindow();
            stage.close();
        }
    }
    
    /**
     * Método para mostrar ventana de error de esta vista.
     * @param titulo: Titulo de la ventana
     * @param mensaje: Mensaje de error de la ventana.
     */
    public void mostrarError (String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Evita que el usuario ingrese dos cardinalidades para la entidad 1.
     */
    public void isUnoEnt1 () {
        if (muchosEnt1.isSelected() == true)
            muchosEnt1.setSelected(false);
    }
    
    /**
     * Evita que el usuario ingrese dos cardinalidades para la entidad 1.
     */
    public void isMuchosEnt1 () {
        if (unoEnt1.isSelected() == true)
            unoEnt1.setSelected(false);
    }
    
    /**
     * Evita que el usuario ingrese dos cardinalidades para la entidad 2.
     */
    public void isUnoEnt2 () {
        if (muchosEnt2.isSelected() == true)
            muchosEnt2.setSelected(false);
    }
    
    /**
     * Evita que el usuario ingrese dos cardinalidades para la entidad 2.
     */
    public void isMuchosEnt2 () {
        if (unoEnt2.isSelected() == true)
            unoEnt2.setSelected(false);
    }
    
}
