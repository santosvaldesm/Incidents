package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Entry;
import com.mycompany.incidents.entities.Incident;
import com.mycompany.incidents.jpaControllers.EntryJpaController;
import com.mycompany.incidents.otherResources.TableController;
import com.mycompany.incidents.jpaControllers.IncidentJpaController;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class IncidentsPanel extends javax.swing.JPanel {

  
  TableController aTableController = new TableController();
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
  IncidentJpaController incidentController = new IncidentJpaController(factory);
  EntryJpaController entryController = new EntryJpaController(factory);
  int txtIncidentId = 0;
  Incident currentIncident = null;
  Entry currentEntry = null;
  
  public IncidentsPanel() {    
    initComponents();
    activeButtons();
    searchIncidents();    
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    entityManagerIncidents = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("Incidents_PU").createEntityManager();
    jSplitPane2 = new javax.swing.JSplitPane();
    jSplitPane3 = new javax.swing.JSplitPane();
    jPanel1 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    txtIncidentCode = new javax.swing.JTextField();
    txtIncidentAffectedUser = new javax.swing.JTextField();
    btnUpdateIncident = new javax.swing.JButton();
    btnCreateIncident = new javax.swing.JButton();
    btnRemoveIncident = new javax.swing.JButton();
    btnClearIncident = new javax.swing.JButton();
    txtIncidentStatus = new javax.swing.JTextField();
    txtIncidentAssigned = new javax.swing.JTextField();
    jScrollPane5 = new javax.swing.JScrollPane();
    txtIncidentDescription = new javax.swing.JEditorPane();
    jPanel3 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableIncidents = new javax.swing.JTable();
    jLabel6 = new javax.swing.JLabel();
    comboFindMode = new javax.swing.JComboBox<>();
    btnImport = new javax.swing.JButton();
    btnClearSearch = new javax.swing.JButton();
    btnSearch = new javax.swing.JButton();
    txtSearch = new javax.swing.JTextField();
    jSplitPane4 = new javax.swing.JSplitPane();
    jPanel2 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jScrollPane4 = new javax.swing.JScrollPane();
    txtEntryDescription = new javax.swing.JEditorPane();
    btnUpdateEntry = new javax.swing.JButton();
    btnCreateEntry = new javax.swing.JButton();
    btnRemoveEntry = new javax.swing.JButton();
    btnClearEntry = new javax.swing.JButton();
    txtEntryDate = new javax.swing.JTextField();
    jScrollPane3 = new javax.swing.JScrollPane();
    tableEntries = new javax.swing.JTable();

    jSplitPane2.setDividerLocation(400);
    jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    jSplitPane3.setDividerLocation(550);

    jLabel2.setText("CODIGO");

    jLabel3.setText("STATUS");

    jLabel4.setText("GESTOR");

    jLabel5.setText("AFECTADO");

    txtIncidentCode.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        txtIncidentCodeKeyTyped(evt);
      }
    });

    btnUpdateIncident.setIcon(new javax.swing.ImageIcon("C:\\Users\\santvamu\\Documents\\NetBeansProjects\\Incidents\\src\\main\\java\\com\\mycompany\\incidents\\media\\Save.png")); // NOI18N
    btnUpdateIncident.setToolTipText("Actualizar incidente");
    btnUpdateIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnUpdateIncidentActionPerformed(evt);
      }
    });

    btnCreateIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nuevo.png"))); // NOI18N
    btnCreateIncident.setToolTipText("Crear nuevo incidente");
    btnCreateIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateIncidentActionPerformed(evt);
      }
    });

    btnRemoveIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar.png"))); // NOI18N
    btnRemoveIncident.setToolTipText("Eliminar incidente");
    btnRemoveIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRemoveIncidentActionPerformed(evt);
      }
    });

    btnClearIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/limpiar.png"))); // NOI18N
    btnClearIncident.setToolTipText("Limpiar formulario");
    btnClearIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearIncidentActionPerformed(evt);
      }
    });

    jScrollPane5.setViewportView(txtIncidentDescription);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane5)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtIncidentStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
              .addComponent(txtIncidentCode, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
              .addComponent(txtIncidentAssigned)
              .addComponent(txtIncidentAffectedUser, javax.swing.GroupLayout.Alignment.TRAILING)))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(btnUpdateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnCreateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnRemoveIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnClearIncident)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnCreateIncident)
          .addComponent(btnUpdateIncident)
          .addComponent(btnRemoveIncident)
          .addComponent(btnClearIncident))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtIncidentCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtIncidentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtIncidentAssigned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtIncidentAffectedUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane3.setRightComponent(jPanel1);

    tableIncidents.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableIncidents.setMinimumSize(new java.awt.Dimension(300, 200));
    tableIncidents.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableIncidentsMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tableIncidents);

    jLabel6.setText("Incidente");

    comboFindMode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seguimiento", "Codigo", "Estado", "Asignado", "Descripcion", "Afectado" }));

    btnImport.setText("Importar");
    btnImport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnImportActionPerformed(evt);
      }
    });

    btnClearSearch.setText("Limpiar");
    btnClearSearch.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearSearchActionPerformed(evt);
      }
    });

    btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lupa.gif"))); // NOI18N
    btnSearch.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSearchActionPerformed(evt);
      }
    });

    txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtSearchKeyPressed(evt);
      }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboFindMode, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(141, 141, 141)
                .addComponent(btnImport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearSearch))
              .addComponent(txtSearch))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jScrollPane1))
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboFindMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6))
              .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnClearSearch)
                .addComponent(btnImport)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane3.setLeftComponent(jPanel3);

    jSplitPane2.setTopComponent(jSplitPane3);

    jSplitPane4.setDividerLocation(550);

    jLabel1.setText("FECHA");

    txtEntryDescription.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        txtEntryDescriptionKeyTyped(evt);
      }
    });
    jScrollPane4.setViewportView(txtEntryDescription);

    btnUpdateEntry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
    btnUpdateEntry.setToolTipText("Actualizar entrada");
    btnUpdateEntry.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnUpdateEntryActionPerformed(evt);
      }
    });

    btnCreateEntry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nuevo.png"))); // NOI18N
    btnCreateEntry.setToolTipText("Crear nueva entrada");
    btnCreateEntry.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateEntryActionPerformed(evt);
      }
    });

    btnRemoveEntry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar.png"))); // NOI18N
    btnRemoveEntry.setToolTipText("Eliminar entrada");
    btnRemoveEntry.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRemoveEntryActionPerformed(evt);
      }
    });

    btnClearEntry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/limpiar.png"))); // NOI18N
    btnClearEntry.setToolTipText("Limpiar formulario");
    btnClearEntry.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearEntryActionPerformed(evt);
      }
    });

    txtEntryDate.setEditable(false);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtEntryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(btnUpdateEntry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreateEntry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveEntry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearEntry)))))
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnCreateEntry)
          .addComponent(btnUpdateEntry)
          .addComponent(btnRemoveEntry)
          .addComponent(btnClearEntry))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtEntryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane4.setRightComponent(jPanel2);

    tableEntries.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableEntries.setMinimumSize(new java.awt.Dimension(300, 200));
    tableEntries.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableEntriesMouseClicked(evt);
      }
    });
    jScrollPane3.setViewportView(tableEntries);

    jSplitPane4.setLeftComponent(jScrollPane3);

    jSplitPane2.setRightComponent(jSplitPane4);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane2)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

    private void btnCreateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateIncidentActionPerformed
      Incident newIncident = new Incident();
      newIncident.setCode(txtIncidentCode.getText());
      newIncident.setStatus(txtIncidentStatus.getText());
      newIncident.setAssigned(txtIncidentAssigned.getText());
      newIncident.setDescription(txtIncidentDescription.getText());
      newIncident.setAffected(txtIncidentAffectedUser.getText());
      incidentController.create(newIncident);      
      searchIncidents();
    }//GEN-LAST:event_btnCreateIncidentActionPerformed

  private void searchIncidents(){        
    List<Incident> incidentsList = incidentController.executeSearchIncident(txtSearch.getText(),comboFindMode.getSelectedIndex());        
    tableIncidents.setModel(aTableController.createModel(incidentsList.toArray(),Incident.columNames()));        
    TableController.cofigureSizeColumns(tableIncidents,Incident.columNames());
    entityManagerIncidents.clear();
    currentIncident = null;
    currentEntry = null;
    tableEntries.setModel(new DefaultTableModel());      
    clearEntryControls();
    clearIncidentControls();
    activeButtons();
  }



  private void searchIncidentsByEntry(){        
    List<Incident> incidentsList = incidentController.executeSearchIncidentByEntry(txtSearch.getText());        
    tableIncidents.setModel(aTableController.createModel(incidentsList.toArray(),Incident.columNames()));        
    TableController.cofigureSizeColumns(tableIncidents,Incident.columNames());
    entityManagerIncidents.clear();
    currentIncident = null;
    currentEntry = null;
    tableEntries.setModel(new DefaultTableModel());      
    clearEntryControls();
    clearIncidentControls();
    activeButtons();
  }


  private void searchEntries(){
    if(currentIncident != null){
      currentIncident = incidentController.findIncident(currentIncident.getId());  
      currentEntry = null;
      tableEntries.setModel(aTableController.createModel(currentIncident.getEntryList().toArray(),Entry.columNames()));
      TableController.cofigureSizeColumns(tableEntries, Entry.columNames());
      clearEntryControls();
      activeButtons();
    }  
  }
  
  private void excecuteSearch(){
    if(comboFindMode.getSelectedIndex()==0){
      searchIncidentsByEntry();
    } else {
      searchIncidents();  
    }
  }
    
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
      excecuteSearch();      
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tableIncidentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableIncidentsMouseClicked
      int idPosition = tableIncidents.getSelectedRow();
      if (idPosition != -1) {
          idPosition = Integer.parseInt(tableIncidents.getModel().getValueAt(idPosition,0).toString());
          currentIncident = incidentController.findIncident(idPosition);
          txtIncidentAffectedUser.setText(currentIncident.getAffected());
          txtIncidentAssigned.setText(currentIncident.getAssigned());
          txtIncidentCode.setText(currentIncident.getCode());
          txtIncidentDescription.setText(currentIncident.getDescription());
          txtIncidentStatus.setText(currentIncident.getStatus());
          txtIncidentId = currentIncident.getId();
          searchEntries();
      }
      currentEntry = null;
      clearEntryControls();
      activeButtons();
    }//GEN-LAST:event_tableIncidentsMouseClicked

  private void clearIncidentControls(){
      txtIncidentAffectedUser.setText("");
      txtIncidentAssigned.setText("");
      txtIncidentCode.setText("");
      txtIncidentDescription.setText("");
      txtIncidentStatus.setText("");
      txtIncidentId = 0;        
  }

  private void clearEntryControls(){
      txtEntryDate.setText("");
      txtEntryDescription.setText("");
  }

  private void activeButtons() {
      btnCreateIncident.setEnabled(false);
      btnCreateEntry.setEnabled(false);
      btnUpdateIncident.setEnabled(false);
      btnUpdateEntry.setEnabled(false);
      btnRemoveIncident.setEnabled(false);
      btnRemoveEntry.setEnabled(false);        
      if(txtIncidentCode.getText().trim().length()!=0){
          btnCreateIncident.setEnabled(true);
      }        
      if(currentIncident!=null){
          btnRemoveIncident.setEnabled(true);
          btnUpdateIncident.setEnabled(true);
          if(txtEntryDescription.getText().trim().length()!=0){
              btnCreateEntry.setEnabled(true);
          }
          if(currentEntry!=null){
              btnRemoveEntry.setEnabled(true);
              btnUpdateEntry.setEnabled(true);
          }
      }                       

  }
    
    private void tableEntriesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEntriesMouseClicked
        int idPosition = tableEntries.getSelectedRow();
        if (idPosition != -1) {
            idPosition = Integer.parseInt(tableEntries.getModel().getValueAt(idPosition,0).toString());
            currentEntry = entryController.findEntry(idPosition);
            txtEntryDate.setText(currentEntry.getEntrydate().toString());
            txtEntryDescription.setText(currentEntry.getEntry());                    
        }
        activeButtons();
    }//GEN-LAST:event_tableEntriesMouseClicked

    private void btnUpdateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateIncidentActionPerformed
      currentIncident.setCode(txtIncidentCode.getText());
      currentIncident.setStatus(txtIncidentStatus.getText());
      currentIncident.setAssigned(txtIncidentAssigned.getText());
      currentIncident.setDescription(txtIncidentDescription.getText());
      currentIncident.setAffected(txtIncidentAffectedUser.getText());
      try {      
          incidentController.edit(currentIncident);
      } catch (Exception ex) {
          Logger.getLogger(IncidentsPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
      searchIncidents();
    }//GEN-LAST:event_btnUpdateIncidentActionPerformed

    private void btnRemoveIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveIncidentActionPerformed
      if(JOptionPane.showConfirmDialog(this, "Confirma la eliminación de este registro?")==0){
        try {
          incidentController.destroy(currentIncident.getId());
        } catch (NonexistentEntityException ex) {
          Logger.getLogger(IncidentsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        searchIncidents();
      }
    }//GEN-LAST:event_btnRemoveIncidentActionPerformed

    private void btnClearIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearIncidentActionPerformed
        searchIncidents();
    }//GEN-LAST:event_btnClearIncidentActionPerformed

    private void txtIncidentCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIncidentCodeKeyTyped
        activeButtons();        
    }//GEN-LAST:event_txtIncidentCodeKeyTyped

  private void btnClearEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearEntryActionPerformed
    searchEntries();
  }//GEN-LAST:event_btnClearEntryActionPerformed

  private void btnRemoveEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveEntryActionPerformed
    if(JOptionPane.showConfirmDialog(this, "Confirma la eliminación de este registro?")==0){
      try {
        entryController.destroy(currentEntry.getId());
      } catch (NonexistentEntityException ex) {
        Logger.getLogger(IncidentsPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
      searchEntries();
    }
  }//GEN-LAST:event_btnRemoveEntryActionPerformed

  private void btnCreateEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateEntryActionPerformed
    Entry newEntry = new Entry();
    newEntry.setEntrydate(new Date());
    newEntry.setEntry(txtEntryDescription.getText());
    newEntry.setIncident(currentIncident);
    entryController.create(newEntry);
    searchEntries();
  }//GEN-LAST:event_btnCreateEntryActionPerformed

  private void btnUpdateEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateEntryActionPerformed
    currentEntry.setEntry(txtEntryDescription.getText());
    currentEntry.setIncident(currentIncident);
    try {
      entryController.edit(currentEntry);
    } catch (Exception ex) {
      Logger.getLogger(IncidentsPanel.class.getName()).log(Level.SEVERE, null, ex);
    }
    searchEntries();
  }//GEN-LAST:event_btnUpdateEntryActionPerformed

  private void txtEntryDescriptionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntryDescriptionKeyTyped
    activeButtons();
  }//GEN-LAST:event_txtEntryDescriptionKeyTyped

  private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
    comboFindMode.setSelectedIndex(0);
    comboFindMode.setEnabled(true);
    txtSearch.setText("");    
    searchIncidents();
  }//GEN-LAST:event_btnClearSearchActionPerformed

  private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
    if(evt.getKeyCode()== KeyEvent.VK_ENTER){
      excecuteSearch();
    } 
  }//GEN-LAST:event_txtSearchKeyPressed

  private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
    DialogUpdateFromExcel dialog = new DialogUpdateFromExcel(null,true);
    dialog.setVisible(true);
  }//GEN-LAST:event_btnImportActionPerformed
  

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnClearEntry;
  private javax.swing.JButton btnClearIncident;
  private javax.swing.JButton btnClearSearch;
  private javax.swing.JButton btnCreateEntry;
  private javax.swing.JButton btnCreateIncident;
  private javax.swing.JButton btnImport;
  private javax.swing.JButton btnRemoveEntry;
  private javax.swing.JButton btnRemoveIncident;
  private javax.swing.JButton btnSearch;
  private javax.swing.JButton btnUpdateEntry;
  private javax.swing.JButton btnUpdateIncident;
  private javax.swing.JComboBox<String> comboFindMode;
  private javax.persistence.EntityManager entityManagerIncidents;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JSplitPane jSplitPane2;
  private javax.swing.JSplitPane jSplitPane3;
  private javax.swing.JSplitPane jSplitPane4;
  private javax.swing.JTable tableEntries;
  private javax.swing.JTable tableIncidents;
  private javax.swing.JTextField txtEntryDate;
  private javax.swing.JEditorPane txtEntryDescription;
  private javax.swing.JTextField txtIncidentAffectedUser;
  private javax.swing.JTextField txtIncidentAssigned;
  private javax.swing.JTextField txtIncidentCode;
  private javax.swing.JEditorPane txtIncidentDescription;
  private javax.swing.JTextField txtIncidentStatus;
  private javax.swing.JTextField txtSearch;
  // End of variables declaration//GEN-END:variables

    
}
