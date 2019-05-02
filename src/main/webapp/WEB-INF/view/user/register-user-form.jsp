<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="Cadastro de Usuário">
    <jsp:body>
        <h1>Cadastro de Usuário</h1>

        <c:if test="${not empty errors}">
            <div class="row">
                <div class="col s12">
                    <div class="card-panel red white-text">
                        <ul>
                            <c:forEach var="error" items="${errors}">
                                <li>${error.message}</li>
                                </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </c:if>

        <form action="usuarios/cadastrar" method="POST">
            <div class="row">
                <div class="input-field col s12">
                    <input id="name" type="text" name="name" />
                    <label for="name" class="active">Nome Completo*</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <input id="email" type="email" name="email" />
                    <label for="email" class="active">Email*</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <input id="password" type="password" name="password" />
                    <label for="password" class="active">Senha*</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <input id="repassword" type="password" name="repassword" />
                    <label for="repassword" class="active">Confirmação de Senha</label>
                </div>
            </div>
            <p>
                <button type="submit" class="waves-effect waves-light btn green darken-3">Enviar</button>
            </p>
        </form>
    </jsp:body>
</t:template>