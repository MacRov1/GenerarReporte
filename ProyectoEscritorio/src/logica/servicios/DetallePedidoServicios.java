/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.servicios;

import logica.Clases.DetallePedido;
import Persistencia.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logica.Clases.Producto;
import logica.Controladores.ControladorProducto;

/**
 *
 * @author UnwantedOpinion
 */
public class DetallePedidoServicios {
    private Connection conexion = new ConexionDB().getConexion();
    
    public boolean eliminarDetallesPedido(int idPedido) {
        String sql = "DELETE FROM pedido_producto WHERE PedidoID = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //agrega un detalle de pedido
    public boolean agregarDetallePedido(int idPedido, DetallePedido detalle) {
        String sql = "INSERT INTO pedido_producto (PedidoID, ProductoID, Cantidad, PrecioVenta) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPedido);
            preparedStatement.setInt(2, detalle.getProducto().getId());
            preparedStatement.setInt(3, detalle.getCantidad());
            preparedStatement.setFloat(4, detalle.getPrecioVenta());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //agrega múltiples detalles del pedido
    public boolean agregarDetallePedido(int idPedido, List<DetallePedido> detalles) {
        boolean resultado = true;
        for (DetallePedido detalle : detalles) {
            if (!agregarDetallePedido(idPedido, detalle)) {
                resultado = false;
            }
        }
        return resultado;
    }
    
    public List<DetallePedido> obtenerDetallesPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT ProductoID, Cantidad, PrecioVenta FROM pedido_producto WHERE PedidoID = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            //instanciamos el controlador de productos para obtener los productos
            ControladorProducto controladorProducto = ControladorProducto.getInstance();

            while (rs.next()) {
                int productoId = rs.getInt("ProductoID");
                int cantidad = rs.getInt("Cantidad");
                float precioVenta = rs.getFloat("PrecioVenta");

                //usamos el controlador de productos para obtener el producto por su ID
                Producto producto = controladorProducto.buscarProducto(productoId);

                //creamos el objeto DetallePedido con los datos obtenidos
                DetallePedido detalle = new DetallePedido(cantidad, precioVenta, producto, idPedido);
                detalles.add(detalle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }
    
    public boolean actualizarDetallesPedido(int idPedido, List<DetallePedido> detalles) {
        try {
            //primero, eliminamos todos los detalles existentes del pedido
            eliminarDetallesPedido(idPedido);

            //dsp, agregamos los nuevos detalles
            return agregarDetallePedido(idPedido, detalles);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
