
package org.mconcoba.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.mconcoba.bean.Horario;
import org.mconcoba.db.Conexion;
import org.mconcoba.sistema.Principal;


public class HorarioController implements Initializable{
    private enum  operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Horario> listaHorario;


    
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private CheckBox cbLunes;
    @FXML private CheckBox cbMartes;
    @FXML private CheckBox cbMiercoles;
    @FXML private CheckBox cbJueves;
    @FXML private CheckBox cbViernes;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes; 
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ComboBox cmbHoraE;
    @FXML private ComboBox cmbHoraS;
    @FXML private ComboBox cmbMinutoE;
    @FXML private ComboBox cmbMinutoS;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbHoraE.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");
        cmbMinutoE.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60");
        cmbHoraS.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08" ,"09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");
        cmbMinutoS.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60");
    }
    
    
    public void guardar(){
        Horario registro = new Horario();
        registro.setHoraEntrada(Time.valueOf(cmbHoraE.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoE.getSelectionModel().getSelectedItem().toString() + ":00"));
        registro.setHoraSalida(Time.valueOf(cmbHoraS.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoS.getSelectionModel().getSelectedItem().toString() + ":00"));
//        registro.setHoraEntrada(Date.valueOf(cmbHoraE.getSelectionModel().getSelectedItem().toString()));
        registro.setLunes(cbLunes.isSelected());
        registro.setMartes(cbMartes.isSelected());
        registro.setMiercoles(cbMiercoles.isSelected());
        registro.setJueves(cbJueves.isSelected());
        registro.setViernes(cbViernes.isSelected());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorario(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getHoraEntrada().toString());
            procedimiento.setString(2, registro.getHoraSalida().toString());
            procedimiento.setBoolean(3, registro.isLunes());
            procedimiento.setBoolean(4, registro.isMartes());
            procedimiento.setBoolean(5, registro.isMiercoles());
            procedimiento.setBoolean(6, registro.isJueves());
            procedimiento.setBoolean(7, registro.isViernes());
            procedimiento.execute();
            listaHorario.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public ObservableList<Horario> getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horario (resultado.getInt("codigoHorario"),
                                        resultado.getTime("horaEntrada"),
                                        resultado.getTime("horaSalida"),
                                        resultado.getBoolean("lunes"),
                                        resultado.getBoolean("martes"),
                                        resultado.getBoolean("miercoles"),
                                        resultado.getBoolean("jueves"),
                                        resultado.getBoolean("viernes")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaHorario = FXCollections.observableList(lista);
    }
    
    
    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Horario, Date>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Horario, Date>("horaSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("viernes"));
    }
    
    
    public void selecionarElementos(){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
//            txtHoraEntrada.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHoraEntrada());
//            txtHoraSalida.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHoraSalida());
            cmbHoraE.getSelectionModel().select(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHoraEntrada().toString().substring(0, 2));
            cmbMinutoE.getSelectionModel().select(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHoraEntrada().toString().substring(3, 5));
            cmbHoraS.getSelectionModel().select(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHoraSalida().toString().substring(0, 2));
            cmbMinutoS.getSelectionModel().select(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHoraSalida().toString().substring(3, 5));
            cbLunes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            cbMartes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            cbMiercoles.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            cbJueves.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            cbViernes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
        }else {
            tblHorarios.getSelectionModel().clearSelection();
        }
    }
    
    
    public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblHorarios.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblHorarios.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblHorarios.getSelectionModel().clearSelection();
                limpiarControles();
                break;
        }
    }
    
    
    public Horario buscarHorario(int codigoHorario){
        Horario resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorario(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horario(registro.getInt("codigoHorario"),
                                        registro.getTime("horaEntrada"),
                                        registro.getTime("horaSalida"),
                                        registro.getBoolean("lunes"),
                                        registro.getBoolean("martes"),
                                        registro.getBoolean("miercoles"),
                                        registro.getBoolean("jueves"),
                                        registro.getBoolean("viernes"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarHorario(?, ?, ?, ?, ?, ?, ?, ?)}");
            Horario registro = (Horario)tblHorarios.getSelectionModel().getSelectedItem();     
            registro.setHoraEntrada(Time.valueOf(cmbHoraE.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoE.getSelectionModel().getSelectedItem().toString() + ":00"));
            registro.setHoraSalida(Time.valueOf(cmbHoraS.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoS.getSelectionModel().getSelectedItem().toString() + ":00"));
            registro.setLunes(cbLunes.isSelected());
            registro.setMartes(cbMartes.isSelected());
            registro.setMiercoles(cbMiercoles.isSelected());
            registro.setJueves(cbJueves.isSelected());
            registro.setViernes(cbViernes.isSelected());
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHoraEntrada().toString());
            procedimiento.setString(3, registro.getHoraSalida().toString());
            procedimiento.setBoolean(4, registro.isLunes());
            procedimiento.setBoolean(5, registro.isMartes());
            procedimiento.setBoolean(6, registro.isMiercoles());
            procedimiento.setBoolean(7, registro.isJueves());
            procedimiento.setBoolean(8, registro.isViernes());
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
                tblHorarios.getSelectionModel().clearSelection();
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                    break;
            case ACTUALIZAR:
                if(cmbHoraE.getSelectionModel().isEmpty() || cmbMinutoE.getSelectionModel().isEmpty() || cmbHoraS.getSelectionModel().isEmpty() || cmbMinutoS.getSelectionModel().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }else{
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                    tblHorarios.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                   
                }
                break;
        }
    }
    

    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                tblHorarios.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar es registro?", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement  procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorario(?)}");
                            procedimiento.setInt(1, ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedimiento.execute();
                            listaHorario.remove(tblHorarios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblHorarios.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblHorarios.getSelectionModel().clearSelection();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccinar un elemento");
                }
        }
    }
        
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                tblHorarios.getSelectionModel().clearSelection();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(cmbHoraE.getSelectionModel().isEmpty() || cmbMinutoE.getSelectionModel().isEmpty() || cmbHoraS.getSelectionModel().isEmpty() || cmbMinutoS.getSelectionModel().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Datos Incompletos");
                    
                }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblHorarios.getSelectionModel().clearSelection();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                    break;
                
        }
    }
    


    public void desactivarControles(){
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
        cbLunes.setDisable(true);
        cbMartes.setDisable(true);
        cbMiercoles.setDisable(true);
        cbJueves.setDisable(true);
        cbViernes.setDisable(true);
        tblHorarios.setDisable(false);
        cmbHoraE.setDisable(true);
        cmbMinutoE.setDisable(true);
        cmbHoraS.setDisable(true);
        cmbMinutoS.setDisable(true);
    }
    
    public void activarControles(){
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
        cbLunes.setDisable(false);
        cbMartes.setDisable(false);
        cbMiercoles.setDisable(false);
        cbJueves.setDisable(false);
        cbViernes.setDisable(false);
        tblHorarios.setDisable(true);
        cmbHoraE.setDisable(false);
        cmbMinutoE.setDisable(false);
        cmbHoraS.setDisable(false);
        cmbMinutoS.setDisable(false);
    }
    
    public void limpiarControles(){
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
        cbLunes.setSelected(false);
        cbMartes.setSelected(false);
        cbMiercoles.setSelected(false);
        cbJueves.setSelected(false);
        cbViernes.setSelected(false);
        cmbHoraE.getSelectionModel().clearSelection();
        cmbMinutoE.getSelectionModel().clearSelection();
        cmbHoraS.getSelectionModel().clearSelection();
        cmbMinutoS.getSelectionModel().clearSelection();
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
    
    
}
