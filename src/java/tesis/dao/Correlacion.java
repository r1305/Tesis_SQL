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

    float promU1;
    float promU2;
    List<UsuarioxActividad> uxa1;
    List<UsuarioxActividad> uxa2;

    public double correlacion(int id1, int id2) {
        double correlacion = 0.0;
        UsuarioxActividad uxa = new UsuarioxActividad();
        uxa1 = uxa.obtenerPuntuacionesPorUsuario(id1);

        uxa2 = uxa.obtenerPuntuacionesPorUsuario(id2);

        List<Integer> co = this.coincidencia(uxa1, uxa2);
        promU1=promedio(id1);
        promU2=promedio(id2);
//        promU1 = promedioLista(uxa1);
//        promU2 = promedioLista(uxa2);
//        float prom1 = this.promedioLista(uxa1);
//        float prom2 = this.promedioLista(uxa2);

        float numerador = this.hallarNumerador(promU1, promU2, uxa1, uxa2, co);

        float denominador = this.hallarDenominador(promU1, promU2, co, uxa1, uxa2);

        correlacion = numerador / denominador;

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

    private float hallarDenominador(
            float prom1, float prom2,
            List<Integer> co, List<UsuarioxActividad> lista1,
            List<UsuarioxActividad> lista2) {
        //Math.pow(0, 0);
        float deno = 0.0f;

        deno = (float) Math.pow(this.sumCuadrado(prom1, co, lista1, lista2)
                * this.sumCuadrado(prom2, co, lista1, lista2), 0.5);
//        System.out.println("deno: "+deno);
        return deno;
    }

    private float sumCuadrado(
            float prom, List<Integer> co, 
            List<UsuarioxActividad> lista, 
            List<UsuarioxActividad> lista2) {
        float sum = 0.0f;
        double n1 = 0.0f;
        double n2 = 0.0f;
        for (int i = 0; i < co.size(); i++) {
            n1 = this.buscarPunt(co.get(i), lista) - prom;
            n2 = this.buscarPunt(co.get(i), lista2) - prom;
            if (n1 == 0) {
                n1 = 1;
            }
//            System.out.println("n1: "+n1);
            if (n2 == 0) {
                n2 = 1;
            }
//            System.out.println("n2: "+n2);
            sum += n1 * n2;
//            System.out.println("sumCuad: "+sum);
        }

        return sum;
    }

    private float hallarNumerador(
            float prom1, float prom2,
            List<UsuarioxActividad> lista1,
            List<UsuarioxActividad> lista2, 
            List<Integer> co) {
        float num = 0.0f;
        double n1 = 0.0f;
        double n2 = 0.0f;
        for (int i = 0; i < co.size(); i++) {
            n1 = this.buscarPunt(co.get(i), lista1) - prom1;
            n2 = this.buscarPunt(co.get(i), lista2) - prom2;
            if (n1 == 0) {
                n1 = 1;
            }
//            System.out.println("n1_num: "+n1);
            if (n2 == 0) {
                n2 = 1;
            }
//            System.out.println("n2_num: "+n2);
            num += (n1) * (n2);
//            System.out.println("num: "+num);
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
        if (String.valueOf(prom).equals("NaN")) {
            prom = 0;
        }
//        System.out.println("promedio: "+prom);
        return prom;
    }
    
    public float promedio(int idUsuario) {
        float sum = 0.0f;
        int cont = 0;
        float acum = 0.0f;
        float prom=0.0f;
        Conexion con = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = con.getConexion();
            String sql = "SELECT puntuacion FROM usuarioxactividad where idUsuario=" + idUsuario;
            pr = cn.prepareStatement(sql);
            /*Calcular el promedio*/
            rs = pr.executeQuery();
            while (rs.next()) {
                sum += rs.getFloat(1);
                cont++;
            }
            prom = sum / cont;
            //System.out.println("prom: " + prom);

            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return prom;
    }

    public List<Integer> coincidencia(
            List<UsuarioxActividad> lista1,
            List<UsuarioxActividad> lista2) {
        List<Integer> co = new ArrayList<>();

        for (int i = 0; i < lista1.size(); i++) {
            if (this.buscar(lista1.get(i).getIdActividad(), lista2)) {
                co.add(lista1.get(i).getIdActividad());
            }
        }
        return co;
    }

    //este metodo no se usa
    public boolean buscar(int idAct, List<UsuarioxActividad> lista2) {
        boolean enc = false;
        int i = 0;
        while (i < lista2.size() && !enc) {
            if (lista2.get(i).getIdActividad() == idAct) {
                enc = true;
            }
            i++;
        }
        return enc;
    }

    public double buscarPunt(int idAct, List<UsuarioxActividad> lista) {
        double punt = 0.0f;
        for (int i = 0; i < lista.size(); i++) {
            if (idAct == lista.get(i).getIdActividad()) {
//                System.out.println("idAct: " + idAct + " - " + lista.get(i).getIdActividad());
                punt = (float) lista.get(i).getPuntuacion();
            }
        }
        return punt;
    }

    public UsuarioxActividad idUsuarios(int id1, int id2) {
//        UsuarioxActividad uxa = new UsuarioxActividad();
        UsuarioxActividad uxa3 = null;

//        List<UsuarioxActividad> uxa1 = uxa.obtenerPuntuacionesPorUsuario(id1);
//        List<UsuarioxActividad> uxa2 = uxa.obtenerPuntuacionesPorUsuario(id2);
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
}
