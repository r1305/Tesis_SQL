/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import tesis.dao.Conexion;

/**
 *
 * @author Julian
 */
public class UsuarioxActividad {

    int idUsuario;
    int idActividad;
    double puntuacion;

    public UsuarioxActividad() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getNombreApellido(String correo) {
        String nombreA = "";

        Conexion c = new Conexion();
        JSONObject o = new JSONObject();
        try {
            Connection con = c.getConexion();
            String strsql = "SELECT * FROM usuarios";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                o.put("nombre", rs.getString("nombre"));
                o.put("edad", rs.getInt("edad"));
                o.put("foto", rs.getString("edad"));
                o.put("correo", rs.getString("correo"));
                
                nombreA=o.toString();

            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return nombreA;
    }

    public List<UsuarioxActividad> obtenerPuntuacionesPorUsuario(int idCliente) {
        List<UsuarioxActividad> uxr = new ArrayList<>();
        Conexion c = new Conexion();
        Connection con = c.getConexion();
        UsuarioxActividad usuAct;
        try {

            String strsql = "SELECT * FROM usuarioxactividad where idUsuario="+idCliente;
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                usuAct = new UsuarioxActividad();
                usuAct.setIdActividad(rs.getInt("idAct"));
                usuAct.setIdUsuario(rs.getInt("idUsuario"));
                usuAct.setPuntuacion(rs.getDouble("puntuacion"));

                uxr.add(usuAct);
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return uxr;
    }

    /*public void insertar(String correo, int idAct, float puntuacion) {
     Conexion c = new Conexion();
     MongoCollection<Document> col = c.getConnection("usuarioxactividad");
     int idUsuario = 0;
     idUsuario = getId(correo);

     if (!this.existe(idUsuario, idAct)) {
     Document doc = new Document("_id", this.getCount() + 1);
     doc.append("idUsuario", idUsuario);
     doc.append("idAct", idAct);
     doc.append("puntuacion", puntuacion);
     doc.append("vez", 1);
     col.insertOne(doc);
     } else {
     this.actualizarPuntuacion(idUsuario, idAct, puntuacion);
     }
    

    

     public boolean existe(int idUsuario, int idAct) {
     Conexion c = new Conexion();
     MongoCollection<Document> col = c.getConnection("usuarioxactividad");
     long count = col.count(and(eq("idUsuario", idUsuario), eq("idAct", idAct)));
     System.out.println(c);
     System.out.println(idUsuario);
     System.out.println(idAct);
     if (count == 1) {
     return true;
     } else {
     return false;
     }
     }*/
    public void actualizarPuntuacion(int idUsuario, int idAct, float puntuacion) {
        Conexion c = new Conexion();
        double puntAnt = 0;
        int vez=0;
        int uxa=0;
        try {

            try (Connection con = c.getConexion()) {
                /*query para obtener datos*/
                String strsql = "SELECT * FROM usuarioxactividad where idUsuario=" + idUsuario + " and idAct=" + idAct;
                PreparedStatement pstm = con.prepareStatement(strsql);
                ResultSet rs = pstm.executeQuery();
                while (rs.next()) {
                    puntAnt = rs.getDouble("puntuacion");
                    vez = rs.getInt("vez") + 1;
                    uxa = rs.getInt("id");
                }
                rs.close();
                pstm.close();

                /* Query para hacer el update*/
                double nuevaPunt = nuevaPuntuacion(puntAnt, puntuacion, vez);
                String query = "update usuarioxactividad set puntuacion=" + nuevaPunt + ", vez=" + vez + " where id=" + uxa;
                PreparedStatement ps = con.prepareStatement(query);
                ps.executeUpdate();
                ps.close();

                /*Query para actualizar puntaje de actividades*/
                String query1 = "update actividades set puntuacion=" + nuevaPunt + " where id=" + idAct;
                PreparedStatement ps1 = con.prepareStatement(query1);
                ps1.executeUpdate();
                ps1.close();

                con.close();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public double nuevaPuntuacion(double puntAnt, float puntuacion, int vez) {
        double punt = 0;
        punt = (puntAnt * (vez - 1) + puntuacion) / (vez);
        return punt;
    }

    public int getId(String correo) {
        Conexion c = new Conexion();
        int user = 0;
        try {

            Connection con = c.getConexion();
            String url = "Select id from usuarios where correo='" + correo + "'";

            PreparedStatement pstm = con.prepareStatement(url);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                user = rs.getInt("id");
            }
            rs.close();
            pstm.close();
            con.close();

        } catch (Exception e) {
            user = 0;
            System.out.println(e);
        }
        return user;
    }

    public String getActividades() {
        Conexion c = new Conexion();
        
        JSONObject o = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            Connection con=c.getConexion();
            String strsql = "SELECT * FROM actividades";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                ja.add(rs.getString("nombre"));
            }           
            o.put("act", ja);
            
            pstm.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return o.toString();
    }

}
