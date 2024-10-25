/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import logica.Clases.Producto;
import logica.Interfaces.IControladorProducto;
import logica.servicios.ProductoServicios;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author UnwantedOpinion
 */
public class ControladorProducto implements IControladorProducto {

    private static ControladorProducto instancia;
    private ProductoServicios productoServicios;

    private ControladorProducto() {
        productoServicios = new ProductoServicios();
    }

    public static ControladorProducto getInstance() {
        if (instancia == null) {
            instancia = new ControladorProducto();
        }
        return instancia;
    }

    @Override
    public boolean altaProducto(Producto producto) {
        return productoServicios.altaProducto(producto);
    }

    @Override
    public boolean modificarProducto(int id, Producto producto) {
        return productoServicios.modificarProducto(id, producto);
    }

    @Override
    public boolean deshabilitarProducto(int id) {
        return productoServicios.deshabilitarProducto(id);
    }

    @Override
    public ArrayList<Producto> listarProductos() {
        return productoServicios.listarProductos();
    }
    
    @Override
    public ArrayList<Producto> listarProductosActivos(){
        return productoServicios.listarProductosActivos();
    }

    @Override
    public ArrayList<Producto> listarProductosPorCategoria(String categoriaSeleccionada){
        return productoServicios.listarProductosPorCategoria(categoriaSeleccionada);
    }
    
    @Override
    public Producto buscarProducto(int id) {
        return productoServicios.buscarProducto(id);
    }
    
    @Override
    public ArrayList<String> obtenerNombresProductos(){
        return productoServicios.obtenerNombresProductos();
    }
    
    @Override
    public ArrayList<Float> obtenerPreciosProductos(){
        return productoServicios.obtenerPreciosProductos();
    }
    
    @Override
    public Producto buscarProductoPorNombre(String nombreProducto){
        return productoServicios.buscarProductoPorNombre(nombreProducto);
    }
    
    @Override
    public boolean nombreProductoExiste(String nombre){
        return productoServicios.nombreProductoExiste(nombre);
    }
    
    @Override
    public boolean skuProductoExiste(String sku){
        return productoServicios.skuProductoExiste(sku);
    }
    
    @Override
    public Producto buscarProductoPorSKU(String sku){
        return productoServicios.buscarProductoPorSKU(sku);
    }
    
    @Override
    public boolean productoEnPedidos(int idProducto){
        return productoServicios.productoEnPedidos(idProducto);
    }
    
    @Override
    public List<Integer> obtenerProveedoresPorProductoID(int productoID){
        return productoServicios.obtenerProveedoresPorProductoID(productoID);
    }
    
    @Override
    public void eliminarProveedoresPorProductoID(int productoID){
        productoServicios.eliminarProveedoresPorProductoID(productoID);
    }
    
    
    
    //NUEVOO
     @Override
    public List<Producto> listarTodos() {
        return productoServicios.obtenerTodosLosProductos();
    }
}
