# Sistema de Droguería con MySQL

## 🚀 Instalación y Configuración

### Requisitos Previos
1. **Java 8 o superior**
2. **MySQL Server 5.7 o superior**
3. **MySQL Connector/J** (driver JDBC)

### Pasos de Instalación

#### 1. Instalar MySQL
- Descargar e instalar MySQL Server desde: https://dev.mysql.com/downloads/mysql/
- Durante la instalación, configurar una contraseña para el usuario `root`
- Asegurarse de que el servicio MySQL esté ejecutándose

#### 2. Crear la Base de Datos
```sql
-- Ejecutar en MySQL Workbench o línea de comandos
mysql -u root -p < crear_base_datos.sql
```

#### 3. Configurar la Conexión
Editar el archivo `MySQLDatabase.java` y actualizar las credenciales:
```java
private static final String URL = "jdbc:mysql://localhost:3306/drogueria_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "tu_contraseña_aqui"; // Cambiar por tu contraseña
```

#### 4. Descargar MySQL Connector/J
- Descargar desde: https://dev.mysql.com/downloads/connector/j/
- Extraer el archivo `mysql-connector-java-x.x.x.jar`
- Agregar al classpath al compilar y ejecutar

#### 5. Compilar y Ejecutar
```bash
# Compilar con el driver de MySQL
javac -cp "mysql-connector-java-x.x.x.jar" *.java

# Ejecutar con el driver de MySQL
java -cp ".:mysql-connector-java-x.x.x.jar" SistemaPedidosFarmacia
```

## 📊 Características

### Base de Datos MySQL
- ✅ **Tabla optimizada** con índices para consultas rápidas
- ✅ **Filtros avanzados** por tipo y distribuidor
- ✅ **Eliminación segura** con confirmación
- ✅ **Estadísticas en tiempo real**

### Funcionalidades
- ✅ **Formulario de pedidos** con validación completa
- ✅ **Efectos neon interactivos** en todos los elementos
- ✅ **Interfaz responsive** que se adapta al tamaño de pantalla
- ✅ **Exportación XML** mantenida para compatibilidad
- ✅ **Gestión completa** de medicamentos

### Estructura de la Base de Datos
```sql
medicamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    distribuidor VARCHAR(100) NOT NULL,
    sucursales TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

## 🎨 Interfaz Moderna
- **Tema oscuro** con acentos verdes
- **Efectos neon** al hacer hover
- **Animaciones suaves** y profesionales
- **Layout responsive** para diferentes pantallas

## 🔧 Solución de Problemas

### Error de Conexión
- Verificar que MySQL esté ejecutándose
- Comprobar credenciales en `MySQLDatabase.java`
- Asegurarse de que la base de datos `drogueria_db` existe

### Error de Driver
- Verificar que `mysql-connector-java-x.x.x.jar` esté en el classpath
- Descargar la versión compatible con tu versión de MySQL

### Error de Compilación
- Verificar que todos los archivos `.java` estén en el mismo directorio
- Asegurarse de tener Java 8 o superior instalado

## 📝 Notas
- La función de exportar XML se mantiene para compatibilidad
- Los datos se almacenan exclusivamente en MySQL
- La aplicación es completamente responsive
- Los efectos neon funcionan en todos los elementos interactivos
