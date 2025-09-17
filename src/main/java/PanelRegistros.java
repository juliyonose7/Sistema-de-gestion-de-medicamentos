// (package eliminado para unificar en default package)

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelRegistros extends JPanel {
    private BaseDatosXML baseDatosXML;
    private MySQLDatabase baseDatosMySQL;
    private boolean usarMySQL;
    private JTable tablaMedicamentos;
    private DefaultTableModel modelo;
    private JComboBox<String> cmbFiltroTipo;
    private JComboBox<String> cmbFiltroDistribuidor;
    
    public PanelRegistros(BaseDatosXML baseDatosXML, MySQLDatabase baseDatosMySQL, boolean usarMySQL) {
        this.baseDatosXML = baseDatosXML;
        this.baseDatosMySQL = baseDatosMySQL;
        this.usarMySQL = usarMySQL;
        
        // Aplicar estilo moderno al panel
        EstiloModerno.aplicarEstiloPanel(this);
        
        configurarComponentes();
        configurarLayout();
        configurarEventos();
        cargarDatos();
    }
    
    private void configurarComponentes() {
        // tabla de medicamentos
        String[] columnas = {"Nombre", "Tipo", "Cantidad", "Distribuidor", "Sucursales", "Fecha"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // hacer la tabla no editable
            }
        };
        tablaMedicamentos = new JTable(modelo);
        tablaMedicamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Aplicar estilo moderno a la tabla
        EstiloModerno.aplicarEstiloTabla(tablaMedicamentos);
        
        // filtros
        String[] tipos = {"Todos los tipos", "Analgésico", "Analéptico", "Anestésico", "Antiácido", "Antidepresivo", "Antibiótico"};
        cmbFiltroTipo = new JComboBox<>(tipos);
        EstiloModerno.aplicarEstiloComboBox(cmbFiltroTipo);
        
        String[] distribuidores = {"Todos los distribuidores", "Cofarma", "Empsephar", "Cemefar"};
        cmbFiltroDistribuidor = new JComboBox<>(distribuidores);
        EstiloModerno.aplicarEstiloComboBox(cmbFiltroDistribuidor);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // panel superior con filtros responsive
        JPanel panelFiltros = ResponsiveManager.createResponsivePanel(new FlowLayout());
        EstiloModerno.aplicarEstiloPanelSecundario(panelFiltros);
        
        // Determinar tamaño de pantalla actual
        Container parent = getParent();
        ResponsiveManager.ScreenSize screenSize = ResponsiveManager.ScreenSize.MEDIUM;
        if (parent != null) {
            screenSize = ResponsiveManager.getScreenSize(parent.getSize());
        }
        
        // Labels con fuente responsive
        JLabel lblFiltroTipo = new JLabel("Filtrar por tipo:");
        lblFiltroTipo.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.NORMAL));
        lblFiltroTipo.setForeground(EstiloModerno.TEXTO_PRINCIPAL);
        panelFiltros.add(lblFiltroTipo);
        
        // Ajustar tamaño de comboboxes
        Dimension comboSize = ResponsiveManager.getResponsiveTextFieldSize(screenSize);
        cmbFiltroTipo.setPreferredSize(comboSize);
        panelFiltros.add(cmbFiltroTipo);
        
        JLabel lblFiltroDistribuidor = new JLabel("Filtrar por distribuidor:");
        lblFiltroDistribuidor.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.NORMAL));
        lblFiltroDistribuidor.setForeground(EstiloModerno.TEXTO_PRINCIPAL);
        panelFiltros.add(lblFiltroDistribuidor);
        
        cmbFiltroDistribuidor.setPreferredSize(comboSize);
        panelFiltros.add(cmbFiltroDistribuidor);
        
        // panel de botones responsive
        JPanel panelBotones = ResponsiveManager.createResponsivePanel(new FlowLayout());
        EstiloModerno.aplicarEstiloPanel(panelBotones);
        
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        JButton btnExportar = new JButton("Exportar XML");
        
        // Aplicar tamaño y fuente responsive a botones
        Dimension buttonSize = ResponsiveManager.getResponsiveButtonSize(screenSize);
        Font buttonFont = ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.BUTTON);
        
        btnActualizar.setPreferredSize(buttonSize);
        btnActualizar.setFont(buttonFont);
        EstiloModerno.aplicarEstiloBotonPrincipal(btnActualizar);
        
        btnEliminar.setPreferredSize(buttonSize);
        btnEliminar.setFont(buttonFont);
        EstiloModerno.aplicarEstiloBotonSecundario(btnEliminar);
        
        btnExportar.setPreferredSize(buttonSize);
        btnExportar.setFont(buttonFont);
        EstiloModerno.aplicarEstiloBotonPrincipal(btnExportar);
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportar);
        
        // panel principal responsive
        JPanel panelPrincipal = ResponsiveManager.createResponsivePanel(new BorderLayout());
        EstiloModerno.aplicarEstiloPanel(panelPrincipal);
        panelPrincipal.add(panelFiltros, BorderLayout.NORTH);
        
        // ScrollPane con tabla responsive
        JScrollPane scrollPane = new JScrollPane(tablaMedicamentos);
        scrollPane.getViewport().setBackground(EstiloModerno.FONDO_PANEL);
        scrollPane.setBackground(EstiloModerno.FONDO_PANEL);
        
        // Ajustar altura de filas de la tabla según tamaño de pantalla
        switch (screenSize) {
            case SMALL:
                tablaMedicamentos.setRowHeight(25);
                break;
            case MEDIUM:
                tablaMedicamentos.setRowHeight(30);
                break;
            case LARGE:
                tablaMedicamentos.setRowHeight(35);
                break;
        }
        
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // configurar eventos de los botones
        btnActualizar.addActionListener(e -> cargarDatos());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnExportar.addActionListener(e -> exportarXML());
        
        // configurar eventos de los filtros
        cmbFiltroTipo.addActionListener(e -> aplicarFiltros());
        cmbFiltroDistribuidor.addActionListener(e -> aplicarFiltros());
    }
    
    private void configurarEventos() {
        // los eventos ya se configuraron en configurarLayout
    }
    
    public void cargarDatos() {
        modelo.setRowCount(0); // limpiar tabla
        
        List<Medicamento> medicamentos;
        if (usarMySQL && baseDatosMySQL != null) {
            medicamentos = baseDatosMySQL.obtenerTodosLosMedicamentos();
        } else {
            medicamentos = baseDatosXML.obtenerTodosLosMedicamentos();
        }
        
        for (Medicamento med : medicamentos) {
            Object[] fila = {
                med.getNombre(),
                med.getTipo(),
                med.getCantidad(),
                med.getDistribuidor(),
                med.getSucursalesString(),
                med.getFecha()
            };
            modelo.addRow(fila);
        }
        
        // mostrar estadisticas
        mostrarEstadisticas();
    }
    
    private void aplicarFiltros() {
        String tipoSeleccionado = (String) cmbFiltroTipo.getSelectedItem();
        String distribuidorSeleccionado = (String) cmbFiltroDistribuidor.getSelectedItem();
        
        modelo.setRowCount(0); // limpiar tabla
        
        List<Medicamento> medicamentos;
        if (usarMySQL && baseDatosMySQL != null) {
            medicamentos = baseDatosMySQL.obtenerMedicamentosFiltrados(tipoSeleccionado, distribuidorSeleccionado);
        } else {
            // Filtrado manual para XML
            medicamentos = baseDatosXML.obtenerTodosLosMedicamentos();
            medicamentos = medicamentos.stream()
                .filter(med -> tipoSeleccionado.equals("Todos los tipos") || med.getTipo().equals(tipoSeleccionado))
                .filter(med -> distribuidorSeleccionado.equals("Todos los distribuidores") || med.getDistribuidor().equals(distribuidorSeleccionado))
                .collect(java.util.stream.Collectors.toList());
        }
        
        for (Medicamento med : medicamentos) {
            Object[] fila = {
                med.getNombre(),
                med.getTipo(),
                med.getCantidad(),
                med.getDistribuidor(),
                med.getSucursalesString(),
                med.getFecha()
            };
            modelo.addRow(fila);
        }
    }
    
    private void eliminarSeleccionado() {
        int filaSeleccionada = tablaMedicamentos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila para eliminar.",
                                        "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombre = (String) modelo.getValueAt(filaSeleccionada, 0);
        String fecha = (String) modelo.getValueAt(filaSeleccionada, 5);
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea eliminar el medicamento '" + nombre + "'?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (usarMySQL && baseDatosMySQL != null) {
                boolean eliminado = baseDatosMySQL.eliminarMedicamento(nombre, fecha);
                if (eliminado) {
                    cargarDatos();
                    JOptionPane.showMessageDialog(this, "Medicamento eliminado exitosamente.",
                                                "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el medicamento.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                baseDatosXML.eliminarMedicamento(nombre, fecha);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Medicamento eliminado exitosamente.",
                                            "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void exportarXML() {
        try {
            // simplemente mostrar un mensaje de que el archivo ya esta disponible
            JOptionPane.showMessageDialog(this, 
                "La base de datos XML ya está disponible en el archivo 'medicamentos.xml' en el directorio del programa.",
                "Exportación", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarEstadisticas() {
        int totalMedicamentos;
        if (usarMySQL && baseDatosMySQL != null) {
            totalMedicamentos = baseDatosMySQL.obtenerTotalMedicamentos();
        } else {
            totalMedicamentos = baseDatosXML.obtenerTotalMedicamentos();
        }
        // actualizar el titulo de la pestaña si es posible
        Container parent = getParent();
        if (parent instanceof JTabbedPane) {
            JTabbedPane tabbedPane = (JTabbedPane) parent;
            int index = tabbedPane.indexOfComponent(this);
            if (index != -1) {
                tabbedPane.setTitleAt(index, "Registros (" + totalMedicamentos + ")");
            }
        }
    }
    
    public void actualizarDatos() {
        cargarDatos();
    }
    
    public void actualizarModo(boolean nuevoModoMySQL) {
        this.usarMySQL = nuevoModoMySQL;
    }
}
