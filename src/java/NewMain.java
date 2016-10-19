
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import tesis.dao.Prediccion;

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
        //Correlacion c=new Correlacion();
        //System.out.println(c.correlacion(3,4));
        //c.correlacion(3, 4);

        /*String o=uxa.getActividades();
         JSONParser p=new JSONParser();
         try{
         JSONObject ob=(JSONObject)p.parse(o);
         JSONArray a=(JSONArray)ob.get("act");
         for(int i=0;i<a.size();i++){
         System.out.println(a.get(i));
         }
         }catch(Exception e){
            
         }*/
//        Conexion c=new Conexion();
//        System.out.println(c.getConexion());
        Prediccion pre = new Prediccion();
        System.out.println(pre.getActividadRecomendada(2));
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(1);
        a.add(2);
        a.add(2);
        a.add(3);
        List<Integer> b = new ArrayList<>();
        b.add(1);
        b.add(2);

//        for (int j = 0; j < b.size(); j++) {
//            for (int i = 0; i < a.size(); i++) {
//                while (Objects.equals(a.get(i), b.get(j))) {
//                    if (Objects.equals(a.get(i), b.get(j))) {
//                        a.remove(i);
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < a.size(); i++) {
//            System.out.println(a.get(i));
//        }

//        pre.hallarPrediccion(3);
//        Correlacion c=new Correlacion();
//        System.out.println(c.correlacion(4, 1));
        /*UsuarioxActividad uxa=new UsuarioxActividad();
         System.out.println(uxa.getNombreApellido("rogger.aburto@gmail.com"));*/
    }

}
