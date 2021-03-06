package com.mycompany.incidents.panels.modelGW;

import javax.swing.JScrollPane;

public class PanelMain extends javax.swing.JPanel {
  
  public PanelMain() {
    initComponents();
    createPanels();
  }
  
  private void createPanels() {                
    PanelConfiguration aPanelConfiguration  = new PanelConfiguration();    
    PanelHomologations aPanelHomologations = new PanelHomologations();
    JScrollPane scrollConfiguration = new JScrollPane(aPanelConfiguration);
    tabbedPaneMain.add("CONFIGURACION",scrollConfiguration);            
    tabbedPaneMain.add("HOMOLOGACIONES",aPanelHomologations);                  
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    tabbedPaneMain = new javax.swing.JTabbedPane();

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(tabbedPaneMain)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(tabbedPaneMain)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents
  

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane tabbedPaneMain;
  // End of variables declaration//GEN-END:variables

  
}
