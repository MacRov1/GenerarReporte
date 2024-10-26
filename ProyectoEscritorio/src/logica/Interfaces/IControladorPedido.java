/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Interfaces;

import java.util.ArrayList;
import java.util.List;
import logica.Clases.Pedido;
import logica.Clases.Pedido.Estado;

/**
 *
 * @author Mateo
 */
public interface IControladorPedido {
    
    public abstract ArrayList<Pedido> getPedidos();
    public abstract boolean eliminarPedido(int idPedido);
    public abstract boolean actualizarPedido(Pedido pedido);
    public abstract boolean agregarPedido(Pedido pedido);
    public abstract boolean actualizarEstadoPedido(int idPedido, String nuevoEstado);
    public abstract ArrayList<Pedido> getPedidosPorVendedor(int idVendedor);
    public abstract ArrayList<Pedido> getPedidosPorVendedorYFecha(int idVendedor, int mes, int año);
    public ArrayList<Pedido> getPedidosPorVendedorClienteYFecha(int idVendedor, int clienteId, int mes, int año);
}
