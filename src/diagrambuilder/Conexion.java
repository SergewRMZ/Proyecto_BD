package diagrambuilder;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;

/**
 * Estructura de datos donde se almacenan las conexiones
 * entre los nodos. Además se tienen los métodos que 
 * actualizan las coordenadas cuando las figuras se desplazan 
 * en el contenedor
 * @author Serge Eduardo Martínez Ramírez
 */


public class Conexion {
    private Figura figura1;
    private Figura figura2;
    private Line linea;
    private boolean partParcial;
    private boolean partTotal;
    
    /**
     * El constructor de la clase Conexión.
     * @param figura1: Primer figura seleccionada.
     * @param figura2: Segunda figura seleccionada.
     * @param parcial: Valor boolean que nos indica si la participación es parcial.
     * @param total : Valor boolean que nos indica si la participación es total.
     */
    public Conexion (Figura figura1, Figura figura2, boolean parcial, boolean total) {
        this.figura1 = figura1;
        this.figura2 = figura2;
        this.partParcial = parcial;
        this.partTotal = total;
        
        // Se crea la nueva línea
        DoubleBinding startX = figura1.getFigura().layoutXProperty().add(figura1.getFigura().boundsInLocalProperty().get().getWidth() / 2);
        DoubleBinding startY = figura1.getFigura().layoutYProperty().add(figura1.getFigura().boundsInLocalProperty().get().getHeight() / 2);
        DoubleBinding endX = figura2.getFigura().layoutXProperty().add(figura2.getFigura().boundsInLocalProperty().get().getWidth() / 2);
        DoubleBinding endY = figura2.getFigura().layoutYProperty().add(figura2.getFigura().boundsInLocalProperty().get().getHeight() / 2);
        linea = new Line();
        linea.setStroke(Color.BLACK);
        if (this.partTotal) {
            linea.setStrokeWidth(3.0);
        }
        
        linea.setStrokeType(StrokeType.OUTSIDE);
        
        linea.startXProperty().bind(startX);
        linea.startYProperty().bind(startY);
        linea.endXProperty().bind(endX);
        linea.endYProperty().bind(endY);
    }
    
    public void actualizarCoordenadas() {
        double x_figura1 = figura1.getFigura().getLayoutX() + figura1.getFigura().getBoundsInLocal().getWidth() / 2;
        double y_figura1 = figura1.getFigura().getLayoutY() + figura1.getFigura().getBoundsInLocal().getHeight() / 2;
        double x_figura2 = figura2.getFigura().getLayoutX() + figura1.getFigura().getBoundsInLocal().getWidth() / 2;
        double y_figura2 = figura2.getFigura().getLayoutY() + figura2.getFigura().getBoundsInLocal().getHeight() / 2;
        
        linea.setStartX(x_figura1);
        linea.setStartY(y_figura1);
        linea.setEndX(x_figura2);
        linea.setEndY(y_figura2);
    }
    
    public Figura getFigura1 () {
        return this.figura1;
    }
    
    public Figura getFigura2 () {
        return this.figura2;
    }
    
    public boolean getPartParcial () {
        return this.partParcial;
    }
    
    public boolean getPartTotal () {
        return this.partTotal;
    }
    
    public Line getLinea () {
        return this.linea;
    }
    
    public Figura buscarFigura (Figura figura) {
        if (figura == this.figura1) {
            return this.figura1;
        }
        
        else if (figura == this.figura2) {
            return this.figura2;
        }
        
        else {
            return null;
        }
    }
    
    public void removerLinea () {
        this.figura1.contenedor.getChildren().remove(this.linea);
    }
    
    
    @Override
    public String toString () {
        return "Se ha conectado " + figura1.getNombre() + " y " + figura2.getNombre();
    }
}
