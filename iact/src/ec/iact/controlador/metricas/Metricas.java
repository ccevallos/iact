package ec.iact.controlador.metricas;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.modelo.Archivo;
import ec.iact.util.Parametros;

/**
 * Servlet que permite consultar los archivos de métricas de capacidad cargados
 * para presentarlos al usuario.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/Metricas.jsx")
public class Metricas extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP post para listar los archivos de métricas
	 * de capacidad existentes. Invoca al método doGet.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Método que recibe la solicitud HTTP get para listar los archivos de métricas
	 * de capacidad existentes.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String caso = (String) session.getAttribute("caso");
		String ubicacion_carpeta_caso = Parametros.UBICACION_INICIO + File.separator + caso + File.separator
				+ Parametros.METRICA;

		try {
			File carpeta_caso = new File(ubicacion_carpeta_caso);

			if (!carpeta_caso.exists())
				carpeta_caso.mkdirs();

			ArrayList<Archivo> archivosMetricas = new ArrayList<Archivo>();
			for (File file : carpeta_caso.listFiles()) {
				Archivo a = new Archivo();
				a.setNombre(file.getName());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				a.setFechaModificacion(sdf.format(new Date(file.lastModified())));
				a.setDimension(file.length());
				archivosMetricas.add(a);
			}

			// Publica los datos para poder presentarlos desde metricas.jsp
			request.setAttribute("archivos", archivosMetricas.toArray());
		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener los datos");
		}

		request.getRequestDispatcher("metricas.jsp").forward(request, response);
	}

}
