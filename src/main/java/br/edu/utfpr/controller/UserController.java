package br.edu.utfpr.controller;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.utfpr.dto.UserDTO;
import br.edu.utfpr.model.domain.Role;
import br.edu.utfpr.model.domain.User;
import br.edu.utfpr.model.mapper.UserMapper;
import br.edu.utfpr.service.UserService;
import br.edu.utfpr.util.Constants;
import br.edu.utfpr.util.Routes;
import br.edu.utfpr.util.Sha256Generator;
import br.edu.utfpr.error.ValidationError;

/**
 * Servlet implementation class LoginController
 */
@WebServlet(urlPatterns = {"/usuarios/cadastrar", "/a/usuarios/remover", "/a/usuarios/listar"})
public class UserController extends HttpServlet {

    UserService userService = new UserService();
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getServletPath().contains(Routes.CREATE)){
            String address = "/WEB-INF/view/user/register-user-form.jsp";
            request.getRequestDispatcher(address).forward(request, response);
        }
        else if(request.getServletPath().contains(Routes.DELETE)){

        }
        else{

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        UserDTO userDTO = new UserDTO(name, email, password, repassword);
        List<ValidationError> errors = formValidation(userDTO);

        //há erro se o vetor for preenchido
        boolean hasError = errors != null;

        if(hasError){
            //reapresenta o formulário com os erros de validação
            sendError(request, response, errors);
            return;
        }

        if(request.getServletPath().contains(Routes.CREATE)){
            //persiste o usuário
            boolean isSuccess = persist(request, response, userDTO);

            if(!isSuccess){
                //reapresenta o formulário com a mensagem de erro
                String address = "/WEB-INF/view/user/register-user-form.jsp";

                errors = new ArrayList<>();
                errors.add(new ValidationError("", "Erro ao persistir os dados."));

                request.setAttribute("br/edu/utfpr/error", errors);
                request.getRequestDispatcher(address).forward(request, response);
                return;
            }

            //formulario de login
            String address = request.getContextPath() + "/login";
            response.sendRedirect(address);
        }
    }

    /**
     *
     * Apresenta o mesmo formulário de cadastro, mas com as mensagens de erro.
     * @param request
     * @param response
     * @param errors
     * @throws ServletException
     * @throws IOException
     */
    private void sendError(HttpServletRequest request, HttpServletResponse response, List<ValidationError> errors) throws ServletException, IOException {
        //reapresenta o formulário com os erros de validação
        String address = "/WEB-INF/view/user/register-user-form.jsp";
        request.setAttribute("br/edu/utfpr/error", errors);
        request.getRequestDispatcher(address).forward(request, response);
    }

    /**
     *
     * Persiste o usuário.
     *
     * @param request
     * @param response
     * @param userDTO
     * @throws IOException
     * @throws ServletException
     */
    private boolean persist(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) throws IOException, ServletException {
        UserMapper userMapper = new UserMapper();
        User user = userMapper.toEntity(userDTO);

        final String hashed = Sha256Generator.generate(user.getPassword());
        user.setPassword(hashed);

        Role role = new Role(userDTO.getEmail(), Constants.USER);
        return userService.saveUserAndRole(user, role);
    }

    /**
     *
     * Valida os campos do formulário de cadastro.
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

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            errors.add(new ValidationError("password", "O campo senha é obrigatório."));
        }

        if (!userDTO.getPassword().equals(userDTO.getRepassword())) {
            errors.add(new ValidationError("password", "A confirmação da senha está diferente."));
        }

        return (errors.isEmpty() ? null : errors);
    }

}
