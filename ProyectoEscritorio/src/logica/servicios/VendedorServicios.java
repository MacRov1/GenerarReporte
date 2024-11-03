/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.servicios;

import Persistencia.ConexionDB;
import logica.Clases.Vendedor;
import java.sql.Connection;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

import Persistencia.Seguridad; //NUEVOO
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author UnwantedOpinion
 */
public class VendedorServicios {

    private Connection conexion = new ConexionDB().getConexion();

   public boolean altaVendedor(Vendedor vendedor) {
        if (vendedor == null) {
            throw new IllegalArgumentException("El vendedor no puede ser null");
        }

        // Verifica que los campos obligatorios no estén vacíos
        if (vendedor.getNomUsuario() == null || vendedor.getNomUsuario().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (vendedor.getContrasenia() == null || vendedor.getContrasenia().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Se asegura de que 'activo' no sea null
        if (vendedor.getActivo() == null) {
            vendedor.setActivo(true); // Valor por defecto
        }

        try {
            // Hash de la contraseña del vendedor
            String hashedPassword = Seguridad.hashPassword(vendedor.getContrasenia());
            System.out.println("Hash de la contraseña: " + hashedPassword); // Para depuración
            
            // Consulta SQL para insertar el nuevo vendedor a la BD
            String sql = "INSERT INTO vendedor (Nombre_Usuario, Contrasenia, Nombre, Cedula, Correo_Electronico, Telefono, Direccion, Fecha_Contratacion, Activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            
            ps.setString(1, vendedor.getNomUsuario());
            ps.setString(2, hashedPassword); // Aquí guardamos la contraseña encriptada
            ps.setString(3, vendedor.getNombre());
            ps.setInt(4, vendedor.getCedula());
            ps.setString(5, vendedor.getCorreo());
            ps.setString(6, vendedor.getTelefono());
            ps.setString(7, vendedor.getDireccion());
            ps.setTimestamp(8, new java.sql.Timestamp(vendedor.getFechaContratacion().getTime()));
            ps.setBoolean(9, vendedor.getActivo());

            // Ejecuta la consulta y retorna true si se inserta correctamente
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean modificarVendedor(int id, Vendedor vendedor) {
        try {
            // Consulta SQL para actualizar los datos del vendedor
            String sql = "UPDATE vendedor SET Nombre_Usuario = ?, Contrasenia = ?, Nombre = ?, Cedula = ?, Correo_Electronico = ?, Telefono = ?, Direccion = ?, Fecha_Contratacion = ?, Activo = ? WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, vendedor.getNomUsuario());
            ps.setString(2, vendedor.getContrasenia());
            ps.setString(3, vendedor.getNombre());
            ps.setInt(4, vendedor.getCedula());
            ps.setString(5, vendedor.getCorreo());
            ps.setString(6, vendedor.getTelefono());
            ps.setString(7, vendedor.getDireccion());
            ps.setTimestamp(8, new java.sql.Timestamp(vendedor.getFechaContratacion().getTime()));
            ps.setBoolean(9, vendedor.getActivo());
            ps.setInt(10, id);

            // Ejecuta la consulta y retorna true si se actualiza correctamente
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deshabilitarVendedor(int id) {
        try {
            //consulta SQL para deshabilitar al vendedor estableciendo activo = 0
            String sql = "UPDATE vendedor SET activo = 0 WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);

            //ejecuta la consulta y retorna true si se deshabilita correctamente
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<Vendedor> listarVendedores() {
        ArrayList<Vendedor> vendedores = new ArrayList<>();
        try {
            //consulta SQL para obtener todos los vendedores
            String sql = "SELECT * FROM vendedor";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            //recorre el ResultSet y crea objetos Vendedor para cada registro
            while(rs.next()) {
                Vendedor vendedor = new Vendedor();
                vendedor.setId(rs.getInt("id"));
                vendedor.setNomUsuario(rs.getString("nombre_usuario"));
                vendedor.setContrasenia(rs.getString("contrasenia"));
                vendedor.setNombre(rs.getString("nombre"));
                vendedor.setCedula(rs.getInt("cedula"));
                vendedor.setCorreo(rs.getString("correo_electronico"));
                vendedor.setTelefono(rs.getString("telefono"));
                vendedor.setDireccion(rs.getString("direccion"));
                vendedor.setFechaContratacion(rs.getDate("fecha_contratacion"));
                vendedor.setActivo(rs.getBoolean("activo"));
                vendedores.add(vendedor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vendedores;
    }
    
    public Vendedor buscarVendedor(int id) {
        Vendedor vendedor = null;
        try {
            String sql = "SELECT * FROM vendedor WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                vendedor = new Vendedor();
                vendedor.setId(rs.getInt("id"));
                vendedor.setNombre(rs.getString("nombre"));
                vendedor.setCedula(rs.getInt("cedula"));
                vendedor.setCorreo(rs.getString("correo_electronico"));
                vendedor.setTelefono(rs.getString("telefono"));
                vendedor.setDireccion(rs.getString("direccion"));
                Timestamp fechaContratacion = rs.getTimestamp("fecha_contratacion");
                vendedor.setFechaContratacion(new java.util.Date(fechaContratacion.getTime()));
                vendedor.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vendedor;
    }
    
    public int obtenerIdVendedorPorNombre(String nombreVendedor) {
        String sql = "SELECT id FROM vendedor WHERE nombre = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreVendedor);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("No se encontró un vendedor con el nombre: " + nombreVendedor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public boolean cedulaEnUso(int cedula) {
        String sql = "SELECT COUNT(*) FROM vendedor WHERE cedula = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean vendedorEstaAsociadoAPedido(int id) {
        String sql = "SELECT COUNT(*) FROM pedido WHERE vendedorID = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //obtiene el nombre de la tabla vendedor con el id del pedido, para mostrar el nombre del vendedor en lugar del id
    public String getNombreVendedorPorId(int idVendedor) {
        String nombre = null;
        String sql = "SELECT nombre FROM vendedor WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
             nombre = resultSet.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener el nombre del vendedor: " + e.getMessage());
        }

        return nombre;
    }
    
    public List<String> obtenerNombresVendedores() {
        List<String> nombres = new ArrayList<>();
        try {
            String sql = "SELECT nombre FROM vendedor";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombres;
    }
    
    public List<String> obtenerNombresVendedoresActivos() {
        List<String> nombres = new ArrayList<>();
        try {
            String sql = "SELECT nombre FROM vendedor WHERE Activo = 1";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombres;
    }
    
    public boolean validarCredenciales(String nombreUsuario, String contrasenia) {
    String sql = "SELECT Contrasenia FROM vendedor WHERE Nombre_Usuario = ?";
    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setString(1, nombreUsuario);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Obtiene la contraseña cifrada de la base de datos
                String hashedPassword = rs.getString("Contrasenia");
                
                // Imprime para depuración
                System.out.println("Contraseña cifrada obtenida: " + hashedPassword);
                
                // Verifica la contraseña ingresada contra la cifrada
                boolean isValid = Seguridad.checkPassword(contrasenia, hashedPassword);
                System.out.println("Contraseña ingresada válida: " + isValid);
                
                return isValid;
            } else {
                System.out.println("Usuario no encontrado: " + nombreUsuario);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Imprime la traza de la excepción para diagnóstico
    }
    return false; // Retorna false si no se encontró el usuario o hubo un error
}
    
    public int obtenerIdPorUsuario(String nombreUsuario) {
        String sql = "SELECT id FROM vendedor WHERE Nombre_Usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
