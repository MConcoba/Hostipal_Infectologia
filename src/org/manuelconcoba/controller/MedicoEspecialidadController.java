
package org.manuelconcoba.controller;

import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.manuelconcoba.bean.Especialidad;
import org.manuelconcoba.bean.Horario;
import org.manuelconcoba.bean.Medico;
import org.manuelconcoba.bean.MedicoEspecialidad;
import org.manuelconcoba.db.Conexion;
import org.manuelconcoba.sistema.Principal;


public class MedicoEspecialidadController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<Medico> listaMedico;
    private ObservableList<Especialidad> listaEspecialidad;
    private ObservableList<Horario> listaHorario;
    
    
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private ComboBox cmbCodigoHorario;
    @FXML private TableView tblMedicoEspecialidad;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colEspecialidad;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colHoras;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedico.setItems(getMedicos());
        cmbCodigoEspecialidad.setItems(getEspecialidades());
        cmbCodigoHorario.setItems(getHorarios());
        
    }
    
    public void guardar(){
        MedicoEspecialidad registro = new MedicoEspecialidad();
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        registro.setCodigoHorario(((Horario)cmbCodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicoEspecialidad(?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getCodigoEspecialidad());
            procedimiento.setInt(3, registro.getCodigoHorario());
            procedimiento.execute();
            listaMedicoEspecialidad.add(registro);
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
                                    resultado.getString("horasalida"),
                                    resultado.getInt("turnoMaximo"),
                                    resultado.getString("sexo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedico = FXCollections.observableList(lista);
    }
    
    public ObservableList<Especialidad> getEspecialidades(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                    resultado.getString("nombreEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEspecialidad = FXCollections.observableList(lista);
    }
    
    public ObservableList<Horario> getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horario(resultado.getInt("codigoHorario"),
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
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicosEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                                                resultado.getInt("codigoMedico"),
                                                resultado.getInt("codigoEspecialidad"),
                                                resultado.getInt("codigoHorario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedicoEspecialidad = FXCollections.observableList(lista);
    }
    
    
    public void cargarDatos(){
        tblMedicoEspecialidad.setItems(getMedicoEspecialidad());
        colCodigo.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedicoEspecialidad"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedico"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoEspecialidad"));
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoHorario"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        
    }
    
    public void selecionarElementos(){
        if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
         cmbCodigoMedico.getSelectionModel().select(buscarMedico(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico()));
         cmbCodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
         cmbCodigoHorario.getSelectionModel().select(buscarHorario(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoHorario()));
        }else{
            tblMedicoEspecialidad.getSelectionModel().clearSelection();
        }
    }
    
    public void deleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblMedicoEspecialidad.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblMedicoEspecialidad.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblMedicoEspecialidad.getSelectionModel().clearSelection();
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
                resultado = new Medico(registro.getInt("codigoMedico"),
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
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidad(registro.getInt("codigoEspecialidad"),
                                            registro.getString("nombreEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
    
    public MedicoEspecialidad buscarMedicoEspecialidad(int codigoMedicoEspecialidad){
        MedicoEspecialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicoEspecialidad(?)}");
            procedimiento.setInt(1, codigoMedicoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new MedicoEspecialidad(registro.getInt("codigoMedicoEspecialidad"),
                                                    registro.getInt("codigoMedico"),
                                                    registro.getInt("codigoEspecialidad"),
                                                    registro.getInt("codigoHorario"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    
    public void reporte(){
        switch(tipoDeOperacion){
                case ACTUALIZAR:
                    limpiarControles();
                    tblMedicoEspecialidad.getSelectionModel().clearSelection();
                    cmbCodigoMedico.setDisable(true);
                    cmbCodigoEspecialidad.setDisable(true);
                    cmbCodigoHorario.setDisable(true);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
        }
    }
    
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbCodigoMedico.setDisable(true);
                    cmbCodigoEspecialidad.setDisable(true);
                    cmbCodigoHorario.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                    break;
            case ACTUALIZAR:
//                 actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                limpiarControles();
                btnEliminar.setDisable(true);
                cmbCodigoMedico.setDisable(true);
                cmbCodigoEspecialidad.setDisable(true);
                cmbCodigoHorario.setDisable(true);
                tblMedicoEspecialidad.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                tblMedicoEspecialidad.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                    int respueta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Medico Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respueta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicoEspecialidad(?)}");
                            procedimiento.setInt(1, ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
                            procedimiento.execute();
                            listaMedicoEspecialidad.remove(tblMedicoEspecialidad.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblMedicoEspecialidad.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblMedicoEspecialidad.getSelectionModel().clearSelection();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccioar un elemnto");
                }
        }
    }

    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                tblMedicoEspecialidad.getSelectionModel().clearSelection();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(cmbCodigoMedico.getSelectionModel().isEmpty() || cmbCodigoEspecialidad.getSelectionModel().isEmpty() || cmbCodigoHorario.getSelectionModel().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblMedicoEspecialidad.getSelectionModel().clearSelection();
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
        cmbCodigoMedico.setDisable(true);
        cmbCodigoEspecialidad.setDisable(true);
        cmbCodigoHorario.setDisable(true);
    }
    
    public void activarControles(){
        cmbCodigoMedico.setDisable(false);
        cmbCodigoEspecialidad.setDisable(false);
        cmbCodigoHorario.setDisable(false);
    }
    
    public void limpiarControles(){
        cmbCodigoMedico.getSelectionModel().clearSelection();
        cmbCodigoEspecialidad.getSelectionModel().clearSelection();
        cmbCodigoHorario.getSelectionModel().clearSelection();
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
