import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

public class BaseDatosXML {
    private static final String ARCHIVO_XML = "medicamentos.xml";
    private static final String RAIZ = "medicamentos";
    private static final String MEDICAMENTO = "medicamento";
    
    public BaseDatosXML() {
        inicializarBaseDatos();
    }
    
    private void inicializarBaseDatos() {
        File archivo = new File(ARCHIVO_XML);
        if (!archivo.exists()) {
            crearArchivoXMLVacio();
        }
    }
    
    private void crearArchivoXMLVacio() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            
            Element raiz = doc.createElement(RAIZ);
            doc.appendChild(raiz);
            
            guardarDocumento(doc);
        } catch (Exception e) {
            System.err.println("Error al crear archivo XML: " + e.getMessage());
        }
    }
    
    public void agregarMedicamento(String nombre, String tipo, int cantidad, String distribuidor, List<String> sucursales) {
        try {
            Document doc = cargarDocumento();
            Element raiz = doc.getDocumentElement();
            
            Element medicamento = doc.createElement(MEDICAMENTO);
            
            // agregar atributos del medicamento
            medicamento.setAttribute("nombre", nombre);
            medicamento.setAttribute("tipo", tipo);
            medicamento.setAttribute("cantidad", String.valueOf(cantidad));
            medicamento.setAttribute("distribuidor", distribuidor);
            medicamento.setAttribute("fecha", obtenerFechaActual());
            
            // agregar sucursales como elementos hijos
            for (String sucursal : sucursales) {
                Element sucursalElement = doc.createElement("sucursal");
                sucursalElement.setTextContent(sucursal);
                medicamento.appendChild(sucursalElement);
            }
            
            raiz.appendChild(medicamento);
            guardarDocumento(doc);
            
        } catch (Exception e) {
            System.err.println("Error al agregar medicamento: " + e.getMessage());
        }
    }
    
    public List<Medicamento> obtenerTodosLosMedicamentos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        
        try {
            Document doc = cargarDocumento();
            NodeList listaMedicamentos = doc.getElementsByTagName(MEDICAMENTO);
            
            for (int i = 0; i < listaMedicamentos.getLength(); i++) {
                Element elemento = (Element) listaMedicamentos.item(i);
                Medicamento medicamento = crearMedicamentoDesdeElemento(elemento);
                medicamentos.add(medicamento);
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
        }
        
        return medicamentos;
    }
    
    public List<Medicamento> buscarMedicamentosPorTipo(String tipo) {
        List<Medicamento> medicamentos = new ArrayList<>();
        List<Medicamento> todos = obtenerTodosLosMedicamentos();
        
        for (Medicamento med : todos) {
            if (med.getTipo().equalsIgnoreCase(tipo)) {
                medicamentos.add(med);
            }
        }
        
        return medicamentos;
    }
    
    public List<Medicamento> buscarMedicamentosPorDistribuidor(String distribuidor) {
        List<Medicamento> medicamentos = new ArrayList<>();
        List<Medicamento> todos = obtenerTodosLosMedicamentos();
        
        for (Medicamento med : todos) {
            if (med.getDistribuidor().equalsIgnoreCase(distribuidor)) {
                medicamentos.add(med);
            }
        }
        
        return medicamentos;
    }
    
    private Medicamento crearMedicamentoDesdeElemento(Element elemento) {
        String nombre = elemento.getAttribute("nombre");
        String tipo = elemento.getAttribute("tipo");
        int cantidad = Integer.parseInt(elemento.getAttribute("cantidad"));
        String distribuidor = elemento.getAttribute("distribuidor");
        String fecha = elemento.getAttribute("fecha");
        
        List<String> sucursales = new ArrayList<>();
        NodeList sucursalesNodes = elemento.getElementsByTagName("sucursal");
        for (int i = 0; i < sucursalesNodes.getLength(); i++) {
            sucursales.add(sucursalesNodes.item(i).getTextContent());
        }
        
        return new Medicamento(nombre, tipo, cantidad, distribuidor, sucursales, fecha);
    }
    
    private Document cargarDocumento() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(ARCHIVO_XML));
    }
    
    private void guardarDocumento(Document doc) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(ARCHIVO_XML));
        transformer.transform(source, result);
    }
    
    private String obtenerFechaActual() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    
    public void eliminarMedicamento(String nombre, String fecha) {
        try {
            Document doc = cargarDocumento();
            NodeList listaMedicamentos = doc.getElementsByTagName(MEDICAMENTO);
            
            for (int i = 0; i < listaMedicamentos.getLength(); i++) {
                Element elemento = (Element) listaMedicamentos.item(i);
                if (elemento.getAttribute("nombre").equals(nombre) && 
                    elemento.getAttribute("fecha").equals(fecha)) {
                    elemento.getParentNode().removeChild(elemento);
                    break;
                }
            }
            
            guardarDocumento(doc);
            
        } catch (Exception e) {
            System.err.println("Error al eliminar medicamento: " + e.getMessage());
        }
    }
    
    public int obtenerTotalMedicamentos() {
        try {
            Document doc = cargarDocumento();
            return doc.getElementsByTagName(MEDICAMENTO).getLength();
        } catch (Exception e) {
            return 0;
        }
    }
}
