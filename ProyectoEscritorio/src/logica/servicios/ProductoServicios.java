/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.servicios;

import Persistencia.ConexionDB;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import logica.Clases.Producto;
import logica.Clases.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UnwantedOpinion
 */
public class ProductoServicios {
    private Connection conexion = new ConexionDB().getConexion();
    CategoriaServicios categoriaServicios = new CategoriaServicios();

    public boolean altaProducto(Producto producto) {
        String sql = "INSERT INTO producto (nombre, descripcion, SKU, stock, precioVenta, CategoriaID, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getSKU());
            ps.setInt(4, producto.getStock());
            ps.setFloat(5, producto.getPrecioVenta());
            ps.setInt(6, producto.getCategoria().getId());

            // Manejo de la imagen
            if (producto.getImagen() != null) {
                ps.setBytes(7, producto.getImagen());
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean modificarProducto(int id, Producto producto) {
        try {
            String sql = "UPDATE producto SET nombre = ?, descripcion = ?, SKU = ?, stock = ?, precioVenta = ?, CategoriaID = ?, Imagen = ? WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getSKU());
            ps.setInt(4, producto.getStock());
            ps.setFloat(5, producto.getPrecioVenta());
            ps.setInt(6, producto.getCategoria().getId());

            // Establecer la imagen como un Blob
            byte[] imagenBytes = producto.getImagen();
            if (imagenBytes != null && imagenBytes.length > 0) {
                ps.setBlob(7, new ByteArrayInputStream(imagenBytes));
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }

            ps.setInt(8, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean deshabilitarProducto(int id) {
        try {
            String sql = "UPDATE producto SET activo = 0 WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM producto";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setSKU(rs.getString("SKU"));
                producto.setStock(rs.getInt("stock"));
                producto.setPrecioVenta(rs.getFloat("precioVenta"));
                producto.setCategoria(buscarCategoriaPorId(rs.getInt("CategoriaID")));
                producto.setActivo(rs.getBoolean("Activo"));
                productos.add(producto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }
    
    public ArrayList<Producto> listarProductosActivos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM producto  WHERE Activo = 1";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setSKU(rs.getString("SKU"));
                producto.setStock(rs.getInt("stock"));
                producto.setPrecioVenta(rs.getFloat("precioVenta"));
                producto.setCategoria(buscarCategoriaPorId(rs.getInt("CategoriaID")));
                producto.setActivo(rs.getBoolean("Activo"));
                productos.add(producto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }
    
    public ArrayList<Producto> listarProductosPorCategoria(String nombreCategoria) {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            Categoria categoria = categoriaServicios.buscarCategoriaPorNombre(nombreCategoria);

            if (categoria != null) {
                String sql = "SELECT * FROM producto WHERE CategoriaID = ? AND Activo = 1";
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setInt(1, categoria.getId());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setId(rs.getInt("id"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setSKU(rs.getString("SKU"));
                    producto.setStock(rs.getInt("stock"));
                    producto.setPrecioVenta(rs.getFloat("precioVenta"));
                    producto.setCategoria(categoria); // Usamos la categoría obtenida
                    producto.setActivo(rs.getBoolean("Activo"));
                    productos.add(producto);
                }
            } else {
                System.out.println("Categoría no encontrada.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }


    
    public Producto buscarProducto(int id) {
        Producto producto = null;
        try {
            String sql = "SELECT * FROM producto WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setSKU(rs.getString("SKU"));
                producto.setStock(rs.getInt("stock"));
                producto.setPrecioVenta(rs.getFloat("precioVenta"));
                producto.setCategoria(buscarCategoriaPorId(rs.getInt("CategoriaID")));

                Blob blob = rs.getBlob("Imagen");
                if (blob != null) {
                    byte[] imagenBytes = blob.getBytes(1, (int) blob.length());
                    producto.setImagen(imagenBytes);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return producto;
    }
    
    // auxiliar para buscar la categoría asociada al producto
    private Categoria buscarCategoriaPorId(int id) {
        CategoriaServicios categoriaServicios = new CategoriaServicios();
        return categoriaServicios.buscarCategoria(id);
    }
    
    //auxiliar para obtener los nombres de los productos
    public ArrayList<String> obtenerNombresProductos() {
        ArrayList<String> nombres = new ArrayList<>();
        try {
            String sql = "SELECT nombre FROM producto";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombres;
    }
    
    //auxiliar para obtener los precios de los productos
    public ArrayList<Float> obtenerPreciosProductos() {
        ArrayList<Float> precios = new ArrayList<>();
        try {
            String sql = "SELECT precioVenta FROM producto";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                precios.add(rs.getFloat("precioVenta"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return precios;
    }
    
    //auxiliar para buscar producto por nombre
    public Producto buscarProductoPorNombre(String nombreProducto) {
        Producto producto = null;
            try {
                String sql = "SELECT * FROM producto WHERE nombre = ?";
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setString(1, nombreProducto);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    producto = new Producto();
                    producto.setId(rs.getInt("id"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setSKU(rs.getString("SKU"));
                    producto.setStock(rs.getInt("stock"));
                    producto.setPrecioVenta(rs.getFloat("precioVenta"));
                    producto.setCategoria(buscarCategoriaPorId(rs.getInt("CategoriaID")));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return producto;
    }
    
    public boolean nombreProductoExiste(String nombre) {
        String sql = "SELECT COUNT(*) FROM producto WHERE nombre = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean skuProductoExiste(String sku) {
        String sql = "SELECT COUNT(*) FROM producto WHERE SKU = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, sku);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public Producto buscarProductoPorSKU(String sku) {
        Producto producto = null;
        try {
            String sql = "SELECT * FROM producto WHERE SKU = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, sku);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setSKU(rs.getString("SKU"));
                producto.setStock(rs.getInt("stock"));
                producto.setPrecioVenta(rs.getFloat("precioVenta"));
                producto.setCategoria(buscarCategoriaPorId(rs.getInt("CategoriaID")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return producto;
    }
    
    public boolean productoEnPedidos(int idProducto) {
        String sql = "SELECT COUNT(*) FROM pedido_producto WHERE ProductoID = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean agregarProductoProveedor(int productoID, int proveedorID) {
        String query = "INSERT INTO producto_proveedor (ProductoID, ProveedorID) VALUES (?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, productoID);
            stmt.setInt(2, proveedorID);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;//retorna true si se insertó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;//retorna false si ocurrió un error o no se insertó ninguna fila
    }
    
    public List<Integer> obtenerProveedoresPorProductoID(int productoID) {
        List<Integer> proveedorIDs = new ArrayList<>();
        String query = "SELECT ProveedorID FROM producto_proveedor WHERE ProductoID = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, productoID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                proveedorIDs.add(rs.getInt("ProveedorID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proveedorIDs;
    }
    
    public void eliminarProveedoresPorProductoID(int productoID) {
        String query = "DELETE FROM producto_proveedor WHERE ProductoID = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, productoID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

}
