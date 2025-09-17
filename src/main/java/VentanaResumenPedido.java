// (package eliminado para unificar en default package)

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaResumenPedido extends JFrame {
    private String nombreMedicamento;
    private String tipoMedicamento;
    private int cantidad;
    private String distribuidor;
    private List<String> sucursales;
    
    // direcciones de las farmacias
    private static final String DIRECCION_PRINCIPAL = "Calle de la Rosa n. 28";
    private static final String DIRECCION_SECUNDARIA = "Calle Alcazabilla n. 3";
    
    public VentanaResumenPedido(String nombreMedicamento, String tipoMedicamento, 
                               int cantidad, String distribuidor, List<String> sucursales) {
        this.nombreMedicamento = nombreMedicamento;
        this.tipoMedicamento = tipoMedicamento;
        this.cantidad = cantidad;
        this.distribuidor = distribuidor;
        this.sucursales = sucursales;
        
        configurarVentana();
        configurarComponentes();
        configurarLayout();
        configurarEventos();
    }
    
    private void configurarVentana() {
        setTitle("Pedido al distribuidor " + distribuidor);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Configuración responsive de la ventana
        setSize(600, 400);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Aplicar estilo moderno
        EstiloModerno.aplicarEstiloVentana(this);
    }
    
    private void configurarComponentes() {
        // no se necesitan componentes adicionales, solo los que se crean en configurarLayout
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // Determinar tamaño de pantalla para responsive
        ResponsiveManager.ScreenSize screenSize = ResponsiveManager.getScreenSize(getSize());
        
        // panel principal responsive con gridbaglayout usando estilo moderno
        JPanel panelPrincipal = ResponsiveManager.createResponsivePanel(new GridBagLayout());
        panelPrincipal = EstiloModerno.crearPanelConGradiente();
        panelPrincipal.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        ResponsiveManager.applyResponsiveConstraints(gbc, screenSize);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // titulo con estilo moderno y fuente responsive
        JLabel lblTitulo = new JLabel("RESUMEN DEL PEDIDO");
        lblTitulo.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.TITLE));
        lblTitulo.setForeground(EstiloModerno.TEXTO_TITULO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);
        
        // informacion del medicamento con fuente responsive
        String textoMedicamento = cantidad + " unidades del " + tipoMedicamento.toLowerCase() + " " + nombreMedicamento;
        JLabel lblMedicamento = new JLabel("<html><div style='text-align: center; width: 400px;'>" + textoMedicamento + "</div></html>");
        lblMedicamento.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.SUBTITLE));
        lblMedicamento.setForeground(EstiloModerno.VERDE_CLARO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        ResponsiveManager.applyResponsiveConstraints(gbc, screenSize);
        panelPrincipal.add(lblMedicamento, gbc);
        
        // informacion de la direccion con fuente responsive
        String textoDireccion = construirTextoDireccion();
        JLabel lblDireccion = new JLabel("<html><div style='text-align: center; width: 400px;'>" + textoDireccion + "</div></html>");
        lblDireccion.setFont(ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.NORMAL));
        lblDireccion.setForeground(EstiloModerno.TEXTO_PRINCIPAL);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        ResponsiveManager.applyResponsiveConstraints(gbc, screenSize);
        panelPrincipal.add(lblDireccion, gbc);
        
        // botones con tamaño responsive
        JButton btnCancelar = new JButton("Cancelar Pedido");
        JButton btnEnviar = new JButton("Enviar Pedido");
        
        Dimension buttonSize = ResponsiveManager.getResponsiveButtonSize(screenSize);
        Font buttonFont = ResponsiveManager.getResponsiveFont(screenSize, ResponsiveManager.FontType.BUTTON);
        
        btnCancelar.setPreferredSize(buttonSize);
        btnCancelar.setFont(buttonFont);
        btnEnviar.setPreferredSize(buttonSize);
        btnEnviar.setFont(buttonFont);
        
        EstiloModerno.aplicarEstiloBotonSecundario(btnCancelar);
        EstiloModerno.aplicarEstiloBotonPrincipal(btnEnviar);
        
        JPanel panelBotones = ResponsiveManager.createResponsivePanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        EstiloModerno.aplicarEstiloPanel(panelBotones);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnEnviar);
        
        // configurar eventos de los botones
        btnCancelar.addActionListener(e -> dispose());
        btnEnviar.addActionListener(e -> enviarPedido());
        
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Hacer que la ventana sea responsive
        ResponsiveManager.makeResponsive(this, panelPrincipal, panelBotones);
    }
    
    private String construirTextoDireccion() {
        StringBuilder texto = new StringBuilder("Para la farmacia situada en ");
        
        if (sucursales.contains("Principal") && sucursales.contains("Secundaria")) {
            texto.append(DIRECCION_PRINCIPAL).append(" y para la situada en ").append(DIRECCION_SECUNDARIA);
        } else if (sucursales.contains("Principal")) {
            texto.append(DIRECCION_PRINCIPAL);
        } else if (sucursales.contains("Secundaria")) {
            texto.append(DIRECCION_SECUNDARIA);
        }
        
        return texto.toString();
    }
    
    private void configurarEventos() {
        // los eventos ya se configuraron en configurarLayout
    }
    
    private void enviarPedido() {
        System.out.println("Pedido enviado exitosamente");
        JOptionPane.showMessageDialog(this, 
            "¡Pedido enviado exitosamente!\n\n" +
            "Distribuidor: " + distribuidor + "\n" +
            "Medicamento: " + nombreMedicamento + "\n" +
            "El medicamento ha sido guardado en la base de datos XML.",
            "Pedido Enviado", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
