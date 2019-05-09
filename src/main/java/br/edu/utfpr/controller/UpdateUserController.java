package br.edu.utfpr.controller;

import br.edu.utfpr.dto.UserDTO;
import br.edu.utfpr.error.ParamException;
import br.edu.utfpr.error.ValidationException;
import br.edu.utfpr.model.domain.User;
import br.edu.utfpr.service.UserService;
import br.edu.utfpr.error.ValidationError;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class LoginController
 */
@WebServlet(urlPatterns = {"/u/usuarios/editar"})
public class UpdateUserController extends HttpServlet {

    UserService userService = new UserService();
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        //verifica erros de parâmetro
        List<ValidationError> errors = userService.paramValidation(id);
        boolean hasError = errors != null;

        if (hasError) {
            throw new ParamException("Parâmetros incorretos!");
        }

        //verifica erro de autorização
        errors = updateValidation(id, request.getUserPrincipal());
        hasError = errors != null;

        if (hasError) {
            throw new ValidationException("Você não tem autorização para alterar este dado.");
        }

        //busca o usuário
        User user = userService.getById(id);

        //apresenta o formulário de edição com o usuário no escopo
        String address = "/WEB-INF/view/user/edit-user-form.jsp";
        request.setAttribute("user", user);
        request.getRequestDispatcher(address).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");

            UserDTO userDTO = new UserDTO(name, email, null, null);
            List<ValidationError> errors = userService.formValidation(userDTO);

            //há erro se o vetor for preenchido
            boolean hasError = errors != null;

            if (hasError) {
                //reapresenta o formulário com os erros de validação
                sendError(request, response, errors);
                return;
            }

            //id é um parâmetro hidden no formulário de edição
            errors = userService.paramValidation(request.getParameter("id"));
            hasError = errors != null;

            if (hasError) {
                //reapresenta o formulário com os erros de validação
                sendError(request, response, errors);
            }

            //atualiza os dados do usuário
            boolean isSuccess = update(request, response, userDTO);

            if (!isSuccess) {
                errors = (errors == null)? new ArrayList<>() : errors;
                errors.add(new ValidationError("", "Opss! Os dados não foram atualizados."));
                sendError(request, response, errors);
            }

            //busca o usuário atualizado
            User user = userService.getById(email);

            String address = request.getContextPath() + "/u/usuarios/editar?id=" + email;
            response.sendRedirect(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Apresenta o mesmo formulário de cadastro, mas com as mensagens de erro.
     *
     * @param request
     * @param response
     * @param errors
     * @throws ServletException
     * @throws IOException
     */
    private void sendError(HttpServletRequest request, HttpServletResponse response, List<ValidationError> errors) throws ServletException, IOException {
        //reapresenta o formulário com os erros de validação
        String address = "/WEB-INF/view/user/edit-user-form.jsp";
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(address).forward(request, response);
        return;
    }

    private boolean update(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) throws IOException, ServletException {
        String id = request.getParameter("id");
        User user = userService.getById(id);

        //nesta rota, só pode atualizar o atributo nome.
        user.setName(userDTO.getName());

        return userService.update(user);
    }

    /**
     * Validação da edição para verificar se tem autorização para modificar os dados
     *
     * @param id
     * @param userPrincipal
     * @return
     */
    private List<ValidationError> updateValidation(String id, Principal userPrincipal) {
        List<ValidationError> errors = new ArrayList<>();
        String username = userPrincipal.getName();

        if (!username.equals(id)) {
            errors.add(new ValidationError("email", "Você não tem autorização para realizar esta ação."));
        }

        return (errors.isEmpty() ? null : errors);
    }
}
