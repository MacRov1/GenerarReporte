/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import logica.Clases.Proveedor;
import logica.Interfaces.IControladorProveedor;
import logica.servicios.ProveedorServicios;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 
 */
public class ControladorProveedor implements IControladorProveedor {
    // Atributos
    private static ControladorProveedor instancia; 
    private ProveedorServicios proveedorServicios; 
    
    private ControladorProveedor() {
        proveedorServicios = new ProveedorServicios();
    }

    public static ControladorProveedor getInstance() {
        if (instancia == null) {
            instancia = new ControladorProveedor();
        }
        return instancia;
    }
    
    // Método para alta de proveedor
    @Override
    public boolean altaProveedor(Proveedor proveedor) {
        return proveedorServicios.altaProveedor(proveedor);
    }

    // Método para modificar proveedor
    @Override
    public boolean modificarProveedor(int id, Proveedor proveedor) {
        return proveedorServicios.modificarProveedor(id, proveedor);
    }

    // Método para eliminar proveedor
    @Override
    public boolean deshabilitarProveedor(int id) {
        return proveedorServicios.deshabilitarProveedor(id);
    }

    // Método para listar proveedores
    @Override
   public ArrayList<Proveedor> listarProveedores() {
    return proveedorServicios.listarProveedores(); // Llamada al método del servicio que lista los proveedores
    }
   
   @Override
   public ArrayList<Proveedor> listarProveedoresActivos(){
       return proveedorServicios.listarProveedoresActivos();
   }
   
   @Override
   public boolean tieneProductosAsociados(int proveedorId){
       return proveedorServicios.tieneProductosAsociados(proveedorId);
   }
   
   @Override
   public boolean nombreProveedorEnUso(String nombre){
       return proveedorServicios.nombreProveedorEnUso(nombre);
   }
   
   @Override
   public List<String> obtenerNombresProveedores(){
       return proveedorServicios.obtenerNombresProveedores();
   }
   
   @Override
   public int obtenerProveedorIDPorNombre(String nombre){
       return proveedorServicios.obtenerProveedorIDPorNombre(nombre);
   }
   
   
   //NUEVOO
   @Override
   public List<Proveedor> listarTodos() {
        return proveedorServicios.obtenerTodosLosProveedores();
    }
}