package ec.iact.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Clase utilizada para el proceso de descompresión del zip de archivos de
 * métricas de capacidad. Se tuvo que sobreescribir esta clase para controlar de
 * mejor manera el momento en que se realiza un cierre del archivo. Cuando se
 * usa el ZipInputStream desde javax.xml.parsers.DocumentBuilderFactory, este
 * último cierra el archivo después de leer el primer contenido. Al
 * sobreescribir esta clase se evita este cierre no deseado.
 * 
 * @see ZipInputStream
 * @author Carlos Cevallos
 *
 */
public class DescomprimeStream extends ZipInputStream {

	/**
	 * Se mantiene la lógica del constructor de ZipInputStream
	 * 
	 * @param in flujo de datos de entrada del archivo zip
	 */
	public DescomprimeStream(InputStream in) {
		super(in);
	}

	/**
	 * Se deshabilita el cierre con este método para evitar errores de lectura de
	 * archivos por cierre durante la lectura. Cuando se lee desde
	 */
	@Override
	public void close() throws IOException {
	}

	/**
	 * Se crea este nuevo método el cual es invocado para el cierre controlado
	 * 
	 * @throws IOException  en caso de errores
	 */
	public void cerrar() throws IOException {
		super.close();
	}
}
