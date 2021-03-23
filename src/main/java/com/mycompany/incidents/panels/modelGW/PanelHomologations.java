/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.panels.modelGW;

import com.mycompany.incidents.entities.GwEcosystemCoverages;
import com.mycompany.incidents.entities.GwHomologCoreToGeneral;
import com.mycompany.incidents.entities.GwHomologGeneralToCore;
import com.mycompany.incidents.jpaControllers.GwEcosystemCoveragesJpaController;
import com.mycompany.incidents.jpaControllers.GwHomologCoreToGeneralJpaController;
import com.mycompany.incidents.jpaControllers.GwHomologGeneralToCoreJpaController;
import com.mycompany.incidents.otherResources.EcosystemSearchTypeEnum;
import com.mycompany.incidents.otherResources.HomologationSearchTypeEnum;
import com.mycompany.incidents.otherResources.HomologationTypeEnum;
import com.mycompany.incidents.otherResources.TableController;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author santvamu
 */
public class PanelHomologations extends javax.swing.JPanel {

  TableController aTableController = new TableController();
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");  
  GwHomologCoreToGeneralJpaController coreToGeneralController = new GwHomologCoreToGeneralJpaController(factory);
  GwHomologGeneralToCoreJpaController generalToCoreController = new GwHomologGeneralToCoreJpaController(factory);
  GwEcosystemCoveragesJpaController ecosystemCoveragesController = new GwEcosystemCoveragesJpaController(factory);
  List<GwHomologCoreToGeneral> coreToGeneralList = null;
  List<GwHomologGeneralToCore> generalToCoreList = null;
  List<GwEcosystemCoverages> ecosystemCoverageList = null;
  
  public PanelHomologations() {
    initComponents();
  }

  private void searchCoreToEcosystemHomologations() {
    HomologationTypeEnum homologationType = HomologationTypeEnum.valueOf(comboHomologCoreEcoType.getSelectedItem().toString());
    HomologationSearchTypeEnum searchType = HomologationSearchTypeEnum.valueOf(comboHomologCoreEcoSearchType.getSelectedItem().toString());
    String valueToSearch = txtSearchHomologCoreEco.getText();
    coreToGeneralList = coreToGeneralController.executeSearchHomologations(valueToSearch, homologationType,searchType,txtResultQueryHomologCoreEcosystem);
    
    tableHomologCoreEcosystem.setModel(aTableController.createModel(coreToGeneralList.toArray(),GwHomologCoreToGeneral.columNames()));        
    TableController.cofigureSizeColumns(tableHomologCoreEcosystem,GwHomologCoreToGeneral.columNames());
  }
  
  private void searchEcosystemToCoreHomologations() {
      HomologationTypeEnum homologationType = HomologationTypeEnum.valueOf(comboHomologEcoCoreType.getSelectedItem().toString());
      HomologationSearchTypeEnum searchType = HomologationSearchTypeEnum.valueOf(comboHomologEcoCoreSearchType.getSelectedItem().toString());
      String valueToSearch = txtSearchHomologEcoCore.getText();
      generalToCoreList = generalToCoreController.executeSearchHomologations(valueToSearch, homologationType,searchType,txtResultQueryHomologEcosystemCore);
    
      tableHomologEcosystemCore.setModel(aTableController.createModel(generalToCoreList.toArray(),GwHomologGeneralToCore.columNames()));        
      TableController.cofigureSizeColumns(tableHomologEcosystemCore,GwHomologGeneralToCore.columNames());
  }
  
  private void searchEcosystemCoverages() {
    EcosystemSearchTypeEnum typeSearch = EcosystemSearchTypeEnum.valueOf(comboEcosystemSearchType.getSelectedItem().toString());
    String valueToSearch = txtSearchEcosystemCoverage.getText();
    ecosystemCoverageList = ecosystemCoveragesController.executeSearchEcosystemCoverages(valueToSearch, typeSearch,txtResultQueryEcosystemCoverages);
    
    tableEcosystemCoverages.setModel(aTableController.createModel(ecosystemCoverageList.toArray(),GwEcosystemCoverages.columNames()));        
    TableController.cofigureSizeColumns(tableEcosystemCoverages,GwEcosystemCoverages.columNames());
    
  }
  
  public DefaultComboBoxModel<String> getHomologationsTypes(){
    DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
    HomologationTypeEnum[] values = HomologationTypeEnum.values();
    for (HomologationTypeEnum value : values) {
      comboModel.addElement(value.toString());
    }    
    return comboModel;
  }
  
  public DefaultComboBoxModel<String> getHomologationsSearchTypes(){
    DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
    HomologationSearchTypeEnum[] values = HomologationSearchTypeEnum.values();
    for (HomologationSearchTypeEnum value : values) {
      comboModel.addElement(value.toString());
    }    
    return comboModel;
  }
  
  public DefaultComboBoxModel<String> getEcosystemSearchTypes(){
    DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
    EcosystemSearchTypeEnum[] values = EcosystemSearchTypeEnum.values();
    for (EcosystemSearchTypeEnum value : values) {
      comboModel.addElement(value.toString());
    }    
    return comboModel;
  }
  
  
    
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableHomologCoreEcosystem = new javax.swing.JTable();
    comboHomologCoreEcoType = new javax.swing.JComboBox<>();
    comboHomologCoreEcoSearchType = new javax.swing.JComboBox<>();
    btnSearchHomologCoreEcosystem = new javax.swing.JButton();
    txtSearchHomologCoreEco = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtResultQueryHomologCoreEcosystem = new javax.swing.JTextField();
    jPanel3 = new javax.swing.JPanel();
    jScrollPane3 = new javax.swing.JScrollPane();
    tableHomologEcosystemCore = new javax.swing.JTable();
    comboHomologEcoCoreType = new javax.swing.JComboBox<>();
    comboHomologEcoCoreSearchType = new javax.swing.JComboBox<>();
    btnSearchHomologEcosystemCore = new javax.swing.JButton();
    txtSearchHomologEcoCore = new javax.swing.JTextField();
    jButton2 = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    txtResultQueryHomologEcosystemCore = new javax.swing.JTextField();
    jPanel4 = new javax.swing.JPanel();
    jScrollPane4 = new javax.swing.JScrollPane();
    tableEcosystemCoverages = new javax.swing.JTable();
    btnSearchEcosystemCoverages = new javax.swing.JButton();
    txtSearchEcosystemCoverage = new javax.swing.JTextField();
    jButton3 = new javax.swing.JButton();
    jLabel6 = new javax.swing.JLabel();
    comboEcosystemSearchType = new javax.swing.JComboBox<>();
    txtResultQueryEcosystemCoverages = new javax.swing.JTextField();
    jPanel2 = new javax.swing.JPanel();
    jPanel5 = new javax.swing.JPanel();
    jButton4 = new javax.swing.JButton();
    jTextField3 = new javax.swing.JTextField();
    jLabel9 = new javax.swing.JLabel();
    jComboBox1 = new javax.swing.JComboBox<>();
    jLabel3 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jTextField1 = new javax.swing.JTextField();
    jLabel8 = new javax.swing.JLabel();
    jTextField4 = new javax.swing.JTextField();
    btnClearIncident = new javax.swing.JButton();
    btnRemoveIncident = new javax.swing.JButton();
    btnCreateIncident = new javax.swing.JButton();
    btnUpdateIncident = new javax.swing.JButton();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jScrollPane5 = new javax.swing.JScrollPane();
    jEditorPane1 = new javax.swing.JEditorPane();
    btnShowQuery = new javax.swing.JButton();
    btnLoadHomologGwEco = new javax.swing.JButton();

    tableHomologCoreEcosystem.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableHomologCoreEcosystem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableHomologCoreEcosystemMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tableHomologCoreEcosystem);

    comboHomologCoreEcoType.setModel(getHomologationsTypes());

    comboHomologCoreEcoSearchType.setModel(getHomologationsSearchTypes());

    btnSearchHomologCoreEcosystem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lupa.gif"))); // NOI18N
    btnSearchHomologCoreEcosystem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSearchHomologCoreEcosystemActionPerformed(evt);
      }
    });

    txtSearchHomologCoreEco.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtSearchHomologCoreEcoKeyPressed(evt);
      }
    });

    jButton1.setText("Limpliar");

    jLabel1.setText("Tipo");

    jLabel4.setText("Por:");

    txtResultQueryHomologCoreEcosystem.setEnabled(false);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtSearchHomologCoreEco)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboHomologCoreEcoType, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboHomologCoreEcoSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(jButton1)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearchHomologCoreEcosystem, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(txtResultQueryHomologCoreEcosystem))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jButton1)
              .addComponent(comboHomologCoreEcoType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel1)
              .addComponent(jLabel4)
              .addComponent(comboHomologCoreEcoSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtSearchHomologCoreEco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(btnSearchHomologCoreEcosystem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
        .addGap(10, 10, 10)
        .addComponent(txtResultQueryHomologCoreEcosystem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jTabbedPane1.addTab("Homologaciones CORE - Ecosistema", jPanel1);

    tableHomologEcosystemCore.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableHomologEcosystemCore.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableHomologEcosystemCoreMouseClicked(evt);
      }
    });
    jScrollPane3.setViewportView(tableHomologEcosystemCore);

    comboHomologEcoCoreType.setModel(getHomologationsTypes());

    comboHomologEcoCoreSearchType.setModel(getHomologationsSearchTypes());

    btnSearchHomologEcosystemCore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lupa.gif"))); // NOI18N
    btnSearchHomologEcosystemCore.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSearchHomologEcosystemCoreActionPerformed(evt);
      }
    });

    txtSearchHomologEcoCore.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtSearchHomologEcoCoreKeyPressed(evt);
      }
    });

    jButton2.setText("Limpliar");

    jLabel2.setText("Tipo");

    jLabel5.setText("Por:");

    txtResultQueryHomologEcosystemCore.setEnabled(false);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtSearchHomologEcoCore)
              .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboHomologEcoCoreType, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboHomologEcoCoreSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(jButton2)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearchHomologEcosystemCore, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
          .addComponent(txtResultQueryHomologEcosystemCore, javax.swing.GroupLayout.Alignment.TRAILING))
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jButton2)
              .addComponent(comboHomologEcoCoreType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel2)
              .addComponent(jLabel5)
              .addComponent(comboHomologEcoCoreSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtSearchHomologEcoCore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(btnSearchHomologEcosystemCore, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtResultQueryHomologEcosystemCore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jTabbedPane1.addTab("Homologaciones Ecosistema - CORE", jPanel3);

    tableEcosystemCoverages.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableEcosystemCoverages.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableEcosystemCoveragesMouseClicked(evt);
      }
    });
    jScrollPane4.setViewportView(tableEcosystemCoverages);

    btnSearchEcosystemCoverages.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lupa.gif"))); // NOI18N
    btnSearchEcosystemCoverages.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSearchEcosystemCoveragesActionPerformed(evt);
      }
    });

    txtSearchEcosystemCoverage.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtSearchEcosystemCoverageKeyPressed(evt);
      }
    });

    jButton3.setText("Limpliar");

    jLabel6.setText("Por:");

    comboEcosystemSearchType.setModel(getEcosystemSearchTypes());

    txtResultQueryEcosystemCoverages.setEnabled(false);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtSearchEcosystemCoverage)
              .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboEcosystemSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearchEcosystemCoverages, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
          .addComponent(txtResultQueryEcosystemCoverages))
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jButton3)
              .addComponent(jLabel6)
              .addComponent(comboEcosystemSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtSearchEcosystemCoverage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(btnSearchEcosystemCoverages, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtResultQueryEcosystemCoverages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jTabbedPane1.addTab("Coberturas Ecosistema", jPanel4);

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Parametros"));

    jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interrogation.png"))); // NOI18N
    jButton4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton4ActionPerformed(evt);
      }
    });

    jLabel9.setText("CD Mapa");

    jComboBox1.setModel(getHomologationsTypes());

    jLabel3.setText("Tipo Homologacion");

    jLabel7.setText("Valor CORE");

    jLabel8.setText("Valor Ecosistema");

    btnClearIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/limpiar.png"))); // NOI18N
    btnClearIncident.setToolTipText("Limpiar formulario");
    btnClearIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearIncidentActionPerformed(evt);
      }
    });

    btnRemoveIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar.png"))); // NOI18N
    btnRemoveIncident.setToolTipText("Eliminar Nota");
    btnRemoveIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRemoveIncidentActionPerformed(evt);
      }
    });

    btnCreateIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nuevo.png"))); // NOI18N
    btnCreateIncident.setToolTipText("Crear nueva Nota");
    btnCreateIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateIncidentActionPerformed(evt);
      }
    });

    btnUpdateIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
    btnUpdateIncident.setToolTipText("Actualizar Nota");
    btnUpdateIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnUpdateIncidentActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel5Layout.createSequentialGroup()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
              .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(10, 10, 10)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jTextField4)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jTextField3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4))
              .addComponent(jTextField1)
              .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addGroup(jPanel5Layout.createSequentialGroup()
            .addComponent(btnUpdateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnCreateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnRemoveIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnClearIncident)))
        .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnClearIncident)
          .addComponent(btnRemoveIncident)
          .addComponent(btnCreateIncident)
          .addComponent(btnUpdateIncident))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel8))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel9)
            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    jTable1.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane2.setViewportView(jTable1);

    jScrollPane5.setViewportView(jEditorPane1);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
          .addComponent(jScrollPane5))
        .addContainerGap())
    );

    jTabbedPane1.addTab("Crear Homologación", jPanel2);

    btnShowQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interrogation.png"))); // NOI18N
    btnShowQuery.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnShowQueryActionPerformed(evt);
      }
    });

    btnLoadHomologGwEco.setText("Cargar Informacion");
    btnLoadHomologGwEco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnLoadHomologGwEcoActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnLoadHomologGwEco)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnShowQuery)
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(btnLoadHomologGwEco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnShowQuery, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTabbedPane1)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents
 
  private void tableHomologCoreEcosystemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableHomologCoreEcosystemMouseClicked
    
  }//GEN-LAST:event_tableHomologCoreEcosystemMouseClicked

  private void btnSearchHomologCoreEcosystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchHomologCoreEcosystemActionPerformed
    searchCoreToEcosystemHomologations();
  }//GEN-LAST:event_btnSearchHomologCoreEcosystemActionPerformed

  private void tableHomologEcosystemCoreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableHomologEcosystemCoreMouseClicked
    
  }//GEN-LAST:event_tableHomologEcosystemCoreMouseClicked

  private void btnSearchHomologEcosystemCoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchHomologEcosystemCoreActionPerformed
    searchEcosystemToCoreHomologations();
  }//GEN-LAST:event_btnSearchHomologEcosystemCoreActionPerformed

  private void tableEcosystemCoveragesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEcosystemCoveragesMouseClicked
    
  }//GEN-LAST:event_tableEcosystemCoveragesMouseClicked

  private void btnSearchEcosystemCoveragesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchEcosystemCoveragesActionPerformed
    searchEcosystemCoverages();
  }//GEN-LAST:event_btnSearchEcosystemCoveragesActionPerformed

  private void txtSearchHomologCoreEcoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchHomologCoreEcoKeyPressed
    if(evt.getKeyCode()== KeyEvent.VK_ENTER){
      searchCoreToEcosystemHomologations();
    }
  }//GEN-LAST:event_txtSearchHomologCoreEcoKeyPressed

  private void txtSearchHomologEcoCoreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchHomologEcoCoreKeyPressed
    if(evt.getKeyCode()== KeyEvent.VK_ENTER){
      searchEcosystemToCoreHomologations();
    }
  }//GEN-LAST:event_txtSearchHomologEcoCoreKeyPressed

  private void txtSearchEcosystemCoverageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEcosystemCoverageKeyPressed
    if(evt.getKeyCode()== KeyEvent.VK_ENTER){
      searchEcosystemCoverages();
    }
  }//GEN-LAST:event_txtSearchEcosystemCoverageKeyPressed

  private void btnShowQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowQueryActionPerformed
    DialogHelp dialog = new DialogHelp(null,true);
    dialog.setTextContent("--HOMOLOGACIONES EXISTENTES DE GENERAL A GW (INSTANCIA PDNLNF)---------------------- 13510\n--El archivo  creado debe tener el nombre: general_core.csv\nSELECT \n       t.CDTIPO       AS homologation,\n       f.CDAPLICACION AS source,\n       t.CDAPLICACION AS target,\n       f.CDVALOR AS source_value, \n       t.CDVALOR AS target_value\nFROM   \n       TTRD_MAPAS f, \n       TTRD_MAPAS t, \n       TTRD_HOMOLOGACION h\nWHERE  f.CDAPLICACION = 'GENERAL_GW'       AND --fuente\n       t.CDAPLICACION = 'CORE_GW'          AND --destino\n       t.CDTIPO IN (\n           'COBERTURA_GW',\n           'TERMINO_GW',\n           'OFFERING_TYPE_FROM_LINE_SUBLINE_GW',\n           'POLICY_TYPE_FROM_LINE_SUBLINE_GW',\n           'REINSURANCECONTRACT_FROM_OFFERING_COVERAGE_GW',\n           'LINE_FROM_OFFERING_TYPE_GW',\n           'APIGATEWAY_PRODUCT_TYPE_CODE_FROM_OFFERING_TYPE_GW',\n           'LOSS_TYPE_FROM_LINE_SUBLINE_GW',\n           'COVERAGE_FROM_OFFERING_COVERAGE_ARTICLE_GW',\n           'LOSS_CAUSE_FROM_SERVICES_GW')  AND --tipo de homologacion\n       t.CDTIPO       = t.CDTIPO           AND\n       f.CDMAPA       = h.CDMAPA           AND\n       t.CDMAPA       = h.CDHOMOLOGADO;\n\n--HOMOLOGACIONES EXISTENTES DE GW A GENERAL (INSTANCIA PDNLNF)---------------------- 8098\n--El archivo  creado debe tener el nombre: core_general.csv\n\nSELECT \n       t.CDTIPO       AS homologation,\n       f.CDAPLICACION AS source,\n       t.CDAPLICACION AS target,\n       f.CDVALOR AS source_value, \n       t.CDVALOR AS target_value\nFROM   TTRD_MAPAS f, \n       TTRD_MAPAS t, \n       TTRD_HOMOLOGACION h\nWHERE  f.CDAPLICACION = 'CORE_GW'       AND --fuente\n       t.CDAPLICACION = 'GENERAL_GW'    AND --destino\n       t.CDTIPO IN (\n           'COBERTURA_GW',\n           'TERMINO_GW',\n           'OFFERING_TYPE_FROM_LINE_SUBLINE_GW',\n           'POLICY_TYPE_FROM_LINE_SUBLINE_GW',\n           'REINSURANCECONTRACT_FROM_OFFERING_COVERAGE_GW',\n           'LINE_FROM_OFFERING_TYPE_GW',\n           'APIGATEWAY_PRODUCT_TYPE_CODE_FROM_OFFERING_TYPE_GW',\n           'LOSS_TYPE_FROM_LINE_SUBLINE_GW',\n           'LOSS_CAUSE_FROM_SERVICES_GW')  AND --tipo de homologacion\n       t.CDTIPO       = t.CDTIPO           AND\n       f.CDMAPA       = h.CDMAPA           AND\n       t.CDMAPA       = h.CDHOMOLOGADO;\n\n--LISTADO DE TODAS LAS COBERTURAS EXISTENTES EN ECOSISTEMA (INSTANCIA PDN)---------------------- 11200\n--El archivo  creado debe tener el nombre: coberturas_ecosistema.csv\nSELECT \n       C.CDRAMO        AS RAMO,\n       C.CDSUBRAMO     AS SUBRAMO,\n       C.CDGARANTIA    AS GARANTIA,\n       C.CDSUBGARANTIA AS SUBGARANTIA,\n       DECODE(SUBSTR(C.CDRAMO_CONTABLE, 0, 1), '1', C.CDRAMO_CONTABLE, C.CDRAMO_HOST) AS RAMO_CONTABLE,\n       P.DSALIAS_1 AS PRODUCTO,\n       C.DSALIAS_2 AS COBERTURA\nFROM \n       DIC_ALIAS_COBERTURAS C, \n       OPS$CORP.DIC_ALIAS_RS P\nWHERE   \n       P.CDRAMO    = C.CDRAMO AND\n       P.CDSUBRAMO = C.CDSUBRAMO\n");
    dialog.setVisible(true);
  }//GEN-LAST:event_btnShowQueryActionPerformed

  private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    DialogHelp dialog = new DialogHelp(null,true);
    dialog.setTextContent("SELECT MAX(CDMAPA)+1 FROM TTRD_MAPAS;");
    dialog.setVisible(true);
  }//GEN-LAST:event_jButton4ActionPerformed

  private void btnClearIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearIncidentActionPerformed

  }//GEN-LAST:event_btnClearIncidentActionPerformed

  private void btnRemoveIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveIncidentActionPerformed

  }//GEN-LAST:event_btnRemoveIncidentActionPerformed

  private void btnCreateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateIncidentActionPerformed

  }//GEN-LAST:event_btnCreateIncidentActionPerformed

  private void btnUpdateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateIncidentActionPerformed

  }//GEN-LAST:event_btnUpdateIncidentActionPerformed

  private void btnLoadHomologGwEcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadHomologGwEcoActionPerformed
    DialogLoadEcosystemData dialog = new DialogLoadEcosystemData(null,true);
    dialog.setVisible(true);
  }//GEN-LAST:event_btnLoadHomologGwEcoActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnClearIncident;
  private javax.swing.JButton btnCreateIncident;
  private javax.swing.JButton btnLoadHomologGwEco;
  private javax.swing.JButton btnRemoveIncident;
  private javax.swing.JButton btnSearchEcosystemCoverages;
  private javax.swing.JButton btnSearchHomologCoreEcosystem;
  private javax.swing.JButton btnSearchHomologEcosystemCore;
  private javax.swing.JButton btnShowQuery;
  private javax.swing.JButton btnUpdateIncident;
  private javax.swing.JComboBox<String> comboEcosystemSearchType;
  private javax.swing.JComboBox<String> comboHomologCoreEcoSearchType;
  private javax.swing.JComboBox<String> comboHomologCoreEcoType;
  private javax.swing.JComboBox<String> comboHomologEcoCoreSearchType;
  private javax.swing.JComboBox<String> comboHomologEcoCoreType;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JButton jButton3;
  private javax.swing.JButton jButton4;
  private javax.swing.JComboBox<String> jComboBox1;
  private javax.swing.JEditorPane jEditorPane1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JTable jTable1;
  private javax.swing.JTextField jTextField1;
  private javax.swing.JTextField jTextField3;
  private javax.swing.JTextField jTextField4;
  private javax.swing.JTable tableEcosystemCoverages;
  private javax.swing.JTable tableHomologCoreEcosystem;
  private javax.swing.JTable tableHomologEcosystemCore;
  private javax.swing.JTextField txtResultQueryEcosystemCoverages;
  private javax.swing.JTextField txtResultQueryHomologCoreEcosystem;
  private javax.swing.JTextField txtResultQueryHomologEcosystemCore;
  private javax.swing.JTextField txtSearchEcosystemCoverage;
  private javax.swing.JTextField txtSearchHomologCoreEco;
  private javax.swing.JTextField txtSearchHomologEcoCore;
  // End of variables declaration//GEN-END:variables
}
