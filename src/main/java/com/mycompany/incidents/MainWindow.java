package com.mycompany.incidents;

import com.mycompany.incidents.otherResources.DataBaseController;
import com.mycompany.incidents.panels.ClosuresPanel;
import com.mycompany.incidents.panels.ReinsurancePanel;
import com.mycompany.incidents.panels.NotesPanel;
import com.mycompany.incidents.panels.IncidentsPanel;
import com.mycompany.incidents.panels.TemplatePanel;
import com.mycompany.incidents.panels.modelGW.PanelMain;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MainWindow extends javax.swing.JFrame {  
    
  public MainWindow() throws SQLException {      
    changeLookAndFeel();
    initComponents();
    initializeComponents();      
  }
  
  private void initializeComponents() throws SQLException{ 
    setExtendedState(MAXIMIZED_BOTH);      
    DataBaseController conection = new DataBaseController();
    conection.connectOrCreateDB();    
    crateWindowCloseListener(conection);
    createPanels();        
  }
  
  @Override
  public  Image getIconImage(){
    Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("star_blue.png"));
    return retValue;
  }
    
  private void crateWindowCloseListener(DataBaseController conection) {
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        try {
          conection.disconect();
        } catch (SQLException ex) {
          Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        e.getWindow().dispose();
        System.exit(0);
      }
    });
  }
    
  private void changeLookAndFeel(){
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(null, "Error al  cargar interfaz del sistema");
    }  
  }
    
  private void createPanels() {       
    try {
      InputStream in = getClass().getResourceAsStream("/config.txt");
      BufferedReader bf = new BufferedReader(new InputStreamReader(in));                  
      String[] splitLine = bf.readLine().split("\t");
      for(String value : splitLine) {
        switch (value){
          case "1":
            IncidentsPanel anIncidentsPanel = new IncidentsPanel();
            tabbedPanePrincipal.add("INCIDENTES",anIncidentsPanel);
          break;
          case "2":
            ReinsurancePanel aReinsurancePanel = new ReinsurancePanel();
            tabbedPanePrincipal.add("REASEGURO",aReinsurancePanel);
          break;
          case "3":
            NotesPanel aNotesPanel = new NotesPanel();
            tabbedPanePrincipal.add("NOTAS",aNotesPanel);      
          break;
          case "4":
            PanelMain aModelPanel = new PanelMain();
            tabbedPanePrincipal.add("MODELO",aModelPanel);
          break;
          case "5":
            ClosuresPanel aClosuresPanel = new ClosuresPanel();
            tabbedPanePrincipal.add("CIERRES",aClosuresPanel);
          break;
          case "6":
            TemplatePanel aTemplatePanel = new TemplatePanel();
            tabbedPanePrincipal.add("PLANTILLA",aTemplatePanel);
          break;
        }
      }
    }
    catch (IOException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage());  
    }
  }
 
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    tabbedPanePrincipal = new javax.swing.JTabbedPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("APUNTES 2020");
    setIconImage(getIconImage());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(tabbedPanePrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(tabbedPanePrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
    );

    tabbedPanePrincipal.getAccessibleContext().setAccessibleName("");

    pack();
  }// </editor-fold>//GEN-END:initComponents

  public static void main(String args[]) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(null, "Error al  cargar interfaz del sistema");
    }

    java.awt.EventQueue.invokeLater(() -> { try {
      new MainWindow().setVisible(true);
      } catch (SQLException ex) {
        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
      }
});
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane tabbedPanePrincipal;
  // End of variables declaration//GEN-END:variables

  
}
