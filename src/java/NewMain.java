
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
        
        Correlacion c=new Correlacion();
//        System.out.println("1,2"+c.correlacion(1, 2));
//        System.out.println("1,3"+c.correlacion(1, 3));
//        System.out.println("1,4"+c.correlacion(1, 4));
//        System.out.println("1,5"+c.correlacion(1, 5));
//        
//        System.out.println("2,3"+c.correlacion(2, 3));
//        System.out.println("2,4 "+c.correlacion(4, 2));
//        System.out.println("4,2 "+c.correlacion(2, 4));
//        System.out.println(Math.pow(0, 0.5));
//        System.out.println("0/0"+(0/Math.pow(0, 0.5)));
//        System.out.println("2,5"+c.correlacion(2, 5));
//        
//        System.out.println("3,4"+c.correlacion(3, 4));
//        System.out.println("3,5"+c.correlacion(3, 5));
//        
//        System.out.println("4,5"+c.correlacion(4, 5));
        Prediccion p=new Prediccion();
        p.getActividadRecomendada(4,"rogger.aburto@gmail.com");
//        p.idCoincidentes(2);
        /*UsuarioxActividad uxa=new UsuarioxActividad();
         System.out.println(uxa.getNombreApellido("rogger.aburto@gmail.com"));*/
//        String gusto="{\"gustos\":[\"basket\",\"adultos\"]}";
//        String correo="rogger.aburto@gmail.com";
        Usuarios u=new Usuarios();
//        u.gustos(gusto,correo);
//        System.out.println(u.validarUsuario("claudia@gmail.com"));
        
    }

}
