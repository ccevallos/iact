package ec.iact.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Clase utilitaria que mantiene información de las rutas relativas de los
 * archivos dentro del modelo. Tiene también constantes de las urls de ciertas
 * funcionalidades y carpetas. Facilita el ajustar parámetros del sistema.
 * 
 * @author Carlos Cevallos
 *
 */
public class Parametros {
	/**
	 * Ruta de archivo de imports generales de Python
	 */
	public final static String NB_IMPORTS = "../../iamodel/plantillas/imports.txt";

	/**
	 * Ruta de archivo de imports Python para redes neuronales.
	 */
	public final static String NB_IMPORTS_RN = "../../iamodel/plantillas/importsRN.txt";
	/**
	 * Ruta del archivo de funciones utilitarias como por ejemplo para gráficos de
	 * datos y evaluación de modelos
	 */
	public final static String NB_FUNCIONES = "../../iamodel/plantillas/funciones.txt";
	/**
	 * Ruta del archivo con código para el modelo de redes neuronales.
	 */
	public final static String NB_MODELO_RN = "../../iamodel/plantillas/modeloRN.txt";
	/**
	 * Ruta del archivo con código para el modelo Random Forest.
	 */
	public final static String NB_RANDOM_FOREST = "../../iamodel/plantillas/randomForest.txt";
	/**
	 * Ruta del archivo con código para el modelo de Máquinas de Vector de Soporte.
	 */
	public final static String NB_SVM = "../../iamodel/plantillas/svm.txt";

	/**
	 * Ruta de carpeta de casos. En esta carpeta se almacenan de forma jerarquica
	 * los archivos de datos y los modelos generados.
	 */
	public final static String UBICACION_INICIO = "../../iamodel/casos";
	/**
	 * Ruta de archivo de configuración de propiedades. En este archivo se
	 * encuentran las propiedades para conexión a la base de datos para obtención de
	 * información de demanda.
	 */
	public final static String UBICACION_CONFIG_BDD = "../../iamodel/configBDD.properties";
	/**
	 * Ruta de los metadatos de un archivo Notebook. Este contenido se añade al
	 * final del Notebook e indica por ejemplo la versión de Python a utilizar.
	 */
	public final static String UBICACION_METADATA_NB = "../../iamodel/plantillas/metadata-nb.txt";
	/**
	 * Url para la servlet que permite listar los casos
	 */
	public final static String URL_LISTAR_CASOS = "ListarCasos.jsx";
	/**
	 * Url para la servlet que permite listar y cargar archivos de métricas
	 */
	public final static String URL_METRICAS = "Metricas.jsx";
	/**
	 * Url para la servlet que permite listar y cargar archivos de demanda
	 */
	public final static String URL_DEMANDA = "Demanda.jsx";
	/**
	 * Url para la ruta de página de inicio
	 */
	public final static String URL_INDEX = "index.jsp";

	/**
	 * Nombre de carpeta para demanda
	 */
	public final static String DEMANDA = "demanda";
	/**
	 * Nombre de carpeta para archivos de métricas de capacidad
	 */
	public final static String METRICA = "capacidad";
	/**
	 * Nombre de carpeta para generación de modelos
	 */
	public final static String MODELO = "modelo";

	/**
	 * Este metodo permite recuperar la información de un archivo properties
	 * 
	 * @param nombreArchivo nombre del archivo desde el cual se recupera las
	 *                      propiedades
	 * @return objeto clave-valor con propiedades recuperadas
	 * @throws Exception  en caso de errores
	 */
	public static Properties obtenerProperties(String nombreArchivo) throws Exception {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(nombreArchivo);
			properties.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Archivos.cerrar(fis);
		}

		return properties;
	}

}
