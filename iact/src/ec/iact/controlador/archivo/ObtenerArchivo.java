package ec.iact.controlador.archivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.util.Parametros;

/**
 * Esta Servlet permite que el usuario pueda descargarse el contenido de un
 * archivo que esté en el repositorio.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/ObtenerArchivo.jsx")
public class ObtenerArchivo extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP Get para obtener el archivo, incluye como
	 * parámetros el nombre del archivo y el tipo si es demanda o métrica.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		// Se obtiene el caso sobre el que se trabaja
		String caso = (String) session.getAttribute("caso");
		// Se indica el nombre del archivo que se desea descargar
		String nombre = request.getParameter("nombre");
		// Se obtiene el tipo de archivo que se carga (demanda o métrica)
		String tipo = request.getParameter("tipo");

		String nombreArchivo = Parametros.UBICACION_INICIO + File.separator + caso + File.separator + tipo
				+ File.separator + nombre;

		// Coloca el content type que corresponde ya sea zip o csv
		if (nombreArchivo.endsWith(".zip")) {
			response.setContentType("application/zip");
		} else {
			response.setContentType("text/csv");
		}

		// Indica el nombre por defecto con el que se descargaría
		response.setHeader("Content-disposition", "attachment; filename=" + nombre);

		// Se lee el archivo y se escribe el contenido para el navegador
		FileInputStream fis = null;
		try {
			// Archivo que existe en el directorio
			fis = new FileInputStream(nombreArchivo);
			// Stream para escribir al navegador
			OutputStream os = response.getOutputStream();
			int data = 0;
			while ((data = fis.read()) >= 0) {
				os.write(data);
			}
			os.flush();
			os.close();
			fis.close();
			System.out.println("Fin");
		} catch (Exception e) {
			try {
				fis.close();
			} catch (Exception e2) {
			}
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Hubo un error al obtener el archivo");
		}

	}

}
