package ec.iact.controlador.casos;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ec.iact.util.Parametros;

/**
 * Servlet que se encarga de crear un caso. Un caso en el sistema se refleja
 * como una carpeta dentro del directorio del modelo.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/CrearCaso.jsx")
public class CrearCaso extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP Post para crear un caso. Por parámetro se
	 * recibe el nombre del caso.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Se obtiene el nombre del caso a crear
		String caso = (String) request.getParameter("nombre");
		File carpetaCaso = new File(Parametros.UBICACION_INICIO + File.separator + caso);
		// Se crea el caso como una carpeta
		carpetaCaso.mkdirs();
		System.out.println(carpetaCaso.getAbsolutePath());
		response.sendRedirect(Parametros.URL_LISTAR_CASOS);
	}

}
