
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.mconcoba.bean.Area;
import org.mconcoba.bean.Cargo;
import org.mconcoba.bean.ResponsableTurno;
import org.mconcoba.db.Conexion;
import org.mconcoba.sistema.Principal;


public class ResponsableTurnoController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Area> listaArea;
    private ObservableList<Cargo> listaCargo;
    
    
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox cmbCodigoArea;
    @FXML private ComboBox cmbCodigoCargo;
    @FXML private TableView tblResponsablesTurnos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colArea;
    @FXML private TableColumn colCodigoCargo;
    @FXML private TableColumn colCargo;    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnOpcion;
    @FXML private GridPane grpTurno;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
        cmbCodigoCargo.setItems(getCargos());
        
    }
    
    public void guardar(){
        ResponsableTurno registro = new ResponsableTurno();
        registro.setApellidosResponsable(txtApellidos.getText());
        registro.setNombresResponsable(txtNombres.getText());
        registro.setTelefonoPersonal(txtTelefono.getText());
        registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
        registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getApellidosResponsable());
            procedimiento.setString(2, registro.getNombresResponsable());
            procedimiento.setString(3, registro.getTelefonoPersonal());
            procedimiento.setInt(4, registro.getCodigoArea());
            procedimiento.setInt(5, registro.getCodigoCargo());
            procedimiento.execute();
            listaResponsableTurno.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ObservableList<Area> getAreas(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Area(resultado.getInt("codigoArea"),
                                    resultado.getString("nombreArea")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaArea = FXCollections.observableList(lista);
    }
    
    
    public ObservableList<Cargo> getCargos(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"),
                                    resultado.getString("nombreCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
    }
    
    public ObservableList<ResponsableTurno> getResponsablesTurnos(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsablesTurnos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                                                resultado.getString("apellidosResponsable"),
                                                resultado.getString("nombresResponsable"),
                                                resultado.getString("telefonoPersonal"),
                                                resultado.getInt("codigoArea"),
                                                resultado.getInt("codigoCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaResponsableTurno = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblResponsablesTurnos.setItems(getResponsablesTurnos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoResponsableTurno"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombresResponsable"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("apellidosResponsable"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal"));
        colCodigoArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoArea"));
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoCargo"));
    }
    
    
    
    public void seleccionarElementos(){
        if(tblResponsablesTurnos.getSelectionModel().getSelectedItem() != null){
            txtNombres.setText(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getNombresResponsable());
            txtApellidos.setText(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getApellidosResponsable());
            txtTelefono.setText(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
            cmbCodigoArea.getSelectionModel().select(buscarArea(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getCodigoArea()));
            cmbCodigoCargo.getSelectionModel().select(buscarCargo(((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getCodigoCargo()));
        }else{
            tblResponsablesTurnos.getSelectionModel().clearSelection();
        }
    }
    
     public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                tblResponsablesTurnos.getSelectionModel().clearSelection();
                break;
         
            case ELIMINAR:
                tblResponsablesTurnos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblResponsablesTurnos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
        }
    }
     
    public Area buscarArea(int codigoArea){
        Area resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Area(registro.getInt("codigoArea"),
                                    registro.getString("nombreArea"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Cargo buscarCargo(int codigoCargo){
        Cargo resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargo(registro.getInt("codigoCargo"),
                                    registro.getString("nombreCargo"));
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
                resultado = new ResponsableTurno(registro.getInt("codigoResponsableTruno"),
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
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarResponsableTurno(?, ?, ?, ?, ?, ?)}");
            ResponsableTurno registro = (ResponsableTurno) tblResponsablesTurnos.getSelectionModel().getSelectedItem();
            registro.setApellidosResponsable(txtApellidos.getText());
            registro.setNombresResponsable(txtNombres.getText());
            registro.setTelefonoPersonal(txtTelefono.getText());
            registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            procedimiento.setInt(1, registro.getCodigoResponsableTurno());
            procedimiento.setString(2, registro.getApellidosResponsable());
            procedimiento.setString(3, registro.getNombresResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getCodigoArea());
            procedimiento.setInt(6, registro.getCodigoCargo());
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
                tblResponsablesTurnos.getSelectionModel().clearSelection();
                cmbCodigoArea.setDisable(true);
                cmbCodigoCargo.setDisable(true);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblResponsablesTurnos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actulaizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbCodigoArea.setDisable(true);
                    cmbCodigoCargo.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                if(txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtTelefono.getText().isEmpty() || cmbCodigoArea.getSelectionModel().getSelectedItem() == null || cmbCodigoCargo.getSelectionModel().getSelectedItem() == null){
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
                    tblResponsablesTurnos.getSelectionModel().clearSelection();
                    cmbCodigoArea.setDisable(true);
                    cmbCodigoCargo.setDisable(true);
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
                tblResponsablesTurnos.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cmbCodigoArea.setDisable(true);
                cmbCodigoCargo.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblResponsablesTurnos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Responsable de Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                            procedimiento.setInt(1, ((ResponsableTurno)tblResponsablesTurnos.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                            procedimiento.execute();
                            listaResponsableTurno.remove(tblResponsablesTurnos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblResponsablesTurnos.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblResponsablesTurnos.getSelectionModel().clearSelection();
                        tipoDeOperacion = operaciones.NINGUNO;
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elmento");
                    limpiarControles();
                    tblResponsablesTurnos.getSelectionModel().clearSelection();
                }
        }
    }
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                tblResponsablesTurnos.getSelectionModel().clearSelection();
                cmbCodigoArea.setDisable(false);
                cmbCodigoCargo.setDisable(false);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtTelefono.getText().isEmpty() || cmbCodigoArea.getSelectionModel().getSelectedItem() == null || cmbCodigoCargo.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showConfirmDialog(null, "Datos Incompletos");
                }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblResponsablesTurnos.getSelectionModel().clearSelection();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    cargarDatos();
                    cmbCodigoArea.setDisable(true);
                    cmbCodigoCargo.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
        }
    }
    
    
    public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        tblResponsablesTurnos.setDisable(false);
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        tblResponsablesTurnos.setDisable(true);
        cmbCodigoArea.getSelectionModel().clearSelection();
        cmbCodigoCargo.getSelectionModel().clearSelection();
    }
    
    public void limpiarControles(){
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
    }
    
    public void verOpcion(){
        btnOpcion.setVisible(false);
        grpTurno.setVisible(true);
    }
    
    public void ocultar(){
        btnOpcion.setVisible(true);
        grpTurno.setVisible(false);
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
    
    public void ventanaTurno(){
        escenarioPrincipal.ventanaTurno();
    }
}
