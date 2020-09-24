package ec.iact.controlador.ia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.modelo.Archivo;
import ec.iact.util.Archivos;
import ec.iact.util.Parametros;

/**
 * Servlet que consulta los campos de los archivos de demanda o métricas para
 * que el usuario los escoja para generar el modelo.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/Proyeccion2.jsx")
public class Proyeccion2 extends HttpServlet {

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
	 * Método que recibe la solicitud HTTP get y que consulta los campos de los
	 * archivos de demanda o métricas para que el usuario los escoja para generar el
	 * modelo
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		// Se obtiene el caso sobre el que se trabaja
		String caso = (String) session.getAttribute("caso");
		String ubicacionCarpeta = Parametros.UBICACION_INICIO + File.separator + caso + File.separator;

		try {

			// Obtiene los archivos seleccionados en el paso anterior
			String[] seleccionados = request.getParameterValues("seleccionado");

			ArrayList<Archivo> archivos = new ArrayList<Archivo>();
			
			// Por cada archivo seleccionado se obtiene la información
			for (String nombreArchivo : seleccionados) {
				File file = new File(ubicacionCarpeta + nombreArchivo);
				int posicionSeparador = nombreArchivo.indexOf('/');
				archivos.add(Archivos.fileToArchivo(file, nombreArchivo.substring(0, posicionSeparador)));
			}

			request.setAttribute("archivos", archivos);

		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener los datos del archivo");
		}

		request.getRequestDispatcher("proyeccion2.jsp").forward(request, response);
	}
}
