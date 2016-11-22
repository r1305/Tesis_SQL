/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Julian
 */
public class Conexion {
    String url = "jdbc:mysql://us-cdbr-iron-east-04.cleardb.net:3306/heroku_74736d96aaaaaed?user=bf5984dcefc6a5&password=77b6157c";
    //url de conexion a la máquina de servidor dedicado
    //usuario: tesis_bd tiene todos los permisos necesarios para acceder desde otro host
    //192.168.118.130: host de la máquina servidor de mysql
    
    String url2="jdbc:mysql://54.227.36.192:3306/tesis?user=root&password=root";
    String url3="jdbc:mysql://localhost:3306/tesis?user=root&password=root";
    

    public Connection getConexion() {
        java.sql.Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            try {
                con = DriverManager.getConnection(url);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }

}
