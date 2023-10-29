package diagrambuilder;

import controller.PropiedadesAtributoController;
import controller.PropiedadesEntidadController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Atributo extends Figura {
    private boolean isMulti = false;
    private boolean creado = false; // Para ver si se ha creado correctamente
    TextField campoAtributo;
    
    // Propiedades de un atributo
    private boolean FK; // Llave Foránea
    private boolean PK; // Llave Primaria
    private boolean isSimple; // Atributo Simple
    private boolean isCompuesto; // Atirbuto Compuesto
    String nombreAtributo;
     
    public Atributo (AnchorPane contenedor) {
        super(contenedor);
        campoAtributo = (TextField) super.componentes.lookup("#nombreAtributo");
    }
    
    public Atributo (AnchorPane contenedor, boolean isMulti) {
        super(contenedor);
        this.isMulti = isMulti;
        campoAtributo = (TextField) super.componentes.lookup("#nombreAtributoM");
    }
     
    @Override
    public void crearFigura() {
        if (isMulti) {
            super.figura = super.componentes.lookup("#contenedorAtributoM");
            try
            {
                this.mostrarVentanaMultivalor();
            } catch (IOException ex)
            {
                Logger.getLogger(Atributo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        else {
            super.figura = super.componentes.lookup("#contenedorAtributo");  
            // Se muestra la ventana para determinar las propiedades de nuestro atributo
            try {
                mostrarPropiedades();
            }   catch (IOException ex) {
                Logger.getLogger(Atributo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
        figura.setOnMousePressed(this::onFiguraPressed);
        figura.setOnMouseDragged(this::onFiguraDragged);
        figura.setOnMouseEntered(this::ponerContorno);
        figura.setOnMouseExited(this::quitarContorno);
        super.menuRapido();
        super.obtenerTexto(campoAtributo); 
    }
    
    
    protected void mostrarPropiedades () throws IOException {
        FXMLLoader propiedadesAtributo = new FXMLLoader(getClass().getResource("/view/propiedadesAtributo.fxml"));
        Parent root = propiedadesAtributo.load();
        PropiedadesAtributoController controlador = propiedadesAtributo.getController();
        Scene scene = new Scene (root);
        Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL); // Evita que el usuario interactúe con otras ventanas
        stage.setScene(scene); // Se establece la escena
        stage.setTitle("Propiedades del Atributo");
        stage.showAndWait(); // Para que la ventana espere 
        
        
        // Obtenemos los valores
        PK = controlador.PK.isSelected();
        FK = controlador.FK.isSelected();
        isSimple = controlador.atributoSimple.isSelected();
        isCompuesto = controlador.atributoCompuesto.isSelected();
        campoAtributo.setText(controlador.nombreAtributo.getText());
        creado = controlador.atributoCreado();
        super.texto = controlador.nombreAtributo.getText();
        
        if (this.PK) {
            this.campoAtributo.setStyle("-fx-font-weight: bold;");
        }
    }
    
     protected void mostrarVentanaMultivalor () throws IOException {
        FXMLLoader propiedades = new FXMLLoader(getClass().getResource("/view/propiedadesEntidad.fxml"));
        Parent root = propiedades.load();
        PropiedadesEntidadController controlador = propiedades.getController();
        controlador.nombreFigura = "Atributo Multivalor"; 
        controlador.nombre.setText("Atributo Multivalor"); // Le indicamos que va a ser una entidad
        Scene scene = new Scene (root);
        Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL); // Evita que el usuario interactúe con otras ventanas
        stage.setScene(scene); // Se establece la escena
        stage.setTitle("Nombre del Atributo Multivalor");
        stage.showAndWait();
        
        // Obtenemos los valores
        campoAtributo.setText(controlador.campoNombre.getText());
        this.creado = controlador.creado;
        super.texto = campoAtributo.getText();
    }
    
    // Getters
    public boolean getPrimaria () {
        return this.PK;
    }
    
    public boolean getForanea () {
        return this.FK;
    }
    
    public boolean getCompuesto () {
        return this.isCompuesto;
    }
    
    public boolean getSimple () {
        return this.isSimple;
    }
     
    public String getNombreAtributo () {
        return super.getNombre();
    }
    
    public boolean getIsMulti() {
        return this.isMulti;
    }
    
    public boolean getAtributoCreado () {
        return this.creado;
    }
}

   