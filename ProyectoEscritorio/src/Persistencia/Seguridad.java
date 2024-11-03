/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {
    // Método para encriptar la contraseña
    public static String hashPassword(String password) {
        // Genera un hash a partir de la contraseña
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Método para verificar la contraseña
    public static boolean checkPassword(String password, String hashed) {
        // Compara la contraseña ingresada con el hash almacenado
        return BCrypt.checkpw(password, hashed);
    }
}
