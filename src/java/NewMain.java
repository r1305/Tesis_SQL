
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tesis.dao.Correlacion;
import tesis.dao.Prediccion;
import tesis.dto.UsuarioxActividad;

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
        
        Prediccion pre=new Prediccion();
        pre.getActividadRecomendada(4);
        
        /*UsuarioxActividad uxa=new UsuarioxActividad();
        System.out.println(uxa.getNombreApellido("rogger.aburto@gmail.com"));*/
        
        
        
    }
    
}
