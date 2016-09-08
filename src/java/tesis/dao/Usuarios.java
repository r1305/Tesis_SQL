/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
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

            String query = "update usuarios set nombre='" + nombre + "', edad="+edad+",url='"+url+"' where correo=" + id;

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
                    + ",'"+clave+"')";

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

    /*public boolean validarUsuario(String correo) {
        boolean ok = false;
        MongoCollection<Document> col = c.getConnection("usuarios");
        //devuelve el usuario que tenga el mismo correo, si existe
        try {
            Document doc = col.find(eq("correo", correo)).first();
            String c = doc.getString("correo");

            if (c != "") {
                ok = true;
            }
        } catch (Exception e) {
            ok = false;
        }
        return ok;
    }

    public String login(String u, String p) {
        String id;
        MongoCollection<Document> col = c.getConnection("usuarios");
        try {
            Document doc = col.find(eq("correo", u)).first();
            if (u.equals(doc.getString("correo")) && p.equals(doc.getString("clave"))) {
                id = doc.getString("correo");
            } else {
                id = "error";
            }
        } catch (NullPointerException e) {
            id = "error";
        }

        return id;
    }

    public String getFbLogin(String correo) {
        String id = "";

        MongoCollection<Document> col = c.getConnection("usuarios");
        try {
            Document doc = col.find(eq("correo", correo)).first();
            if (correo.equals(doc.getString("correo"))) {
                id = String.valueOf(doc.getInteger("_id"));
            }
        } catch (NullPointerException e) {
            id = "fail";
        }

        return id;
    }*/

}
