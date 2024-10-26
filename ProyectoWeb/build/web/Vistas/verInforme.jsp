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
        <!-- Encabezado para la tabla de pedidos -->
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
                    // Obtener la lista de pedidos del atributo de la solicitud
                    ArrayList<Pedido> pedidos = (ArrayList<Pedido>) request.getAttribute("pedidos");
                    ClienteServicios clienteServicios = new ClienteServicios(); // Servicio para obtener datos del cliente
                    if (pedidos != null && !pedidos.isEmpty()) {
                        for (Pedido pedido : pedidos) {
                            // Determinar el estado del pedido con un switch
                            String estadoPedido;
                            switch (pedido.getEstado()) {
                                case EN_PREPARACION:
                                    estadoPedido = "En Preparación";
                                    break;
                                case EN_VIAJE:
                                    estadoPedido = "En Viaje";
                                    break;
                                case ENTREGADO:
                                    estadoPedido = "Entregado";
                                    break;
                                case CANCELADO:
                                    estadoPedido = "Cancelado";
                                    break;
                                default:
                                    estadoPedido = "Estado Desconocido"; // Manejar un estado no esperado
                                    break;
                            }

                            // Obtener el nombre del cliente usando el ID del pedido
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
            
        <!-- Encabezado para la tabla de productos vendidos -->
        <div class="text-center mt-4 p-3 border border-dark rounded" style="background-color: #f8f9fa;">
            <h2>Productos Vendidos</h2>
        </div>

        <table id="tablaProductos" class="table table-striped table-hover table-bordered mx-auto">
            <thead class="table-dark">
                <tr>
                    <th>Producto</th>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th>Cantidad</th>
                    <th>Precio Venta</th>
                </tr>
            </thead>
            <tbody>
                <%
                    // Obtener los detalles agrupados de los pedidos
                    List<DetallePedido> detalles = (List<DetallePedido>) request.getAttribute("detalles");
                    double totalIngresos = 0.0; // Inicializa el total de ingresos

                    if (detalles != null && !detalles.isEmpty()) {
                        for (DetallePedido detalle : detalles) {
                            // Sumar los ingresos
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

        <!-- Mostrar los ingresos generados con borde -->
        <div class="text-center mt-4 p-3 border border-dark rounded" style="background-color: #f8f9fa;">
            <h3>Ingresos Generados: $<%= totalIngresos %></h3>
        </div>
    </div>

    <footer class="text-center mt-4" style="background-color: #212529; color: white; padding: 10px 0;">
        <p>&copy; 2024 Programación de Aplicaciones</p>
    </footer>
</body>
</html>