package com.mycompany.incidents.otherResources;

import com.mycompany.incidents.entities.GwLobModel;
import com.mycompany.incidents.entities.PrintableTable;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class TableController {    
    
  public TableModel createModel(Object[] entitiesList, String[] columNames)  {                     
    String[][] arrayResult = getArrayResult(entitiesList);        
    return new DefaultTableModel(arrayResult, columNames){
      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;  
      }
    };
  }
  
  public String[][] getArrayResult(Object[] entitiesList){
    String[][] arrayResult = null;
    for (int i = 0; i < entitiesList.length; i++) {
      PrintableTable anElement = (PrintableTable)entitiesList[i];
      if(arrayResult == null){
        arrayResult = new String[entitiesList.length][anElement.getData().length];    
      }            
      arrayResult[i]=anElement.getData();
    }        
    return arrayResult == null ? new String[0][0] : arrayResult;
  }  
  /*
  public TableModel createModelForLOB(List<GwLobModel> entitiesList,
                                          TypeKeysEnum category)  {                     
    String[][] arrayResult = getArrayResultForLOB(entitiesList,category);
    
    
    return new DefaultTableModel(arrayResult, GwLobModel.columNames(category)){
      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;  
      }
    };
  }
  
  public String[][] getArrayResultForLOB(List<GwLobModel> entitiesList,TypeKeysEnum category){
    String[][] arrayResult = null;
    for (int i = 0; i < entitiesList.size(); i++) {
      GwLobModel anElement = entitiesList.get(i);
      if(arrayResult == null){
        arrayResult = new String[entitiesList.size()][GwLobModel.columNames(category).length];    
      }            
      arrayResult[i]=anElement.getData(category);
    }        
    return arrayResult == null ? new String[0][0] : arrayResult;
  }  
  */
  public static void cofigureSizeColumns(JTable table, String[] columNames) {
    double sumAllNamesSize = 0;
    int tW = table.getWidth() == 0 ? 800 : table.getWidth();    
    for(String columnName : columNames){
      sumAllNamesSize = sumAllNamesSize + columnName.length();
    }
    for (int indexColumn = 0; indexColumn < table.getColumnCount(); indexColumn++) {
      DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
      TableColumn col = colModel.getColumn(indexColumn);
      double percentage = (columNames[indexColumn].length() * 100) / sumAllNamesSize;
      int width = (int) ((percentage * tW) / 100);                    
      col.setPreferredWidth(width);        
    }
  }    
}
