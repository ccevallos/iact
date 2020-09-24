package ec.iact.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Clase utilitaria que facilita las actividades de conexión y ejecución de
 * sentencias hacia la Base de Datos
 * 
 * @author Carlos Cevallos
 *
 */
public class BaseDatos {

	/**
	 * Este método permite crear una conexión a la base de datos utilizando la
	 * configuración realizada en el archivo iamodel/config.properties
	 * 
	 * @param claveUsuarioBDD clave del usuario en la base de datos
	 * @return devuelve la conexión abierta a la Base de Datos
	 * @throws Exception  en caso de errores
	 */
	public static Connection crearConexion(String claveUsuarioBDD) throws Exception {
		// Obtiene las configuraciones del archivo iamodel/config.properties
		Properties config = Parametros.obtenerProperties(Parametros.UBICACION_CONFIG_BDD);
		Connection con = null;

		try {
			String driverBDD = config.getProperty("driverBDD");
			String urlBDD = config.getProperty("urlBDD");
			String usuarioBDD = config.getProperty("usuarioBDD");

			// Carga el driver
			Class.forName(driverBDD);
			// Crea la conexión
			con = DriverManager.getConnection(urlBDD, usuarioBDD, claveUsuarioBDD);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	/**
	 * Facilita un cierre seguro de la conexión
	 * @param con conexión de base de datos
	 */
	public static void cerrar(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Facilita un cierre seguro de un listado de resultados de base de datos
	 * @param rs listado de resultados de base de datos
	 */
	public static void cerrar(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Facilita un cierre seguro de un PreparedStatement
	 * @param ps objeto PreparedStatement a cerrar
	 */
	public static void cerrar(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
		}
	}

}
