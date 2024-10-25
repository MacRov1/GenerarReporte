/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author macro
 */

import logica.Clases.Pedido;
import logica.Clases.Producto;
import logica.Clases.Cliente;
import logica.Clases.Categoria;
import logica.Fabrica;
import logica.Interfaces.IControladorPedido;
import logica.Interfaces.IControladorProducto;
import logica.Interfaces.IControladorCliente;
import logica.Interfaces.IControladorCategoria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/reportes")
public class GenerarReporteServlet extends HttpServlet {
    private Fabrica fabrica;
    private IControladorPedido controladorPedido;
    private IControladorProducto controladorProducto;
    private IControladorCliente controladorCliente;
    private IControladorCategoria controladorCategoria;

    @Override
    public void init() throws ServletException {
        fabrica = Fabrica.getInstance();
        controladorPedido = fabrica.getIControladorPedido();
        controladorProducto = fabrica.getIControladorProducto();
        controladorCliente = fabrica.getIControladorCliente();
        controladorCategoria = fabrica.getIControladorCategoria();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fecha = request.getParameter("fecha"); // Solo una fecha
        String categoriaId = request.getParameter("categoriaId");
        String clienteId = request.getParameter("clienteId");

        // Obtener los pedidos filtrados por fecha, cliente y categoría
        List<Pedido> pedidosFiltrados = controladorPedido.obtenerPedidosFiltrados(fecha, categoriaId, clienteId);

        // Resumen de ventas
        int numeroPedidos = pedidosFiltrados.size();
        int productosVendidos = contarProductosVendidos(pedidosFiltrados);
        double ingresosGenerados = calcularIngresos(pedidosFiltrados);
        String estadoPedidos = obtenerEstadoPedidos(pedidosFiltrados);

        // Obtener todas las categorías y clientes para los filtros
        List<Categoria> categorias = controladorCategoria.listarCategorias();
        List<Cliente> clientes = controladorCliente.listarClientes();

        // Enviar los resultados a la página JSP
        request.setAttribute("pedidos", pedidosFiltrados);
        request.setAttribute("categorias", categorias);
        request.setAttribute("clientes", clientes);
        request.setAttribute("numeroPedidos", numeroPedidos);
        request.setAttribute("productosVendidos", productosVendidos);
        request.setAttribute("ingresosGenerados", ingresosGenerados);
        request.setAttribute("estadoPedidos", estadoPedidos);
        request.setAttribute("fecha", fecha); // Solo una fecha
        request.setAttribute("categoriaId", categoriaId);
        request.setAttribute("clienteId", clienteId);

        request.getRequestDispatcher("/Vistas/reportes.jsp").forward(request, response);
    }

    // Método para contar los productos vendidos en los pedidos
    private int contarProductosVendidos(List<Pedido> pedidos) {
        int totalProductos = 0;
        for (Pedido pedido : pedidos) {
            totalProductos += pedido.getProductos().size(); // Suponiendo que Pedido tiene un método getProductos()
        }
        return totalProductos;
    }

    // Método para calcular el ingreso total de los pedidos
    private double calcularIngresos(List<Pedido> pedidos) {
        double totalIngresos = 0.0;
        for (Pedido pedido : pedidos) {
            totalIngresos += pedido.getTotal();
        }
        return totalIngresos;
    }

    // Método para obtener el estado de los pedidos
    private String obtenerEstadoPedidos(List<Pedido> pedidos) {
        int completados = 0, cancelados = 0;
        for (Pedido pedido : pedidos) {
            if ("Completado".equals(pedido.getEstado())) {
                completados++;
            } else if ("Cancelado".equals(pedido.getEstado())) {
                cancelados++;
            }
        }
        return "Completados: " + completados + ", Cancelados: " + cancelados;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}