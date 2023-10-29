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
import javafx.scene.shape.Ellipse;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AtributoDerivado extends Figura {
    private TextField campoAtributoDerivado;
    private boolean creado = false;
    public AtributoDerivado(AnchorPane contenedor) {
        super(contenedor);
        campoAtributoDerivado = (TextField) super.componentes.lookup("#nombreAtributoD");     
    }
    
    @Override
    public void crearFigura () {
        super.figura = super.componentes.lookup("#contenedorAtributoDerivado");
        Ellipse elipse = (Ellipse) super.componentes.lookup("#figuraDerivado");
        elipse.setStrokeDashOffset(10);
        elipse.getStrokeDashArray().addAll(10.0, 10.0);
        super.figura.setOnMousePressed(this::onFiguraPressed);
        super.figura.setOnMouseDragged(this::onFiguraDragged);
        super.figura.setOnMouseEntered(this::ponerContorno);
        super.figura.setOnMouseExited(this::quitarContorno);
        super.menuRapido();
        super.obtenerTexto(campoAtributoDerivado); 
        try
        {
            this.mostrarVentana();
        } catch (IOException ex)
        {
            Logger.getLogger(AtributoDerivado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void mostrarVentana () throws IOException {
        FXMLLoader propiedades = new FXMLLoader(getClass().getResource("/view/propiedadesEntidad.fxml"));
        Parent root = propiedades.load();
        PropiedadesEntidadController controlador = propiedades.getController();
        controlador.nombreFigura = "Atributo Derivado"; 
        controlador.nombre.setText("Atributo Derivado"); // Le indicamos que va a ser una entidad
        Scene scene = new Scene (root);
        Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL); // Evita que el usuario interactúe con otras ventanas
        stage.setScene(scene); // Se establece la escena
        stage.setTitle("Nombre del Atributo Derivado");
        stage.showAndWait();
        
        // Obtenemos los valores
        campoAtributoDerivado.setText(controlador.campoNombre.getText());
        creado = controlador.creado;
        super.texto = campoAtributoDerivado.getText();
    }
    
    public String getNombreDerivado () {
        return super.getNombre();
    }
    
    public boolean getAtributoDerivadoCreado () {
        return this.creado;
    }

}
