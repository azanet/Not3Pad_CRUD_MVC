/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.MetodosBBDD_Consultas;
import Modelo.objetoTablaDescripcion;
import Vistas.PanelMenuBar;
import Vistas.VistaBBDD_Modificar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.util.HashMap;
import java.util.TreeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author davidf
 */
public class ControladorBBDD_Modificar {
    
    private final VistaBBDD_Modificar vista; 
    private final MetodosBBDD_Consultas metodos;
    
    
//DefiniendoTreeSet de objetosConsulta, que recibiremos de la BBDD (se ordenarán por ID) ya que el objeto implementa la clase comparable
    TreeSet<objetoTablaDescripcion> ListaObjetosDescripcion = new TreeSet<>();
    HashMap<Integer, Integer> MapaSeleccionarDescripcion = new HashMap<>();

    private String[] grupos;
    private String[] articulos;
    
    public int grupoSeleccionado = 0;
    public int articuloSeleccionado = 0;
    

    public ControladorBBDD_Modificar(VistaBBDD_Modificar vista, MetodosBBDD_Consultas metodos) {
        
        this.vista = vista;
        this.metodos = metodos;
        
  Iniciar();
            
    }


   
    
    
    private void Iniciar(){
        
      //JPOPUPMENU Agregamos a los botones el JPopupMenu su correspondiente Listener
            //JPOPUPMENU Agregamos a los botones el JPopupMenu su correspondiente Listener
        vista.pop_Rehacer.addActionListener(new OyentePopRehacer());
        vista.pop_Deshacer.addActionListener(new OyentePopDeshacer());
        vista.pop_Copiar.addActionListener(new OyentePopCopiar());
        vista.pop_Cortar.addActionListener(new OyentePopCortar());
        vista.pop_Pegar.addActionListener(new OyentePopPegar());
        vista.pop_Imprimir.addActionListener(new OyentePopImprimir());
        vista.comboGrupos_DUO.addActionListener(new OyenteComboGrupos_DUO());
        vista.comboArticulos_DUO.addActionListener(new OyenteComboArticulos_DUO());
        vista.comboElegirDescripcion.addActionListener(new OyenteComboElegirDescripcion());

       //Abajo del codigo se agfrega un BOTONEXPORTACONSULTAc que esta declarado como estático en la vista proncipal, para poder recuiperar el textArea
      vista.BotonModificarDescripcion.addActionListener(new OyenteModificarDescripcion());
       vista.BotonObtenerDescripcion.addActionListener(new OyenteObtenerDescripcion());
      vista.BotonlimpiarPantalla.addActionListener(new OyenteLimpiarPantalla());
        vista.Botonsalir.addActionListener(new OyenteSalir());

      

        //Cargamos un elemento en el COMBOBOX grupo, y el LISTENER se encargará de lanzar el resto de metodos necesarios para ir cargando los comboboxes en orden para evitar errores 
        vista.comboGrupos_DUO.addItem("============>");
        vista.comboArticulos_DUO.addItem("============>");
        vista.comboElegirDescripcion.addItem("============>");
 
    }//Fin del metodo INICIAR
    
    
    
    private void BorrarPantalla(){
            vista.textArea.setText("");
            vista.comboArticulos_DUO.setSelectedIndex(0);
            vista.comboGrupos_DUO.setSelectedIndex(0);
            vista.comboElegirDescripcion.setSelectedIndex(0);

    }
    
    
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    
    //Este metodo será utilizado Para Refrescar los comboboxes de GRUPOS y ARTICULOS
    private void RefrescarCombobox_DUO() {
        
        //Actualizaremos la lista de grupos (haremos peticion a BBDD) SOLO si el GRUPOSELECCIONADO ES 0
        if (grupoSeleccionado == 0) {
            //Ejecutamos la CONSULTA "LecturaGrupos", la cual nos devolverá un array con los nombre de los grupos existentes
             grupos = metodos.LecturaGrupos();
             
            vista.comboGrupos_DUO.removeAllItems();//Removemos Items que existan en el combobox
            //Y con esto inicializamos o actualizamos nustras combobox de Grupos
            for (String grupo : grupos) {
                vista.comboGrupos_DUO.addItem(grupo);
            }//Fin del FOR Grupos
        //Revisaremos se hay alguna opcion elegida en GRUPO, si no la hay, no cargamos el combo de articulos
        
        }
        
        //Cargamos Articulos con los nombre recibidos de la BBDD
        articulos = metodos.LecturaArticulos((String) vista.comboGrupos_DUO.getSelectedItem());
        vista.comboArticulos_DUO.removeAllItems();
        for (String articulo : articulos) {
            vista.comboArticulos_DUO.addItem(articulo);
        }//Fin del FOR Grupos
              
    }//Fin de REFRESCAR ComboboxesDUO
    
    

////////ESTE METODO ES PARA REFRESCAR EL COMBOBOX DE DESCRIPCION /////////////////////////
    //para cada Descripcion agregada, almacenaremos el ID de esta en un HASHMAP como VALUE
    // y como KEY se utilizará el metodo ".getItemCount()" Para saber en qué posición del comboBox se ha agregado
    //Luego utilizaremos el metodo "getSelectedIndex()+1" para pasarselo a nuestro Hashmap cuando el usuario quiera
    //interactuar con las descripciones, ya sea para enviar la modificación o para obtener la descripcion
    //ya que al obtener el ID realizaremos la peticion a la BBDD con esta, para hacelo todo mas sencillo
    private void RefrescarComboboxDescripcion() {
             
        if(vista.comboArticulos_DUO.getSelectedIndex()>0) {  
                   
         ListaObjetosDescripcion =metodos.LecturaDescripcion_DUO(vista.comboGrupos_DUO.getSelectedItem().toString(),vista.comboArticulos_DUO.getSelectedItem().toString()); //Poner aqui consulta con grupos y articulos 
        MapaSeleccionarDescripcion= new HashMap<>();
         //Hacemos que se ordene el TreeSet con el comparador que trae el objeto (Que ordenará según el ID)
         ListaObjetosDescripcion.comparator();
          vista.comboElegirDescripcion.removeAllItems();
          vista.comboElegirDescripcion.addItem("============>");
            for (objetoTablaDescripcion obj : ListaObjetosDescripcion) {
                
                vista.comboElegirDescripcion.addItem("ID["+obj.getID()+"]: "+obj.getDescripcion());//Poner aqui consulta con  articulos
                MapaSeleccionarDescripcion.put(vista.comboElegirDescripcion.getItemCount(), obj.getID());
//Comprobar  VALOR de contador de ITEMS    
//            System.out.println(vista.comboElegirDescripcion.getItemCount());
                }
        }else{
                ListaObjetosDescripcion= new TreeSet<>();
                MapaSeleccionarDescripcion= new HashMap<>();
                vista.comboElegirDescripcion.removeAllItems();
                vista.comboElegirDescripcion.addItem("============>");
        
        }
            
     }//Fin de REFRESCAR ComboboxeDESCRIPCION
    
//==============================================================================  

    
//==============================================================================      
//Este metodo será utilizado para saber si DESBLOQUEAR O NO las opciones siguientes para la consulta
//ACCIONES NECESARIAS PARA IR DESBLOQUEANDO LOS SIGUIENTES COMBOBOXES
//PARA DE ESTA FORMA, OBLIGAR AL USUARIO A QUE HAGA LA CONSULTA CORRECTAMENTE
   
    private void DesbloqueoDeOpcionesGrupo_DUO() {

        if (grupoSeleccionado > 0) {
            //DESBLOQEUAR CAMPO DE ARTICULO
            vista.tituloArticulo_DUO.setEnabled(true);
            vista.comboArticulos_DUO.setEnabled(true);
   
        } else {

            vista.tituloArticulo_DUO.setEnabled(false);
            vista.comboArticulos_DUO.setEnabled(false);
            vista.tituloElegirDescripcion.setEnabled(false);
            vista.comboElegirDescripcion.setEnabled(false);
            vista.tituloDescripcion.setEnabled(false);
            vista.BotonModificarDescripcion.setEnabled(false);
            vista.textArea.setEnabled(false);

        }//Fin del else

    }//Fin de desbloqueDeOpcionesGrupo_DUO

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       
    private void DesbloqueoDeOpcionesArticulo_DUO() {

        if (articuloSeleccionado > 0) {
            //DESBLOQEUAR CAMPO DE DESCRIPCION
            vista.tituloElegirDescripcion.setEnabled(true);
            vista.comboElegirDescripcion.setEnabled(true);
            vista.tituloDescripcion.setEnabled(true);
            vista.BotonModificarDescripcion.setEnabled(true);
            vista.textArea.setEnabled(true);

         
         } else if (articuloSeleccionado == 0 && grupoSeleccionado == 0) {
            //BLOQEUAR CAMPO DE DESCRIPCION
            vista.tituloElegirDescripcion.setEnabled(false);
            vista.comboElegirDescripcion.setEnabled(false);
            vista.tituloDescripcion.setEnabled(false);
            vista.BotonModificarDescripcion.setEnabled(false);
            vista.textArea.setEnabled(false);
    
        }else {
            //BLOQEUAR CAMPO DE DESCRIPCION
            vista.tituloElegirDescripcion.setEnabled(false);
            vista.comboElegirDescripcion.setEnabled(false);
             vista.tituloDescripcion.setEnabled(false);
            vista.BotonModificarDescripcion.setEnabled(false);
            vista.textArea.setEnabled(false);//Fin del else

         }
    }//FIN DE DESBLOQUEO-OPCIONES_ARTUCULO_DUO
    
//==============================================================================   

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
///////////////////////CREANDO OYENTES DE COMBOBOX////////////////////////////// 
// En la ejecucion del codigo, estos oyentes se iran ejecutando de 
//forma secuencial si es llamado el anterior, para mantener los comboboxes correctamente actualizados//
////////////////////////////////////////////////////////////////////////////////
//==============================================================================
//==============================================================================
    class OyenteComboGrupos_DUO implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            //Recogemos el INDICE donde se encuentra, para luego poder volver a recuperarlo
            grupoSeleccionado = vista.comboGrupos_DUO.getSelectedIndex();
            vista.textArea.setText("");
            DesbloqueoDeOpcionesArticulo_DUO();
            DesbloqueoDeOpcionesGrupo_DUO();
      
            RefrescarCombobox_DUO();
        }//Fin de la accion sobreescrita

    }//Fin del oyenteInsertarDescripcion   

//==============================================================================
//==============================================================================
    class OyenteComboArticulos_DUO implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
             
            articuloSeleccionado = vista.comboArticulos_DUO.getSelectedIndex();
            vista.textArea.setText("");
            DesbloqueoDeOpcionesArticulo_DUO();
            
            RefrescarComboboxDescripcion();

        }//Fin de la accion sobreescrita

    }//Fin del oyenteInsertarDescripcion   
    
//==============================================================================
//==============================================================================
   class OyenteComboElegirDescripcion implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            if(vista.comboElegirDescripcion.getSelectedIndex()>0){
                
                vista.BotonModificarDescripcion.setEnabled(true);
                vista.BotonObtenerDescripcion.setEnabled(true);
            }else{
                vista.BotonModificarDescripcion.setEnabled(false);
                vista.BotonObtenerDescripcion.setEnabled(false);
            }
        }
    }//Find el oyente ComboGRUPOS_SOLO
            

//==============================================================================
//==============================================================================
////////////////////////////////////////////////////////////////////////////////    
//////////////////Declarando OYENTES DE LOS BOTONES PARA REALIZAR ACCIONES /////
     class OyenteModificarDescripcion implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
                
           
        //////INSERTAR AQUI LAS CONSULTAS PARA ENVIAR LA MODIFICACION
        //Y LOS METODOS
            if(vista.textArea.getText().trim().equals("")){
                JOptionPane.showMessageDialog(vista.BotonModificarDescripcion, "La consulta a enviar NO puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
     
            }else{
                //ENVIANDO DESCRIPCION MODIFICADA
                if(metodos.ModificarDescripcion(MapaSeleccionarDescripcion.get(vista.comboElegirDescripcion.getSelectedIndex()+1), vista.textArea.getText())){
                    JOptionPane.showMessageDialog(vista.BotonModificarDescripcion, "La Descripción se Modificó SATISFACTORIAMENTE", "MODIFICACIÓN EXITOSA", JOptionPane.INFORMATION_MESSAGE);
                }else{
                     JOptionPane.showMessageDialog(vista.BotonModificarDescripcion, "No se realizó la modificación", "Error de CONSULTA", JOptionPane.ERROR_MESSAGE);
                }
              //  System.out.println(MapaSeleccionarDescripcion.get(vista.comboElegirDescripcion.getSelectedIndex()+1));
              BorrarPantalla();
            }
                
        }//Fin de la accion sobreescrita

    }//Fin del oyenteInsertarDescripcion  

     //==============================================================================
//==============================================================================
////////////////////////////////////////////////////////////////////////////////    
//////////////////Declarando OYENTES DE LOS BOTONES PARA REALIZAR ACCIONES /////
     class OyenteObtenerDescripcion implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
                
     
            vista.textArea.setText("");
            //Lanzamos la consulta para recoger la descripcion seleccionada
            String ObtenerDescripcion=metodos.ObtenerDescripcion(MapaSeleccionarDescripcion.get(vista.comboElegirDescripcion.getSelectedIndex()+1));
                   
            vista.textArea.append(ObtenerDescripcion);
            
        }//Fin de la accion sobreescrita

    }//Fin del oyenteObtenerDescripcion  
     
//==============================================================================
//==============================================================================
    class OyenteLimpiarPantalla implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            BorrarPantalla();

        }//Fin de la accion sobreescrita

    }//Fin del oyenteInsertarDescripcion  

//==============================================================================
//==============================================================================
    class OyenteSalir implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            //Hacemos que se cierre la ventana, SIN DETENER LA EJECUCIÓN DEL PROGRAMA
            vista.dispose();
            //Deseleccionamos el CheckBox correspondiente del MENU-BAR
            PanelMenuBar.modificar.setSelected(false);

        }//Fin de la accion sobreescrita

    }//Fin del oyenteInsertarDescripcion   

//==============================================================================
//============================================================================== 
////////////////////////////////////////////////////////////////////////////////    
///////////////////  POP-UP MENU  //////////////////////////////////////////////
////////////CONFIGURARÉ LOS OYENTES DEL JPOPUPMENU EN ESTA MISMA CLASE//////////   
    ////////////////////////////////////////////////////////////////////////////////   
    class OyentePopRehacer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //ESCRIBIR CÓDIGO DEL BOTÓN AQUÍ
            if (vista.manager.canRedo()) {
                vista.manager.redo();
            }
        }//Fin action performed
    }//Fin del OyenteCOPIAR

////////////////////////////////////////////////////////////////////////////////   
    class OyentePopDeshacer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //ESCRIBIR CÓDIGO DEL BOTÓN AQUÍ
            if (vista.manager.canUndo()) {
                vista.manager.undo();
            }

        }//Fin action performed
    }//Fin del OyenteCOPIAR

    class OyentePopCopiar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //ESCRIBIR CÓDIGO DEL BOTÓN AQUÍ

            vista.textArea.copy();
        }//Fin action performed
    }//Fin del OyenteCOPIAR

////////////////////////////////////////////////////////////////////////////////   
    class OyentePopCortar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //ESCRIBIR CÓDIGO DEL BOTÓN AQUÍ

            vista.textArea.cut();
        }//Fin action performed
    }//Fin del OyenteCOPIAR

////////////////////////////////////////////////////////////////////////////////   
    class OyentePopPegar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //ESCRIBIR CÓDIGO DEL BOTÓN AQUÍ
            vista.textArea.paste();
        }//Fin action performed
    }//Fin del OyenteCOPIAR

    ////////////////////////////////////////////////////////////////////////////////   
    class OyentePopImprimir implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                //ESCRIBIR CÓDIGO DEL BOTÓN AQUÍ
                vista.textArea.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, "Se produjo un error de impresión", "Error de Impresión", JOptionPane.ERROR_MESSAGE);
            }
        }//Fin action performed
    }//Fin del OyenteCOPIAR


    
    
    
    
    
    
}//Fin de la clase CONTROLADOR-INSERTAR
