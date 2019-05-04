<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="Edição de Usuário">
    <jsp:body>
        <h1>Edição de Usuário</h1>

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

        <h4>Dados Pessoais</h4>
        <form action="u/usuarios/editar" method="POST">
            <div class="row">
                <div class="input-field col s12">
                    <input id="name" type="text" name="name" value="${user.name}"/>
                    <label for="name" class="active">Nome Completo*</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <input id="email" type="email" name="email" value="${user.email}" readonly/>
                    <label for="email" class="active">Email*</label>
                </div>
            </div>
            <input type="hidden" name="id" value="${user.email}"/>
            <p>
                <button type="submit" class="waves-effect waves-light btn green darken-3">Salvar</button>
            </p>
        </form>

        <h4>Alterar a Senha</h4>
        <form action="u/usuarios/senha" method="POST">
            <div class="row">
                <div class="input-field col s12">
                    <input id="current-password" type="password" name="current-password" disabled/>
                    <label for="current-password" class="active">Senha Atual*</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <input id="password" type="password" name="password" disabled/>
                    <label for="password" class="active">Senha*</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <input id="repassword" type="password" name="repassword" disabled/>
                    <label for="repassword" class="active">Confirmação de Senha*</label>
                </div>
            </div>
            <p>
                <button type="submit" class="waves-effect waves-light btn green darken-3">Salvar</button>
            </p>
        </form>
    </jsp:body>
</t:template>