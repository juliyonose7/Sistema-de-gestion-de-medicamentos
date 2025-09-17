// importaciones para manejo de listas, fechas y timestamps de bases de datos
import java.util.List;
import java.util.Arrays;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * clase modelo que representa un medicamento en el sistema de farmacia
 * encapsula todas las propiedades y comportamientos de un medicamento
 * soporta múltiples formatos de inicialización (xml y mysql)
 */
public class Medicamento {
    
    // ===================== atributos principales del medicamento =====================
    
    private String nombre;              // nombre comercial o genérico del medicamento
    private String tipo;                // categoría farmacológica (analgésico, antibiótico, etc.)
    private int cantidad;               // unidades disponibles en inventario
    private String distribuidor;        // empresa distribuidora (cofarma, empsephar, cemefar)
    private List<String> sucursales;    // lista de sucursales donde está disponible
    private String fecha;               // fecha de registro en formato legible
    
    /**
     * constructor principal para inicialización completa desde xml
     * @param nombre nombre del medicamento
     * @param tipo categoría farmacológica
     * @param cantidad unidades en inventario
     * @param distribuidor empresa distribuidora
     * @param sucursales lista de sucursales disponibles
     * @param fecha fecha de registro como string
     */
    public Medicamento(String nombre, String tipo, int cantidad, String distribuidor, List<String> sucursales, String fecha) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.distribuidor = distribuidor;
        this.sucursales = sucursales;
        this.fecha = fecha;
    }
    
    /**
     * constructor especializado para datos provenientes de mysql
     * convierte timestamp a formato string y procesa sucursales concatenadas
     * @param nombre nombre del medicamento
     * @param tipo categoría farmacológica
     * @param cantidad unidades en inventario
     * @param distribuidor empresa distribuidora
     * @param sucursalesString sucursales como string separado por comas
     * @param fecha timestamp de mysql
     */
    public Medicamento(String nombre, String tipo, int cantidad, String distribuidor, String sucursalesString, Timestamp fecha) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.distribuidor = distribuidor;
        // convertir string de sucursales separadas por comas a lista
        this.sucursales = Arrays.asList(sucursalesString.split(", "));
        // formatear timestamp a string legible
        this.fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
    }
    
    // getters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public String getDistribuidor() { return distribuidor; }
    public List<String> getSucursales() { return sucursales; }
    public String getFecha() { return fecha;     }
    
    // setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setDistribuidor(String distribuidor) { this.distribuidor = distribuidor; }
    public void setSucursales(List<String> sucursales) { this.sucursales = sucursales; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    @Override
    public String toString() {
        return String.format("Medicamento: %s (%s) - %d unidades - Distribuidor: %s - Fecha: %s", 
                           nombre, tipo, cantidad, distribuidor, fecha);
    }
    
    public String getSucursalesString() {
        if (sucursales == null || sucursales.isEmpty()) {
            return "Ninguna";
        }
        return String.join(", ", sucursales);
    }
}
