package ec.iact.controlador.ia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.iact.modelo.Archivo;
import ec.iact.util.Archivos;
import ec.iact.util.Notebook;
import ec.iact.util.Parametros;

/**
 * Servlet que se encarga de Generar el Modelo Inteligente en formato Jupyter
 * Notebook. Recibe como parámetros el nombre del modelo y las características
 * que debe incluir en la generación.
 * 
 * @author Carlos Cevallos
 *
 */
@WebServlet("/GenerarModelo.jsx")
public class GenerarModelo extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe la solicitud HTTP post con todos los parámetros del
	 * formulario. En base a estos datos se genera el archivo Notebook con
	 * comentários Markdown y el código Python necesario.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		// Se obtiene el caso sobre el que se trabaja
		String caso = (String) session.getAttribute("caso");
		// Se obtiene el listado de archivos y campos seleccionados en las pantallas
		// anteriores del wizard
		@SuppressWarnings("unchecked")
		List<Archivo> archivos = (List<Archivo>) session.getAttribute("archivosSeleccionados");

		// Se obtiene el nombre del modelo (archivo)
		String nombreModelo = request.getParameter("nombreModelo");
		// Se obtiene indicador que indica si se sobreescribe o no el archivo en caso
		// que exista.
		String sobreescribe = request.getParameter("sobreescribe");

		// Obtiene las características que se deben incluir en el modelo.
		String analisis = request.getParameter("analisis");
		String regresionLineal = request.getParameter("regresionLineal");
		String randomForest = request.getParameter("randomForest");
		String svm = request.getParameter("svm");
		String redNeuronal = request.getParameter("redNeuronal");
		String ubicacionCarpeta = Parametros.UBICACION_INICIO + File.separator + caso + File.separator
				+ Parametros.MODELO;
		String nombreArchivo = ubicacionCarpeta + File.separator + nombreModelo + ".ipynb";

		// contador utilizado para generár la dimensión de entrada del modelo de red neuronal 
		int numeroCabecerasDemanda = 0;
		try {
			// contador que se usa para enumerar los titulos markdown
			int nroTitulo = 1;

			Archivos.carpetaCreada(ubicacionCarpeta);
			
			File modelo = new File(nombreArchivo);
			
			// valida si el archivo existe
			if (modelo.exists()) {
				// si existe y se solicita sobreescribir se elimina el archivo
				if (sobreescribe != null) {
					modelo.delete();
				} else {
					// si no sobreescribe y existe el archivo, se arroja un error.
					request.setAttribute("error", "Existe un archivo con ese nombre y no se seleccionó sobreescribir");
					request.getRequestDispatcher("proyeccion3.jsp").forward(request, response);
					return;
				}
			}

			// Se crea el objeto utilitario que permite crear el archivo notebook
			Notebook n = new Notebook(nombreArchivo);

			// Se crean comentarios iniciales
			n.crearMarkdown("# Modelo " + nombreModelo);
			n.crearMarkdown("El presente modelo ha sido generado por la herramienta IACT para el caso: " + caso);
			n.crearMarkdown(new String[] { "## " + nroTitulo++ + ". Carga de librerias y funciones utilitarias",
					"Se importan las librerías necesarias" });

			// Se incluye contenido de archivo de imports generales
			n.crearCodigo(new File(Parametros.NB_IMPORTS));

			// Se incluyen imports de red neuronal si se pide incluir este modelo
			if (redNeuronal != null && redNeuronal.equals("si")) {
				n.crearMarkdown("Imports para red neuronal");
				n.crearCodigo(new File(Parametros.NB_IMPORTS_RN));
			}
			
			// Se incluyen funciones utilitarias
			n.crearMarkdown("Funciones utilitarias");
			n.crearCodigo(new File(Parametros.NB_FUNCIONES));

			// Se genera el código para la preparación de datos
			n.crearMarkdown("## " + nroTitulo++ + ". Preparación de datos");
			
			// Cada lista guarda secciones de código que después se agregan al notebook
			List<String> cargaArchivos = new ArrayList<String>();
			List<String> analisisArchivos = new ArrayList<String>();
			List<String> ajustarIndices = new ArrayList<String>();
			List<String> agruparDatos = new ArrayList<String>();
			List<String> ordenarArchivos = new ArrayList<String>();
			List<String> joinArchivos = new ArrayList<String>();
			List<String> metricasCabeceras = new ArrayList<String>();

			String nombreAnterior = null;
			StringJoiner colDemandaSJ = new StringJoiner("','");
			
			// Se itera por cada archivo ya sea de demanda o de capacidad
			for (Archivo archivo : archivos) {
				// Controla si el archivo tiene caracter '-' y lo pasa a '_'
				String nombre = archivo.getNombre().substring(0, archivo.getNombre().length() - 4).replace("-", "_");

				// Genera código para cargar datos a pandas
				String carga = nombre + "_data = pd.read_csv('../" + archivo.getCarpeta() + "/" + archivo.getNombre()
						+ "')";
				// Genera código para usar fecha a nivel de minutos y no segundos
				String ajustarIndice = nombre + "_data['" + archivo.getIndice() + "'] = " + nombre + "_data['"
						+ archivo.getIndice() + "'].str[:-3]";
				
				// Genera código de agrupación de datos
				String agruparDato = nombre + "_data = " + nombre + "_data.groupby(['" + archivo.getIndice()
						+ "']).agg({";

				// Genera código para ordenar los datos por la columna indice
				String ordenar = nombre + "_data = " + nombre + "_data.sort_values('" + archivo.getIndice() + "')";

				StringJoiner agruparDatoCabecerasSJ = new StringJoiner(",");

				List<String> cabeceras = new ArrayList<String>();

				// permite extraer las columnas de la demanda para obtener X
				// y permite extraer las columnas de las métricas para obtener Y
				for (String cabecera : archivo.getCabecerasEscogidas()) {
					if (!cabecera.equals(archivo.getIndice())) {
						if (archivo.getCarpeta().equals(Parametros.DEMANDA)) {
							colDemandaSJ.add(cabecera);
							// para la demanda se suma el número de transacciones en cada minuto
							agruparDatoCabecerasSJ.add("'" + cabecera + "':sum");
							numeroCabecerasDemanda++;
						} else {
							metricasCabeceras.add(cabecera);
							// para capacidad se toma el mayor valor.
							// de requerir el usuario podría ajustar sobre el código generado.
							agruparDatoCabecerasSJ.add("'" + cabecera + "':max");
						}
						cabeceras.add(cabecera);
					}
				}

				agruparDato += agruparDatoCabecerasSJ.toString() + "})";

				// Genera código de union de archivos con join de pandas
				if (nombreAnterior != null) {
					String join = "consolidado_data=" + nombreAnterior + ".join(" + nombre + "_data,how='inner')";
					joinArchivos.add(join);
					nombreAnterior = "consolidado_data";
				} else {
					nombreAnterior = nombre + "_data";
				}

				// Se añaden a las listas de código para después genera el archivo
				cargaArchivos.add(carga);
				
				// Código para análisis de datos
				analisisArchivos.add(nombre + "_data.head(5)");
				analisisArchivos.add(nombre + "_data.describe()");
				for (String cabecera : cabeceras) {
					analisisArchivos.add("dibuja_linea([" + nombre + "_data['" + cabecera + "']], 'Grafico')");
				}

				ajustarIndices.add(ajustarIndice);
				agruparDatos.add(agruparDato);
				ordenarArchivos.add(ordenar);
			}

			// El código generado se envía al archivo notebook
			
			n.crearMarkdown("Carga de datos desde los archivos cvs");
			n.crearCodigo(cargaArchivos);

			// Solo se incluye si el usuario escogió generar el análisis
			// Presenta el análisis de los datos antes del preprocesamiento
			if (analisis != null && analisis.equals("si")) {
				n.crearMarkdown("## " + nroTitulo++ + ". Analisis de datos originales");
				for (String analisisA : analisisArchivos) {
					n.crearCodigo(analisisA);
				}
			}

			n.crearMarkdown("Ajustar datos del indice");
			n.crearCodigo(ajustarIndices);
			n.crearMarkdown("Agrupar datos del indice");
			n.crearCodigo(agruparDatos);
			n.crearMarkdown("Ordena datos por indice");
			n.crearCodigo(ordenarArchivos);
			n.crearMarkdown("Se une la información por indices");
			n.crearCodigo(joinArchivos);
			n.crearMarkdown("Procesamiento adicional de datos");
			n.crearCodigo("#Aquí puede agregar setencias adicionales de preprocesamiento de datos");

			n.crearMarkdown("Se extran los valores de X y Y");
			n.crearCodigo("X=consolidado_data.loc[:,['" + colDemandaSJ.toString() + "']]");
			n.crearMarkdown("Se normalizan los valores de X entre 0 y 1");
			n.crearCodigo("normalizar_por_mayor(X)");

			// Solo se incluye si el usuario escogió generar el análisis
			// Presenta el análisis de los datos ya preprocesados
			if (analisis != null && analisis.equals("si")) {
				n.crearMarkdown("## " + nroTitulo++ + ". Analisis de datos preparados");
				for (String analisisA : analisisArchivos) {
					n.crearCodigo(analisisA);
				}
				n.crearCodigo("consolidado_data.head(5)");
				n.crearCodigo("consolidado_data.describe()");
			}

			// Se genera esta sección si se seleccionó al menos un modelo IA
			if ((regresionLineal != null && regresionLineal.equals("si"))
					|| (randomForest != null && randomForest.equals("si")) || (svm != null && svm.equals("si"))
					|| (redNeuronal != null && redNeuronal.equals("si"))) {

				n.crearMarkdown("## " + nroTitulo + ". Modelos de predicción");

				int i = 1;
				
				// Por cada métrica de capacidad se generan los modelos de pronóstico
				for (String metrica : metricasCabeceras) {
					
					n.crearMarkdown("### " + nroTitulo + "." + i + " Metrica: " + metrica);
					
					// Genera código para separar los datos de entrenamiento y, de métricas ya conocidas
					n.crearCodigo("y = consolidado_data['" + metrica + "']",
							"#se sugiere descomentar la siguiente linea para convertir bytes a MB",
							"#mejora la capacidad de predicción con números más pequeños", "#y=y/(1024*1024)");
					
					// Genera código para separación de datos de entrenamiento y pruebas
					n.crearMarkdown("Se separa datos de entrenamiento y pruebas");
					n.crearCodigo(
							"X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20, random_state = 123)");
					n.crearCodigo("resultados=list()");
					n.crearCodigo("algoritmos=list()");

					// Genera modelo de regresión lineal, si el usuario lo escogió
					if (regresionLineal != null && regresionLineal.equals("si")) {
						n.crearMarkdown("**Modelo de regresión lineal**");
						n.crearCodigo("lReg = LinearRegression().fit(X_train, y_train)", "y_pred=lReg.predict(X_test)",
								"resultados.append(y_pred)", "algoritmos.append('Regresion Lineal')");
					}
					// Genera modelo de Random forest, si el usuario lo escogió
					if (randomForest != null && randomForest.equals("si")) {
						n.crearMarkdown("**Modelo Random Forest**");
						n.crearCodigo(new File(Parametros.NB_RANDOM_FOREST));
					}
					// Genera modelo de máquinas de vector de soporte, si el usuario lo escogió
					if (svm != null && svm.equals("si")) {
						n.crearMarkdown("**Modelo Support Vector Machine (Regression)**");
						n.crearCodigo(new File(Parametros.NB_SVM));
					}
					// Genera modelo de red neuronal, si el usuario lo escogió
					if (redNeuronal != null && redNeuronal.equals("si")) {
						n.crearMarkdown("**Modelo Red Neuronal**");
						Map<String, Object> parametros = new Hashtable<String, Object>();
						// se define el parametro dinámico del número de atributos en X
						// se usa para la dimension de entrada de la red
						parametros.put("dimensionEntrada", numeroCabecerasDemanda);
						n.crearCodigo(new File(Parametros.NB_MODELO_RN), parametros);
					}

					// Genera código para evaluar los resultados del modelo entrenado
					n.crearCodigo("df=evalua(y_test, resultados, algoritmos)", "display(df)");

					i++;
				}
			}
			n.cerrar();

		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener los datos del archivo");
			e.printStackTrace();
		}

		// Genera mensaje de éxito y con un link del modelo para abrir con Jupyter Notebook.
		
		request.setAttribute("exito", "Modelo <a href='http://localhost:8888/notebooks/" + caso + "/modelo/"
				+ nombreModelo + ".ipynb' target='_blank'>" + nombreModelo + "</a> generado exitosamente");
		request.getRequestDispatcher("proyeccion3.jsp").forward(request, response);
	}
}
