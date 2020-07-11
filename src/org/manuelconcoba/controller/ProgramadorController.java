
package org.manuelconcoba.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;
import org.manuelconcoba.db.Conexion;
import org.manuelconcoba.sistema.Principal;


public class ProgramadorController implements Initializable{
    private Principal escenarioPrincipal;


    public void borrarTodo(){
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de Eliminar la base de Datos", "Eliminar todo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(respuesta == JOptionPane.YES_OPTION){
            try{
                PreparedStatement drop = Conexion.getInstancia().getConexion().prepareCall("call sp_BorrarTodo");
                drop.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
      
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        

    }
    
}
