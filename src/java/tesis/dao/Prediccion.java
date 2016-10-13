/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tesis.dto.UsuarioxActividad;

/**
 *
 * @author Julian
 */
public class Prediccion {

    UsuarioxActividad uxa = new UsuarioxActividad();
    Correlacion c = new Correlacion();

    public String getActividadRecomendada(int idUsuario) {
        Conexion c = new Conexion();
        List<String> l = new ArrayList<>();

        //obtener puntuacion promedio del usuario a para la actividad x
        float puntuacion = hallarPrediccion(idUsuario);
        System.out.println("puntuacion: " + puntuacion);

        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        JSONObject ob = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            cn = c.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql="";
            if (String.valueOf(puntuacion) == "NaN") {
                sql = "SELECT * FROM actividades order by puntuacion limit 3";
            }else{
                sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion;
            }
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt("id"));
                o.put("nombre", rs.getString("nombre"));
                o.put("fecha", rs.getString("fecha"));
                o.put("capacidad", rs.getInt("capacidad"));
                o.put("puntaje", rs.getFloat("puntuacion"));
                ja.add(o);

            }
            ob.put("reco", ja);
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return ob.toString();

    }

    public float hallarPrediccion(int idUsuario) {
        float prediccion = 0;

        //Usuario para evaluar
        List<UsuarioxActividad> uxr = uxa.obtenerPuntuacionesPorUsuario(idUsuario);
        List<UsuarioxActividad> uxr2 = c.traerTodos();

        //Promedio del usuario a evaluar
        float promA = this.promedio(uxr);
        //DesvEst del usuario a evaluar
        float desvEst = this.desvEst(uxr);
        //Lista de puntuaciones de un restaurante por usuario

        prediccion = promA + desvEst * this.hallarNumerador(idUsuario, uxr2)
                / this.hallarDenominador(idUsuario, uxr2);

        return prediccion;
    }

    private float hallarNumerador(int idUsuario, List<UsuarioxActividad> uxr) {
        float sum = 0.0f;
        //aqui debo llamar a idUsuarios
        UsuarioxActividad uxa = new UsuarioxActividad();
        List<UsuarioxActividad> uxa1 = new ArrayList<>();

        for (int i = 0; i < uxr.size(); i++) {
            uxa1.add(c.idUsuarios(idUsuario, uxr.get(i).getIdUsuario()));
        }
        for (int i = 0; i < uxa1.size(); i++) {
            sum += ((uxa1.get(i).getPuntuacion() - this.promedio(uxa.obtenerPuntuacionesPorUsuario(uxa1.get(i).getIdUsuario())))
                    / this.desvEst(uxa.obtenerPuntuacionesPorUsuario(uxa1.get(i).getIdUsuario())))
                    * c.correlacion(idUsuario, uxa1.get(i).getIdUsuario());
        }
        return sum;
    }

    private float hallarDenominador(int idUsuario, List<UsuarioxActividad> uxr) {
        float den = 0.0f;
        UsuarioxActividad uxa = new UsuarioxActividad();
        List<UsuarioxActividad> uxa1 = new ArrayList<>();

        for (int i = 0; i < uxr.size(); i++) {
            uxa1.add(c.idUsuarios(idUsuario, uxr.get(i).getIdUsuario()));
        }
        for (int i = 0; i < uxa1.size(); i++) {
            den += c.correlacion(idUsuario, uxa1.get(i).getIdUsuario());
        }
        return den;
    }

    private float promedio(List<UsuarioxActividad> punts) {
        float prom = 0.0f;
        float sum = 0.0f;
        int cont = 0;
        for (int i = 0; i < punts.size(); i++) {
            sum += punts.get(i).getPuntuacion();
            cont++;
        }
        prom = sum / cont;
        return prom;
    }

    public float desvEst(List<UsuarioxActividad> punts) {
        float desvEst = 0.0f;
        float acum = 0.0f;
        int cont = 0;
        for (int i = 0; i < punts.size(); i++) {

            acum += (punts.get(i).getPuntuacion()-promedio(punts))
                    * (punts.get(i).getPuntuacion()-promedio(punts));
            cont++;
        }
        //cont-1 pasa a ser cont y se inicializa en 1, antes en 0
        desvEst = (float) Math.pow(acum / (cont), 0.5);
        return desvEst;
    }

}
