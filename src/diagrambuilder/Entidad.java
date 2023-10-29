package diagrambuilder;

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

public class Entidad extends Figura {
    private boolean isDebil = false; // Para determinar si una entidad es débil
    private boolean creado = false; // Para ver si se ha creado correctamente
    private boolean isUno = false; 
    private boolean isMuchos = false;
    // Accediendo al textfield
    TextField campoEntidad;
    
    public Entidad (AnchorPane contenedor) {
        super(contenedor);
    }
    
    // Constructor para crear una entidad cuando esta sea debil
    public Entidad (AnchorPane contenedor, boolean isDebil) {
        super(contenedor);
        this.isDebil = isDebil;
    }
    
    @Override
        public void crearFigura() {
            if (isDebil) {
                super.figura = super.componentes.lookup("#contenedorEntidadDebil");
                campoEntidad = (TextField) super.componentes.lookup("#nombreEntidadDebil");
            }   else {
                super.figura = super.componentes.lookup("#contenedorEntidad");
                campoEntidad = (TextField) super.componentes.lookup("#nombreEntidad");
            }

            /* Función para mostrar la ventana que el usuario tiene que llenar */
            try
            {
                mostrarPropiedades();
            } catch (IOException ex)
            {
                Logger.getLogger(Entidad.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Agregar los eventos de arrastrar y soltar a la figura          
            figura.setOnMousePressed(this::onFiguraPressed);
            figura.setOnMouseDragged(this::onFiguraDragged);
            figura.setOnMouseEntered(this::ponerContorno);
            figura.setOnMouseExited(this::quitarContorno);
            menuRapido();
            obtenerTexto(campoEntidad); // Para escuchar los cambios en el campo de Texto
            figura.setOnMouseClicked(this::activarBotonEliminar);
        }
        
    protected void mostrarPropiedades () throws IOException {
        FXMLLoader propiedades = new FXMLLoader(getClass().getResource("/view/propiedadesEntidad.fxml"));
        Parent root = propiedades.load();
        PropiedadesEntidadController controlador = propiedades.getController();
        controlador.nombreFigura = "Entidad"; 
        controlador.nombre.setText("Entidad"); // Le indicamos que va a ser una entidad
        Scene scene = new Scene (root);
        Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL); // Evita que el usuario interactúe con otras ventanas
        stage.setScene(scene); // Se establece la escena
        stage.setTitle("Nombre de la Entidad");
        stage.showAndWait();
        
        // Obtenemos los valores
        campoEntidad.setText(controlador.campoNombre.getText());
        creado = controlador.creado;
        super.texto = campoEntidad.getText();
    }
     
    
    // Getters
    public String getNombreEntidad () {
        return getNombre();
    }
    
    public boolean getIsDebil () {
        return this.isDebil;
    }
    
    public boolean getCreado () {
        return this.creado;
    }
}

   