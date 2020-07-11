
package org.manuelconcoba.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.manuelconcoba.sistema.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    



    
    @FXML private GridPane grpModulos;
    @FXML private GridPane grpReportes;
    @FXML private GridPane grpAcerca;
    @FXML private GridPane grpOpciones;
    @FXML private Button btnAdelante;
    @FXML private Button btnAtras;
    @FXML private ImageView imageView;


     @Override
    public void initialize(URL location, ResourceBundle resources) {

        
    }   
 
    
    public void adelante(){
        btnAdelante.setVisible(false);
        btnAdelante.setOpacity(1.00);
        btnAtras.setVisible(true);
        grpOpciones.setVisible(true);
        grpModulos.setVisible(false);
        grpReportes.setVisible(false);
        grpAcerca.setVisible(false);
        imageView.setOpacity(1);
    }
    
    public void atras(){
        btnAdelante.setVisible(true);
        btnAdelante.setOpacity(0.5);
        btnAtras.setVisible(false);
        grpOpciones.setVisible(false);
    }
    
    public void modulos(){
        grpModulos.setVisible(true);
        grpReportes.setVisible(false);
        grpAcerca.setVisible(false);
        imageView.setOpacity(0.5);
//        grpHora.setOpacity(0.5);
    }

    public void reportes(){
        grpReportes.setVisible(true);
        grpModulos.setVisible(false);
        grpAcerca.setVisible(false);
        imageView.setOpacity(0.5);
//        grpHora.setOpacity(0.5);
    }
    
    public void acercaDe(){
        grpAcerca.setVisible(true);
        grpModulos.setVisible(false);
        grpReportes.setVisible(false);
        imageView.setOpacity(0.5);
//        grpHora.setOpacity(0.5);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    public void ventanaMedico(){
        escenarioPrincipal.ventanaMedico();
    }
    
    
    public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
    
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    
    public void ventanaArea(){
        escenarioPrincipal.ventanaArea();
    }
    
    public void ventanaCargo(){
        escenarioPrincipal.ventanaCargo();
    }
    
    
    public void ventanaTelefenoMedico(){
        escenarioPrincipal.ventanaTelefonoMedico();
    }
    
    
    public void ventanaContactoUrgencia(){
        escenarioPrincipal.ventanaContactoUrgencia();
    }
    
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    
    public void ventanaHorario(){
        escenarioPrincipal.ventanaHorario();
    }
    
    
    public void ventanaMedicoEspecialidad(){
        escenarioPrincipal.ventanaMedicoEspecialidad();
    }
    
    
    public void ventanaTurno(){
        escenarioPrincipal.ventanaTurno();
    }
    
    public void ventanaResponsableTurno(){
        escenarioPrincipal.ventanaResponsableTurno();
    }


   
    
    
   

}
