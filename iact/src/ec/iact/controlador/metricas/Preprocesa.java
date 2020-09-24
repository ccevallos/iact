package ec.iact.controlador.metricas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.zip.ZipEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ec.iact.util.DescomprimeStream;
import ec.iact.util.Parametros;

/**
 * Servlet que permite convertir a CSV un zip de archivos de métricas xml
 * generadas por Pandora Software Agent.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/Preprocesa.jsx")
public class Preprocesa extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP post. Invoca al método doGet.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Método que atiende una solicitud HTTP get, en la cual se recibe el nombre del
	 * archivo zip a procesar. El resultado es la creación de un archivo CSV.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		FileInputStream fis = null;
		try {
			HttpSession session = request.getSession();

			// Se obtiene el caso sobre el que se trabaja
			String caso = (String) session.getAttribute("caso");
			// Consulta el parámetro que tiene el nombre del archivo a preprocesar
			String nombre = request.getParameter("nombre");
			String nombreArchivo = Parametros.UBICACION_INICIO + File.separator + caso + File.separator
					+ Parametros.METRICA + File.separator + nombre;

			fis = new FileInputStream(nombreArchivo);
			// Invoca al método que descomprime el zip y preprocesa los datos.
			// El nombre del archivo de destino es el mismo zip cambiado de extensión.
			descomprimirPreprocesar(fis, nombreArchivo.replaceAll(".zip", ".csv"));

			fis.close();
			System.out.println("Fin");
		} catch (Exception e) {
			try {
				fis.close();
			} catch (Exception e2) {
			}
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Hubo un error en la carga");
		}

	}

	/**
	 * Este método toma un flujo de datos de un archivo comprimido que contiene el
	 * monitoreo realizado con el software agent de pandora y lee uno a uno los
	 * archivos xml para armar un archivo csv, en donde cada métrica se coloca como
	 * una columna y cada fila representa los datos de una medición.
	 * 
	 * @param is                   es el flujo de entrada (InputStream) del archivo
	 *                             comprimido
	 * @param nombreArchivoDestino es el nombre del archivo que se creará como
	 *                             resultado del preprocesamiento
	 * @throws Exception en caso de errores
	 */
	public void descomprimirPreprocesar(InputStream is, String nombreArchivoDestino) throws Exception {
		DescomprimeStream zis = new DescomprimeStream(is);

		// Se crea un treemap para que los datos mantengan un orden en base a la clave
		// que en este caso es la fecha
		TreeMap<String, String> datos = new TreeMap<String, String>();

		FileOutputStream fos = new FileOutputStream(nombreArchivoDestino);
		PrintWriter pw = new PrintWriter(fos);

		List<String> nombreMetricas = new ArrayList<String>();
		boolean obtenerNombreMetricas = true;

		StringJoiner cabeceras = new StringJoiner(",");
		cabeceras.add("Fecha");

		@SuppressWarnings("unused")
		ZipEntry salida = null;

		// recorre todo el buffer extrayendo uno a uno cada archivo.zip y creándolos de
		// nuevo en su archivo original
		while (null != (salida = zis.getNextEntry())) {
			String[] valores = obtieneValoresXML(zis, nombreMetricas, obtenerNombreMetricas);
			datos.put(valores[0], valores[1]);

			if (obtenerNombreMetricas) {
				obtenerNombreMetricas = false;
			}
			// zis.closeEntry();
		}

		for (String nombre : nombreMetricas) {
			cabeceras.add(nombre);
		}

		pw.print(cabeceras.toString());

		for (String fecha : datos.keySet()) {
			String valores = datos.get(fecha);
			pw.print("\n" + fecha + "," + valores);
		}

		pw.close();
		fos.close();

	}

	/**
	 * Este método obtiene los valores de las métricas de un archivo generado por
	 * Pandora Software Agent.
	 * 
	 * @param is                    Flujo de archivo XML que se va a leer.
	 * @param nombreMetricas        Los datos se leen y concatenan en el orden de
	 *                              esta lista de nombres de métricas. Si el
	 *                              parámetro obtenerNombreMetricas es verdadero, en
	 *                              este arreglo se añaden los nombres de las
	 *                              métricas identificadas en el archivo.
	 * 
	 * @param obtenerNombreMetricas indica si se obtienen o no las métricas
	 * @return retorna un arreglo, en donde el primer elemento es la fecha y el
	 *         segundo tiene los valores de las métricas separados por comas
	 * @throws Exception  en caso de errores
	 */
	public String[] obtieneValoresXML(InputStream is, List<String> nombreMetricas, boolean obtenerNombreMetricas)
			throws Exception {
		// Objetos para leer el XML
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("module");

		// Se extrae la fecha de la cabecera
		String fecha = doc.getDocumentElement().getAttribute("timestamp");

		// Contiene los atributos (métricas) que se encuentran en el XML
		HashMap<String, String> atributos = new HashMap<String, String>();

		// Objeto para combinar los valores separados por comas
		StringJoiner filaDatos = new StringJoiner(",");

		// Se recorre los nodos del XML. Cada nodo es una métrica.
		for (int cont = 0; cont < nList.getLength(); cont++) {
			Node nNode = nList.item(cont);
			NodeList hijos = nNode.getChildNodes();

			String nombre = "";
			String valor = "";

			// Se recorre los nodos hijos dentro de una métrica
			// Se obtiene el nombre y los valores de cada métrica
			for (int cont_hijo = 0; cont_hijo < hijos.getLength(); cont_hijo++) {
				Node hijo = hijos.item(cont_hijo);
				String nombreHijo = hijo.getNodeName();

				// Obtiene el nombre de la métrica
				if (nombreHijo.equals("name")) {
					nombre = hijo.getTextContent();
				}
				// Obtiene el valor de la métrica
				if (nombreHijo.equals("data")) {
					// cambia las comas por puntos para los valores decimales
					valor = hijo.getTextContent().replace(",", ".");
				}
			}
			if (obtenerNombreMetricas) {
				nombreMetricas.add(nombre);
			}
			// Se añade la métrica al mapa
			atributos.put(nombre, valor);
		}

		// Se leen los datos y se concatenan en el orden en el que se encuentra la lista
		// de nombres de métricas
		for (String metrica : nombreMetricas) {
			filaDatos.add(atributos.get(metrica));
		}

		// retorna los datos
		return new String[] { fecha, filaDatos.toString() };
	}

}
