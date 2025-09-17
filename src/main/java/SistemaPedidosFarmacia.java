import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;



// sistema de gestión de pedidos para farmacia con interfaz moderna
// utiliza dual persistencia (xml/mysql) y efectos visuales neón


/**
 * clase principal del sistema de pedidos de farmacia
 * implementa una interfaz moderna con estilo neón verde/negro
 * soporta almacenamiento dual (xml y mysql) con cambio dinámico
 */
public class SistemaPedidosFarmacia extends JFrame {
    
    // ===================== campos de interfaz gráfica =====================
    
    // campos de entrada para datos del medicamento
    private JTextField txtNombreMedicamento;    // nombre del medicamento a registrar
    private JComboBox<String> cmbTipoMedicamento; // selector del tipo (antibiótico, analgésico, etc.)
    private JTextField txtCantidad;             // cantidad numérica del medicamento
    
    // botones de radio para selección de distribuidor (excluyente)
    private JRadioButton rbCofarma, rbEmpsephar, rbCemefar;
    
    // checkboxes para selección de sucursales (múltiple)
    private JCheckBox chkPrincipal, chkSecundaria;
    
    // botones de acción principales
    private JButton btnBorrar, btnConfirmar;    // limpiar formulario y confirmar registro
    private JButton btnUsarXML, btnUsarMySQL;   // cambio dinámico entre bases de datos
    private JLabel lblModoActual;               // indicador visual del modo activo
    
    // ===================== sistema de persistencia dual =====================
    
    // instancias de las dos bases de datos disponibles
    private BaseDatosXML baseDatosXML;     // almacenamiento en archivo xml local
    private MySQLDatabase baseDatosMySQL;  // almacenamiento en base de datos mysql
    private boolean usarMySQL = false;     // flag para determinar cuál base usar
    
    // ===================== componentes de navegación =====================
    
    // sistema de pestañas para organizar funcionalidades
    private JTabbedPane tabbedPane;    // contenedor principal de pestañas
    private JPanel panelFormulario;    // pestaña de registro de medicamentos
    private JPanel panelRegistros;     // pestaña de consulta y gestión
    
    // ===================== elementos decorativos =====================
    
    // imagen animada con efectos neón para mejorar la experiencia visual
    private JLabel lblGifAnimado;
    
    /**
     * constructor principal que inicializa todo el sistema
     * configura la interfaz, bases de datos y elementos visuales
     */
    public SistemaPedidosFarmacia() {
        // aplicar tema visual moderno antes de crear cualquier componente
        EstiloModerno.configurarLookAndFeel();
        
        // configurar propiedades básicas de la ventana principal
        configurarVentana();
        
        // crear e inicializar todos los componentes de la interfaz
        inicializarComponentes();
        
        // inicializar sistema de persistencia xml como opción por defecto
        baseDatosXML = new BaseDatosXML();
        
        // intentar establecer conexión con mysql como opción alternativa
        try {
            baseDatosMySQL = new MySQLDatabase();
            if (baseDatosMySQL.isConectado()) {
                usarMySQL = true;
                System.out.println("usando mysql como base de datos principal");
            } else {
                System.out.println("mysql no disponible, usando xml como alternativa");
            }
        } catch (Exception e) {
            System.out.println("mysql no disponible, usando xml: " + e.getMessage());
            baseDatosMySQL = null;
        }
        
        // organizar todos los componentes en el diseño final
        configurarLayout();
        
        // asociar eventos a botones y elementos interactivos
        configurarEventos();
        
        // establecer estado inicial de la interfaz según modo activo
        actualizarInterfazModo();
    }
    
    /**
     * configura las propiedades básicas de la ventana principal
     * establece título, comportamiento de cierre, icono y dimensiones
     */
    private void configurarVentana() {
        setTitle("sistema de pedidos de farmacia por julian cardenas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // cargar y establecer icono personalizado para la aplicación
        try {
            ImageIcon icono = new ImageIcon("ICONO PROGRAMA.png");
            setIconImage(icono.getImage());
            System.out.println("icono cargado correctamente");
        } catch (Exception e) {
            System.out.println("no se pudo cargar el icono: " + e.getMessage());
        }
        
        // aplicar tema visual moderno a toda la ventana
        EstiloModerno.aplicarEstiloVentana(this);
        
        // establecer dimensiones optimizadas para pantallas modernas
        setSize(1600, 1000); // tamaño inicial amplio para mejor experiencia
        setMinimumSize(new Dimension(1200, 800)); // tamaño mínimo funcional
        setLocationRelativeTo(null); // centrar en pantalla automáticamente
        setResizable(true); // permitir redimensionamiento dinámico
    }
    
    /**
     * crea e inicializa todos los componentes de la interfaz de usuario
     * aplica estilos modernos y configuraciones específicas a cada elemento
     */
    private void inicializarComponentes() {
        
        // ===================== campo de nombre del medicamento =====================
        txtNombreMedicamento = new JTextField(20);
        EstiloModerno.aplicarEstiloTextField(txtNombreMedicamento); // estilo visual neón
        agregarHoverListener(txtNombreMedicamento); // efectos de hover interactivos
        
        // ===================== selector de tipo de medicamento =====================
        String[] tipos = {"", "analgésico", "analéptico", "anestésico", "antiácido", "antidepresivo", "antibiótico"};
        cmbTipoMedicamento = new JComboBox<>(tipos);
        EstiloModerno.aplicarEstiloComboBox(cmbTipoMedicamento); // dropdown con estilo moderno
        agregarHoverListener(cmbTipoMedicamento); // animaciones de interacción
        
        // ===================== campo numérico de cantidad =====================
        txtCantidad = new JTextField(10);
        EstiloModerno.aplicarEstiloTextField(txtCantidad); // estilo consistente
        agregarHoverListener(txtCantidad); // efectos visuales uniformes
        
        // ===================== grupo de distribuidores (selección única) =====================
        rbCofarma = new JRadioButton("cofarma");
        rbEmpsephar = new JRadioButton("empsephar");
        rbCemefar = new JRadioButton("cemefar");
        
        // aplicar estilo neón a cada radio button para consistencia visual
        EstiloModerno.aplicarEstiloRadioButton(rbCofarma);
        EstiloModerno.aplicarEstiloRadioButton(rbEmpsephar);
        EstiloModerno.aplicarEstiloRadioButton(rbCemefar);
        
        // Agregar hover listeners a radio buttons
        agregarHoverListener(rbCofarma);
        agregarHoverListener(rbEmpsephar);
        agregarHoverListener(rbCemefar);
        
        // agrupar botones de radio
        ButtonGroup grupoDistribuidor = new ButtonGroup();
        grupoDistribuidor.add(rbCofarma);
        grupoDistribuidor.add(rbEmpsephar);
        grupoDistribuidor.add(rbCemefar);
        
        // sucursales
        chkPrincipal = new JCheckBox("Farmacia Principal");
        chkSecundaria = new JCheckBox("Farmacia Secundaria");
        
        // Aplicar estilo a los checkboxes
        EstiloModerno.aplicarEstiloCheckBox(chkPrincipal);
        EstiloModerno.aplicarEstiloCheckBox(chkSecundaria);
        
        // Agregar hover listeners a checkboxes
        agregarHoverListener(chkPrincipal);
        agregarHoverListener(chkSecundaria);
        
        // botones con efecto neón personalizado (sin aplicar estilo externo para no sobrescribir render)
    btnBorrar = new NeonButton("Borrar", TipoBoton.SECUNDARIO);
    btnConfirmar = new NeonButton("Confirmar", TipoBoton.PRINCIPAL);
        
        // botones de selección de base de datos
    btnUsarXML = new NeonButton("Usar XML", TipoBoton.SECUNDARIO);
    btnUsarMySQL = new NeonButton("Usar MySQL", TipoBoton.SECUNDARIO);
        
        // etiqueta para mostrar modo actual
        lblModoActual = new JLabel("Modo actual: XML");
        lblModoActual.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblModoActual.setForeground(EstiloModerno.VERDE_CLARO);
        
        // cargar el gif animado
        cargarGifAnimado();
    }

    // Enum para tipos de botón neón (extraído fuera para evitar error de miembro interno)
    private static enum TipoBoton { PRINCIPAL, SECUNDARIO }

    // Botón personalizado con halo verde neón en hover y click
    private class NeonButton extends JButton {
        private boolean hover = false;
        private boolean presionado = false;
        private final TipoBoton tipo;
        private final Color colorBase;
        private final Color colorTexto;
        private final Color colorHover;
        private final Color colorPress;

    public NeonButton(String text, TipoBoton tipo) {
            super(text);
            this.tipo = tipo;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 14));

            if (tipo == TipoBoton.PRINCIPAL) {
                colorBase = EstiloModerno.VERDE_PRINCIPAL;
                colorHover = EstiloModerno.VERDE_CLARO;
                colorPress = new Color(0, 180, 110);
                colorTexto = Color.WHITE;
            } else {
                colorBase = new Color(45,45,45);
                colorHover = new Color(70,70,70);
                colorPress = new Color(30,30,30);
                colorTexto = Color.WHITE;
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { 
                    hover = true; 
                    // Activar conexión visual
                    activarConexionVisual(NeonButton.this);
                    repaint(); 
                }
                @Override
                public void mouseExited(MouseEvent e) { 
                    hover = false; 
                    presionado = false; 
                    // Desactivar conexión visual
                    desactivarConexionVisual();
                    repaint(); 
                }
                @Override
                public void mousePressed(MouseEvent e) { presionado = true; repaint(); }
                @Override
                public void mouseReleased(MouseEvent e) { presionado = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            // Fondo base dinámico
            Color fondo;
            if (presionado) fondo = colorPress;
            else if (hover) fondo = colorHover;
            else fondo = colorBase;

            g2.setColor(fondo);
            int arc = 14;
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // Halo neón (solo para principal y en hover)
            if (hover) {
                int capas = 4;
                for (int i = capas; i >= 1; i--) {
                    float alpha = 0.12f * (i / (float)capas);
                    int expand = 4 + i * 3;
                    Color c = (tipo == TipoBoton.PRINCIPAL) ? new Color(0, 255, 160, (int)(alpha * 255)) : new Color(120,120,120,(int)(alpha*180));
                    g2.setColor(c);
                    g2.fillRoundRect(-expand/2, -expand/2, w + expand, h + expand, arc + expand, arc + expand);
                }
            }

            // Borde suave
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(0,0,0,120));
            g2.drawRoundRect(0, 0, w-1, h-1, arc, arc);

            // Texto
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(colorTexto);
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(getText(), tx, ty);

            g2.dispose();
        }
    }
    
    // Método para agregar hover listener a cualquier componente
    private void agregarHoverListener(JComponent componente) {
        componente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                activarConexionVisual(componente);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                desactivarConexionVisual();
            }
        });
    }
    
    // Métodos para controlar la conexión visual
    private void activarConexionVisual(JComponent componente) {
        if (lblGifAnimado != null) {
            lblGifAnimado.putClientProperty("conexionActiva", Boolean.TRUE);
            lblGifAnimado.putClientProperty("botonActivo", componente);
            lblGifAnimado.repaint();
        }
    }
    
    private void desactivarConexionVisual() {
        if (lblGifAnimado != null) {
            lblGifAnimado.putClientProperty("conexionActiva", Boolean.FALSE);
            lblGifAnimado.putClientProperty("botonActivo", null);
            lblGifAnimado.repaint();
        }
    }
    
    // configurar el diseno de la interfaz
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // crear el panel de pestañas responsive
        tabbedPane = new JTabbedPane();
        EstiloModerno.aplicarEstiloTabbedPane(tabbedPane);
        
        // pestaña 1: formulario
        panelFormulario = crearPanelFormulario();
        tabbedPane.addTab("Formulario", null, panelFormulario, "Formulario de pedido de medicamentos");
        
        // pestaña 2: registros
        panelRegistros = crearPanelRegistros();
        tabbedPane.addTab("Registros", null, panelRegistros, "Base de datos de medicamentos");
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Hacer que toda la ventana principal sea responsive
        ResponsiveManager.makeResponsive(this, tabbedPane, panelFormulario, (JComponent) panelRegistros);
    }
    
    // crear el panel del formulario principal
    private JPanel crearPanelFormulario() {
        // Usar panel responsive con gradiente para efecto moderno
        JPanel panel = ResponsiveManager.createResponsivePanel(new BorderLayout());
        panel = EstiloModerno.crearPanelConGradiente();
        panel.setLayout(new BorderLayout());
        
        // panel principal responsive con gridbaglayout
        JPanel panelPrincipal = ResponsiveManager.createResponsivePanel(new GridBagLayout());
        EstiloModerno.aplicarEstiloPanel(panelPrincipal);
        
        // Configuración responsive inicial
        Dimension currentSize = getSize();
        ResponsiveManager.ScreenSize screenSize = ResponsiveManager.getScreenSize(currentSize);
        
        GridBagConstraints gbc = new GridBagConstraints();
        ResponsiveManager.applyResponsiveConstraints(gbc, screenSize);
        gbc.anchor = GridBagConstraints.WEST;
        
        // titulo con estilo moderno y fuente responsive
        JLabel lblTitulo = new JLabel("FORMULARIO DE PEDIDO DE MEDICAMENTOS");
        lblTitulo.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.TITLE));
        lblTitulo.setForeground(EstiloModerno.TEXTO_TITULO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = ResponsiveManager.getResponsiveInsets(screenSize);
        gbc.insets = new Insets(30, 20, 50, 20); // espacio especial para titulo
        panelPrincipal.add(lblTitulo, gbc);
        
        // panel central responsive para el formulario
        JPanel panelFormularioCentro = ResponsiveManager.createResponsivePanel(new GridBagLayout());
        EstiloModerno.aplicarEstiloPanelSecundario(panelFormularioCentro);
        GridBagConstraints gbcForm = new GridBagConstraints();
        ResponsiveManager.applyResponsiveConstraints(gbcForm, screenSize);
        gbcForm.anchor = GridBagConstraints.CENTER;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        
        // Ajustar tamaños de componentes según el tamaño de pantalla
        Dimension textFieldSize = ResponsiveManager.getResponsiveTextFieldSize(screenSize);
        txtNombreMedicamento.setPreferredSize(textFieldSize);
        txtCantidad.setPreferredSize(textFieldSize);
        cmbTipoMedicamento.setPreferredSize(textFieldSize);
        
        // nombre del medicamento con fuente responsive
        JLabel lblNombre = new JLabel("Nombre del Medicamento:");
        lblNombre.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.SUBTITLE));
        lblNombre.setForeground(EstiloModerno.VERDE_CLARO);
        gbcForm.gridx = 0;
        gbcForm.gridy = 0;
        gbcForm.gridwidth = 1;
        panelFormularioCentro.add(lblNombre, gbcForm);
        
        gbcForm.gridx = 1;
        gbcForm.gridwidth = 2;
        panelFormularioCentro.add(txtNombreMedicamento, gbcForm);
        
        // tipo de medicamento con fuente responsive
        JLabel lblTipo = new JLabel("Tipo del Medicamento:");
        lblTipo.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.SUBTITLE));
        lblTipo.setForeground(EstiloModerno.VERDE_CLARO);
        gbcForm.gridx = 0;
        gbcForm.gridy = 1;
        gbcForm.gridwidth = 1;
        panelFormularioCentro.add(lblTipo, gbcForm);
        
        gbcForm.gridx = 1;
        gbcForm.gridwidth = 2;
        panelFormularioCentro.add(cmbTipoMedicamento, gbcForm);
        
        // cantidad con fuente responsive
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.SUBTITLE));
        lblCantidad.setForeground(EstiloModerno.VERDE_CLARO);
        gbcForm.gridx = 0;
        gbcForm.gridy = 2;
        gbcForm.gridwidth = 1;
        panelFormularioCentro.add(lblCantidad, gbcForm);
        
        gbcForm.gridx = 1;
        gbcForm.gridwidth = 2;
        panelFormularioCentro.add(txtCantidad, gbcForm);
        
        // distribuidor con fuente responsive
        JLabel lblDistribuidor = new JLabel("Distribuidor:");
        lblDistribuidor.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.SUBTITLE));
        lblDistribuidor.setForeground(EstiloModerno.VERDE_CLARO);
        gbcForm.gridx = 0;
        gbcForm.gridy = 3;
        gbcForm.gridwidth = 1;
        panelFormularioCentro.add(lblDistribuidor, gbcForm);
        
        // Panel responsive para distribuidores
        JPanel panelDistribuidor = ResponsiveManager.createResponsivePanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        EstiloModerno.aplicarEstiloPanel(panelDistribuidor);
        
        // Ajustar fuente de radio buttons según tamaño de pantalla
        Font radioFont = ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.NORMAL);
        rbCofarma.setFont(radioFont);
        rbEmpsephar.setFont(radioFont);
        rbCemefar.setFont(radioFont);
        
        panelDistribuidor.add(rbCofarma);
        panelDistribuidor.add(rbEmpsephar);
        panelDistribuidor.add(rbCemefar);
        
        gbcForm.gridx = 1;
        gbcForm.gridwidth = 2;
        panelFormularioCentro.add(panelDistribuidor, gbcForm);
        
        // sucursal con fuente responsive
        JLabel lblSucursal = new JLabel("Sucursal:");
        lblSucursal.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.SUBTITLE));
        lblSucursal.setForeground(EstiloModerno.VERDE_CLARO);
        gbcForm.gridx = 0;
        gbcForm.gridy = 4;
        gbcForm.gridwidth = 1;
        panelFormularioCentro.add(lblSucursal, gbcForm);
        
        // Panel responsive para sucursales
        JPanel panelSucursal = ResponsiveManager.createResponsivePanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        EstiloModerno.aplicarEstiloPanel(panelSucursal);
        
        // Ajustar fuente de checkboxes según tamaño de pantalla
        chkPrincipal.setFont(radioFont);
        chkSecundaria.setFont(radioFont);
        
        panelSucursal.add(chkPrincipal);
        panelSucursal.add(chkSecundaria);
        
        gbcForm.gridx = 1;
        gbcForm.gridwidth = 2;
        panelFormularioCentro.add(panelSucursal, gbcForm);
        
        // botones con tamaño responsive
        Dimension buttonSize = ResponsiveManager.getResponsiveButtonSize(screenSize);
        btnBorrar.setPreferredSize(buttonSize);
        btnConfirmar.setPreferredSize(buttonSize);
        btnUsarXML.setPreferredSize(buttonSize);
        btnUsarMySQL.setPreferredSize(buttonSize);
        
        // Aplicar fuente responsive a botones
        Font buttonFont = ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.BUTTON);
        btnBorrar.setFont(buttonFont);
        btnConfirmar.setFont(buttonFont);
        btnUsarXML.setFont(buttonFont);
        btnUsarMySQL.setFont(buttonFont);
        
        // Panel principal de botones
        JPanel panelBotones = ResponsiveManager.createResponsivePanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        EstiloModerno.aplicarEstiloPanel(panelBotones);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnConfirmar);
        
        // Panel de selección de base de datos
        JPanel panelSeleccionDB = ResponsiveManager.createResponsivePanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        EstiloModerno.aplicarEstiloPanel(panelSeleccionDB);
        panelSeleccionDB.add(lblModoActual);
        panelSeleccionDB.add(btnUsarXML);
        panelSeleccionDB.add(btnUsarMySQL);
        
        // Panel contenedor de todos los botones
        JPanel panelTodosBotones = ResponsiveManager.createResponsivePanel(new BorderLayout());
        EstiloModerno.aplicarEstiloPanel(panelTodosBotones);
        panelTodosBotones.add(panelBotones, BorderLayout.CENTER);
        panelTodosBotones.add(panelSeleccionDB, BorderLayout.SOUTH);
        
        gbcForm.gridx = 0;
        gbcForm.gridy = 5;
        gbcForm.gridwidth = 3;
        ResponsiveManager.applyResponsiveConstraints(gbcForm, screenSize);
        panelFormularioCentro.add(panelTodosBotones, gbcForm);
        
        // agregar panel central al panel principal con peso responsive
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = screenSize == ResponsiveManager.ScreenSize.SMALL ? 4 : 2;
        gbc.anchor = GridBagConstraints.CENTER;
        ResponsiveManager.applyResponsiveConstraints(gbc, screenSize);
        
        // Ajustar espaciado según tamaño de pantalla
        switch (screenSize) {
            case SMALL:
                gbc.insets = new Insets(10, 20, 10, 20);
                gbc.weightx = 1.0;
                break;
            case MEDIUM:
                gbc.insets = new Insets(20, 50, 20, 50);
                gbc.weightx = 0.7;
                break;
            case LARGE:
                gbc.insets = new Insets(20, 80, 20, 80); // Márgenes más amplios para aprovechar el ancho
                gbc.weightx = 0.6;
                break;
        }
        
        panelPrincipal.add(panelFormularioCentro, gbc);
        
        // gif animado - solo mostrar en pantallas medianas y grandes
        if (lblGifAnimado != null && screenSize != ResponsiveManager.ScreenSize.SMALL) {
            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.NORTHWEST; // Cambiar a NORTHWEST para mover a la izquierda
            gbc.insets = new Insets(20, -20, 20, 40); // Mover más hacia la izquierda
            gbc.weightx = 0.3;
            gbc.weighty = 0.0;
            panelPrincipal.add(lblGifAnimado, gbc);
        }
        
        panel.add(panelPrincipal, BorderLayout.CENTER);
        
        // Hacer que el panel sea responsive
        ResponsiveManager.makeResponsive(this, panel, panelPrincipal, panelFormularioCentro);
        
        return panel;
    }
    
    // crear el panel de registros responsive
    private JPanel crearPanelRegistros() {
        PanelRegistros panelRegistros = new PanelRegistros(baseDatosXML, baseDatosMySQL, usarMySQL);
        
        // Hacer que el panel de registros sea responsive
        ResponsiveManager.makeResponsive(this, panelRegistros);
        
        return panelRegistros;
    }
    
    // configurar eventos de botones y teclado
    private void configurarEventos() {
        btnBorrar.addActionListener(e -> borrarFormulario());
        btnConfirmar.addActionListener(e -> confirmarPedido());
        
        // Eventos para selección de base de datos
        btnUsarXML.addActionListener(e -> cambiarAModoXML());
        btnUsarMySQL.addActionListener(e -> cambiarAModoMySQL());
        
        // agregar tecla de escape para salir de pantalla completa
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                if (gd.isFullScreenSupported() && gd.getFullScreenWindow() == SistemaPedidosFarmacia.this) {
                    gd.setFullScreenWindow(null);
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }
        };
        getRootPane().registerKeyboardAction(escapeAction, "ESCAPE", escapeKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    // Métodos para cambiar modo de base de datos
    private void cambiarAModoXML() {
        usarMySQL = false;
        actualizarInterfazModo();
        JOptionPane.showMessageDialog(this, 
            "Modo cambiado a XML.\nLos datos se guardarán en medicamentos.xml",
            "Modo XML", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cambiarAModoMySQL() {
        System.out.println("=== Intentando cambiar a modo MySQL ===");
        System.out.println("Estado actual - usarMySQL: " + usarMySQL);
        System.out.println("baseDatosMySQL es null: " + (baseDatosMySQL == null));
        
        // Si ya está en modo MySQL, mostrar mensaje informativo
        if (usarMySQL) {
            System.out.println("Ya está en modo MySQL");
            JOptionPane.showMessageDialog(this, 
                "Ya estás usando MySQL como base de datos.\nLos datos se están guardando en la base de datos MySQL.",
                "Modo MySQL", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Si baseDatosMySQL es null, intentar crear una nueva instancia
        if (baseDatosMySQL == null) {
            System.out.println("baseDatosMySQL es null, intentando crear nueva instancia...");
            try {
                baseDatosMySQL = new MySQLDatabase();
                System.out.println("Nueva instancia de MySQLDatabase creada");
            } catch (Exception e) {
                System.out.println("Error al crear nueva instancia: " + e.getMessage());
                baseDatosMySQL = null;
            }
        }
        
        if (baseDatosMySQL != null) {
            System.out.println("Verificando conexión MySQL...");
            boolean conectado = baseDatosMySQL.isConectado();
            System.out.println("MySQL conectado: " + conectado);
        }
        
        if (baseDatosMySQL == null || !baseDatosMySQL.isConectado()) {
            System.out.println("Mostrando mensaje de error MySQL no disponible");
            JOptionPane.showMessageDialog(this, 
                "MySQL no está disponible.\n" +
                "Para usar MySQL necesitas:\n" +
                "1. Instalar MySQL Server\n" +
                "2. Crear la base de datos 'drogueria_db'\n" +
                "3. Descargar mysql-connector-java.jar\n" +
                "4. Configurar credenciales en MySQLDatabase.java",
                "MySQL No Disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        System.out.println("Cambiando a modo MySQL exitosamente");
        usarMySQL = true;
        actualizarInterfazModo();
        JOptionPane.showMessageDialog(this, 
            "Modo cambiado a MySQL.\nLos datos se guardarán en la base de datos MySQL",
            "Modo MySQL", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void actualizarInterfazModo() {
        if (usarMySQL) {
            lblModoActual.setText("Modo actual: MySQL");
            lblModoActual.setForeground(EstiloModerno.VERDE_CLARO);
            btnUsarMySQL.setEnabled(false);
            btnUsarXML.setEnabled(true);
        } else {
            lblModoActual.setText("Modo actual: XML");
            lblModoActual.setForeground(EstiloModerno.VERDE_CLARO);
            btnUsarXML.setEnabled(false);
            btnUsarMySQL.setEnabled(true);
        }
        
        // Actualizar el panel de registros
        if (panelRegistros instanceof PanelRegistros) {
            ((PanelRegistros) panelRegistros).actualizarModo(usarMySQL);
            ((PanelRegistros) panelRegistros).actualizarDatos();
        }
    }
    
    // limpiar todos los campos del formulario
    private void borrarFormulario() {
        txtNombreMedicamento.setText("");
        cmbTipoMedicamento.setSelectedIndex(0);
        txtCantidad.setText("");
        rbCofarma.setSelected(false);
        rbEmpsephar.setSelected(false);
        rbCemefar.setSelected(false);
        chkPrincipal.setSelected(false);
        chkSecundaria.setSelected(false);
    }
    
    // procesar el pedido y guardarlo
    private void confirmarPedido() {
        // validar todos los campos
        if (!validarFormulario()) {
            return;
        }
        
        // obtener datos del formulario
        String nombreMedicamento = txtNombreMedicamento.getText().trim();
        String tipoMedicamento = (String) cmbTipoMedicamento.getSelectedItem();
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());
        String distribuidor = obtenerDistribuidorSeleccionado();
        List<String> sucursales = obtenerSucursalesSeleccionadas();
        
        // guardar en la base de datos (MySQL si está disponible, sino XML)
        if (usarMySQL && baseDatosMySQL != null) {
            baseDatosMySQL.agregarMedicamento(nombreMedicamento, tipoMedicamento, cantidad, distribuidor, sucursales);
        } else {
            baseDatosXML.agregarMedicamento(nombreMedicamento, tipoMedicamento, cantidad, distribuidor, sucursales);
        }
        
        // actualizar la pestaña de registros
        if (panelRegistros instanceof PanelRegistros) {
            ((PanelRegistros) panelRegistros).actualizarDatos();
        }
        
        // mostrar ventana de resumen
        mostrarResumenPedido(nombreMedicamento, tipoMedicamento, cantidad, distribuidor, sucursales);
    }
    
    // validar que todos los campos esten correctos
    private boolean validarFormulario() {
        // validar nombre del medicamento
        String nombre = txtNombreMedicamento.getText().trim();
        if (nombre.isEmpty() || !nombre.matches("^[a-zA-Z0-9\\s]+$")) {
            JOptionPane.showMessageDialog(this, 
                "El nombre del medicamento debe contener solo caracteres alfanuméricos y espacios.",
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // validar tipo de medicamento
        if (cmbTipoMedicamento.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un tipo de medicamento.",
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // validar cantidad
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "La cantidad debe ser un número entero positivo.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "La cantidad debe ser un número entero válido.",
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // validar distribuidor
        if (!rbCofarma.isSelected() && !rbEmpsephar.isSelected() && !rbCemefar.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un distribuidor.",
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // validar sucursal
        if (!chkPrincipal.isSelected() && !chkSecundaria.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar al menos una sucursal.",
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    // obtener el distribuidor seleccionado
    private String obtenerDistribuidorSeleccionado() {
        if (rbCofarma.isSelected()) return "Cofarma";
        if (rbEmpsephar.isSelected()) return "Empsephar";
        if (rbCemefar.isSelected()) return "Cemefar";
        return "";
    }
    
    // obtener las sucursales seleccionadas
    private List<String> obtenerSucursalesSeleccionadas() {
        List<String> sucursales = new ArrayList<>();
        if (chkPrincipal.isSelected()) {
            sucursales.add("Principal");
        }
        if (chkSecundaria.isSelected()) {
            sucursales.add("Secundaria");
        }
        return sucursales;
    }
    
    // mostrar ventana con resumen del pedido
    private void mostrarResumenPedido(String nombreMedicamento, String tipoMedicamento, 
                                    int cantidad, String distribuidor, List<String> sucursales) {
        VentanaResumenPedido ventanaResumen = new VentanaResumenPedido(
            nombreMedicamento, tipoMedicamento, cantidad, distribuidor, sucursales);
        ventanaResumen.setVisible(true);
    }
    
    // cargar y configurar la imagen de cruz verde
    private void cargarGifAnimado() {
        try {
            ImageIcon iconoCruzVerde = null;
            
            // Intentar cargar desde el JAR primero
            try {
                java.net.URL imgURL = getClass().getResource("/resources/assets/cruzverde.png");
                if (imgURL != null) {
                    iconoCruzVerde = new ImageIcon(imgURL);
                    System.out.println("Cruz verde cargada desde JAR");
                } else {
                    // Intentar ruta alternativa sin barra inicial
                    imgURL = getClass().getClassLoader().getResource("resources/assets/cruzverde.png");
                    if (imgURL != null) {
                        iconoCruzVerde = new ImageIcon(imgURL);
                        System.out.println("Cruz verde cargada desde JAR (ruta alternativa)");
                    }
                }
            } catch (Exception e) {
                System.out.println("No se pudo cargar desde JAR: " + e.getMessage());
            }
            
            // Si no se pudo cargar desde JAR, intentar desde archivo
            if (iconoCruzVerde == null) {
                File imagenFile = new File("src/main/resources/assets/cruzverde.png");
                if (imagenFile.exists()) {
                    iconoCruzVerde = new ImageIcon(imagenFile.getAbsolutePath());
                    System.out.println("Cruz verde cargada desde archivo");
                } else {
                    System.err.println("No se encontró cruzverde.png en la carpeta assets.");
                    lblGifAnimado = null;
                    return;
                }
            }
    
            // Redimensionar la imagen a dimensiones cuadradas más grandes para animación uniforme
            Image img = iconoCruzVerde.getImage();
            Image imgEscalada = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            iconoCruzVerde = new ImageIcon(imgEscalada);
            
            // JLabel personalizado con halo neón en hover y conexión visual
            final ImageIcon iconFinal = iconoCruzVerde;
            lblGifAnimado = new JLabel() {
                private boolean hover = false;
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth();
                    int h = getHeight();
                    
                    // Dibujar imagen centrada
                    int imgW = iconFinal.getIconWidth();
                    int imgH = iconFinal.getIconHeight();
                    int x = (w - imgW) / 2;
                    int y = (h - imgH) / 2;
                    iconFinal.paintIcon(this, g2, x, y);
                    
                    // Verificar si hay conexión activa
                    Boolean conexionActiva = (Boolean) getClientProperty("conexionActiva");
                    
                    if (hover || (conexionActiva != null && conexionActiva)) {
                        // Dibujar halo neón verde (círculos concéntricos suaves)
                        int radioBase = Math.min(imgW, imgH) + 10;
                        int centroX = w / 2;
                        int centroY = h / 2;
                        for (int i = 0; i < 6; i++) {
                            float alpha = 0.08f - (i * 0.01f);
                            if (alpha <= 0) break;
                            int r = radioBase + i * 10;
                            g2.setColor(new Color(0, 255, 136, (int) (alpha * 255)));
                            g2.fillOval(centroX - r / 2, centroY - r / 2, r, r);
                        }
                        // Contorno interno suave
                        g2.setStroke(new BasicStroke(3f));
                        g2.setColor(new Color(0, 255, 160));
                        g2.drawOval(centroX - (radioBase - 20) / 2, centroY - (radioBase - 20) / 2, (radioBase - 20), (radioBase - 20));
                        
                        // Sin línea de conexión - solo efecto halo
                    } else {
                        // Borde base discreto (azul oscuro de la imagen) para integrarlo sin líneas blancas
                        g2.setColor(new Color(10, 40, 90));
                        g2.setStroke(new BasicStroke(2f));
                        int imgCircleR = Math.min(imgW, imgH);
                        g2.drawOval((w - imgCircleR) / 2, (h - imgCircleR) / 2, imgCircleR, imgCircleR);
                    }
                    g2.dispose();
                }
            };
            lblGifAnimado.setPreferredSize(new Dimension(400, 400));
            lblGifAnimado.setOpaque(false);
            lblGifAnimado.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JLabel)e.getSource()).putClientProperty("hover", Boolean.TRUE);
                    ((JLabel)e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    // activar hover y repintar
                    try {
                        java.lang.reflect.Field f = e.getSource().getClass().getDeclaredField("hover");
                        f.setAccessible(true);
                        f.set(e.getSource(), true);
                    } catch (Exception ignored) {}
                    e.getComponent().repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    try {
                        java.lang.reflect.Field f = e.getSource().getClass().getDeclaredField("hover");
                        f.setAccessible(true);
                        f.set(e.getSource(), false);
                    } catch (Exception ignored) {}
                    e.getComponent().repaint();
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            lblGifAnimado = null;
        }
    }
    
    // metodo principal para ejecutar la aplicacion
    public static void main(String[] args) {
        // Configurar el estilo moderno desde el inicio
        EstiloModerno.configurarLookAndFeel();
        
        // crear y mostrar la ventana principal con el nuevo estilo
        SwingUtilities.invokeLater(() -> {
            try {
                SistemaPedidosFarmacia ventana = new SistemaPedidosFarmacia();
                ventana.setVisible(true);
                    
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al inicializar la aplicación:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
