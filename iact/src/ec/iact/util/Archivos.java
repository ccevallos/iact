package ec.iact.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import ec.iact.modelo.Archivo;

/**
 * Clase utilitaria para manipular archivos de texto
 * 
 * @author Carlos Cevallos
 *
 */
public class Archivos {

	/**
	 * Permite crear la carpeta padre del archivo
	 * 
	 * @param ubicacionCarpeta ruta del archivo
	 * @return devuelve la referencia a la carpeta creada como un File
	 */
	public static File carpetaCreada(String ubicacionCarpeta) {
		File carpeta = new File(ubicacionCarpeta);
		if (!carpeta.exists())
			carpeta.mkdirs();
		return carpeta;
	}

	/**
	 * Crea un value object de tipo Archivo a partir de un objeto File
	 * 
	 * @param file    objeto File
	 * @param carpeta carpeta en la que se encuentra
	 * @return Value object de tipo Archivo a partir de un objeto File.
	 */
	public static Archivo fileToArchivo(File file, String carpeta) {
		String nombreArchivo = file.getName();
		Archivo a = new Archivo();
		a.setNombre(nombreArchivo);
		a.setCarpeta(carpeta);
		a.setCabeceras(obtenerCabeceras(file));
		return a;
	}

	/**
	 * Lee la primera fila del archivo y extrae las cabecerasl
	 * 
	 * @param file archivo del cual extrae las cabeceras
	 * @return devuelve un String con las cabeceras separadas por comas
	 */
	public static String obtenerCabeceras(File file) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		String cabeceras = "";
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			reader = new BufferedReader(isr);
			cabeceras = reader.readLine();
		} catch (Exception e) {

		} finally {
			cerrar(reader);
			cerrar(isr);
			cerrar(fis);
		}
		return cabeceras;
	}

	/**
	 * Permite un cierre controlado
	 * @param is flujo a cerrar
	 */
	public static void cerrar(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Permite un cierre controlado
	 * @param r reader a cerrar
	 */
	public static void cerrar(Reader r) {
		try {
			if (r != null) {
				r.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Permite un cierre controlado
	 * @param os flujo a cerrar
	 */
	public static void cerrar(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Permite un cierre controlado
	 * @param w writer a cerrar
	 */
	public static void cerrar(Writer w) {
		try {
			if (w != null) {
				w.close();
			}
		} catch (Exception e) {
		}
	}
}
