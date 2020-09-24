<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ec.iact.util.Parametros" %>	

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<c:if test="${sessionScope.caso ==null}">
	<c:set var="info" scope="request" value="Para continuar debe seleccionar un caso en la opción 'Casos de análisis'." />
</c:if>
<c:set var="menuActivo" scope="request" value="menu_modelo" />

<jsp:include page="cabecera.jsp" flush="true">
    <jsp:param name="titulo" value="Proyección de Capacidad"/>
</jsp:include>


	<div class="espacio_seccion">
		<div class="texto_seccion">Generar análisis de datos</div>
		<form method="post" action="Proyeccion2.jsx">

		<table>
			<tr class="cabecera">
				<th><input type="checkbox" id="todos" /> Seleccionar</th>
				<th>Tipo</th>
				<th>Archivo</th>
				<th>Columnas Archivo</th>
			</tr>
			<c:forEach var="archivo" items="${requestScope.archivos}">
			<c:if test='${archivo.carpeta.equals("demanda")}'>
				<c:set var="estiloTipoArchivo" scope="page" value="demanda" />
			</c:if>
			<c:if test='${!archivo.carpeta.equals("demanda")}'>
				<c:set var="estiloTipoArchivo" scope="page" value="metrica" />
			</c:if>
			
			
				<tr class="lista ${estiloTipoArchivo}">

					<td style="text-align:center">
						<input type="checkbox" name="seleccionado" value="${archivo.carpeta}/${archivo.nombre}" />
					</td>
					<td>
				     	${archivo.carpeta}
					</td>
					<td>
					<a class="${estiloTipoArchivo}" href="ObtenerArchivo.jsx?tipo=<%=Parametros.DEMANDA%>&nombre=${archivo.nombre}"
					   alt="Obtener archivo">

					<i class="material-icons ${estiloTipoArchivo}">article</i>${archivo.nombre}  </a></td>
					<td style="width:30px">
				     	${archivo.cabeceras}
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<input type="submit" value="Siguiente" />
		
		
   		</form>
	</div>

<script type="text/javascript" >
   document.getElementById('todos').onclick = function() {
	  var checkboxes = document.getElementsByName('seleccionado');
	  for (var checkbox of checkboxes) {
	    checkbox.checked = this.checked;
	  }
	}
</script>


<jsp:include page="pie.jsp" flush="true" />
