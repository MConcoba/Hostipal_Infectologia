
package org.mconcoba.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.mconcoba.bean.Medico;
import org.mconcoba.bean.TelefonoMedico;
import org.mconcoba.db.Conexion;
import org.mconcoba.sistema.Principal;


public class TelefonoMedicoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoMedico> listaTelefonoMedico;
    private ObservableList<Medico> listaMedico;
    
    
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private TableView tblTelefonoMedicos;
    @FXML private TableColumn colCodigoTelefono;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private TableColumn colCodigoMedico;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       cmbCodigoMedico.setItems(getMedicos());
    }
    
    
    public void guardar(){
        TelefonoMedico registro = new TelefonoMedico();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoMedico(?, ?, ?)}");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getCodigoMedico());
            procedimiento.execute();
            listaTelefonoMedico.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public ObservableList<Medico> getMedicos(){
            ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medico(resultado.getInt("codigoMedico"),
                                    resultado.getInt("licenciaMedica"),
                                    resultado.getString("nombres"),
                                    resultado.getString("apellidos"),
                                    resultado.getString("horaEntrada"),
                                    resultado.getString("horaSalida"),
                                    resultado.getInt("turnoMaximo"),
                                    resultado.getString("sexo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedico = FXCollections.observableList(lista);
    }
    
    
    public ObservableList<TelefonoMedico> getTelefonoMedicos(){
        ArrayList<TelefonoMedico> lista = new ArrayList<TelefonoMedico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonoMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),
                                            resultado.getString("telefonoPersonal"),
                                            resultado.getString("telefonoTrabajo"),
                                            resultado.getInt("codigoMedico")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTelefonoMedico = FXCollections.observableList(lista);
    }
    
    
    public void cargarDatos(){
        tblTelefonoMedicos.setItems(getTelefonoMedicos());
        colCodigoTelefono.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoTelefonoMedico"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoPersonal"));
        colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoTrabajo"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoMedico"));
    }
    
    
    public void seleccionarElementos(){
        if(tblTelefonoMedicos.getSelectionModel().getSelectedItem() != null){
            txtTelefonoPersonal.setText(((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
            txtTelefonoTrabajo.setText(((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
            cmbCodigoMedico.getSelectionModel().select(buscarMedico(((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        }else{
            tblTelefonoMedicos.getSelectionModel().clearSelection();
        }
    }
    
    public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblTelefonoMedicos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblTelefonoMedicos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblTelefonoMedicos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
        }
    }
    
    
    public Medico buscarMedico(int codigoMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medico( registro.getInt("codigoMedico"),
                                        registro.getInt("licenciaMedica"),
                                        registro.getString("apellidos"),
                                        registro.getString("nombres"),
                                        registro.getString("horaEntrada"),
                                        registro.getString("horaSalida"),
                                        registro.getInt("turnoMaximo"),
                                        registro.getString("sexo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;   
    }
    
    
    public TelefonoMedico buscarTelefonoMedico(int codigoTelefonoMedico){
        TelefonoMedico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoMedico(?)}");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TelefonoMedico (registro.getInt("codigoTelefonoMedico"),
                                                registro.getString("telefonoPersonal"),
                                                registro.getString("telefonoTrabajo"),
                                                registro.getInt("codigoMedico"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTelefonoMedico(?, ?, ?, ?)}");
            TelefonoMedico registro = (TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem();
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
            registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch (tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                tblTelefonoMedicos.getSelectionModel().clearSelection();
                cmbCodigoMedico.setDisable(true);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTelefonoMedicos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbCodigoMedico.setDisable(true); 
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                if(txtTelefonoTrabajo.getText().length() == 0){
                    txtTelefonoTrabajo.setText("--------");
                }if(txtTelefonoPersonal.getText().length() == 8 && txtTelefonoTrabajo.getText().length() == 8 && !cmbCodigoMedico.getSelectionModel().isEmpty()){
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                    tblTelefonoMedicos.getSelectionModel().clearSelection();
                    cmbCodigoMedico.setDisable(true); 
                    tipoDeOperacion = operaciones.NINGUNO;
                }else{
                    JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }
                break;
        }
    }
    

    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                tblTelefonoMedicos.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cmbCodigoMedico.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblTelefonoMedicos.getSelectionModel().getSelectedItem() != null){
                    int respueta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el regisro?", "Eliminar Telefono Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respueta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoMedico(?)}");
                            procedimiento.setInt(1, ((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                            procedimiento.execute();
                            listaTelefonoMedico.remove(tblTelefonoMedicos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblTelefonoMedicos.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblTelefonoMedicos.getSelectionModel().clearSelection();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                tblTelefonoMedicos.getSelectionModel().clearSelection();
                cmbCodigoMedico.setDisable(false);      
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtTelefonoTrabajo.getText().length() == 0){
                    txtTelefonoTrabajo.setText("--------");
                }if(txtTelefonoPersonal.getText().length() == 8 && txtTelefonoTrabajo.getText().length() == 8 && !cmbCodigoMedico.getSelectionModel().isEmpty()){
                    guardar();
                    desactivarControles(); 
                    limpiarControles();
                    tblTelefonoMedicos.getSelectionModel().clearSelection();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    cargarDatos();
                    cmbCodigoMedico.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                }else{
                    JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }
                break;
        }
    }
    

    public void desactivarControles(){
        txtTelefonoPersonal.setEditable(false);
        txtTelefonoTrabajo.setEditable(false);
        tblTelefonoMedicos.setDisable(false);
    }
    
    
    public void activarControles(){
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);
        tblTelefonoMedicos.setDisable(true);
    }
    
    
    public void limpiarControles(){
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
        cmbCodigoMedico.getSelectionModel().clearSelection();
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
    
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }


    public void soloNumerosP(KeyEvent keyEvent) {
        try{
            char teclado = keyEvent.getCharacter().charAt(0);
            
            if (!Character.isDigit(teclado) ||  txtTelefonoPersonal.getText().length() == 8)
                keyEvent.consume();
        }catch (Exception e){ 
            e.printStackTrace();
        }
    }
    
    public void soloNumerosT(KeyEvent keyEvent) {
        try{
            char teclado = keyEvent.getCharacter().charAt(0);
            
            if (!Character.isDigit(teclado) ||  txtTelefonoTrabajo.getText().length() == 8)
                keyEvent.consume();
        }catch (Exception e){ 
            e.printStackTrace();
        }
    }
    
}
