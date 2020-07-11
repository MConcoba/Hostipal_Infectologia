package org.manuelconcoba.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.manuelconcoba.bean.Medico;
import org.manuelconcoba.db.Conexion;
import org.manuelconcoba.ireport.GenerarReporte;
import org.manuelconcoba.sistema.Principal;

public class MedicoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    
    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtTurno;
    @FXML private TextField txtSexo;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private CheckBox cbMasculino;
    @FXML private CheckBox cbFemenino;
    @FXML private ComboBox cmbHoraE;
    @FXML private ComboBox cmbHoraS;
    @FXML private ComboBox cmbMinutoE;
    @FXML private ComboBox cmbMinutoS;
    @FXML private GridPane grpTelefonos;
    @FXML private GridPane grpAreas;
    @FXML private GridPane grpCargos;
    @FXML private GridPane grpTelefonoMedico;
    @FXML private Button btnOpcion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbHoraE.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08" ,"09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");
        cmbMinutoE.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60");
        cmbHoraS.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08" ,"09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");
        cmbMinutoS.getItems().addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60");
    }

    public void guardar(){
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(cmbHoraE.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoE.getSelectionModel().getSelectedItem().toString() + ":00");
        registro.setHoraSalida(cmbHoraS.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoS.getSelectionModel().getSelectedItem().toString() + ":00");
        registro.setSexo(txtSexo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico(?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getLicenciaMedica());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getHoraEntrada());
            procedimiento.setString(5, registro.getHoraSalida());
            procedimiento.setString(6, registro.getSexo());
            procedimiento.execute();
             listaMedico.add(registro);
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
                                    resultado.getString("apellidos"),
                                    resultado.getString("nombres"),
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
    
    
    public void cargarDatos(){
        tblMedicos.setItems(getMedicos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("codigoMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("licenciaMedica"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaSalida"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Medico, String>("turnoMaximo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));
    }
    
    
    public void seleccionarElementos(){
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
            txtLicenciaMedica.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
            txtNombres.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombres()));
            txtApellidos.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
            cmbHoraE.getSelectionModel().select(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada().substring(0, 2));
            cmbMinutoE.getSelectionModel().select(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada().substring(3, 5));
            cmbHoraS.getSelectionModel().select(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida().substring(0, 2));
            cmbMinutoS.getSelectionModel().select(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida().substring(3, 5));
//            txtHoraEntrada.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
//            txtHoraSalida.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
            txtTurno.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
            txtSexo.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
            cbMasculino.setSelected(txtSexo.getText().equals("Masculino"));
            cbFemenino.setSelected(txtSexo.getText().equals("Femenino"));  
        }else {
     
            tblMedicos.getSelectionModel().clearSelection();
        }
    }
    
    
    public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblMedicos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblMedicos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblMedicos.getSelectionModel().clearSelection();
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
   
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedico(?, ?, ?, ?, ?, ?, ?)}");
            Medico registro = (Medico)tblMedicos.getSelectionModel().getSelectedItem();
            registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setHoraEntrada(cmbHoraE.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoE.getSelectionModel().getSelectedItem().toString() + ":00");
            registro.setHoraSalida(cmbHoraS.getSelectionModel().getSelectedItem().toString() + ":" + cmbMinutoS.getSelectionModel().getSelectedItem().toString() + ":00");
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getLicenciaMedica());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNombres());
            procedimiento.setString(5, registro.getHoraEntrada());
            procedimiento.setString(6, registro.getHoraSalida());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoMedico", null);
        GenerarReporte.mostrarReporte("ReporteMedicos.jasper", "Reporte de Medicos", parametros);
    }
    
//    public void generarReporte(){
//        switch (tipoDeOperacion){
//            case NINGUNO:
//                imprimirReporte();
//                desactivarControles();
//                limpiarControles();
//                tipoDeOperacion = operaciones.NINGUNO;
//                break;
//        }
//    }
    
    public void reporte(){
        switch (tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                desactivarControles();
                limpiarControles();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                tblMedicos.getSelectionModel().clearSelection();
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
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
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
                if(txtLicenciaMedica.getText().isEmpty() || txtLicenciaMedica.getText().length() <= 3 || txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || cmbHoraE.getSelectionModel().isEmpty() 
                            || cmbMinutoE.getSelectionModel().isEmpty() || cmbHoraS.getSelectionModel().isEmpty() || cmbMinutoS.getSelectionModel().isEmpty() || txtSexo.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Datos Incompletos", "Error", 0);
                    
                }else{
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                    tblMedicos.getSelectionModel().clearSelection();
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
                tblMedicos.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedico(?)}");
                            procedimiento.setInt(1, ((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            listaMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex()); 
                            limpiarControles();
                            tblMedicos.getSelectionModel().clearSelection();   
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblMedicos.getSelectionModel().clearSelection();   
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
                tblMedicos.getSelectionModel().clearSelection();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtLicenciaMedica.getText().isEmpty() || txtLicenciaMedica.getText().length() <= 3 || txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || cmbHoraE.getSelectionModel().isEmpty() 
                            || cmbMinutoE.getSelectionModel().isEmpty() || cmbHoraS.getSelectionModel().isEmpty() || cmbMinutoS.getSelectionModel().isEmpty() || txtSexo.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Datos Incompletos", "Error", 0);
                }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblMedicos.getSelectionModel().clearSelection();
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
        txtLicenciaMedica.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
//        txtHoraEntrada.setEditable(false);
//        txtHoraSalida.setEditable(false);
        txtTurno.setEditable(false);
        txtSexo.setEditable(false);
        cbMasculino.setDisable(true);
        cbFemenino.setDisable(true);
        tblMedicos.setDisable(false);
        cmbHoraE.setDisable(true);
        cmbHoraS.setDisable(true);
        cmbMinutoE.setDisable(true);
        cmbMinutoS.setDisable(true);
    }
    
    
    public void activarControles(){
        txtLicenciaMedica.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
//        txtHoraEntrada.setEditable(true);
//        txtHoraSalida.setEditable(true);
        txtTurno.setEditable(false);
        txtSexo.setEditable(true);
        cbFemenino.setDisable(false);
        cbMasculino.setDisable(false);
        tblMedicos.setDisable(true);
        cmbHoraE.setDisable(false);
        cmbHoraS.setDisable(false);
        cmbMinutoE.setDisable(false);
        cmbMinutoS.setDisable(false);
    }
    
    
    public void limpiarControles(){
        txtLicenciaMedica.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
//        txtHoraEntrada.setText("");
//        txtHoraSalida.setText("");
        txtTurno.setText("");
        txtSexo.setText("");
        cbMasculino.setSelected(false);
        cbFemenino.setSelected(false);
        cmbHoraE.getSelectionModel().clearSelection();
        cmbHoraS.getSelectionModel().clearSelection();
        cmbMinutoE.getSelectionModel().clearSelection();
        cmbMinutoS.getSelectionModel().clearSelection();
        
    }
    
    
    @FXML
    public void sexo(ActionEvent event){
        if(event.getSource() == cbFemenino){
            cbMasculino.setDisable(true);
            if(!cbFemenino.isSelected()){
                cbMasculino.setDisable(false);
                txtSexo.setText(""); 
            }
            txtSexo.setText("Femenino");
        }if(event.getSource() == cbMasculino){
            cbFemenino.setDisable(true);
            if(!cbMasculino.isSelected()){
                cbFemenino.setDisable(false);
                txtSexo.setText("");
            }
            txtSexo.setText("Masculino");
        
        }if(!cbMasculino.isSelected() && !cbFemenino.isSelected()){
             txtSexo.setText("");
        }
    }
    
    public void normal(){
        btnOpcion.setVisible(true);
        grpTelefonoMedico.setVisible(false);
    }
    
    public void verOpcion(){
        btnOpcion.setVisible(false);
        grpTelefonoMedico.setVisible(true);
    }
    
    public void nexOpcione(){
        if(grpTelefonos.isVisible() && !grpAreas.isVisible() && !grpCargos.isVisible()){
            grpAreas.setVisible(true);
            grpTelefonos.setVisible(false);
        }else if(!grpTelefonos.isVisible() && grpAreas.isVisible() && !grpCargos.isVisible()){
            grpCargos.setVisible(true);
            grpAreas.setVisible(false);
        }else if(!grpTelefonos.isVisible() && !grpAreas.isVisible() && grpCargos.isVisible()){
            grpTelefonos.setVisible(true);
            grpCargos.setVisible(false);
        }
    }
    
    public void atrasOpcion(){
        if(grpTelefonos.isVisible() && !grpAreas.isVisible() && !grpCargos.isVisible()){
            grpCargos.setVisible(true);
            grpTelefonos.setVisible(false);
        }else if(!grpTelefonos.isVisible() && !grpAreas.isVisible() && grpCargos.isVisible()){
            grpAreas.setVisible(true);
            grpCargos.setVisible(false);
        }else if(!grpTelefonos.isVisible() && grpAreas.isVisible() && !grpCargos.isVisible()){
            grpTelefonos.setVisible(true);
            grpAreas.setVisible(false);
        }
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     
    public void ventanaArea(){
        escenarioPrincipal.ventanaArea();
    }
    
    public void ventanaCargo(){
        escenarioPrincipal.ventanaCargo();
    }
    
    public void ventanaTelefonoMedico(){
        escenarioPrincipal.ventanaTelefonoMedico();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
 
    public void soloNumeros(KeyEvent evento){
        try{
            char teclado = evento.getCharacter().charAt(0);
            if(!Character.isDigit(teclado) || txtLicenciaMedica.getText().length() == 4)
                evento.consume();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
