<%-- 
    Document   : verInforme
    Created on : 25 oct 2024, 19:33:50
    Author     : AlanCeballos
--%>

<%@page import="logica.Clases.DetallePedido"%>
<%@page import="java.util.List"%>
<%@page import="logica.servicios.DetallePedidoServicios"%>
<%@page import="logica.servicios.ClienteServicios"%>
<%@page import="logica.Clases.Pedido"%>
<%@page import="java.util.ArrayList"%>
<%
    //verifica si hay una sesi�n activa
    if (session == null || session.getAttribute("usuario") == null) {
        //redirige a Login.jsp si el usuario no est� autenticado
        response.sendRedirect("Login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Informe de Pedidos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="Styles/historialPedidos.css">
</head>
<body>
    <header class="d-flex align-items-center justify-content-between p-3 bg-dark text-white">
        <div class="encabezado">
            <h1>Informe de Pedidos</h1>
        </div>
    </header>

    <div class="contenido">
        <!-- Tabla de Pedidos -->
        <div class="text-center mt-4 p-3 border border-dark rounded" style="background-color: #f8f9fa;">
            <h2>Pedidos Hechos</h2>
        </div>

        <table id="tablaPedidos" class="table table-striped table-hover table-bordered mx-auto">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                    <th>Total</th>
                    <th>Cliente</th>
                </tr>
            </thead>
            <tbody>
                <%
                    ArrayList<Pedido> pedidos = (ArrayList<Pedido>) request.getAttribute("pedidos");
                    ClienteServicios clienteServicios = new ClienteServicios();
                    if (pedidos != null && !pedidos.isEmpty()) {
                        for (Pedido pedido : pedidos) {
                            String estadoPedido;
                            switch (pedido.getEstado()) {
                                case EN_PREPARACION: estadoPedido = "En Preparaci�n"; break;
                                case EN_VIAJE: estadoPedido = "En Viaje"; break;
                                case ENTREGADO: estadoPedido = "Entregado"; break;
                                case CANCELADO: estadoPedido = "Cancelado"; break;
                                default: estadoPedido = "Estado Desconocido"; break;
                            }

                            String nombreCliente = clienteServicios.getNombreClientePorId(pedido.getIdCliente());
                %>
                            <tr>
                                <td><%= pedido.getIdentificador() %></td>
                                <td><%= pedido.getFechaPedido() %></td>
                                <td><%= estadoPedido %></td>
                                <td><%= pedido.getTotal() %></td>
                                <td><%= nombreCliente != null ? nombreCliente : "Desconocido" %></td>
                            </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="5" class="text-center">No hay pedidos disponibles.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <!-- Tabla de Productos Vendidos -->
        <div class="text-center mt-4 p-3 border border-dark rounded" style="background-color: #f8f9fa;">
            <h2>Productos Vendidos</h2>
        </div>

        <table id="tablaProductos" class="table table-striped table-hover table-bordered mx-auto">
            <thead class="table-dark">
                <tr>
                    <th>Producto</th>
                    <th>Nombre</th>
                    <th>Descripci�n</th>
                    <th>Cantidad</th>
                    <th>Precio Venta</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<DetallePedido> detalles = (List<DetallePedido>) request.getAttribute("detalles");
                    double totalIngresos = 0.0;

                    if (detalles != null && !detalles.isEmpty()) {
                        for (DetallePedido detalle : detalles) {
                            totalIngresos += detalle.getCantidad() * detalle.getPrecioVenta();
                %>
                            <tr>
                                <td><%= detalle.getProducto().getId() %></td>
                                <td><%= detalle.getProducto().getNombre() %></td>
                                <td><%= detalle.getProducto().getDescripcion() %></td>
                                <td><%= detalle.getCantidad() %></td>
                                <td><%= detalle.getPrecioVenta() %></td>
                            </tr>
                <%
                        }
                    } else {
                %>
                        <tr>
                            <td colspan="5" class="text-center">No hay productos vendidos disponibles.</td>
                        </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <!-- Total Ingresos -->
        <div class="text-center mt-4 p-3 border border-dark rounded" style="background-color: #f8f9fa;">
            <h3>Ingresos Generados: $<%= totalIngresos %></h3>
        </div>

        <!-- Bot�n para Descargar PDF -->
        <div class="descargar-pdf-container text-end mt-4">
            <a href="<%= request.getContextPath() %>/descargarPDF?mes=<%= request.getParameter("mes") %>&anio=<%= request.getParameter("anio") %>&nombreCliente=<%= request.getParameter("nombreCliente") %>&nombreCategoria=<%= request.getParameter("nombreCategoria") %>" 
               class="btn btn-descargar-pdf">Descargar PDF</a>
        </div>
    </div>

    <footer class="text-center mt-4" style="background-color: #212529; color: white; padding: 10px 0;">
        <p>&copy; 2024 Programaci�n de Aplicaciones</p>
    </footer>
</body>
</html>