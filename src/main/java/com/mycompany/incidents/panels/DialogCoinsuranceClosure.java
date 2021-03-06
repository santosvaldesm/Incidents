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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DialogCoinsuranceClosure extends javax.swing.JDialog implements Runnable {
  
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
  ClosureJpaController closureController = new ClosureJpaController(factory);
  Closure currentClosure = null;
  String rutaCarpeta = "";  
  String coasegEcoFileName = "COASEGURO_ECO.csv";
  String coasegSapFileName = "COASEGURO_SAP.csv";
  String coasegEcoHeader = "\"ID\";\"RECORD_CODE\";\"RAMO_A\";\"POLIZA\";\"RAMO_SINIESTRO\";\"NRO_SINIESTRO\";\"RAMO_FECHA\";\"FECHA_PROCESO\";\"RAMO_B\";\"DOCUMENTO\";\"RAMO_FECHA_PAGO\";\"FECHA_PAGO\";\"OFICINA_REGISTRO\";\"USUARIO_REG\";\"FECHA_REGISTRO\";\"HORA_REGISTRO\";\"SECUENCIA\";\"UNIDAD_ESTRATEGICA\";\"CIIU\";\"REGION\";\"SECTOR\";\"SUBSECTOR\";\"CODP\";\"VLR_IVA_RETENIDO\";\"PORC_RETENC_IVA\";\"VLR_DESC_RETENC_IVA\";\"PORC_ICA\";\"VLR_RETENC_ICA\";\"VLR_DESC_RETENC_ICA\";\"CONCEPTO_PAGO_RETENC\";\"ID_GRAN_CONTRIBUYENTE\";\"ID_IVA_REGIMEN_COS\";\"VLR_BASE_IVA\";\"FILLER\";\"AMPARO\";\"PLAN_A\";\"OPERACION_REGISTRO\";\"CLASE_PAGO\";\"IDENTIFICADOR\";\"CIA_COASEGURADORA\";\"PORC_PARTICIPACION\";\"RAMO_AGENTE\";\"COD_AGENTE\";\"PORC_PARTIC_AGENTE\";\"DCTO_LIDER\";\"FECHA_RECLAMACION\";\"FECHA_FIN_INCAPAC\";\"UNIDADES\";\"VLR_UNIDAD\";\"VLR_FACT_SALUD\";\"VLR_ASEG_MIN_DEDUD\";\"VLR_IND_PARC_DED_INF\";\"VLR_IND_TOT_RECIBO\";\"MONEDA\";\"CED_BENEFICIARIO_A\";\"NOMB_BENEFICIARIO_A\";\"OFICINA\";\"OPERACION_PAGO\";\"REMISION\";\"VLR_PAGADO\";\"VLR_PAGADO_TOT_SINI\";\"IDENT_PAGO\";\"USUARIO_PAGO\";\"VECTOR_BANDERA\";\"OFICINA_RADICACION\";\"FECHA_OCURRENCIA\";\"FECHA_AVISO\";\"NOMB_ASEGURADO\";\"CED_ASEGURADO\";\"VLR_POOL\";\"VLR_AUTOMATICO\";\"FACULTAT_INTERIOR\";\"FACULTAT_EXTERIOR\";\"RIESGO\";\"ARTICULO\";\"SUBAMPARO\";\"TOMADOR\";\"PORC_RETEFTE\";\"VLR_RETEFTE\";\"CLASE_SERV_CONTRAP\";\"NOMB_BENEFICIARIO_B\";\"CED_BENEFICIARIO_B\";\"RETENEDOR\";\"RESPONSABLE_IVA\";\"BASE_IVA\";\"PORC_IVA\";\"VLR_IVA\";\"PORC_IVA_SURA\";\"TOTAL_IVA_SURA\";\"NRO_CHEQUE\";\"COD_BANCO\";\"CUENTA_BANCO\";\"VLR_CHEQUE\";\"NRO_PAGARE\";\"CUOTAS_PAGARE\";\"FEC_VENC_PAG\";\"VLR_PAGARE\";\"CAUSA\";\"PLAN_B\";\"RESUL\";\"CDCIA_MATRIZ\";\"CDPAIS\";\"SNCORE\";\"PTTASA_CAMBIO\";\"POPARTLIDER\";\"PARTICIPATION\"";
  String coasegSapHeader = "St;Clave ref.2;Lib.mayor;Asignaci¢n;Clase;Texto;N§ doc.;NIT;Cta.mayor;Cta.alt.;Clave ref.1;Referencia;Importe en MD;Mon.;Importe en ML;Fe.contab.;Fecha doc.";
  boolean runProcess = false;
  private Thread h1;  
  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
  DataBaseController conection = new DataBaseController();
  HashMap<ClosureTypeEnum,String> searchCriteria = null;
  int currentItemNumber = 0;//cuantos items ha finalizado en el proceso actual 
  int totalItemNumber = 0;//total de items en el proceso actual  
  int rowCountGW = 0;
  String limitInconsistencies ="-"; //desde que valor se considera inconsistenca
  String lastRowInfo="";
    
  public DialogCoinsuranceClosure(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    btnStart.setEnabled(false);
    spinnerLimit.setValue(1);
  }  
 
  @Override
  public void run() {        
    try {            
      if(runProcess) {
        progressTotal(100,0);    
        progressProcess(100,0);    
        btnStart.setEnabled(false);
        btnSelectFileGW.setEnabled(false);
        printInOutputText("\nINICIO: " + sdf.format(new Date()));
        limitInconsistencies = spinnerLimit.getValue().toString();            
        
        printInOutputText("\nValidando archivos...");          
        IncidentsUtil.validateFile(rutaCarpeta,coasegEcoFileName,coasegEcoHeader);
        IncidentsUtil.validateFile(rutaCarpeta,coasegSapFileName,coasegSapHeader);
        progressTotal(100,5); 
        
        printInOutputText("\nLimpiando tablas...");          
        regenerateTableClosure();                   
        progressTotal(100,10);        
        
        printInOutputText("\nCargando Archivo COASEGURO_ECO.csv...");        
        readAndInsertRows("ECO",rutaCarpeta+"\\"+coasegEcoFileName); 
        progressTotal(100,20);    
                
        printInOutputText("\nCargando Archivo COASEGURO_SAP.csv ...");        
        readAndInsertRows("SAP",rutaCarpeta+"\\"+coasegSapFileName); 
        progressTotal(100,30);    
        
        printInOutputText("\nCreando acumulados...");                
        calculateAvgTotals();//Se determina los acumulados
        progressTotal(100,50); 
        
        searchInconsistencies();        
                
        printInOutputText("\nEliminando temporales...");          
        regenerateTableClosure();//eliminar datos              
        progressTotal(100,100);    

        printInOutputText("\nFIN: " + sdf.format(new Date()));
        printInOutputText("\nFINALIZO CORRECTAMENTE");     
      } 
    } catch (FileNotFoundException e1) {      
      printInOutputText("\nFileNotFoundException: " + e1.toString() + "\n"+lastRowInfo);
    } catch (IOException e2) {
      printInOutputText("\nIOException: " + e2.toString() + "\n"+lastRowInfo);
    } catch (SQLException ex) { 
      printInOutputText("\nSQLException: " + ex.toString() + "\n"+lastRowInfo);
    } catch (Exception e3) {
      printInOutputText("\nException: " + e3.toString() + "\n"+lastRowInfo);
    }
    
    progressTotal(100,100);
    progressProcess(100,100);
    runProcess=false;
  }
  
  private void printInOutputText(String textToAdd){
    outputTxt.setText(outputTxt.getText() + textToAdd);
    outputTxt.setCaretPosition(outputTxt.getDocument().getLength());
  }
  
  public void readAndInsertRows(String origen,String fileUrl) throws FileNotFoundException, IOException, Exception    {    
    File aFile = new File(fileUrl);            
    FileReader fr = new FileReader (aFile);
    BufferedReader br = new BufferedReader(fr);      
    currentItemNumber=0;
    int rowNum=(int)(aFile.length()/350);//350 bytes es el promedio del tamaño de una linea
    String lineaStr=br.readLine();//la primer linea es cabecera 
    switch(origen){
      case "ECO":  //al tiempo ingresa ECO y SALVAM_ECO                     
        while((lineaStr = br.readLine())!=null){
          insertRowInDB(lineaStr);        
          progressProcess(rowNum, ++currentItemNumber);
        }
        break;    
      case "SAP":          
        while((lineaStr = br.readLine())!=null){
          updateRowInDB("SAP","ECO",lineaStr);
          progressProcess(rowNum, ++currentItemNumber);
        }                
        break;
    }           
    progressProcess(100,0);
    fr.close();
    br.close();
  }
  
  private void insertRowInDB(String rowInfo) throws Exception  {   
    lastRowInfo = rowInfo; 
    String[] rowInfoSplit = rowInfo.split(";");        
    String documento      = rowInfoSplit[9].replaceAll("\"", "").toLowerCase();
    String siniestro      = rowInfoSplit[5].replaceAll("\"", "");
    String poliza         = rowInfoSplit[3].replaceAll("\"", "");
    String ramo           = rowInfoSplit[2].replaceAll("\"", "");   
    String moneda         = rowInfoSplit[53].replaceAll("\"", "");
    String participacion  = rowInfoSplit[105].replaceAll("\"", "");
    String pttasacambio   = rowInfoSplit[103].replaceAll("\"", "");    
    
    searchCriteria = new HashMap<>();
    searchCriteria.put(ClosureTypeEnum.referencia,documento); 
    searchCriteria.put(ClosureTypeEnum.moneda,moneda);     
    List<Closure> aClosureList = closureController.findBySearchCriteria(searchCriteria);
    if(aClosureList.isEmpty()){//Se crea nuevo registro      
      Closure newClosure = new Closure(); 
      newClosure.setOrigen("ECO");
      newClosure.setReferencia(documento); 
      newClosure.setMoneda(moneda);               //AQUI APARECE 1 o 0 PREGUNTAR
      newClosure.setClaimnumber(siniestro);
      newClosure.setPolicynumber(poliza);
      newClosure.setRamo(ramo);
      newClosure.setValorCienGw(determineDoubleValue(participacion));        //Sacar Suma de PARTICIPATION 
      newClosure.setValorReasGw(determineDoubleValue(pttasacambio));         //Sacar Promedio de PTTASA_CAMBIO 
      newClosure.setRowTxt("1");//en RowTxt se almacenara el divisor para sacar promedio a PTTASA_CAMBIO      
      
      closureController.create(newClosure);            
    }else{//se modifica registro existente
      Closure aClosure = aClosureList.get(0);//se toma el primer encontrado      
      aClosure.setValorCienGw(sumarDoubles(aClosure.getValorCienGw(),determineDoubleValue(participacion)));        //Sacar Suma de PARTICIPATION 
      aClosure.setValorReasGw(sumarDoubles(aClosure.getValorReasGw(),determineDoubleValue(pttasacambio)));         //Sacar Promedio de PTTASA_CAMBIO 
      aClosure.setRowTxt(incrementIntStr(aClosure.getRowTxt()));//en RowTxt se almacenara el divisor para sacar promedio a PTTASA_CAMBIO            
      closureController.edit(aClosure);
    }    
  }
  
  private void updateRowInDB(String origen,String destino,String rowInfo) throws Exception  {  
    //todos los registros que llegan a este metodo son de SAP
    lastRowInfo = rowInfo; 
    String[] rowInfoSplit = rowInfo.split(";");        
    String ramo         = rowInfoSplit[1];
    String referencia   = rowInfoSplit[11].toLowerCase();
    String importeML    = rowInfoSplit[14];
    String importeMD    = rowInfoSplit[12];
    String poliza       = rowInfoSplit[10];    
    String reclamacion  = rowInfoSplit[5]; 
    
    String TRM = calculateTRM(importeML,importeMD);//TRM = "importe ML" dividido entre "Importe MD"
    String ABS = calculateABS(importeMD);//Valor Absoluto de "Importe MD"
      
    searchCriteria = new HashMap<>();    //En la siguiente instruccion me queda la duda
    searchCriteria.put(ClosureTypeEnum.origen,destino);// se coloca destino por que se intenta actualizar un registro de gw 
    searchCriteria.put(ClosureTypeEnum.referencia,referencia);     
    List<Closure> aClosureList = closureController.findBySearchCriteria(searchCriteria);
    if(aClosureList.isEmpty()){//no existe en gw se crea nuevo registro      
      Closure newClosure = new Closure();
      newClosure.setOrigen(origen);//al ser nuevo el origen es de sap
      newClosure.setRamo(ramo);
      newClosure.setClaimnumber(reclamacion);
      newClosure.setPolicynumber(poliza);
      newClosure.setReferencia(referencia);       
      newClosure.setValorCienSap(determineDoubleValue(ABS));      //sacar suma     de ABS
      newClosure.setValorReasSap(determineDoubleValue(TRM));      //Sacar promedio de TRM
      newClosure.setEstado("1");//en Estado se almacena divisor para sacar promedio a TRM            
      closureController.create(newClosure);            
    }else{//se modifica registro de gw existente
      Closure aClosure = aClosureList.get(0);//se toma el primer encontrado      
      aClosure.setValorCienSap(sumarDoubles(aClosure.getValorCienSap(),determineDoubleValue(ABS)));      //sacar suma     de ABS
      aClosure.setValorReasSap(sumarDoubles(aClosure.getValorReasSap(),determineDoubleValue(TRM)));      //Sacar promedio de TRM
      aClosure.setEstado(incrementIntStr(aClosure.getEstado()));//en Estado se almacena divisor para sacar promedio a TRM            
      closureController.edit(aClosure);
    }    
  }  
  
  private void searchInconsistencies() throws IOException, SQLException, ClassNotFoundException{    
    String rutaResult = IncidentsUtil.determineUrl(rutaCarpeta,"CruceCoaseguro");
    File file = new File(rutaResult);                
    if (!file.exists()) {// Si el archivo no existe es creado
        file.createNewFile();
    }
    FileWriter fw = new FileWriter(file);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write( "\"TIPO ERROR\";\"DOCUMENTO\";\"NRO_SINIESTRO\";\"POLIZA\";"
            + "\"RAMO_A\";\"SUM PART\";\"AVG TASA\";"
            + "\"ABS SAP\";\"TRM SAP\";\"DIF ABS\";\"DIF TRM\";\"DETALLE\";\n");
    
    insertInconsistencyInFile(bw,"ECO",ClosureTypeEnum.faltanteEnSAP);
    insertInconsistencyInFile(bw,"ECO",ClosureTypeEnum.AbsMayorQue);
    insertInconsistencyInFile(bw,"ECO",ClosureTypeEnum.AbsMenorQue);
    insertInconsistencyInFile(bw,"ECO",ClosureTypeEnum.TrmMenorQue);
    insertInconsistencyInFile(bw,"ECO",ClosureTypeEnum.TrmMayorQue);
    
    insertInconsistencyInFile(bw,"SAP",ClosureTypeEnum.faltanteEnECO);    
    insertInconsistencyInFile(bw,"SAP",ClosureTypeEnum.AbsMayorQue);
    insertInconsistencyInFile(bw,"SAP",ClosureTypeEnum.AbsMenorQue);
    insertInconsistencyInFile(bw,"SAP",ClosureTypeEnum.TrmMenorQue);
    insertInconsistencyInFile(bw,"SAP",ClosureTypeEnum.TrmMayorQue);    
    bw.close();
    fw.close();
  }
  
  private void insertInconsistencyInFile(BufferedWriter bw, String origen, 
          ClosureTypeEnum criterio) throws IOException{    
    printInOutputText("\nAnalizando  - " + criterio.name());
    currentItemNumber=0;                    
    List<Closure> resultList=searchInconsistency(origen,criterio,limitInconsistencies);              
    String strRow = "";
    for(Closure aClosure : resultList) {  
      lastRowInfo = IncidentsUtil.determineRowInfo(aClosure);
      strRow = "\"REF DE " + origen + " CON " + criterio.name() + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getReferencia())   + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getClaimnumber())  + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getPolicynumber()) + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getRamo())         + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getValorCienGw())  + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getValorReasGw())  + "\";\"" +  
              IncidentsUtil.determineStringValue(aClosure.getValorCienSap()) + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getValorReasSap()) + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getDiferCien())    + "\";\"" + 
              IncidentsUtil.determineStringValue(aClosure.getDiferReas())    + "\";\n";
      bw.write(strRow);
      progressProcess(resultList.size(), ++currentItemNumber);
    }    
  }
  
  private List<Closure> searchInconsistency(String origen,ClosureTypeEnum typeDif,String limit) {    
    searchCriteria = new HashMap<>();
    searchCriteria.put(ClosureTypeEnum.origen,origen);     
    searchCriteria.put(typeDif,limit);    
    return closureController.findBySearchCriteria(searchCriteria);        
  }
  
  private String incrementIntStr(String aValue) {
    if(aValue==null){
      return "1";
    }
    int result = Integer.parseInt(aValue);
    result = result + 1;
    return String.valueOf(result);
  }
  
  private Double determineDoubleValue(String aValue) {
    return aValue.length() == 0 ? null: Double.parseDouble(aValue);
  }
  
  private String calculateTRM(String importeML, String importeMD) {
    double resultDouble = Double.parseDouble(importeML) / Double.parseDouble(importeMD);
    return String.valueOf(resultDouble);
  }
  
  private String calculateABS(String importeMD) {    
    double resultDouble = Math.abs(Double.parseDouble(importeMD));
    return String.valueOf(resultDouble);
  }
  
  public void calculateAvgTotals() throws Exception {
    //calcula el promedio de los registros    
    List<Closure> aClosureList = closureController.findClosureEntities();
    for(Closure aClosure : aClosureList){            
      if(aClosure.getValorReasGw() != null){
        aClosure.setValorReasGw(dividirDoubles(aClosure.getValorReasGw(),determineDoubleValue(aClosure.getRowTxt())));
      }
      if(aClosure.getValorReasSap() != null){
        aClosure.setValorReasSap(dividirDoubles(aClosure.getValorReasSap(),determineDoubleValue(aClosure.getEstado())));
      }      
      
      aClosure.setDiferCien(restarDoubles(aClosure.getValorCienGw(),aClosure.getValorCienSap()));
      aClosure.setDiferReas(restarDoubles(aClosure.getValorReasGw(),aClosure.getValorReasSap()));
      closureController.edit(aClosure); 
    }
  }
  
  private Double dividirDoubles(Double first,Double second){
    if(first == null || second == null) {
      return null;
    }    
    return first / second;
  }  
  
  private Double restarDoubles(Double first,Double second){
    if(first == null && second == null) {
      return null;
    }
    if(first == null ) {
      return second * -1;
    }
    if(second == null ) {
      return first;
    }    
    return first - second;
  }  
  
  private Double sumarDoubles(Double first,Double second){
    if(first == null && second == null) {
      return null;
    }
    if(first == null ) {
      return second;
    }
    if(second == null ) {
      return first;
    }    
    return first + second;
  }
  
  private double calculateSUM(String[] strValues) {
    double result=0;
    for(String value : strValues){
      result = result + Double.parseDouble(value);
    }    
    return result;
  }
  
  public double calculateAVG(String[] strValues){
    if(strValues.length==0){
      return 0;
    }
    return calculateSUM(strValues) / strValues.length;
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
    
  private void activeBtnStart(){
    if(labelFileGW.getText().length()!=0 ){
      btnStart.setEnabled(true);
    } else {
      btnStart.setEnabled(false);
    }
  }
    
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    progressBar = new javax.swing.JProgressBar();
    btnSelectFileGW = new javax.swing.JButton();
    btnStart = new javax.swing.JButton();
    labelFileGW = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    outputTxt = new javax.swing.JTextArea();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    progressBarProceso = new javax.swing.JProgressBar();
    jLabel1 = new javax.swing.JLabel();
    spinnerLimit = new javax.swing.JSpinner();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("CIERRE DE COASEGURO");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    progressBar.setStringPainted(true);

    btnSelectFileGW.setText("Seleccionar");
    btnSelectFileGW.setActionCommand("Seleccionar");
    btnSelectFileGW.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    btnSelectFileGW.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelectFileGWActionPerformed(evt);
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

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel3.setText("PROCESO ACTUAL: ");

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel4.setText("PROGRESO TOTAL: ");

    progressBarProceso.setStringPainted(true);

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel1.setText("Limite Monetario");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
              .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(spinnerLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSelectFileGW)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFileGW, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(progressBarProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
              .addComponent(progressBarProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(spinnerLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(btnSelectFileGW)))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelFileGW, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
          .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        .addContainerGap())
    );

    btnStart.getAccessibleContext().setAccessibleName("btnStart");
    btnStart.getAccessibleContext().setAccessibleDescription("");

    setSize(new java.awt.Dimension(652, 549));
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void btnSelectFileGWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFileGWActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(fileChooser)==JFileChooser.APPROVE_OPTION) {      
      rutaCarpeta = fileChooser.getSelectedFile().getAbsolutePath();
      labelFileGW.setText(rutaCarpeta);            
    }   
    activeBtnStart();
  }//GEN-LAST:event_btnSelectFileGWActionPerformed
  
  private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
    progressTotal(100,0);       
    runProcess = true;
    h1 = new Thread(this);
    h1.start();
  }//GEN-LAST:event_btnStartActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    //closeExcelFile();
  }//GEN-LAST:event_formWindowClosing
 
  /**
   * @param args the command line arguments
   */
  @SuppressWarnings("Convert2Lambda")
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
      java.util.logging.Logger.getLogger(DialogCoinsuranceClosure.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    
    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        DialogCoinsuranceClosure dialog = new DialogCoinsuranceClosure(new javax.swing.JFrame(), true);
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
  private javax.swing.JButton btnSelectFileGW;
  private javax.swing.JButton btnStart;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labelFileGW;
  private javax.swing.JTextArea outputTxt;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JProgressBar progressBarProceso;
  private javax.swing.JSpinner spinnerLimit;
  // End of variables declaration//GEN-END:variables

  

  

  
}