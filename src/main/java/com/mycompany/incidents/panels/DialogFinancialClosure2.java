package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Closure;
import com.mycompany.incidents.jpaControllers.ClosureJpaController;
import com.mycompany.incidents.otherResources.ClosureTypeEnum;
import com.mycompany.incidents.otherResources.DataBaseController;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DialogFinancialClosure2 extends javax.swing.JDialog implements Runnable {
  
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
  ClosureJpaController closureController = new ClosureJpaController(factory);
  Closure currentClosure = null;
  String ruta = "";
	String hoja = "TI - Empresariales";        
  boolean runProcess = false;
  private Thread h1;
  String[] sheetNames={"SAP RESE","GW RESE","SAP GAST","GW GAST","SAP PAGO","GW PAGO","SAP SALV","GW SALV"};
  String[] sheetGWNames  = {"GW RESE","GW GAST","GW PAGO","GW SALV"};
  DataFormatter objDefaultFormat = new DataFormatter();
  FormulaEvaluator objFormulaEvaluator = null;
  HashMap<ClosureTypeEnum,String> searchCriteria = null;
  String limitInconsistencies ="100";
  int rownum = 0;
  HashMap<String,Integer> sheetSize = new HashMap<>();//numero de lineas por hoja  
  String sheetInconcistecyName = "-";
  SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  DataBaseController conection = new DataBaseController();
  int currentBarProgress = 0;
  int currentItemNumber = 0;//cuantos items ha finalizado en el proceso actual 
  int totalItemNumber = 0;//total de items en el proceso actual
    
  public DialogFinancialClosure2(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    btnStart.setEnabled(false);    
  }
  
  @Override
  public void run() {        
    
    try {            
      if(runProcess) {                  
        progressTotal(100,0);                   
        sheetInconcistecyName = "ERRORES_" + sdf.format(new Date()).replaceAll("/", "-").replaceAll(":", "-").replaceAll(" ", "_");
        btnStart.setEnabled(false);
        btnSelectFile.setEnabled(false);
        txtLimit.setEnabled(false);
        outputTxt.setText(outputTxt.getText()+"\nINICIO: " + sdf.format(new Date()));
        outputTxt.setText(outputTxt.getText()+"\nCargando Archivo excel...");
        loadExcelFile();    
        String validResult = invalidFile();    
        if(validResult.length()!=0) {
          outputTxt.setText(outputTxt.getText()+validResult);
        } else {      
            createInconsistencesSheet();
            progressTotal(100,0);
            outputTxt.setText(outputTxt.getText()+"\nLimpiando tablas...");          
            regenerateTableClosure();              
            limitInconsistencies = txtLimit.getText();            
            readAndInsertRows();        
            searchInconsistencies();
            outputTxt.setText(outputTxt.getText()+"\nLimpiando tablas...");          
            regenerateTableClosure();
            outputTxt.setText(outputTxt.getText()+"\nFIN: " + sdf.format(new Date()));
            outputTxt.setText(outputTxt.getText()+"\nFINALIZO CORRECTAMENTE");                              
        }
      } 
    } catch (FileNotFoundException e1) {      
      outputTxt.setText(outputTxt.getText()+"\nFileNotFoundException: " + e1.getMessage());
    } catch (IOException e2) {
      outputTxt.setText(outputTxt.getText()+"\nIOException: " + e2.getMessage());
    } catch (SQLException ex) { 
      outputTxt.setText(outputTxt.getText()+"\nSQLException: " + ex.getMessage());
    } catch (Exception e3) {
      outputTxt.setText(outputTxt.getText()+"\nException: " + e3.getMessage());
    }
    progressTotal(100,100);
    progressProcess(100,100);
    runProcess=false;
  }
  
  
  
  public void readAndInsertRows() throws Exception  {
    
    for (String sheetGWName : sheetGWNames) {      
      //registrando GW----------------------
      outputTxt.setText(outputTxt.getText()+"\nRegistrando " + sheetGWName);
      XSSFSheet sheet = anExcelWorbook.getSheet(sheetGWName);
      objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) anExcelWorbook);      
      Iterator<Row> rowIterator = sheet.iterator();//obtener todas las filas de la hoja excel 
      ArrayList<Object> columnIdentifiers = determineColumns(sheetGWName);         
      Row row = rowIterator.hasNext()? rowIterator.next() : null;			
      currentItemNumber=0;
      while (rowIterator.hasNext()) {//recorrer campos gw e insertar regitros        
        insertRowInDB(rowIterator.next(),columnIdentifiers);        
        progressProcess(sheetSize.get(sheetGWName), ++currentItemNumber);
      }                
      currentBarProgress = currentBarProgress + 10;
      progressTotal(100,currentBarProgress);      
      
      //registrando SAP----------------------      
      String sheetSAPName = sheetGWName.replaceAll("GW", "SAP");
      outputTxt.setText(outputTxt.getText()+"\nRegistrando " + sheetSAPName);
      sheet = anExcelWorbook.getSheet(sheetSAPName); //recorrer campos sap y actulizar registros     
      rowIterator = sheet.iterator();//obtener todas las filas de la hoja excel 
      columnIdentifiers = determineColumns(sheetSAPName);    
      row = rowIterator.hasNext()? rowIterator.next() : null;	
      currentItemNumber=0;
      while (rowIterator.hasNext()) {                        
        updateRowInDB(rowIterator.next(),columnIdentifiers);
        progressProcess(sheetSize.get(sheetSAPName), ++currentItemNumber);
      }  
      currentBarProgress = currentBarProgress + 10;
      progressTotal(100,currentBarProgress);      
    } 
  }
  
  
  
  private String determineReferenceValue(Object columnIdentifier,Row row) {
    if(columnIdentifier == null){
      return null;
    }
    String value = getCellString(row.getCell((int)columnIdentifier),objFormulaEvaluator,objDefaultFormat);
    if(value.contains("*")){
      return value.substring(0, value.indexOf("*"));
    }
    if(value.length()==0){
      return null;
    }
    return value;
  }  
 
  private void insertRowInDB(Row row,ArrayList<Object> columnIdentifiers) throws Exception {     
    String origen       = columnIdentifiers.get(0).toString();                
    String tipo         = columnIdentifiers.get(1).toString();
    String ramo         = determineStringValue(columnIdentifiers.get(4),row);
    String referencia   = determineReferenceValue(columnIdentifiers.get(5),row);
    String clave   = referencia == null ? null:              //Si no tiene referencia, la clave queda null 
                     referencia.toLowerCase() + "-" + ramo;  //si hay referencia la clave = referencia-ramo      
    Double valorCien    = determineDoubleValue(columnIdentifiers.get(6),row);
    Double valorReas    = determineDoubleValue(columnIdentifiers.get(7),row);          
    
    if(referencia.compareTo("cc:8662211")==0){
     System.out.println("AQUI");    
    } 
    System.out.println("insertRowInDB(GW) " + referencia);
    currentClosure = closureController.findClosureByReferenciaOrigenTipo(origen,tipo,clave);
   
    if(currentClosure!=null && clave != null) {                  
      currentClosure.setValorCienGw(determineDoubleValue(currentClosure.getValorCienGw()) + determineDoubleValue(valorCien));
      currentClosure.setValorReasGw(determineDoubleValue(currentClosure.getValorReasGw()) + determineDoubleValue(valorReas));      
      closureController.edit(currentClosure);      
    }
    else {//se crea nuevo registro si no existe una clave(referencia-ramo) igual, o el registro no tiene referencia
      Closure newClosure = new Closure();
      newClosure.setOrigen(origen);
      newClosure.setTipo(tipo);
      newClosure.setClaimnumber(determineStringValue(columnIdentifiers.get(2),row));
      newClosure.setPolicynumber(determineStringValue(columnIdentifiers.get(3),row));
      newClosure.setRamo(ramo);
      newClosure.setReferencia(clave);
      newClosure.setValorCienGw(valorCien);
      newClosure.setValorReasGw(valorReas);
      newClosure.setMoneda(determineStringValue(columnIdentifiers.get(8),row));
      newClosure.setEstado(determineStringValue(columnIdentifiers.get(9),row));      
      closureController.create(newClosure);       
    }    
  }
  
  private void updateRowInDB(Row row,ArrayList<Object> columnIdentifiers) throws Exception {     
    //Todos los registros que llegan a esta funcion seran de SAP
    String origen       = columnIdentifiers.get(0).toString();                
    String tipo         = columnIdentifiers.get(1).toString();
    String ramo         = determineStringValue(columnIdentifiers.get(4),row);
    String referencia   = determineReferenceValue(columnIdentifiers.get(5),row).toLowerCase() + "-" + ramo;        
    
    if(referencia.compareTo("cc:7490003-013")==0){
     System.out.println("AQUI");    
    }
    System.out.println("updateRowInDB(SAP) " + referencia);
    
    Double valorCien    = determineDoubleValue(columnIdentifiers.get(6),row);//sacado del excel
    Double valorReas    = determineDoubleValue(columnIdentifiers.get(7),row);//sacado del excel        
    
    
    
    currentClosure = closureController.findClosureByReferenciaOrigenTipo("GW",tipo,referencia);   
    if(currentClosure!=null){//se encontro registro de GW, actualizarlo                    
      double valorCienSap = determineDoubleValue(currentClosure.getValorCienSap()) + determineDoubleValue(valorCien);
      double valorReasSap = determineDoubleValue(currentClosure.getValorReasSap()) + determineDoubleValue(valorReas);
      currentClosure.setValorCienSap(valorCienSap);
      currentClosure.setValorReasSap(valorReasSap);
      if(tipo.equals("RESERVA") || tipo.equals("GASTO") || tipo.equals("SALVAMENTO")) {
        currentClosure.setDiferCien(determineDoubleValue(currentClosure.getValorCienGw()) + valorCienSap);
        currentClosure.setDiferReas(determineDoubleValue(currentClosure.getValorReasGw()) - valorReasSap);
      } else if(tipo.equals("PAGO")){
        currentClosure.setDiferCien(determineDoubleValue(currentClosure.getValorCienGw()) - valorCienSap);
        currentClosure.setDiferReas(determineDoubleValue(currentClosure.getValorReasGw()) + valorReasSap);
      }
      closureController.edit(currentClosure);      
    } else {//si no encuntra resultados indica que esta en SAP pero no esta en GW
      
      currentClosure = closureController.findClosureByReferenciaOrigenTipo("SAP",tipo,referencia);
      if(currentClosure!=null){//exite registro de SAP hay que actualizarlo
        double valorCienSap = determineDoubleValue(currentClosure.getValorCienSap()) + determineDoubleValue(valorCien);
        double valorReasSap = determineDoubleValue(currentClosure.getValorReasSap()) + determineDoubleValue(valorReas);
        currentClosure.setValorCienSap(valorCienSap);
        currentClosure.setValorReasSap(valorReasSap);
        if(currentClosure.getValorCienGw()!=null) {//actualizar diferencia solo si hay registro tambien en gw
          if(tipo.equals("RESERVA") || tipo.equals("GASTO") || tipo.equals("SALVAMENTO")) {
            currentClosure.setDiferCien(determineDoubleValue(currentClosure.getValorCienGw()) + valorCienSap);
            currentClosure.setDiferReas(determineDoubleValue(currentClosure.getValorReasGw()) - valorReasSap);
          } else if(tipo.equals("PAGO")){
            currentClosure.setDiferCien(determineDoubleValue(currentClosure.getValorCienGw()) - valorCienSap);
            currentClosure.setDiferReas(determineDoubleValue(currentClosure.getValorReasGw()) + valorReasSap);
          }
        }
        closureController.edit(currentClosure); 
      }else{//no existe registro de SAP hay que crearlo
        Closure newClosure = new Closure();
        newClosure.setOrigen(origen);
        newClosure.setTipo(tipo);
        newClosure.setClaimnumber(determineStringValue(columnIdentifiers.get(2),row));
        newClosure.setPolicynumber(determineStringValue(columnIdentifiers.get(3),row));
        newClosure.setRamo(ramo);
        newClosure.setReferencia(referencia);
        //ValorCienGw va a quedar null
        //ValorReasGw va a quedar null
        newClosure.setValorCienSap(valorCien);
        newClosure.setValorReasSap(valorReas);

        newClosure.setMoneda(determineStringValue(columnIdentifiers.get(8),row));
        newClosure.setEstado(determineStringValue(columnIdentifiers.get(9),row));      
        closureController.create(newClosure);
      }
    }
  }
  
  /*
  Dependiendo de cada pestaña del excel saber de que columna se saca un dato
  */
  private ArrayList<Object> determineColumns(String sheetName) {    
    switch(sheetName) {
                                                         //ORIGEN  TIPO          CLAIMN POLI RAMO REF VAL100 VALREA MONEDA ESTADO  
      case "SAP RESE": return new ArrayList<>(Arrays.asList("SAP" , "RESERVA"    ,null, 2    , 1  , 3 , 4    , 5    , 0    , null ));
      case "GW RESE":  return new ArrayList<>(Arrays.asList("GW"  , "RESERVA"    ,0   , 1    , 2  , 4 , 6    , 5    , 7    , 3    ));      
      case "SAP GAST": return new ArrayList<>(Arrays.asList("SAP" , "GASTO"      ,null, 2    , 1  , 3 , 4    , null , 0    , null ));      
      case "GW GAST":  return new ArrayList<>(Arrays.asList("GW"  , "GASTO"      ,0   , 1    , 2  , 3 , 5    , null , 4    , null ));
      case "SAP PAGO": return new ArrayList<>(Arrays.asList("SAP" , "PAGO"       ,null, null , 1  , 2 , 3    , 4    , 0    , null ));        
      case "GW PAGO":  return new ArrayList<>(Arrays.asList("GW"  , "PAGO"       ,0   , 1    , 2  , 3 , 7    , 4    , 6    , 5    ));  
      case "SAP SALV": return new ArrayList<>(Arrays.asList("SAP" , "SALVAMENTO" ,null, 2    , 1  , 3 , 4    , 5    , 0    , null ));        
      case "GW SALV":  return new ArrayList<>(Arrays.asList("GW"  , "SALVAMENTO" ,0   , 1    , 2  , 3 , 7    , 5    , 6    , 4    ));
    }
    return null;
  }
  
  /*
  se lo crea con anterioridad para saber si el archivo no esta abierto
  */
  private void createInconsistencesSheet() throws FileNotFoundException, IOException{
    XSSFSheet sheet = anExcelWorbook.createSheet(sheetInconcistecyName);      
    createHeaderRow(sheet.createRow(rownum++));
    FileOutputStream os = new FileOutputStream(anExcelFile);
    anExcelWorbook.write(os);
  }
  
  private void searchInconsistencies() throws FileNotFoundException, IOException{  
    
    XSSFSheet sheet = anExcelWorbook.getSheet(sheetInconcistecyName);      
    currentBarProgress=70;
    progressTotal(100,currentBarProgress);                
    
    insertInconsistencyInSheet("GW","RESERVA",ClosureTypeEnum.diferenciaCienMayorQue,sheet);    
    insertInconsistencyInSheet("GW","RESERVA",ClosureTypeEnum.diferenciaCienMenorQue,sheet);
    insertInconsistencyInSheet("GW","RESERVA",ClosureTypeEnum.diferenciaReaseguroMayorQue,sheet);
    insertInconsistencyInSheet("GW","RESERVA",ClosureTypeEnum.diferenciaReaseguroMenorQue,sheet);
    insertInconsistencyInSheet("GW","RESERVA",ClosureTypeEnum.cienNoEncontradoEnSap,sheet);
    insertInconsistencyInSheet("SAP","RESERVA",ClosureTypeEnum.cienNoEncontradoEnGW,sheet);
        
    insertInconsistencyInSheet("GW","PAGO",ClosureTypeEnum.diferenciaCienMayorQue,sheet);
    insertInconsistencyInSheet("GW","PAGO",ClosureTypeEnum.diferenciaCienMenorQue,sheet);
    insertInconsistencyInSheet("GW","PAGO",ClosureTypeEnum.diferenciaReaseguroMayorQue,sheet);
    insertInconsistencyInSheet("GW","PAGO",ClosureTypeEnum.diferenciaReaseguroMenorQue,sheet);
    insertInconsistencyInSheet("GW","PAGO",ClosureTypeEnum.cienNoEncontradoEnSap,sheet);
    insertInconsistencyInSheet("SAP","PAGO",ClosureTypeEnum.cienNoEncontradoEnGW,sheet);
    
    insertInconsistencyInSheet("GW","GASTO",ClosureTypeEnum.diferenciaCienMayorQue,sheet);
    insertInconsistencyInSheet("GW","GASTO",ClosureTypeEnum.diferenciaCienMenorQue,sheet);
    insertInconsistencyInSheet("GW","GASTO",ClosureTypeEnum.diferenciaReaseguroMayorQue,sheet);
    insertInconsistencyInSheet("GW","GASTO",ClosureTypeEnum.diferenciaReaseguroMenorQue,sheet);
    insertInconsistencyInSheet("GW","GASTO",ClosureTypeEnum.cienNoEncontradoEnSap,sheet);
    insertInconsistencyInSheet("SAP","GASTO",ClosureTypeEnum.cienNoEncontradoEnGW,sheet);
    
    insertInconsistencyInSheet("GW","SALVAMENTO",ClosureTypeEnum.diferenciaCienMayorQue,sheet);
    insertInconsistencyInSheet("GW","SALVAMENTO",ClosureTypeEnum.diferenciaCienMenorQue,sheet);
    insertInconsistencyInSheet("GW","SALVAMENTO",ClosureTypeEnum.diferenciaReaseguroMayorQue,sheet);
    insertInconsistencyInSheet("GW","SALVAMENTO",ClosureTypeEnum.diferenciaReaseguroMenorQue,sheet);
    insertInconsistencyInSheet("GW","SALVAMENTO",ClosureTypeEnum.cienNoEncontradoEnSap,sheet);
    insertInconsistencyInSheet("SAP","SALVAMENTO",ClosureTypeEnum.cienNoEncontradoEnGW,sheet);
    
    FileOutputStream os = new FileOutputStream(anExcelFile);
    anExcelWorbook.write(os); 
    outputTxt.setText(outputTxt.getText()+"\nFinalizando ");
  }
 
  private void createHeaderRow(Row aRow){    
    aRow.createCell(0).setCellValue("TIPO ERROR");
    aRow.createCell(1).setCellValue("REFERENCIA");
    aRow.createCell(2).setCellValue("RAMO");
    aRow.createCell(3).setCellValue("CLAIM NUMBER");
    aRow.createCell(4).setCellValue("POLCY NUMBER");
    aRow.createCell(5).setCellValue("MONEDA");
    aRow.createCell(6).setCellValue("ESTADO");
    aRow.createCell(7).setCellValue("VALOR 100 GW");
    aRow.createCell(8).setCellValue("REAS GW");
    aRow.createCell(9).setCellValue("VALOR 100 SAP");
    aRow.createCell(10).setCellValue("REAS SAP");
    aRow.createCell(11).setCellValue("DIF 100");
    aRow.createCell(12).setCellValue("DIF REAS");
    aRow.createCell(13).setCellValue("DETALLE");    
  }
  
  
  private boolean isZeroOrNull(Double value) {    
    if(value == null){
      return true;
    }
    return value == 0;
  }
  
  private boolean isOmitedItem(Closure aResult) {    
    
    if(aResult.getValorCienGw()==null && isZeroOrNull(aResult.getValorCienSap()) &&
       isZeroOrNull(aResult.getValorReasSap())) {//tiene que estar de primera
      return true;
    }
    if(aResult.getEstado()==null){//las siguietes instricciones no funcionan sin esta validacion
      return false;
    }    
    if(aResult.getEstado().contains("Anulado") && aResult.getValorCienSap()==null && 
       aResult.getValorCienGw()==0             && aResult.getValorReasGw()==0) {        
      return true;
    }    
    if(aResult.getEstado().contains("Aprobaci")){
      return true;
    }
    return aResult.getEstado().contains("En espera");    
  }
  
  private void insertInconsistencyInSheet(String origen, 
          String tipo, ClosureTypeEnum criterio, XSSFSheet sheet) 
                                         throws FileNotFoundException, IOException {    
    progressProcess(100, 0);
    progressTotal(100,++currentBarProgress);
    outputTxt.setText(outputTxt.getText()+"\nAnalizando " + tipo + " - " + criterio.name());
    
    List<Closure> resultList=searchInconsistency(origen,tipo,criterio,limitInconsistencies);        
    if(resultList.isEmpty()){
      return;
    }else{
      totalItemNumber = resultList.size();
      currentItemNumber = 0;      
    }                
    for(Closure aResult : resultList) {
      progressProcess(totalItemNumber, ++currentItemNumber);      
      if(isOmitedItem(aResult)){
        continue;
      }            
      Row row = sheet.createRow(rownum++);      
      int cellnum = 0;
      createCellInRow(row,cellnum++,tipo+" "+criterio.name());
      createCellInRow(row,cellnum++,aResult.getReferencia());
      createCellInRow(row,cellnum++,aResult.getRamo());
      createCellInRow(row,cellnum++,aResult.getClaimnumber());
      createCellInRow(row,cellnum++,aResult.getPolicynumber());
      createCellInRow(row,cellnum++,aResult.getMoneda());
      createCellInRow(row,cellnum++,aResult.getEstado());      
      
      if(tipo.equals("GASTO")){//gasto no realiza analisis de reaseguro
        createCellInRow(row,cellnum++,aResult.getValorCienGw()==null?"#N/A":aResult.getValorCienGw());                 
        createCellInRow(row,cellnum++,"NO APLICA");
        createCellInRow(row,cellnum++,aResult.getValorCienSap()==null?"#N/A":aResult.getValorCienSap());
        createCellInRow(row,cellnum++,"NO APLICA");
        createCellInRow(row,cellnum++,aResult.getDiferCien()==null?"#N/A":aResult.getDiferCien());
        createCellInRow(row,cellnum++,"NO APLICA");
      }  
      else {
        createCellInRow(row,cellnum++,aResult.getValorCienGw()==null?"#N/A":aResult.getValorCienGw());                 
        createCellInRow(row,cellnum++,aResult.getValorReasGw()==null?"#N/A":aResult.getValorReasGw());
        createCellInRow(row,cellnum++,aResult.getValorCienSap()==null?"#N/A":aResult.getValorCienSap());
        createCellInRow(row,cellnum++,aResult.getValorReasSap()==null?"#N/A":aResult.getValorReasSap());
        createCellInRow(row,cellnum++,aResult.getDiferCien()==null?"#N/A":aResult.getDiferCien());
        createCellInRow(row,cellnum++,aResult.getDiferReas()==null?"#N/A":aResult.getDiferReas());
      }      
    }    
           
  }
  
  private void createCellInRow(Row aRow,int cellNum,Object obj){
    Cell aCell = aRow.createCell(cellNum);
    if (obj instanceof String) { 
        aCell.setCellValue((String) obj); 
    } else if (obj instanceof Boolean) { 
      aCell.setCellValue((Boolean) obj); 
    } else if (obj instanceof Date) { 
      aCell.setCellValue((Date) obj); 
    } else if (obj instanceof Double) { 
      aCell.setCellValue((Double) obj); 
    }      
  }  
  
  private List<Closure> searchInconsistency(String origen,String tipo,ClosureTypeEnum typeDif,String limit) {    
    searchCriteria = new HashMap<>();
    searchCriteria.put(ClosureTypeEnum.origen,origen); 
    searchCriteria.put(ClosureTypeEnum.tipo,tipo);
    searchCriteria.put(typeDif,limit);    
    return closureController.findBySearchCriteria(searchCriteria);        
  }
  
  private void regenerateTableClosure() throws SQLException, ClassNotFoundException{  
    conection.connect();     
    //conection.removeTablesClosures();
    conection.createTablesClosures(); 
    conection.disconect();
  }

  private void progressProcess(int total, int actual){        
    this.progressBarProceso.setValue((int)((actual*100)/total));
  }
  
  private void progressTotal(int total, int actual){        
    this.progressBar.setValue((int)((actual*100)/total));
  }
  
  private String determineStringValue(Object columnIdentifier,Row row) {
    return columnIdentifier == null? null: getCellString(row.getCell((int)columnIdentifier),objFormulaEvaluator,objDefaultFormat);
  }  
    
  private String getCellString(Cell cell, FormulaEvaluator objFormulaEvaluator, DataFormatter objDefaultFormat){
    objFormulaEvaluator.evaluate(cell); // This will evaluate the cell, And any type of cell will return string value
    return objDefaultFormat.formatCellValue(cell,objFormulaEvaluator);          
  }
  
  private Double determineDoubleValue(Object columnIdentifier,Row row){
    return columnIdentifier == null? null: row.getCell((int)columnIdentifier).getNumericCellValue();
  }
  
  private Double determineDoubleValue(Double aValue){
    return aValue == null ? 0 : aValue;
  }  
  
  //este metodo elimina filas que esten vacias
  private String removeEmptyRows(){
    String strOut="";        
    for (String sheetName : sheetNames) {
      XSSFSheet sheet = anExcelWorbook.getSheet(sheetName);
      boolean stop = false;
      boolean nonBlankRowFound;
      short c;      
      while (stop == false) {
        nonBlankRowFound = false;
        XSSFRow lastRow = sheet.getRow(sheet.getLastRowNum());
        for (c = lastRow.getFirstCellNum(); c <= lastRow.getLastCellNum(); c++) {
          XSSFCell cell = lastRow.getCell(c);
          if (cell != null && lastRow.getCell(c).getCellType() != CellType.BLANK) {
            nonBlankRowFound = true;
          }
        }
        if (nonBlankRowFound == true) {
          stop = true;
        } else {
          sheet.removeRow(lastRow);
        }
      }
    }
    return strOut;
  }
  
  
  private String invalidFile() {
    String strOut="";    
    for (String sheetName : sheetNames) {
      XSSFSheet sheet = anExcelWorbook.getSheet(sheetName);
      if (sheet==null) {
        strOut = strOut + "ERROR NO SE ENCONTRO: " + sheetName + " / ";
      } else {
        sheetSize.put(sheetName, sheet.getLastRowNum());
      }
      if (sheetName.compareTo(sheetInconcistecyName) == 0) {
        strOut = strOut + "ERROR: EXISTE HOJA " + sheetInconcistecyName + " / ";
        break;
      }
    }
    return strOut;
  }
    
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    progressBar = new javax.swing.JProgressBar();
    btnSelectFile = new javax.swing.JButton();
    btnStart = new javax.swing.JButton();
    labelFile = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    outputTxt = new javax.swing.JTextArea();
    jLabel1 = new javax.swing.JLabel();
    txtLimit = new javax.swing.JFormattedTextField();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    progressBarProceso = new javax.swing.JProgressBar();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("CIERRE FINANCIERO");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    progressBar.setStringPainted(true);

    btnSelectFile.setText("Selccionar");
    btnSelectFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelectFileActionPerformed(evt);
      }
    });

    btnStart.setText("Iniciar");
    btnStart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnStartActionPerformed(evt);
      }
    });

    outputTxt.setEditable(false);
    outputTxt.setColumns(20);
    outputTxt.setRows(5);
    outputTxt.setName(""); // NOI18N
    jScrollPane1.setViewportView(outputTxt);

    jLabel1.setText("Limite Monetario");

    txtLimit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
    txtLimit.setText("3");

    jLabel3.setText("PROCESO ACTUAL: ");

    jLabel4.setText("PROGRESO TOTAL: ");

    progressBarProceso.setStringPainted(true);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1)
          .addGroup(layout.createSequentialGroup()
            .addGap(6, 6, 6)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelectFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFile, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                  .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(progressBarProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(progressBarProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnSelectFile))
          .addComponent(labelFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnStart, javax.swing.GroupLayout.Alignment.TRAILING))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
        .addContainerGap())
    );

    btnStart.getAccessibleContext().setAccessibleName("btnStart");
    btnStart.getAccessibleContext().setAccessibleDescription("");

    setSize(new java.awt.Dimension(622, 549));
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void btnSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFileActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    if(fileChooser.showOpenDialog(fileChooser)==JFileChooser.APPROVE_OPTION) {
      ruta = fileChooser.getSelectedFile().getAbsolutePath();    		
      labelFile.setText(fileChooser.getSelectedFile().getName());      
      btnStart.setEnabled(true);
    }
    
  }//GEN-LAST:event_btnSelectFileActionPerformed
  
  File anExcelFile = null;
  FileInputStream anExcelFileInputStream = null;
  XSSFWorkbook anExcelWorbook = null;
  //FileOutputStream os = null;
    
  private void loadExcelFile(){
    try {
      anExcelFile = new File(ruta);
      anExcelFileInputStream = new FileInputStream(anExcelFile);
      anExcelWorbook = new XSSFWorkbook(anExcelFileInputStream);// leer archivo excel	 
      //os = new FileOutputStream(anExcelFile);
    } catch (FileNotFoundException ex) {
      outputTxt.setText(outputTxt.getText()+"\nFileNotFoundException: " + ex.getMessage());
      Logger.getLogger(DialogFinancialClosure2.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      outputTxt.setText(outputTxt.getText()+"\nIOException: " + ex.getMessage());
      Logger.getLogger(DialogFinancialClosure2.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        anExcelFileInputStream.close();
      } catch (IOException ex) {
        outputTxt.setText(outputTxt.getText()+"\nIOException: " + ex.getMessage());
        Logger.getLogger(DialogFinancialClosure2.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  private void closeExcelFile(){
    try {
      if(anExcelWorbook!=null){
        anExcelWorbook.close();
      }
      if(anExcelFileInputStream!=null){
        anExcelFileInputStream.close();
      }
      conection.disconect();
    } catch (IOException ex) {
      outputTxt.setText(outputTxt.getText()+"\nIOException: " + ex.getMessage());
      Logger.getLogger(DialogFinancialClosure2.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      outputTxt.setText(outputTxt.getText()+"\nIOException: " + ex.getMessage());
      Logger.getLogger(DialogFinancialClosure2.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  
  private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
    runProcess = true;
    h1 = new Thread(this);
    h1.start();
  }//GEN-LAST:event_btnStartActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    closeExcelFile();
  }//GEN-LAST:event_formWindowClosing

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(DialogFinancialClosure2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    
    //</editor-fold>
    //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        DialogFinancialClosure2 dialog = new DialogFinancialClosure2(new javax.swing.JFrame(), true);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(java.awt.event.WindowEvent e) {
            System.exit(0);
          }
        });
        dialog.setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnSelectFile;
  private javax.swing.JButton btnStart;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labelFile;
  private javax.swing.JTextArea outputTxt;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JProgressBar progressBarProceso;
  private javax.swing.JFormattedTextField txtLimit;
  // End of variables declaration//GEN-END:variables

  
}