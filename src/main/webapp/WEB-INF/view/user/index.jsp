<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:template title="Usuário">
	<jsp:body>
		<h1>Tela do Usuário</h1>

		<a href="u/usuarios/editar?id=${username}">Editar Perfil</a>
	</jsp:body>
</t:template>