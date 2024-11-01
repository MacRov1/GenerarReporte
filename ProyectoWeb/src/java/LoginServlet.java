/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logica.Fabrica;
import logica.Interfaces.IControladorVendedor;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //redirigimos a Login.jsp si se accede con GET
        request.getRequestDispatcher("Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("usuario");
        String password = request.getParameter("password");

        try {
            //validamos las credenciales del usuario
            if (validateUsuario(userName, password)) {
                //obtenemos el ID del vendedor a través del controlador
                IControladorVendedor controladorVendedor = Fabrica.getInstance().getIControladorVendedor();
                Integer idVendedor = controladorVendedor.obtenerIdPorUsuario(userName);

                //creamos la sesión y guardamos el usuario y el idVendedor
                HttpSession session = request.getSession();
                session.setAttribute("usuario", userName);
                session.setAttribute("idVendedor", idVendedor);  //guardamos el ID del vendedor en la sesión
                
                //redirigimos al Home.jsp
                response.sendRedirect("Home.jsp");
            } else {
                //usuario o contraseña incorrectos
                request.setAttribute("errorMessage", "Usuario o contraseña incorrectos.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al procesar la solicitud.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }

    //método para validar las credenciales del usuario
    private boolean validateUsuario(String username, String password) {
        //obtenemos instancia del controlador de vendedores
        IControladorVendedor controladorVendedor = Fabrica.getInstance().getIControladorVendedor();
        //validamos las credenciales
        return controladorVendedor.validarCredenciales(username, password);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para validar credenciales y gestionar inicio de sesión";
    }
}
