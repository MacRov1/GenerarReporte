/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.Controladores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.Clases.Usuario;
import logica.Interfaces.IControladorUsuario;
import logica.servicios.UsuariosServicios;

/**
 *
 * @author LucasCiceri
 */
public class ControladorUsuario implements IControladorUsuario {

    private Map<String, Usuario> Usuarios;
    private UsuariosServicios servicioUsuarios;
    private static ControladorUsuario instancia;

    public ControladorUsuario() {
        this.servicioUsuarios = new UsuariosServicios();
    }

    public static ControladorUsuario getInstance() {
        if (instancia == null) {
            instancia = new ControladorUsuario();
        }
        return instancia;
    }

    public ArrayList<Usuario> listUsers() {
        ArrayList<Usuario> usuarios = servicioUsuarios.getUsers();
        return usuarios;
    }

    public boolean login(String email, String password) {
        try {
            Usuario usuario = (Usuario) servicioUsuarios.getUser(email);
            System.out.println("usu: " + usuario);
            if (usuario == null) {
                return false;
            } else if (password.equals(usuario.getPassword())) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}
