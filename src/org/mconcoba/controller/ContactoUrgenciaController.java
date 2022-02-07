
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
import org.mconcoba.bean.ContactoUrgencia;
import org.mconcoba.bean.Paciente;
import org.mconcoba.db.Conexion;
import org.mconcoba.sistema.Principal;


public class ContactoUrgenciaController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ContactoUrgencia> listaContactoUrgencia;
    private ObservableList<Paciente> listaPaciente;
    
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumeroContacto;
    @FXML private ComboBox cmbCodigoPaciente; 
    @FXML private TableView tblContactoUrgencia;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPaciente.setItems(getPacientes());
    }
    
    
    public void guardar(){
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNumeroContacto(txtNumeroContacto.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getApellidos());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.setInt(4, registro.getCodigoPaciente());
            procedimiento.execute();
             listaContactoUrgencia.add(registro);
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
    
    
    public ObservableList<ContactoUrgencia> getContactosUrgencia(){
        ArrayList<ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarContactosUrgencia}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),
                                                resultado.getString("nombres"),
                                                resultado.getString("apellidos"),
                                                resultado.getString("numeroContacto"),
                                                resultado.getInt("codigoPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaContactoUrgencia = FXCollections.observableList(lista);
    }
    
    
    public void cargarDatos(){
        tblContactoUrgencia.setItems(getContactosUrgencia());
        colCodigo.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoContactoUrgencia"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("numeroContacto"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoPaciente"));
    }
    
    
    public void seleccionarElementos(){
        if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
            txtNombres.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
            txtNumeroContacto.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto());
            cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        }else{
            tblContactoUrgencia.getSelectionModel().clearSelection();
        }
    }
    
    
    public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblContactoUrgencia.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblContactoUrgencia.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblContactoUrgencia.getSelectionModel().clearSelection();
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
    
    
    public ContactoUrgencia buscarContactoUrgencia (int codigoContactoUrgencia){
        ContactoUrgencia resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
            procedimiento.setInt(1, codigoContactoUrgencia);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new ContactoUrgencia(registro.getInt("codigoContactoUrgencia"),
                                                registro.getString("nombres"),
                                                registro.getString("apellidos"),
                                                registro.getString("numeroContacot"),
                                                registro.getInt("codigoPaciente"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarContactoUrgencia(?, ?, ?, ?, ?)}");
            ContactoUrgencia registro = (ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem();
            registro.setApellidos(txtApellidos.getText());
            registro.setNombres(txtNombres.getText());
            registro.setNumeroContacto(txtNumeroContacto.getText());
            registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            procedimiento.setInt(1, registro.getCodigoContactoUrgencia());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getNumeroContacto());
            procedimiento.setInt(5, registro.getCodigoPaciente());
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
                tblContactoUrgencia.getSelectionModel().clearSelection();
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
                if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    cmbCodigoPaciente.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                    break;
            case ACTUALIZAR:
                if(txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtNumeroContacto.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }else if(txtNumeroContacto.getText().length() < 7 ||  txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Verifique el Número de Telefono");
                }else{
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                    cmbCodigoPaciente.setDisable(true);
                    tblContactoUrgencia.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                    
                }
                break;
        }
    }
    

    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                tblContactoUrgencia.getSelectionModel().clearSelection();
                cmbCodigoPaciente.setDisable(true);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Contacto Urgencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                            procedimiento.setInt(1, ((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                            procedimiento.execute();
                            listaContactoUrgencia.remove(tblContactoUrgencia.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblContactoUrgencia.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblContactoUrgencia.getSelectionModel().clearSelection();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }

    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                tblContactoUrgencia.getSelectionModel().clearSelection();
                activarControles();
                cmbCodigoPaciente.setDisable(false);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtNumeroContacto.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Datos Incompletos");
                }else if(txtNumeroContacto.getText().length() < 7 || txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Verifique el Número de Telefono");
                }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblContactoUrgencia.getSelectionModel().clearSelection();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    cmbCodigoPaciente.setDisable(true);
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
            break;
        }
    }
    

    public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtNumeroContacto.setEditable(false);
//        cmbCodigoPaciente.setEditable(false);
        tblContactoUrgencia.setDisable(false);
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtNumeroContacto.setEditable(true);
//        cmbCodigoPaciente.setEditable(false);
        tblContactoUrgencia.setDisable(true);
    
    }
    
    public void limpiarControles(){
       txtNombres.setText("");
        txtApellidos.setText("");
        txtNumeroContacto.setText("");
        cmbCodigoPaciente.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
    public void soloNumeros(KeyEvent KeyEvent){
        try{
            
            char teclado = KeyEvent.getCharacter().charAt(0);
            if(!Character.isDigit(teclado) || txtNumeroContacto.getText().length() == 8)
                KeyEvent.consume();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
