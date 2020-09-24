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
 * Servlet que se encarga de eliminar un caso dado su nombre.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/EliminarCaso.jsx")
public class EliminarCaso extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;
       
	/**
	 * Método que recibe la solicitud HTTP Get para eliminar un caso. Por parámetro se
	 * recibe el nombre del caso.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String caso = (String)request.getParameter("nombre");
		File carpetaCaso=new File(Parametros.UBICACION_INICIO+File.separator+caso);
		carpetaCaso.delete();
		response.sendRedirect(Parametros.URL_LISTAR_CASOS);
	}

}
