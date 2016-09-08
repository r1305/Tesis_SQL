/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dao;

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
        for(int i=0;i<co.size();i++){
            System.out.println(co.get(i));
        }

        float numerador = hallarNumerador(uxa1, uxa2, co);

        float denominador = hallarDenominador(co, uxa1, uxa2);

        correlacion = numerador / denominador;

        return correlacion;
    }

    private float hallarDenominador(List<Integer> co, List<UsuarioxActividad> lista1,
            List<UsuarioxActividad> lista2) {
        float deno = 0.0f;

        deno = (float) Math.pow(this.sumCuadrado(co, lista1)
                * this.sumCuadrado(co, lista2), 0.5);
        return deno;
    }

    private float sumCuadrado(List<Integer> co, List<UsuarioxActividad> lista) {
        float sum = 0.0f;
        for (int i = 0; i < co.size(); i++) {
            sum += (this.buscarPunt(co.get(i), lista))
                    * (this.buscarPunt(co.get(i), lista));
        }
        return sum;
    }

    private float hallarNumerador(List<UsuarioxActividad> lista1, List<UsuarioxActividad> lista2, List<Integer> co) {

        float num = 0.0f;
        for (int i = 0; i < co.size(); i++) {
            num += (buscarPunt(co.get(i), lista1))
                    * (buscarPunt(co.get(i), lista2));
        }
        return num;
    }

    public float promedioLista(List<UsuarioxActividad> lista, List<Integer> co) {
        float suma = 0.0f;
        int cont = 0;
        float prom;

        for (int i = 0; i < co.size(); i++) {
            suma += buscarPunt(co.get(i), lista);
            cont++;
        }

        prom = suma / cont;

        return prom;
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

    public boolean buscar(int idRest, List<UsuarioxActividad> lista2) {
        boolean enc = false;
        int i = 0;
        while (i < lista2.size() && !enc) {
            if (lista2.get(i).getIdActividad() == idRest) {
                enc = true;
            }
            i++;
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
        boolean enc = false;
        int i = 0;
        while (!enc && i < lista.size()) {
            if (idRest == lista.get(i).getIdActividad()) {
                enc = true;
                punt = lista.get(i).getPuntuacion();

            }
            i++;
        }
        return punt;
    }

}
