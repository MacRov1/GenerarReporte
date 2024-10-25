/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Clases;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String SKU;
    private int stock;
    private float precioVenta;
    private Categoria categoria;
    private Boolean activo;

    public Producto(){
    }

    public Producto(int id, String nombre, String descripcion, String SKU, int stock, float precioVenta, Categoria categoria, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.SKU = SKU;
        this.stock = stock;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.activo = activo;
    }
    public Producto(int id, String nombre, String descripcion, float precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precio;
        this.stock = stock;
    }
    
    public int getId() { 
        return id; }
    
    public void setId(int id) { 
        this.id = id; }

    public String getNombre() { 
        return nombre; }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; }

    public String getDescripcion() { 
        return descripcion; }
    
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; }

    public String getSKU() { 
        return SKU; }
    
    public void setSKU(String SKU) { 
        this.SKU = SKU; }

    public int getStock() { 
        return stock; }
    
    public void setStock(int stock) { 
        this.stock = stock; }

    public float getPrecioVenta() { 
        return precioVenta; }
    
    public void setPrecioVenta(float precioVenta) { 
        this.precioVenta = precioVenta; }

    public Categoria getCategoria() { 
        return categoria; }
    
    public void setCategoria(Categoria categoria) { 
        this.categoria = categoria; }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}

