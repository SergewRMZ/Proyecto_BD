package diagrambuilder;

import controller.PropiedadesEntidadController;
import controller.ViewCardinalidadController;
import controller.configuracionConectar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Relacion extends Figura {
    private boolean isDebil = false;
    private boolean creado = false; // Para ver si se ha creado correctamente
    
    // Variables que almacenan el campo de texto de la relación y las cardinalidades.
    private TextField campoRelacion;
    private Label cardUno;
    private Label cardDos;
    
    // Variables para almacenar la cardinalidad de la relación
    private boolean IsUnoEntidadUno;
    private boolean IsUnoEntidadDos;
    private boolean IsMuchosEntidadUno;
    private boolean IsMuchosEntidadDos;
    
    public Relacion (AnchorPane contenedor) {
        super(contenedor);
        campoRelacion = (TextField) super.componentes.lookup("#nombreRelacion");
        
        // Obtenemos referencias a las etiquetas del label
        this.cardUno = (Label) this.componentes.lookup("#cardUno");
        this.cardDos = (Label) this.componentes.lookup("#cardDos");
    }
    
    public Relacion (AnchorPane contenedor, boolean isDebil) {
        super(contenedor);
        this.isDebil = isDebil;
        campoRelacion = (TextField) super.componentes.lookup("#nombreRelacionD");
        
        // Obtenemos referencias a las etiquetas del label de la relación débil
        this.cardUno = (Label) this.componentes.lookup("#cardUnoD");
        this.cardDos = (Label) this.componentes.lookup("#cardDosD");
    }
    
    @Override
    public void crearFigura() {
        if (this.isDebil) {
            super.figura = super.componentes.lookup("#contenedorRelacionD");
        }
            
        else {
            super.figura = super.componentes.lookup("#contenedorRelacion");
        }

        /* Función para mostrar la ventana que el usuario tiene que llenar */
        try {
            mostrarPropiedades();
            
            
        } catch (IOException ex) {
            Logger.getLogger(Entidad.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        // Agregar los eventos de arrastrar y soltar a la figura    
        figura.setOnMousePressed(this::onFiguraPressed);
        figura.setOnMouseDragged(this::onFiguraDragged);
        figura.setOnMouseEntered(this::ponerContorno);
        figura.setOnMouseExited(this::quitarContorno);
        menuRapido();
        obtenerTexto(campoRelacion);
       
        habilitarCardinalidad();
    }  
    
    protected void mostrarPropiedades () throws IOException {
        FXMLLoader propiedades = new FXMLLoader(getClass().getResource("/view/propiedadesEntidad.fxml"));
        Parent root = propiedades.load();
        PropiedadesEntidadController controlador = propiedades.getController();
        controlador.nombreFigura = "Relación"; 
        controlador.nombre.setText("Relación"); // Le indicamos que va a ser una entidad
        Scene scene = new Scene (root);
        Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL); // Evita que el usuario interactúe con otras ventanas
        stage.setScene(scene); // Se establece la escena
        stage.setTitle("Nombre de la Relación");
        stage.showAndWait();
        
        // Obtenemos los valores 
        campoRelacion.setText(controlador.campoNombre.getText());
        creado = controlador.creado;
        super.texto = controlador.campoNombre.getText();
    }
    
    public void habilitarCardinalidad () { 

        this.figura.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int contadorEntidades = 0;
                List<Conexion> conexiones = configuracionConectar.getListaConexiones();
                List<Figura> entidades = new ArrayList<>();
                for (Conexion conexion : conexiones) {
                    if ((conexion.getFigura1() == this && conexion.getFigura2() instanceof Entidad) || (conexion.getFigura2() == this && conexion.getFigura1() instanceof Entidad)) {
                        contadorEntidades++;
                        if (conexion.getFigura1() instanceof Entidad)
                            entidades.add(conexion.getFigura1());
                        else
                            entidades.add(conexion.getFigura2());
                    } 
                }
                
                if (contadorEntidades == 2) {
                    FXMLLoader cardinalidad = new FXMLLoader (getClass().getResource("/view/ViewCardinalidad.fxml"));
                    Parent root;
                    try {
                        
                        // Cargamos el FXML
                        root = cardinalidad.load();
                        // Cargamos el controlador de la vista.
                        ViewCardinalidadController controller = cardinalidad.getController();
                        controller.nombreEnt1.setText(entidades.get(0).getNombre());
                        controller.nombreEnt2.setText(entidades.get(1).getNombre());
                        Scene scene = new Scene (root);
                        Stage stage = new Stage ();
                        stage.initModality(Modality.APPLICATION_MODAL); // Evita que el usuario interactúe con otras ventanas
                        stage.setScene(scene); // Se establece la escena
                        stage.setTitle("Establecer Cardinalidad");
                        stage.showAndWait(); // Para que la ventana espere 
                        
                        
                        // Obtenemos los valores
                        this.IsUnoEntidadUno = controller.isUnoEnt1;
                        this.IsMuchosEntidadUno = controller.isMuchosEnt1;
                        this.IsUnoEntidadDos = controller.isUnoEnt2;
                        this.IsMuchosEntidadDos = controller.isMuchosEnt2;
                        
                        if (this.IsMuchosEntidadUno)
                            this.cardUno.setText("N");
                        else
                            this.cardUno.setText("1");
                        
                        if (this.IsMuchosEntidadUno && this.IsMuchosEntidadDos)
                            this.cardDos.setText("M");
                        
                        if (this.IsMuchosEntidadDos)
                            this.cardDos.setText("N");
                        
                        else
                            this.cardDos.setText("1");
                        
                    } catch (IOException ex) {
                        Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                }   
               
            }
        });        
    }
    
    // Getters
    public String getNombreRelacion () {
        return super.getNombre();
    }
    
    public boolean getRelacionCreada () {
        return this.creado;
    } 
}

   