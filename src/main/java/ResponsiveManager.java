// (package eliminado para unificar en default package)

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Gestor de diseño responsive para la aplicación de droguería
 * Maneja el redimensionamiento automático y adaptación de componentes
 */
public class ResponsiveManager {
    
    // Breakpoints para diferentes tamaños de ventana
    public static final int SMALL_WIDTH = 800;
    public static final int MEDIUM_WIDTH = 1200;
    public static final int LARGE_WIDTH = 1600;
    
    public static final int SMALL_HEIGHT = 600;
    public static final int MEDIUM_HEIGHT = 800;
    public static final int LARGE_HEIGHT = 1000;
    
    /**
     * Configura un componente para que sea responsive
     */
    public static void makeResponsive(JFrame frame, JComponent... components) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = frame.getSize();
                adjustLayout(size, components);
            }
        });
        
        // Aplicar ajuste inicial
        adjustLayout(frame.getSize(), components);
    }
    
    /**
     * Ajusta el layout basado en el tamaño actual
     */
    private static void adjustLayout(Dimension size, JComponent... components) {
        ScreenSize screenSize = getScreenSize(size);
        
        for (JComponent component : components) {
            if (component instanceof JPanel) {
                adjustPanel((JPanel) component, screenSize);
            } else if (component instanceof JTabbedPane) {
                adjustTabbedPane((JTabbedPane) component, screenSize);
            }
        }
    }
    
    /**
     * Determina el tamaño de pantalla basado en las dimensiones
     */
    public static ScreenSize getScreenSize(Dimension size) {
        if (size.width <= SMALL_WIDTH || size.height <= SMALL_HEIGHT) {
            return ScreenSize.SMALL;
        } else if (size.width <= MEDIUM_WIDTH || size.height <= MEDIUM_HEIGHT) {
            return ScreenSize.MEDIUM;
        } else {
            return ScreenSize.LARGE;
        }
    }
    
    /**
     * Ajusta un panel según el tamaño de pantalla
     */
    private static void adjustPanel(JPanel panel, ScreenSize size) {
        switch (size) {
            case SMALL:
                adjustForSmallScreen(panel);
                break;
            case MEDIUM:
                adjustForMediumScreen(panel);
                break;
            case LARGE:
                adjustForLargeScreen(panel);
                break;
        }
    }
    
    /**
     * Ajusta un TabbedPane según el tamaño de pantalla
     */
    private static void adjustTabbedPane(JTabbedPane tabbedPane, ScreenSize size) {
        Font font;
        switch (size) {
            case SMALL:
                font = EstiloModerno.FUENTE_PEQUENA;
                break;
            case MEDIUM:
                font = EstiloModerno.FUENTE_NORMAL;
                break;
            case LARGE:
                font = EstiloModerno.FUENTE_BOTON;
                break;
            default:
                font = EstiloModerno.FUENTE_NORMAL;
        }
        tabbedPane.setFont(font);
    }
    
    /**
     * Ajustes para pantalla pequeña
     */
    private static void adjustForSmallScreen(JPanel panel) {
        // Reducir espaciado
        if (panel.getLayout() instanceof GridBagLayout) {
            Component[] components = panel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JComponent) {
                    JComponent jcomp = (JComponent) comp;
                    if (jcomp.getBorder() != null) {
                        jcomp.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(EstiloModerno.BORDE_PRINCIPAL, 1),
                            BorderFactory.createEmptyBorder(5, 8, 5, 8)
                        ));
                    }
                }
            }
        }
    }
    
    /**
     * Ajustes para pantalla mediana
     */
    private static void adjustForMediumScreen(JPanel panel) {
        // Espaciado normal
        if (panel.getLayout() instanceof GridBagLayout) {
            Component[] components = panel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JComponent) {
                    JComponent jcomp = (JComponent) comp;
                    if (jcomp.getBorder() != null) {
                        jcomp.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(EstiloModerno.BORDE_PRINCIPAL, 1),
                            BorderFactory.createEmptyBorder(8, 12, 8, 12)
                        ));
                    }
                }
            }
        }
    }
    
    /**
     * Ajustes para pantalla grande
     */
    private static void adjustForLargeScreen(JPanel panel) {
        // Espaciado amplio
        if (panel.getLayout() instanceof GridBagLayout) {
            Component[] components = panel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JComponent) {
                    JComponent jcomp = (JComponent) comp;
                    if (jcomp.getBorder() != null) {
                        jcomp.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(EstiloModerno.BORDE_PRINCIPAL, 1),
                            BorderFactory.createEmptyBorder(12, 16, 12, 16)
                        ));
                    }
                }
            }
        }
    }
    
    /**
     * Obtiene dimensiones responsive para campos de texto
     */
    public static Dimension getResponsiveTextFieldSize(ScreenSize size) {
        switch (size) {
            case SMALL:
                return new Dimension(200, 30);
            case MEDIUM:
                return new Dimension(300, 35);
            case LARGE:
                return new Dimension(400, 40);
            default:
                return new Dimension(300, 35);
        }
    }
    
    /**
     * Obtiene dimensiones responsive para botones
     */
    public static Dimension getResponsiveButtonSize(ScreenSize size) {
        switch (size) {
            case SMALL:
                return new Dimension(100, 35);
            case MEDIUM:
                return new Dimension(130, 40);
            case LARGE:
                return new Dimension(160, 45);
            default:
                return new Dimension(130, 40);
        }
    }
    
    /**
     * Obtiene insets responsive para GridBagLayout
     */
    public static Insets getResponsiveInsets(ScreenSize size) {
        switch (size) {
            case SMALL:
                return new Insets(8, 10, 8, 10);
            case MEDIUM:
                return new Insets(12, 15, 12, 15);
            case LARGE:
                return new Insets(16, 20, 16, 20);
            default:
                return new Insets(12, 15, 12, 15);
        }
    }
    
    /**
     * Obtiene fuente responsive según el tamaño
     */
    public static Font getResponsiveFont(ScreenSize size, FontType type) {
        int baseSize;
        switch (size) {
            case SMALL:
                baseSize = 12;
                break;
            case MEDIUM:
                baseSize = 14;
                break;
            case LARGE:
                baseSize = 16;
                break;
            default:
                baseSize = 14;
        }
        
        switch (type) {
            case TITLE:
                return new Font("Segoe UI", Font.BOLD, baseSize + 8);
            case SUBTITLE:
                return new Font("Segoe UI", Font.BOLD, baseSize + 4);
            case NORMAL:
                return new Font("Segoe UI", Font.PLAIN, baseSize);
            case BUTTON:
                return new Font("Segoe UI", Font.BOLD, baseSize);
            case SMALL:
                return new Font("Segoe UI", Font.PLAIN, baseSize - 2);
            default:
                return new Font("Segoe UI", Font.PLAIN, baseSize);
        }
    }
    
    /**
     * Enum para tipos de tamaño de pantalla
     */
    public enum ScreenSize {
        SMALL, MEDIUM, LARGE
    }
    
    /**
     * Enum para tipos de fuente
     */
    public enum FontType {
        TITLE, SUBTITLE, NORMAL, BUTTON, SMALL
    }
    
    /**
     * Crea un panel responsive que se adapta automáticamente
     */
    public static JPanel createResponsivePanel(LayoutManager layout) {
        return new JPanel(layout) {
            @Override
            public void doLayout() {
                super.doLayout();
                // Obtener el contenedor padre para determinar el tamaño
                Container parent = getParent();
                if (parent != null) {
                    Dimension parentSize = parent.getSize();
                    ScreenSize screenSize = getScreenSize(parentSize);
                    adjustPanel(this, screenSize);
                }
            }
        };
    }
    
    /**
     * Aplica espaciado responsive a un GridBagConstraints
     */
    public static void applyResponsiveConstraints(GridBagConstraints gbc, ScreenSize size) {
        gbc.insets = getResponsiveInsets(size);
        
        // Ajustar weightx y weighty según el tamaño
        switch (size) {
            case SMALL:
                gbc.weightx = Math.min(gbc.weightx, 0.8);
                gbc.weighty = Math.min(gbc.weighty, 0.8);
                break;
            case MEDIUM:
                gbc.weightx = Math.min(gbc.weightx, 1.0);
                gbc.weighty = Math.min(gbc.weighty, 1.0);
                break;
            case LARGE:
                gbc.weightx = Math.min(gbc.weightx, 1.2);
                gbc.weighty = Math.min(gbc.weighty, 1.2);
                break;
        }
    }
}