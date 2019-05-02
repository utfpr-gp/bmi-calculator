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
import br.edu.utfpr.model.domain.User;
import br.edu.utfpr.model.mapper.UserMapper;
import br.edu.utfpr.service.UserService;
import br.edu.utfpr.util.Constants;
import br.edu.utfpr.util.ValidationError;

/**
 * Servlet implementation class LoginController
 */
@WebServlet(urlPatterns = {"/usuarios/cadastrar"})
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String address = "/WEB-INF/view/user/register-user-form.jsp";
        request.getRequestDispatcher(address).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        UserDTO userDTO = new UserDTO(name, email, password, repassword);
        List<ValidationError> errors = validation(userDTO);

        if (errors == null) {
            UserService userService = new UserService();
            if(request.getServletPath().contains(Constants.CREATE)){
                UserMapper userMapper = new UserMapper();
                User user = userMapper.toEntity(userDTO);
                boolean isSuccess = userService.save(user);

                if(isSuccess){
                    //formulario de login
                    String address = request.getContextPath() + "/login";
                    response.sendRedirect(address);
                }
                else{
                    //reapresenta o formulário com a mensagem de erro
                    String address = "/WEB-INF/view/user/register-user-form.jsp";

                    errors = new ArrayList<>();
                    errors.add(new ValidationError("", "Erro ao persistir os dados."));

                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher(address).forward(request, response);
                }
            }
        } else {
            //reapresenta o formulário com os erros de validação
            String address = "/WEB-INF/view/user/register-user-form.jsp";
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(address).forward(request, response);
        }

    }

    private List<ValidationError> validation(UserDTO userDTO) {
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
