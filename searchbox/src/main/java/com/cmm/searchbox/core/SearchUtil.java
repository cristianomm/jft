/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmm.searchbox.core;

import com.cmm.searchbox.Grid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Cristiano M Martins
 */
public class SearchUtil {
    
    private String query;
    private String searchField;
    private String resultField;
    private Object value;
    private JTextField txtField;
    private ArrayList<String> fields;
    private Connection connection;
    
    
    public SearchUtil(Connection connection){
        this.connection = connection;
        this.fields = new ArrayList<String>();
    }
    
    public boolean showGrid(String param){
        boolean ret = false;
        
        //realiza a consulta
        ResultSet rs = queryDB(query);
        
        TableModel model = buildTableModel(rs);
        
        //cria um grid para ser populado e mostrado
        Grid grid = new Grid(this);
        
        grid.setTableModel(model);
        
        grid.setVisible(true);
        return ret;
    }

    public void setResultField(String resultField) {
        this.resultField = resultField;
    }

    public String getResultField() {
        return resultField;
    }
    
    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchField() {
        return searchField;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public void addField(String field){
        fields.add(field);
    }
    
    public String[] getFields(){
        return (String[])fields.toArray();
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setTxtField(JTextField txtField) {
        this.txtField = txtField;
    }

    public JTextField getTxtField() {
        return txtField;
    }
    
    
    
    
    private TableModel buildTableModel(ResultSet resultSet){
        DefaultTableModel model = null;
        
        if(resultSet!= null){
            try{
                Object[] colNames = new Object[fields.size()];
                
                int p=0;
                for (String s : fields) {
                    colNames[p++] = s;
                }
                model = new DefaultTableModel(colNames, 1);
                while(resultSet.next()){
                    Object[] row = new Object[fields.size()];
                    for(int i=0; i<colNames.length;i++){
                        row[i] = resultSet.getObject(i+1);
                    }
                    model.addRow(row);
                }
                
            }catch(SQLException e){
                
            }
        }
        
        return model;
    }
    
    private ResultSet queryDB(String query){
        ResultSet result = null;
        
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            result = ps.executeQuery();
        }catch(SQLException e){
            System.out.println(e);
        }catch(NullPointerException e){
            System.out.println(e);
        }
        
        return result;
    }
    
}
