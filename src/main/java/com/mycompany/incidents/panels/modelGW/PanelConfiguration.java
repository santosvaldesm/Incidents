package com.mycompany.incidents.panels.modelGW;

import com.mycompany.incidents.entities.GwLobModel;
import com.mycompany.incidents.entities.GwTypeCode;
import com.mycompany.incidents.jpaControllers.GwLobModelJpaController;
import com.mycompany.incidents.jpaControllers.GwTypeCodeJpaController;
import com.mycompany.incidents.otherResources.IncidentsUtil;
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
    HashMap<TypeKeysEnum, String> searchCriteria = null;

    GwTypeCode currentOffering = null;
    GwTypeCode currentLossPartyType = null;
    GwTypeCode currentCostCategory = null;
    GwTypeCode currentCoverage = null;
    GwTypeCode currentCoverageSubtype = null;
    GwTypeCode currentCovTerm = null;
    GwTypeCode currentLossCause = null;
    GwTypeCode currentCostType = null;
    GwTypeCode currentPolicyType = null;
    GwTypeCode currentExposure = null;
    GwTypeCode currentExposureType = null;

    List<GwTypeCode> offeringList = typeCodeController.findFilterTypeCodesByCategory(TypeKeysEnum.OfferingType_Ext, "");
    List<GwTypeCode> lossPartyTypeList = new ArrayList<>();
    List<GwTypeCode> coverageList = new ArrayList<>();
    List<GwTypeCode> coverageSubtypeList = new ArrayList<>();
    List<GwTypeCode> covTermList = new ArrayList<>();
    List<GwTypeCode> lossCauseList = new ArrayList<>();
    List<GwTypeCode> costCategoryList = new ArrayList<>();
    List<GwTypeCode> costTypeList = new ArrayList<>();
    List<GwTypeCode> policyTypeList = new ArrayList<>();
    List<GwTypeCode> exposureList = new ArrayList<>();
    List<GwTypeCode> exposureTypeList = new ArrayList<>();

    private void setCoverage(String coverageCode) {
        currentCoverage = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CoverageType, coverageCode);
        //CoverageSUbtype
        searchCriteria = new HashMap<>();
        searchCriteria.put(TypeKeysEnum.OfferingType_Ext, currentOffering.getTypeCode());
        searchCriteria.put(TypeKeysEnum.CoverageType, currentCoverage.getTypeCode());
        List<GwLobModel> aList = lobModelController.findBySearchCriteria(searchCriteria);
        coverageSubtypeList = typeCodeController.createFilteredTypeCodeList(aList, TypeKeysEnum.CoverageSubtype);
        configureTable(tableCoverageSubtype, coverageSubtypeList.toArray(), GwTypeCode.columNames());
        setPanelTitle(panelCoverageSubtype, "SUBCOBERTURAS PARA COBERTURA: " + currentCoverage.getTypeCode());
        //CovTermPattern    
        covTermList = typeCodeController.createFilteredTypeCodeList(aList, TypeKeysEnum.CovTermPattern);
        configureTable(tableCovTerm, covTermList.toArray(), GwTypeCode.columNamesWhitType());
        setPanelTitle(panelCovTerm, "TERMINOS PARA COBERTURA: " + currentCoverage.getTypeCode());
        //LossCause
        lossCauseList = typeCodeController.createFilteredTypeCodeList(aList, TypeKeysEnum.LossCause);
        configureTable(tableLossCause, lossCauseList.toArray(), GwTypeCode.columNamesWhitType());
        labelLossCause.setText("CAUSAS PARA COBERTURA: " + currentCoverage.getTypeCode());
    }

    private void setCovTerm(String covTermCode) {
        currentCovTerm = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CovTermPattern, covTermCode);
        //CostCategory
        searchCriteria = new HashMap<>();
        searchCriteria.put(TypeKeysEnum.OfferingType_Ext, currentOffering.getTypeCode());
        searchCriteria.put(TypeKeysEnum.CoverageType, currentCoverage.getTypeCode());
        searchCriteria.put(TypeKeysEnum.CovTermPattern, currentCovTerm.getTypeCode());
        List<GwLobModel> aList = lobModelController.findBySearchCriteria(searchCriteria);
        costCategoryList = typeCodeController.createFilteredTypeCodeList(aList, TypeKeysEnum.CostCategory);
        configureTable(tableCostCategory, costCategoryList.toArray(), GwTypeCode.columNames());
        labelCostCategory.setText("CATEGORIAS DE COSTO PARA TERMINO: " + currentCovTerm.getTypeCode());
    }

    private void setCostCategory(String costCategoryCode) {
        currentCostCategory = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CostCategory, costCategoryCode);
        //CostType
        String[] aSplitSearchCode = currentCostCategory.getOtherCategory().split(";");
        costTypeList = new ArrayList<>();
        for (String aSearchCode : aSplitSearchCode) {
            if (aSearchCode.compareTo("-") == 0) {
                continue;
            }
            boolean isFound = typeCodeController.isFoundTypeCodeInGwTypeCodeList(costTypeList, aSearchCode);
            if (!isFound && !aSearchCode.isEmpty()) {
                costTypeList.add(typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.CostType, aSearchCode));
            }
        }
        configureTable(tableCostType, costTypeList.toArray(), GwTypeCode.columNames());
        labelCostType.setText("TIPO DE COSTO PARA CATEGORIA DE COSTO: " + currentCostCategory.getTypeCode());
    }

    private void setCoverageSubtype(String coverageSubtypeCode) {
        //CoverageSUbtype
        searchCriteria = new HashMap<>();
        searchCriteria.put(TypeKeysEnum.OfferingType_Ext, currentOffering.getTypeCode());
        searchCriteria.put(TypeKeysEnum.CoverageType, currentCoverage.getTypeCode());
        searchCriteria.put(TypeKeysEnum.CoverageSubtype, coverageSubtypeCode);        
        List<GwLobModel> aListCoverageSubtype = lobModelController.findBySearchCriteria(searchCriteria);
        GwLobModel firstCoverageSubtype =  aListCoverageSubtype.get(0); //En teoria solo deberia haber un registro en aListCoverageSubtype        
        //Tipo de perdida
        labelLossPartyType.setText("PARTE RESPONSABLE para " + coverageSubtypeCode);
        currentLossPartyType = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.LossPartyType, firstCoverageSubtype.getLossPartyType());        
        lossPartyTypeList = new ArrayList<>();        
        lossPartyTypeList.add(currentLossPartyType);        
        configureTable(tableLossPartyType, lossPartyTypeList.toArray(), GwTypeCode.columNames());        
        //Exposicion
        labelExposure.setText("EXPOSICION  para " + coverageSubtypeCode);
        currentExposure = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.ExposureType, firstCoverageSubtype.getExposureType());        
        exposureList = new ArrayList<>();        
        exposureList.add(currentExposure);        
        configureTable(tableExposure, exposureList.toArray(), GwTypeCode.columNames());        
        //Tipo de exposicion
        labelExposureType.setText("TIPO DE EXPOSICION  para " + coverageSubtypeCode);
        currentExposureType = new GwTypeCode();
        currentExposureType.setTypeKeyName("ExposureType");
        currentExposureType.setTypeCode(firstCoverageSubtype.getCoverageSubtypeClass());
        currentExposureType.setNameEs(firstCoverageSubtype.getCoverageSubtypeClass());
        exposureTypeList = new ArrayList<>();        
        exposureTypeList.add(currentExposureType);        
        configureTable(tableExposureType, exposureTypeList.toArray(), GwTypeCode.columNames());  
    }

    private void setPanelTitle(JPanel aPanel, String newTitle) {
        TitledBorder aTitleBorder = (TitledBorder) aPanel.getBorder();
        aTitleBorder.setTitle(newTitle);
        aPanel.repaint();
    }

    void configureTable(JTable table, Object[] toArray, String[] columNames) {
        table.setModel(aTableController.createModel(toArray, columNames));
        TableController.cofigureSizeColumns(table, columNames);
    }

    public PanelConfiguration() {
        initComponents();
        searchOffering();
    }

    private void searchOffering() {
        offeringList = typeCodeController.findFilterTypeCodesByCategory(TypeKeysEnum.OfferingType_Ext, txtFilterOffering.getText());
        configureTable(tableOffering, offeringList.toArray(), GwTypeCode.columNames());
        clearInfoByOfferingChange();
        if (tableOffering.getRowCount() > 0) {
            tableOffering.changeSelection(0, 1, false, false);
            setOffering(tableOffering.getModel().getValueAt(0, 0).toString());
        }
    }

    private void offeringElementSelected() {
        clearInfoByOfferingChange();
        int idPosition = -1;
        if (tableOffering.getSelectedRow() != -1) {
            idPosition = tableOffering.getSelectedRow();
        } else if (tableOffering.getRowCount() > -1) {
            tableOffering.changeSelection(0, 1, false, false);
            idPosition = 0;
        }
        if (idPosition != -1) {
            setOffering(tableOffering.getModel().getValueAt(idPosition, 0).toString());
        }
    }

    private void setOffering(String offeringCode) {
        //Offering
        currentOffering = typeCodeController.findTypeCodeByCategoryAndCode(TypeKeysEnum.OfferingType_Ext, offeringCode);
        searchCriteria = new HashMap<>();
        searchCriteria.put(TypeKeysEnum.OfferingType_Ext, currentOffering.getTypeCode());
        List<GwLobModel> aList = lobModelController.findBySearchCriteria(searchCriteria);

        //Coverage
        coverageList = typeCodeController.createFilteredTypeCodeList(aList, TypeKeysEnum.CoverageType);
        configureTable(tableCoverage, coverageList.toArray(), GwTypeCode.columNames());
        setPanelTitle(panelCoverage, "COBERTURAS DEL PRODUCTO: " + currentOffering.getTypeCode());
        currentCoverage = null;

        //PolicyType  
        policyTypeList = typeCodeController.createFilteredTypeCodeList(aList, TypeKeysEnum.PolicyType);
        configureTable(tablePolicyType, policyTypeList.toArray(), GwTypeCode.columNames());
        labelPolicyType.setText("TIPOS DE POLIZA PARA EL PRODUCTO: " + currentOffering.getTypeCode());

    }

    private void clearInfoByOfferingChange() {
        Object[] emptyArray = new Object[0];
        setPanelTitle(panelCoverage, "COBERTURAS");
        configureTable(tableCoverage, emptyArray, GwTypeCode.columNames());
        labelPolicyType.setText("TIPOS DE POLIZA");
        configureTable(tablePolicyType, emptyArray, GwTypeCode.columNames());
        clearInfoByCoverageChange();
    }

    //coberura cambia => subcoberturas, covterms, loss causes  
    private void clearInfoByCoverageChange() {
        Object[] emptyArray = new Object[0];
        setPanelTitle(panelCoverageSubtype, "SUBCOBERTURAS");
        configureTable(tableCoverageSubtype, emptyArray, GwTypeCode.columNames());
        
        labelLossPartyType.setText("PARTE RESPONSABLE");
        configureTable(tableLossPartyType, emptyArray, GwTypeCode.columNames());        
        
        labelExposure.setText("EXPOSICION");
        configureTable(tableExposure, emptyArray, GwTypeCode.columNames());
        
        labelExposureType.setText("TIPO DE EXPOSICION");
        configureTable(tableExposureType, emptyArray, GwTypeCode.columNames());
        
        setPanelTitle(panelCovTerm, "TERMINOS");
        configureTable(tableCovTerm, emptyArray, GwTypeCode.columNamesWhitType());
        
        labelLossCause.setText("CAUSAS");
        configureTable(tableLossCause, emptyArray, GwTypeCode.columNamesWhitType());
        
        clearInfoByCovTermChange();
    }

    //covterms cambia => costcategories  
    private void clearInfoByCovTermChange() {
        Object[] emptyArray = new Object[0];
        labelCostCategory.setText("CATEGORIAS DE COSTO");
        configureTable(tableCostCategory, emptyArray, GwTypeCode.columNames());
        labelCostType.setText("TIPOS DE COSTO");
        configureTable(tableCostType, emptyArray, GwTypeCode.columNames());
    }

    private void coverageElementSelected() {
        clearInfoByCoverageChange();
        int idPosition = tableCoverage.getSelectedRow();
        if (idPosition != -1) {
            setCoverage(tableCoverage.getModel().getValueAt(idPosition, 0).toString());
        } else if (tableCoverage.getRowCount() > 0) {
            idPosition = 1; //seleccionar primer registro
            setCoverage(tableCoverage.getModel().getValueAt(idPosition, 0).toString());
        }
    }

    private void covTermElementSelected() {
        clearInfoByCovTermChange();
        int idPosition = tableCovTerm.getSelectedRow();
        if (idPosition != -1) {
            setCovTerm(tableCovTerm.getModel().getValueAt(idPosition, 0).toString());
        } else if (tableCovTerm.getRowCount() > 0) {
            idPosition = 1; //seleccionar primer registro
            setCovTerm(tableCovTerm.getModel().getValueAt(idPosition, 0).toString());
        }
    }

    private void costCategoryElementSelected() {
        int idPosition = tableCostCategory.getSelectedRow();
        if (idPosition != -1) {
            setCostCategory(tableCostCategory.getModel().getValueAt(idPosition, 0).toString());
        } else if (tableCostCategory.getRowCount() > 0) {
            idPosition = 1; //seleccionar primer registro
            setCostCategory(tableCostCategory.getModel().getValueAt(idPosition, 0).toString());
        }
    }

    private void coverageSubTypeElementSelected() {
        int idPosition = tableCoverageSubtype.getSelectedRow();
        if (idPosition != -1) {
            setCoverageSubtype(tableCoverageSubtype.getModel().getValueAt(idPosition, 0).toString());
        } else if (tableCoverageSubtype.getRowCount() > 0) {
            idPosition = 1; //seleccionar primer registro
            setCoverageSubtype(tableCoverageSubtype.getModel().getValueAt(idPosition, 0).toString());
        }
    }

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
        tableLossPartyType = new javax.swing.JTable();
        labelLossPartyType = new javax.swing.JLabel();
        labelExposureType = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tableExposure = new javax.swing.JTable();
        labelExposure = new javax.swing.JLabel();
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

        jSplitPane1.setDividerLocation(500);

        jPanel1.setLayout(new java.awt.GridLayout(2, 1));

        panelOffering.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PRODUCTOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14))); // NOI18N

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
            .addGroup(panelOfferingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOfferingLayout.createSequentialGroup()
                        .addComponent(txtFilterOffering)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetailProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelPolicyType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        panelOfferingLayout.setVerticalGroup(
            panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOfferingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelOfferingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilterOffering, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(btnRefresh)
                    .addComponent(btnDetailProduct))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPolicyType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(panelOffering);

        panelCoverageSubtype.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sub Coberturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14))); // NOI18N

        tableCoverageSubtype.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableCoverageSubtype.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCoverageSubtypeMouseClicked(evt);
            }
        });
        tableCoverageSubtype.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableCoverageSubtypeKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(tableCoverageSubtype);

        btnDetailCoverage3.setText("Detalles");

        jButton3.setText("Limpiar");

        tableLossPartyType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane10.setViewportView(tableLossPartyType);

        labelLossPartyType.setText("TIPO DE PERDIDA");

        labelExposureType.setText("TIPO EXPOSICION");

        tableExposure.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane11.setViewportView(tableExposure);

        labelExposure.setText("EXPOSICION");

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
                    .addGroup(panelCoverageSubtypeLayout.createSequentialGroup()
                        .addComponent(txtFilterCoverageSubtype)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetailCoverage3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelLossPartyType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelExposureType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelExposure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane12)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        panelCoverageSubtypeLayout.setVerticalGroup(
            panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoverageSubtypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCoverageSubtypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilterCoverageSubtype, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(btnDetailCoverage3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelLossPartyType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelExposure)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelExposureType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addGroup(panelCoverageLayout.createSequentialGroup()
                        .addComponent(txtFilterCoverage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetailCoverage, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelLossCause, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addContainerGap())
        );
        panelCoverageLayout.setVerticalGroup(
            panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCoverageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCoverageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilterCoverage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7)
                    .addComponent(btnDetailCoverage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelLossCause)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addGroup(panelCovTermLayout.createSequentialGroup()
                        .addComponent(txtFilterTerms)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDetailCoverage2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelCostType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7)
                    .addComponent(labelCostCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        panelCovTermLayout.setVerticalGroup(
            panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCovTermLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCovTermLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilterTerms, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6)
                    .addComponent(btnDetailCoverage2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCostCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCostType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
      coverageElementSelected();
  }//GEN-LAST:event_tableCoverageMouseClicked

  private void tableCovTermMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCovTermMouseClicked
      covTermElementSelected();
  }//GEN-LAST:event_tableCovTermMouseClicked

  private void tableCostCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCostCategoryMouseClicked
      costCategoryElementSelected();
  }//GEN-LAST:event_tableCostCategoryMouseClicked

  private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
      //refreshTables();    
  }//GEN-LAST:event_btnRefreshActionPerformed

  private void btnShowScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowScriptActionPerformed
      DialogHelp dialog = new DialogHelp(null, true);      
			dialog.setTextContent(IncidentsUtil.getText("ProductModelHelp"));
      dialog.setVisible(true);
  }//GEN-LAST:event_btnShowScriptActionPerformed

  private void btnCargarModeloGWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarModeloGWActionPerformed
      DialogLoadModel dialog = new DialogLoadModel(null, true);
      dialog.setVisible(true);
  }//GEN-LAST:event_btnCargarModeloGWActionPerformed

  private void tableOfferingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableOfferingKeyReleased
      offeringElementSelected();
  }//GEN-LAST:event_tableOfferingKeyReleased

  private void tableCoverageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCoverageKeyReleased
      coverageElementSelected();
  }//GEN-LAST:event_tableCoverageKeyReleased

  private void tableCovTermKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCovTermKeyReleased
      covTermElementSelected();
  }//GEN-LAST:event_tableCovTermKeyReleased

  private void tableCostCategoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCostCategoryKeyReleased
      costCategoryElementSelected();
  }//GEN-LAST:event_tableCostCategoryKeyReleased

  private void txtFilterOfferingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilterOfferingKeyReleased
      searchOffering();
  }//GEN-LAST:event_txtFilterOfferingKeyReleased

    private void tableCoverageSubtypeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCoverageSubtypeKeyReleased
        coverageSubTypeElementSelected();
    }//GEN-LAST:event_tableCoverageSubtypeKeyReleased

    private void tableCoverageSubtypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCoverageSubtypeMouseClicked
        coverageSubTypeElementSelected();
    }//GEN-LAST:event_tableCoverageSubtypeMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCargarModeloGW;
    private javax.swing.JButton btnDetailCoverage;
    private javax.swing.JButton btnDetailCoverage2;
    private javax.swing.JButton btnDetailCoverage3;
    private javax.swing.JButton btnDetailProduct;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnShowScript;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
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
    private javax.swing.JLabel labelExposure;
    private javax.swing.JLabel labelExposureType;
    private javax.swing.JLabel labelLossCause;
    private javax.swing.JLabel labelLossPartyType;
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
    private javax.swing.JTable tableLossPartyType;
    private javax.swing.JTable tableOffering;
    private javax.swing.JTable tablePolicyType;
    private javax.swing.JTextField txtFilterCoverage;
    private javax.swing.JTextField txtFilterCoverageSubtype;
    private javax.swing.JTextField txtFilterOffering;
    private javax.swing.JTextField txtFilterTerms;
    // End of variables declaration//GEN-END:variables

}
