/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logica.Clases.DetallePedido;
import logica.Clases.Pedido;
import logica.servicios.CategoriaServicios;
import logica.servicios.ClienteServicios;
import logica.servicios.DetallePedidoServicios;
import logica.servicios.PedidosServicios;
import logica.servicios.ProductoServicios;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet(urlPatterns = {"/descargarPDF"})
public class DescargarPDFServlet extends HttpServlet {
    private PedidosServicios pedidosServicios = new PedidosServicios();
    private ProductoServicios productoServicios = new ProductoServicios();
    private DetallePedidoServicios detallePedidosServicios = new DetallePedidoServicios();
    private ClienteServicios clienteServicios = new ClienteServicios();
    private CategoriaServicios categoriaServicios = new CategoriaServicios();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer idVendedor = (Integer) session.getAttribute("idVendedor");

        if (idVendedor != null) {
            String nombreCliente = request.getParameter("nombreCliente");
            String nombreCategoria = request.getParameter("nombreCategoria");
            System.out.println("Filtro Cliente: " + nombreCliente + ", Filtro Categoria: " + nombreCategoria);

            try {
                int mes = Integer.parseInt(request.getParameter("mes"));
                int anio = Integer.parseInt(request.getParameter("anio"));

                System.out.println("Mes: " + mes + ", Año: " + anio);

                Integer clienteId = nombreCliente != null ? clienteServicios.obtenerIdPorNombre(nombreCliente) : null;
                Integer categoriaId = nombreCategoria != null ? categoriaServicios.obtenerIdPorNombre(nombreCategoria) : null;

                List<Pedido> pedidosVendedor = obtenerPedidosFiltrados(idVendedor, mes, anio, clienteId, categoriaId);
                System.out.println("Total pedidos obtenidos: " + pedidosVendedor.size());

                List<DetallePedido> detallesAgrupados = agruparDetallesPedidos(pedidosVendedor);
                System.out.println("Total detalles obtenidos: " + detallesAgrupados.size());

                if (pedidosVendedor.isEmpty() || detallesAgrupados.isEmpty()) {
                    System.out.println("No se encontraron datos para los filtros aplicados.");
                }

                generarPDF(response, pedidosVendedor, detallesAgrupados);
            } catch (NumberFormatException | DocumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al generar el PDF: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("Login.jsp");
        }
    }

    private List<Pedido> obtenerPedidosFiltrados(Integer idVendedor, int mes, int anio, Integer clienteId, Integer categoriaId) {
        if (clienteId != null && categoriaId != null) {
            return pedidosServicios.getPedidosPorVendedorTodos(idVendedor, mes, anio, categoriaId, clienteId);
        } else if (clienteId != null) {
            return pedidosServicios.getPedidosPorVendedorClienteYFecha(idVendedor, mes, anio, clienteId);
        } else if (categoriaId != null) {
            return pedidosServicios.getPedidosPorVendedorCategoriaYFecha(idVendedor, mes, anio, categoriaId);
        } else {
            return pedidosServicios.getPedidosPorVendedorYFecha(idVendedor, mes, anio);
        }
    }

    private List<DetallePedido> agruparDetallesPedidos(List<Pedido> pedidosVendedor) {
        List<DetallePedido> todosDetalles = new ArrayList<>();
        for (Pedido pedido : pedidosVendedor) {
            List<DetallePedido> detalles = detallePedidosServicios.obtenerDetallesPedido(pedido.getIdentificador());
            todosDetalles.addAll(detalles);
        }
        return todosDetalles;
    }

    private void generarPDF(HttpServletResponse response, List<Pedido> pedidos, List<DetallePedido> detalles) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=InformePedidos.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Informe de Pedidos"));
        document.add(new Paragraph("Pedidos Hechos"));
        document.add(new Paragraph(" "));

        // Tabla de pedidos
        PdfPTable tablePedidos = new PdfPTable(5);
        tablePedidos.addCell("ID");
        tablePedidos.addCell("Fecha");
        tablePedidos.addCell("Estado");
        tablePedidos.addCell("Total");
        tablePedidos.addCell("Cliente");

        // Agregar datos de pedidos a la tabla
        for (Pedido pedido : pedidos) {
            tablePedidos.addCell(String.valueOf(pedido.getIdentificador()));
            tablePedidos.addCell(pedido.getFechaPedido().toString());
            tablePedidos.addCell(pedido.getEstado().toString());
            tablePedidos.addCell(String.valueOf(pedido.getTotal()));
            tablePedidos.addCell(clienteServicios.getNombreClientePorId(pedido.getIdCliente()));
        }

        document.add(tablePedidos);
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Productos Vendidos"));
        document.add(new Paragraph(" "));

        // Tabla de detalles de pedidos
        PdfPTable tableDetalles = new PdfPTable(5);
        tableDetalles.addCell("Producto");
        tableDetalles.addCell("Nombre");
        tableDetalles.addCell("Descripción");
        tableDetalles.addCell("Cantidad");
        tableDetalles.addCell("Precio Venta");

        // Agregar datos de detalles a la tabla
        for (DetallePedido detalle : detalles) {
            tableDetalles.addCell(String.valueOf(detalle.getProducto().getId()));
            tableDetalles.addCell(detalle.getProducto().getNombre());
            tableDetalles.addCell(detalle.getProducto().getDescripcion());
            tableDetalles.addCell(String.valueOf(detalle.getCantidad()));
            tableDetalles.addCell(String.valueOf(detalle.getPrecioVenta()));
        }

        document.add(tableDetalles);
        document.add(new Paragraph(" "));

        // Calcular ingresos generados
        double ingresosGenerados = 0;
        for (DetallePedido detalle : detalles) {
            ingresosGenerados += detalle.getPrecioVenta() * detalle.getCantidad();
        }

        document.add(new Paragraph("Ingresos Generados: $" + ingresosGenerados));

        document.close();
    }
}