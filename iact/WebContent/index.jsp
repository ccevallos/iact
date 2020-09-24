<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<c:if test="${sessionScope.caso ==null}">
	<c:set var="info" scope="request" value="Para continuar debe seleccionar un caso en la opción 'Casos de análisis'." />
</c:if>
<c:set var="menuActivo" scope="request" value="menu_inicio" />

<jsp:include page="cabecera.jsp" flush="true">
    <jsp:param name="titulo" value="Inicio"/>
</jsp:include>

<jsp:include page="pie.jsp" flush="true" />
