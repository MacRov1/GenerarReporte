/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import java.util.List;
import logica.Clases.Pedido;
import logica.Interfaces.IControladorPedido;
import logica.servicios.PedidosServicios;

public class ControladorPedido implements IControladorPedido {

    private PedidosServicios servicioPedidos;
    private static ControladorPedido instancia;

    private ControladorPedido() {
        this.servicioPedidos = new PedidosServicios();
    }

    // Método Singleton
    public static ControladorPedido getInstance() {
        if (instancia == null) {
            instancia = new ControladorPedido();
        }
        return instancia;
    }

    @Override
    public List<Pedido> getPedidos() {
        return servicioPedidos.getPedidos();
    }

    @Override
    public boolean eliminarPedido(int idPedido) {
        return servicioPedidos.eliminarPedido(idPedido);
    }

    @Override
    public boolean actualizarPedido(Pedido pedido) {
        return servicioPedidos.actualizarPedido(pedido);
    }

    @Override
    public boolean agregarPedido(Pedido pedido) {
        return servicioPedidos.agregarPedido(pedido);
    }

    @Override
    public boolean actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        return servicioPedidos.actualizarEstadoPedido(idPedido, nuevoEstado);
    }

    @Override
    public List<Pedido> obtenerPedidosFiltrados(String fecha, String categoriaId, String clienteId) {
        return servicioPedidos.obtenerPedidosFiltrados(fecha, categoriaId, clienteId);
    }
}

