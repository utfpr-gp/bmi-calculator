<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="Edição de Usuário">
    <jsp:body>
        <h1>Lista de Usuários</h1>

        <c:if test="${not empty message}">
            <div class="row">
                <div class="col s12">
                    <div class="card-panel blue white-text">
                            ${message}
                    </div>
                </div>
            </div>
        </c:if>

        <table class="responsive-table">
            <thead>
            <tr>
                <th>Email</th>
                <th>Nome</th>
                <th></th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td>${u.email}</td>
                    <td>${u.name}</td>
                    <td><a href="a/usuarios/remover?id=${u.email}">Remover</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </jsp:body>
</t:template>