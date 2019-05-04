package br.edu.utfpr.controller;

import br.edu.utfpr.dto.UserDTO;
import br.edu.utfpr.model.domain.Role;
import br.edu.utfpr.model.domain.User;
import br.edu.utfpr.model.mapper.UserMapper;
import br.edu.utfpr.service.UserService;
import br.edu.utfpr.util.Constants;
import br.edu.utfpr.util.ValidationError;

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
        try {
            //verifica erros de parâmetro
            List<ValidationError> errors = paramValidation(id);
            boolean hasError = errors != null;

            if (hasError) {
                //TODO Exception 400

                return;
            }

            //verifica erro de autorização
            errors = updateValidation(id, request.getUserPrincipal());
            hasError = errors != null;

            if (hasError) {
                //TODO Exception 400
                return;
            }

            //busca o usuário
            User user = userService.getById(id);

            //apresenta o formulário de edição com o usuário no escopo
            String address = "/WEB-INF/view/user/edit-user-form.jsp";
            request.setAttribute("user", user);
            request.getRequestDispatcher(address).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        UserDTO userDTO = new UserDTO(name, email, null, null);
        List<ValidationError> errors = formValidation(userDTO);

        //há erro se o vetor for preenchido
        boolean hasError = errors != null;

        if (hasError) {
            //reapresenta o formulário com os erros de validação
            sendError(request, response, errors);
            return;
        }

        //id é um parâmetro hidden no formulário de edição
        errors = paramValidation(request.getParameter("id"));
        hasError = errors != null;

        if (hasError) {
            //reapresenta o formulário com os erros de validação
            sendError(request, response, errors);
            return;
        }

        //atualiza os dados do usuário
        hasError = update(request, response, userDTO);

        if(hasError){
            //TODO dispara excption para abrir a página de erro
        }

        //busca o usuário atualizado
        User user = userService.getById(email);

        String address = request.getContextPath() + "/u/usuarios/editar?id=" + email;
        response.sendRedirect(address);
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

    /**
     * Valida o parâmetro id usado em edições e remoções
     *
     * @param id
     * @return
     */
    private List<ValidationError> paramValidation(String id) {
        List<ValidationError> errors = new ArrayList<>();

        if (id == null || id.isEmpty()) {
            errors.add(new ValidationError("id", "O identificador do item é obrigatório."));
        }

        User user = userService.getById(id);
        if (user == null) {
            errors.add(new ValidationError("id", "O item não foi encontrado."));
        }
        return (errors.isEmpty() ? null : errors);
    }

    /**
     * Valida os campos do formulário de dados pessoais.
     *
     * @param userDTO
     * @return
     */
    private List<ValidationError> formValidation(UserDTO userDTO) {
        List<ValidationError> errors = new ArrayList<>();

        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            errors.add(new ValidationError("name", "O campo nome é obrigatório."));
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            errors.add(new ValidationError("email", "O campo email é obrigatório."));
        }

        return (errors.isEmpty() ? null : errors);
    }

}
