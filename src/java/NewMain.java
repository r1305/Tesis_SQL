
import tesis.dao.Correlacion;
import tesis.dao.Prediccion;
import tesis.dao.Usuarios;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Julian
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here       
        Prediccion p=new Prediccion();
        System.out.println("usuario: rogger.aburto@gmail.com");
        p.getActividadRecomendada(62,"rogger.aburto@gmail.com");
        System.out.println("");
        System.out.println("");
        System.out.println("usuario: usuario@gmail.com");     
        p.getActividadRecomendada(72,"usuario@gmail.com");
        
    }

}
