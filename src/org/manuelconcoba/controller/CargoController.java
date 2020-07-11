
package org.manuelconcoba.controller;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.manuelconcoba.bean.Cargo;
import org.manuelconcoba.db.Conexion;
import org.manuelconcoba.sistema.Principal;

public class CargoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargo;
    
    
    @FXML private TextField txtCargo;
    @FXML private TableView tblCargos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    
    public void guardar(){
        Cargo registro = new Cargo();
        registro.setNombreCargo(txtCargo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargo(?)}");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.execute();
             listaCargo.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
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
    
    
    public void cargarDatos(){
        tblCargos.setItems(getCargos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
    }
    
    
    public void seleccionarElementos(){
        if(tblCargos.getSelectionModel().getSelectedItem() != null){
            txtCargo.setText(((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo());  
        }else{
            tblCargos.getSelectionModel().clearSelection();
        }
    }
    
    
    public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblCargos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblCargos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblCargos.getSelectionModel().clearSelection();
                limpiarControles();
                break;
        }
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
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargo(?, ?)}");
            Cargo registro = (Cargo)tblCargos.getSelectionModel().getSelectedItem();     
            registro.setNombreCargo(txtCargo.getText());
            procedimiento.setInt(1, registro.getCodigoCargo());
            procedimiento.setString(2, registro.getNombreCargo());
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
                tblCargos.getSelectionModel().clearSelection();
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
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
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
                if(txtCargo.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Dato Incompleto");
                }else{
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                    tblCargos.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                    break;
        }
    }
    
  
    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                tblCargos.getSelectionModel().clearSelection();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                            procedimiento.setInt(1, ((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                            procedimiento.execute();
                            listaCargo.remove(tblCargos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCargos.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                        tblCargos.getSelectionModel().clearSelection();
                    }        
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccinar un elemento");
                }
        }
    }
    
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                tblCargos.getSelectionModel().clearSelection();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                if(txtCargo.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Dato Incompleto");
                }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblCargos.getSelectionModel().clearSelection();
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
        txtCargo.setEditable(false);
        tblCargos.setDisable(false);
    }
    
    
    public void activarControles(){
        txtCargo.setEditable(true);
        tblCargos.setDisable(true);
    }
    
    
    public void limpiarControles(){
       txtCargo.setText("");
    }
   

    public Principal getEsecenarioPrincipa() {
        return escenarioPrincipal;
    }

    public void setEsecenarioPrincipa(Principal esecenarioPrincipa) {
        this.escenarioPrincipal = esecenarioPrincipa;
    }
    
    public void ventanaMedico(){
        escenarioPrincipal.ventanaMedico();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
   
    
}
