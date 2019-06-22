package proyectounix;

import java.sql.*;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class ConexionDB {
    private Connection conexion;
    private PreparedStatement consulta;
    private PreparedStatement inserta;
    public ResultSet datos;//posible public
    private int datosi;
    private final String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://192.168.1.103:3306/";
    
    public ConexionDB() {
        this.conexion = null;
        this.consulta = null;
        this.datos = null;
    }
    
    public Connection getConnection(String DB, String usr, String psswrd) {
        try {
            Class.forName(driver);
            url += DB;
            conexion = DriverManager.getConnection(url,usr,psswrd);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la conexion", "Error", ERROR_MESSAGE);
            conexion = null;
        }
        return conexion;
    }
    
    public void consultar(String DB, String usr, String psswrd, String sql) {
        try {
            conexion = (Connection) this.getConnection(DB, usr, psswrd);
            consulta = conexion.prepareStatement(sql);
            datos = consulta.executeQuery();
            
               
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo hacer la consulta", "Error", ERROR_MESSAGE);
        }
    }

    
    public void actulizar(String DB, String usr, String psswrd, String sql) {
        try {
            conexion = (Connection) this.getConnection(DB, usr, psswrd);
            inserta = conexion.prepareStatement(sql);
            datosi = inserta.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede actualizar", "Error", ERROR_MESSAGE);
        } finally {
            this.desconectar();
        }
        
    }
    
    public void insertar(String DB, String usr, String psswrd, String pro, Integer can, Float cos) {
        int max = 0;
        try {
            conexion = (Connection) this.getConnection(DB, usr, psswrd);
            inserta = conexion.prepareStatement("INSERT INTO tienda.Inventario (ID, Producto, Cantidad, "
                    + "Costo) VALUES (NULL, ?, ?, ?)");
            inserta.setString(1,pro);
            inserta.setInt(2,can);
            inserta.setFloat(3,cos);
            consulta = conexion.prepareStatement("SELECT MAX(ID)FROM tienda.Inventario");
            datosi = inserta.executeUpdate();
            datos = consulta.executeQuery();
            while (datos.next()) {
                max = datos.getInt("MAX(ID)");
            } 
            if (datosi == 1) {
                JOptionPane.showMessageDialog(null, "Valores correctamente insertados\nClave: "+max, "Informacion", INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al inserar", "Informacion", WARNING_MESSAGE);
            }
   
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede insertarr", "Error", ERROR_MESSAGE);
        } finally {
            this.desconectar();
        }
    }   
    
    public void desconectar() {
        try {
           if(conexion != null) conexion.close();
           if(consulta != null) consulta.close();
           if(datos != null) datos.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo desconectar", "Error", ERROR_MESSAGE);
        }
    }
}
