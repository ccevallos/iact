package ec.iact.controlador.archivo;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import ec.iact.util.Parametros;

/**
 * Servlet creada para permitir la carga de archivos que se suben al sistema.
 * 
 * @author Carlos Cevallos
 *
 */

@WebServlet("/CargaArchivo.jsx")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, location = ".")
public class CargaArchivo extends HttpServlet {

	/**
	 * Identificador de la versión de la clase
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que recibe una petición HTTP POST en formato multipart en la cual se encuentra el archivo cargado.
	 * Este método carga en la carpeta del modelo el archivo remitido desde el navegador.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			HttpSession session = request.getSession();
			//Se obtiene el caso sobre el que se trabaja
			String caso = (String) session.getAttribute("caso");
			//Se obtiene el tipo de archivo que se carga (demanda o métrica) 
			String tipo=request.getParameter("tipo");

			// Recorre todos los archivos de carga
			for (Part part : request.getParts()) {
				String nombreArchivoCargado = part.getSubmittedFileName();

				String ubicacion_carpeta_carga = Parametros.UBICACION_INICIO + 
						File.separator + caso + File.separator+
						tipo +File.separator;

				// Crea la carpeta en el caso que no exista
				File carpetaCarga = new File(ubicacion_carpeta_carga);
				if (!carpetaCarga.exists())
					carpetaCarga.mkdirs();

				// Almacena el archivo en disco
				part.write(ubicacion_carpeta_carga + nombreArchivoCargado);
			}
		} catch (IllegalStateException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El archivo excede el tamaño permitido");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Hubo un error en la carga");
		}
	}

}
