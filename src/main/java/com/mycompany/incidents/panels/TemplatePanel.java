package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Lista;
import com.mycompany.incidents.jpaControllers.ListaJpaController;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.IncidentException;
import com.mycompany.incidents.otherResources.TableController;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
/**
 *
 * @author santvamu
 */
public class TemplatePanel extends javax.swing.JPanel {

  TableController aTableController = new TableController();
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");  
  ListaJpaController listaController = new ListaJpaController(factory);
  int txtNoteId = 0;
  Lista currentLista = null;
    
  public TemplatePanel() {
    initComponents();    
    cargarCombos();
    searchListas();
    limpiarTodo();
  }
  
  private void cargarCombos(){
    List<Lista> listaList = listaController.executeSearchLista("");
    comboAgrupador.removeAllItems();
    comboAgrupador.addItem("");
    comboCausa.removeAllItems();
    comboCausa.addItem("");
    comboProceso.removeAllItems();    
    comboProceso.addItem("");
    comboEstado.removeAllItems();
    comboEstado.addItem("");
    comboResponsable.removeAllItems();
    comboResponsable.addItem("");
    comboDiagnostico.removeAllItems();
    comboDiagnostico.addItem("");
    comboAccion.removeAllItems();    
    comboAccion.addItem("");
    ComboDescripcion.removeAllItems();
    ComboDescripcion.addItem("");
    comboOperatividad.removeAllItems();
    comboOperatividad.addItem("");
    comboRaizal.removeAllItems();
    comboRaizal.addItem("");
    for(Lista aLista : listaList){
      switch(aLista.getNombre()){
        case "Causa":
          comboCausa.addItem(aLista.getValor());
          break;
        case "Agrupador":
          comboAgrupador.addItem(aLista.getValor());
          break;
        case "Proceso":
          comboProceso.addItem(aLista.getValor());
          break;
        case "Estado":
          comboEstado.addItem(aLista.getValor());
          break;
        case "Responsable":
          comboResponsable.addItem(aLista.getValor());
          break;
        case "Operatividad":
          comboOperatividad.addItem(aLista.getValor());
          break;
        case "Diagnostico":
          comboDiagnostico.addItem(aLista.getValor());
          break;
        case "Accion":
          comboAccion.addItem(aLista.getValor());
          break;
        case "Descripcion":
          ComboDescripcion.addItem(aLista.getValor());
          break;        
        case "Raizal":
          comboRaizal.addItem(aLista.getValor());
          break;
      }
      
    }
  }
  private void searchListas(){        
    List<Lista> listaList = listaController.executeSearchLista(txtSearchLista.getText());        
    tableListas.setModel(aTableController.createModel(listaList.toArray(),Lista.columNames()));        
    TableController.cofigureSizeColumns(tableListas,Lista.columNames());    
    currentLista = null;          
    clearListaControls();
    activeButtons();    
  }
  
  private void clearListaControls(){
      comboTipoLista.setSelectedIndex(-1);
      txtValor.setText("");
  }
  
  private void activeButtons() {
    btnCreateLista.setEnabled(false);        
    btnUpdateLista.setEnabled(false);        
    btnRemoveLista.setEnabled(false);        
    if(comboTipoLista.getSelectedIndex()!= -1 && 
       txtValor.getText().trim().length() != 0){
        btnCreateLista.setEnabled(true);
    }        
    if(currentLista != null){
        btnRemoveLista.setEnabled(true);
        btnUpdateLista.setEnabled(true);            
    }    
  }
  
  private void updateCurrentLista() throws IncidentException {
    if(currentLista!=null) {
      if(txtValor.getText().length()>2000){
        throw new IncidentException("El numero de caracteres de la descripción supera el limite de 2.000 caracteres");        
      }
      currentLista.setNombre(comboTipoLista.getSelectedItem().toString());
      currentLista.setValor(txtValor.getText());      
      try {
        listaController.edit(currentLista);
      } catch (Exception ex) {
        throw new IncidentException("Error al editar la nota\n" + ex.getMessage());
      }
    }
  }
  
  private void validateChanges() throws IncidentException{
    if(currentLista !=null && 
       currentLista.getValor().compareTo(txtValor.getText())!=0) {
      int result = JOptionPane.showConfirmDialog(this, "Existen cambios en la descripción, \n¿desea guardarlos?","Cambios sin guardar",JOptionPane.YES_NO_OPTION);
      switch(result){
        case JOptionPane.OK_OPTION:
          updateCurrentLista();
          break;                
      } 
    }
  }
  
  private void changeIncidentSelection(){
    try {
      validateChanges();
      int idPosition = tableListas.getSelectedRow();
      if (idPosition != -1) {
          idPosition = Integer.parseInt(tableListas.getModel().getValueAt(idPosition,0).toString());
          currentLista = listaController.findLista(idPosition);
          comboTipoLista.setSelectedItem(currentLista.getNombre());
          txtValor.setText(currentLista.getValor());                  
      }         
      activeButtons();
    } catch (IncidentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
    }
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel2 = new javax.swing.JPanel();
    btnClear = new javax.swing.JButton();
    btnGenerte = new javax.swing.JButton();
    jPanel4 = new javax.swing.JPanel();
    jSplitPane1 = new javax.swing.JSplitPane();
    panelDatosLista = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    comboAgrupador = new javax.swing.JComboBox<>();
    jLabel2 = new javax.swing.JLabel();
    comboCausa = new javax.swing.JComboBox<>();
    jLabel3 = new javax.swing.JLabel();
    comboProceso = new javax.swing.JComboBox<>();
    jLabel4 = new javax.swing.JLabel();
    txtRaizal = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    comboEstado = new javax.swing.JComboBox<>();
    jLabel6 = new javax.swing.JLabel();
    comboResponsable = new javax.swing.JComboBox<>();
    jLabel12 = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    txtDiagnostico = new javax.swing.JTextPane();
    comboDiagnostico = new javax.swing.JComboBox<>();
    jLabel13 = new javax.swing.JLabel();
    jScrollPane3 = new javax.swing.JScrollPane();
    txtAccion = new javax.swing.JTextPane();
    comboAccion = new javax.swing.JComboBox<>();
    jLabel14 = new javax.swing.JLabel();
    jScrollPane4 = new javax.swing.JScrollPane();
    txtDescripcion = new javax.swing.JTextPane();
    ComboDescripcion = new javax.swing.JComboBox<>();
    jLabel15 = new javax.swing.JLabel();
    comboOperatividad = new javax.swing.JComboBox<>();
    jLabel16 = new javax.swing.JLabel();
    txtCredenciales = new javax.swing.JTextField();
    comboRaizal = new javax.swing.JComboBox<>();
    jScrollPane1 = new javax.swing.JScrollPane();
    txtOuput = new javax.swing.JTextPane();
    jPanel5 = new javax.swing.JPanel();
    jSplitPane2 = new javax.swing.JSplitPane();
    jPanel6 = new javax.swing.JPanel();
    btnClearSearch = new javax.swing.JButton();
    btnSearch = new javax.swing.JButton();
    txtSearchLista = new javax.swing.JTextField();
    jScrollPane5 = new javax.swing.JScrollPane();
    tableListas = new javax.swing.JTable();
    jPanel7 = new javax.swing.JPanel();
    btnUpdateLista = new javax.swing.JButton();
    btnCreateLista = new javax.swing.JButton();
    btnRemoveLista = new javax.swing.JButton();
    btnClearLista = new javax.swing.JButton();
    jLabel7 = new javax.swing.JLabel();
    comboTipoLista = new javax.swing.JComboBox<>();
    jScrollPane7 = new javax.swing.JScrollPane();
    txtValor = new javax.swing.JTextPane();
    jLabel8 = new javax.swing.JLabel();

    btnClear.setText("Limpiar / RecargarListas");
    btnClear.setToolTipText("Limpia y recarga todas la listas");
    btnClear.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearActionPerformed(evt);
      }
    });

    btnGenerte.setText("Generar");
    btnGenerte.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGenerteActionPerformed(evt);
      }
    });

    jSplitPane1.setDividerLocation(600);

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel1.setText("Agrupador del Error: ");

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Causa del error");

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel3.setText("Proceso del error: ");

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel4.setText("HU Raizal / Mejora");

    txtRaizal.setHorizontalAlignment(javax.swing.JTextField.LEFT);

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel5.setText("Estado Raizal");

    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel6.setText("Responsable solucion");

    jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel12.setText("Diagnostico");

    jScrollPane2.setViewportView(txtDiagnostico);

    comboDiagnostico.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboDiagnosticoItemStateChanged(evt);
      }
    });

    jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel13.setText("Accion ejecutada");

    jScrollPane3.setViewportView(txtAccion);

    comboAccion.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboAccionItemStateChanged(evt);
      }
    });

    jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel14.setText("Confirmar operatividad");

    jScrollPane4.setViewportView(txtDescripcion);

    ComboDescripcion.setMaximumSize(new java.awt.Dimension(150, 150));
    ComboDescripcion.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        ComboDescripcionItemStateChanged(evt);
      }
    });

    jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel15.setText("Descripcion solucion");

    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel16.setText("ID credenciales");

    comboRaizal.setName(""); // NOI18N
    comboRaizal.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboRaizalItemStateChanged(evt);
      }
    });

    javax.swing.GroupLayout panelDatosListaLayout = new javax.swing.GroupLayout(panelDatosLista);
    panelDatosLista.setLayout(panelDatosListaLayout);
    panelDatosListaLayout.setHorizontalGroup(
      panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelDatosListaLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDatosListaLayout.createSequentialGroup()
            .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(comboRaizal, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(comboProceso, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(txtRaizal, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(comboCausa, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(comboEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(comboAgrupador, javax.swing.GroupLayout.Alignment.TRAILING, 0, 219, Short.MAX_VALUE)
              .addComponent(comboResponsable, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addGroup(panelDatosListaLayout.createSequentialGroup()
            .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(comboDiagnostico, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(comboAccion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(ComboDescripcion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(10, 10, 10)
            .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(comboOperatividad, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jScrollPane2)
              .addComponent(txtCredenciales))))
        .addContainerGap())
    );
    panelDatosListaLayout.setVerticalGroup(
      panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelDatosListaLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(comboAgrupador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(comboCausa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(comboProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelDatosListaLayout.createSequentialGroup()
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(comboRaizal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(txtRaizal, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(comboResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
          .addGroup(panelDatosListaLayout.createSequentialGroup()
            .addComponent(jLabel12)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(comboDiagnostico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelDatosListaLayout.createSequentialGroup()
            .addComponent(jLabel13)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(comboAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 100, Short.MAX_VALUE))
          .addComponent(jScrollPane3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelDatosListaLayout.createSequentialGroup()
            .addComponent(jLabel15)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(ComboDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(comboOperatividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelDatosListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtCredenciales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    jSplitPane1.setLeftComponent(panelDatosLista);

    jScrollPane1.setViewportView(txtOuput);

    jSplitPane1.setRightComponent(jScrollPane1);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
      .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
          .addContainerGap()
          .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
          .addContainerGap()))
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 672, Short.MAX_VALUE)
      .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
          .addContainerGap()
          .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
          .addContainerGap()))
    );

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(btnGenerte)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnClear)
            .addGap(0, 652, Short.MAX_VALUE))
          .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnClear)
          .addComponent(btnGenerte))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    jTabbedPane1.addTab("GENERAR", jPanel2);

    jSplitPane2.setDividerLocation(400);

    btnClearSearch.setText("Limpiar");
    btnClearSearch.setToolTipText("");
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

    txtSearchLista.setToolTipText("");
    txtSearchLista.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtSearchListaKeyPressed(evt);
      }
    });

    tableListas.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableListas.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableListasMouseClicked(evt);
      }
    });
    tableListas.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        tableListasKeyReleased(evt);
      }
    });
    jScrollPane5.setViewportView(tableListas);

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
      jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel6Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
          .addGroup(jPanel6Layout.createSequentialGroup()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(btnClearSearch)
                .addGap(0, 0, Short.MAX_VALUE))
              .addComponent(txtSearchLista))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel6Layout.setVerticalGroup(
      jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel6Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel6Layout.createSequentialGroup()
            .addComponent(btnClearSearch)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtSearchLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane2.setRightComponent(jPanel6);

    btnUpdateLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
    btnUpdateLista.setToolTipText("Actualizar Nota");
    btnUpdateLista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnUpdateListaActionPerformed(evt);
      }
    });

    btnCreateLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nuevo.png"))); // NOI18N
    btnCreateLista.setToolTipText("Crear nueva Nota");
    btnCreateLista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateListaActionPerformed(evt);
      }
    });

    btnRemoveLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar.png"))); // NOI18N
    btnRemoveLista.setToolTipText("Eliminar Nota");
    btnRemoveLista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRemoveListaActionPerformed(evt);
      }
    });

    btnClearLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/limpiar.png"))); // NOI18N
    btnClearLista.setToolTipText("Limpiar formulario");
    btnClearLista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearListaActionPerformed(evt);
      }
    });

    jLabel7.setText("Tipo");

    comboTipoLista.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Agrupador", "Causa", "Proceso", "Estado", "Responsable", "Diagnostico", "Accion", "Descripcion", "Operatividad", "Raizal", "ErrorReaseg", "MensajesComunes" }));
    comboTipoLista.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboTipoListaItemStateChanged(evt);
      }
    });

    txtValor.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtValorKeyReleased(evt);
      }
    });
    jScrollPane7.setViewportView(txtValor);

    jLabel8.setText("Nombre");

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
      jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel7Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane7)
          .addGroup(jPanel7Layout.createSequentialGroup()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(btnUpdateLista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCreateLista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRemoveLista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearLista, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboTipoLista, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 29, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel7Layout.setVerticalGroup(
      jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel7Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(btnCreateLista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnClearLista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnRemoveLista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnUpdateLista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel7)
          .addComponent(comboTipoLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel8)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane2.setLeftComponent(jPanel7);

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane2)
        .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane2)
        .addContainerGap())
    );

    jTabbedPane1.addTab("LISTAS", jPanel5);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jTabbedPane1)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jTabbedPane1)
        .addContainerGap())
    );

    jTabbedPane1.getAccessibleContext().setAccessibleName("LISTAS");
  }// </editor-fold>//GEN-END:initComponents

  private void tableListasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListasKeyReleased
    try {
      validateChanges();
      changeIncidentSelection();
    } catch (IncidentException e) {
      JOptionPane.showConfirmDialog(this, e.getMessage());
    }
  }//GEN-LAST:event_tableListasKeyReleased

  private void tableListasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListasMouseClicked
    changeIncidentSelection();
  }//GEN-LAST:event_tableListasMouseClicked

  private void txtSearchListaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchListaKeyPressed
    if(evt.getKeyCode()== KeyEvent.VK_ENTER){
      try {
        validateChanges();
        searchListas();
      } catch (IncidentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
      }
    }
  }//GEN-LAST:event_txtSearchListaKeyPressed

  private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
    try {
      validateChanges();
      searchListas();
    } catch (IncidentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
    }
  }//GEN-LAST:event_btnSearchActionPerformed

  private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
    try {
      validateChanges();
      txtSearchLista.setText("");
      searchListas();
    } catch (IncidentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
    }
  }//GEN-LAST:event_btnClearSearchActionPerformed

  private void comboTipoListaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTipoListaItemStateChanged
    activeButtons();
  }//GEN-LAST:event_comboTipoListaItemStateChanged

  private void btnClearListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearListaActionPerformed
    try {
      validateChanges();
      searchListas();      
    } catch (IncidentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
    }
  }//GEN-LAST:event_btnClearListaActionPerformed

  private void btnRemoveListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveListaActionPerformed
    if(JOptionPane.showConfirmDialog(this, "Confirma la eliminación de este registro?")==0){
      try {
        listaController.destroy(currentLista.getId());
        searchListas();        
      } catch (NonexistentEntityException ex) {
        JOptionPane.showMessageDialog(this,"Error al eliminar nota: \n" + ex.getMessage());
      }
    }
  }//GEN-LAST:event_btnRemoveListaActionPerformed

  private void btnCreateListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateListaActionPerformed
    Lista newLista = new Lista();
    newLista.setNombre(comboTipoLista.getSelectedItem().toString());
    newLista.setValor(txtValor.getText());
    listaController.create(newLista);
    searchListas();    
  }//GEN-LAST:event_btnCreateListaActionPerformed

  private void btnUpdateListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateListaActionPerformed
    try {
      updateCurrentLista();
      searchListas();
      
    } catch (IncidentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
    }
  }//GEN-LAST:event_btnUpdateListaActionPerformed

  private void btnGenerteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerteActionPerformed
    // TODO add your handling code here:
    txtOuput.setText(
      "* Agrupador del Error: " + (comboAgrupador.getSelectedIndex()!=-1 ? comboAgrupador.getSelectedItem().toString() : "") + " |\n" +
      "* Causa del Error: " + (comboCausa.getSelectedIndex()!=-1 ? comboCausa.getSelectedItem().toString() : "") + " | \n" +
      "* Proceso del Error: " + (comboProceso.getSelectedIndex()!=-1 ? comboProceso.getSelectedItem().toString() : "") + " |\n" +
      "* HU Raizal / Mejora: " + txtRaizal.getText() + " | \n" +
      "* Estado Raizal: " + (comboEstado.getSelectedIndex()!=-1 ? comboEstado.getSelectedItem().toString() : "")  + " | \n" +
      "* Responsable Solucion: " + (comboResponsable.getSelectedIndex()!=-1 ? comboResponsable.getSelectedItem().toString() : "")  + " | \n" +
      "* Diagnostico: " + txtDiagnostico.getText() + " | \n" +
      "* Accion Ejecutada: " + txtAccion.getText() + " | \n" +
      "* Descripcion de Solucion: " + txtDescripcion.getText() + " |\n" +
      "* Confirmar operatividad del usuario Afectado: " + (comboOperatividad.getSelectedIndex()!=-1 ? comboOperatividad.getSelectedItem().toString() : "")  + " |\n" +
      "* ID Formulario de Solicitud de Credenciales: " + txtCredenciales.getText() + " |");
  }//GEN-LAST:event_btnGenerteActionPerformed

  private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
    cargarCombos();
    limpiarTodo();
  }//GEN-LAST:event_btnClearActionPerformed

  private void txtValorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorKeyReleased
    activeButtons();
  }//GEN-LAST:event_txtValorKeyReleased

  private void comboRaizalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboRaizalItemStateChanged
    if(comboRaizal.getSelectedIndex()!= -1){      
      String aValue =  comboRaizal.getSelectedItem().toString();
      if(aValue.length()!=0 && aValue.contains("\n")){      
        aValue = aValue.substring(0,aValue.indexOf("\n"));
        txtRaizal.setText(aValue);
      }else{
        txtRaizal.setText(aValue);
      }
    } 
  }//GEN-LAST:event_comboRaizalItemStateChanged

  private void comboDiagnosticoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboDiagnosticoItemStateChanged
    if(comboDiagnostico.getSelectedIndex()!= -1){      
      String aValue =  comboDiagnostico.getSelectedItem().toString();
      if(aValue.length()!=0 && aValue.contains("\n")){      
        aValue = aValue.substring(aValue.indexOf("\n"),aValue.length());
        txtDiagnostico.setText(aValue);
      }else{
        txtDiagnostico.setText(aValue);
      }
    }
  }//GEN-LAST:event_comboDiagnosticoItemStateChanged

  private void comboAccionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboAccionItemStateChanged
    if(comboAccion.getSelectedIndex()!= -1){      
      String aValue =  comboAccion.getSelectedItem().toString();
      if(aValue.length()!=0 && aValue.contains("\n")){      
        aValue = aValue.substring(aValue.indexOf("\n"),aValue.length());
        txtAccion.setText(aValue);
      }else{
        txtAccion.setText(aValue);
      }
    }
  }//GEN-LAST:event_comboAccionItemStateChanged

  private void ComboDescripcionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboDescripcionItemStateChanged
    if(ComboDescripcion.getSelectedIndex()!= -1){      
      String aValue =  ComboDescripcion.getSelectedItem().toString();
      if(aValue.length()!=0 && aValue.contains("\n")){      
        aValue = aValue.substring(aValue.indexOf("\n"),aValue.length());
        txtDescripcion.setText(aValue);
      }else{
        txtDescripcion.setText(aValue);
      }
    }
  }//GEN-LAST:event_ComboDescripcionItemStateChanged

  private void limpiarTodo(){
    txtOuput.setText("");
    comboAgrupador.setSelectedIndex(-1);
    comboCausa.setSelectedIndex(-1);
    comboProceso.setSelectedIndex(-1);
    txtRaizal.setText("");    
    comboEstado.setSelectedIndex(-1);
    comboResponsable.setSelectedIndex(-1);
    txtDiagnostico.setText("");
    comboDiagnostico.setSelectedIndex(-1);
    txtAccion.setText("");
    comboAccion.setSelectedIndex(-1);
    txtDescripcion.setText("");
    ComboDescripcion.setSelectedIndex(-1);
    comboOperatividad.setSelectedIndex(-1);
    comboRaizal.setSelectedIndex(-1);
    txtCredenciales.setText("");
  }  

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox<String> ComboDescripcion;
  private javax.swing.JButton btnClear;
  private javax.swing.JButton btnClearLista;
  private javax.swing.JButton btnClearSearch;
  private javax.swing.JButton btnCreateLista;
  private javax.swing.JButton btnGenerte;
  private javax.swing.JButton btnRemoveLista;
  private javax.swing.JButton btnSearch;
  private javax.swing.JButton btnUpdateLista;
  private javax.swing.JComboBox<String> comboAccion;
  private javax.swing.JComboBox<String> comboAgrupador;
  private javax.swing.JComboBox<String> comboCausa;
  private javax.swing.JComboBox<String> comboDiagnostico;
  private javax.swing.JComboBox<String> comboEstado;
  private javax.swing.JComboBox<String> comboOperatividad;
  private javax.swing.JComboBox<String> comboProceso;
  private javax.swing.JComboBox<String> comboRaizal;
  private javax.swing.JComboBox<String> comboResponsable;
  private javax.swing.JComboBox<String> comboTipoLista;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JPanel jPanel6;
  private javax.swing.JPanel jPanel7;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JScrollPane jScrollPane7;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSplitPane jSplitPane2;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JPanel panelDatosLista;
  private javax.swing.JTable tableListas;
  private javax.swing.JTextPane txtAccion;
  private javax.swing.JTextField txtCredenciales;
  private javax.swing.JTextPane txtDescripcion;
  private javax.swing.JTextPane txtDiagnostico;
  private javax.swing.JTextPane txtOuput;
  private javax.swing.JTextField txtRaizal;
  private javax.swing.JTextField txtSearchLista;
  private javax.swing.JTextPane txtValor;
  // End of variables declaration//GEN-END:variables
}
