/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package not3CRUD;

import Controlador.ControladorVistaPrincipal;
import Modelo.MetodosPrincipal;
import Vistas.VistaPrincipal;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author davidf
 */
public class not3CRUD {

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args) {
        
            // TODO code application logic here
        VistaPrincipal vistaPrincipal = new VistaPrincipal();
        MetodosPrincipal metodosPrincipal = new MetodosPrincipal();
        ControladorVistaPrincipal controladorPrincipal;
        
   //DEFINIENDO CON QUÉ CONSTRUCTOR SE ABRIRÁ NUESTRO PROGRAMA, DEPENDIENDO DE SI RECIBE UN ARCHIVO O NO     
        //Habilitando poder abrir directamente el programa con un archivo
        if(args.length>0 ){//INICIANDO CON ARCHIVO
             controladorPrincipal = new ControladorVistaPrincipal(vistaPrincipal, metodosPrincipal,args[0]);
        
         
            
        }else{//INICIANDOSE SIN ARCHIVO
             controladorPrincipal = new ControladorVistaPrincipal(vistaPrincipal, metodosPrincipal);
         
          
        }
       
        // Display the addition of the two numbers
    //    int int1 = Integer.parseInt(args[0]);
        
       
    
       

        //En el controladorVistaPrincipal, se encuentra un método que controlará la salida de nuestro progrma
        // guardará un objeto de PanelPestaña que recuperará más tarde cuanmdo el programa sea abierto
        //Controlará si los archivos se encuentran guardados, etc.
    }//Fin del bloque Main

}//Fin de la clase principal
