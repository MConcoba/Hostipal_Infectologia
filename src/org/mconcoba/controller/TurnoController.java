
package org.mconcoba.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.mconcoba.bean.MedicoEspecialidad;
import org.mconcoba.bean.Paciente;
import org.mconcoba.bean.ResponsableTurno;
import org.mconcoba.bean.Turno;
import org.mconcoba.db.Conexion;
import org.mconcoba.sistema.Principal;


public class TurnoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Turno> listaTurno;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fechaTurno;
    private DatePicker fechaCita;
    
    @FXML private TextField txtValorCita;
    @FXML private ComboBox cmbCodigoMedicoEspecialidad;
    @FXML private ComboBox cmbCodigoResponsableTurno;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private GridPane grpFechaTurno;
    @FXML private GridPane grpFechaCita;
    @FXML private TableView tblTurnos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colResponsable;
    @FXML private TableColumn colCodigoResponsable;
    @FXML private TableColumn colFechaTurno;
    @FXML private TableColumn colCodigoMedicoEspecialidad;
    @FXML private TableColumn colPaciente;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colValorCita;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnOpciones;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedicoEspecialidad.setItems(getMedicoEspecialidad());
        cmbCodigoResponsableTurno.setItems(getResponsableTurno());
        cmbCodigoPaciente.setItems(getPacientes());
        fechaTurno = new DatePicker(Locale.ENGLISH);
        fechaTurno.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaTurno.getCalendarView().todayButtonTextProperty().set("Today");
        fechaTurno.getCalendarView().setShowWeeks(false);
        fechaTurno.getStylesheets().add("/org/manuelconcoba/resource/DatePicker.css");
        grpFechaTurno.add(fechaTurno, 0, 0);
        fechaCita = new DatePicker(Locale.ENGLISH);
        fechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaCita.getCalendarView().todayButtonTextProperty().set("Today");
        fechaCita.getCalendarView().setShowWeeks(false);
        fechaCita.getStylesheets().add("/org/manuelconcoba/resource/DatePicker.css");
        grpFechaCita.add(fechaCita, 0, 0);
    }

    
    public void guardar(){
        Turno registro = new Turno();
        registro.setFechaTurno(fechaTurno.getSelectedDate());
        registro.setFechaCita(fechaCita.getSelectedDate());
        registro.setValorCita(Double.parseDouble(txtValorCita.getText()));
        registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
        registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?, ?, ?, ?, ?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaTurno().getTime()));
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setDouble(3, registro.getValorCita());
            procedimiento.setInt(4, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(5, registro.getCodigoResponsableTurno());
            procedimiento.setInt(6, registro.getCodigoPaciente());
            procedimiento.execute();
            listaTurno.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
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
    
    
    public ObservableList<ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsablesTurnos}");
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                lista.add(new ResponsableTurno(registro.getInt("codigoResponsableTurno"),
                                            registro.getString("apellidosResponsable"),
                                            registro.getString("nombresResponsable"),
                                            registro.getString("telefonoPersonal"),
                                            registro.getInt("codigoArea"),
                                            registro.getInt("codigoCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaResponsableTurno = FXCollections.observableList(lista);
    }
    
    
    public ObservableList<Paciente> getPacientes(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                                        resultado.getString("DPI"),
                                        resultado.getString("nombres"),
                                        resultado.getString("apellidos"),
                                        resultado.getDate("fechaNacimiento"),
                                        resultado.getInt("edad"),
                                        resultado.getString("Direccion"),
                                        resultado.getString("Ocupacion"),
                                        resultado.getString("sexo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableList(lista);
    }
    
    public ObservableList<Turno> getTurnos(){
        ArrayList<Turno> lista = new ArrayList<Turno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurnos}");
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                lista.add(new Turno(registro.getInt("codigoTurno"),
                                    registro.getDate("fechaTurno"),
                                    registro.getDate("fechaCita"),
                                    registro.getDouble("valorCita"),
                                    registro.getInt("codigoMedicoEspecialidad"),
                                    registro.getInt("codigoResponsableTurno"),
                                    registro.getInt("codigoPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTurno = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblTurnos.setItems(getTurnos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoTurno"));
        colFechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaTurno"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaCita"));
        colValorCita.setCellValueFactory(new PropertyValueFactory<Turno, Double>("valorCita"));
        colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoMedicoEspecialidad"));
        colCodigoResponsable.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoResponsableTurno"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("colCodigoPaciente"));
    }
    
    
    public void seleccionarElementos(){
        if(tblTurnos.getSelectionModel().getSelectedItem() != null){
            txtValorCita.setText(String.valueOf(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getValorCita()));
            fechaTurno.setSelectedDate(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getFechaTurno());
            fechaCita.setSelectedDate(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getFechaCita());
            cmbCodigoMedicoEspecialidad.getSelectionModel().select(buscarMedicoEspecialidad(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad()));
            cmbCodigoResponsableTurno.getSelectionModel().select(buscarResponsableTurno(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
            cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        }else{
            tblTurnos.getSelectionModel().clearSelection();
        }
    }
    
    
    public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblTurnos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblTurnos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblTurnos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
        }
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
    
    public ResponsableTurno buscarResponsableTurno(int codigoResponsableTurno){
        ResponsableTurno resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarResponsableTurno(?)}");
                procedimiento.setInt(1, codigoResponsableTurno);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new ResponsableTurno(registro.getInt("codigoResponsableTurno"),
                                                        registro.getString("apellidosResponsable"),
                                                        registro.getString("nombresResponsable"),
                                                        registro.getString("telefonoPersonal"),
                                                        registro.getInt("codigoArea"),
                                                        registro.getInt("codigoCargo"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery(); 
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                        registro.getString("DPI"),
                                        registro.getString("nombres"),
                                        registro.getString("apellidos"),
                                        registro.getDate("fechaNacimiento"),
                                        registro.getInt("edad"),
                                        registro.getString("direccion"),
                                        registro.getString("ocupacion"),
                                        registro.getString("sexo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Turno buscarTurno(int codigoTurnos){
        Turno resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTurno(?)}");
            procedimiento.setInt(1, codigoTurnos);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Turno(registro.getInt("codigoTurno"),
                                    registro.getDate("fechaTurnos"),
                                    registro.getDate("fechcaCita"),
                                    registro.getDouble("valorCita"),
                                    registro.getInt("codigoMedicoEspecialidad"),
                                    registro.getInt("codigoResponsableTurno"),
                                    registro.getInt("codigoPaciente"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTurno(?, ?, ?, ?, ?, ?, ?)}");
            Turno registro = (Turno)tblTurnos.getSelectionModel().getSelectedItem();
            registro.setFechaTurno(fechaTurno.getSelectedDate());
            registro.setFechaCita(fechaCita.getSelectedDate());
            registro.setValorCita(Double.parseDouble(txtValorCita.getText()));
            registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
            registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
            registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            procedimiento.setInt(1, registro.getCodigoTurno());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaTurno().getTime()));
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setDouble(4, registro.getValorCita());
            procedimiento.setInt(5, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(6, registro.getCodigoResponsableTurno());
            procedimiento.setInt(7, registro.getCodigoPaciente());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                tblTurnos.getSelectionModel().clearSelection();
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
                if(tblTurnos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbCodigoMedicoEspecialidad.setDisable(true);
                    cmbCodigoResponsableTurno.setDisable(true);
                    cmbCodigoPaciente.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                if(fechaTurno.getSelectedDate() == null || fechaCita.getSelectedDate() == null || txtValorCita.getText().isEmpty() || cmbCodigoMedicoEspecialidad.getSelectionModel().isEmpty() || cmbCodigoResponsableTurno.getSelectionModel().isEmpty() || cmbCodigoPaciente.getSelectionModel().isEmpty()){
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
                    tblTurnos.getSelectionModel().clearSelection();
                    cmbCodigoMedicoEspecialidad.setDisable(true);
                    cmbCodigoResponsableTurno.setDisable(true);
                    cmbCodigoPaciente.setDisable(true);
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
                tblTurnos.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cmbCodigoMedicoEspecialidad.setDisable(true);
                cmbCodigoResponsableTurno.setDisable(true);
                cmbCodigoPaciente.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblTurnos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTurno(?)}");
                            procedimiento.setInt(1, ((Turno)tblTurnos.getSelectionModel().getSelectedItem()).getCodigoTurno());
                            procedimiento.execute();
                            limpiarControles();
                            tblTurnos.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblTurnos.getSelectionModel().clearSelection();
                    }       
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    limpiarControles();
                    tblTurnos.getSelectionModel().clearSelection();
                }
        }
    }
    
    
    public void nuevo(){
      switch(tipoDeOperacion){
          case NINGUNO:
                activarControles();
                limpiarControles();
                tblTurnos.getSelectionModel().clearSelection();
                cmbCodigoMedicoEspecialidad.setDisable(false);
                cmbCodigoResponsableTurno.setDisable(false);
                cmbCodigoPaciente.setDisable(false);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion =operaciones.GUARDAR;
                break;
          case GUARDAR:
              if(fechaTurno.getSelectedDate() == null || fechaCita.getSelectedDate() == null || txtValorCita.getText().isEmpty() || cmbCodigoMedicoEspecialidad.getSelectionModel().isEmpty() || cmbCodigoResponsableTurno.getSelectionModel().isEmpty() || cmbCodigoPaciente.getSelectionModel().isEmpty()){
                  JOptionPane.showMessageDialog(null, "Datos Incompletos");
              }else{
                  guardar();
                  desactivarControles();
                  limpiarControles();
                  tblTurnos.getSelectionModel().clearSelection();
                  btnNuevo.setText("Nuevo");
                  btnEliminar.setText("Eliminar");
                  btnEditar.setDisable(false);
                  btnReporte.setDisable(false);
                  cargarDatos();
                  cmbCodigoMedicoEspecialidad.setDisable(true);
                  cmbCodigoResponsableTurno.setDisable(true);
                  cmbCodigoPaciente.setDisable(true);
                  tipoDeOperacion = operaciones.NINGUNO;
              }
              break;
      }  
    }
    
    
    
    public void desactivarControles(){
        txtValorCita.setEditable(false);
        grpFechaTurno.setDisable(true);
        grpFechaCita.setDisable(true);
        tblTurnos.setDisable(false);
    }
    
    
    public void activarControles(){
        txtValorCita.setEditable(true);
        grpFechaTurno.setDisable(false);
        grpFechaCita.setDisable(false);
        tblTurnos.setDisable(true);
    }
    
    
    public void limpiarControles(){
        txtValorCita.setText("");
        fechaTurno.setSelectedDate(null);
        fechaCita.setSelectedDate(null);
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
 
    public void ventanaResponsableTurno(){
        escenarioPrincipal.ventanaResponsableTurno();
    }
}
