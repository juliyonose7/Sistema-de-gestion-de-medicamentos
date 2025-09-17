

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Clase que centraliza el estilo visual moderno para la aplicación de droguería
 * Tema: Negro y verde con estilo aero/moderno
 */
public class EstiloModerno {
    
    // PALETA DE COLORES PRINCIPAL
    public static final Color FONDO_PRINCIPAL = new Color(30, 30, 30);           // Negro principal
    public static final Color FONDO_SECUNDARIO = new Color(45, 45, 45);         // Gris oscuro
    public static final Color FONDO_PANEL = new Color(40, 40, 40);              // Gris panel
    
    public static final Color VERDE_PRINCIPAL = new Color(0, 150, 100);         // Verde principal
    public static final Color VERDE_HOVER = new Color(0, 180, 120);             // Verde hover
    public static final Color VERDE_CLARO = new Color(100, 255, 180);           // Verde claro
    public static final Color VERDE_OSCURO = new Color(0, 120, 80);             // Verde oscuro
    
    public static final Color TEXTO_PRINCIPAL = new Color(240, 240, 240);       // Blanco suave
    public static final Color TEXTO_SECUNDARIO = new Color(180, 180, 180);      // Gris claro
    public static final Color TEXTO_TITULO = new Color(100, 255, 180);          // Verde claro
    
    public static final Color BORDE_PRINCIPAL = new Color(80, 80, 80);          // Borde gris
    public static final Color BORDE_ACTIVO = new Color(0, 150, 100);            // Borde verde
    
    public static final Color ERROR = new Color(220, 80, 80);                   // Rojo error
    public static final Color EXITO = new Color(80, 200, 120);                  // Verde éxito
    public static final Color ADVERTENCIA = new Color(255, 180, 0);             // Amarillo advertencia
    
    // FUENTES
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_PEQUENA = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);
    
    /**
     * Configura el Look and Feel global de la aplicación
     */
    public static void configurarLookAndFeel() {
        try {
            // Configurar propiedades del sistema
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
            // Usar el Look and Feel del sistema como base
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            
            // Personalizar componentes globales
            configurarUIDefaults();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Configura los defaults globales de UI
     */
    private static void configurarUIDefaults() {
        // Panel defaults
        UIManager.put("Panel.background", FONDO_PRINCIPAL);
        UIManager.put("Panel.foreground", TEXTO_PRINCIPAL);
        
        // Button defaults
        UIManager.put("Button.background", VERDE_PRINCIPAL);
        UIManager.put("Button.foreground", TEXTO_PRINCIPAL);
        UIManager.put("Button.font", FUENTE_BOTON);
        UIManager.put("Button.border", crearBordeRedondeado(BORDE_ACTIVO));
        
        // TextField defaults
        UIManager.put("TextField.background", FONDO_SECUNDARIO);
        UIManager.put("TextField.foreground", TEXTO_PRINCIPAL);
        UIManager.put("TextField.caretForeground", VERDE_CLARO);
        UIManager.put("TextField.selectionBackground", VERDE_PRINCIPAL);
        UIManager.put("TextField.selectionForeground", TEXTO_PRINCIPAL);
        
        // ComboBox defaults
        UIManager.put("ComboBox.background", FONDO_SECUNDARIO);
        UIManager.put("ComboBox.foreground", TEXTO_PRINCIPAL);
        
        // Table defaults
        UIManager.put("Table.background", FONDO_PANEL);
        UIManager.put("Table.foreground", TEXTO_PRINCIPAL);
        UIManager.put("Table.selectionBackground", VERDE_PRINCIPAL);
        UIManager.put("Table.selectionForeground", TEXTO_PRINCIPAL);
        UIManager.put("Table.gridColor", BORDE_PRINCIPAL);
        
        // TabbedPane defaults
        UIManager.put("TabbedPane.background", FONDO_PRINCIPAL);
        UIManager.put("TabbedPane.foreground", TEXTO_PRINCIPAL);
        UIManager.put("TabbedPane.selected", VERDE_PRINCIPAL);
    }
    
    /**
     * Aplica estilo moderno a un JFrame
     */
    public static void aplicarEstiloVentana(JFrame frame) {
        frame.getContentPane().setBackground(FONDO_PRINCIPAL);
        frame.setBackground(FONDO_PRINCIPAL);
    }
    
    /**
     * Aplica estilo moderno a un JPanel
     */
    public static void aplicarEstiloPanel(JPanel panel) {
        panel.setBackground(FONDO_PRINCIPAL);
        panel.setForeground(TEXTO_PRINCIPAL);
    }
    
    /**
     * Aplica estilo moderno a un JPanel secundario
     */
    public static void aplicarEstiloPanelSecundario(JPanel panel) {
        panel.setBackground(FONDO_PANEL);
        panel.setForeground(TEXTO_PRINCIPAL);
        panel.setBorder(crearBordeRedondeado(BORDE_PRINCIPAL));
    }
    
    /**
     * Aplica estilo moderno a un JLabel título
     */
    public static void aplicarEstiloTitulo(JLabel label) {
        label.setFont(FUENTE_TITULO);
        label.setForeground(TEXTO_TITULO);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    /**
     * Aplica estilo moderno a un JLabel subtítulo
     */
    public static void aplicarEstiloSubtitulo(JLabel label) {
        label.setFont(FUENTE_SUBTITULO);
        label.setForeground(VERDE_CLARO);
    }
    
    /**
     * Aplica estilo moderno a un JLabel normal
     */
    public static void aplicarEstiloLabel(JLabel label) {
        label.setFont(FUENTE_NORMAL);
        label.setForeground(TEXTO_PRINCIPAL);
    }
    
    /**
     * Aplica estilo moderno a un JTextField
     */
    public static void aplicarEstiloTextField(JTextField textField) {
        textField.setBackground(FONDO_SECUNDARIO);
        textField.setForeground(TEXTO_PRINCIPAL);
        textField.setCaretColor(VERDE_CLARO);
        textField.setSelectionColor(VERDE_PRINCIPAL);
        textField.setSelectedTextColor(TEXTO_PRINCIPAL);
        textField.setFont(FUENTE_NORMAL);
        textField.setBorder(crearBordeTextField());
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 35));
    }
    
    /**
     * Aplica estilo moderno a un JComboBox
     */
    public static void aplicarEstiloComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(FONDO_SECUNDARIO);
        comboBox.setForeground(TEXTO_PRINCIPAL);
        comboBox.setFont(FUENTE_NORMAL);
        comboBox.setBorder(crearBordeTextField());
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 35));
    }
    
    /**
     * Aplica estilo moderno a un JButton principal
     */
    public static void aplicarEstiloBotonPrincipal(JButton button) {
        button.setBackground(VERDE_PRINCIPAL);
        button.setForeground(TEXTO_PRINCIPAL);
        button.setFont(FUENTE_BOTON);
        button.setBorder(crearBordeBoton());
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 20, 40));
        
        // Efectos hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(VERDE_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(VERDE_PRINCIPAL);
            }
        });
    }
    
    /**
     * Aplica estilo moderno a un JButton secundario
     */
    public static void aplicarEstiloBotonSecundario(JButton button) {
        button.setBackground(FONDO_SECUNDARIO);
        button.setForeground(TEXTO_PRINCIPAL);
        button.setFont(FUENTE_BOTON);
        button.setBorder(crearBordeBoton());
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 20, 40));
        
        // Efectos hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 60, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(FONDO_SECUNDARIO);
            }
        });
    }
    
    /**
     * Aplica estilo moderno a un JRadioButton
     */
    public static void aplicarEstiloRadioButton(JRadioButton radioButton) {
        radioButton.setBackground(FONDO_PRINCIPAL);
        radioButton.setForeground(TEXTO_PRINCIPAL);
        radioButton.setFont(FUENTE_NORMAL);
        radioButton.setFocusPainted(false);
    }
    
    /**
     * Aplica estilo moderno a un JCheckBox
     */
    public static void aplicarEstiloCheckBox(JCheckBox checkBox) {
        checkBox.setBackground(FONDO_PRINCIPAL);
        checkBox.setForeground(TEXTO_PRINCIPAL);
        checkBox.setFont(FUENTE_NORMAL);
        checkBox.setFocusPainted(false);
    }
    
    /**
     * Aplica estilo moderno a una JTable
     */
    public static void aplicarEstiloTabla(JTable table) {
        table.setBackground(FONDO_PANEL);
        table.setForeground(TEXTO_PRINCIPAL);
        table.setSelectionBackground(VERDE_PRINCIPAL);
        table.setSelectionForeground(TEXTO_PRINCIPAL);
        table.setGridColor(BORDE_PRINCIPAL);
        table.setFont(FUENTE_NORMAL);
        table.setRowHeight(30);
        
        // Estilo del header
        if (table.getTableHeader() != null) {
            table.getTableHeader().setBackground(FONDO_SECUNDARIO);
            table.getTableHeader().setForeground(VERDE_CLARO);
            table.getTableHeader().setFont(FUENTE_BOTON);
        }
    }
    
    /**
     * Aplica estilo moderno a un JTabbedPane
     */
    public static void aplicarEstiloTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setBackground(FONDO_PRINCIPAL);
        tabbedPane.setForeground(TEXTO_PRINCIPAL);
        tabbedPane.setFont(FUENTE_BOTON);
    }
    
    /**
     * Crea un borde redondeado personalizado
     */
    public static Border crearBordeRedondeado(Color color) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }
    
    /**
     * Crea un borde para TextField
     */
    public static Border crearBordeTextField() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_PRINCIPAL, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }
    
    /**
     * Crea un borde para botones
     */
    public static Border crearBordeBoton() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0,0,0,0), 1, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        );
    }
    
    /**
     * Crea un panel con gradiente (efecto aero)
     */
    public static JPanel crearPanelConGradiente() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, FONDO_PRINCIPAL,
                    0, getHeight(), FONDO_SECUNDARIO
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
}