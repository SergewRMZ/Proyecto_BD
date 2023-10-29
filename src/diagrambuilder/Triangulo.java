package diagrambuilder;

import controller.ERextendidoController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Triangulo extends Figura {
    // Atributos 
    public boolean creado = false;
    public boolean partial = false;
    public boolean total = false;
    public boolean disjunction =  false;
    public boolean overlap =  false;
    public Label label;
    
    
    // Constructor para generalización/especialización parcial
    public Triangulo (AnchorPane contenedor) {
        super(contenedor);
        label = (Label) super.componentes.lookup("#labelTipo");
    }
    
    @Override
    public void crearFigura () {
        super.figura = super.componentes.lookup("#contenedorGE");
        // Agregar los eventos de arrastrar y soltar a la figura
            
        figura.setOnMousePressed(this::onFiguraPressed);
        figura.setOnMouseDragged(this::onFiguraDragged);
        figura.setOnMouseEntered(this::ponerContorno);
        figura.setOnMouseExited(this::quitarContorno);
        super.menuRapido();
        try
        {
            this.cargarConfiguracion();
        } catch (IOException ex)
        {
            Logger.getLogger(Triangulo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarConfiguracion () throws IOException {
        FXMLLoader configuracionERX = new FXMLLoader (getClass().getResource("/view/ERextendido.fxml"));
        Parent root = configuracionERX.load();
        ERextendidoController controlador = configuracionERX.getController();
        
        controlador.setTriangulo(this);
        Scene scene = new Scene (root);
        Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setScene(scene);
        stage.setTitle("Especialización/Generalización");
        stage.showAndWait();
        
        // Obtener los valores
        this.partial = controlador.isParcial;
        this.total = controlador.isTotal;
        this.overlap = controlador.isTraslape;
        this.disjunction = controlador.isDisyuncion;
        this.creado = controlador.creado;
        
        if (this.overlap)
            this.label.setText("Is a t");
        else 
            this.label.setText("Is a d");
    }
    
    public boolean getTrianguloCreado () {
        return this.creado;
    }
    
 
}

   