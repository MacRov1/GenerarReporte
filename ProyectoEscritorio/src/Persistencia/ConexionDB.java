package Persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConexionDB {
    private String host;
    private String port;
    private String db;
    private String user;
    private String pass;
    
    private Connection conexion = null;

    public ConexionDB() {
        // Leer las propiedades desde el archivo Config.properties
        host = LeerProperties("host");
        port = LeerProperties("port");
        db = LeerProperties("db");
        user = LeerProperties("user");
        pass = LeerProperties("pass");
    }
    
    public String LeerProperties(String caso) {
        Properties prop = new Properties();
        InputStream archivo = null;

        try {
            archivo = new FileInputStream(System.getProperty("user.dir") + "\\Config.properties");
            prop.load(archivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            if (archivo != null) {
                try {
                    archivo.close(); // Cerrar el archivo después de usarlo
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        }
        
        return prop.getProperty(caso, ""); // Devuelve vacío si la propiedad no se encuentra
    }
   
    public Connection getConexion() {
        if (conexion == null) {
            try {
                Driver driver = new com.mysql.cj.jdbc.Driver();
                DriverManager.registerDriver(driver);
                conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conexion;
    }
    
    public void cerrar() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
}
