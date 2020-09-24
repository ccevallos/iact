<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ec.iact.util.Parametros" %>	
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="menuActivo" scope="request" value="menu_metricas" />

<jsp:include page="cabecera.jsp" flush="true">
	<jsp:param name="titulo" value="Carga de métricas de infraestructura" />
</jsp:include>

<div class="loader"></div>

<script type="text/javascript" src="js/dropzone.min.js"></script>
<script type="text/javascript" src="js/carga.js"></script>

<div>
	<div class="espacio_seccion">
		<div class="texto_seccion">Carga de archivos de datos</div>
		<form id="fileUpload" action="CargaArchivo.jsx" class="dropzone">
			<input type="hidden" name="tipo" value="<%=Parametros.METRICA%>" />
			<div class="fallback">
				<input name="file" type="file" multiple />
			</div>
		</form>

		<input type="button" value="Cargar archivos"
			onclick="fileUpload.dropzone.processQueue()" />
	</div>
	<div class="espacio_seccion">
		<div class="texto_seccion">Datos cargados</div>
		<table>
			<tr class="cabecera">
				<th>Archivo</th>
				<th>Generar CSV</th>
				<th>Fecha</th>
				<th>Tamaño (Bytes)</th>
				<th>Eliminar</th>
			</tr>
			
			<c:forEach var="archivo" items="${requestScope.archivos}">
				<tr class="lista">
					<td>
					<a href="ObtenerArchivo.jsx?tipo=<%=Parametros.METRICA%>&nombre=${archivo.nombre}"
					   alt="Obtener archivo">
					<i class="material-icons">article</i>${archivo.nombre}  </a></td>
					<td class="centrar">
					    <c:if
							test='${archivo.nombre.endsWith(".zip")}'>
							<i onclick="preprocesa('${archivo.nombre}');"
							   class="material-icons clicable">library_books</i>
						</c:if>
					</td>
					<td>
				     	${archivo.fechaModificacion}
					</td>
					<td style="width:30px;text-align:right">
				     	${archivo.dimension}
					</td>
					<td class="centrar"><a href="EliminarArchivo.jsx?tipo=<%=Parametros.METRICA%>&nombre=${archivo.nombre}"
						alt="Eliminar caso"
						onclick="return confirm('¿Está seguro que desea eliminar el archivo ${archivo.nombre}?');">
							<i class="material-icons rojo">delete</i>
					</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>
<jsp:include page="pie.jsp" flush="true" />
