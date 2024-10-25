/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import java.sql.SQLException;
import logica.Interfaces.IControladorCliente;
import logica.Clases.Cliente;
import logica.servicios.ClienteServicios;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jairo
 */
public class ControladorCliente implements IControladorCliente {
    private ClienteServicios ClienteServicios;
    private static ControladorCliente instancia;

    public ControladorCliente() {
        this.ClienteServicios = new ClienteServicios();
    }

    public static ControladorCliente getInstance() {
        if (instancia == null) {
            instancia = new ControladorCliente();
        }
        return instancia;
    }
    
    @Override
    public ArrayList<Cliente> listarClientes() {
        ArrayList<Cliente> clientes = ClienteServicios.getClientes();
        return clientes;
    }
    
    public boolean agregarCliente(String nombre, String email, String rut, String telefono, Date fechaRegistro) {
        Cliente cliente = new Cliente(nombre, email, rut, telefono, fechaRegistro);
        return ClienteServicios.agregarCliente(cliente);
    }
    
    public boolean existeRut(String rut) {
    return ClienteServicios.existeRut(rut);
}

    public boolean deshabilitarCliente(String rut) {
        // Verifica si el cliente con el RUT dado existe
        if (!ClienteServicios.existeRut(rut)) {
            return false; // Cliente no encontrado
        }
        // Realiza la eliminación del cliente
        return ClienteServicios.deshabilitarCliente(rut);
    }
    
    @Override
    public DefaultTableModel cargarDatosEnTabla() {
    DefaultTableModel modeloTabla = new DefaultTableModel();
    modeloTabla.addColumn("RUT");
    modeloTabla.addColumn("Nombre");
    modeloTabla.addColumn("Teléfono");
    modeloTabla.addColumn("Correo");
    modeloTabla.addColumn("Activo");

    ArrayList<Cliente> clientes = listarClientes();

    for (Cliente cliente : clientes) {
        Object[] fila = {
            cliente.getNum_rut(),
            cliente.getNom_empresa(),
            cliente.getTelefono(),
            cliente.getCorreo_electronico(),
            (cliente.getActivo() != null && cliente.getActivo()) ? "Sí" : "No"
        };
        modeloTabla.addRow(fila);
    }
    return modeloTabla;
    }
    
    @Override
    // Método para obtener un cliente por su RUT
    public Cliente obtenerClientePorRut(String rut) {
        return ClienteServicios.getClientePorRut(rut);
    }
    
    @Override
    public boolean actualizarCliente(Cliente cliente) {
        return ClienteServicios.actualizarCliente(cliente);
    }
    
    @Override
    public int obtenerIdClientePorNombre(String nombreCliente){
        return ClienteServicios.obtenerIdClientePorNombre(nombreCliente);
    }
    
    @Override
    public boolean existeNombreCliente(String nombre){
        return ClienteServicios.existeNombreCliente(nombre);
    }

    @Override
    public String obtenerNombreClientePorId(int idCliente) {
        String nombreCliente = "";
        return nombreCliente = ClienteServicios.getNombreClientePorId(idCliente);
    }

    @Override
    public List<String> obtenerNombresClientes() {
        return ClienteServicios.obtenerNombresClientes();
    }
    
    @Override
    public List<String> obtenerNombresClientesActivos(){
        return ClienteServicios.obtenerNombresClientesActivos();
    }
}
