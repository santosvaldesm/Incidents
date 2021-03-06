package com.mycompany.incidents.panels.modelGW;

import com.mycompany.incidents.entities.GwLobModel;
import com.mycompany.incidents.entities.GwTypeCode;
import com.mycompany.incidents.jpaControllers.GwLobModelJpaController;
import com.mycompany.incidents.jpaControllers.GwTypeCodeJpaController;
import com.mycompany.incidents.otherResources.DataBaseController;
import com.mycompany.incidents.otherResources.TypeKeysEnum;
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

public class DialogLoadModel extends javax.swing.JDialog implements Runnable  {

  private Thread h1;
  String ruta = "";
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");  
  GwTypeCodeJpaController typeCodeController = new GwTypeCodeJpaController(factory);
  GwLobModelJpaController lobModelController = new GwLobModelJpaController(factory);
  
  
  
  public DialogLoadModel(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    btnStart.setEnabled(false); 
  }
  
  int totalItems=17000;
  int currentItem=1;
    
  @Override
  public void run() {    
    if(!btnStart.isEnabled()) {
      regenerateTablesGW();
      loadTypelist(TypeKeysEnum.CostCategory.name());      
      loadTypelist(TypeKeysEnum.CostType.name());      
      loadTypelist(TypeKeysEnum.CoverageSubtype.name());      
      loadTypelist(TypeKeysEnum.CoverageType.name());      
      loadTypelist(TypeKeysEnum.CovTermPattern.name());      
      loadTypelist(TypeKeysEnum.ExposureType.name());      
      loadTypelist(TypeKeysEnum.InternalPolicyType.name());      
      loadTypelist(TypeKeysEnum.LOBCode.name());                  
      loadTypelist(TypeKeysEnum.LossCause.name());      
      loadTypelist(TypeKeysEnum.LossPartyType.name());      
      loadTypelist(TypeKeysEnum.LossType.name());      
      loadTypelist(TypeKeysEnum.OfferingType_Ext.name());  
      loadTypelist(TypeKeysEnum.PolicyTab.name());      
      loadTypelist(TypeKeysEnum.PolicyType.name());                  
      loadLobModel(TypeKeysEnum.LobModel.name());
      System.out.println("EL TOTAL DE REGISTROS ES: " + currentItem);
      progreso(100,100);                    
      this.dispose();
    }    
  }
  
  private void progreso(int total,int actual){
    if(actual%100==0){            
      double porcentaje = (actual*100)/(total);
      this.progressBar.setValue((int)porcentaje);
    }
  }  
  
  private void loadTypelist(String typelistName){       
    
    
    FileReader fr = null;    
    try {
      FileInputStream archivo = new FileInputStream(ruta + typelistName + ".txt");
      BufferedReader br = new BufferedReader(new InputStreamReader(archivo, "utf-8"));
      String linea;      
      String splitRow[] = null;
      int numColumns= br.readLine().split("\t").length;
      while((linea=br.readLine())!=null){        
        splitRow = linea.split("\t");        
        GwTypeCode newTypeCode = new GwTypeCode();        
        newTypeCode.setTypeCode(splitRow[0]);
        newTypeCode.setNameEs(splitRow[1]);
        newTypeCode.setNameUs(splitRow[2]);        
        if(numColumns==4){
          newTypeCode.setOtherCategory(splitRow[3]);        
        }
        newTypeCode.setIsNew(false);        
        newTypeCode.setRetired(false);        
        newTypeCode.setTypeKeyName(typelistName);        
        typeCodeController.create(newTypeCode);
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
  
  private void loadLobModel(String typelistName){
    FileReader fr = null;    
    try {      
      FileInputStream archivo = new FileInputStream(ruta + typelistName + ".txt");
      BufferedReader br = new BufferedReader(new InputStreamReader(archivo, "utf-8"));
      String linea;      
      String splitRow[] = null;      
      int numColumns= br.readLine().split("\t").length;
      while((linea=br.readLine())!=null){        
        splitRow = linea.split("\t");            
        GwLobModel newLobModel = new GwLobModel();                
        newLobModel.setOffering(splitRow[0]);               //Offering	
        newLobModel.setLobCode(splitRow[1]);                //LOBCode	
        newLobModel.setPolicyType(splitRow[2]);             //PolicyType	
        newLobModel.setLossType(splitRow[3]);               //LossType	
        newLobModel.setCoverageType(splitRow[4]);           //CoverageType	
        newLobModel.setInternalPolicyType(splitRow[5]);     //InternalPolicyType	
        newLobModel.setPolicyTab(splitRow[6]);              //PolicyTab	
        newLobModel.setLossPartyType(splitRow[7]);          //LossPartyType	
        newLobModel.setCoverageSubtype(splitRow[8]);        //CoverageSubtype	
        newLobModel.setExposureType(splitRow[9]);           //ExposureType	
        newLobModel.setCovtermPattern(splitRow[10]);        //CovTermPattern	
        newLobModel.setCoverageSubtypeClass(splitRow[11]);  //CoverageSubtypeClass	
        newLobModel.setLimitOrDeductible(splitRow[12]);     //Limite o Deducible	
        newLobModel.setLossCause(splitRow[13]);             //Causas por cobertura	
        newLobModel.setCostCategory(splitRow[14]);          //Categorias de costo
        
        lobModelController.create(newLobModel);
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
  
  private void regenerateTablesGW(){               
    try {
      DataBaseController conection = new DataBaseController();
      conection.connect();      
      conection.removeTablesGW();
      conection.createTablesGW();
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
    setTitle("Cargar LOBModel a BD");

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
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
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

    setSize(new java.awt.Dimension(622, 188));
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
      java.util.logging.Logger.getLogger(DialogLoadModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(DialogLoadModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(DialogLoadModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(DialogLoadModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        DialogLoadModel dialog = new DialogLoadModel(new javax.swing.JFrame(), true);
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
