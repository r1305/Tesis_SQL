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
import java.util.Objects;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tesis.dto.PuntuacionxActividad;
import tesis.dto.UsuarioxActividad;

/**
 *
 * @author Julian
 */
public class Prediccion {

    UsuarioxActividad uxa = new UsuarioxActividad();
    Correlacion c = new Correlacion();
    float prom = 0.0f;
    float desvEst = 0.0f;

    public String getActividadRecomendada(int idUsuario, String correo) {
        String rpta = "";
        //obtener puntuacion promedio del usuario a para la actividad x
        List<PuntuacionxActividad> puntuacion = hallarPrediccion(idUsuario);
        List<Integer> co = puntuados(idUsuario);
        System.out.println("mis actividades");
        for (int u : co) {
            System.out.println(u);
        }
        try {
            int j = 0;
            while (j < co.size()) {
                for (int i = 0; i < puntuacion.size(); i++) {
                    if (Objects.equals(co.get(j), puntuacion.get(i).getAct())) {
                        puntuacion.remove(i);
                    }
                }
                j++;
            }
            rpta = listarPred(puntuacion);
            JSONParser p = new JSONParser();
            JSONObject o = (JSONObject) p.parse(rpta);
            JSONArray a = (JSONArray) o.get("reco");
            //System.out.println(a.size());
            if (a.size() == 0) {
                rpta = listarxGustos(correo);
            }

        } catch (Exception e) {
            System.out.println("catch: " + e);
            rpta = listarxGustos(correo);//cambiar por listar por gustos
            //System.out.println(rpta);
        }
        System.out.println("recomendaciones");
        for (PuntuacionxActividad puntuacion1 : puntuacion) {
            System.out.println(puntuacion1.getAct());
        }

        System.out.println("rpta: " + rpta);

        return rpta;

    }

    public String listarPred(List<PuntuacionxActividad> l) {
        JSONArray ja = new JSONArray();
        JSONObject ob = new JSONObject();
        for (int i = 0; i < l.size(); i++) {
            Connection cn;
            Conexion con = new Conexion();
            ResultSet rs;
            PreparedStatement pr;

            try {
                cn = con.getConexion();
                //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
                String sql = "";

                sql = "SELECT * FROM actividades where id=" + l.get(i).getAct();
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
                rs.close();
                pr.close();
                cn.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        ob.put("reco", ja);
        return ob.toString();
    }

    public String listarxGustos(String correo) {
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        JSONObject ob = new JSONObject();
        JSONArray ja = new JSONArray();
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "";

            sql = "SELECT * FROM actividades where categoria in(select categoria from usuarioxcategoria where idUsuario='" + correo + "');";
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

    public List<UsuarioxActividad> idCoincidentes(int idUsuario) {
        List<UsuarioxActividad> l = new ArrayList<>();
        Conexion con = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        int cont = 0;
        try {
            cn = con.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
//            String sql = "SELECT uxa.idUsuario,uxa.idAct,uxa.puntuacion FROM tesis.usuarios u "
//                    + "join usuarioxactividad uxa "
//                    + "on u.id=uxa.idUsuario where idUsuario not in (" + idUsuario + ")";
            String sql = "SELECT b.puntuacion, b.idAct,b.idUsuario,a.idUsuario "
                    + "FROM usuarioxactividad b "
                    + "join usuarioxactividad a "
                    + "on (b.idUsuario=" + idUsuario + ") "
                    + "where (a.idAct=b.idAct) "
                    + "and "
                    + "(a.idUsuario<>b.idUsuario) "
                    + "group by idAct;";

            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();

            while (rs.next()) {
                if (idUsuario != rs.getInt(1)) {
//                    System.out.println("idUsuario: "+idUsuario);
//                    System.out.println("idOtro: "+rs.getInt(1));
                    System.out.println("idUsuario: " + idUsuario + " - " + rs.getInt(1));
                    cont++;
                    double cor = c.correlacion(idUsuario, rs.getInt(1));
//                    System.out.println("cor: " + cor);
//                    System.out.println("corre: "+cor);
                    boolean enc = false;
//                    if (cor != 0) {
                    if (String.valueOf(cor) != "NaN") {

                        for (int i = 0; i < l.size(); i++) {
                            if (l.get(i).getIdActividad() == rs.getInt(2)) {
                                enc = true;
                                break;
                            }
                        }
                        if (!enc) {
                            UsuarioxActividad uxa = new UsuarioxActividad();
                            uxa.setIdUsuario(rs.getInt(1));
                            uxa.setIdActividad(rs.getInt(2));
                            uxa.setPuntuacion(rs.getFloat(3));
                            l.add(uxa);
                        }
                    }
                }
            }
            System.out.println("cont: " + cont);
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return l;
    }

    public List<Integer> puntuados(int idUsuario) {
        List<Integer> l = new ArrayList<>();
        Conexion c = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = c.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "SELECT * FROM usuarioxactividad where idUsuario=" + idUsuario;
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                l.add(rs.getInt("idAct"));
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return l;
    }

    public List<UsuarioxActividad> todos() {
        List<UsuarioxActividad> l = new ArrayList<>();
        Conexion c = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = c.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "SELECT * FROM usuarioxactividad";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                UsuarioxActividad uxa = new UsuarioxActividad();
                uxa.setIdUsuario(rs.getInt("idUsuario"));
                uxa.setIdActividad(rs.getInt("idAct"));
                uxa.setPuntuacion(rs.getFloat("puntuacion"));
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return l;
    }

    public List<Integer> todasActividades() {
        List<Integer> l = new ArrayList<>();
        Conexion c = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = c.getConexion();
            //String sql = "SELECT * FROM actividades where puntuacion>=" + puntuacion+" and fecha>=current_date()";
            String sql = "SELECT * FROM actividades";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                l.add(rs.getInt("id"));
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return l;
    }

    public List<PuntuacionxActividad> hallarPrediccion(int idUsuario) {
        float prediccion = 0;

        //Usuario para evaluar
        List<UsuarioxActividad> uxr = uxa.obtenerPuntuacionesPorUsuario(idUsuario);
        List<UsuarioxActividad> uxr2 = c.traerTodos();
        promedio(idUsuario);
        List<PuntuacionxActividad> pred = new ArrayList<>();
        List<UsuarioxActividad> idActs = idCoincidentes(idUsuario);

        for (int i = 0; i < idActs.size(); i++) {
            PuntuacionxActividad pxl = new PuntuacionxActividad();
            prediccion = prom + desvEst * this.hallarNumerador(idUsuario, uxr2, idActs.get(i).getIdActividad())
                    / this.hallarDenominador(idUsuario, uxr2);
            pxl.setAct(idActs.get(i).getIdActividad());
            pxl.setPunt(prediccion);
            pred.add(pxl);
        }
        return pred;
    }

    private float hallarNumerador(int idUsuario, List<UsuarioxActividad> uxr, int idAct) {
        float sum = 0.0f;
        double s = 0.0f;
        //aqui debo llamar a idUsuarios
        UsuarioxActividad uxa = new UsuarioxActividad();
        List<UsuarioxActividad> uxa1 = idCoincidentes(idUsuario);
        for (int i = 0; i < uxa1.size(); i++) {
            s = ((puntuacionCruzada(idUsuario, idAct) - prom)
                    / desvEst * c.correlacion(idUsuario, uxa1.get(i).getIdUsuario()));
            if (String.valueOf(s).equals("NaN")) {
                s = 0;
            }
            sum += s;
        }
        return sum;
    }

    public float puntuacionCruzada(int idUsuario, int idAct) {
        float punt = 0.0f;
        Conexion con = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = con.getConexion();
            String sql = "SELECT puntuacion FROM usuarioxactividad where idUsuario=" + idUsuario + " and idAct=" + idAct;
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                try {
                    punt = rs.getFloat(1);
                } catch (Exception e) {
                    punt = 0;
                }
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return punt;
    }

    private float hallarDenominador(int idUsuario, List<UsuarioxActividad> uxr) {
        float den = 0.0f;
        UsuarioxActividad uxa = new UsuarioxActividad();
        List<UsuarioxActividad> uxa1 = new ArrayList<>();
        for (int i = 0; i < uxr.size(); i++) {
            uxa1.add(c.idUsuarios(idUsuario, uxr.get(i).getIdUsuario()));
        }
        for (int i = 0; i < uxa1.size(); i++) {
            try {
                den += c.correlacion(idUsuario, uxa1.get(i).getIdUsuario());
            } catch (Exception e) {
                den += 0;
            }
        }
        return den;
    }

    public void promedio(int idUsuario) {
        float sum = 0.0f;
        int cont = 0;
        float acum = 0.0f;
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
            System.out.println("prom: " + prom);

            /*calcular la desvEst*/
            rs = pr.executeQuery();
            while (rs.next()) {
                acum += (rs.getFloat(1) - prom) * (rs.getFloat(1) - prom);
            }
            desvEst = (float) Math.pow(acum / (cont), 0.5);
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
