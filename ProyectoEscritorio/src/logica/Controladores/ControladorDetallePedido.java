/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import logica.Clases.DetallePedido;
import logica.Interfaces.IControladorDetallePedido;
import logica.servicios.DetallePedidoServicios;

/**
 *
 * @author UnwantedOpinion
 */
public class ControladorDetallePedido implements IControladorDetallePedido {
    
    private static ControladorDetallePedido instancia;
    private DetallePedidoServicios detallesServicios;
    
    private ControladorDetallePedido() {
        detallesServicios = new DetallePedidoServicios();
    }

    public static ControladorDetallePedido getInstance() {
        if (instancia == null) {
            instancia = new ControladorDetallePedido();
        }
        return instancia;
    }
    
    @Override
    public boolean eliminarDetallesPedido(int idPedido){
        return detallesServicios.eliminarDetallesPedido(idPedido);
    }
    
    @Override
    public boolean agregarDetallePedido(int idPedido, DetallePedido detalle){
        return detallesServicios.agregarDetallePedido(idPedido, detalle);
    }
    
}
