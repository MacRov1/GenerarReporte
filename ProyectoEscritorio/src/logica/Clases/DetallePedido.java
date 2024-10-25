/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Clases;

/**
 *
 * @author UnwantedOpinion
 */
public class DetallePedido {
    private int cantidad;
    private float precioVenta;
    private Producto producto;
    private int pedidoID;
    
    public DetallePedido(int cantidad, float precioVenta, Producto producto, int pedidoID) {
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.producto = producto;
        this.pedidoID = pedidoID;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getPedido() {
        return pedidoID;
    }

    public void setPedido(int pedidoID) {
        this.pedidoID = pedidoID;
    }

    public float subTotal() {
        return cantidad * precioVenta;
    }
}
