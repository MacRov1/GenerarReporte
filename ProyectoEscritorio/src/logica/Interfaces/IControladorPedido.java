/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Interfaces;

import java.util.List;
import logica.Clases.Pedido;

public interface IControladorPedido {

    List<Pedido> getPedidos();

    boolean eliminarPedido(int idPedido);

    boolean actualizarPedido(Pedido pedido);

    boolean agregarPedido(Pedido pedido);

    boolean actualizarEstadoPedido(int idPedido, String nuevoEstado);

    List<Pedido> obtenerPedidosFiltrados(String fecha, String categoriaId, String clienteId);
}