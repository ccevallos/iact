package ec.iact.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene información de un archivo.
 * 
 * @author Carlos Cevallos
 *
 */
public class Archivo implements Serializable{
	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * Atributo nombre de archivo
	 */
	private String nombre;
	/** 
	 * Carpeta en la que se encuentra el archivo
	 */
	private String carpeta;
	/** 
	 * Fecha de última modificación del archivo
	 */
	private String fechaModificacion;
	/** 
	 * Tamaño del archivo
	 */
	private Long dimension;
	/** 
	 * String con cabeceras del archivo separadas por coma
	 */
	private String cabeceras;
	private String indice;
	private List<String> cabecerasEscogidas=new ArrayList<String>();
	
	
	public String toString() {
		return "Carpeta:" +carpeta + " Nombre:" + nombre + " Indice:" + indice;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCarpeta() {
		return carpeta;
	}
	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Long getDimension() {
		return dimension;
	}
	public void setDimension(Long dimension) {
		this.dimension = dimension;
	}
	public String getCabeceras() {
		return cabeceras;
	}
	public void setCabeceras(String cabeceras) {
		this.cabeceras = cabeceras;
	}
	public String getIndice() {
		return indice;
	}
	public void setIndice(String indice) {
		this.indice = indice;
	}
	public List<String> getCabecerasEscogidas() {
		return cabecerasEscogidas;
	}
	public void setCabecerasEscogidas(List<String> cabecerasEscogidas) {
		this.cabecerasEscogidas = cabecerasEscogidas;
	}
	
}
