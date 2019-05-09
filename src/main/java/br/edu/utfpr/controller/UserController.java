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
import br.edu.utfpr.error.ParamException;
import br.edu.utfpr.model.domain.Role;
import br.edu.utfpr.model.domain.User;
import br.edu.utfpr.model.mapper.UserMapper;
import br.edu.utfpr.service.RoleService;
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
    RoleService roleService = new RoleService();
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String address = "";
        if(request.getServletPath().contains(Routes.CREATE)){
            address = "/WEB-INF/view/user/register-user-form.jsp";
            request.getRequestDispatcher(address).forward(request, response);
        }
        else if(request.getServletPath().contains(Routes.DELETE)){

            String id = request.getParameter("id");

            //verifica erros de parâmetro
            List<ValidationError> errors = userService.paramValidation(id);
            boolean hasError = errors != null;

            if (hasError) {
                throw new ParamException("Parâmetros incorretos!");
            }

            //remove o usuário
            boolean isSuccess = userService.deleteUserAndRole(id);
            String message = null;
            if(isSuccess){
                message = "Usuário removido com sucesso!";
            }
            else{
                message = "Oppss! O usuário não pôde ser removido.";
            }
            address = request.getContextPath() + "/a/usuarios/listar";
            //armazena no escopo de flash
            request.setAttribute("flash.message", message);
            //como a ação altera o estado do servidor, faz redirecionamento
            response.sendRedirect(address);
        }
        else{
            //listagem
            List<User> users = userService.findAll();
            List<UserDTO> usersDTO = new ArrayList<>();

            for(User u : users){
                Role role = roleService.getById(u.getEmail());
                //não lista administradores
                if(role.getRole().equals(Constants.ADMIN)){
                    continue;
                }
                usersDTO.add(UserMapper.toDTO(u));
            }

            request.setAttribute("users", usersDTO);
            address = "/WEB-INF/view/user/list-users.jsp";
            request.getRequestDispatcher(address).forward(request, response);
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

                request.setAttribute("errors", errors);
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
        request.setAttribute("errors", errors);
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
