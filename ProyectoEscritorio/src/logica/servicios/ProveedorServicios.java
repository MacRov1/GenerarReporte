/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.servicios;

import Persistencia.ConexionDB;
import logica.Clases.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorServicios {

    private Connection conexion;

    public ProveedorServicios() {
        conexion = new ConexionDB().getConexion();
    }

    // Método para obtener un proveedor por su ID
    public Proveedor getProveedor(int id) {
        Proveedor proveedor = null;
        String sql = "SELECT * FROM proveedor WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setCorreoElectronico(rs.getString("correo_electronico"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setActivo(rs.getBoolean("activo"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return proveedor;
    }

    // Método para listar todos los proveedores
    public ArrayList<Proveedor> listarProveedores() {
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setCorreoElectronico(rs.getString("correo_electronico"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setActivo(rs.getBoolean("activo"));
                proveedores.add(proveedor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return proveedores;
    }
    
    public ArrayList<Proveedor> listarProveedoresActivos() {
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        try {
            String sql = "SELECT * FROM proveedor WHERE activo = 1";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setCorreoElectronico(rs.getString("correo_electronico"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setActivo(rs.getBoolean("activo"));
                proveedores.add(proveedor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return proveedores;
    }


    // Método para validar existencia de proveedor por ID
    public boolean existeProveedor(int id) {
        String sql = "SELECT id FROM proveedor WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            rs.close();
            return existe;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Método para insertar un nuevo proveedor
    public boolean altaProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO proveedor (nombre, telefono, direccion, correo_electronico) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getDireccion());
            ps.setString(4, proveedor.getCorreoElectronico());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Método para actualizar un proveedor existente
    public boolean modificarProveedor(int id, Proveedor proveedor) {
       String sql = "UPDATE proveedor SET nombre = ?, telefono = ?, direccion = ?, correo_electronico = ? WHERE id = ?";
    try (Connection conexion = new ConexionDB().getConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setString(1, proveedor.getNombre());
        ps.setString(2, proveedor.getTelefono());
        ps.setString(3, proveedor.getDireccion());
        ps.setString(4, proveedor.getCorreoElectronico());
        ps.setInt(5, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
    }

    //método para deshabilitar un proveedor por su ID
    public boolean deshabilitarProveedor(int id) {
        String sql = "UPDATE proveedor SET activo = 0 WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    //auxiliar para verificar si un proveedor tiene productos asociados
    public boolean tieneProductosAsociados(int proveedorId) {
        String sql = "SELECT COUNT(*) FROM producto_proveedor WHERE proveedorID = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, proveedorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                return count > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //auxiliar para verificar si un nombre de proveedor ya está en uso
    public boolean nombreProveedorEnUso(String nombre) {
        String sql = "SELECT COUNT(*) FROM proveedor WHERE nombre = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                return count > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public List<String> obtenerNombresProveedores() {
        List<String> nombresProveedores = new ArrayList<>();
        try {
            String sql = "SELECT Nombre FROM proveedor";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                nombresProveedores.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombresProveedores;
    }
    
    public int obtenerProveedorIDPorNombre(String nombre) {
        int proveedorID = -1;//valor por defecto si lo encuentra
        String query = "SELECT ID FROM proveedor WHERE Nombre = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    proveedorID = rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedorID;
    }
    
    public String obtenerNombreProveedorPorID(int proveedorID) {
        String nombreProveedor = null;
        String query = "SELECT Nombre FROM proveedor WHERE ID = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, proveedorID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombreProveedor = rs.getString("Nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombreProveedor;
    }

}