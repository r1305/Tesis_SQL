
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import tesis.dao.Conexion;
import tesis.dao.Correlacion;
import tesis.dao.Prediccion;
import tesis.dao.Usuarios;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class NewMain {

    /**
     * @param args the command line arguments
     */
    public void insertarCódigo(int i) {
        Conexion c = new Conexion();
        try (Connection conn = c.getConexion()) {
            String query = "INSERT INTO codigos"
                    + "(codigo)"
                    + "     VALUES"
                    + "(" + i + ")";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here       
        Prediccion p = new Prediccion();
//        System.out.println("usuario: rogger.aburto@gmail.com");
//        p.getActividadRecomendada(62,"rogger.aburto@gmail.com");
//        System.out.println("");
//        System.out.println("");
        System.out.println("usuario: u2@gmail.com");     
        p.getActividadRecomendada(86,"u2@gmail.com");
//        System.out.println("usuario: usuario@gmail.com");     
//        p.getActividadRecomendada(62,"usuario@gmail.com");
//        NewMain n = new NewMain();
//        for (int i = 0; i < 3000; i++) {
//            n.insertarCódigo(i+1);
//        }

    }

}
