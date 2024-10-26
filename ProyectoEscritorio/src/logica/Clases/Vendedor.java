/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Clases;
import java.util.Date;
/**
 *
 * @author UnwantedOpinion
 */
public class Vendedor {
    private int id;
    private String nomUsuario;
    private String contrasenia;
    
    private String nombre;
    private int cedula;
    private String telefono;
    private String correo;
    private String direccion;
    private Date fechaContratacion;
    private Boolean activo;
    
    public Vendedor(){
        
    }
    
    public Vendedor(int id, String nombre, int cedula, String correo, String telefono, String direccion, Date fechaContratacion) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaContratacion = fechaContratacion;
    }
    
    public Vendedor(int id, String nombre, int cedula, String telefono, String correo, String direccion, Date fechaContratacion, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }

    public Vendedor(int id, String nomUsuario, String contrasenia, String nombre, int cedula, String correo, String telefono, String direccion, Date fechaContratacion, Boolean activo) {
        this.id = id;
        this.nomUsuario = nomUsuario;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
