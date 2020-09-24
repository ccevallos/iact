package ec.iact.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Clase utilitaria que permite crear un documento Notebook que incluye código
 * Python y comentarios en Markdown.
 * 
 * @author Carlos Cevallos
 *
 */
public class Notebook {
	/**
	 * Flujo utilizado para añadir contenido al archivo.
	 */
	private FileOutputStream fos;
	/**
	 * Writer utilizado para añadir contenido al archivo.
	 */
	private PrintWriter out;
	/**
	 * Permite conocer si se está añadiendo código en la primera celda o no. Esto
	 * permite controlar la coma que se añade para separar las celdas.
	 */
	private boolean esPrimeraCelda = true;

	/**
	 * Constructor que permite crear un archivo con el nombre dado.
	 * 
	 * @param nombreArchivo nombre del archivo Notebook que se desea crear
	 * @throws FileNotFoundException  en caso de errores
	 */
	public Notebook(String nombreArchivo) throws FileNotFoundException {
		this.fos = new FileOutputStream(nombreArchivo);
		this.out = new PrintWriter(fos);
		// Crea la cabecera del archivo notebook
		out.print("{\"cells\":[");
	}

	/**
	 * Esta clase facilita el añadir código Markdown recibidos como parámetros
	 * dinámicos. Se puede invocar así: crearMarkdown("texto1","texto2);
	 * 
	 * @param textos valores de textos markdown a incluir
	 */
	public void crearMarkdown(String... textos) {
		crearMarkdown(Arrays.asList(textos));
	}

	/**
	 * Esta clase facilita el añadir código Markdown recibidos como una lista de
	 * Strings;
	 * 
	 * @param textos valores de textos markdown a incluir
	 */
	public void crearMarkdown(List<String> textos) {
		if (!esPrimeraCelda) {
			out.print(",");
		} else {
			esPrimeraCelda = false;
		}

		out.print("{\"cell_type\": \"markdown\",\"metadata\": {},\r\n" + "  \"source\": [");
		StringJoiner sj = new StringJoiner("\\n\",");
		for (String texto : textos) {
			sj.add("\"" + texto);
		}

		out.print(sj.toString() + "\"]}\r\n");
	}

	/**
	 * Esta clase facilita el añadir código Python importado desde un archivo.
	 * 
	 * @param archivo objeto File del contenido que se desea cargar como Markdown
	 */

	public void crearCodigo(File archivo) {
		crearCodigo(archivo, null);
	}

	/**
	 * Esta clase facilita el añadir código Python importado desde un archivo y
	 * reemplaza las variables definidas en este con los valores pasados en
	 * parametrosDinamicos. Un parámetro en el archivo tiene el formato ${parametro}
	 * y en el mapa su clave sería "parametro".
	 * 
	 * @param archivo             objeto File del contenido que se desea cargar como
	 *                            Markdown
	 * @param parametrosDinamicos mapa con los valores de los parámetros dinámicos a
	 *                            reemplazar.
	 */
	public void crearCodigo(File archivo, Map<String, Object> parametrosDinamicos) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		ArrayList<String> codigoLista = new ArrayList<String>();
		try {
			fis = new FileInputStream(archivo);
			isr = new InputStreamReader(fis);
			reader = new BufferedReader(isr);
			String linea = null;
			while ((linea = reader.readLine()) != null) {
				if (parametrosDinamicos != null) {
					linea = evaluaParametros(linea, parametrosDinamicos);
				}
				codigoLista.add(linea);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Archivos.cerrar(reader);
			Archivos.cerrar(isr);
			Archivos.cerrar(fis);
		}

		crearCodigo(codigoLista);
	}

	/**
	 * Función interna utilizada en crearCodigo para reemplazar un parámetro en un
	 * texto por el valor correspondiente que se encuentre en parametrosDinamicos.
	 * 
	 * @param texto               texto sobre el cual se reemplazará el parámetro
	 * @param parametrosDinamicos mapa con los valores de los parámetros dinámicos a
	 *                            reemplazar.
	 * @return devuelve el texto con los parámetros reemplazados
	 */
	private String evaluaParametros(String texto, Map<String, Object> parametrosDinamicos) {
		String resultado = texto;

		for (String parametro : parametrosDinamicos.keySet()) {
			String valor = parametrosDinamicos.get(parametro).toString();
			resultado = texto.replace("${" + parametro + "}", valor);
		}

		return resultado;
	}

	/**
	 * Esta clase facilita el añadir código Python recibido como parámetros
	 * dinámicos. Se puede invocar así: crearCodigo("texto1","texto2);
	 * 
	 * @param textos textos de código Python a incluir
	 */

	public void crearCodigo(String... textos) {
		crearCodigo(Arrays.asList(textos));
	}

	/**
	 * Esta clase facilita el añadir código Python recibidos como una lista de
	 * Strings
	 * 
	 * @param textos textos de código Python a incluir
	 */
	public void crearCodigo(List<String> textos) {
		if (!esPrimeraCelda) {
			out.print(",");
		} else {
			esPrimeraCelda = false;
		}

		out.print("{\"cell_type\": \"code\",\"execution_count\": null,\"metadata\": {},\"outputs\": [],\r\n"
				+ "  \"source\": [");
		StringJoiner sj = new StringJoiner("\\n\",");
		for (String texto : textos) {
			sj.add("\"" + texto);
		}

		out.print(sj.toString() + "\"]}\r\n");
		out.flush();
	}

	/**
	 * Metodo interno para incluir texto de un archivo en el Notebook
	 * 
	 * @param nombreArchivo archivo a incluir
	 */
	private void copiarPlantilla(String nombreArchivo) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(nombreArchivo);
			int dato;
			String texto = "";
			while (0 <= (dato = fis.read())) {
				texto += (char) dato;
			}
			out.write(texto);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Incluye el código de finalización del Notebook y cierra la conexión al
	 * archivo
	 */
	public void cerrar() {
		try {
			copiarPlantilla(Parametros.UBICACION_METADATA_NB);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Archivos.cerrar(out);
			Archivos.cerrar(fos);
		}

	}
}
