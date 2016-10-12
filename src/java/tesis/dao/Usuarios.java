/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;

/**
 *
 * @author Julian
 */
public class Usuarios {

    Conexion c = new Conexion();

    public boolean updateFoto(int id, String url) {

        boolean ok = false;
        try {

            Connection conn = c.getConexion();

            String query = "update usuarios set foto='" + url + "' where correo=" + id;

            PreparedStatement ps = conn.prepareStatement(query);

            ps.executeUpdate();
            ok = true;
            ps.close();
            conn.close();
        } catch (Exception ex) {
            ok = false;
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ok;
    }

    public boolean updatePerfil(int id, String nombre, int edad, String url) {
        boolean ok = false;
        try {

            Connection conn = c.getConexion();

            String query = "update usuarios set nombre='" + nombre + "', edad=" + edad + ",url='" + url + "' where correo=" + id;

            PreparedStatement ps = conn.prepareStatement(query);

            ps.executeUpdate();
            ok = true;
            ps.close();
            conn.close();
        } catch (Exception ex) {
            ok = false;
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ok;

    }

    public boolean createUser(String nombre, int edad, String url, String correo, String clave) {
        boolean ok = false;

        try (Connection conn = c.getConexion()) {
            String query = "INSERT INTO usuarios"
                    + "(nombre"
                    + ",edad"
                    + ",foto"
                    + ",correo"
                    + ",clave)"
                    + "     VALUES"
                    + "('" + nombre + "'"
                    + ",'" + edad + "'"
                    + "," + url + ""
                    + ",'" + correo + "'"
                    + ",'" + clave + "')";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            ok = true;
            ps.close();
            conn.close();
        } catch (Exception ex) {
            ok = false;
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ok;
    }

    public boolean validarUsuario(String correo) {
        boolean ok = false;
        String co = "";
        try {
            Connection con = c.getConexion();
            String strsql = "SELECT * FROM usuarios where correo='" + correo + "'";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                co = rs.getString("correo");
            }
            if (co != "" || co != null) {
                ok = true;
            }

            pstm.close();
            con.close();

        } catch (Exception e) {
            ok = false;
            System.out.println(e);
        }
        return ok;
    }

    public String login(String u, String p) {
        String id="";
        try {
            Connection con = c.getConexion();
            String strsql = "SELECT * FROM usuarios where correo='" + u + "' and clave='"+p+"'";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                id = rs.getString("correo");
            }

            pstm.close();
            con.close();

        } catch (Exception e) {
            id = "error";
            System.out.println(e);
        }        

        return id;
    }

    public String getFbLogin(String correo) {
        String id = "";
        try{
            Connection con=c.getConexion();
            String strsql = "SELECT * FROM usuarios where correo='"+correo+"'";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                id=String.valueOf(rs.getString("id"));
            }
            pstm.close();
            rs.close();
            con.close();
            
        }catch(Exception e){
            id="fail";
            System.out.println(e);
        }       

        return id;
    }

}
