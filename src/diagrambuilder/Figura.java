package diagrambuilder;
 
import controller.InterfazGraficaController;
import controller.configuracionConectar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public abstract class Figura {
        protected Node figura; // Atributo que almacena la referencia cualquuier nodo (Entidad, Atributo, etc.).
        protected DropShadow dropShadow; // Para efecto de contorno en las figuras
        protected double offsetX;
        protected double offsetY;
        public AnchorPane contenedor;
        protected AnchorPane componentes; // Componentes de otro archivo FXML donde están los diseños
        protected String texto = ""; // Se almacena el texto recuperado de un campo de texto
        
        /* Se recibe el contenedor para poder establecer los límites
        y así hacer que las figuras solo se muevan sobre el contenedor */
        public Figura (AnchorPane contenedor){
            this.contenedor = contenedor;
            
            // Se cargan los componentes donde se encuentran las entidades, relaciones, atributos
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/componentes.fxml"));
            componentes = loader.load();
            
            //  Creando el efecto de dropShadow para el contorno 
            dropShadow = new DropShadow();
            dropShadow.setColor (Color.BLUE);
            dropShadow.setRadius(10);

            } catch (IOException e) {
            e.printStackTrace();
            }
        }
        
        // Retorna el nodo que contiene la figura
        public Node getFigura() {
            return figura;
        }
        
        /**
         * Función encarga de crear la figura dependiendo en qué clase se llama esté método.
         */
        protected abstract void crearFigura();

        /**
         * Evento que detecta cuando el usuario presiona la figura y obtiene la coordenadas
         * @param event 
         */
        protected void onFiguraPressed(MouseEvent event) {
            // Calcular la diferencia entre la posición del clic y la posición actual de la figura
            offsetX = event.getSceneX() - figura.getLayoutX();
            offsetY = event.getSceneY() - figura.getLayoutY();
        }

        /**
         * Evento que detecta cuando el usuario está arrastrando la figura,
         * va actualizando las coordenadas
         * @param event 
         */
        protected void onFiguraDragged(MouseEvent event) {
            // Calcular la nueva posición de la figura teniendo en cuenta el desplazamiento del arrastre
            double newPosX = event.getSceneX() - offsetX;
            double newPosY = event.getSceneY() - offsetY;
            
            // Obtenemos los límites del contenedor
            double contenedorWidth = this.contenedor.getWidth();
            double contenedorHeight = this.contenedor.getHeight();
            
             // Limitar la posición en el eje X
            if (newPosX < 0) {
                newPosX = 0;
            } else if (newPosX > contenedorWidth) {
                newPosX = contenedorWidth - figura.getLayoutX();
            }

            // Limitar la posición en el eje Y
            if (newPosY < 0) {
                newPosY = 0;
            } else if (newPosY > contenedorHeight) {
                newPosY = contenedorHeight - figura.getLayoutY();
            }

            // Establecer la nueva posición de la figura
            figura.setLayoutX(newPosX);
            figura.setLayoutY(newPosY);
            
            // Obtenemos el ancho y alto de la figura
            double figuraWidth = figura.getBoundsInLocal().getWidth();
            double figuraHeight = figura.getBoundsInLocal().getHeight();
        }    
            
        /**
         * Evento que escucha los cambio que se realizar en el campo de texto
         * @param campo_texto: Es el campo de texto del cual se escucha el evento.
         */
        protected void obtenerTexto (TextField campo_texto) {
            campo_texto.setOnKeyPressed (event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    texto = campo_texto.getText();
                    System.out.println("El nombre ingresado es: " + texto);
                }
            });
        }
        
        protected void activarBotonEliminar (MouseEvent event) {
            
        }
        
        /**
         * Se encarga de mostrar la barra de herramientas al usuario
         * cuando da click derecho.
         */
        protected void menuRapido () {
            InterfazGraficaController interfazObject =  new InterfazGraficaController ();
            ContextMenu contextMenu = new ContextMenu();
            
            // Se crean los elementos del menú
            MenuItem eliminar = new MenuItem("Eliminar");
            MenuItem conectar = new MenuItem("Conectar");
            
            // Se agrega la acción para el evento "Eliminar"
            eliminar.setOnAction(e -> {
                this.eliminarFigura();
            });
            
            // Se agrega la acción para el evento "Conectar"
            conectar.setOnAction(e -> {
                try {                 
                    interfazObject.cargarConfiguracionConectar(this);
                } catch (IOException ex) {
                    Logger.getLogger(Figura.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            // Se agrega el item al menú
            contextMenu.getItems().addAll(eliminar, conectar);
            
            // Se muestra el menú contextual cuando se solicita en la figura
            this.figura.setOnContextMenuRequested(e -> {
                contextMenu.show(figura, e.getSceneX(), (e.getSceneY() - 50));
            });   
        }
        
        /**
         * Método utilizado para eliminar la figura de las listas. 
         * Se activa cuando el usuario acciona el botón eliminar, se remueve la figura
         * del scrollpane y posteriormente se remueve de la lista.
         */
        protected void eliminarFigura () {
            this.eliminarConexiones();
            
            this.contenedor.getChildren().remove(this.figura);
            if (this instanceof Entidad) {
                List<Entidad> listaEntidades = InterfazGraficaController.getListaEntidades();
                List<Entidad> listaEntidadesDebiles = InterfazGraficaController.getListaEntidadesDebiles();
                this.removerEntidad(listaEntidades);
                this.removerEntidad(listaEntidadesDebiles);
            }
            
            if (this instanceof Atributo) {
                List<Atributo> listaAtributos = InterfazGraficaController.getListaAtributos();
                List<Atributo> listaAtributosDebiles = InterfazGraficaController.getListaAtributosMultivalor();
                List<AtributoDerivado> listaAtributoDerivados = InterfazGraficaController.getListaAtributosDerivados();
                
                this.removerAtributoDerivado(listaAtributoDerivados);
                this.removerAtributo(listaAtributos);
                this.removerAtributo(listaAtributosDebiles);
            }
            
            if (this instanceof Relacion) {
                List<Relacion> listaRelaciones = InterfazGraficaController.getListaRelaciones();
                List<Relacion> listaRelacionesDebiles = InterfazGraficaController.getListaRelacionesDebiles();
                
                this.removerRelacion(listaRelaciones);
                this.removerRelacion(listaRelacionesDebiles);
            }
            
        }
        
        /**
         * Método para remover las conexiones que incluyen a la figura que 
         * queremos eliminar.
         */
        public void eliminarConexiones () {
            List <Conexion> conexiones = configuracionConectar.conexiones;
            List <Conexion> conexionEliminar = new ArrayList<>();
            
            for (Conexion conexion : conexiones) {
                if (conexion.getFigura1() == this || conexion.getFigura2() == this) {
                    conexion.removerLinea();
                    conexionEliminar.add(conexion);
                }
            }
            
            // Eliminar las conexiones
            conexiones.removeAll(conexionEliminar);
        }
        
        /**
         * Método usado para poder remover un atributo de la lista.
         * @param listaAtributos: Lista que contiene los atributos
         */
        public void removerAtributo (List<Atributo> listaAtributos) {
            for (Atributo atributo : listaAtributos) {
                if (atributo.equals(this)) {
                    listaAtributos.remove(this);
                    break;
                }
            }
        }
        
        /**
         * Método usado para poder remover un atributo derivado de la lista.
         * @param listaAtributosDerivados: Lista que contiene las entidades
         */
        public void removerAtributoDerivado (List<AtributoDerivado> listaAtributosDerivados) {
            for (AtributoDerivado atributo : listaAtributosDerivados) {
                if (atributo.equals(this)) {
                    listaAtributosDerivados.remove(this);
                    break;
                }
            }
        }
        
        /**
         * Método usado para poder remover una entidad de la lista.
         * @param listaEntidades: Lista que contiene las entidades
         */
        public void removerEntidad (List<Entidad> listaEntidades) {
            for (Entidad entidad : listaEntidades) {
                if (entidad.equals(this)) {
                    listaEntidades.remove(this);
                    break;
                }
            }
        }
        
        /**
         * Método usado para poder remover una relación de la lista.
         * @param listaRelaciones: Lista que contiene las relaciones
         */
        public void removerRelacion (List<Relacion> listaRelaciones) {
            for (Relacion relacion : listaRelaciones) {
                if (relacion.equals(this)) {
                    listaRelaciones.remove(this);
                    break;
                }
            }
        }
           
        /**
         * 
         * @return El nombre que se almacena en el campo de texto de cada figura 
         */
        public String getNombre () {
            return this.texto;
        }   
        
        /* Eventos para mostrar un contorno de color al posicionar el mouse sobre la figura */
        protected void ponerContorno (MouseEvent event) {
            figura.setEffect(dropShadow);
        }
        
        protected void quitarContorno (MouseEvent event) {
            figura.setEffect(null);
        }
    }