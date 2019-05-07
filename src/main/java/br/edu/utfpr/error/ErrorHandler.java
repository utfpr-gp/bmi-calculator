package br.edu.utfpr.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ErrorHandler
 */
@WebServlet("/error-handler")
public class ErrorHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer)
				request.getAttribute("javax.servlet.error.status_code");

		ValidationError error = null;

		if(throwable != null){
			System.out.println("Erro de exception" + throwable.getMessage());
			error = new ValidationError(throwable.getMessage());
		}
		else if(statusCode != null){
			System.out.println("Erro de status");
			if(statusCode == 403){
				error = new ValidationError("Você não tem permissão de acesso a esta área.");
			}
			else{
				System.out.println("Erro qualquer");
				error = new ValidationError("Erro " + statusCode);
			}
		}
		else{
			error = new ValidationError(null);
		}

		request.setAttribute("error", error);
		request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
