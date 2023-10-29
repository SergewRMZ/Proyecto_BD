package controller;

import diagrambuilder.Figura;
import diagrambuilder.Entidad;
import diagrambuilder.Relacion;
import diagrambuilder.Atributo;
import diagrambuilder.AtributoDerivado;
import diagrambuilder.Conexion;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class configuracionConectar implements Initializable {
    @FXML public AnchorPane contenedorPrincipal;
    @FXML public VBox contenedorEntidad;
    @FXML public VBox contenedorAtributo;
    @FXML public VBox contenedorRelacion;
    @FXML public ChoiceBox<String> choiceBoxEntidades;
    @FXML public ChoiceBox<String> choiceBoxAtributos;
    @FXML public ChoiceBox<String> choiceBoxRelaciones;
    @FXML public Button btnConectar;
    @FXML public Label nombreEntidad;
    @FXML public RadioButton partParcial;
    @FXML public RadioButton partTotal;
    public boolean isParcial = false;
    public boolean isTotal = false;
    public static List<Conexion> conexiones = new ArrayList<>();
    
    List<Entidad> listaEntidades;
    List<Atributo> listaAtributos;
    List<Relacion> listaRelaciones;
    List<Entidad> listaEntidadesDebiles;
    List<Atributo> listaAtributosMultivalor;
    List<Relacion> listaRelacionesDebiles;
    List<AtributoDerivado> listaAtributosDerivados;
    
    String[] nombreEntidades;
    String[] nombreAtributos;
    String[] nombreRelaciones;
    
    public Figura figuraSeleccionada; 
    public int contadorChoice = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        this.nombreEntidades = InterfazGraficaController.getArregloNombreEntidades();
        this.nombreAtributos = InterfazGraficaController.getArregloNombreAtributos();
        this.nombreRelaciones = InterfazGraficaController.getArregloNombreRelaciones();
        
        this.listaEntidades = InterfazGraficaController.getListaEntidades();
        this.listaEntidadesDebiles = InterfazGraficaController.getListaEntidadesDebiles();
        this.listaAtributos = InterfazGraficaController.getListaAtributos();
        this.listaAtributosMultivalor = InterfazGraficaController.getListaAtributosMultivalor();
        this.listaAtributosDerivados = InterfazGraficaController.getListaAtributosDerivados();
        this.listaRelaciones = InterfazGraficaController.getListaRelaciones();
        this.listaRelacionesDebiles = InterfazGraficaController.getListaRelacionesDebiles();
        
        // Insertamos las listas de elementos que tenemos en el contenedos a los ChoiceBox.
        this.choiceBoxEntidades.getItems().addAll(nombreEntidades);
        this.choiceBoxAtributos.getItems().addAll(nombreAtributos);
        this.choiceBoxRelaciones.getItems().addAll(nombreRelaciones);
        
        // Agregando los eventos
        this.choiceBoxEntidades.setOnAction(event -> {verificarChoice(); });
        this.choiceBoxAtributos.setOnAction(event -> {verificarChoice(); });
        this.choiceBoxRelaciones.setOnAction(event -> {verificarChoice(); });
       
        this.btnConectar.setOnAction(event -> conectarFiguras());
        this.partParcial.setOnAction(event -> isParcial());
        this.partTotal.setOnAction(event -> isTotal());
        
        this.figuraSeleccionada = InterfazGraficaController.getFiguraSeleccionada();
        this.comprobarAtributo();
    }
    
    /**
     * Si la figura que se quiere conectar es un atributo entonces se deshabilita 
     * la participación total.
     */
    public void comprobarAtributo () {
        if (this.figuraSeleccionada instanceof Atributo || this.figuraSeleccionada instanceof AtributoDerivado) {
            partTotal.setDisable(true);
        }
    }
    
    /**
     * Método para corroborar que el usuario solo escoja una entidad,
     * un atributo o una relación.
     */
    @FXML
    public void verificarChoice() {
        this.contadorChoice++;
        if (this.contadorChoice > 1) {
            mostrarError("Error de Selección", "No puede seleccionar más de dos opciones. Se han restablecido los valores");
            reestablecerChoice();
        }
    }
    
    /**
     * Método para mostrar ventana de error de esta vista.
     * @param titulo: Titulo de la ventana
     * @param mensaje: Mensaje de error de la ventana.
     */
    public void mostrarError (String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Si el usuario escoge más de dos opciones de la vista, se muestra
     * una ventana de error y se reestablecen los valores.
     */
    public void reestablecerChoice () {
        // Restablecer valores
        this.contadorChoice = 0;
        choiceBoxEntidades.getSelectionModel().clearSelection();
        choiceBoxAtributos.getSelectionModel().clearSelection();
        choiceBoxRelaciones.getSelectionModel().clearSelection();
    }
    
    /**
     * Método encargado para conectar las figuras seleccionadas.
     */
    public void conectarFiguras() {
        if (this.partParcial.isSelected() || this.partTotal.isSelected()) { 
            Figura figuraSeleccionadaChoice = null;
            // Si la opción elegida es una entidad
            if (choiceBoxEntidades.getValue() != null) {
                String nombreEntidadSeleccionada = choiceBoxEntidades.getValue();

                for (Entidad entidad : listaEntidades) {
                    if (entidad.getNombreEntidad().equals(nombreEntidadSeleccionada)) {
                        figuraSeleccionadaChoice = entidad;  
                        break;
                    }
                }

                for (Entidad entidadDebil : listaEntidadesDebiles) {
                    if (entidadDebil.getNombreEntidad().equals(nombreEntidadSeleccionada)) {
                        figuraSeleccionadaChoice = entidadDebil;
                        break;
                    }
                } 

            }

            // Si la opción elegida es un atributo.
            else if (choiceBoxAtributos.getValue() != null) {
                String nombreAtributoSeleccionado = choiceBoxAtributos.getValue();

                for (Atributo atributo : listaAtributos) {
                    if (atributo.getNombreAtributo().equals(nombreAtributoSeleccionado)) {
                        figuraSeleccionadaChoice = atributo;
                        break;
                    }
                }

                for (Atributo atributoDebil : listaAtributosMultivalor) {
                    if (atributoDebil.getNombreAtributo().equals(nombreAtributoSeleccionado)) {
                        figuraSeleccionadaChoice = atributoDebil;
                        break;
                    }
                }

                for (AtributoDerivado atributo : listaAtributosDerivados) {
                    if (atributo.getNombreDerivado().equals(nombreAtributoSeleccionado)) {
                        figuraSeleccionadaChoice = atributo;
                        break;
                    }
                }

            }

            // Si la opción elegida es una relación.
            else if (choiceBoxRelaciones.getValue() != null) {
                String nombreRelacionSeleccionada = choiceBoxRelaciones.getValue();

                for (Relacion relacion : listaRelaciones) {
                    if (relacion.getNombreRelacion().equals(nombreRelacionSeleccionada)) {
                        figuraSeleccionadaChoice = relacion;
                        break;
                    }
                }

                for (Relacion relacionDebil : listaRelacionesDebiles) {
                    if (relacionDebil.getNombreRelacion().equals(nombreRelacionSeleccionada)) {
                        figuraSeleccionadaChoice = relacionDebil;
                        break;
                    }
                }
            }
            
            else {
                mostrarError("Error de selección de figura", "Necesita seleccionar una entidad, una relación o un atributo");
            }

            /**
             * Una vez encontrada la figura que el usuario escogió, tenemos 
             * que corroborar que no sea la misma y que la conexión entre estas
             * figuras exista.
             */
            if (figuraSeleccionadaChoice != null) {
                if (figuraSeleccionadaChoice == this.figuraSeleccionada)
                    mostrarError ("Error de Selección", "Estás intentando relacionar el mismo objeto");

                
                else {
                    int interruptor = 0;
                    for (Conexion conexion : conexiones) {
                        if (conexion.buscarFigura(figuraSeleccionadaChoice) != null && conexion.buscarFigura(this.figuraSeleccionada) != null) {
                            mostrarError("Error de relación", "La relación ya existe");
                            interruptor++;
                            break;
                        }
                    }
                    
                    if (figuraSeleccionadaChoice instanceof Atributo && this.figuraSeleccionada instanceof Atributo) {
                    Atributo atributo1 = (Atributo) figuraSeleccionadaChoice;
                    Atributo atributo2 = (Atributo) this.figuraSeleccionada;
                    
                    if(!(atributo1.getCompuesto() ^ atributo2.getCompuesto())) {
                       mostrarError("Error atributos", "Para conectar dos atributos, uno debe de ser compuesto"); 
                       interruptor++;
                    }
                }


                    if (interruptor == 0) {  
                        //Se crea la nueva conexión
                        Conexion nuevaConexion = new Conexion (figuraSeleccionadaChoice, this.figuraSeleccionada, isParcial, isTotal);

                        // Agregamos la nueva conexión al contenedor
                        figuraSeleccionadaChoice.contenedor.getChildren().add(nuevaConexion.getLinea());

                        // Colocamos las figuras encima de la línea.
                        figuraSeleccionadaChoice.getFigura().toFront();
                        this.figuraSeleccionada.getFigura().toFront();
                        nuevaConexion.getLinea().toBack();

                        // Agregamos una nueva conexión al arreglo que contiene todas las conexiones
                        conexiones.add(nuevaConexion);

                        // Cerrar la ventana
                        Stage stage = (Stage) contenedorPrincipal.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
        
        else {
            mostrarError ("Error", "Tiene que establecer si la relación es con participación parcial o participación total");
        }
    }
    
    
    public void isParcial () {
        if (partTotal.isSelected() == true) {
            partTotal.setSelected(false);
            isTotal = false;
        }
        
        isParcial = true;
    }
    
    public void isTotal () {
        if (partParcial.isSelected() == true) {
            partParcial.setSelected(false);
            isParcial = false;
        }
        
        isTotal = true;
    }
    
    /**
     * Función auxiliar para poder corroborar que las figuras se estén enlazando correctamente.
     */
    public static void mostrarConexiones () {
        for (Conexion conexion : conexiones) {
            System.out.println(conexion.toString());
        }
    }
    
    public static List<Conexion> getListaConexiones () {
        return conexiones;
    }
   
    public void setFiguraSeleccionada(Figura figura) {
        this.figuraSeleccionada = figura;
        System.out.println("Figura recibida");
    }
}
