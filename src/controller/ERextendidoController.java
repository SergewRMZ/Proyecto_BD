package controller;

import diagrambuilder.Conexion;
import diagrambuilder.Entidad;
import diagrambuilder.Figura;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Serge
 */
public class ERextendidoController implements Initializable {
    @FXML public AnchorPane contenedor;
    @FXML public RadioButton partParcial;
    @FXML public RadioButton partTotal;
    @FXML public RadioButton disyuncion;
    @FXML public RadioButton traslape;
    @FXML public Button btnAceptar;
    @FXML public ChoiceBox<String> choiceEntidades;
    
    public String[] nombreEntidades;
    private List<Entidad> listaEntidades;
    private List<Entidad> listaEntidadesDebiles;
    public boolean isParcial;
    public boolean isTotal;
    public boolean isDisyuncion;
    public boolean isTraslape;
    public boolean creado = false;
    public Figura triangulo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Se obtienen las listas de Entidades
        this.nombreEntidades = InterfazGraficaController.getArregloNombreEntidades();
        this.listaEntidades = InterfazGraficaController.getListaEntidades();
        this.listaEntidadesDebiles = InterfazGraficaController.getListaEntidadesDebiles();
        
        // Se asignan los eventos a los radio button de la vista
        this.partParcial.setOnAction(event -> this.isParcial());
        this.partTotal.setOnAction(event -> this.isTotal());
        this.disyuncion.setOnAction(event -> this.isDisyuncion());
        this.traslape.setOnAction(event -> this.isTraslape());
        btnAceptar.setOnAction(event -> guardarValores());
        
        
        // Establecemos la primera opción y asignamos los valores de cadenas
        this.choiceEntidades.getItems().add("Entidad Padre");
        this.choiceEntidades.getItems().addAll(this.nombreEntidades);
        this.choiceEntidades.getSelectionModel().selectFirst();
        
    }   
    
    public void guardarValores() {
        if (this.choiceEntidades.getValue() == null || this.choiceEntidades.getValue().equals("Entidad Padre")) {
            mostrarError("Error de selección", "Necesita seleccionar cuál será la entidad padre");
        }
        
        else if (!this.partParcial.isSelected() &&
                !this.partTotal.isSelected() &&
                !this.disyuncion.isSelected() &&
                !this.traslape.isSelected()) {
            
            mostrarError ("Error", "Necesita especificar el tipo de generalización/especialización");
        }
        
        else if ((!this.partParcial.isSelected() && !this.partTotal.isSelected()) 
                    || (!this.disyuncion.isSelected() && !this.traslape.isSelected())) {
            mostrarError ("Error", "Necesita especificar el tipo de generalización/especialización");
        }
        
        else {
            // Se guardan los valores de los radio button
            this.isDisyuncion = disyuncion.isSelected();
            this.isTraslape = traslape.isSelected();
            this.isParcial = partParcial.isSelected();
            this.isTotal = partTotal.isSelected();
            
            // Se llama la función para conectar con el padre
            this.conectarPadre();
            
            this.creado = true;
            Stage stage = (Stage) contenedor.getScene().getWindow();
            stage.close();
        }
    }
    
    public void conectarPadre () {
        String entidadPadre = choiceEntidades.getValue();
        Figura figuraPadre = null;
        
        // Buscamos en los dos arreglos la entidad padre
        for (Entidad entidad : this.listaEntidades) {
            if (entidad.getNombreEntidad().equals(entidadPadre)) {
                figuraPadre = entidad;
                break;
            }
        }
        
        for (Entidad entidad : this.listaEntidadesDebiles) {
            if (entidad.getNombreEntidad().equals(entidadPadre)) {
                figuraPadre = entidad;
                break;
            }
        }
        
        
        // Una vez encontrada, establecemos la conexión
        if (figuraPadre != null) {
            // Obtenemos la lista que contiene las conexiones de las figuras
            List<Conexion> conexiones = configuracionConectar.getListaConexiones();
            
            Conexion nuevaConexion = new Conexion (this.triangulo, figuraPadre, this.isParcial, this.isTotal);
            // Agregamos la conexión al contenedor
            this.triangulo.contenedor.getChildren().add(nuevaConexion.getLinea());
            
            // Ponemos enfrente las figuras de la línea
            this.triangulo.getFigura().toFront();
            figuraPadre.getFigura().toFront();
            nuevaConexion.getLinea().toBack();
            
            // Agregamos la conexión a la lista
            conexiones.add(nuevaConexion);
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
    
    public void isParcial () {
        if (partTotal.isSelected() == true)
            partTotal.setSelected(false);
    }
    
    public void isTotal () {
        if (partParcial.isSelected() == true)
            partParcial.setSelected(false);
    }
    
    public void isDisyuncion () {
        if (traslape.isSelected() == true)
            traslape.setSelected(false);
    }
    
    public void isTraslape () {
        if (disyuncion.isSelected() == true)
            disyuncion.setSelected(false);
    }  
    
    public void setTriangulo (Figura triangulo) {
        this.triangulo = triangulo;
    }
}
