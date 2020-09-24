package ec.iact.filtro;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.util.Parametros;

/**
 * Filtro Web que permite verificar si que exista un caso seleccionado para las
 * funcionalides de gestion de datos de demanda y métricas y para la generación
 * del modelo.
 * 
 * @author Carlos Cevallos
 *
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "*.jsp", "*.jsx" })
public class ValidaCasoSeleccionado implements Filter {

	/**
	 * Intercepta las peticiones HTTP y verifica que exista un caso seleccionado
	 * para cualquier funcionalidad que no sea gestión de casos. Si no está
	 * seleccionado el caso se redirecciona a la página de listado de casos para que
	 * el usuario escoja uno.
	 * 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		if (session.getAttribute("caso") != null || req.getRequestURI().endsWith("Casos.jsx")
				|| req.getRequestURI().endsWith("Caso.jsx") || req.getRequestURI().endsWith("index.jsp")) {

			// permite reconocer tildes en parámetros de las JSPs
			request.setCharacterEncoding("UTF-8");

			chain.doFilter(request, response);
		} else {
			res.sendRedirect(Parametros.URL_LISTAR_CASOS);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

}
