package ec.iact.controlador.ia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.modelo.Archivo;

/**
 * Servlet que prepara la información previo a la generación del modelo.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/Proyeccion3.jsx")
public class Proyeccion3 extends HttpServlet {

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
	 * Método que recibe la solicitud HTTP get y prepara los datos para la
	 * generación del modelo publicándolos en la variable de sesión
	 * archivosSeleccionados.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		try {
			// Obtiene las columnas seleccionadas en el paso anterior
			String[] seleccionados = request.getParameterValues("seleccionado");

			// Arma un listado de archivos en donde cada item tiene solo las columnas
			// seleccionadas.
			LinkedHashMap<String, Archivo> archivos = new LinkedHashMap<String, Archivo>();
			Archivo archivo = null;

			// Por cada columna seleccionada arma la información de la lista de archivos
			for (String seleccionado : seleccionados) {
				String[] valores = seleccionado.split(":");
				String nombreArchivo = valores[0];
				String indice = request.getParameter(nombreArchivo);
				String columna = valores[1];
				int posicionSeparador = nombreArchivo.indexOf('/');
				String carpeta = nombreArchivo.substring(0, posicionSeparador);
				String nombre = nombreArchivo.substring(posicionSeparador + 1);

				if (archivos.containsKey(nombreArchivo)) {
					archivo = archivos.get(nombreArchivo);
				} else {
					archivo = new Archivo();
					archivo.setNombre(nombre);
					archivo.setCarpeta(carpeta);
					archivo.setIndice(indice);
					archivos.put(nombreArchivo, archivo);
				}
				archivo.getCabecerasEscogidas().add(columna);
			}

			// Se convierte de LinkedHashMap values a ArrayList
			List<Archivo> archivosList = new ArrayList<Archivo>();
			for (Archivo archivoDatos : archivos.values()) {
				archivosList.add(archivoDatos);
			}

			// Se publica los datos en session para que los pueda leer la Servlet
			// GenerarModelo
			session.setAttribute("archivosSeleccionados", archivosList);
		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener los datos del archivo");
		}

		request.getRequestDispatcher("proyeccion3.jsp").forward(request, response);
	}

}
