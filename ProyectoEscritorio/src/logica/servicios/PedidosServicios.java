/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.servicios;

import Persistencia.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import logica.Clases.Pedido;
import logica.Clases.Pedido.Estado;
import logica.Clases.Producto;

public class PedidosServicios {

    private Connection conexion = new ConexionDB().getConexion();
    private DetallePedidoServicios detallePedidoServicios = new DetallePedidoServicios();

    // Obtiene todos los pedidos
    public List<Pedido> getPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        try {
            String sql = "SELECT p.Identificador, p.FechaPedido, p.Estado, p.Total, "
                    + "p.VendedorID, p.ClienteID, c.Nom_Empresa as nombre_cliente, "
                    + "v.Nombre as nombre_vendedor, v.Cedula as cedula_vendedor, v.Telefono as telefono_vendedor "
                    + "FROM pedido p "
                    + "JOIN cliente c ON p.ClienteID = c.ID "
                    + "JOIN vendedor v ON p.VendedorID = v.ID "
                    + "ORDER BY CASE p.Estado "
                    + "WHEN 'EN_PREPARACION' THEN 1 "
                    + "WHEN 'EN_VIAJE' THEN 2 "
                    + "WHEN 'ENTREGADO' THEN 3 "
                    + "WHEN 'CANCELADO' THEN 4 "
                    + "END, p.Identificador";

            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int identificador = resultSet.getInt("Identificador");
                Date fechaPedido = resultSet.getDate("FechaPedido");
                String estadoStr = resultSet.getString("Estado");
                Estado estado = Estado.valueOf(estadoStr);
                float total = resultSet.getFloat("Total");
                int idVendedor = resultSet.getInt("VendedorID");
                int idCliente = resultSet.getInt("ClienteID");

                Pedido pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idCliente);
                pedidos.add(pedido);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    // Elimina un pedido
    public boolean eliminarPedido(int idPedido) {
        boolean resultado = detallePedidoServicios.eliminarDetallesPedido(idPedido);
        if (resultado) {
            String sql = "DELETE FROM pedido WHERE Identificador = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, idPedido);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // Actualiza un pedido
    public boolean actualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedido SET FechaPedido = ?, Estado = ?, Total = ?, VendedorID = ?, ClienteID = ? WHERE Identificador = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setTimestamp(1, new java.sql.Timestamp(pedido.getFechaPedido().getTime()));
            ps.setString(2, pedido.getEstado().name());
            ps.setFloat(3, pedido.getTotal());
            ps.setInt(4, pedido.getIdVendedor());
            ps.setInt(5, pedido.getIdCliente());
            ps.setInt(6, pedido.getIdentificador());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Agrega un nuevo pedido
    public boolean agregarPedido(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (FechaPedido, Estado, Total, VendedorID, ClienteID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmtPedido = conexion.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
            stmtPedido.setTimestamp(1, new java.sql.Timestamp(pedido.getFechaPedido().getTime()));
            stmtPedido.setString(2, pedido.getEstado().name());
            stmtPedido.setFloat(3, pedido.getTotal());
            stmtPedido.setInt(4, pedido.getIdVendedor());
            stmtPedido.setInt(5, pedido.getIdCliente());
            int rowsAffected = stmtPedido.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmtPedido.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int pedidoID = generatedKeys.getInt(1);
                    pedido.setIdentificador(pedidoID);

                    return detallePedidoServicios.agregarDetallePedido(pedidoID, pedido.getDetallesPedidos());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Actualiza el estado de un pedido
    public boolean actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE identificador = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPedido);
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Obtiene los pedidos filtrados por fecha, categoría y cliente
    public List<Pedido> obtenerPedidosFiltrados(String fecha, String categoriaId, String clienteId) {
        List<Pedido> pedidos = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM pedido WHERE FechaPedido = ?");

        if (categoriaId != null && !categoriaId.isEmpty()) {
            query.append(" AND Identificador IN (SELECT PedidoID FROM Pedido_Producto WHERE ProductoID IN (SELECT ID FROM producto WHERE CategoriaID = ?))");
        }

        if (clienteId != null && !clienteId.isEmpty()) {
            query.append(" AND ClienteID = ?");
        }

        try (PreparedStatement stmt = conexion.prepareStatement(query.toString())) {
            int paramIndex = 1;
            stmt.setString(paramIndex++, fecha);

            if (categoriaId != null && !categoriaId.isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(categoriaId));
            }

            if (clienteId != null && !clienteId.isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(clienteId));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int identificador = rs.getInt("Identificador");
                    Date fechaPedido = rs.getDate("FechaPedido");
                    String estadoStr = rs.getString("Estado");
                    Estado estado = Estado.valueOf(estadoStr);
                    float total = rs.getFloat("Total");
                    int idVendedor = rs.getInt("VendedorID");
                    int idCliente = rs.getInt("ClienteID");

                    Pedido pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idCliente);
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
    
    // MAS NUEVO AUN
    // Método para obtener los productos de un pedido
    public List<Producto> obtenerProductosPorPedido(int idPedido) {
    List<Producto> productos = new ArrayList<>();

    String sql = "SELECT p.ID, p.Nombre, p.Descripcion, p.Precio, p.Stock "
               + "FROM producto p "
               + "JOIN Pedido_Producto pp ON p.ID = pp.ProductoID "
               + "WHERE pp.PedidoID = ?";

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setInt(1, idPedido);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idProducto = rs.getInt("ID");
                String nombre = rs.getString("Nombre");
                String descripcion = rs.getString("Descripcion");
                float precio = rs.getFloat("Precio");
                int stock = rs.getInt("Stock");

                Producto producto = new Producto(idProducto, nombre, descripcion, precio, stock);
                productos.add(producto);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return productos;
}
   public Pedido obtenerPedidoPorId(int id) {
    Pedido pedido = null;
    try {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            // Mapeo de los datos del pedido
            pedido = new Pedido();
            pedido.setIdentificador(rs.getInt("id")); // Mapeo del identificador
            pedido.setFechaPedido(rs.getDate("fechaPedido")); // Mapeo de la fecha del pedido
            pedido.setEstado(Pedido.Estado.valueOf(rs.getString("estado"))); // Mapeo del estado del pedido (EN_PREPARACION, EN_VIAJE, ENTREGADO, CANCELADO)
            pedido.setTotal(rs.getFloat("total")); // Mapeo del total del pedido
            pedido.setIdVendedor(rs.getInt("idVendedor")); // Mapeo del ID del vendedor
            pedido.setIdCliente(rs.getInt("idCliente")); // Mapeo del ID del cliente
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return pedido;
}
}
   