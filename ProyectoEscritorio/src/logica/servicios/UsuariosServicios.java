/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.servicios;

import Persistencia.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logica.Clases.Usuario;

/**
 *
 * @author LucasCiceri
 */
public class UsuariosServicios {

    public UsuariosServicios() {
    }
    private Connection conexion = new ConexionDB().getConexion();

    /*  public Usuario getUser(String email) throws SQLException {
        Usuario result = null;
        result = null;
        try {

            ArrayList<Usuario> resultado = new ArrayList<Usuario>();

            PreparedStatement status = conexion.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = status.executeQuery();
            System.out.println("NExt" + status);
            System.out.println(rs.next());
            while (rs.next()) {
                String mail = rs.getString("email");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String password = rs.getString("password");
                System.out.println(mail + apellido + password);
                resultado.add(new Usuario(mail, nombre, apellido, password));
            }
            rs.close();
            for (Usuario usuario : resultado) {
                System.out.println(usuario.getEmail().toString() +"--"+ email.toString());
                System.out.println(usuario.getEmail().toString().equals(email.toString()));
                if (usuario.getEmail().equals(email)) {
                    System.out.println("logica.servicios.UsuariosServicios.getUser()");
                    result = usuario;
                }
            }
            System.out.println(result);
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }*/
    public Usuario getUser(String email) throws SQLException {
        try {
            Usuario resultado = new Usuario();
            PreparedStatement status = conexion.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = status.executeQuery();
            while (rs.next()) {
                String mail = rs.getString("email");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String password = rs.getString("password");
                if (mail.equals(email)) {
                    resultado = new Usuario(mail, nombre, apellido, password);
                };
            }
            rs.close();
            return resultado;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<Usuario> getUsers() {
        ArrayList<Usuario> resultado = new ArrayList<Usuario>();
        try {
            PreparedStatement status = conexion.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = status.executeQuery();
            while (rs.next()) {
                resultado.add(new Usuario(rs.getString("nombre"), rs.getString("apellido"), null));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return resultado;
    }

}
