package com.mycompany.incidents.panels.modelGW;

import com.mycompany.incidents.entities.GwLobModel;
import com.mycompany.incidents.entities.GwTypeCode;
import com.mycompany.incidents.jpaControllers.GwLobModelJpaController;
import com.mycompany.incidents.jpaControllers.GwTypeCodeJpaController;
import com.mycompany.incidents.otherResources.TableController;
import com.mycompany.incidents.otherResources.TypeKeysEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

public class PanelConfiguration extends javax.swing.JPanel {

  TableController aTableController = new TableController();
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
  GwTypeCodeJpaController typeCodeController = new GwTypeCodeJpaController(factory);  
  GwLobModelJpaController lobModelController = new GwLobModelJpaController(factory);  
  HashMap<TypeKeysEnum,String> searchCriteria = null;
  
  GwTypeCode currentOffering        = null;  
  GwTypeCode currentLossType        = null;
  GwTypeCode currentCostCategory    = null;
  GwTypeCode currentCoverage        = null;  
  GwTypeCode currentCoverageSubtype = null;  
  GwTypeCode currentCovTerm         = null;  
  GwTypeCode currentLossCause       = null;  
  GwTypeCode currentCostType        = null;
  GwTypeCode currentPolicyType        = null;
  
  List<GwTypeCode> offeringList        = typeCodeController.findFilterTypeCodesByCategory(TypeKeysEnum.OfferingType_Ext,"");
  List<GwTypeCode> lossTypeList        = new ArrayList<>();  
  List<GwTypeCode> coverageList        = new ArrayList<>();   
  List<GwTypeCode> coverageSubtypeList = new ArrayList<>();
  List<GwTypeCode> covTermList         = new ArrayList<>();
  List<GwTypeCode> lossCauseList       = new ArrayList<>();
  List<GwTypeCode> costCategoryList    = new ArrayList<>();
  List<GwTypeCode> costTypeList        = new ArrayList<>();  
  List<GwTypeCode> policyTypeList        = new ArrayList<>();  
  
  /*
  private void setCoverage(String coverageCode) {    
    currentCoverage = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CoverageType,coverageCode);    
    //CoverageSUbtype
    searchCriteria = new HashMap<>();
    searchCriteria.put(TypeKeysEnum.OfferingType_Ext, currentOffering.getTypeCode());    
    searchCriteria.put(TypeKeysEnum.CoverageType, currentCoverage.getTypeCode());    
    List<GwLobModel> aList = lobModelController.findBySearchCriteria(searchCriteria);      
    coverageSubtypeList = typeCodeController.createFilteredTypeCodeList(aList,TypeKeysEnum.CoverageSubtype);     
    configureTable(tableCoverageSubtype,coverageSubtypeList.toArray(),GwTypeCode.columNames());    
    setPanelTitle(panelCoverageSubtype,"CoverageSubtypes for Coverage: " + currentCoverage.getTypeCode());    
    //CovTermPattern    
    covTermList = typeCodeController.createFilteredTypeCodeList(aList,TypeKeysEnum.CovTermPattern);     
    configureTable(tableCovTerm,covTermList.toArray(),GwTypeCode.columNames());    
    setPanelTitle(panelCovTerm,"(+)CovTerms for Coverage: " + currentCoverage.getTypeCode());    
    //LossCause
    lossCauseList = typeCodeController.createFilteredTypeCodeList(aList,TypeKeysEnum.LossCause);     
    configureTable(tableLossCause,lossCauseList.toArray(),GwTypeCode.columNames());    
    labelLossCause.setText("LossCauses for Coverage: " + currentCoverage.getTypeCode());    
  }
  */
  /*
  private void setCovTerm(String covTermCode) {    
    currentCovTerm = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CovTermPattern,covTermCode);    
    //CostCategory
    searchCriteria = new HashMap<>();
    searchCriteria.put(TypeKeysEnum.OfferingType_Ext, currentOffering.getTypeCode());    
    searchCriteria.put(TypeKeysEnum.CoverageType, currentCoverage.getTypeCode());    
    searchCriteria.put(TypeKeysEnum.CovTermPattern, currentCovTerm.getTypeCode());    
    List<GwLobModel> aList = lobModelController.findBySearchCriteria(searchCriteria);      
    costCategoryList = typeCodeController.createFilteredTypeCodeList(aList,TypeKeysEnum.CostCategory);     
    configureTable(tableCostCategory,costCategoryList.toArray(),GwTypeCode.columNames());    
    labelCostCategory.setText("(+)CostCategories for CovTerm: " + currentCovTerm.getTypeCode());    
  }
  */
  /*
  private void setCostCategory(String costCategoryCode) {
    currentCostCategory = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CostCategory,costCategoryCode);
    //CostType
    String[] aSplitSearchCode = currentCostCategory.getOtherCategory().split(";");
    costTypeList = new ArrayList<>();
    for(String aSearchCode : aSplitSearchCode) {
      if(aSearchCode.compareTo("-")==0){
        continue;
      }
      boolean isFound = typeCodeController.isFoundTypeCodeInGwTypeCodeList(costTypeList,aSearchCode);      
      if(!isFound && !aSearchCode.isEmpty()){
        costTypeList.add(typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CostType,aSearchCode));
      }
    }     
    configureTable(tableCostType,costTypeList.toArray(),GwTypeCode.columNames());    
    labelCostType.setText("CostTypes for CostCategory: " + currentCostCategory.getTypeCode());    
  }
  */
  
  private void setPanelTitle(JPanel aPanel, String newTitle) {    
    TitledBorder aTitleBorder = (TitledBorder)aPanel.getBorder() ;
    aTitleBorder.setTitle(newTitle);
    aPanel.repaint();
  }
  
  
  void configureTable(JTable table, Object[] toArray, String[] columNames) {
    table.setModel(aTableController.createModel(toArray,columNames));        
    TableController.cofigureSizeColumns(table,columNames);    
  }  
   
  public PanelConfiguration() {
    initComponents();
    searchOffering(); 
  }
  
  
  private void searchOffering() {        
    offeringList = typeCodeController.findFilterTypeCodesByCategory(TypeKeysEnum.OfferingType_Ext,txtFilterOffering.getText());
    configureTable(tableOffering,offeringList.toArray(),GwTypeCode.columNames());     
    //clearInfoByOfferingChange();
    if(tableOffering.getRowCount() > 0){
     tableOffering.changeSelection(0, 1, false, false);
     setOffering(tableOffering.getModel().getValueAt(0,0).toString());        
    }   
  }
  
  private void offeringElementSelected(){
    //clearInfoByOfferingChange();
    int idPosition = -1;
    if(tableOffering.getSelectedRow() != -1){
      idPosition = tableOffering.getSelectedRow();
    }else if(tableOffering.getRowCount() > -1){
       tableOffering.changeSelection(0, 1, false, false);
       idPosition = 0;
    }        
    if (idPosition != -1) {
      setOffering(tableOffering.getModel().getValueAt(idPosition,0).toString());      
    } 
  }
  
  private void setOffering(String offeringCode) {    
    //Offering
    currentOffering = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.OfferingType_Ext,offeringCode);
    searchCriteria = new HashMap<>();
    searchCriteria.put(TypeKeysEnum.OfferingType_Ext, currentOffering.getTypeCode());    
    List<GwLobModel> aList = lobModelController.findBySearchCriteria(searchCriteria);  
    
    //Coverage
    coverageList = typeCodeController.createFilteredTypeCodeList(aList,TypeKeysEnum.CoverageType);    
    configureTable(tableCoverage,coverageList.toArray(),GwTypeCode.columNames());    
    setPanelTitle(panelCoverage,"(+)Coverages for Offering: " + currentOffering.getTypeCode());
    currentCoverage = null;    
    
    //PolicyType  
    policyTypeList = typeCodeController.createFilteredTypeCodeList(aList,TypeKeysEnum.PolicyType);     
    configureTable(tablePolicyType,policyTypeList.toArray(),GwTypeCode.columNames());    
    labelPolicyType.setText("PolicyTypes for Offering: " + currentOffering.getTypeCode());
    
  }
  
  
  
  /*
  private void clearInfoByOfferingChange(){
    Object[] emptyArray = new Object[0];    
    setPanelTitle(panelCoverage,       "(+)Coverages");
    configureTable(tableCoverage,emptyArray,GwTypeCode.columNames());
    labelPolicyType.setText("PolicyTypes");
    configureTable(tablePolicyType,emptyArray,GwTypeCode.columNames());
    clearInfoByCoverageChange();
  }
  */
  /*
  
  
  //coberura cambia => subcoberturas, covterms, loss causes  
  private void clearInfoByCoverageChange(){
    Object[] emptyArray = new Object[0];
    setPanelTitle(panelCoverageSubtype,"CoverageSubtypes");
    configureTable(tableCoverageSubtype,emptyArray,GwTypeCode.columNames());
    setPanelTitle(panelCovTerm,        "(+)CovTerms");
    configureTable(tableCovTerm,emptyArray,GwTypeCode.columNames());
    labelLossCause.setText("LossCauses");
    configureTable(tableLossCause,emptyArray,GwTypeCode.columNames());
    clearInfoByCovTermChange();
  }
  */
  /*
  
//covterms cambia => costcategories  
  private void clearInfoByCovTermChange(){
    Object[] emptyArray = new Object[0];    
    labelCostCategory.setText("(+)CostCategories");
    configureTable(tableCostCategory,emptyArray,GwTypeCode.columNames());
    labelCostType.setText("CostTypes");
    configureTable(tableCostType,emptyArray,GwTypeCode.columNames());
  }
  */
  
  
  
  /*
  private void coverageElementSelected(){
    clearInfoByCoverageChange();
    int idPosition = tableCoverage.getSelectedRow();
    if (idPosition != -1) {
      setCoverage(tableCoverage.getModel().getValueAt(idPosition,0).toString());
    } else if(tableCoverage.getRowCount() > 0) {
      idPosition = 1; //seleccionar primer registro
      setCoverage(tableCoverage.getModel().getValueAt(idPosition,0).toString());
    }
  }
  */
  /*
  private void covTermElementSelected(){
    clearInfoByCovTermChange();
    int idPosition = tableCovTerm.getSelectedRow();
    if (idPosition != -1) {
      setCovTerm(tableCovTerm.getModel().getValueAt(idPosition,0).toString());
    } else if(tableCovTerm.getRowCount() > 0) {
      idPosition = 1; //seleccionar primer registro
      setCovTerm(tableCovTerm.getModel().getValueAt(idPosition,0).toString());
    }
  }
  */
  /*
  private void costCategoryElementSelected(){
    int idPosition = tableCostCategory.getSelectedRow();
    if (idPosition != -1) {
      setCostCategory(tableCostCategory.getModel().getValueAt(idPosition,0).toString());
    } else if(tableCostCategory.getRowCount() > 0) {
      idPosition = 1; //seleccionar primer registro
      setCostCategory(tableCostCategory.getModel().getValueAt(idPosition,0).toString());
    }
  }
  */
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jSplitPane1 = new javax.swing.JSplitPane();
    jPanel1 = new javax.swing.JPanel();
    panelOffering = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableOffering = new javax.swing.JTable();
    btnDetailProduct = new javax.swing.JButton();
    btnRefresh = new javax.swing.JButton();
    txtFilterOffering = new javax.swing.JTextField();
    jScrollPane4 = new javax.swing.JScrollPane();
    tablePolicyType = new javax.swing.JTable();
    labelPolicyType = new javax.swing.JLabel();
    jButton5 = new javax.swing.JButton();
    panelCoverageSubtype = new javax.swing.JPanel();
    jScrollPane6 = new javax.swing.JScrollPane();
    tableCoverageSubtype = new javax.swing.JTable();
    btnDetailCoverage3 = new javax.swing.JButton();
    txtFilterCoverageSubtype = new javax.swing.JTextField();
    jButton3 = new javax.swing.JButton();
    jScrollPane10 = new javax.swing.JScrollPane();
    tableLossType = new javax.swing.JTable();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jScrollPane11 = new javax.swing.JScrollPane();
    tableExposure = new javax.swing.JTable();
    jLabel3 = new javax.swing.JLabel();
    jScrollPane12 = new javax.swing.JScrollPane();
    tableExposureType = new javax.swing.JTable();
    jPanel2 = new javax.swing.JPanel();
    panelCoverage = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    tableCoverage = new javax.swing.JTable();
    btnDetailCoverage = new javax.swing.JButton();
    txtFilterCoverage = new javax.swing.JTextField();
    labelLossCause = new javax.swing.JLabel();
    jScrollPane8 = new javax.swing.JScrollPane();
    tableLossCause = new javax.swing.JTable();
    jButton7 = new javax.swing.JButton();
    panelCovTerm = new javax.swing.JPanel();
    jScrollPane5 = new javax.swing.JScrollPane();
    tableCovTerm = new javax.swing.JTable();
    btnDetailCoverage2 = new javax.swing.JButton();
    txtFilterTerms = new javax.swing.JTextField();
    labelCostCategory = new javax.swing.JLabel();
    labelCostType = new javax.swing.JLabel();
    jScrollPane7 = new javax.swing.JScrollPane();
    tableCostCategory = new javax.swing.JTable();
    jScrollPane9 = new javax.swing.JScrollPane();
    tableCostType = new javax.swing.JTable();
    jButton6 = new javax.swing.JButton();
    btnShowScript = new javax.swing.JButton();
    btnCargarModeloGW = new javax.swing.JButton();
    btnShowQuery1 = new javax.swing.JButton();

    jSplitPane1.setDividerLocation(500);

    jPanel1.setLayout(new java.awt.GridLayout(2, 1));

    panelOffering.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "(+)Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14))); // NOI18N

    tableOffering.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableOffering.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableOfferingMouseClicked(evt);
      }
    });
    tableOffering.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        tableOfferingKeyReleased(evt);
      }
    });
    jScrollPane1.setViewportView(tableOffering);

    btnDetailProduct.setText("Detalles");

    btnRefresh.setText("Refrescar");
    btnRefresh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRefreshActionPerformed(evt);
      }
    });

    txtFilterOffering.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFilterOfferingKeyReleased(evt);
      }
    });

    tablePolicyType.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane4.setViewportView(tablePolicyType);

    labelPolicyType.setText("TIPO DE POLIZA:");

    jButton5.setText("Limpiar");

    javax.swing.GroupLayout panelOfferingLayout = new javax.swing.GroupLayout(panelOffering);
    panelOffering.setLayout(panelOfferingLayout);
    panelOfferingLayout.setHorizontalGroup(
      panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOfferingLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
          .addComponent(labelPolicyType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(txtFilterOffering))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
          .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnDetailProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    panelOfferingLayout.setVerticalGroup(
      panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOfferingLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtFilterOffering, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton5))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addGroup(panelOfferingLayout.createSequentialGroup()
            .addComponent(btnRefresh)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnDetailProduct)
            .addGap(0, 191, Short.MAX_VALUE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(labelPolicyType)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jPanel1.add(panelOffering);

    panelCoverageSubtype.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sub Coberturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14))); // NOI18N

    tableCoverageSubtype.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane6.setViewportView(tableCoverageSubtype);

    btnDetailCoverage3.setText("Detalles");

    jButton3.setText("Limpiar");

    tableLossType.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane10.setViewportView(tableLossType);

    jLabel1.setText("TIPO DE PERDIDA");

    jLabel2.setText("TIPO EXPOSICION");

    tableExposure.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane11.setViewportView(tableExposure);

    jLabel3.setText("EXPOSICION");

    tableExposureType.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane12.setViewportView(tableExposureType);

    javax.swing.GroupLayout panelCoverageSubtypeLayout = new javax.swing.GroupLayout(panelCoverageSubtype);
    panelCoverageSubtype.setLayout(panelCoverageSubtypeLayout);
    panelCoverageSubtypeLayout.setHorizontalGroup(
      panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelCoverageSubtypeLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtFilterCoverageSubtype)
          .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
          .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnDetailCoverage3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );
    panelCoverageSubtypeLayout.setVerticalGroup(
      panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelCoverageSubtypeLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtFilterCoverageSubtype, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelCoverageSubtypeLayout.createSequentialGroup()
            .addComponent(btnDetailCoverage3)
            .addGap(0, 107, Short.MAX_VALUE))
          .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel3)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jPanel1.add(panelCoverageSubtype);

    jSplitPane1.setLeftComponent(jPanel1);

    jPanel2.setLayout(new java.awt.GridLayout(2, 1));

    panelCoverage.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Coberturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14))); // NOI18N

    tableCoverage.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableCoverage.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableCoverageMouseClicked(evt);
      }
    });
    tableCoverage.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        tableCoverageKeyReleased(evt);
      }
    });
    jScrollPane2.setViewportView(tableCoverage);

    btnDetailCoverage.setText("Detalles");

    labelLossCause.setText("CAUSAS:");

    tableLossCause.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane8.setViewportView(tableLossCause);

    jButton7.setText("Limpiar");

    javax.swing.GroupLayout panelCoverageLayout = new javax.swing.GroupLayout(panelCoverage);
    panelCoverage.setLayout(panelCoverageLayout);
    panelCoverageLayout.setHorizontalGroup(
      panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelCoverageLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(labelLossCause, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
          .addComponent(txtFilterCoverage, javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnDetailCoverage, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );
    panelCoverageLayout.setVerticalGroup(
      panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelCoverageLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtFilterCoverage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton7))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelCoverageLayout.createSequentialGroup()
            .addComponent(btnDetailCoverage)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 236, Short.MAX_VALUE))
          .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(labelLossCause)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jPanel2.add(panelCoverage);

    panelCovTerm.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Terminos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14))); // NOI18N

    tableCovTerm.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableCovTerm.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableCovTermMouseClicked(evt);
      }
    });
    tableCovTerm.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        tableCovTermKeyReleased(evt);
      }
    });
    jScrollPane5.setViewportView(tableCovTerm);

    btnDetailCoverage2.setText("Detalles");

    labelCostCategory.setText("CATEGORIA DE COSTO:");

    labelCostType.setText("TIPO DE COSTO: ");

    tableCostCategory.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableCostCategory.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableCostCategoryMouseClicked(evt);
      }
    });
    tableCostCategory.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        tableCostCategoryKeyReleased(evt);
      }
    });
    jScrollPane7.setViewportView(tableCostCategory);

    tableCostType.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    jScrollPane9.setViewportView(tableCostType);

    jButton6.setText("Limpiar");

    javax.swing.GroupLayout panelCovTermLayout = new javax.swing.GroupLayout(panelCovTerm);
    panelCovTerm.setLayout(panelCovTermLayout);
    panelCovTermLayout.setHorizontalGroup(
      panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelCovTermLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
          .addComponent(labelCostType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(labelCostCategory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(txtFilterTerms, javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
          .addComponent(btnDetailCoverage2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    panelCovTermLayout.setVerticalGroup(
      panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(panelCovTermLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtFilterTerms, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton6))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(panelCovTermLayout.createSequentialGroup()
            .addComponent(btnDetailCoverage2)
            .addGap(0, 128, Short.MAX_VALUE))
          .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(labelCostCategory)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(labelCostType)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
        .addContainerGap())
    );

    jPanel2.add(panelCovTerm);

    jSplitPane1.setRightComponent(jPanel2);

    btnShowScript.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interrogation.png"))); // NOI18N
    btnShowScript.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnShowScriptActionPerformed(evt);
      }
    });

    btnCargarModeloGW.setText("Cargar Modelo");
    btnCargarModeloGW.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCargarModeloGWActionPerformed(evt);
      }
    });

    btnShowQuery1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interrogation.png"))); // NOI18N
    btnShowQuery1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnShowQuery1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jSplitPane1)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnCargarModeloGW, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnShowScript)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnShowQuery1)
            .addGap(0, 0, Short.MAX_VALUE))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(btnShowQuery1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnShowScript, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnCargarModeloGW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jSplitPane1)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void tableOfferingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOfferingMouseClicked
    offeringElementSelected();
  }//GEN-LAST:event_tableOfferingMouseClicked

  private void tableCoverageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCoverageMouseClicked
    //coverageElementSelected();
  }//GEN-LAST:event_tableCoverageMouseClicked

  private void tableCovTermMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCovTermMouseClicked
    //covTermElementSelected();   
  }//GEN-LAST:event_tableCovTermMouseClicked

  private void tableCostCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCostCategoryMouseClicked
    //costCategoryElementSelected();
  }//GEN-LAST:event_tableCostCategoryMouseClicked

  private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
    //refreshTables();    
  }//GEN-LAST:event_btnRefreshActionPerformed

  private void btnShowScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowScriptActionPerformed
    DialogHelp dialog = new DialogHelp(null,true);
    dialog.setTextContent("\n\n//TODO: en las funciones de llamado para lob model permitir seleccionar si requiere retirados o no\n//en el programa cargar y tachar los retirados\n\nuses java.io.File\nuses java.io.FileOutputStream\nuses java.io.PrintWriter\nuses java.util.HashMap\nuses java.text.SimpleDateFormat\nuses java.util.Date\nuses java.util.ArrayList\nuses gw.api.util.LocaleUtil\nuses gw.i18n.ILocale\nuses gw.entity.TypeKey\nuses gw.entity.ITypeList\nuses gw.lang.reflect.TypeSystem\n\nvar _esCO : ILocale = LocaleUtil.toLanguage(typekey.LanguageType.TC_ES_CO)\nvar _enUS : ILocale = LocaleUtil.toLanguage(typekey.LanguageType.TC_EN_US)\n\nvar _dateFormat = new SimpleDateFormat(\"HH:mm:ss\")\nvar _startDate = new Date()\nvar _pathOut = \"D:/\" + _startDate + \"_outLobModels\"\nvar _writeLobModel : PrintWriter = null\nvar _writeLob : PrintWriter = null\nvar _offering = \"\"\nvar _lobCode = \"\"\nvar _policyType = \"\"\nvar _lossType = \"\"\nvar _coverageType = \"\"\nvar _internalPolicyType = \"\"\nvar _policyTab = \"\"\nvar _lossPartyType = \"\"\nvar _coverageSubType = \"\"\nvar _exposureType = \"\"\nvar _covTermPattern = \"\"\nvar _coverageSubtypeClass = \"\"\nvar _limitDeducible = \"\"\nvar _lossCauses = \"\"\nvar _costCategories = \"\"\nvar _mapCovTermsByCoverage : HashMap<String, List<typekey.CovTermPattern>> = null\nvar _mapCostCategoryByCovTerm : HashMap<String, String> = null\nvar _mapLossPartyByCoverageSubType : HashMap<String, String> = null\nvar _mapLossCauseByCoverageType : HashMap<String, String> = null\nvar _mapOfferingByCov : HashMap<String, String> = null\nvar _mapLossTypeByLobCode : HashMap<String, String> = null\nvar _mapInternalPolicyByPolicyType : HashMap<String, String> = null\nvar _mapPolicyTabByPolicyType : HashMap<String, String> = null\n\nstartProcess()\n\n/**/\nfunction startProcess() {\n  createDirectories()\n  print(timeEvent(\"Directorios cargados... \"))\n\n  generateTypeListByName(\"OfferingType_Ext\")\n  generateTypeListByName(\"LOBCode\")\n  generateTypeListByName(\"PolicyType\")\n  generateTypeListByName(\"LossType\")\n  generateTypeListByName(\"CoverageType\")\n  generateTypeListByName(\"InternalPolicyType\")\n  generateTypeListByName(\"PolicyTab\")\n  generateTypeListByName(\"LossPartyType\")\n  generateTypeListByName(\"CoverageSubtype\")\n  generateTypeListByName(\"ExposureType\")\n  generateTypeListByName(\"CovTermPattern\")\n  generateTypeListByName(\"LossCause\")\n  generateTypeListByName(\"CostType\")\n  generateTypeListByNameAndCategory(\"CostCategory\",typekey.CostType)\n  print(timeEvent(\"TypeList cargados... \"))\n\n  _mapCovTermsByCoverage         = loadMapCovTermsByCoverage()\n  _mapCostCategoryByCovTerm      = loadMapTypeListWithConcatenatedCategory(\"CostCategory\",typekey.CovTermPattern)\n  _mapLossPartyByCoverageSubType = loadMapTypeListWithConcatenatedCategory(\"LossPartyType\",typekey.CoverageSubtype)\n  _mapLossCauseByCoverageType    = loadMapTypeListWithConcatenatedCategory(\"LossCause\",typekey.CoverageType)\n  _mapOfferingByCov              = loadMapTypeListWithConcatenatedCategory(typekey.OfferingType_Ext,\"CoverageType\")\n  _mapLossTypeByLobCode          = loadMapTypeListWithConcatenatedCategory(typekey.LossType,\"LOBCode\")\n  _mapInternalPolicyByPolicyType = loadMapTypeListWithConcatenatedCategory(typekey.InternalPolicyType,\"PolicyType\")\n  _mapPolicyTabByPolicyType      = loadMapTypeListWithConcatenatedCategory(typekey.PolicyTab,\"PolicyType\")\n  print(timeEvent(\"Mapas cargados... \"))\n\n  generateLobModel()\n  print(timeEvent(\"FIN, LobModel cargado \"))\n}\n\n\n/**/\nfunction generateLobModel() {\n  var header = (\"Offering\\t\" + \"LOBCode\\t\" + \"PolicyType\\t\" + \"LossType\\t\" + \"CoverageType\\t\" + \"InternalPolicyType\\t\" + \"PolicyTab\\t\" + \"LossPartyType\\t\" + \"CoverageSubtype\\t\" + \"ExposureType\\t\" + \"CovTermPattern\\t\" + \"CoverageSubtypeClass\\t\" + \"Limite o Deducible\\t\" + \"Causas por cobertura\\t\" + \"Categorias de costo\")\n  var fileLobModel = new File(_pathOut + \"/LobModel.txt\")\n  fileLobModel.write(header)\n  _writeLobModel = new PrintWriter(new FileOutputStream(fileLobModel, true))\n  _writeLobModel.write(\"\\n\")\n  typekey.LOBCode.getTypeKeys(false).where(\\ lob -> lob != typekey.LOBCode.TC_PERSONALAUTOLINE).each(\\lob -> {\n    lob.Categories.whereTypeIs(PolicyType).each(\\ polType -> {\n      polType.Categories.whereTypeIs(CoverageType).where(\\ cov -> !cov.Retired).each(\\ cov -> {\n        cov.Categories.whereTypeIs(CoverageSubtype).where(\\subcov -> !subcov.Retired).each(\\ subcov -> {\n          var exp = subcov.Categories.whereTypeIs(typekey.ExposureType).first()\n          _coverageSubtypeClass = determineCoverageSubtypeClass(subcov)\n          _lobCode = lob.Code\n          _policyType = polType.Code\n          _coverageType = cov.Code\n          _coverageSubType = subcov.Code\n          _exposureType = exp.Code\n          _offering = _mapOfferingByCov.get(cov.Code)\n          _lossType = _mapLossTypeByLobCode.get(lob.Code)\n          _internalPolicyType = _mapInternalPolicyByPolicyType.get(polType.Code)\n          _policyTab = _mapPolicyTabByPolicyType.get(polType.Code)\n          _lossPartyType = _mapLossPartyByCoverageSubType.get(subcov.Code)\n          _lossCauses = _mapLossCauseByCoverageType.get(cov.Code) ?: \"\"\n          var covTermByCov = _mapCovTermsByCoverage.get(cov.Code)\n          var CovTermPatternList = covTermByCov?.where(\\covT ->\n                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).contains(subcov)) or\n                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).IsEmpty and covT.Categories.whereTypeIs(typekey.ExposureType).contains(exp)) or\n                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).IsEmpty and covT.Categories.whereTypeIs(typekey.ExposureType).IsEmpty))\n          if(CovTermPatternList.HasElements) {\n            CovTermPatternList.each(\\covTerm -> {\n              _limitDeducible = getLimOrDed(covTerm)\n              _covTermPattern = covTerm.Code\n              _costCategories = _mapCostCategoryByCovTerm.get(covTerm.Code) ?: \"\"\n              write()\n            })\n          }else {\n            _covTermPattern = \"\"\n            _limitDeducible = \"\"\n            _costCategories = \"\"\n            write()\n          }\n        })\n      })\n    })\n  })\n  _writeLobModel.flush()\n  _writeLobModel.close()\n}\n\n/**/\nfunction createDirectories() {\n  if (!getFile(_pathOut).exists()) {\n    getFile(_pathOut).mkdirs()\n  }\n}\n\n/**/\nfunction getFile(path : String) : File {\n  return new File(path)\n}\n/**/\nfunction timeEvent(valueText : String) : String {\n  var currentDate = new Date()\n  return valueText + _dateFormat.format(currentDate) + \" Duracion \" + timeDiff(_startDate,new Date()) + \" segundos\"\n}\n\n/**/\nfunction timeDiff(fechaInicio : Date, fechaTermino : Date) : float {\n  return ((fechaTermino.getTime() / 1000) - (fechaInicio.getTime() / 1000))\n}\n\n/**/\nfunction getLimOrDed(term : CovTermPattern) : String {\n  for (filter in typekey.CovTermPattern.Type.TypeFilters) {\n    if (filter.Includes.contains(term)) {\n      return filter.Name.replace(\"_Ext\", \"\")\n    }\n  }\n  return \"\"\n}\n\n/**/\nfunction determineCoverageSubtypeClass(subcov : typekey.CoverageSubtype) : String {\n  if (subcov.Code.contains(\"Art\")) {\n    return \"Articulo\"\n  }\n  if (subcov.Code.contains(\"Bkt\") or subcov.Code.contains(\"Blanket\") or subcov.Code.contains(\"BKT\")) {\n    return \"Blanket\"\n  }\n  if (subcov.Code.contains(\"Ubi\")) {\n    return \"Ubicacion\"\n  }\n  return \"\"\n}\n\n/**/\nfunction write() {\n  var body = (_offering + \"\\t\" + _lobCode + \"\\t\" + _policyType + \"\\t\" + _lossType + \"\\t\" + _coverageType + \"\\t\" +\n      _internalPolicyType + \"\\t\" + _policyTab + \"\\t\" + _lossPartyType + \"\\t\" + _coverageSubType + \"\\t\" + _exposureType + \"\\t\" +\n      _covTermPattern + \"\\t\" + _coverageSubtypeClass + \"\\t\" + _limitDeducible + \"\\t\" + _lossCauses + \"\\t\" + _costCategories)\n  _writeLobModel.write(body)\n  _writeLobModel.write(\"\\n\")\n}\n\n/**/\nfunction generateTypeListByNameAndCategory(typeListName : String, aTypeTarget : Type<TypeKey>) {\n  var aTypeList = TypeSystem.getByFullName(\"typekey.\" + typeListName) as ITypeList\n  var fileTypeList = new File(_pathOut + \"/\" + typeListName + \".txt\")\n  fileTypeList.write(\"Code\\tNameES\\tNameUS\\t\" + typeListName + \"\\n\")\n  var writerTypeList = new PrintWriter(new FileOutputStream(fileTypeList, true))\n  aTypeList.getTypeKeys(false).each(\\ aTypeKey -> {\n    writerTypeList.write(aTypeKey.Code + \"\\t\" + aTypeKey.getDisplayName(_esCO) + \"\\t\" + aTypeKey.getDisplayName(_enUS) + \"\\t\" +\n        aTypeKey.Categories.whereTypeIs(aTypeTarget)*.Code.join(\";\") + \"\\n\")\n  })\n  writerTypeList.flush()\n  writerTypeList.close()\n}\n\n/**/\nfunction generateTypeListByName(typeListName : String) {\n  var aTypeList = TypeSystem.getByFullName(\"typekey.\" + typeListName) as ITypeList\n  var fileTypeList = new File(_pathOut + \"/\" + typeListName + \".txt\")\n  fileTypeList.write(\"Code\\tNameES\\tNameUS\\n\")\n  var writerTypeList = new PrintWriter(new FileOutputStream(fileTypeList, true))\n  aTypeList.getTypeKeys(false).each(\\ aTypeKey -> {\n    writerTypeList.write(aTypeKey.Code + \"\\t\" + aTypeKey.getDisplayName(_esCO) + \"\\t\" + aTypeKey.getDisplayName(_enUS) + \"\\n\")\n  })\n  writerTypeList.flush()\n  writerTypeList.close()\n}\n\n/**/\nfunction loadMapCovTermsByCoverage() : HashMap<String, List<typekey.CovTermPattern>> {\n  var mapCovTermsByCoverage = new HashMap<String, List<typekey.CovTermPattern>>()\n  for (aCovTerm in typekey.CovTermPattern.getTypeKeys(false)) {\n    var coverageList =  aCovTerm.Categories.whereTypeIs(typekey.CoverageType)\n    for (aCoverage in coverageList) {\n      var aResultSearch = mapCovTermsByCoverage.get(aCoverage.Code)\n      if(aResultSearch == null) {\n        aResultSearch = new ArrayList<typekey.CovTermPattern>()\n      }\n      aResultSearch.add(aCovTerm)\n      mapCovTermsByCoverage.put(aCoverage.Code, aResultSearch)\n    }\n  }\n  return mapCovTermsByCoverage\n}\n\n/**\n* se usa cuando en la categoria esta la clave del mapa y en los typecodes(del primer parametro) estan los valores a concatenar\n*/\nfunction loadMapTypeListWithConcatenatedCategory(typeKeyName : String, categoryFilter : Type<TypeKey>) : HashMap<String, String> {\n  var mapReturn = new HashMap<String, String>()\n  var aTypeKey = TypeSystem.getByFullName(\"typekey.\" + typeKeyName) as ITypeList\n  for (aTypeCode in aTypeKey.getTypeKeys(false)) {\n    var categories =  aTypeCode.Categories.whereTypeIs(categoryFilter)\n    for (aCategory in categories) {\n      var mapResultSearch = mapReturn.get(aCategory.Code)\n      mapReturn.put(aCategory.Code, mapResultSearch == null ? aTypeCode.Code : (mapResultSearch + \";\" + aTypeCode.Code))\n    }\n  }\n  return mapReturn\n}\n\n\n/**\n* se usa cuando en el typecode(del segundo parametro) esta la clave del mapa y en las categorias estan los valores a concatenar\n*/\nfunction loadMapTypeListWithConcatenatedCategory(categoryFilter : Type<TypeKey>, typeKeyName : String) : HashMap<String, String> {\n  var mapReturn = new HashMap<String, String>()\n  var aTypeKey = TypeSystem.getByFullName(\"typekey.\" + typeKeyName) as ITypeList\n  for (aTypeCode in aTypeKey.getTypeKeys(false)) {\n    var categoriesConcatenate =  aTypeCode.Categories.whereTypeIs(categoryFilter).where(\\aCate -> not aCate.Retired)*.Code.join(\";\")\n    mapReturn.put(aTypeCode.Code, categoriesConcatenate)\n  }\n  return mapReturn\n}");
    dialog.setVisible(true);
  }//GEN-LAST:event_btnShowScriptActionPerformed

  private void btnCargarModeloGWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarModeloGWActionPerformed
    DialogLoadModel dialog = new DialogLoadModel(null,true);
    dialog.setVisible(true);
  }//GEN-LAST:event_btnCargarModeloGWActionPerformed
   
  private void tableOfferingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableOfferingKeyReleased
    offeringElementSelected();
  }//GEN-LAST:event_tableOfferingKeyReleased

  private void tableCoverageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCoverageKeyReleased
    //coverageElementSelected();
  }//GEN-LAST:event_tableCoverageKeyReleased

  private void tableCovTermKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCovTermKeyReleased
    //covTermElementSelected();
  }//GEN-LAST:event_tableCovTermKeyReleased

  private void tableCostCategoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCostCategoryKeyReleased
    //costCategoryElementSelected();
  }//GEN-LAST:event_tableCostCategoryKeyReleased

  private void btnShowQuery1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowQuery1ActionPerformed
    DialogHelp dialog = new DialogHelp(null,true);
    String aText = "MEJORAS POR REALIZAR\n";
    aText = aText + "1. De SubCoberturas salen:\n";
    aText = aText + "   - Tipo de perdida -> LossType\n";
    aText = aText + "   - Exposición	-> ExposureType\n";
    aText = aText + "   - TipoExposicion  -> CoverageSUbtypeClassification\n";
    aText = aText + "\n";
    aText = aText + "2. Termino lleva un campo:\n";
    aText = aText + "   - Limit/deductible  (columna limite o deducible) sale de CovTermPattern.Type(colocarla como una nueva columna)\n";
    aText = aText + "3. Causa lleva un campo\n";
    aText = aText + "   - Tipo de causa(colocarla como una nueva columna)\n";
    aText = aText + "4. Sacar tambien el modelo de autos\n";
    dialog.setTextContent(aText);
    dialog.setVisible(true);
  }//GEN-LAST:event_btnShowQuery1ActionPerformed

  private void txtFilterOfferingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilterOfferingKeyReleased
    searchOffering();
  }//GEN-LAST:event_txtFilterOfferingKeyReleased


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCargarModeloGW;
  private javax.swing.JButton btnDetailCoverage;
  private javax.swing.JButton btnDetailCoverage2;
  private javax.swing.JButton btnDetailCoverage3;
  private javax.swing.JButton btnDetailProduct;
  private javax.swing.JButton btnRefresh;
  private javax.swing.JButton btnShowQuery1;
  private javax.swing.JButton btnShowScript;
  private javax.swing.JButton jButton3;
  private javax.swing.JButton jButton5;
  private javax.swing.JButton jButton6;
  private javax.swing.JButton jButton7;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane10;
  private javax.swing.JScrollPane jScrollPane11;
  private javax.swing.JScrollPane jScrollPane12;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JScrollPane jScrollPane6;
  private javax.swing.JScrollPane jScrollPane7;
  private javax.swing.JScrollPane jScrollPane8;
  private javax.swing.JScrollPane jScrollPane9;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JLabel labelCostCategory;
  private javax.swing.JLabel labelCostType;
  private javax.swing.JLabel labelLossCause;
  private javax.swing.JLabel labelPolicyType;
  private javax.swing.JPanel panelCovTerm;
  private javax.swing.JPanel panelCoverage;
  private javax.swing.JPanel panelCoverageSubtype;
  private javax.swing.JPanel panelOffering;
  private javax.swing.JTable tableCostCategory;
  private javax.swing.JTable tableCostType;
  private javax.swing.JTable tableCovTerm;
  private javax.swing.JTable tableCoverage;
  private javax.swing.JTable tableCoverageSubtype;
  private javax.swing.JTable tableExposure;
  private javax.swing.JTable tableExposureType;
  private javax.swing.JTable tableLossCause;
  private javax.swing.JTable tableLossType;
  private javax.swing.JTable tableOffering;
  private javax.swing.JTable tablePolicyType;
  private javax.swing.JTextField txtFilterCoverage;
  private javax.swing.JTextField txtFilterCoverageSubtype;
  private javax.swing.JTextField txtFilterOffering;
  private javax.swing.JTextField txtFilterTerms;
  // End of variables declaration//GEN-END:variables

  
}
