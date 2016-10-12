/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tesis.dao.Conexion;
import tesis.dao.Usuarios;

/**
 *
 * @author Julian
 */
public class Update extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String correo=request.getParameter("correo");
        String url=request.getParameter("url");
        String nombre=request.getParameter("name");
        int edad=Integer.parseInt(request.getParameter("edad"));
        
        int id=getId(correo);
                
        Usuarios u=new Usuarios();
        //boolean ok=u.updateFoto(id, url);
        u.updatePerfil(id, nombre,edad, url);
        String json="";
        //json=new Gson().toJson(ok);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("ok");
    }
    
    public int getId(String correo) {
        Conexion c = new Conexion();
        int id=0;
        try{
            Connection con=c.getConexion();
            String strsql = "SELECT * FROM usuarios where correo='"+correo+"'";
            PreparedStatement pstm = con.prepareStatement(strsql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                id=rs.getInt("id");
            }            
            rs.close();
            pstm.close();
            con.close();
            
        }catch(Exception e){
            
            System.out.println(e);
            id=0;
        }
        return id;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
