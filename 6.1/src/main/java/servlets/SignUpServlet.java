package servlets;

import services.AccountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;
    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        accountService.singUp(login, password);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("SignedUp");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
