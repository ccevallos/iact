<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="menuActivo" scope="request" value="menu_casos" />
<c:set var="info" scope="request" value="Para continuar debe seleccionar un caso." />
<c:if test='${param["borraSeleccion"]!=null}'>
   <c:set var="caso" scope="session" value="${null}" />
</c:if>

<jsp:include page="cabecera.jsp" flush="true" >
    <jsp:param name="titulo" value="Casos de análisis"/>
</jsp:include>

<div class="barra">
	<form name="frmCrearCaso" method="POST" action="CrearCaso.jsx">
		Crear caso: <input type="text" name="nombre" placeholder="Ingrese el nombre" />

		<button class="material-icons" aria-label="Crear caso" onclick="document.frmCrearCaso.submit()">add_circle</button>
	</form>
</div>
<table>
	<tr class="cabecera">
		<th>Caso</th>
		<th>Eliminar</th>
	</tr>
	<c:forEach var="caso" items="${requestScope.casos}">
		<tr class="lista">
			<td>
				<a href="SeleccionarCaso.jsx?nombre=${caso}" alt="Seleccionar a caso"><i class="material-icons">ballot</i> ${caso}</a>
			</td>
			<td class="centrar">
				<a href="EliminarCaso.jsx?nombre=${caso}" alt="Eliminar caso" onclick="return confirm('¿Está seguro que desea eliminar el caso ${caso}?');">
				<i class="material-icons rojo">delete</i></a>
			</td>
		</tr>
	</c:forEach>
</table>
<jsp:include page="pie.jsp" flush="true" />
