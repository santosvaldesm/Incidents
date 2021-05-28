package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Closure;
import com.mycompany.incidents.jpaControllers.ClosureJpaController;
import com.mycompany.incidents.otherResources.ClosureTypeEnum;
import com.mycompany.incidents.otherResources.DataBaseController;
import com.mycompany.incidents.otherResources.IncidentsUtil;
import java.io.BufferedReader;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DialogReinsuranceClosure extends javax.swing.JDialog implements Runnable {
  
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
  ClosureJpaController closureController = new ClosureJpaController(factory);
  Closure currentClosure = null;
  String rutaCarpeta = "";  
  String nombreArchivoECO = "REASEGURO_ECO.csv";  
  String nombreArchivoGW = "REASEGURO_GW.csv";  
  HashMap<ClosureTypeEnum,String> searchCriteria = null;
  boolean runProcess = false;
  private Thread h1;
  String headerGW = "\"ID\";\"POLICYNUMBER\";\"CLAIMNUMBER\";\"ACCOUNTING_BRANCH\";\"CDTRANSACCION\";\"PUBLICID\";\"REFLECTIONID\";\"CREATETIME\";\"AGREEMENTNUMBER\";\"SUBTYPE\";\"REINSURER\";\"PARTICIPATIONPERCENTAGE\";\"PARTICIPATIONAMOUNT\";\"DEPOSITRATE\";\"INTERESTRATE\";\"COMMISSIONRATE\";\"COMMISSIONADJUSTMENTRATE\";\"TIPO\";\"REFERENCESTATUS\";\"TIPOTRANSACCION\";\"ACCOUNTINGDATE\";\"BULKED\";\"REINSURERCODE\";\"BROKERNAME\";\"BROKERCODE\";\"ISREFLECTION\";\"TIPORA\";\"TIPOMOVIMIENTO\";\"MONEDA\";\"RF_STATUS\"";
  String headerECO = "\"ID\";\"ROW_ID\";\"CDTRANSACCION\";\"CDAPLICACION\";\"FECIERRE\";\"CDRAMO\";\"CDPLAN\";\"CDMONEDA\";\"CDGARANTIA\";\"CDTIPO_POLIZA\";\"CDCONTRATO\";\"POCOMISION\";\"PODEPOSITO\";\"POINTERES\";\"POAJUSTE_COMISION\";\"POIMPUESTO\";\"PTGASTOS_VARIOS\";\"OPTIPO_VALOR\";\"CDTIPO_MOVIMIENTO\";\"PTVALOR_MOVIMIENTO\";\"PTVALOR_MOV_CEDIDO\";\"SNPROPORCIONAL\";\"SNFACULTATIVO\";\"CDCIA_SURA\";\"CDTIPO_CONTRATO\";\"OPTIPO_REASEGURO\";\"NMPOLIZA\";\"CDTOMADOR\";\"FEINI_VIGENCIA\";\"FEFIN_VIGENCIA\";\"CDREASEGURADOR\";\"SNCORREDOR\";\"CDCORREDOR\";\"TIPORA\";\"TIPOMOVIMIENTO\";\"MONEDA\";\"NMSINIESTRO\";\"NMRECIBO\"";
  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
  DataBaseController conection = new DataBaseController();
  int currentItemNumber = 0;//cuantos items ha finalizado en el proceso actual 
  int totalItemNumber = 0;//total de items en el proceso actual  
	XSSFCellStyle headerStyle;
	XSSFCellStyle cellStyle;
	XSSFCellStyle cellStyleGray;
    
  public DialogReinsuranceClosure(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    btnStart.setEnabled(false);    
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
        printInOutputText("\nValidando archivos...");          
        IncidentsUtil.validateFile(rutaCarpeta,nombreArchivoECO,headerECO);
        IncidentsUtil.validateFile(rutaCarpeta,nombreArchivoGW,headerGW);        
        progressTotal(100,5); 
        
        printInOutputText("\nLimpiando tablas...");                  
        regenerateTableClosure();                   
        progressTotal(100,10);  
             
        printInOutputText("\nCargando Archivo GW...");        
        readAndInsertGwRows(); 
        progressTotal(100,40);    
        
        printInOutputText("\nCarganto archivo ECO...");        
        readAndInsertEcoRows(); 
        progressTotal(100,70); 
        
        searchInconsistencies();        
        
        printInOutputText("\nEliminando temporales...");          
        regenerateTableClosure();//eliminar datos              
        progressTotal(100,100);    

        printInOutputText("\nFIN: " + sdf.format(new Date()));
        printInOutputText("\nFINALIZO CORRECTAMENTE");                
      } 
    } catch (FileNotFoundException e1) {      
      printInOutputText("\nFileNotFoundException: " + e1.toString());
    } catch (IOException e2) {
      printInOutputText("\nIOException: " + e2.toString());
    } catch (SQLException ex) { 
      printInOutputText("\nSQLException: " + ex.toString());
    } catch (Exception e3) {
      printInOutputText("\nException: " + e3.toString());
    }
    progressTotal(100,100);
    progressProcess(100,100);
    runProcess=false;
  }
  
   private void printInOutputText(String textToAdd) {
    outputTxt.setText(outputTxt.getText() + textToAdd);
    outputTxt.setCaretPosition(outputTxt.getDocument().getLength());
  }
   
  public void readAndInsertGwRows() throws FileNotFoundException, IOException, Exception {    
      //Se insertan los registros de GW
      String fileUrl= rutaCarpeta + "\\" + nombreArchivoGW;      
      File archivo = new File (fileUrl);
      FileReader fr = new FileReader (archivo);
      BufferedReader br = new BufferedReader(fr);      
      currentItemNumber=0;
      String lineaStr=br.readLine();//la primer linea es cabecera                  
      int rowNum=(int)(archivo.length()/350);//350 bytes es el promedio del tamaño de una linea
      while((lineaStr = br.readLine())!=null){
        insertGwInfoInDB(lineaStr);        
        progressProcess(rowNum, ++currentItemNumber);
      }                 
      progressProcess(100,0);
      fr.close();                                          
  }
  
  public void readAndInsertEcoRows() throws FileNotFoundException, IOException, Exception {    
      //Se insertan los registros de GW
      String fileUrl= rutaCarpeta + "\\" + nombreArchivoECO;      
      File archivo = new File (fileUrl);
      FileReader fr = new FileReader (archivo);
      BufferedReader br = new BufferedReader(fr);      
      currentItemNumber=0;
      String lineaStr=br.readLine();//la primer linea es cabecera                  
      int rowNum=(int)(archivo.length()/350);//350 bytes es el promedio del tamaño de una linea
      while((lineaStr = br.readLine())!=null){
        insertEcoInfoInDB(lineaStr);        
        progressProcess(rowNum, ++currentItemNumber);
      }                 
      progressProcess(100,0);
      fr.close();                                          
  }
  
  private void insertGwInfoInDB(String rowInfo)  {   
    String[] rowInfoSplit = rowInfo.split(";");    
    String claimNumber         =  rowInfoSplit[2].replaceAll("\"", "");
    String policyNumber        = rowInfoSplit[1].replaceAll("\"", "");
    String ramo                = rowInfoSplit[3].replaceAll("\"", "");
    String estado              = rowInfoSplit[18].replaceAll("\"", "");
    String moneda              = rowInfoSplit[28].replaceAll("\"", "");
    
    String tipoTransaccion     = rowInfoSplit[19].replaceAll("\"", "");
    String isReflection        = rowInfoSplit[25].replaceAll("\"", "");
    String cdTransaction       = rowInfoSplit[4].replaceAll("\"", "");
    String publicId            = rowInfoSplit[5].replaceAll("\"", "");
    String reinsuranceCode     = rowInfoSplit[22].replaceAll("\"", "");
    String reflectionId        = rowInfoSplit[6].replaceAll("\"", "");
    String participationAmount = rowInfoSplit[12].replaceAll("\"", "");
    String contrato            = rowInfoSplit[8].replaceAll("\"", "");
    String referenciaSeparada  = 
            reflectionId        + ";" + 
            cdTransaction       + "-" +     //En ECO cdTranaccion esta unida a PublicID
            publicId            + ";" +
            reinsuranceCode     + ";" +
            participationAmount + ";" +
            contrato;
    
    String claveReferencia;
    switch(tipoTransaccion){
      case "Payment":
          if(isReflection.compareTo("1")==0){  
            claveReferencia = reflectionId + "-R:" + cdTransaction + "-" + publicId + "-" + reinsuranceCode + "-" + participationAmount;
          } else {
            claveReferencia = cdTransaction + "-" + publicId + "-" + reinsuranceCode + "-" + participationAmount;
          }          
        break;
      default:
          if(isReflection.compareTo("1")==0){            
            claveReferencia = reflectionId + "-R:" + cdTransaction + "-" + reinsuranceCode + "-" + participationAmount;
          } else {
            claveReferencia = cdTransaction + "-" + reinsuranceCode + "-" + participationAmount;
          }
        break;        
    }    
    Closure newClosure = new Closure();      
    newClosure.setOrigen("GW");
    newClosure.setClaimnumber(claimNumber);
    newClosure.setPolicynumber(policyNumber);
    newClosure.setRamo(ramo);
    newClosure.setEstado(estado);
    newClosure.setMoneda(moneda);    
    newClosure.setReferencia(claveReferencia);    
    newClosure.setRowTxt(referenciaSeparada); 
    
    closureController.create(newClosure);            
  }
  
  private void insertEcoInfoInDB(String rowInfo) throws Exception  {  
    //todos los registros que llegan a este metodo son de ECO
    String[] rowInfoSplit = rowInfo.split(";");  
    
    String claimNumber         =  rowInfoSplit[36].replaceAll("\"", "");
    String policyNumber        = rowInfoSplit[26].replaceAll("\"", "");
    String ramo                = rowInfoSplit[5].replaceAll("\"", "");
    String estado              = "#N/A";
    String moneda              = rowInfoSplit[35].replaceAll("\"", "");
    
    String cdTransaction       = rowInfoSplit[2].replaceAll("\"", "");
    String reinsuranceCode     = rowInfoSplit[30].replaceAll("\"", "");
    String participationAmount = rowInfoSplit[19].replaceAll("\"", "");
    String contrato            = rowInfoSplit[10].replaceAll("\"", "");
    String referenciaSeparada  =      ";" + //reflectionId no se si se pueda sacar de ecosistema
            cdTransaction           + ";" + //en ECO transaction y publicId van juntos            
            reinsuranceCode         + ";" +
            participationAmount     + ";" +
            contrato; 
    String claveReferencia = cdTransaction + "-" + reinsuranceCode + "-" + participationAmount;           
    searchCriteria = new HashMap<>();
    searchCriteria.put(ClosureTypeEnum.referencia,claveReferencia);
    List<Closure> resultList = closureController.findBySearchCriteria(searchCriteria);            
    if(resultList.isEmpty()){      
      Closure newClosure = new Closure();
      newClosure.setOrigen("ECO");//al ser nuevo el origen es de ECO
      newClosure.setClaimnumber(claimNumber);
      newClosure.setPolicynumber(policyNumber);
      newClosure.setRamo(ramo);
      newClosure.setEstado(estado);
      newClosure.setMoneda(moneda);      
      newClosure.setReferencia(claveReferencia);       
      newClosure.setRowTxt(referenciaSeparada);                   
      closureController.create(newClosure);            
    }else{//se modifica registro de gw existente
      for(Closure aClosure : resultList){      
        aClosure.setValorCienGw(new Double(resultList.size()));//numero veces registro ECO esta en GW
        aClosure.setValorCienSap(incrementValue(aClosure.getValorCienSap()));//numero veces registro GW esta en ECO
        closureController.edit(aClosure);
      }
    }    
  }
  
  private Double incrementValue(Double aValue){
    return aValue == null ? 1d : (aValue + 1d); 
  }
  
  
  private void searchInconsistencies() throws IOException, SQLException, ClassNotFoundException{
    String rutaResult = IncidentsUtil.determineUrl(rutaCarpeta,"CruceReaseguro", ".xlsx");
		int rowPosition = 0;
    XSSFWorkbook anExcelWorbook = new XSSFWorkbook();		
		createStyles(anExcelWorbook);
		XSSFSheet sheetInconsistency = anExcelWorbook.createSheet("Inconsistency");
		String[] headerReinsurance = new String[]{"TIPO ERROR","SINIESTRO","POLIZA","RAMO","ESTADO",
            "MONEDA","REFLECTION_ID","CD_TRANSACTION","REINSURANCE_CODE",
            "PARTICIPATION_AMOUNT","CONTRATO","REFERENCIA","DETALLE"};
		insertHeader(sheetInconsistency,headerReinsurance,rowPosition++);
		
		if(checkGwNoEstaEco.isSelected()){
      rowPosition = insertInconsistencyInFile(sheetInconsistency,"GW",ClosureTypeEnum.NoEncontradaEnEco,rowPosition);
    }
    if(checkEcoNoEstaGw.isSelected()){
      insertInconsistencyInFile(sheetInconsistency,"ECO",ClosureTypeEnum.NoEncontradaEnGw,rowPosition);    
    }    
    
		FileOutputStream file = new FileOutputStream(rutaResult);
		anExcelWorbook.write(file);
		file.close();
  }
	
	private void insertHeader(XSSFSheet aSheet, String[] headers,int rowPosition){
		XSSFRow headerRow = aSheet.createRow(rowPosition++);		
		for (int i = 0; i < headers.length; ++i) {			
			IncidentsUtil.createCellInRow(headerRow,i,headers[i],headerStyle);			
		}		
	}
	
	private void createStyles(XSSFWorkbook anExcelWorbook){
		headerStyle = IncidentsUtil.createHeaderStyle(anExcelWorbook);
	  cellStyle = IncidentsUtil.createCellStyle(anExcelWorbook);		
	  cellStyleGray = IncidentsUtil.createCellStyleGray(anExcelWorbook);		
	}
  
  private int insertInconsistencyInFile(XSSFSheet aSheet, String origen, 
                                    ClosureTypeEnum criterio, int rowPosition) {
    printInOutputText("\nAnalizando  - " + criterio.name());
    currentItemNumber=0;
    List<Closure> resultList=searchInconsistency(origen,criterio);
    for(Closure aClosure : resultList) {
			int numcol = 0;
			XSSFRow newRow = aSheet.createRow(rowPosition++);
			IncidentsUtil.createCellInRow(newRow,numcol++,"REF DE " + origen + " " + criterio.name(),cellStyleGray);
			IncidentsUtil.createCellInRow(newRow,numcol++,aClosure.getClaimnumber(),cellStyle);
			IncidentsUtil.createCellInRow(newRow,numcol++,aClosure.getPolicynumber(),cellStyle);
			IncidentsUtil.createCellInRow(newRow,numcol++,aClosure.getRamo(),cellStyle);
			IncidentsUtil.createCellInRow(newRow,numcol++,aClosure.getEstado(),cellStyle);
			IncidentsUtil.createCellInRow(newRow,numcol++,aClosure.getMoneda(),cellStyle);
			String[] rowTxtSplit = aClosure.getRowTxt().split(";");
			for(String value : rowTxtSplit) {
			  IncidentsUtil.createCellInRow(newRow,numcol++,value,cellStyle);
			}
			IncidentsUtil.createCellInRow(newRow,11,aClosure.getReferencia(),cellStyle);
      progressProcess(resultList.size(), ++currentItemNumber);
    }    
		return rowPosition;
  }   
  
  private List<Closure> searchInconsistency(String origen,ClosureTypeEnum typeDif) {    
    searchCriteria = new HashMap<>();
    searchCriteria.put(ClosureTypeEnum.origen,origen);     
    searchCriteria.put(typeDif,"1");       
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
    checkGwNoEstaEco = new javax.swing.JCheckBox();
    checkEcoNoEstaGw = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("CIERRE DE REASEGURO");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    progressBar.setStringPainted(true);

    btnSelectFileGW.setText("CARPETA");
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

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel3.setText("PROCESO ACTUAL: ");

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel4.setText("PROGRESO TOTAL: ");

    progressBarProceso.setStringPainted(true);

    checkGwNoEstaEco.setSelected(true);
    checkGwNoEstaEco.setText("Ref GW no esta ECO");
    checkGwNoEstaEco.setToolTipText("");
    checkGwNoEstaEco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        checkGwNoEstaEcoActionPerformed(evt);
      }
    });

    checkEcoNoEstaGw.setSelected(true);
    checkEcoNoEstaGw.setText("Ref ECO no esta GW");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnSelectFileGW, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(progressBarProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(checkGwNoEstaEco, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(checkEcoNoEstaGw, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
              .addComponent(labelFileGW, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(6, 6, 6)
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
              .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(checkGwNoEstaEco))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(checkEcoNoEstaGw)
              .addComponent(progressBarProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(btnSelectFileGW)
              .addComponent(labelFileGW, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
        .addContainerGap())
    );

    btnStart.getAccessibleContext().setAccessibleName("btnStart");
    btnStart.getAccessibleContext().setAccessibleDescription("");

    setSize(new java.awt.Dimension(742, 549));
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

  private void checkGwNoEstaEcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkGwNoEstaEcoActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_checkGwNoEstaEcoActionPerformed
 
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
      java.util.logging.Logger.getLogger(DialogReinsuranceClosure.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    
    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        DialogReinsuranceClosure dialog = new DialogReinsuranceClosure(new javax.swing.JFrame(), true);
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
  private javax.swing.JCheckBox checkEcoNoEstaGw;
  private javax.swing.JCheckBox checkGwNoEstaEco;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labelFileGW;
  private javax.swing.JTextArea outputTxt;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JProgressBar progressBarProceso;
  // End of variables declaration//GEN-END:variables

  
}