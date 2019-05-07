<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template title="Error!!">
	<jsp:body>
		<h2>Ooops!! Alguma coisa muito errada aconteceu!</h2>
		<p>Pedimos desculpas! Falhamos e em breve iremos corrigir!</p>
		<c:if test="${not empty error}">
			<p>
				<strong>Mensagem de Erro: </strong>
				${error.message}
			</p>
		</c:if>
		<div class="row">
			<div class="col s12 center">
				<img class="img-responsive" src="resources/img/error.png" height="500px">
			</div>		
		</div>		  
	</jsp:body>
</t:template>