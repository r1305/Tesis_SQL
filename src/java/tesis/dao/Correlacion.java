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
public class Correlacion {

    public double correlacion(int id1, int id2) {
        double correlacion = 0.0;
        UsuarioxActividad uxa = new UsuarioxActividad();
        List<UsuarioxActividad> uxa1 = uxa.obtenerPuntuacionesPorUsuario(id1);

        List<UsuarioxActividad> uxa2 = uxa.obtenerPuntuacionesPorUsuario(id2);

        List<Integer> co = coincidencia(uxa1, uxa2);

        float numerador = hallarNumerador(uxa1, uxa2, co);

        float denominador = hallarDenominador(co, uxa1, uxa2);

        correlacion = numerador / denominador;

        return correlacion;
    }
    
    public List<UsuarioxActividad> traerTodos(){
        List<UsuarioxActividad> l=new ArrayList<>();
        UsuarioxActividad uxa;
        Conexion c = new Conexion();
            try {
                Connection con = c.getConexion();
                String strsql = "SELECT * FROM usuarioxactividad";
                PreparedStatement pstm = con.prepareStatement(strsql);

                ResultSet rs = pstm.executeQuery();
                while (rs.next()) {
                    uxa = new UsuarioxActividad();
                    uxa.setIdUsuario(rs.getInt("idUsuario"));
                    uxa.setPuntuacion(rs.getFloat("puntuacion"));
                    uxa.setIdActividad(rs.getInt("idAct"));
                    l.add(uxa);
                }
                rs.close();
                con.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        
        return l;
    }

    private float hallarDenominador(List<Integer> co, List<UsuarioxActividad> lista1,
            List<UsuarioxActividad> lista2) {
        float deno = 0.0f;

        deno = (float) Math.pow(this.sumCuadrado( co,lista1, lista2)
                * this.sumCuadrado(co,lista1, lista2), 0.5);
        return deno;
    }

    private float sumCuadrado(List<Integer> co, List<UsuarioxActividad> lista,List<UsuarioxActividad> lista2) {
        float sum = 0.0f;
        for (int i = 0; i < co.size(); i++) {
            sum += (this.buscarPunt(co.get(i), lista) - promedioLista(lista))
                    * (this.buscarPunt(co.get(i), lista2) - promedioLista(lista2));
        }
        return sum;
    }

    private float hallarNumerador(List<UsuarioxActividad> lista1, List<UsuarioxActividad> lista2, List<Integer> co) {

        float num = 0.0f;
        //float prom=buscarPunt(idRest, lista2);
        for (int i = 0; i < co.size(); i++) {
            num += (buscarPunt(co.get(i), lista1) - promedioLista(lista2))
                    * (buscarPunt(co.get(i), lista2) - promedioLista(lista2));
        }
        return num;
    }

    public float promedioLista(List<UsuarioxActividad> lista) {
        float suma = 0.0f;
        int cont = 0;
        float prom;

        for (int i = 0; i < lista.size(); i++) {
            suma += lista.get(i).getPuntuacion();
            cont++;
        }

        prom = suma / cont;

        return prom;
    }

    public UsuarioxActividad idUsuarios(int id1, int id2) {
        UsuarioxActividad uxa = new UsuarioxActividad();
        UsuarioxActividad uxa3 = null;

        List<UsuarioxActividad> uxa1 = uxa.obtenerPuntuacionesPorUsuario(id1);
        List<UsuarioxActividad> uxa2 = uxa.obtenerPuntuacionesPorUsuario(id2);

        List<Integer> co = coincidencia(uxa1, uxa2);
        Conexion c = new Conexion();
        for (int coin : co) {
            try {
                Connection con = c.getConexion();
                String strsql = "SELECT * FROM usuarioxactividad where idAct=" + coin;
                PreparedStatement pstm = con.prepareStatement(strsql);

                ResultSet rs = pstm.executeQuery();
                while (rs.next()) {
                    uxa3 = new UsuarioxActividad();
                    uxa3.setIdUsuario(rs.getInt("idUsuario"));
                    uxa3.setPuntuacion(rs.getFloat("puntuacion"));
                    uxa3.setIdActividad(rs.getInt("idAct"));

                }
                rs.close();
                con.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        }        
        return uxa3;
    }

    public List<Integer> coincidencia(List<UsuarioxActividad> lista1,
            List<UsuarioxActividad> lista2) {
        List<Integer> co = new ArrayList<>();

        for (int i = 0; i < lista1.size(); i++) {
            if (this.buscar(lista1.get(i).getIdActividad(), lista2)) {
                co.add(lista1.get(i).getIdActividad());
            }
        }
        return co;
    }

    public boolean buscar(int idAct, List<UsuarioxActividad> lista2) {
        boolean enc = false;
        int i = 0;
        for(i=0;i<lista2.size();i++){
            if (lista2.get(i).getIdActividad() == idAct) {
                enc=true;
            }
        }
        

        return enc;
    }

    public double buscarPunt(int idRest, List<UsuarioxActividad> lista) {
        double punt = 0.0f;

        for (int i = 0; i < lista.size(); i++) {

            if (idRest == lista.get(i).getIdActividad()) {
                punt = (float) lista.get(i).getPuntuacion();
            }
        }
        return punt;
    }

}
