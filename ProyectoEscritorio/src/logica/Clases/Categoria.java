/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Clases;

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion;
    private Boolean activo;

    public Categoria() {
    }

    public Categoria(int id, String nombre, String descripcion, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() { 
        return nombre; }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; }

    public String getDescripcion() { 
        return descripcion; }
    
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}

