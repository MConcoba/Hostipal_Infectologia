
package org.manuelconcoba.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.time.LocalDate;
import org.manuelconcoba.bean.Paciente;
import org.manuelconcoba.db.Conexion;
import org.manuelconcoba.sistema.Principal;


public class PacienteController implements Initializable{

    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fecha; 
    
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEdad;
    @FXML private TextField txtSexo;
    @FXML private TextField txtOcupacion;
    @FXML private TextField txtDireccion;
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private CheckBox cbMasculino;
    @FXML private CheckBox cbFemenino;
    @FXML private GridPane grpFecha;
    @FXML private GridPane grpContactoUrgencia;
    @FXML private Button btnOpcion;
    
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
        
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/manuelconcoba/resource/DatePicker.css");
        grpFecha.add(fecha, 0, 0);
    }

    
    public void guardar(){
        
        Paciente registro = new Paciente();
        registro.setDPI(txtDPI.getText().substring(0, 8).concat("-") + txtDPI.getText().substring(8, 10).concat("-") + txtDPI.getText().substring(11, 13));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setFechaNacimiento(fecha.getSelectedDate());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPaciente(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getOcupacion());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
            listaPaciente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
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
    
    
    public void cargarDatos(){
        tblPacientes.setItems(getPacientes());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String> ("DPI"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Paciente, String> ("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String> ("apellidos"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date> ("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer> ("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String> ("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String> ("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String> ("sexo"));
    }

    
    public void seleccionarElementos(){
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
            txtDPI.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDPI());
            txtNombres.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
            txtEdad.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
            txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
            txtOcupacion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
            txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
            fecha.setSelectedDate(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
            cbMasculino.setSelected(txtSexo.getText().equals("Masculino"));
            cbFemenino.setSelected(txtSexo.getText().equals("Femenino"));
        }else {
            tblPacientes.getSelectionModel().clearSelection();
        }
    }
    
    
   public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblPacientes.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblPacientes.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblPacientes.getSelectionModel().clearSelection();
                limpiarControles();
                break;
        }
        
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
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPaciente(?, ?, ?, ?, ?, ?, ?, ?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setDPI(txtDPI.getText());
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setFechaNacimiento(fecha.getSelectedDate());
            registro.setDireccion(txtDireccion.getText());
            registro.setOcupacion(txtOcupacion.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getDPI());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNombres());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
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
                tblPacientes.getSelectionModel().clearSelection();
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    if(cbMasculino.isSelected()){
                        cbFemenino.setDisable(true);
                    }else if(cbFemenino.isSelected()){
                        cbMasculino.setDisable(true);
                    }
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                    break;
            case ACTUALIZAR:
                if(txtDPI.getText().isEmpty() || txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || fecha.getSelectedDate() == null || txtSexo.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtOcupacion.getText().isEmpty()){
                     JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }else if(txtDPI.getText().length() < 13){
                    JOptionPane.showMessageDialog(null, "El numero de DPI es incorrecto");
                }else if(!txtDPI.getText().isEmpty() && !txtNombres.getText().isEmpty() && !txtApellidos.getText().isEmpty() && fecha.getSelectedDate() != null && !txtSexo.getText().isEmpty() && !txtDireccion.getText().isEmpty() && !txtOcupacion.getText().isEmpty()){
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                    tblPacientes.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                    
                    break;
                }
        }
    }
    
    
    
    
    
    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                tblPacientes.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
            default:
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                            procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                            procedimiento.execute();
                            listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblPacientes.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblPacientes.getSelectionModel().clearSelection();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemnto");
                }
        }
    }
    

    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                tblPacientes.getSelectionModel().clearSelection();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                if(txtDPI.getText().isEmpty() || txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || fecha.getSelectedDate() == null || txtSexo.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtOcupacion.getText().isEmpty()){
                     JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }else if(txtDPI.getText().length() < 13){
                    JOptionPane.showMessageDialog(null, "El numero de DPI es incorrecto");
                }else if(!txtDPI.getText().isEmpty() && !txtNombres.getText().isEmpty() && !txtApellidos.getText().isEmpty() && fecha.getSelectedDate() != null && !txtSexo.getText().isEmpty() && !txtDireccion.getText().isEmpty() && !txtOcupacion.getText().isEmpty()){
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblPacientes.getSelectionModel().clearSelection();
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
        txtDPI.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtEdad.setEditable(false);
        txtSexo.setEditable(false);
        txtDireccion.setEditable(false);
        txtOcupacion.setEditable(false);
        grpFecha.setDisable(true);
        cbMasculino.setDisable(true);
        cbFemenino.setDisable(true);
        tblPacientes.setDisable(false);
    }
    
    public void activarControles(){
        txtDPI.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtEdad.setEditable(false);
        txtSexo.setEditable(true);
        txtDireccion.setEditable(true);
        txtOcupacion.setEditable(true);
        grpFecha.setDisable(false);
        cbMasculino.setDisable(false);
        cbFemenino.setDisable(false);
        tblPacientes.setDisable(true);
    }
    
    public void limpiarControles(){
        txtDPI.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEdad.setText("");
        txtSexo.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        cbMasculino.setSelected(false);
        cbFemenino.setSelected(false);
        fecha.setSelectedDate(null);
    }
    
    public void sexo(ActionEvent event){
        if(event.getSource() == cbFemenino){
            cbMasculino.setDisable(true);
            if(!cbFemenino.isSelected()){
                cbMasculino.setDisable(false);
                cbFemenino.setSelected(false);
                txtSexo.setText("");
            }
            txtSexo.setText("Femenino");
        }else if(event.getSource() == cbMasculino){
            cbFemenino.setDisable(true);
            if(!cbMasculino.isSelected()){
                cbFemenino.setDisable(false);
                txtSexo.setText("");
            }
            txtSexo.setText("Masculino");
        
        }else if(!cbMasculino.isSelected() && !cbFemenino.isSelected()){
             txtSexo.setText("");
        }
    }
    
    public void normal(){
        btnOpcion.setVisible(true);
        grpContactoUrgencia.setVisible(false);
    }
    
    public void verOpcion(){
        btnOpcion.setVisible(false);
        grpContactoUrgencia.setVisible(true);
    }

    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaContactoUrgencia(){
        escenarioPrincipal.ventanaContactoUrgencia();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal(); 
    }

    public void soloNumeros(KeyEvent KeyEvent){
        try{
            char teclado = KeyEvent.getCharacter().charAt(0);
            if(!Character.isDigit(teclado) || txtDPI.getText().length() == 13)
                KeyEvent.consume();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

