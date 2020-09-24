package ec.iact.controlador.demanda;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.util.Archivos;
import ec.iact.util.BaseDatos;
import ec.iact.util.Parametros;

/**
 * Servlet que permite extraer los datos de la Base y generar un archivo CSV. La
 * servlet recibe por parámetros: el nombre del archivo a generar, la clave del
 * usuario de BDD, un indicador de vista previa o generación de archivo y los
 * selects que se ejecutarán a la base de datos.
 * 
 * El primer select obtiene los tipos de transacciones y el segundo obtiene los
 * datos que se sumarizan.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/DemandaBDD.jsx")
public class DemandaBDD extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP post para generar la vista previa o el
	 * archivo CSV. Invoca al método doGet.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Método que recibe la solicitud HTTP get para generar la vista previa o el
	 * archivo CSV.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			// Se obtiene el caso sobre el que se trabaja
			String caso = (String) session.getAttribute("caso");

			// Se obtienen los demás parámetros del formulario
			String nombreArchivo = request.getParameter("nombreArchivo");
			String archivoDestino = Parametros.UBICACION_INICIO + File.separator + caso + File.separator
					+ Parametros.DEMANDA + File.separator + nombreArchivo;
			String claveBDD = request.getParameter("clave");
			String selectTipos = request.getParameter("selectTipos");
			String selectDatos = request.getParameter("selectDatos");
			String vistaPrevia = request.getParameter("vistaPrevia");

			// Se asegura que se hayan llenado todos los campos
			if (claveBDD.equals("") || selectTipos.equals("") || selectDatos.equals("")) {
				request.setAttribute("error",
						"Campo no llenado, revise que haya llenado todos los campos del formulario");
			} else {

				// Se crea la conexión a la base de datos
				Connection con = BaseDatos.crearConexion(claveBDD);
				// Se ejecuta el select de consulta de tipos
				String[] tipos = obtenerTiposTransacciones(con, selectTipos);

				// Si la solicitud es vista previa. Se toman los primeros registros y se publica
				// en request para su presentación en pantalla.
				if (vistaPrevia != null && vistaPrevia.equals("true")) {
					String[] datos = vistaPreviaDatosDemanda(con, archivoDestino, selectDatos, tipos);
					request.setAttribute("datosPrevios", datos);
					// Si es generación de archivo.
				} else {
					generaArchivoDemanda(con, archivoDestino, selectDatos, tipos);
					request.setAttribute("exito", "Archivo: <a href='ObtenerArchivo.jsx?tipo=" + Parametros.DEMANDA
							+ "&nombre=" + nombreArchivo + "' >" + nombreArchivo + "</a> generado exitosamente");
				}
				BaseDatos.cerrar(con);
			}
		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener la información: " + e.getMessage());
			e.printStackTrace();
		}

		request.getRequestDispatcher("demandaBDD.jsp").forward(request, response);
	}

	/**
	 * Este método permite obtener la información de los tipos de transacciones
	 * 
	 * @param con         corresponde a la conexión creada hacia la base de datos.
	 * @param selectTipos sentencia select que permite obtener los tipos de datos
	 * @return retorna un arreglo de los tipos de transacciones devuelto por el
	 *         select
	 * @throws Exception  en caso de errores
	 */
	public String[] obtenerTiposTransacciones(Connection con, String selectTipos) throws Exception {
		PreparedStatement stm = con.prepareStatement(selectTipos);
		ResultSet rs = stm.executeQuery();

		List<String> tipos = new ArrayList<String>();

		while (rs.next()) {
			String tipo = rs.getString(1);
			tipos.add(tipo);
		}

		return tipos.toArray(new String[0]);
	}

	/**
	 * Este método permite ejecutar la consulta a la base de datos y generar el
	 * archivo CSV
	 * 
	 * @param c                    objeto que tiene la conexión a la Base de Datos
	 * @param nombreArchivoDestino nombre del archivo que debe generar
	 * @param selectDemanda        select que ejecutará para traer los datos de
	 *                             demanda
	 * @param tiposTransaccion     tipos de transacciones que se registrarán en el
	 *                             CSV
	 * 
	 * @throws Exception en caso de error
	 */
	public void generaArchivoDemanda(Connection c, String nombreArchivoDestino, String selectDemanda,
			String[] tiposTransaccion) throws Exception {
		// StringJoiner es una clase que permite concatenar textos separados por coma
		StringJoiner titulos = new StringJoiner(",");
		// Coloca el título de la primera columna que debe ser la fecha
		titulos.add("Fecha");
		// Se crea un indice de posiciones dado un tipo de transaccion
		HashMap<String, Integer> indiceTipos = new HashMap<String, Integer>();

		// Añade a las cabeceras el resto de columnas con el prefijo "nro_" ya que será
		// la carga transaccional de cada tipo (número de transacciones).
		for (int cont = 0; cont < tiposTransaccion.length; cont++) {
			indiceTipos.put(tiposTransaccion[cont], cont);
			titulos.add("nro_" + tiposTransaccion[cont]);
		}

		// Se crea un treemap para que los datos mantengan un orden en base a la clave
		// que en este caso es la fecha
		TreeMap<String, long[]> datos = new TreeMap<String, long[]>();

		// Prepara el select
		PreparedStatement stm = c.prepareStatement(selectDemanda);

		// Ejecuta la consulta y se obtienen los datos
		ResultSet rs = stm.executeQuery();

		// Se itera por cada fila de datos. Una fila tiene la carga de un tipo de
		// transacción específico.
		while (rs.next()) {
			String fecha = rs.getString(1);
			String tipo = rs.getString(2);
			Long nro_transacciones = rs.getLong(3);

			// Si no existe un dato con la fecha se lo agrega
			if (!datos.containsKey(fecha)) {
				datos.put(fecha, new long[tiposTransaccion.length]);
			}
			// Concatena carga de una misma fecha
			datos.get(fecha)[indiceTipos.get(tipo)] = nro_transacciones;
		}

		BaseDatos.cerrar(rs);
		BaseDatos.cerrar(stm);

		// Crea el archivo CSV
		FileOutputStream fos = new FileOutputStream(nombreArchivoDestino);
		PrintWriter pw = new PrintWriter(fos);
		
		// Se guardan las cabeceras
		pw.print(titulos.toString());

		// Se itera por los datos y se transforman a CSV
		for (String fecha : datos.keySet()) {
			StringJoiner datosCSV = new StringJoiner(",");
			datosCSV.add(fecha);
			long[] valores = datos.get(fecha);
			
			for (long valor : valores) {
				datosCSV.add(String.valueOf(valor));
			}
			pw.print("\n" + datosCSV.toString());
		}

		pw.flush();
		Archivos.cerrar(pw);
		Archivos.cerrar(fos);
	}

	/**
	 * Este método permite ejecutar la consulta a la base de datos y presentar los
	 * datos de los primeros 5 registros
	 * 
	 * @param c                    objeto que tiene la conexión a la Base de Datos
	 * @param nombreArchivoDestino nombre del archivo que debe generar
	 * @param selectDemanda        select que ejecutará para traer los datos de
	 *                             demanda
	 * @param tiposTransaccion     tipos de transacciones que se registrarán en el
	 *                             CSV
	 * @return devuelve el listado de textos CSV de cabecera y 5 primeros registros 
	 * @throws Exception en caso de error
	 */
	public String[] vistaPreviaDatosDemanda(Connection c, String nombreArchivoDestino, String selectDemanda,
			String[] tiposTransaccion) throws Exception {
		// StringJoiner es una clase que permite unir textos separandolos por coma
		StringJoiner titulos = new StringJoiner(",");
		// Coloca el título de la primera columna que debe ser la fecha
		titulos.add("Fecha");
		// Se crea un indice de posiciones dado un tipo de transaccion
		HashMap<String, Integer> indiceTipos = new HashMap<String, Integer>();

		// Añade a las cabeceras el resto de columnas con el prefijo "nro_" ya que será
		// la carga transaccional de cada tipo (número de transacciones).
		for (int cont = 0; cont < tiposTransaccion.length; cont++) {
			indiceTipos.put(tiposTransaccion[cont], cont);
			titulos.add("nro_" + tiposTransaccion[cont]);
		}

		// Se crea un treemap para que los datos mantengan un orden en base a la clave
		// que en este caso es la fecha
		TreeMap<String, long[]> datos = new TreeMap<String, long[]>();

		// Prepara el select
		PreparedStatement stm = c.prepareStatement(selectDemanda);

		// Ejecuta la consulta y se obtienen los datos
		ResultSet rs = stm.executeQuery();

		boolean datosPruebaCompletos = false;

		// Se itera por cada fila de datos. Una fila tiene la carga de un tipo de
		// transacción específico.
		while (rs.next() && !datosPruebaCompletos) {
			String fecha = rs.getString(1);
			String tipo = rs.getString(2);
			Long nro_transacciones = rs.getLong(3);

			if (!datos.containsKey(fecha) && datos.size() == 5) {
				// Bandera que indica que se completó los 5 registros y termina el trabajo
				datosPruebaCompletos = true;
			} else {

				// Si no existe un dato con la fecha se lo agrega
				if (!datos.containsKey(fecha)) {
					datos.put(fecha, new long[tiposTransaccion.length]);
				}
				// Concatena carga de una misma fecha
				datos.get(fecha)[indiceTipos.get(tipo)] = nro_transacciones;
			}
		}

		List<String> datosPreliminar = new ArrayList<String>();
		datosPreliminar.add(titulos.toString());
		
		// arma los datos como CSV
		for (String fecha : datos.keySet()) {
			StringJoiner datosCSV = new StringJoiner(",");
			datosCSV.add(fecha);
			long[] valores = datos.get(fecha);
			for (long valor : valores) {
				datosCSV.add(String.valueOf(valor));
			}
			datosPreliminar.add("\n" + datosCSV.toString());
		}
		return datosPreliminar.toArray(new String[0]);
	}

}
