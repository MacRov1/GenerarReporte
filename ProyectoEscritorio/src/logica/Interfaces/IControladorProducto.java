/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package logica.Interfaces;

import logica.Clases.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author UnwantedOpinion
 */
public interface IControladorProducto {
    public abstract boolean altaProducto(Producto producto);
    public abstract boolean modificarProducto(int id, Producto producto);
    public abstract boolean deshabilitarProducto(int id);
    public abstract ArrayList<Producto> listarProductos();
    public abstract ArrayList<Producto> listarProductosActivos();
    public abstract ArrayList<Producto> listarProductosPorCategoria(String categoriaSeleccionada);
    public abstract Producto buscarProducto(int id);
    public abstract ArrayList<String> obtenerNombresProductos();
    public abstract ArrayList<Float> obtenerPreciosProductos();
    public abstract Producto buscarProductoPorNombre(String nombreProducto);
    public abstract boolean nombreProductoExiste(String nombre);
    public abstract boolean skuProductoExiste(String sku);
    public abstract Producto buscarProductoPorSKU(String sku);
    public abstract boolean productoEnPedidos(int idProducto);
    public abstract List<Integer> obtenerProveedoresPorProductoID(int productoID);
    public abstract void eliminarProveedoresPorProductoID(int productoID);
    
    //NUEVOO
    public abstract List<Producto> listarTodos();
}
