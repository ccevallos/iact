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
 * Servlet que consulta los archivos de demanda o métricas para que el usuario
 * los escoja para generar el modelo.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/Proyeccion1.jsx")
public class Proyeccion1 extends HttpServlet {

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
	 * Método que recibe la solicitud HTTP get y consulta los archivos de demanda o
	 * de métricas para que el usuario los escoja.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		// Se obtiene el caso sobre el que se trabaja
		String caso = (String) session.getAttribute("caso");
		String ubicacionCarpetaDemanda = Parametros.UBICACION_INICIO + File.separator + caso + File.separator
				+ Parametros.DEMANDA;
		String ubicacionCarpetaMetrica = Parametros.UBICACION_INICIO + File.separator + caso + File.separator
				+ Parametros.METRICA;

		try {
			File carpetaDemanda = Archivos.carpetaCreada(ubicacionCarpetaDemanda);
			File carpetaMetrica = Archivos.carpetaCreada(ubicacionCarpetaMetrica);

			// Obtiene archivos de demanda
			ArrayList<Archivo> archivos = new ArrayList<Archivo>();
			for (File file : carpetaDemanda.listFiles()) {
				if (file.getName().toLowerCase().endsWith(".csv")) {
					archivos.add(Archivos.fileToArchivo(file, Parametros.DEMANDA));
				}
			}
			// Obtiene archivos de capacidad
			for (File file : carpetaMetrica.listFiles()) {
				if (file.getName().toLowerCase().endsWith(".csv")) {
					archivos.add(Archivos.fileToArchivo(file, Parametros.METRICA));
				}
			}

			request.setAttribute("archivos", archivos);

		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener los datos del archivo");
		}

		request.getRequestDispatcher("proyeccion1.jsp").forward(request, response);
	}

}
