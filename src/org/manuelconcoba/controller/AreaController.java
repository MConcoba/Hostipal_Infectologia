
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
import org.manuelconcoba.bean.Area;
import org.manuelconcoba.db.Conexion;
import org.manuelconcoba.sistema.Principal;


public class AreaController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> listaArea;
    
    
    @FXML private TextField txtArea;
    @FXML private TableView tblAreas;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colArea;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void guardar(){
        Area registro = new Area();
        registro.setNombreArea(txtArea.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarArea(?)}");
            procedimiento.setString(1, registro.getNombreArea());
            procedimiento.execute();
             listaArea.add(registro);
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
                lista.add(new Area( resultado.getInt("codigoArea"),
                                    resultado.getString("nombreArea")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaArea = FXCollections.observableList(lista);
    }
        
    
    public void cargarDatos(){
        tblAreas.setItems(getAreas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codigoArea"));
        colArea.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
    }
    
    
    public void seleccionarElementos(){
        if(tblAreas.getSelectionModel().getSelectedItem() != null){
            txtArea.setText(((Area)tblAreas.getSelectionModel().getSelectedItem()).getNombreArea());   
        }else{
            tblAreas.getSelectionModel().clearSelection();
        }
    }
    
    
    public void deseleccionarElementos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                tblAreas.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case ACTUALIZAR:
                
                break;
            case ELIMINAR:
                tblAreas.getSelectionModel().clearSelection();
                limpiarControles();
                break;
            case NUEVO:
                tblAreas.getSelectionModel().clearSelection();
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
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarArea(?, ?)}");
            Area registro = (Area)tblAreas.getSelectionModel().getSelectedItem();     
            registro.setNombreArea(txtArea.getText());
            procedimiento.setInt(1, registro.getCodigoArea());
            procedimiento.setString(2, registro.getNombreArea());
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
                tblAreas.getSelectionModel().clearSelection();
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
                if(tblAreas.getSelectionModel().getSelectedItem() != null){
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
                if(txtArea.getText().isEmpty()){
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
                    tblAreas.getSelectionModel().clearSelection();
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
                tblAreas.getSelectionModel().clearSelection();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblAreas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarArea(?)}");
                            procedimiento.setInt(1, ((Area)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea());
                            procedimiento.execute();
                            listaArea.remove(tblAreas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblAreas.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        tblAreas.getSelectionModel().clearSelection();
                        limpiarControles();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                tblAreas.getSelectionModel().clearSelection();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                if(txtArea.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Dato Incompleto");
                }else{
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    tblAreas.getSelectionModel().clearSelection();
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
        txtArea.setEditable(false);
        tblAreas.setDisable(false);
    }
    
    public void activarControles(){
        txtArea.setEditable(true);
        tblAreas.setDisable(true);
    }
    
    public void limpiarControles(){
       txtArea.setText("");
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
    
    
}
