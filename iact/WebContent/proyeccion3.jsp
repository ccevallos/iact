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

    <form id="formBDD" method="post" action="GenerarModelo.jsx">

	<div class="espacio_seccion">
		<div class="texto_seccion">Configuración de Modelo</div>
		<table style="width:90%">
			<tr class="cabecera">
				<th style="width:30%">Atributo</th>
				<th style="width:70%">Valor</th>
			</tr>
			<tr class="lista">
				<td>Nombre archivo modelo:</td>
				<td><input type="text" name="nombreModelo" required placeholder="Ingrese el nombre del archivo que se generará" value="modeloIA1" /></td>
			</tr>
			<tr class="lista">
				<td>Sobreescribir archivo:</td>
				<td><input type="checkbox" name="sobreescribe" value="si" /></td>
			</tr>
			<tr class="lista">
				<td>Contenido a incluir:</td>
				<td>
				<div>
				   <input type="checkbox" name="analisis" value="si"/>
				   Análisis de Datos
				</div>
				<div>
					<input type="checkbox" name="regresionLineal" value="si" checked />
				   Modelo de Regresion Lineal
				</div>
				<div>
					<input type="checkbox" name="randomForest" value="si" checked />
				   Modelo Random Forest
				</div>
				<div>
					<input type="checkbox" name="svm" value="si" checked />
				   Modelo Support Vector Machine
				</div>
				<div>
					<input type="checkbox" name="redNeuronal" value="si" checked />
				   Modelo de Red Neuronal
				</div>
				</td>
			</tr>
		</table>
	</div>

		<input type="submit" value="Siguiente" />

	</form>

<jsp:include page="pie.jsp" flush="true" />
