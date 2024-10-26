/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.servicios;

import Persistencia.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import logica.Clases.DetallePedido;
import logica.Clases.Pedido;
import logica.Clases.Pedido.Estado;

/**
 *
 * @author Mateo
 */
public class PedidosServicios {

    private Connection conexion = new ConexionDB().getConexion();
    private DetallePedidoServicios detallePedidoServicios = new DetallePedidoServicios();
    
    //obtiene todos los datos de los pedidos y los devuelve en un ArrayList
    public ArrayList<Pedido> getPedidos() {
        ArrayList<Pedido> pedidos = new ArrayList<>();

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

                    //inserta los detalles del pedido asociados
                    return detallePedidoServicios.agregarDetallePedido(pedidoID, pedido.getDetallesPedidos());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Pedido obtenerPedidoPorId(int idPedido) {
        Pedido pedido = null;
        
        String sql = "SELECT Identificador, FechaPedido, Estado, Total, VendedorID, ClienteID "
                + "FROM pedido WHERE Identificador = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int identificador = rs.getInt("Identificador");
                Date fechaPedido = rs.getDate("FechaPedido");
                String estadoStr = rs.getString("Estado");
                Pedido.Estado estado = Pedido.Estado.valueOf(estadoStr);
                float total = rs.getFloat("Total");
                int idVendedor = rs.getInt("VendedorID");
                int idCliente = rs.getInt("ClienteID");

                pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idCliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pedido;
    }
    
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

    public ArrayList<Pedido> getPedidosPorVendedor(int idVendedor) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try {
            String sql = "SELECT Identificador, FechaPedido, Estado, Total, VendedorID, ClienteID "
                       + "FROM pedido WHERE VendedorID = ? ORDER BY FechaPedido DESC";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idVendedor);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int identificador = resultSet.getInt("Identificador");
                Date fechaPedido = resultSet.getDate("FechaPedido");
                String estadoStr = resultSet.getString("Estado");
                Pedido.Estado estado = Pedido.Estado.valueOf(estadoStr);
                float total = resultSet.getFloat("Total");
                int idCliente = resultSet.getInt("ClienteID");

                Pedido pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idCliente);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
    
    public ArrayList<Pedido> getPedidosPorVendedorYFecha(int idVendedor, int mes, int año) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try {
            String sql = "SELECT Identificador, FechaPedido, Estado, Total, VendedorID, ClienteID " +
                         "FROM pedido " +
                         "WHERE VendedorID = ? AND MONTH(FechaPedido) = ? AND YEAR(FechaPedido) = ? " +
                         "ORDER BY FechaPedido DESC";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idVendedor);
            statement.setInt(2, mes);
            statement.setInt(3, año);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int identificador = resultSet.getInt("Identificador");
                Date fechaPedido = resultSet.getDate("FechaPedido");
                String estadoStr = resultSet.getString("Estado");
                Pedido.Estado estado = Pedido.Estado.valueOf(estadoStr);
                float total = resultSet.getFloat("Total");
                int idCliente = resultSet.getInt("ClienteID");

                Pedido pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idCliente);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public ArrayList<Pedido> getPedidosPorVendedorCategoriaYFecha(int idVendedor, int mes, int año, Integer idCategoria) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try {
            // Construir la consulta SQL
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p.Identificador, p.FechaPedido, p.Estado, p.Total, p.VendedorID, p.ClienteID ")
               .append("FROM pedido p ")
               .append("JOIN detallepedido d ON p.Identificador = d.PedidoID ")
               .append("JOIN producto pr ON d.ProductoID = pr.ProductoID ")
               .append("WHERE p.VendedorID = ? AND MONTH(p.FechaPedido) = ? AND YEAR(p.FechaPedido) = ? ");

            // Agregar condición para categoría, si se proporciona
            if (idCategoria != null) {
                sql.append("AND pr.CategoriaID = ? ");
            }

            sql.append("ORDER BY p.FechaPedido DESC");

            // Preparar el statement
            PreparedStatement statement = conexion.prepareStatement(sql.toString());
            statement.setInt(1, idVendedor);
            statement.setInt(2, mes);
            statement.setInt(3, año);

            int index = 4; // El índice comienza en 4 después de los parámetros obligatorios
            if (idCategoria != null) {
                statement.setInt(index++, idCategoria); // Asignar idCategoria si está presente
            }

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int identificador = resultSet.getInt("Identificador");
                Date fechaPedido = resultSet.getDate("FechaPedido");
                String estadoStr = resultSet.getString("Estado");
                Pedido.Estado estado = Pedido.Estado.valueOf(estadoStr);
                float total = resultSet.getFloat("Total");
                int idCliente = resultSet.getInt("ClienteID");

                // Crear un nuevo objeto Pedido y añadirlo a la lista
                Pedido pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idCliente);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
    
    public ArrayList<Pedido> getPedidosPorVendedorClienteYFecha(int idVendedor, int mes, int año, Integer idCliente) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try {
            // Construir la consulta SQL
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p.Identificador, p.FechaPedido, p.Estado, p.Total, p.VendedorID, p.ClienteID ")
               .append("FROM pedido p ")
               .append("WHERE p.VendedorID = ? AND MONTH(p.FechaPedido) = ? AND YEAR(p.FechaPedido) = ? ");

            // Agregar condición para cliente, si se proporciona
            if (idCliente != null) {
                sql.append("AND p.ClienteID = ? ");
            }

            sql.append("ORDER BY p.FechaPedido DESC");

            // Preparar el statement
            PreparedStatement statement = conexion.prepareStatement(sql.toString());
            statement.setInt(1, idVendedor);
            statement.setInt(2, mes);
            statement.setInt(3, año);

            int index = 4; // El índice comienza en 4 después de los parámetros obligatorios
            if (idCliente != null) {
                statement.setInt(index++, idCliente); // Asignar idCliente si está presente
            }

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int identificador = resultSet.getInt("Identificador");
                Date fechaPedido = resultSet.getDate("FechaPedido");
                String estadoStr = resultSet.getString("Estado");
                Pedido.Estado estado = Pedido.Estado.valueOf(estadoStr);
                float total = resultSet.getFloat("Total");
                int idClienteResult = resultSet.getInt("ClienteID");

                // Crear un nuevo objeto Pedido y añadirlo a la lista
                Pedido pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idClienteResult);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
    
    public ArrayList<Pedido> getPedidosPorVendedorTodos(int idVendedor, int mes, int año, Integer idCategoria, Integer idCliente) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try {
            // Construir la consulta SQL
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p.Identificador, p.FechaPedido, p.Estado, p.Total, p.VendedorID, p.ClienteID ")
               .append("FROM pedido p ")
               .append("JOIN DetallePedido dp ON p.Identificador = dp.PedidoID ") // Asegurar que se una con DetallePedido
               .append("WHERE p.VendedorID = ? AND MONTH(p.FechaPedido) = ? AND YEAR(p.FechaPedido) = ? ");

            // Agregar condición para idCliente, si se proporciona
            if (idCliente != null) {
                sql.append("AND p.ClienteID = ? ");
            }

            // Agregar condición para idCategoria, si se proporciona
            if (idCategoria != null) {
                sql.append("AND dp.CategoriaID = ? "); // Filtrar directamente por CategoriaID en DetallePedido
            }

            sql.append("ORDER BY p.FechaPedido DESC");

            // Preparar el statement
            PreparedStatement statement = conexion.prepareStatement(sql.toString());
            statement.setInt(1, idVendedor);
            statement.setInt(2, mes);
            statement.setInt(3, año);

            int index = 4; // El índice comienza en 4 después de los parámetros obligatorios
            if (idCliente != null) {
                statement.setInt(index++, idCliente); // Asignar idCliente si está presente
            }

            if (idCategoria != null) {
                statement.setInt(index++, idCategoria); // Asignar idCategoria si está presente
            }

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int identificador = resultSet.getInt("Identificador");
                Date fechaPedido = resultSet.getDate("FechaPedido");
                String estadoStr = resultSet.getString("Estado");
                Pedido.Estado estado = Pedido.Estado.valueOf(estadoStr);
                float total = resultSet.getFloat("Total");
                int idClienteResult = resultSet.getInt("ClienteID");

                // Crear un nuevo objeto Pedido y añadirlo a la lista
                Pedido pedido = new Pedido(identificador, fechaPedido, estado, total, idVendedor, idClienteResult);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

}
