<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" href="css/basic.min.css"></link>
<link rel="stylesheet" type="text/css" href="css/dropzone.min.css"></link>
<link rel="stylesheet" href="css/iconos.css">
<link rel="stylesheet" href="css/mdc.button.min.css">
<link rel="stylesheet" type="text/css" href="css/style.css"></link>
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<link href='https://fonts.googleapis.com/css?family=Aclonica'
	rel='stylesheet'>
	
<link  rel="icon"   href="img/icono.png" type="image/png" />
<title>${param["titulo"]}</title>
</head>


<body>

	<div class="container-fluid">
	<div class="row">
		<div class="col-12 titulo">
			<span class="texto-logo">IACT</span>
			<span class="texto-titulo">Intelicia Artificial - Capacidad Tecnológica</span>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-3 col-md-4 col-sm-12">
			<c:if test="${sessionScope.caso !=null}">
				<div class="caso">Caso: ${sessionScope.caso}
				<div style="float:right;">
				<a href="ListarCasos.jsx?borraSeleccion=true" ><i class="material-icons rojo" >unpublished</i></a>
				</div>
				</div>
			</c:if>
			<div class="vertical-menu">
				<a id="menu_inicio" href="index.jsp">Inicio</a>
				<a id="menu_casos" href="ListarCasos.jsx">Casos de Análisis</a>
					
			<c:if test="${sessionScope.caso !=null}">
				 <a id="menu_demanda" href="Demanda.jsx">Cargar Demanda Archivos</a>
				 <a id="menu_demandaBDD" href="demandaBDD.jsp">Cargar Demanda BDD</a>
				 <a id="menu_metricas" href="Metricas.jsx">Cargar Métricas Infraestructura</a>
				 <a	id="menu_modelo" href="Proyeccion1.jsx">Generar Modelo</a>
			</c:if>
			</div>
		</div>
		
		<script>
			document.getElementById('${menuActivo}').className='active';
		</script>

		<div class="col-lg-9 col-md-8 col-sm-12">
			<div class="titulo-pantalla">${param["titulo"]}</div>

			<c:if test="${requestScope.info != null}">
				<div class="info">${info}</div>
			</c:if>
			<c:if test="${requestScope.alerta != null}">
				<div class="warning">${alerta}</div>
			</c:if>
			<c:if test="${requestScope.exito != null}">
				<div class="success">${exito}</div>
			</c:if>
			<c:if test="${requestScope.error != null}">
				<div class="error">${error}</div>
			</c:if>