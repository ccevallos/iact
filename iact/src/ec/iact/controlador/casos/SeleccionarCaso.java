package ec.iact.controlador.casos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.util.Parametros;

/**
 * Servlet que permite seleccionar el caso sobre el que se desea trabajar. Se
 * almacena el caso seleccionado en una variable de sesión.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/SeleccionarCaso.jsx")
public class SeleccionarCaso extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP get con el nombre de caso como
	 * parámetro.para listar los casos existentes. Almacena el caso en una variable
	 * de sesión.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Se obtiene el nombre del caso
		String caso = request.getParameter("nombre");
		HttpSession session = request.getSession();

		// Se publica el caso en variable de sesión
		session.setAttribute("caso", caso);

		response.sendRedirect(Parametros.URL_INDEX);
	}

}
