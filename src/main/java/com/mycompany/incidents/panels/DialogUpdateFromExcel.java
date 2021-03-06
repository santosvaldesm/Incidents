/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Entry;
import com.mycompany.incidents.entities.Incident;
import com.mycompany.incidents.jpaControllers.EntryJpaController;
import com.mycompany.incidents.jpaControllers.IncidentJpaController;
import com.mycompany.incidents.otherResources.TableController;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator; 
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DialogUpdateFromExcel extends javax.swing.JDialog implements Runnable {

  TableController aTableController = new TableController();
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
  IncidentJpaController incidentController = new IncidentJpaController(factory);
  EntryJpaController entryController = new EntryJpaController(factory);
  int txtIncidentId = 0;
  Incident currentIncident = null;
  Entry currentEntry = null;   
  String ruta = "";
	String hoja = "TI - Empresariales";        
  boolean runProcess = false;
  private Thread h1;
  
  public DialogUpdateFromExcel(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    btnStart.setEnabled(false);    
  }
  
  @Override
  public void run() {        
    if(runProcess) {        
      progress(100,0);
      try {			
        FileInputStream file = new FileInputStream(new File(ruta));
        XSSFWorkbook worbook = new XSSFWorkbook(file);// leer archivo excel			
        XSSFSheet sheet = worbook.getSheetAt(0);//obtener la hoja que se va leer			
        int numberOfRows = sheet.getLastRowNum();
        int currentRow = 0;
        DataFormatter objDefaultFormat = new DataFormatter();
        FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) worbook);      
        Iterator<Row> rowIterator = sheet.iterator();//obtener todas las filas de la hoja excel 
        
        
        Row row = null;			
        boolean first = false;
        while (rowIterator.hasNext()) {
          progress(numberOfRows,currentRow++);
          if(row == null) {
            first = true;          
          }
          row = rowIterator.next();												
          if(!first) {
            processRow(row,objDefaultFormat,objFormulaEvaluator);				
          }  
          first = false;
        }  
        this.dispose();
      } catch (FileNotFoundException e1) {      
        labelProcess.setText("\nERROR: " + e1.getMessage());
      } catch (IOException e2) {
        labelProcess.setText("\nERROR: " + e2.getMessage());
      } catch (Exception e3) {
        labelProcess.setText("\nERROR: " + e3.getMessage());
      }
    } 
    runProcess=false;
  }

  private void progress(int total,int actual){
    if(total==0){
      total=1;
    }
    double porcentaje=0;
    porcentaje=(actual*100)/total;
    this.progressBar.setValue((int)porcentaje);
  }  

  private int processRow(Row row, DataFormatter objDefaultFormat, FormulaEvaluator objFormulaEvaluator) throws Exception {
    
    String code = getCellString(row.getCell(0),objFormulaEvaluator,objDefaultFormat);
    if(code == null || code.trim().length()==0){
      return 0;
    }
    currentIncident = incidentController.findIncidentByIncidentCode(code);
    if(currentIncident!=null){
      //processResult = processResult + "ACTUALIZAR: " + currentIncident.getId() + " - " + currentIncident.getCode() + "\n";      
      String txtStatus=getCellString(row.getCell(1),objFormulaEvaluator,objDefaultFormat);      
      currentIncident.setStatus(validateDifference(currentIncident.getStatus(),txtStatus));      
      String txtAssigned = getCellString(row.getCell(2),objFormulaEvaluator,objDefaultFormat);
      currentIncident.setAssigned(validateDifference(currentIncident.getAssigned(),txtAssigned));      
      String txtDescription = getCellString(row.getCell(3),objFormulaEvaluator,objDefaultFormat);
      currentIncident.setDescription(validateDifference(currentIncident.getDescription(),txtDescription));      
      String entryTxt = getCellString(row.getCell(4),objFormulaEvaluator,objDefaultFormat);            
      String txtAffected = getCellString(row.getCell(5),objFormulaEvaluator,objDefaultFormat);
      currentIncident.setAffected(validateDifference(currentIncident.getAffected(),txtAffected));      
      try {      
        incidentController.edit(currentIncident);
      } catch (Exception ex) {
        throw new Exception("\nERROR: " + ex.getMessage());
      }
      
      validateEntry(currentIncident,entryTxt);
    }
    else{
      //processResult = processResult + "CREAR : " + code + "\n";
      Incident newIncident = new Incident();
      newIncident.setCode(code);
      newIncident.setStatus(getCellString(row.getCell(1),objFormulaEvaluator,objDefaultFormat));
      newIncident.setAssigned(getCellString(row.getCell(2),objFormulaEvaluator,objDefaultFormat));
      newIncident.setDescription(getCellString(row.getCell(3),objFormulaEvaluator,objDefaultFormat));
      String entryTxt = getCellString(row.getCell(4),objFormulaEvaluator,objDefaultFormat);      
      newIncident.setAffected(getCellString(row.getCell(5),objFormulaEvaluator,objDefaultFormat));
      incidentController.create(newIncident);       
      validateEntry(newIncident,entryTxt);
    }
    return 1;
  }
  
  private int validateEntry(Incident anIncident, String entryTxt) {    
    
    if(entryTxt.trim().length()==0) {
      return 0;
    }
    
    Entry newEntry = new Entry();
    newEntry.setEntrydate(new Date());
    newEntry.setEntry(entryTxt);        
    newEntry.setIncident(anIncident); 
    
    if(anIncident.getEntryList().isEmpty()){             
      entryController.create(newEntry);   
    }else{//se busca la ultima y se compara si es igual a la nueva entrada     
      if(createLastEntry(anIncident,entryTxt) != null){        
        entryController.create(newEntry);
      }
    }
    return 1;
  }
  
  private String createLastEntry(Incident anIncident, String entryTxt) {    
    boolean exists = false;
    if(entryTxt.trim().length()==0){
      return null;//no ingresar por que no hay nueva entrada
    }       
    for(Entry anEntry : anIncident.getEntryList()){      
      if(anEntry.getEntry().compareTo(entryTxt)==0){
        return null;//no ingresar por que ya existe
      }
    }        
    return entryTxt;//ingresar por que es una nueva entrada(o se modifico)
    
  }
  
  private String validateDifference(String currentValue, String newValue) {
    if(newValue.trim().length()!=0){
      return newValue;
    }else{
      return currentValue;
    }
  }
  
  private String getCellString(Cell cell, FormulaEvaluator objFormulaEvaluator, DataFormatter objDefaultFormat){
    objFormulaEvaluator.evaluate(cell); // This will evaluate the cell, And any type of cell will return string value
    return objDefaultFormat.formatCellValue(cell,objFormulaEvaluator);          
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    progressBar = new javax.swing.JProgressBar();
    labelProcess = new javax.swing.JLabel();
    btnSelectFile = new javax.swing.JButton();
    btnStart = new javax.swing.JButton();
    labelFile = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Cargar incidentes desde archivo Excel");

    btnSelectFile.setText("Selccionar");
    btnSelectFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelectFileActionPerformed(evt);
      }
    });

    btnStart.setText("Inciar");
    btnStart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnStartActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(btnStart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnSelectFile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(labelProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(labelFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnSelectFile)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnStart))
          .addGroup(layout.createSequentialGroup()
            .addComponent(labelFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(labelProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    setSize(new java.awt.Dimension(622, 188));
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

  private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
    btnStart.setEnabled(false);
    btnSelectFile.setEnabled(false);
    runProcess = true;
    h1 = new Thread(this);
    h1.start();
  }//GEN-LAST:event_btnStartActionPerformed

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
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(DialogUpdateFromExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(DialogUpdateFromExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(DialogUpdateFromExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(DialogUpdateFromExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        DialogUpdateFromExcel dialog = new DialogUpdateFromExcel(new javax.swing.JFrame(), true);
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
  private javax.swing.JLabel labelFile;
  private javax.swing.JLabel labelProcess;
  private javax.swing.JProgressBar progressBar;
  // End of variables declaration//GEN-END:variables
}
