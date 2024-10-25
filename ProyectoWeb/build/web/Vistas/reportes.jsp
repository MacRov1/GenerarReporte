<%-- 
    Document   : reportes.jsp
    Created on : 18 oct. 2024, 5:25:26 p. m.
    Author     : macro
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reporte de Ventas, Proveedores y Productos</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/reportes.css">
</head>
<body>
    <div class="container">
        <h1>Reporte de Ventas, Proveedores y Productos</h1>

        <!-- Filtros -->
        <form action="${pageContext.request.contextPath}/reportes" method="post">
            <label for="fechaPedido">Fecha del Pedido:</label>
            <input type="date" id="fechaPedido" name="fechaPedido" value="${fechaPedido}">
            
            <label for="categoriaId">Categoría de Producto:</label>
            <select id="categoriaId" name="categoriaId">
                <option value="">Seleccionar categoría</option>
                <c:forEach var="categoria" items="${categorias}">
                    <option value="${categoria.id}" <c:if test="${categoria.id == categoriaId}">selected</c:if>>${categoria.nombre}</option>
                </c:forEach>
            </select>

            <label for="clienteId">Cliente:</label>
            <select id="clienteId" name="clienteId">
                <option value="">Seleccionar cliente</option>
                <c:forEach var="cliente" items="${clientes}">
                    <option value="${cliente.num_rut}" <c:if test="${cliente.num_rut == clienteId}">selected</c:if>>${cliente.nom_empresa}</option>
                </c:forEach>
            </select>

            <button type="submit">Filtrar</button>
        </form>

        <!-- Resumen de Ventas -->
        <h2>Resumen de Ventas</h2>
        <p>Número de pedidos: ${numeroPedidos}</p>
        <p>Productos vendidos: ${productosVendidos}</p>
        <p>Ingresos generados: ${ingresosGenerados}</p>
        <p>Estado de pedidos: ${estadoPedidos}</p>

        <!-- Tabla de Pedidos Filtrados -->
        <h2>Lista de Pedidos</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Cliente</th>
                    <th>Fecha</th>
                    <th>Total</th>
                    <th>Estado</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="pedido" items="${pedidos}">
                    <tr>
                        <td>${pedido.id}</td>
                        <td>${pedido.cliente.nom_empresa}</td>
                        <td>${pedido.fecha}</td>
                        <td>${pedido.total}</td>
                        <td>${pedido.estado}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Tabla de Proveedores -->
        <h2>Lista de Proveedores</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Teléfono</th>
                    <th>Dirección</th>
                    <th>Correo Electrónico</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="proveedor" items="${proveedores}">
                    <tr>
                        <td>${proveedor.id}</td>
                        <td>${proveedor.nombre}</td>
                        <td>${proveedor.telefono}</td>
                        <td>${proveedor.direccion}</td>
                        <td>${proveedor.correoElectronico}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Tabla de Productos -->
        <h2>Lista de Productos</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Precio de Venta</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="producto" items="${productos}">
                    <tr>
                        <td>${producto.id}</td>
                        <td>${producto.nombre}</td>
                        <td>${producto.categoria.nombre}</td>
                        <td>${producto.precioVenta}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>