
package org.mconcoba.sistema;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mconcoba.controller.AreaController;
import org.mconcoba.controller.CargoController;
import org.mconcoba.controller.ContactoUrgenciaController;
import org.mconcoba.controller.EspecialidadController;
import org.mconcoba.controller.HorarioController;
import org.mconcoba.controller.MedicoController;
import org.mconcoba.controller.MedicoEspecialidadController;
import org.mconcoba.controller.MenuPrincipalController;
import org.mconcoba.controller.PacienteController;
import org.mconcoba.controller.ProgramadorController;
import org.mconcoba.controller.ResponsableTurnoController;
import org.mconcoba.controller.TelefonoMedicoController;
import org.mconcoba.controller.TurnoController;


public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/mconcoba/view/";
    private final String PAQUETE_CSS = "/org/mconcoba/resource/";
    
    private Stage escenarioPrincipal;
    public Stage acerca;
    private Scene escena;
    
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        // escenarioPrincipal.setResizable(false);
        escenarioPrincipal.setTitle("Hospital de Infectología");
        escenarioPrincipal.getIcons().add(new Image("/org/mconcoba/images/newAngel.png"));
        menuPrincipal();
        escenarioPrincipal.show();
    }
    

    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("NewView.fxml", 900, 650, "EscenarioPrincipal.css");
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    
    public void ventanaMedico(){
        try{
            MedicoController medicoController = (MedicoController) cambiarEscena("MedicoView.fxml", 1000, 650, "MedicoEstilo.css");
            medicoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidad(){
        try{
            EspecialidadController especialidadController = (EspecialidadController) cambiarEscena("EspecialidadesView.fxml", 600, 650, "MedicoEstilo.css");
            especialidadController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaArea(){
        try{
            AreaController areaController = (AreaController) cambiarEscena("AreaView.fxml", 600, 650, "MedicoEstilo.css");
            areaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCargo(){
        try{
            CargoController cargoController = (CargoController) cambiarEscena("CargoView.fxml", 600, 650, "MedicoEstilo.css");
            cargoController.setEsecenarioPrincipa(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTelefonoMedico(){
        try{
            TelefonoMedicoController telefonoMedicoController = (TelefonoMedicoController) cambiarEscena("TelefonoMedicoView.fxml", 790, 550, "MedicoEstilo.css");
            telefonoMedicoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaHorario(){
        try{
            HorarioController horarioController = (HorarioController) cambiarEscena("HorarioView.fxml", 830, 600, "MedicoEstilo.css");
            horarioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicoEspecialidad(){
        try{
            MedicoEspecialidadController medicoEspecialidadController = (MedicoEspecialidadController) cambiarEscena("MedicoEspecialidadView.fxml", 1000, 650, "MedicoEstilo.css");
            medicoEspecialidadController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPaciente(){
        try{
            PacienteController pacienteController = (PacienteController) cambiarEscena("PacienteView.fxml", 1000, 650, "MedicoEstilo.css");
            pacienteController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaContactoUrgencia(){
        try{
            ContactoUrgenciaController contactoUrgenciaController = (ContactoUrgenciaController) cambiarEscena("ContactoUrgenciaView.fxml", 1000, 600, "MedicoEstilo.css");
            contactoUrgenciaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaResponsableTurno(){
        try{
            ResponsableTurnoController responsableTurnoController = (ResponsableTurnoController) cambiarEscena("ResponsableTurnoView.fxml", 1000, 650, "MedicoEstilo.css");
            responsableTurnoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaTurno(){
        try{
            TurnoController turnoController = (TurnoController) cambiarEscena("TurnoView.fxml", 1000, 650, "MedicoEstilo.css");
            turnoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programadorController = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 650, 530, "MedicoEstilo.css");
            programadorController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        } 
    }

    
    
    public Initializable cambiarEscena(String fxml, int ancho, int  alto, String css) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escena.getStylesheets().add(Principal.class.getResource(PAQUETE_CSS + css).toExternalForm());
        escenarioPrincipal.sizeToScene();
        escenarioPrincipal.centerOnScreen();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
