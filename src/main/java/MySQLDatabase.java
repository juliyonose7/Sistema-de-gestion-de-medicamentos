
import java.sql.*;
import java.util.*;
import java.io.*;
// importaciones necesarias para conexión jdbc, manejo de datos y archivos
/**
 * clase para manejo de persistencia en base de datos mysql
 * implementa patrón autocloseable para gestión automática de recursos
 * incluye reconexión automática, creación de base de datos y configuración flexible
 */
public class MySQLDatabase implements AutoCloseable {
    
    // ===================== configuración de conexión mysql =====================
    
    // parámetros de conexión con valores por defecto (sobreescribibles via mysql.properties)
    private String url = "jdbc:mysql://localhost:3306/drogueria_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private String username = "root";                    // usuario por defecto de mysql
    private String password = "";                        // contraseña vacía por seguridad inicial
    private int maxReconnectAttempts = 3;               // intentos máximos de reconexión
    private long reconnectBackoffMs = 1500L;            // tiempo de espera entre reintentos (ms)
    private boolean autoCreateDatabase = true;          // crear automáticamente la base si no existe

    // ===================== gestión de conexión y estado =====================
    
    private Connection connection;              // conexión activa a la base de datos
    private boolean tablaVerificada = false;   // flag para evitar verificaciones repetidas de tabla

    /**
     * constructor por defecto que utiliza configuración estándar
     */
    public MySQLDatabase() { 
        this(null); 
    }

    /**
     * constructor principal que permite sobreescribir configuración
     * @param overrides propiedades personalizadas para sobreescribir defaults
     */
    public MySQLDatabase(Properties overrides) {
        try {
            // cargar configuración desde archivo y aplicar overrides si existen
            cargarConfiguracion(overrides);
            
            // logging inicial para debug de conexión
            System.out.println("[mysql][init] driver: com.mysql.cj.jdbc.driver");
            System.out.println("[mysql][init] url objetivo: " + url);
            System.out.println("[mysql][init] usuario: " + username);
            
            // cargar driver jdbc de mysql
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // establecer conexión con sistema de reintentos automáticos
            conectarConReintentos();
            
            // verificar/crear estructura de tabla necesaria
            crearTablaSiNoExiste();
            
            System.out.println("[mysql] inicializado correctamente usando url=" + url);
        } catch (ClassNotFoundException e) {
            logError("no se pudo cargar el driver mysql (mysql-connector)", e);
        } catch (SQLException e) {
            logError("error sql inicializando conexión", e);
        } catch (Exception e) {
            logError("error inesperado inicializando mysqldatabase", e);
        }
    }

    /**
     * carga la configuración desde archivo mysql.properties y aplica overrides
     * permite personalización flexible de parámetros de conexión
     * @param overrides propiedades adicionales para sobreescribir configuración
     */
    private void cargarConfiguracion(Properties overrides) {
        Properties props = new Properties();
        File configFile = new File("mysql.properties");
        
        // intentar cargar configuración desde archivo si existe
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
                System.out.println("[mysql][cfg] cargado mysql.properties");
            } catch (IOException ex) {
                System.err.println("[mysql] no se pudo leer mysql.properties: " + ex.getMessage());
            }
        } else {
            System.out.println("[mysql][cfg] no existe mysql.properties, usando valores por defecto");
        }
        if (overrides != null) props.putAll(overrides);
        url = props.getProperty("mysql.url", url);
        username = props.getProperty("mysql.username", username);
        password = props.getProperty("mysql.password", password);
        maxReconnectAttempts = Integer.parseInt(props.getProperty("mysql.maxReconnectAttempts", String.valueOf(maxReconnectAttempts)));
        reconnectBackoffMs = Long.parseLong(props.getProperty("mysql.reconnectBackoffMs", String.valueOf(reconnectBackoffMs)));
        autoCreateDatabase = Boolean.parseBoolean(props.getProperty("mysql.autoCreateDatabase", String.valueOf(autoCreateDatabase)));
    }

    private void conectar() throws SQLException {
        System.out.println("[MySQL][CONNECT] Intentando conectar a: " + url);
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("[MySQL][CONNECT] Conexión establecida OK");
        } catch (SQLException e) {
            if (autoCreateDatabase && e.getMessage() != null && e.getMessage().contains("Unknown database")) {
                System.err.println("[MySQL][CONNECT] Base de datos no existe. Intentando crearla...");
                crearBaseDatosSiNoExiste();
                // reintentar
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("[MySQL][CONNECT] Conectado tras crear base de datos");
            } else {
                throw e;
            }
        }
    }

    private void crearBaseDatosSiNoExiste() {
        try {
            // Extraer nombre de la base de datos de la URL
            // Formato esperado: jdbc:mysql://host:puerto/nombre?...params
            String sinPrefix = url.substring("jdbc:mysql://".length());
            String afterHost = sinPrefix.substring(sinPrefix.indexOf('/') + 1);
            String dbName = afterHost.contains("?") ? afterHost.substring(0, afterHost.indexOf('?')) : afterHost;

            String urlSinDB = url.replace("/" + dbName, "/");
            if (urlSinDB.matches(".*/$")) {
                // ok
            }
            System.out.println("[MySQL][DB] Creando base de datos '" + dbName + "' usando URL: " + urlSinDB);
            try (Connection tmp = DriverManager.getConnection(urlSinDB, username, password); Statement st = tmp.createStatement()) {
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci");
                System.out.println("[MySQL][DB] Base de datos verificada/creada: " + dbName);
            }
        } catch (Exception ex) {
            System.err.println("[MySQL][DB] No se pudo crear la base de datos automáticamente: " + ex.getMessage());
        }
    }

    private void conectarConReintentos() throws SQLException {
        int intentos = 0;
        while (true) {
            try {
                conectar();
                break;
            } catch (SQLException e) {
                intentos++;
                if (intentos >= maxReconnectAttempts) throw e;
                System.err.println("[MySQL] Falló conexión intento " + intentos + ": " + e.getMessage() +
                        " -> reintentando en " + reconnectBackoffMs + "ms");
                try { Thread.sleep(reconnectBackoffMs); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }

    private void crearTablaSiNoExiste() throws SQLException {
        if (tablaVerificada) return;
        String sql = "CREATE TABLE IF NOT EXISTS medicamentos (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(255) NOT NULL," +
                "tipo VARCHAR(100) NOT NULL," +
                "cantidad INT NOT NULL," +
                "distribuidor VARCHAR(100) NOT NULL," +
                "sucursales TEXT NOT NULL," +
                "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "INDEX idx_nombre (nombre)," +
                "INDEX idx_tipo (tipo)," +
                "INDEX idx_distribuidor (distribuidor)," +
                "INDEX idx_fecha (fecha)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            tablaVerificada = true;
            System.out.println("[MySQL] Tabla 'medicamentos' lista");
        }
    }

    private void asegurarConexion() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("[MySQL][CHECK] Conexión nula o cerrada. Reintentando...");
                conectarConReintentos();
            }
        } catch (SQLException e) {
            logError("No se pudo reestablecer la conexión", e);
        }
    }

    public boolean agregarMedicamento(String nombre, String tipo, int cantidad, String distribuidor, List<String> sucursales) {
        asegurarConexion();
        String sql = "INSERT INTO medicamentos (nombre, tipo, cantidad, distribuidor, sucursales) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, tipo);
            pstmt.setInt(3, cantidad);
            pstmt.setString(4, distribuidor);
            pstmt.setString(5, String.join(", ", sucursales));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        System.out.println("[MySQL] Insertado medicamento id=" + idGenerado);
                    }
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logError("Error al agregar medicamento", e);
            return false;
        }
    }

    public List<Medicamento> obtenerTodosLosMedicamentos() {
        asegurarConexion();
        List<Medicamento> medicamentos = new ArrayList<>();
        String sql = "SELECT * FROM medicamentos ORDER BY fecha DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicamento med = new Medicamento(
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getInt("cantidad"),
                        rs.getString("distribuidor"),
                        rs.getString("sucursales"),
                        rs.getTimestamp("fecha")
                );
                medicamentos.add(med);
            }
        } catch (SQLException e) {
            logError("Error al obtener medicamentos", e);
        }

        return medicamentos;
    }

    public List<Medicamento> obtenerMedicamentosFiltrados(String tipo, String distribuidor) {
        asegurarConexion();
        List<Medicamento> medicamentos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM medicamentos WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (tipo != null && !tipo.equals("Todos los tipos")) {
            sql.append(" AND tipo = ?");
            params.add(tipo);
        }

        if (distribuidor != null && !distribuidor.equals("Todos los distribuidores")) {
            sql.append(" AND distribuidor = ?");
            params.add(distribuidor);
        }

        sql.append(" ORDER BY fecha DESC");

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Medicamento med = new Medicamento(
                            rs.getString("nombre"),
                            rs.getString("tipo"),
                            rs.getInt("cantidad"),
                            rs.getString("distribuidor"),
                            rs.getString("sucursales"),
                            rs.getTimestamp("fecha")
                    );
                    medicamentos.add(med);
                }
            }
        } catch (SQLException e) {
            logError("Error al obtener medicamentos filtrados", e);
        }

        return medicamentos;
    }

    public boolean eliminarMedicamento(String nombre, String fecha) {
        asegurarConexion();
        String sql = "DELETE FROM medicamentos WHERE nombre = ? AND DATE(fecha) = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, fecha);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logError("Error al eliminar medicamento", e);
            return false;
        }
    }

    public boolean eliminarMedicamentoPorId(int id) {
        asegurarConexion();
        String sql = "DELETE FROM medicamentos WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("Error al eliminar medicamento por id", e);
            return false;
        }
    }

    public boolean actualizarCantidad(int id, int nuevaCantidad) {
        asegurarConexion();
        String sql = "UPDATE medicamentos SET cantidad = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, nuevaCantidad);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("Error al actualizar cantidad", e);
            return false;
        }
    }

    public List<Medicamento> buscarPorNombre(String patron) {
        asegurarConexion();
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicamentos WHERE nombre LIKE ? ORDER BY fecha DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + patron + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Medicamento(
                            rs.getString("nombre"),
                            rs.getString("tipo"),
                            rs.getInt("cantidad"),
                            rs.getString("distribuidor"),
                            rs.getString("sucursales"),
                            rs.getTimestamp("fecha")
                    ));
                }
            }
        } catch (SQLException e) {
            logError("Error en búsqueda por nombre", e);
        }
        return lista;
    }

    public int obtenerTotalMedicamentos() {
        asegurarConexion();
        String sql = "SELECT COUNT(*) FROM medicamentos";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logError("Error al obtener total de medicamentos", e);
        }

        return 0;
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[MySQL] Conexión cerrada");
            }
        } catch (SQLException e) {
            logError("Error al cerrar conexión", e);
        }
    }

    public boolean isConectado() {
        try {
            if (connection == null) {
                System.out.println("[MySQL] connection es null");
                return false;
            }
            if (connection.isClosed()) {
                System.out.println("[MySQL] connection está cerrada");
                return false;
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.executeQuery("SELECT 1");
            }
            System.out.println("[MySQL] conexión verificada");
            return true;
        } catch (SQLException e) {
            logError("Error al verificar conexión", e);
            return false;
        }
    }

    public String obtenerEstadoConexion() {
        try {
            if (connection == null) return "NULL";
            if (connection.isClosed()) return "CERRADA";
            return "ABIERTA";
        } catch (SQLException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private void logError(String msg, Exception e) {
        System.err.println("[MySQL] " + msg + ": " + e.getMessage());
    }

    @Override
    public void close() {
        cerrarConexion();
    }
}
