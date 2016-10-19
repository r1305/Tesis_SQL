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
//        List<UsuarioxActividad> uxa1 = uxa.obtenerPuntuacionesPorUsuario(id1);
//        for(UsuarioxActividad u:uxa1){
//            System.out.println(u.getIdActividad()+" - "+u.getPuntuacion());
//        }
//
//        List<UsuarioxActividad> uxa2 = uxa.obtenerPuntuacionesPorUsuario(id2);
//        for(UsuarioxActividad u:uxa2){
//            System.out.println(u.getIdActividad()+" - "+u.getPuntuacion());
//        }
//
//        List<Integer> co = coincidencia(uxa1, uxa2);
//        for(int i:co){
//            System.out.println(i);
//        }

        List<UsuarioxActividad> uxa1 = uxa.obtenerPuntuacionesPorUsuario(id1);
//        System.out.println("usuario: " + id1);
//        for (UsuarioxActividad u : uxa1) {
//            System.out.println(u.getIdActividad() + " - " + u.getPuntuacion());
//        }
        
//        System.out.println("usuario: " + id2);
        List<UsuarioxActividad> uxa2 = uxa.obtenerPuntuacionesPorUsuario(id2);
//        for (UsuarioxActividad u : uxa2) {
//            System.out.println(u.getIdActividad() + " - " + u.getPuntuacion());
//        }
        
//        System.out.println("*****");
        
        List<Integer> co = this.coincidencia(uxa1, uxa2);
        float prom1 = this.promedioLista(uxa1, co);
//        System.out.println("** 1 **");
//        System.out.println("prom1: "+prom1);
        float prom2 = this.promedioLista(uxa2, co);
//        System.out.println("** 2 **");
//        System.out.println("prom1: "+prom2);

        float numerador = this.hallarNumerador(prom1, prom2, uxa1, uxa2, co);

        float denominador = this.hallarDenominador(prom1, prom2, co, uxa1, uxa2);

//        float numerador = hallarNumerador(uxa1, uxa2, co);
//        System.out.println("numerador: "+numerador);
//
//        float denominador = hallarDenominador(co, uxa1, uxa2);
//        System.out.println("deno: "+denominador);
        correlacion = numerador / denominador;
//        if(String.valueOf(correlacion).equals("NaN")){
//            correlacion=0;
//        }

        return correlacion;
    }

    public List<UsuarioxActividad> traerTodos() {
        List<UsuarioxActividad> l = new ArrayList<>();
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

    private float hallarDenominador(float prom1, float prom2,
            List<Integer> co, List<UsuarioxActividad> lista1,
            List<UsuarioxActividad> lista2) {
        //Math.pow(0, 0);
        float deno = 0.0f;

        deno = (float) Math.pow(this.sumCuadrado(prom1, co, lista1,lista2)
                * this.sumCuadrado(prom2, co,lista1, lista2), 0.5);
//        System.out.println("deno: "+deno);
        return deno;
    }

//    private float hallarDenominador(List<Integer> co, List<UsuarioxActividad> lista1,
//            List<UsuarioxActividad> lista2) {
//        float deno = 0.0f;
//
//        deno = (float) Math.pow(this.sumCuadrado(co, lista1, lista2)
//                * this.sumCuadrado(co, lista1, lista2), 0.5);
//        return deno;
//    }
    private float sumCuadrado(float prom, List<Integer> co, List<UsuarioxActividad> lista, List<UsuarioxActividad> lista2) {
        float sum = 0.0f;
        for (int i = 0; i < co.size(); i++) {
            sum += (this.buscarPunt(co.get(i), lista) - prom)
                    * (this.buscarPunt(co.get(i), lista2) - prom);
//            System.out.println("sumCuad: "+sum);
        }
        
        return sum;
    }

//    private float sumCuadrado(List<Integer> co, List<UsuarioxActividad> lista, List<UsuarioxActividad> lista2) {
//        float sum = 0.0f;
//        for (int i = 0; i < co.size(); i++) {
//            sum += (this.buscarPunt(co.get(i), lista) - promedioLista(lista,co))
//                    * (this.buscarPunt(co.get(i), lista2) - promedioLista(lista2,co));
//        }
//        return sum;
//    }
    private float hallarNumerador(float prom1, float prom2,
            List<UsuarioxActividad> lista1, List<UsuarioxActividad> lista2, List<Integer> co) {
        float num = 0.0f;
        for (int i = 0; i < co.size(); i++) {
            num += (this.buscarPunt(co.get(i), lista1) - prom1)
                    * (this.buscarPunt(co.get(i), lista2) - prom2);
//            System.out.println("num: "+num);
        }
        
        return num;
    }

//    private float hallarNumerador(List<UsuarioxActividad> lista1, List<UsuarioxActividad> lista2, List<Integer> co) {
//
//        float num = 0.0f;
//        //float prom=buscarPunt(idRest, lista2);
//        for (int i = 0; i < co.size(); i++) {
//            System.out.println(buscarPunt(co.get(i), lista1)+" - "+promedioLista(lista1,co));
//            num += (buscarPunt(co.get(i), lista1) - promedioLista(lista1,co))
//                    * (buscarPunt(co.get(i), lista2) - promedioLista(lista2,co));
//            System.out.println("num+= "+num);
//        }
//        return num;
//    }
    public float promedioLista(List<UsuarioxActividad> lista,
            List<Integer> co) {
        float suma = 0.0f;
        int cont = 0;
        float prom;

        for (int i = 0; i < lista.size(); i++) {
            suma += lista.get(i).getPuntuacion();
//            System.out.println("sumI: "+suma);
            cont++;
//            System.out.println("contI: "+cont);
        }
//        System.out.println("suma: "+suma);
        prom = suma / cont;
        if(String.valueOf(prom).equals("NaN")){
            prom=0;
        }
//        System.out.println("promedio: "+prom);
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
        while (i < lista2.size() && !enc) {
            if (lista2.get(i).getIdActividad() == idAct) {
                enc = true;
            }
            i++;
        }
//        for (i = 0; i < lista2.size(); i++) {
//            if (lista2.get(i).getIdActividad() == idAct) {
//                enc = true;
//            }
//        }
        return enc;
    }

    public double buscarPunt(int idAct, List<UsuarioxActividad> lista) {
        double punt = 0.0f;
        boolean enc = false;
//        int i = 0;
//        while (!enc && i < lista.size()) {
//            if (idAct == lista.get(i).getIdActividad()) {
//                enc = true;
//                punt = (float) lista.get(i).getPuntuacion();
//                System.out.println("punt: "+punt);
//            }
//            i++;
//        }
        for (int i = 0; i < lista.size(); i++) {
            if (idAct == lista.get(i).getIdActividad()) {
                punt = (float) lista.get(i).getPuntuacion();
//                System.out.println("punt: "+punt);
            }
        }
        return punt;
    }
}
