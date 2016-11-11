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
    
    public boolean validarCodigo(int cod) {
        boolean ok = false;
        Conexion c = new Conexion();
        int cont = 0;
        try {
            Connection con = c.getConexion();
            String strsql = "select count(*) from codigos where codigo=" + cod;
            PreparedStatement pstm = con.prepareStatement(strsql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                cont = rs.getInt(1);
            }
            if (cont == 1) {
                ok = true;
            } else {
                ok = false;
            }

            con.close();
            rs.close();
            pstm.close();
        } catch (Exception e) {
            ok = false;
            System.out.println(e);
        }
        return ok;
    }

    public boolean registrarEstadistica(String user, int idAct, String rpta) {
        boolean ok = false;
        Conexion c = new Conexion();
        try {
            Connection con = c.getConexion();
            String strsql = "insert into estadisticas (idAct,usuario,respuesta) values(?,?,?)";
            PreparedStatement pstm = con.prepareStatement(strsql);
            pstm.setInt(1, idAct);
            pstm.setString(2, user);
            pstm.setString(3, rpta);
            pstm.executeUpdate();
            ok = true;
            con.close();
            pstm.close();
        } catch (Exception e) {
            ok = false;
            System.out.println(e);
        }
        return ok;
    }
    
    public boolean registrarAsistencia(String user,int idAct){
        boolean ok=false;
        Conexion c = new Conexion();
        try {
            Connection con = c.getConexion();
            String strsql = "insert into asistencia (idAct,usuario) values(?,?)";
            PreparedStatement pstm = con.prepareStatement(strsql);
            pstm.setInt(1, idAct);
            pstm.setString(2, user);
            pstm.executeUpdate();
            ok = true;
            con.close();
            pstm.close();
        } catch (Exception e) {
            ok = false;
            System.out.println(e);
        }
        return ok;
    }
    
    public boolean validarAsistencia(String user,int idAct){
        boolean ok=false;
        Conexion c = new Conexion();
        int cont=0;
        try {
            Connection con = c.getConexion();
            String strsql = "select count(*) from asistencia where idAct="+idAct+" and usuario='"+user+"'";
            PreparedStatement pstm = con.prepareStatement(strsql);
            ResultSet rs=pstm.executeQuery();
            while(rs.next()){
                cont=rs.getInt(1);
            }
            if(cont==0){
                registrarAsistencia(user, idAct);
                ok=true;
            }else{
                ok=false;
            }
            con.close();
            rs.close();
            pstm.close();
        } catch (Exception e) {
            ok = false;
            System.out.println(e);
        }
        return ok;
    }

    public String getNombreApellido(String correo) {
        String nombreA = "";

        Conexion c = new Conexion();
        JSONObject o = new JSONObject();
        try {
            Connection con = c.getConexion();
            String strsql = "SELECT * FROM usuarios where correo='" + correo + "'";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                o.put("nombre", rs.getString("nombre"));
                o.put("edad", rs.getInt("edad"));
                o.put("foto", rs.getString("foto"));
                o.put("correo", rs.getString("correo"));

                nombreA = o.toString();

            }
            pstm.close();
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

            String strsql = "SELECT * FROM usuarioxactividad where idUsuario=" + idCliente;
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                usuAct = new UsuarioxActividad();
                usuAct.setIdActividad(rs.getInt("idAct"));
                usuAct.setIdUsuario(rs.getInt("idUsuario"));
                usuAct.setPuntuacion(rs.getDouble("puntuacion"));

//                System.out.println("obtenerPunt: "+usuAct.getIdActividad()+" - "+usuAct.getIdUsuario()+" - "+usuAct.getPuntuacion());
                uxr.add(usuAct);
            }
            pstm.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return uxr;
    }

    public boolean insertar(String correo, int idAct, float puntuacion) {
        Conexion c = new Conexion();
        boolean ok = false;
        try {
            Connection con = c.getConexion();
            String strsql = "insert into usuarioxactividad (puntuacion,vez,idUsuario,idAct) values(?,?,?,?)";
            PreparedStatement pstm = con.prepareStatement(strsql);
            int idUsuario = 0;
            idUsuario = getId(correo);
            if (!this.existe(idUsuario, idAct)) {
                pstm.setFloat(1, puntuacion);
                pstm.setInt(2, 1);
                pstm.setInt(3, idUsuario);
                pstm.setInt(4, idAct);
                pstm.executeUpdate();
                ok = true;
            } else {
                ok = this.actualizarPuntuacion(idUsuario, idAct, puntuacion);
            }

            con.close();
            pstm.close();
        } catch (Exception e) {
            ok = false;
            System.out.println(e);
        }
        return ok;
    }

    public boolean existe(int idUsuario, int idAct) {
        Conexion c = new Conexion();
        int cont = 0;
        try {
            Connection con = c.getConexion();
            String strsql = "SELECT * FROM usuarioxactividad where idUsuario=" + idUsuario + " and idAct=" + idAct;
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                cont++;
            }
            con.close();
            rs.close();
            pstm.close();
            if (cont == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean actualizarPuntuacion(int idUsuario, int idAct, float puntuacion) {
        Conexion c = new Conexion();
        boolean ok = false;
        double puntAnt = 0;
        int vez = 0;
        int uxa = 0;
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
                ok = true;
                ps1.close();
                con.close();
            }

        } catch (Exception e) {
            ok = false;
            System.out.println(e);
        }
        return ok;
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

        JSONObject ob = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            Connection con = c.getConexion();
            String strsql = "SELECT * FROM actividades";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt("id"));
                o.put("nombre", rs.getString("nombre"));
                o.put("fecha", rs.getString("fecha"));
                o.put("capacidad", rs.getInt("capacidad"));
                o.put("puntaje", rs.getFloat("puntuacion"));
                ja.add(o);
            }
            ob.put("act", ja);

            pstm.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ob.toString();
    }

    public String searchActividades(String search) {
        Conexion c = new Conexion();
        JSONObject ob = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            Connection con = c.getConexion();
            String strsql = "SELECT * FROM actividades where categoria like '%" + search + "%'";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt("id"));
                o.put("nombre", rs.getString("nombre"));
                o.put("fecha", rs.getString("fecha"));
                o.put("capacidad", rs.getInt("capacidad"));
                o.put("puntaje", rs.getFloat("puntuacion"));
                ja.add(o);
            }
            ob.put("act", ja);
            rs.close();
            pstm.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ob.toString();
    }

}
