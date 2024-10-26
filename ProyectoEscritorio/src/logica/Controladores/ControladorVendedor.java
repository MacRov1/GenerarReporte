/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import java.sql.SQLException;
import logica.Clases.Vendedor;
import logica.Interfaces.IControladorVendedor;
import logica.servicios.VendedorServicios;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author UnwantedOpinion
 */
public class ControladorVendedor implements IControladorVendedor  {
    
    private static ControladorVendedor instancia;
    private VendedorServicios vendedorServicios;
    
    private ControladorVendedor() {
        vendedorServicios = new VendedorServicios();
    }

    public static ControladorVendedor getInstance() {
        if (instancia == null) {
            instancia = new ControladorVendedor();
        }
        return instancia;
    }
    
    @Override
    public boolean altaVendedor(Vendedor vendedor) {
        return vendedorServicios.altaVendedor(vendedor);
    }

    @Override
    public boolean modificarVendedor(int id, Vendedor vendedor) {
        return vendedorServicios.modificarVendedor(id, vendedor);
    }

    @Override
    public boolean deshabilitarVendedor(int id) {
        return vendedorServicios.deshabilitarVendedor(id);
    }

    @Override
    public ArrayList<Vendedor> listarVendedores() {
        return vendedorServicios.listarVendedores();
    }
    
    @Override
    public Vendedor buscarVendedor(int id) {
        return vendedorServicios.buscarVendedor(id);
    }
    
    @Override
    public int obtenerIdVendedorPorNombre(String nombreVendedor){
        return vendedorServicios.obtenerIdVendedorPorNombre(nombreVendedor);
    }
    
    @Override
    public boolean cedulaEnUso(int cedula){
        return vendedorServicios.cedulaEnUso(cedula);
    }
    
    @Override
    public boolean vendedorEstaAsociadoAPedido(int id){
        return vendedorServicios.vendedorEstaAsociadoAPedido(id);
    }
    
    @Override
    //Devuelve el nombre del vendedor
    public String obtenerNombreVendedorPorId(int idVendedor) {
        String nombreVendedor = "";
        return nombreVendedor = vendedorServicios.getNombreVendedorPorId(idVendedor);
    }
    
    @Override
    public List<String> obtenerNombresVendedores() {
        return vendedorServicios.obtenerNombresVendedores();
    }
    
    @Override
    public List<String> obtenerNombresVendedoresActivos(){
        return vendedorServicios.obtenerNombresVendedoresActivos();
    }
    
    @Override
    public boolean validarCredenciales(String nombreUsuario, String contrasenia){
        return vendedorServicios.validarCredenciales(nombreUsuario, contrasenia);
    }
    
    @Override
    public int obtenerIdPorUsuario(String nombreUsuario){
        return vendedorServicios.obtenerIdPorUsuario(nombreUsuario);
    }
}