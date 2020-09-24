package ec.iact.controlador.archivo;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.util.Parametros;

/**
 * Esta Servlet permite eliminar un archivo que se encuentre en la carpeta del
 * modelo.
 *
 * @author Carlos Cevallos
 *
 */
@WebServlet("/EliminarArchivo.jsx")
public class EliminarArchivo extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe las peticiones HTTP Get. Elimina un archivo dado el caso,
	 * tipo (demanda o metrica) y nombre.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		//Se obtiene el caso sobre el que se trabaja
		String caso = (String) session.getAttribute("caso");
		//Se obtiene el nombre del archivo que se desea eliminar 
		String nombre = request.getParameter("nombre");
		//Se obtiene el tipo de archivo que se carga (demanda o métrica) 
		String tipo = request.getParameter("tipo");

		String nombreArchivo = Parametros.UBICACION_INICIO + File.separator + caso + File.separator + tipo
				+ File.separator + nombre;

		File carpetaCaso = new File(nombreArchivo);
		
		//Se elimina el archivo
		carpetaCaso.delete();
		if (tipo.equals(Parametros.DEMANDA)) {
			response.sendRedirect(Parametros.URL_DEMANDA);
		} else {
			response.sendRedirect(Parametros.URL_METRICAS);
		}
	}

}
