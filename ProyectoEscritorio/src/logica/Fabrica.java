/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import logica.Controladores.ControladorUsuario;
import logica.Interfaces.IControladorUsuario;

import logica.Controladores.ControladorVendedor;
import logica.Interfaces.IControladorVendedor;

import logica.Controladores.ControladorProducto;
import logica.Interfaces.IControladorProducto;

import logica.Controladores.ControladorCategoria;
import logica.Interfaces.IControladorCategoria;

import logica.Controladores.ControladorPedido;
import logica.Interfaces.IControladorPedido;

import logica.Interfaces.IControladorProveedor;
import logica.Controladores.ControladorProveedor;

import logica.Interfaces.IControladorCliente;
import logica.Controladores.ControladorCliente;

/**
 *
 * @author Santiago.S
 */
public class Fabrica {
    
    private static Fabrica instancia;
    
    private Fabrica() {
    }
    
    public static Fabrica getInstance() {
        if (instancia == null) {
            instancia = new Fabrica();
        }
        return instancia;
    }
    
    public IControladorUsuario getIControladorUsuario() {
        IControladorUsuario ControladorU = ControladorUsuario.getInstance();
        return ControladorU;
    }
     
    public IControladorVendedor getIControladorVendedor() {
        IControladorVendedor ControladorV = ControladorVendedor.getInstance();
        return ControladorV;
    }
    
    public IControladorProducto getIControladorProducto() {
        IControladorProducto ControladorP = ControladorProducto.getInstance();
        return ControladorP;
    }

    public IControladorCategoria getIControladorCategoria() {
        IControladorCategoria ControladorC = ControladorCategoria.getInstance();
        return ControladorC;
    }
    
    public IControladorPedido getIControladorPedido() {
        IControladorPedido ControladorP = ControladorPedido.getInstance();
        return ControladorP; 
    }
    
    public IControladorProveedor getIControladorProveedor() {
        IControladorProveedor ControladorProv = ControladorProveedor.getInstance();
        return ControladorProv;
    }
    
    public IControladorCliente getIControladorCliente() {
        IControladorCliente ControladorCli = ControladorCliente.getInstance();
        return ControladorCli;
    }
}
