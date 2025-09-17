# Sistema de Pedidos de Farmacia 💊

Sistema completo de gestión de pedidos de medicamentos para farmacias, desarrollado en Java con interfaz gráfica Swing moderna. Permite realizar pedidos a distribuidores farmacéuticos, validar datos, almacenar información en base de datos (XML/MySQL) y gestionar registros de medicamentos.

## 🌟 Características Principales

### 🎨 Interfaz Moderna
- **Tema Aero Negro/Verde** con efectos neón y halos visuales
- **Diseño Responsive** que se adapta a diferentes tamaños de pantalla
- **Animaciones Interactivas** con conexiones visuales entre componentes
- **Botones Neón Personalizados** con efectos hover y click
- **Cruz Verde Animada** como elemento decorativo principal

### 📋 Funcionalidades del Formulario
- **Validación Completa** de todos los campos con mensajes informativos
- **Campos Especializados** para medicamentos farmacéuticos
- **Selección de Distribuidores** (Cofarma, Empsephar, Cemefar)
- **Gestión de Sucursales** (Principal, Secundaria)
- **Confirmación de Pedidos** con ventana de resumen detallada

### 💾 Persistencia Dual
- **Base de Datos XML** para almacenamiento local
- **Base de Datos MySQL** para entornos empresariales
- **Cambio Dinámico** entre modos de almacenamiento
- **Reconexión Automática** en caso de pérdida de conexión
- **Configuración Flexible** mediante archivos de propiedades

### 📊 Panel de Registros
- **Tabla Completa** con todos los medicamentos almacenados
- **Filtros Avanzados** por tipo y distribuidor
- **Estadísticas en Tiempo Real** en títulos de pestañas
- **Eliminación Selectiva** con confirmación de seguridad

## 🏗️ Estructura del Proyecto

```
drogueria/
├── src/main/java/
│   ├── SistemaPedidosFarmacia.java    # Clase principal con interfaz moderna
│   ├── EstiloModerno.java             # Sistema de estilos neón y responsive
│   ├── ResponsiveManager.java         # Gestión de diseño adaptativo
│   ├── MySQLDatabase.java             # Conexión y operaciones MySQL
│   ├── BaseDatosXML.java              # Persistencia XML local
│   ├── PanelRegistros.java            # Panel de gestión de registros
│   ├── VentanaResumenPedido.java      # Ventana de confirmación
│   └── Medicamento.java               # Modelo de datos
├── src/main/resources/assets/
│   └── cruzverde.png                  # Icono de cruz verde personalizado
├── mysql.properties.example           # Template de configuración MySQL
├── crear_base_datos.sql               # Script de creación de BD
├── setup_database.sql                 # Script de configuración completa
├── .gitignore                         # Exclusiones para Git
└── README.md                          # Esta documentación
```

## 🚀 Instalación y Configuración

### Prerrequisitos
- **Java 8+** (JDK o JRE)
- **MySQL Server 8.0+** (opcional, para persistencia MySQL)
- **Maven** (para compilación, opcional)

### Configuración Rápida

1. **Clonar el repositorio:**
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd drogueria
   ```

2. **Configurar MySQL (Opcional):**
   ```bash
   # Copiar template de configuración
   cp mysql.properties.example mysql.properties
   
   # Editar credenciales en mysql.properties
   # mysql.username=tu_usuario
   # mysql.password=tu_password
   ```

3. **Crear base de datos MySQL:**
   ```bash
   # Ejecutar script SQL
   mysql -u root -p < crear_base_datos.sql
   ```

4. **Compilar y ejecutar:**
   ```bash
   # Compilación manual
   javac -cp "mysql-connector-java-8.0.33.jar" src/main/java/*.java
   java -cp ".:mysql-connector-java-8.0.33.jar:src/main/java" SistemaPedidosFarmacia
   
   # O con Maven (si tienes pom.xml)
   mvn compile exec:java -Dexec.mainClass="SistemaPedidosFarmacia"
   ```

## ⚙️ Configuración Avanzada

### MySQL Configuration (`mysql.properties`)
```properties
mysql.url=jdbc:mysql://localhost:3306/drogueria_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
mysql.username=tu_usuario
mysql.password=tu_password
mysql.maxReconnectAttempts=3
mysql.reconnectBackoffMs=1500
mysql.autoCreateDatabase=true
```

### Características Responsivas
El sistema se adapta automáticamente a tres tamaños de pantalla:
- **Pequeña** (< 1024px): Layout compacto, componentes apilados
- **Mediana** (1024-1440px): Layout equilibrado con espaciado moderado
- **Grande** (> 1440px): Layout amplio con máximo aprovechamiento del espacio

## 🎯 Funcionalidades Técnicas

### Sistema de Estilos Moderno
- **Paleta de Colores** negro/verde con 8 tonalidades predefinidas
- **Efectos Neón** en botones, campos y elementos interactivos
- **Gradientes Aero** en paneles y fondos
- **Tipografía Responsive** con 5 tipos de fuente escalables

### Gestión de Datos
- **Validación Robusta** con regex y verificaciones de integridad
- **Timestamps Automáticos** para todos los registros
- **Manejo de Errores** con mensajes informativos
- **Backup Automático** entre sistemas XML/MySQL

### Arquitectura del Código
- **Patrón MVC** separando lógica, datos y presentación
- **Gestión de Recursos** con AutoCloseable para conexiones
- **Logging Detallado** para debugging y monitoreo
- **Configuración Externa** para flexibilidad de deployment

## 🔒 Seguridad

- ✅ **Sin credenciales hardcodeadas** en el código fuente
- ✅ **Configuración externa** mediante archivos .properties
- ✅ **Validación de entrada** para prevenir inyecciones
- ✅ **Gestión segura de conexiones** con timeouts y reintentos

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver `LICENSE` para más detalles.

## 🆘 Soporte

Para reportar bugs o solicitar features, por favor abre un issue en el repositorio de GitHub.

## 🎨 Screenshots

<img width="766" height="582" alt="image" src="https://github.com/user-attachments/assets/2341dcea-11fa-4066-9a76-ba39c6d5c57a" />

<img width="851" height="483" alt="image" src="https://github.com/user-attachments/assets/10671c29-dad8-445a-8e6a-dab72a345f65" />

<img width="1005" height="515" alt="image" src="https://github.com/user-attachments/assets/731170e3-5328-4169-84fc-655fbaf08b96" />

<img width="1005" height="285" alt="image" src="https://github.com/user-attachments/assets/19d59384-4c86-49b2-b5e1-2cf6b046e5a7" />



para correr el proyecto debes crear tu server local con MSQL  y tu propia db y reemplazar los campos en los archivos *mysql.properties* y 
*crear_db_mysql.ps1*. 
---


**Desarrollado con ❤️ desde COLOMBIA usando Java Swing y APIREST**
