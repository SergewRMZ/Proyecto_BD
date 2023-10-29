package controller;

import diagrambuilder.Triangulo;
import diagrambuilder.Figura;
import diagrambuilder.Entidad;
import diagrambuilder.Relacion;
import diagrambuilder.Atributo;
import diagrambuilder.AtributoDerivado;
import diagrambuilder.Conexion;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.stage.FileChooser;
import javafx.scene.layout.VBox;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;



import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InterfazGraficaController implements Initializable {

    @FXML private VBox contenedorVBox;
    
    @FXML private AnchorPane contenedor;
    @FXML private ScrollPane contenedorScroll;
    
    // Botones
    @FXML private Button btnEntidad;
    @FXML private Button btnEntidadDebil;
    @FXML private Button btnAtributo;
    @FXML private Button btnAtributoMulti;
    @FXML private Button btnRelacion;
    @FXML private Button btnRelacionDebil;
    @FXML private Button btnGE;
    @FXML private Button btnAtributoDerivado;
    
    @FXML private MenuButton configButton;
    @FXML private MenuItem btnGuardar;
    @FXML private Button btnVerificar;
    
    // Variable para contener el nodo padre de los botones diseñados
    @FXML private AnchorPane componentes;
    @FXML private FileChooser seleccionaDoc;
    @FXML private Pane contenedorBotones;
    @FXML private Label textoComponentes;
    
    /* Las  listas son estáticas para poder acceder a ellas mediante el nombre de la clase,
    ya que no requerimos de más instancias de interfaz */
    public static Figura figuraSeleccionada;
    private static List <Entidad> entidades = new ArrayList<>(); // Lista Entidades
    private static List <Entidad> entidadesDebiles = new ArrayList<>(); // Lista Entidades Atributos
    private static List <Atributo> atributos = new ArrayList<>(); // Lista Atributos
    private static List <Atributo> atributosMultivalor = new ArrayList<>(); // Lista Atributos Multivalor
    private static List <AtributoDerivado> atributosDerivados = new ArrayList<>(); // Lista de Atributos Derivados
    private static List <Relacion> relacionesDebiles = new ArrayList<>(); // Lista Relaciones 
    private static List <Relacion> relaciones = new ArrayList<>(); // Lista Relaciones
    private static List <Triangulo> triangulos = new ArrayList<>();
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/componentes.fxml"));
            componentes = loader.load();
            seleccionaDoc = new FileChooser();
            seleccionaDoc.setTitle("Guardar Diagrama");
            seleccionaDoc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

            
        }   catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    @FXML
    public void verificarDiagrama () {
        List <Conexion> conexiones = configuracionConectar.getListaConexiones();
        List <Conexion> entidadAtributo = new ArrayList <>(); // Almacenará las conexiones entre una entidad y una llave
        if (!conexiones.isEmpty()) {
            int flag = 0;
            for (Conexion conexion : conexiones) {
                Figura figura1 =  conexion.getFigura1();
                Figura figura2 =  conexion.getFigura2();
                if (figura1 instanceof Entidad && figura2 instanceof Entidad) {
                    InterfazGraficaController.mostrarError("Error de relación", 
                            "No se puede conectar dos entidades sin una relación de por medio, ha conectado " + figura1.getNombre() + " y " + figura2.getNombre());
                    flag++;
                    break;
                }
                
                if (figura1 instanceof Relacion && figura2 instanceof Relacion) {
                    InterfazGraficaController.mostrarError("Error de relación",
                            "No se pueden relacionar dos relaciones, ha conectado " + figura1.getNombre() + " y " + figura2.getNombre());
                    flag++;
                    break;
                }
                
                if (figura1 instanceof Atributo && figura2 instanceof Atributo) {
                    comprobarAtributos ((Atributo) figura1, (Atributo) figura2);
                    flag++;
                    break;
                }
           
            }
            
            if (flag == 0) {
                // Crea la ventana emergente (Alert)
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("¡Todo está correcto!");

                // Muestra la ventana emergente y espera a que el usuario la cierre
                alert.showAndWait();
            }
        }
    }
        
    protected void comprobarAtributos (Atributo atributo1, Atributo atributo2) {
        if (atributo1.getPrimaria() && atributo2.getPrimaria ()) {
            mostrarError("Error de relación de atributos", 
                    "No puede conectar dos atributos llave: " + atributo1.getNombre() + " y " + atributo2.getNombre());
        } 
        
        else if (atributo1.getSimple() && atributo2.getSimple()) {
            mostrarError("Error de relación de atributos", 
                    "No puede conectar dos atributos simple: " + atributo1.getNombre() + " y " + atributo2.getNombre());
        }
        
        else if (atributo1.getIsMulti() && atributo2.getIsMulti()) {
            mostrarError("Error de relación de atributos", 
                    "No puede conectar dos atributos multivalor: " + atributo1.getNombre() + " y " + atributo2.getNombre());
        }
        
        else if (atributo1.getForanea() && atributo2.getForanea()) {
            mostrarError("Error de relación de atributos", 
                    "No puede conectar dos atributos con llave foránea: " + atributo1.getNombre() + " y " + atributo2.getNombre());
        }
    }
    
    /**
     * Método para mostrar ventana de error de esta vista.
     * @param titulo: Titulo de la ventana
     * @param mensaje: Mensaje de error de la ventana.
     */
    public static void mostrarError (String titulo, String mensaje) {
       
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
 
    @FXML
    public void crearEntidad(ActionEvent event) {
        Entidad entidad = new Entidad(contenedor);
        entidad.crearFigura();
        
        if (entidad.getCreado()) {
            contenedor.getChildren().add(entidad.getFigura());
            entidades.add(entidad);
        }
    }
    
    @FXML
    public void crearEntidadDebil (ActionEvent event) {
        Entidad entidadDebil = new Entidad(contenedor, true);
        entidadDebil.crearFigura();
        
        if (entidadDebil.getCreado()) {
            contenedor.getChildren().add(entidadDebil.getFigura());
            entidadesDebiles.add(entidadDebil);
        }
    }
    
    @FXML
    public void crearAtributo(ActionEvent event) {
        Atributo atributo = new Atributo(contenedor);
        atributo.crearFigura();
        
        if (atributo.getAtributoCreado()) {
            contenedor.getChildren().add(atributo.getFigura());
            atributos.add(atributo);
        }
        
    }
    
    @FXML
    public void crearAtributoMulti (ActionEvent event) {
        Atributo atributoMulti = new Atributo(contenedor, true);
        atributoMulti.crearFigura();
        
        if (atributoMulti.getAtributoCreado()) {
            contenedor.getChildren().add(atributoMulti.getFigura());
            atributosMultivalor.add(atributoMulti);
        }
    }
    
    @FXML
    public void crearAtributoDerivado (ActionEvent event) {
        AtributoDerivado atributoDerivado = new AtributoDerivado(contenedor);
        atributoDerivado.crearFigura();
        
        if (atributoDerivado.getAtributoDerivadoCreado()) {
            System.out.println("Creando derivado");
            contenedor.getChildren().add(atributoDerivado.getFigura());
            atributosDerivados.add(atributoDerivado);
        }
    }
    
     
    @FXML
    public void crearRelacionDebil (ActionEvent event) {
        Relacion relacionDebil = new Relacion(contenedor, true);
        relacionDebil.crearFigura();
    
        if (relacionDebil.getRelacionCreada()) {
            contenedor.getChildren().add(relacionDebil.getFigura());
            relacionesDebiles.add(relacionDebil);
        }
    }
    
    @FXML
    public void crearRelacion (ActionEvent event) {
        Relacion relacion = new Relacion(contenedor);
        relacion.crearFigura();
        
        if (relacion.getRelacionCreada()) {
            contenedor.getChildren().add(relacion.getFigura());
            relaciones.add(relacion);
        }
    }
    
    @FXML
    public void crearTriangulo(ActionEvent event) {
        Triangulo triangulo = new Triangulo(contenedor);
        triangulo.crearFigura();
        
        if (triangulo.getTrianguloCreado()) {
            // Agregar la figura al contenedor
            contenedor.getChildren().add(triangulo.getFigura());
        }
    }
    
    /**
     * Carga la vista para poder conectar entidades, relaciones y atributos.
     * @param event 
     */
    @FXML
    public void cargarConfiguraciones (ActionEvent event) {
        WritableImage wImage = new WritableImage((int) this.contenedor.getWidth(), (int) this.contenedor.getHeight());
        this.contenedor.snapshot(new SnapshotParameters(), wImage);
        FileChooser sFileChooser = new FileChooser();
        sFileChooser.setTitle("Guardar Imagen");
        sFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de imagen", "*.png"));
        File archivoG = sFileChooser.showSaveDialog(contenedor.getScene().getWindow());
        
        if (archivoG != null) {
            try {
                // Convierte la imagen WritableImage en una instancia de BufferedImage
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(wImage, null);

                // Guarda la imagen en el archivo seleccionado
                ImageIO.write(bufferedImage, "png", archivoG);
            }
            
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    
    @FXML
    public void nuevoArchivo (ActionEvent event) {
        // Crear una alerta de confirmación
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Nuevo Archivo");
        alert.setHeaderText("¿Estás seguro de crear un nuevo archivo?");
        alert.setContentText("Se eliminará todo el contenido actual del contenedor.");

        // Obtener la respuesta del usuario
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        // Si el usuario confirma la acción, eliminar el contenido del contenedor
        if (result == ButtonType.OK) {
            contenedor.getChildren().clear();
            InterfazGraficaController.atributos.clear();
            InterfazGraficaController.atributosDerivados.clear();
            InterfazGraficaController.atributosMultivalor.clear();
            InterfazGraficaController.entidades.clear();
            InterfazGraficaController.entidadesDebiles.clear();
            InterfazGraficaController.relaciones.clear();
            InterfazGraficaController.relacionesDebiles.clear();
            InterfazGraficaController.triangulos.clear();
            configuracionConectar.conexiones.clear();
        }
    }
    
    /**
     * Método para cargar la vista donde tenemos las entidades, relaciones y atributos
     * que podemos conectar con la figura en la que el usuario da click en conectar 
     * @param figuraSeleccionada: Es la figura en la que se efectuó este evento.
     */
    @FXML
    public void cargarConfiguracionConectar (Figura figuraSeleccionada) throws IOException {
        InterfazGraficaController.figuraSeleccionada = figuraSeleccionada;
        // Cargamos la vista y el controlador de la clase configuracionConectar
        FXMLLoader configuracionConectar = new FXMLLoader (getClass().getResource("/view/configuracionConectar.fxml"));
        Parent root = configuracionConectar.load();
        configuracionConectar controlador_configuracion = configuracionConectar.getController();
        
        // Se carga la escena
        Scene scene = new Scene (root);
        Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL); // Evita que el usuario interactúe con otras ventanas
        stage.setScene(scene); // Se establece la escena
        stage.setTitle("Relacionar");
        stage.showAndWait();
    }
    
    public void mostrarAtributos () {
        int i;
        for (i = 0; i < atributos.size(); i ++) {
            System.out.println(atributos.get(i).getNombreAtributo());
        }
    }
    
    public static List<Entidad> getListaEntidades () {
        return entidades;
    }
    
    public static List<Atributo> getListaAtributos () {
        return atributos;
    }
    
    public static List<Relacion> getListaRelaciones () {
        return relaciones;
    }
    
    public static List<AtributoDerivado> getListaAtributosDerivados () {
        return atributosDerivados;
    }
    
    public static List<Entidad> getListaEntidadesDebiles () {
        return entidadesDebiles;
    }
    
    public static List<Atributo> getListaAtributosMultivalor () {
        return atributosMultivalor;
    }
    
    public static List<Relacion> getListaRelacionesDebiles () {
        return relacionesDebiles;
    }
    
    /**
     * Función encargada de obtener los nombres de todos los atributos 
     * y los almacena en un arreglo de cadenas.
     * @return El arreglo de cadenas
     */
    public static String[] getArregloNombreAtributos () {
        List<String> nombreAtributos = new ArrayList<>();
        for (Atributo atributo:atributos) {
            nombreAtributos.add(atributo.getNombreAtributo());
        }
        
        for (Atributo atributo:atributosMultivalor) {
            nombreAtributos.add(atributo.getNombreAtributo());
        }
        
        for (AtributoDerivado atributo:atributosDerivados) {
            nombreAtributos.add(atributo.getNombreDerivado());
        }
        
        return nombreAtributos.toArray(String[]::new);
    }
    
    /**
     * Función encargada de obtener los nombres de todas las entidades y
     * los almacena en un arreglo de cadenas.
     * @return El arreglo de cadenas
     */
    public static String[] getArregloNombreEntidades () {
        List <String> nombreEntidades = new ArrayList<>();
        for (Entidad entidad : entidades) {
            nombreEntidades.add(entidad.getNombreEntidad());
        }
        
        for (Entidad entidadDebil : entidadesDebiles) {
            nombreEntidades.add(entidadDebil.getNombreEntidad());
        }
        
        return nombreEntidades.toArray(String[] :: new);
    }
    
    /**
     * Función encargada de obtener los nombres de todas las relaciones y
     * los almacena en un arreglo de cadenas.
     * @return El arreglo de cadenas
     */
    public static String[] getArregloNombreRelaciones(){
        List <String> nombreRelaciones = new ArrayList<>();
        for (Relacion relacion : relaciones) {
            nombreRelaciones.add(relacion.getNombreRelacion());
        }
        
        for (Relacion relacionDebil : relacionesDebiles) {
            nombreRelaciones.add(relacionDebil.getNombreRelacion());
        }
        
        return nombreRelaciones.toArray(String[] :: new);
    }
    
    public static Figura getFiguraSeleccionada () {
        return InterfazGraficaController.figuraSeleccionada;
    }
}