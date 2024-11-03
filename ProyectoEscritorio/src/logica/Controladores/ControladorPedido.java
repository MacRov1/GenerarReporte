/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import logica.Clases.Pedido;
import logica.Clases.Pedido.Estado;
import logica.Interfaces.IControladorPedido;
import logica.servicios.PedidosServicios;

/**
 *
 * @author Mateo
 */
public class ControladorPedido implements IControladorPedido {

    private Map<String, Pedido> Pedidos;
    private PedidosServicios servicioPedidos;
    private static ControladorPedido instancia;

    public ControladorPedido() {
        this.servicioPedidos = new PedidosServicios();
    }

    //Metodo Singleton
    public static ControladorPedido getInstance() {
        if (instancia == null) {
            instancia = new ControladorPedido();
        }
        return instancia;
    }

    @Override
    //Devuelve todos los pedidos
    public ArrayList<Pedido> getPedidos() {
        ArrayList<Pedido> pedidos = servicioPedidos.getPedidos();
        return pedidos;
    }
    
    @Override
    public boolean eliminarPedido(int idPedido) {
        return servicioPedidos.eliminarPedido(idPedido);
    }

    @Override
    public boolean actualizarPedido(Pedido pedido) {
        return servicioPedidos.actualizarPedido(pedido);
    }
    
    // Método para agregar un pedido
    @Override
    public boolean agregarPedido(Pedido pedido) {
        boolean exito = servicioPedidos.agregarPedido(pedido);
        return exito;
    }
    
    @Override
    public boolean actualizarEstadoPedido(int idPedido, String nuevoEstado){
        return servicioPedidos.actualizarEstadoPedido(idPedido, nuevoEstado);
    }

    @Override
    public ArrayList<Pedido> getPedidosPorVendedor(int idVendedor){
        return servicioPedidos.getPedidosPorVendedor(idVendedor);
    }
    
    @Override
    public ArrayList<Pedido> getPedidosPorVendedorYFecha(int idVendedor, int mes, int año){
        return servicioPedidos.getPedidosPorVendedorYFecha(idVendedor, mes, año);
    }
    
    @Override
    public ArrayList<Pedido> getPedidosPorVendedorClienteYFecha(int idVendedor, int clienteId, int mes, int año){
        return servicioPedidos.getPedidosPorVendedorClienteYFecha(idVendedor, clienteId, mes, año);
    }
    
    @Override
    public boolean cancelarPedido(int idPedido){
        return servicioPedidos.cancelarPedido(idPedido);
    }
}
