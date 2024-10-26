import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logica.Fabrica;
import logica.Interfaces.IControladorCliente;
import logica.Clases.Cliente;

@WebServlet(urlPatterns = {"/clientes"})
public class ClientesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtenemos la instancia del controlador de clientes
            IControladorCliente controladorCliente = Fabrica.getInstance().getIControladorCliente();
            List<Cliente> clientes = controladorCliente.listarClientes();

            // Guardamos el listado de clientes en el request
            request.setAttribute("clientes", clientes);

            // Redirigimos a la página JSP para mostrar el listado
            request.getRequestDispatcher("Vistas/listadoClientes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al obtener los clientes.");
            request.getRequestDispatcher("Vistas/listadoClientes.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet que maneja la gestión de clientes.";
    }
}
