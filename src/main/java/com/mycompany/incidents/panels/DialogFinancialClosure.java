package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Closure;
import com.mycompany.incidents.jpaControllers.ClosureJpaController;
import com.mycompany.incidents.otherResources.ClosureTypeEnum;
import com.mycompany.incidents.otherResources.DataBaseController;
import com.mycompany.incidents.otherResources.IncidentsUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DialogFinancialClosure extends javax.swing.JDialog implements Runnable {
  
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
  ClosureJpaController closureController = new ClosureJpaController(factory);
  Closure currentClosure = null;
  
  
  boolean runProcess = false;
  private Thread h1;
    
  String nomArchivoSapReserva        = "SAP_RESERVAS.csv";  
  String nomArchivoGwReserva         = "GW_RESERVAS.csv";    
  String nomArchivoSapGastos         = "SAP_GASTOS.csv";  
  String nomArchivoGwGastos          = "GW_GASTOS.csv";  
  String nomArchivoSapPagos          = "SAP_PAGOS.csv";  
  String nomArchivoGwPagos           = "GW_PAGOS.csv";  
  String nomArchivoSapSalvamentos    = "SAP_SALVAMENTOS.csv";  
  String nomArchivoGwSalvamentos     = "GW_SALVAMENTOS.csv";    
  String rutaCarpeta                 = "";  
  
  String headerArchivoSapReserva     = "Moneda;Clase;Ramo;Poliza;Referencia;Reserva 100% Importe MD;Reserva 100% Importe ML;Gto Liquidacion Importe MD;Gto Liquidacion Importe ML;Reaseguro Importe MD;Reaseguro Importe ML";
  String headerArchivoSapPagos       = "Moneda;Clase;Ramo;Poliza;Referencia;Texto posicion;Pagos Importe MD;Pagos Importe ML;Reaseguro Importe MD;Reaseguro Importe ML";
  String headerArchivoSapSalvamentos = "Moneda;Clase;Ramo;Poliza;Referencia;Texto posicion;Salvamentos Importe MD;Salvamentos Importe ML;Reaseguro Importe MD;Reaseguro Importe ML";
  String headerArchivoGwReserva      = "\"ID\";\"CLAIMNUMBER\";\"ESMIGRADO\";\"POLICYNUMBER\";\"ID_TRA\";\"TIPO_TRANSACCION\";\"COST_CATEGORY\";\"RAMO_CONTABLE\";\"ESTADO\";\"CREATETIME\";\"PUBLICID_TRA\";\"TRANSACCION_ORIGEN\";\"CLAIMAMOUNT\";\"CEDIDO\";\"RETENIDO\";\"SURA_RETENIDO\";\"VALOR_BRUTO\";\"MONEDA\";\"MOVIMIENTO\";\"ESTADO_CHEQUE\";\"RECALCULADO\";\"DIFERENCIA\";\"REFLECTION\"";
  String headerArchivoGwGastos       = "\"ID\";\"CLAIMNUMBER\";\"POLICYNUMBER\";\"FECHA_SINIESTRO\";\"FECHA_AVISO\";\"TIPO_TRANSACCION\";\"COST_CATEGORY\";\"RAMO_CONTABLE\";\"SUBTYPE\";\"ESTADO\";\"CREATETIME\";\"PUBLICID_TRA\";\"CLAIMAMOUNT\";\"FECHA_CONTABILIZACION\";\"MONEDA\";\"RESERVETYPE\";\"REFERENCEID\";\"PERCENTAJE\";\"SAP_AMOUNT\";\"LIQUIDATIONEXPENSESRESERVE\"";
  String headerArchivoGwPagos        = "\"ID\";\"TIPO_COASEGURO\";\"CLAIMNUMBER\";\"POLICYNUMBER\";\"RAMO_CONTABLE\";\"COST_CATEGORY\";\"COINSURANCE_EXT\";\"FECHA_SINIESTRO\";\"RECALCULADO\";\"TIPO\";\"NUMERO_TRANSACCION\";\"TRANSACCION_ORIGEN\";\"PAGO_SOLO_SURA\";\"MASIVO\";\"CEDIDO\";\"RETENIDO\";\"SURA_RETENIDO\";\"ESTADO\";\"FECHA_TRANSACCION\";\"VALOR_NETO\";\"MONEDA\";\"VALOR_BRUTO\";\"VALOR_CON_ICM\";\"VALOR_ICM\";\"VALOR_SIN_COASEG\";\"VALOR_BRUTO_SIN_COA\";\"DIFERENCIA\";\"REFLECTION\"";
  String headerArchivoGwSalvamentos  = "\"ID\";\"CLAIMNUMBER\";\"POLICYNUMBER\";\"RAMO_CONTABLE\";\"RECALCULADO\";\"COINSURANCE_EXT\";\"COST_CATEGORY\";\"FECHA_SINIESTRO\";\"TIPO\";\"NUMERO_TRANSACCION\";\"TRANSACCION_ORIGEN\";\"ESTADO\";\"FECHA_TRANSACCION\";\"CEDIDO\";\"RETENIDO\";\"RETENCION_PURA\";\"VALOR_NETO\";\"VALOR_BRUTO\";\"MONEDA\";\"VALOR_SIN_COASEG\";\"DIFERENCIA\";\"REFLECTION\"";
                                     //"ID";"CLAIMNUMBER";"POLICYNUMBER";"RAMO_CONTABLE";"RECALCULADO";"COINSURANCE_EXT";"COST_CATEGORY";"FECHA_SINIESTRO";"TIPO";"NUMERO_TRANSACCION";"TRANSACCION_ORIGEN";"ESTADO";"FECHA_TRANSACCION";"CEDIDO";"RETENIDO";"RETENCION_PURA";"VALOR_NETO";"VALOR_BRUTO";"MONEDA";"VALOR_SIN_COASEG";"DIFERENCIA";"REFLECTION"
  
  String[] fileNamesGw  = {nomArchivoGwReserva,nomArchivoGwGastos,nomArchivoGwPagos,nomArchivoGwSalvamentos};
  String[] fileNamesSap  = {nomArchivoSapReserva,nomArchivoSapGastos,nomArchivoSapPagos,nomArchivoSapSalvamentos};
  HashMap<ClosureTypeEnum,String> searchCriteria = null;
  String limitInconsistencies ="-";  
  SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  SimpleDateFormat sdfHora=new SimpleDateFormat("HH:mm:ss");
  DataBaseController conection = new DataBaseController();
  String lastRowInfo="";
  int currentBarProgress = 0;
  int currentItemNumber = 0;//cuantos items ha finalizado en el proceso actual 
  int totalItemNumber = 0;//total de items en el proceso actual
    
  public DialogFinancialClosure(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    btnStart.setEnabled(false);    
    spinnerLimit.setValue(1);
  }
  
   /*
  EN EL MANUAL DE AYUDA COLOCAR
    NOMBRES DE ARCHIVOS
    Dejar solo una cabecera uniendo la fia 1 con la dos
    verifcar las siguiente CABECERA: ...
  
    antes de convertir a CSV: Colocar 'IMPORTE MD' y 'IMPORTE ML' como tipo de celda 'General' 
    no impota que ya esten como 'General' se debe repetir la operación
  
  
  */
  
  @Override
  public void run() {        
    
    try {            
      if(runProcess) {                  
        progressTotal(100,0);                           
        btnStart.setEnabled(false);
        btnSelectFile.setEnabled(false);
        spinnerLimit.setEnabled(false);
        limitInconsistencies = spinnerLimit.getValue().toString();            
        
        
        printInOutputText("\nINICIO: " + sdf.format(new Date()));        
        printInOutputText("\nValidando archivos...");                  
        IncidentsUtil.validateFile(rutaCarpeta,nomArchivoSapReserva,headerArchivoSapReserva);
        IncidentsUtil.validateFile(rutaCarpeta,nomArchivoGwReserva,headerArchivoGwReserva);        
        IncidentsUtil.validateFile(rutaCarpeta,nomArchivoGwGastos,headerArchivoGwGastos);
        IncidentsUtil.validateFile(rutaCarpeta,nomArchivoSapPagos,headerArchivoSapPagos);
        IncidentsUtil.validateFile(rutaCarpeta,nomArchivoGwPagos,headerArchivoGwPagos);
        IncidentsUtil.validateFile(rutaCarpeta,nomArchivoSapSalvamentos,headerArchivoSapSalvamentos);
        IncidentsUtil.validateFile(rutaCarpeta,nomArchivoGwSalvamentos,headerArchivoGwSalvamentos);     
        progressTotal(100,5); 
        
        printInOutputText("\nLimpiando tablas...");          
        regenerateTableClosure();              
        progressTotal(100,10);        

        readAndInsertRowsGw(); 
        
        readAndInsertRowsSap();

        searchInconsistencies();

        printInOutputText("\nEliminando temporales...");          
        regenerateTableClosure();

        printInOutputText("\nFIN: " + sdf.format(new Date()));
        printInOutputText("\nFINALIZO CORRECTAMENTE");                              
        
      } 
    } catch (FileNotFoundException e1) {      
      printInOutputText("\nFileNotFoundException: " + e1.toString()+"\n"+lastRowInfo);
    } catch (IOException e2) {
      printInOutputText("\nIOException: " + e2.toString()+"\n"+lastRowInfo);
    } catch (SQLException ex) { 
      printInOutputText("\nSQLException: " + ex.toString()+"\n"+lastRowInfo);
    } catch (Exception e3) {
      printInOutputText("\nException: " + e3.toString()+"\n"+lastRowInfo);
    }
    progressTotal(100,100);
    progressProcess(100,100);
    runProcess=false;
  }
  
  private void printInOutputText(String textToAdd){
    outputTxt.setText(outputTxt.getText() + textToAdd);    
    outputTxt.setCaretPosition(outputTxt.getDocument().getLength());
  }
  
  
  
  public void readAndInsertRowsGw() throws Exception  {    
    for (String fileNameGw : fileNamesGw) {            
      printInOutputText("\nRegistrando " + fileNameGw + "...");
      progressProcess(100,0);
      String fileUrl= rutaCarpeta + "\\" + fileNameGw;      
      File archivo = new File (fileUrl);
      FileReader fr = new FileReader (archivo);
      BufferedReader br = new BufferedReader(fr);      
      currentItemNumber=0;
      String rowInfo=br.readLine();//la primer linea es cabecera                  
      int rowNum=(int)(archivo.length()/220);//220 bytes es el promedio del tamaño de una linea
      ArrayList<Object> columnIdentifiers = determineColumns(fileNameGw); 
      while((rowInfo = br.readLine())!=null){
        insertGwInfoInDB(rowInfo,columnIdentifiers);        
        progressProcess(rowNum, ++currentItemNumber);
      }                 
      fr.close();
      br.close();
      currentBarProgress = currentBarProgress + 10;
      progressTotal(100,currentBarProgress);       
    } 
  }
  
  public void readAndInsertRowsSap() throws Exception  {    
    for (String fileNameSap : fileNamesSap) {            
      printInOutputText("\nRegistrando " + fileNameSap + "...");
      progressProcess(100,0);
      String fileUrl = fileNameSap.compareTo(nomArchivoSapGastos) == 0 ? rutaCarpeta + "\\" + nomArchivoSapReserva : //para gastos usar el de reservas
                                                                         rutaCarpeta + "\\" + fileNameSap;      
      File archivo = new File (fileUrl);
      FileReader fr = new FileReader (archivo);
      BufferedReader br = new BufferedReader(fr);      
      currentItemNumber=0;
      String rowInfo=br.readLine();//la primer linea es cabecera                  
      int rowNum=(int)(archivo.length()/80);//80 bytes es el promedio del tamaño de una linea
      ArrayList<Object> columnIdentifiers = determineColumns(fileNameSap); 
      while((rowInfo = br.readLine())!=null){
        updateRowInDB(rowInfo,columnIdentifiers);        
        progressProcess(rowNum, ++currentItemNumber);
      }                 
      fr.close();
      br.close();
      currentBarProgress = currentBarProgress + 10;
      progressTotal(100,currentBarProgress);       
    } 
  }
  
  private String determineReferenceValue(Object columnIdentifier,String[] rowInfoSplit) {
    if(columnIdentifier == null){
      return null;
    }
    String value = rowInfoSplit[(int)columnIdentifier].replaceAll("\"", "");
    if(value.contains("*")){
      return value.substring(0, value.indexOf("*"));
    }
    if(value.length()==0){
      return null;
    }
    return value;
  }  
 
  private void insertGwInfoInDB(String rowInfo,ArrayList<Object> columnIdentifiers) throws Exception {     
    lastRowInfo = rowInfo;    
    String[] rowInfoSplit = rowInfo.split(";");    
    String origen       = columnIdentifiers.get(0).toString();                
    String tipo         = columnIdentifiers.get(1).toString();
    String ramo         = IncidentsUtil.determineStringValue(columnIdentifiers.get(4),rowInfoSplit);
    String referencia   = determineReferenceValue(columnIdentifiers.get(5),rowInfoSplit);
    String clave   = referencia == null ? null:              //Si no tiene referencia, la clave queda null 
                     referencia.toLowerCase() + "-" + ramo;  //si hay referencia la clave = referencia-ramo      
    Double valorCien    = IncidentsUtil.determineDoubleValue(columnIdentifiers.get(6),rowInfoSplit);
    Double valorReas    = IncidentsUtil.determineDoubleValue(columnIdentifiers.get(7),rowInfoSplit);          
           
    currentClosure = closureController.findClosureByReferenciaOrigenTipo(origen,tipo,clave);
   
    if(currentClosure!=null && clave != null) {                  
      currentClosure.setValorCienGw(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) + 
                                    IncidentsUtil.determineDoubleValue(valorCien));
      currentClosure.setValorReasGw(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) + 
                                    IncidentsUtil.determineDoubleValue(valorReas));      
      closureController.edit(currentClosure);      
    }
    else {//se crea nuevo registro si no existe una clave(referencia-ramo) igual, o el registro no tiene referencia
      Closure newClosure = new Closure();
      newClosure.setOrigen(origen);
      newClosure.setTipo(tipo);
      newClosure.setClaimnumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(2),rowInfoSplit));
      newClosure.setPolicynumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(3),rowInfoSplit));
      newClosure.setRamo(ramo);
      newClosure.setReferencia(clave);
      newClosure.setValorCienGw(valorCien);
      newClosure.setValorReasGw(valorReas);
      newClosure.setMoneda(IncidentsUtil.determineStringValue(columnIdentifiers.get(8),rowInfoSplit));
      newClosure.setEstado(IncidentsUtil.determineStringValue(columnIdentifiers.get(9),rowInfoSplit));      
      closureController.create(newClosure);       
    }    
  }
  
  private void updateRowInDB(String rowInfo,ArrayList<Object> columnIdentifiers) throws Exception {     
    lastRowInfo = rowInfo;
    //Todos los registros que llegan a esta funcion seran de SAP
    String[] rowInfoSplit = rowInfo.split(";"); 
    String origen       = columnIdentifiers.get(0).toString();                
    String tipo         = columnIdentifiers.get(1).toString();
    String ramo         = IncidentsUtil.determineStringValue(columnIdentifiers.get(4),rowInfoSplit);
    String referencia   = determineReferenceValue(columnIdentifiers.get(5),rowInfoSplit).toLowerCase() + "-" + ramo;        
    Double valorCien    = IncidentsUtil.determineDoubleValue(columnIdentifiers.get(6),rowInfoSplit);//sacado del excel
    Double valorReas    = IncidentsUtil.determineDoubleValue(columnIdentifiers.get(7),rowInfoSplit);//sacado del excel        
       
    currentClosure = closureController.findClosureByReferenciaOrigenTipo("GW",tipo,referencia);   
    if(currentClosure!=null){//se encontro registro de GW, actualizarlo                    
      double valorCienSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorCienSap()) + 
                            IncidentsUtil.determineDoubleValue(valorCien);
      double valorReasSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorReasSap()) + 
                            IncidentsUtil.determineDoubleValue(valorReas);
      currentClosure.setValorCienSap(valorCienSap);
      currentClosure.setValorReasSap(valorReasSap);
      if(tipo.equals("RESERVA") || tipo.equals("GASTO") || tipo.equals("SALVAMENTO")) {
        currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) + valorCienSap);
        currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) - valorReasSap);
      } else if(tipo.equals("PAGO")){
        currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) - valorCienSap);
        currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) + valorReasSap);
      }
      closureController.edit(currentClosure);      
    } else {//si no encuntra resultados indica que esta en SAP pero no esta en GW
      
      currentClosure = closureController.findClosureByReferenciaOrigenTipo("SAP",tipo,referencia);
      if(currentClosure!=null){//exite registro de SAP hay que actualizarlo
        double valorCienSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorCienSap()) + 
                              IncidentsUtil.determineDoubleValue(valorCien);
        double valorReasSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorReasSap()) + 
                              IncidentsUtil.determineDoubleValue(valorReas);
        currentClosure.setValorCienSap(valorCienSap);
        currentClosure.setValorReasSap(valorReasSap);
        if(currentClosure.getValorCienGw()!=null) {//actualizar diferencia solo si hay registro tambien en gw
          if(tipo.equals("RESERVA") || tipo.equals("GASTO") || tipo.equals("SALVAMENTO")) {
            currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) + valorCienSap);
            currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) - valorReasSap);
          } else if(tipo.equals("PAGO")){
            currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) - valorCienSap);
            currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) + valorReasSap);
          }
        }
        closureController.edit(currentClosure); 
      }else{//no existe registro de SAP hay que crearlo
        Closure newClosure = new Closure();
        newClosure.setOrigen(origen);
        newClosure.setTipo(tipo);
        newClosure.setClaimnumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(2),rowInfoSplit));
        newClosure.setPolicynumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(3),rowInfoSplit));
        newClosure.setRamo(ramo);
        newClosure.setReferencia(referencia);
        //ValorCienGw va a quedar null
        //ValorReasGw va a quedar null
        newClosure.setValorCienSap(valorCien);
        newClosure.setValorReasSap(valorReas);

        newClosure.setMoneda(IncidentsUtil.determineStringValue(columnIdentifiers.get(8),rowInfoSplit));
        newClosure.setEstado(IncidentsUtil.determineStringValue(columnIdentifiers.get(9),rowInfoSplit));      
        closureController.create(newClosure);
      }
    }
  }
  
  /*
  Dependiendo de cada archivo saber de que columna se saca un dato
  */
  private ArrayList<Object> determineColumns(String fileName) {    
    switch(fileName) {
      
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo Ref     ReseImpMD ReaImpMD  Moneda Estado  
      case "SAP_RESERVAS.csv":    return new ArrayList<>(Arrays.asList("SAP", "RESERVA",    null,  3,    2,   4,      5,        9,        0,     null   ));      
      
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo PuIdTra VlrBruto  Cedido    Moneda Estado  
      case "GW_RESERVAS.csv":     return new ArrayList<>(Arrays.asList("GW",  "RESERVA",    1,     3,    7,   10,     16,       13,       17,    8      ));
      
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo Ref     GtLiImpMD ReaImpMD  Moneda Estado  
      case "SAP_GASTOS.csv":      return new ArrayList<>(Arrays.asList("SAP", "GASTO",      null,  3,    2,   4,      7,        null,     0,     null   ));
                  
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo Ref     liqResExp VlrRea    Moneda Estado  
      case "GW_GASTOS.csv":       return new ArrayList<>(Arrays.asList("GW",  "GASTO",      1,     2,    7,   11,     19,       null,     14,    null   ));
      
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo Ref     PagImpMD  ReaImpMD  Moneda Estado  
      case "SAP_PAGOS.csv":       return new ArrayList<>(Arrays.asList("SAP", "PAGO",       null,  3,    2,   4,      6,        8,        0,     null   ));
      
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo NumTra  VlrSinCoa Cedido    Moneda Estado ARREGLADO  
      case "GW_PAGOS.csv":        return new ArrayList<>(Arrays.asList("GW",  "PAGO",       2,     3,    4,   10,     24,       14,       20,    17     ));
      
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo Ref     SalImpMD  ReaImpMD  Moneda Estado  
      case "SAP_SALVAMENTOS.csv": return new ArrayList<>(Arrays.asList("SAP", "SALVAMENTO", null,  3,    2,   4,      6,        8,        0,     null   ));
      
      //                                                               ORIGEN TIPO          Claim# Poli# Ramo NumTra  VlrSinCoa Cedido    Moneda Estado  
      case "GW_SALVAMENTOS.csv":  return new ArrayList<>(Arrays.asList("GW",  "SALVAMENTO", 1,     2,    3,   9,      19,       13,       18    ,11     ));
    }
    return null;
  }
   
  
  private void searchInconsistencies() throws FileNotFoundException, IOException{              
    String rutaResult = IncidentsUtil.determineUrl(rutaCarpeta,"CruceFinanciero");
    File file = new File(rutaResult);                
    if (!file.exists()) {// Si el archivo no existe es creado
        file.createNewFile();
    }
    FileWriter fw = new FileWriter(file);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(createHeaderRow());
    
    currentBarProgress=70;
    progressTotal(100,currentBarProgress);                
    
    insertInconsistencyInFile("GW","RESERVA",ClosureTypeEnum.diferenciaCienMayorQue, bw);    
    insertInconsistencyInFile("GW","RESERVA",ClosureTypeEnum.diferenciaCienMenorQue, bw);
    insertInconsistencyInFile("GW","RESERVA",ClosureTypeEnum.diferenciaReaseguroMayorQue, bw);
    insertInconsistencyInFile("GW","RESERVA",ClosureTypeEnum.diferenciaReaseguroMenorQue, bw);
    insertInconsistencyInFile("GW","RESERVA",ClosureTypeEnum.cienNoEncontradoEnSap, bw);
    insertInconsistencyInFile("SAP","RESERVA",ClosureTypeEnum.cienNoEncontradoEnGW, bw);
        
    insertInconsistencyInFile("GW","PAGO",ClosureTypeEnum.diferenciaCienMayorQue, bw);
    insertInconsistencyInFile("GW","PAGO",ClosureTypeEnum.diferenciaCienMenorQue, bw);
    insertInconsistencyInFile("GW","PAGO",ClosureTypeEnum.diferenciaReaseguroMayorQue, bw);
    insertInconsistencyInFile("GW","PAGO",ClosureTypeEnum.diferenciaReaseguroMenorQue, bw);
    insertInconsistencyInFile("GW","PAGO",ClosureTypeEnum.cienNoEncontradoEnSap, bw);
    insertInconsistencyInFile("SAP","PAGO",ClosureTypeEnum.cienNoEncontradoEnGW, bw);
    
    insertInconsistencyInFile("GW","GASTO",ClosureTypeEnum.diferenciaCienMayorQue, bw);
    insertInconsistencyInFile("GW","GASTO",ClosureTypeEnum.diferenciaCienMenorQue, bw);
    insertInconsistencyInFile("GW","GASTO",ClosureTypeEnum.diferenciaReaseguroMayorQue, bw);
    insertInconsistencyInFile("GW","GASTO",ClosureTypeEnum.diferenciaReaseguroMenorQue, bw);
    insertInconsistencyInFile("GW","GASTO",ClosureTypeEnum.cienNoEncontradoEnSap, bw);
    insertInconsistencyInFile("SAP","GASTO",ClosureTypeEnum.cienNoEncontradoEnGW, bw);
    
    insertInconsistencyInFile("GW","SALVAMENTO",ClosureTypeEnum.diferenciaCienMayorQue, bw);
    insertInconsistencyInFile("GW","SALVAMENTO",ClosureTypeEnum.diferenciaCienMenorQue, bw);
    insertInconsistencyInFile("GW","SALVAMENTO",ClosureTypeEnum.diferenciaReaseguroMayorQue, bw);
    insertInconsistencyInFile("GW","SALVAMENTO",ClosureTypeEnum.diferenciaReaseguroMenorQue, bw);
    insertInconsistencyInFile("GW","SALVAMENTO",ClosureTypeEnum.cienNoEncontradoEnSap, bw);
    insertInconsistencyInFile("SAP","SALVAMENTO",ClosureTypeEnum.cienNoEncontradoEnGW, bw);
       
    bw.close();
    fw.close();    
  }
 
  private String createHeaderRow(){    
    return "\"TIPO ERROR\";" +
    "\"REFERENCIA\";" +
    "\"RAMO\";" +
    "\"CLAIM NUMBER\";" +
    "\"POLCY NUMBER\";" +
    "\"MONEDA\";" +
    "\"ESTADO\";" +
    "\"VALOR 100 GW\";" +
    "\"REAS GW\";" +
    "\"VALOR 100 SAP\";" +
    "\"REAS SAP\";" +
    "\"DIF 100\";" +
    "\"DIF REAS\";" +
    "\"DETALLE\";\n";
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
  
  private void insertInconsistencyInFile(String origen,String tipo, ClosureTypeEnum criterio, BufferedWriter bw) 
                                         throws FileNotFoundException, IOException {    
    progressProcess(100, 0);
    progressTotal(100,++currentBarProgress);
    printInOutputText("\nAnalizando " + tipo + " - " + criterio.name() + "...");
    
    List<Closure> resultList=searchInconsistency(origen,tipo,criterio,limitInconsistencies);        
    if(resultList.isEmpty()){
      return;
    }else{
      totalItemNumber = resultList.size();
      currentItemNumber = 0;      
    } 
    String strRow = "";
    for(Closure aResult : resultList) {
      lastRowInfo = IncidentsUtil.determineRowInfo(aResult);
      progressProcess(totalItemNumber, ++currentItemNumber);      
      if(isOmitedItem(aResult)) { 
        continue;
      }      
      strRow = "\"" + tipo + " " + criterio.name() + "\";\"" + 
              aResult.getReferencia()   + "\";\"" +               
              aResult.getRamo()  + "\";\"" +
              aResult.getClaimnumber()  + "\";\"" +               
              aResult.getPolicynumber()  + "\";\"" +               
              aResult.getMoneda()  + "\";\"" +               
              aResult.getEstado() + "\";";
            
      if(tipo.equals("GASTO")){//gasto no realiza analisis de reaseguro
        strRow = strRow + "\"" + 
              IncidentsUtil.determineStringValue(aResult.getValorCienGw())  + "\";\"" +
              "NO APLICA"   + "\";\"" +
              IncidentsUtil.determineStringValue(aResult.getValorCienSap()) + "\";\"" +
              "NO APLICA" + "\";\"" +
              IncidentsUtil.determineStringValue(aResult.getDiferCien())    + "\";\"" +
              "NO APLICA" + "\";";
      }  
      else {
        strRow = strRow + "\"" + 
              IncidentsUtil.determineStringValue(aResult.getValorCienGw())  + "\";\"" +
              IncidentsUtil.determineStringValue(aResult.getValorReasGw())  + "\";\"" +
              IncidentsUtil.determineStringValue(aResult.getValorCienSap()) + "\";\"" +
              IncidentsUtil.determineStringValue(aResult.getValorReasSap()) + "\";\"" +
              IncidentsUtil.determineStringValue(aResult.getDiferCien())    + "\";\"" +
              IncidentsUtil.determineStringValue(aResult.getDiferReas())    + "\";";
      }
      strRow = strRow + "\n";
      bw.write(strRow);      
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
    conection.removeTableClosure();
    conection.createTablesClosures(); 
    conection.disconect();
  }

  private void progressProcess(int total, int actual){        
    this.progressBarProceso.setValue((int)((actual*100)/total));
  }
  
  private void progressTotal(int total, int actual){        
    this.progressBar.setValue((int)((actual*100)/total));
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
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    progressBarProceso = new javax.swing.JProgressBar();
    spinnerLimit = new javax.swing.JSpinner();

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
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(progressBarProceso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addComponent(spinnerLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelectFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFile, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(progressBarProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(spinnerLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnSelectFile))
              .addComponent(labelFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
        .addContainerGap())
    );

    btnStart.getAccessibleContext().setAccessibleName("btnStart");
    btnStart.getAccessibleContext().setAccessibleDescription("");

    setSize(new java.awt.Dimension(662, 549));
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void btnSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFileActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(fileChooser)==JFileChooser.APPROVE_OPTION) {
      rutaCarpeta = fileChooser.getSelectedFile().getAbsolutePath();    		
      labelFile.setText(rutaCarpeta);      
      btnStart.setEnabled(true);
    }
    
  }//GEN-LAST:event_btnSelectFileActionPerformed
    
  private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
    runProcess = true;
    h1 = new Thread(this);
    h1.start();
  }//GEN-LAST:event_btnStartActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    
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
      java.util.logging.Logger.getLogger(DialogFinancialClosure.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        DialogFinancialClosure dialog = new DialogFinancialClosure(new javax.swing.JFrame(), true);
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
  private javax.swing.JSpinner spinnerLimit;
  // End of variables declaration//GEN-END:variables

  

  
}