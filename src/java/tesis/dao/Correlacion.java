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
    List<Integer> co;
    float sum;
    Conexion c = new Conexion();
    Connection con = c.getConexion();

    public double correlacion(int id1, int id2) {
        double correlacion = 0.0;
        UsuarioxActividad uxa = new UsuarioxActividad();
        uxa1 = uxa.obtenerPuntuacionesPorUsuario(id1);

        uxa2 = uxa.obtenerPuntuacionesPorUsuario(id2);

        co = this.coincidencia(uxa1, uxa2);
        promU1 = promedio(id1);
        promU2 = promedio(id2);
        double n1=0;
        double n2=0;
        /*Sumatoria de i=1 hasta m de (ra,i-promA)*(ru,i-promU)*/
        for (int i = 0; i < co.size(); i++) {
            n1 = this.buscarPunt(co.get(i), uxa1) - promU1;
            n2 = this.buscarPunt(co.get(i), uxa2) - promU2;
            if (n1 == 0) {
                n1 = 1;
            }
            if (n2 == 0) {
                n2 = 1;
            }
            sum += (n1) * (n2);

        }

        double numerador = sum; //this.hallarNumerador(uxa1, uxa2);
        double denominador =Math.pow(sum * sum, 0.5);// this.hallarDenominador(uxa1, uxa2);
        correlacion = numerador / denominador;
        return correlacion;
    }

    public List<UsuarioxActividad> traerTodos() {
        List<UsuarioxActividad> l = new ArrayList<>();
        UsuarioxActividad uxa;
        
        try {
            
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

//    private float hallarDenominador(List<UsuarioxActividad> lista1,
//            List<UsuarioxActividad> lista2) {
//        float deno = 0.0f;
//        deno = (float) Math.pow(sum * sum, 0.5);
//        return deno;
//    }
//
//    private float sumCuadrado(
//            List<UsuarioxActividad> lista,
//            List<UsuarioxActividad> lista2) {
//        float sum = 0.0f;
//        double n1 = 0.0f;
//        double n2 = 0.0f;
//        for (int i = 0; i < co.size(); i++) {
//            n1 = this.buscarPunt(co.get(i), lista) - promU1;
//            n2 = this.buscarPunt(co.get(i), lista2) - promU2;
//            if (n1 == 0) {
//                n1 = 1;
//            }
//            if (n2 == 0) {
//                n2 = 1;
//            }
//            sum += n1 * n2;
//        }
//        return sum;
//    }
//
//    private float hallarNumerador(List<UsuarioxActividad> lista1,
//            List<UsuarioxActividad> lista2) {
//        float num = 0.0f;
//        double n1 = 0.0f;
//        double n2 = 0.0f;
//        for (int i = 0; i < co.size(); i++) {
//            n1 = this.buscarPunt(co.get(i), lista1) - promU1;
//            n2 = this.buscarPunt(co.get(i), lista2) - promU2;
//            if (n1 == 0) {
//                n1 = 1;
//            }
//            if (n2 == 0) {
//                n2 = 1;
//            }
//            num += (n1) * (n2);
//
//        }
//        return num;
//    }

    public float promedio(int idUsuario) {
        float sum = 0.0f;
        int cont = 0;
        float prom = 0.0f;
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
            if (this.buscarQuick(lista1.get(i).getIdActividad(), lista2)) {
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
        return enc;
    }

    public boolean buscarQuick(int idAct, List<UsuarioxActividad> lista2) {
        boolean enc = false;
        int n = lista2.size();
        int centro, inf = 0, sup = n - 1;
        while (inf <= sup) {
            centro = ((sup + inf) / 2);
            
            //System.out.println("idAct: "+lista2.get(centro).getIdActividad());
            if (lista2.get(centro).getIdActividad() == idAct) {
                return true;
            } else if (idAct < lista2.get(centro).getIdActividad()) {
                sup = centro - 1;
            } else {
               inf=centro+1;
            }
        }

        return enc;
    }

    public double buscarPunt(int idAct, List<UsuarioxActividad> lista) {
        double punt = 0.0f;
        for (int i = 0; i < lista.size(); i++) {
            if (idAct == lista.get(i).getIdActividad()) {
                punt = (float) lista.get(i).getPuntuacion();
            }
        }
        return punt;
    }
    
    public UsuarioxActividad idUsuarios(int id1, int id2) {
        UsuarioxActividad uxa3 = null;

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
