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
 * Servlet que se encarga de listar los casos existentes (carpetas).
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/ListarCasos.jsx")
public class ListarCasos extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP post para listar los casos existentes.
	 * Invoca al método doGet.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Método que recibe la solicitud HTTP get para listar los casos existentes.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			File ubicacionInicio = new File(Parametros.UBICACION_INICIO);

			// En caso de no existir la carpeta base, se la crea.
			if (!ubicacionInicio.exists())
				ubicacionInicio.mkdir();

			// Se obtienen las carpetas existentes, las cuales son los casos.
			String[] casos = ubicacionInicio.list();
			request.setAttribute("casos", casos);

			System.out.println("ListarCasos");

		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener los datos");
		}

		request.getRequestDispatcher("listarCasos.jsp").forward(request, response);
	}
}
