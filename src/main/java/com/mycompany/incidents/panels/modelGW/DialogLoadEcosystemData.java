package com.mycompany.incidents.panels.modelGW;

//import com.mycompany.incidents.entities.GwHomologationsCoreGeneral;
//import com.mycompany.incidents.jpaControllers.GwHomologationsCoreGeneralJpaController;
import com.mycompany.incidents.entities.GwEcosystemCoverages;
import com.mycompany.incidents.jpaControllers.GwHomologCoreToGeneralJpaController;
import com.mycompany.incidents.entities.GwHomologCoreToGeneral;
import com.mycompany.incidents.entities.GwHomologGeneralToCore;
import com.mycompany.incidents.jpaControllers.GwEcosystemCoveragesJpaController;
import com.mycompany.incidents.jpaControllers.GwHomologGeneralToCoreJpaController;
import com.mycompany.incidents.otherResources.DataBaseController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFileChooser;

public class DialogLoadEcosystemData extends javax.swing.JDialog implements Runnable  {

  private Thread h1;
  String ruta = "";
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");  
  GwHomologCoreToGeneralJpaController homologCoreToGeneralController = new GwHomologCoreToGeneralJpaController(factory);
  GwHomologGeneralToCoreJpaController homologGeneralToCoreController = new GwHomologGeneralToCoreJpaController(factory);
  GwEcosystemCoveragesJpaController ecosystemCoveragesJpaController = new GwEcosystemCoveragesJpaController(factory);
  
  int totalItems=0;
  int currentItem=1;
  
  public DialogLoadEcosystemData(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    totalItems = totalItems + 9000;//Homologatios Core to General
    totalItems = totalItems + 14000;//Homologatios general to Core
    totalItems = totalItems + 11200;//Ecosystem Coverages
    btnStart.setEnabled(false); 
  } 
  
    
  @Override
  public void run() {    
    if(!btnStart.isEnabled()) {
      regenerateTablesEcosystem();  
      String[] fileNames = new String[]{"core_general.csv","general_core.csv","coberturas_ecosistema.csv"};
      if(validateFileNames(fileNames)){     
        loadHomologationsCoreToGeneral(fileNames[0]);
        loadHomologationsGeneralToCore(fileNames[1]);
        loadEcosystemCoverages(fileNames[2]);
        System.out.println("EL TOTAL DE REGISTROS ES: " + currentItem);
        progreso(100,100);                    
        this.dispose();
      }
    }    
  }
  
  private boolean validateFileNames(String[] fileNames){
    for(String name : fileNames){
      File aFile = new File(ruta + name);
      if(!aFile.exists()){
        labelProcess.setText("\nERROR: No existe el archivo: " + name);
        return false;
      }
    }
    return true;
    
  }
  
  private void progreso(int total,int actual){
    if(actual%100==0){            
      double porcentaje = (actual*100)/(total);
      this.progressBar.setValue((int)porcentaje);
    }
  }  
  
  private void loadHomologationsCoreToGeneral(String fileName){
    FileReader fr = null;    
    try {      
      //FileInputStream archivo = new FileInputStream(ruta);
      FileInputStream archivo = new FileInputStream(ruta + fileName);
      BufferedReader br = new BufferedReader(new InputStreamReader(archivo, "utf-8"));
      String linea;      
      String splitRow[] = null;      
      int numColumns= br.readLine().split("\t").length;
      while((linea=br.readLine())!=null){        
        linea=linea.replaceAll("\"", "");        
        splitRow = linea.split(";");                    
        GwHomologCoreToGeneral newHomolgation = new GwHomologCoreToGeneral();                
        newHomolgation.setHomologationName(splitRow[1]);//Offering	
        newHomolgation.setSource(splitRow[4]);//LOBCode	
        newHomolgation.setTarget(splitRow[5]);//PolicyType	        
        homologCoreToGeneralController.create(newHomolgation);
        progreso(totalItems,currentItem++);        
      }        
    }
    catch(IOException e){
       labelProcess.setText("\nERROR: " + e.getMessage());
    }finally{
       try{                    
          if( null != fr ) fr.close();     
       }catch (IOException e2){ 
          labelProcess.setText("\nERROR: " + e2.getMessage());
       }
    }
  }
  
  private void loadHomologationsGeneralToCore(String fileName){
    FileReader fr = null;    
    try {      
      FileInputStream archivo = new FileInputStream(ruta + fileName);
      BufferedReader br = new BufferedReader(new InputStreamReader(archivo, "utf-8"));
      String linea;      
      String splitRow[] = null;      
      int numColumns= br.readLine().split("\t").length;
      while((linea=br.readLine())!=null){        
        linea=linea.replaceAll("\"", "");        
        splitRow = linea.split(";");                    
        GwHomologGeneralToCore newHomolgation = new GwHomologGeneralToCore();                
        newHomolgation.setHomologationName(splitRow[1]);//Offering	
        newHomolgation.setSource(splitRow[4]);//LOBCode	
        newHomolgation.setTarget(splitRow[5]);//PolicyType	        
        homologGeneralToCoreController.create(newHomolgation);
        progreso(totalItems,currentItem++);        
      }        
    }
    catch(IOException e){
       labelProcess.setText("\nERROR: " + e.getMessage());
    }finally{
       try{                    
          if( null != fr ) fr.close();     
       }catch (IOException e2){ 
          labelProcess.setText("\nERROR: " + e2.getMessage());
       }
    }
  }
  
  private String completeEcosystemCode(String code){
    String aCompleteCode="";    
    if(code.length()==3){
      return code;
    }
    if(code.length()==2){
      return "0" + code;
    }
    if(code.length()==1){
      if(code.compareTo("-")==0){
        return code;
      }else{
        return "00" + code;
      }      
    }    
    return aCompleteCode;
  }
  
  private void loadEcosystemCoverages(String fileName){
    FileReader fr = null;    
    try {      
      FileInputStream archivo = new FileInputStream(ruta + fileName);
      BufferedReader br = new BufferedReader(new InputStreamReader(archivo, "utf-8"));
      String linea;      
      String splitRow[] = null;      
      int numColumns= br.readLine().split("\t").length;
      while((linea=br.readLine())!=null){        
        linea=linea.replaceAll(";\"\";",";\"-\";");//datos vacios por guion
        linea=linea.replaceAll("\"", "");//eliminar todas las comillas dobles      
        splitRow = linea.split(";");                    
        GwEcosystemCoverages newEcosystemCoverage = new GwEcosystemCoverages();                
        newEcosystemCoverage.setRamo(completeEcosystemCode(splitRow[1]));
        newEcosystemCoverage.setSubramo(completeEcosystemCode(splitRow[2]));
        newEcosystemCoverage.setGarantia(completeEcosystemCode(splitRow[3]));
        newEcosystemCoverage.setSubgarantia(completeEcosystemCode(splitRow[4]));
        newEcosystemCoverage.setRamoContable(completeEcosystemCode(splitRow[5]));
        newEcosystemCoverage.setProduct(splitRow[6]);
        newEcosystemCoverage.setCoverageName(splitRow[7]);
        ecosystemCoveragesJpaController.create(newEcosystemCoverage);
        progreso(totalItems,currentItem++);        
      }        
    }
    catch(IOException e){
       labelProcess.setText("\nERROR: " + e.getMessage());
    }finally{
       try{                    
          if( null != fr ) fr.close();     
       }catch (IOException e2){ 
          labelProcess.setText("\nERROR: " + e2.getMessage());
       }
    }
  }
  
  private void regenerateTablesEcosystem(){               
    try {
      DataBaseController conection = new DataBaseController();
      conection.connect();      
      conection.removeTablesEcosystem();
      conection.createTablesEcosystem();
      conection.disconect();        
    } catch (SQLException | ClassNotFoundException ex) {
      Logger.getLogger(PanelMain.class.getName()).log(Level.SEVERE, null, ex);
    } 
  }
      
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    progressBar = new javax.swing.JProgressBar();
    btnStart = new javax.swing.JButton();
    btnSelectFile = new javax.swing.JButton();
    labelFile = new javax.swing.JLabel();
    labelProcess = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("CARGAR HOMOLOGACIONES");

    btnStart.setText("Iniciar");
    btnStart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnStartActionPerformed(evt);
      }
    });

    btnSelectFile.setText("Selccionar");
    btnSelectFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelectFileActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(btnSelectFile))
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
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnSelectFile)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnStart)
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(labelFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(labelProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );

    setSize(new java.awt.Dimension(725, 188));
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
    btnStart.setEnabled(false);
    h1 = new Thread(this);
    h1.start();
    
  }//GEN-LAST:event_btnStartActionPerformed

  private void btnSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFileActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    if(fileChooser.showOpenDialog(fileChooser)==JFileChooser.APPROVE_OPTION) {      
      ruta = fileChooser.getSelectedFile().getAbsolutePath();
      ruta = ruta.substring(0,ruta.lastIndexOf("\\")+1);
      labelFile.setText(ruta);  
      btnStart.setEnabled(true);
    }
  }//GEN-LAST:event_btnSelectFileActionPerformed

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
      java.util.logging.Logger.getLogger(DialogLoadEcosystemData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(DialogLoadEcosystemData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(DialogLoadEcosystemData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(DialogLoadEcosystemData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>    

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        DialogLoadEcosystemData dialog = new DialogLoadEcosystemData(new javax.swing.JFrame(), true);
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
