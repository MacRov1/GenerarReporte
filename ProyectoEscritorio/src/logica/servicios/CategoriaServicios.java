/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.servicios;

import Persistencia.ConexionDB;
import logica.Clases.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author UnwantedOpinion
 */
public class CategoriaServicios {
    private Connection conexion = new ConexionDB().getConexion();

    public boolean altaCategoria(Categoria categoria) {
        try {
            String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean modificarCategoria(int id, Categoria categoria) {
        try {
            String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deshabilitarCategoria(int id) {
    try {
        String sql = "UPDATE categoria SET activo = 0 WHERE id = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, id);
        
        return ps.executeUpdate() > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}

    public ArrayList<Categoria> listarCategorias() {
        ArrayList<Categoria> categorias = new ArrayList<>();
        try {
            String sql = "SELECT * FROM categoria";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categoria.setActivo(rs.getBoolean("activo"));
                categorias.add(categoria);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categorias;
    }
    
    public ArrayList<Categoria> listarCategoriasActivas() {
        ArrayList<Categoria> categorias = new ArrayList<>();
        try {
            String sql = "SELECT * FROM categoria WHERE Activo = 1";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categoria.setActivo(rs.getBoolean("activo"));
                categorias.add(categoria);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categorias;
    }
    
    public Categoria buscarCategoria(int id) {
        Categoria categoria = null;
        try {
            String sql = "SELECT * FROM categoria WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categoria;
    }
    
    public List<String> obtenerNombresCategorias() {
        List<String> nombresCategorias = new ArrayList<>();
        try {
            String sql = "SELECT nombre FROM categoria";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                nombresCategorias.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombresCategorias;
    }
    
    //auxiliar para buscar una categoría por nombre
    public Categoria buscarCategoriaPorNombre(String nombre) {
        Categoria categoria = null;
        String sql = "SELECT * FROM categoria WHERE nombre = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                categoria = new Categoria();
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categoria.setId(rs.getInt("id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return categoria;
    }
    
    //auxiliar para verificar si hay productos dentro de esta categoria
    public boolean categoriaTieneProductos(int categoriaId) {
        int cantidadProductos = 0;
        String sql = "SELECT COUNT(*) FROM producto WHERE ID = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sql)){
                ps.setInt(1, categoriaId);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    cantidadProductos = rs.getInt(1);
                }
                
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return cantidadProductos > 0;
    }
    
    public int obtenerIdPorNombre(String nombreCategoria) {
        String sql = "SELECT id FROM categoria WHERE nombre = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreCategoria);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException("No se encontró una categoría con el nombre: " + nombreCategoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Retorna -1 en caso de error
        }
    }

}
