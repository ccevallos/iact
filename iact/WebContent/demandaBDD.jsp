<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ec.iact.util.Parametros,java.util.Properties" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="menuActivo" scope="request" value="menu_demandaBDD" />

<jsp:include page="cabecera.jsp" flush="true">
	<jsp:param name="titulo" value="Carga de demanda" />
</jsp:include>

<% request.setAttribute("prop",Parametros.obtenerProperties(Parametros.UBICACION_CONFIG_BDD)); %>

    <form id="formBDD" method="post" action="DemandaBDD.jsx">

	<div class="espacio_seccion">
	    <div class="texto_seccion">Configuración de Base de Datos</div>
	    <div>Si desea modificar estos parámetros debe editarse el archivo "configBDD.properties"</div>
		<table style="width:90%">
			<tr class="cabecera">
				<th style="width:30%">Atributo</th>
				<th style="width:70%">Valor</th>
			</tr>
			<c:forEach var="clavePropiedad" items="${prop.keySet()}">
				<tr class="lista">
					<td>${clavePropiedad}</td>
					<td>${prop.get(clavePropiedad)}</td>
				</tr>
			</c:forEach>

			<tr class="lista">
				<td>Clave usuario</td>
				<td><input type="password" name="clave" required placeholder="Ingrese la clave" /></td>
			</tr>
			<tr class="lista">
				<td>Nombre del archivo CSV a generar</td>
				<td><input type="text" name="nombreArchivo" required placeholder="Ingrese el nombre del archivo" value="demandaBDD.csv" /></td>
			</tr>
			<tr class="lista">
				<td>Select para tipos de transacciones</td>
				<td><textarea name="selectTipos" rows="4" required style="width:100%">select distinct tipo from transaccion
				</textarea></td>
			</tr>
			<tr class="lista">
				<td>Select conteo por tipo de transacción</td>
				<td><textarea name="selectDatos" rows="4" required style="width:100%">select to_char( fecha, 'YYYY-MM-DD HH24:MI:SS') as Fecha, tipo, count(*) as numero_transacciones from transaccion where fecha>=to_timestamp('2020/08/02 23:39:00', 'YYYY/MM/DD HH24:MI:SS') group by Fecha, tipo</textarea></td>
			</tr>

		</table>
	</div>
	<input type="hidden" id="vistaPrevia" name="vistaPrevia" value="false" />
	<script type="text/javascript">
		function submitVistaPrevia(){
			document.getElementById("vistaPrevia").value="true";
			var formValid = document.getElementById("formBDD").checkValidity();
            return formValid;
		}
	</script>
	
    <input type="submit" value="Vista Previa" onclick="submitVistaPrevia();"/>&nbsp;<input type="submit" value="Obtener datos"/>
    </form>


	<div class="espacio_seccion">
	    <div class="texto_seccion">Vista Previa</div>
		<table style="width:90%">
			<c:forEach var="datosPrevio" items="${datosPrevios}">
				<tr class="lista">
					<td>${datosPrevio}</td>
				</tr>
			</c:forEach>
		</table>
	</div>

<jsp:include page="pie.jsp" flush="true" />
