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
import tesis.dto.UsuarioxActividad;

/**
 *
 * @author Julian
 */
public class Prediccion {

    UsuarioxActividad uxa = new UsuarioxActividad();
    Correlacion c = new Correlacion();

    public List<String> getActividadRecomendada(int idUsuario) {
        Conexion c = new Conexion();
        List<String> l=new ArrayList<>();
        
        //obtener puntuacion promedio del usuario a para la actividad x
        float puntuacion = hallarPrediccion(idUsuario);
        System.out.println("puntuacion: "+puntuacion);
        
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = c.getConexion();
            String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion;
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                l.add(nombre);
                System.out.println(nombre+" - "+rs.getFloat("puntuacion"));
                
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
                
        return l;

    }

    public float hallarPrediccion(int idUsuario) {
        float prediccion = 0;

        //Usuario para evaluar
        List<UsuarioxActividad> uxr = uxa.obtenerPuntuacionesPorUsuario(idUsuario);

        //Promedio del usuario a evaluar
        float promA = this.promedio(uxr);
        //DesvEst del usuario a evaluar
        float desvEst = this.desvEst(uxr);
        //Lista de puntuaciones de un restaurante por usuario

        prediccion = promA + desvEst * this.hallarNumerador(idUsuario, uxr)
                / this.hallarDenominador(idUsuario, uxr);

        return prediccion;
    }

    private float hallarNumerador(int idUsuario, List<UsuarioxActividad> uxr) {
        float num = 0.0f;
        float sum = 0.0f;

        for (int i = 0; i < uxr.size(); i++) {
            sum += ((uxr.get(i).getPuntuacion()
                    - this.promedio(uxa.obtenerPuntuacionesPorUsuario(uxr.get(i).getIdUsuario())))
                    * c.correlacion(idUsuario, uxr.get(i).getIdUsuario()))
                    / this.desvEst(uxa.obtenerPuntuacionesPorUsuario(uxr.get(i).getIdUsuario()));
        }
        return sum;
    }

    private float hallarDenominador(int idUsuario, List<UsuarioxActividad> uxr) {
        float den = 0.0f;
        for (int i = 0; i < uxr.size(); i++) {
            den += c.correlacion(idUsuario, uxr.get(i).getIdUsuario());
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

            acum += (punts.get(i).getPuntuacion())
                    * (punts.get(i).getPuntuacion());
            cont++;
        }
        //cont-1 pasa a ser cont y se inicializa en 1, antes en 0
        desvEst = (float) Math.pow(acum / (cont), 0.5);
        return desvEst;
    }

}
