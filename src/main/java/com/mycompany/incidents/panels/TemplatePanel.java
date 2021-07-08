package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Lista;
import com.mycompany.incidents.entities.Template;
import com.mycompany.incidents.jpaControllers.ListaJpaController;
import com.mycompany.incidents.jpaControllers.TemplateJpaController;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.IncidentException;
import com.mycompany.incidents.otherResources.TableController;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    //String strRsult = "";     
    TemplateJpaController templateController = new TemplateJpaController(factory);
    Template currentTemplate = null;

    public TemplatePanel() {
        initComponents();
        cargarCombos();
        searchListas();
        searchTemplates();
        limpiarTodo();
    }

    private void cargarCombos() {
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
        comboOperatividad.removeAllItems();
        comboOperatividad.addItem("");
        for (Lista aLista : listaList) {
            switch (aLista.getNombre()) {
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
            }

        }
    }

    private void searchListas() {
        List<Lista> listaList = listaController.executeSearchLista(txtSearchLista.getText());
        tableListas.setModel(aTableController.createModel(listaList.toArray(), Lista.columNames()));
        TableController.cofigureSizeColumns(tableListas, Lista.columNames());
        currentLista = null;
        clearListaControls();
        activeButtonsListas();
    }

    private void clearListaControls() {
        comboTipoLista.setSelectedIndex(-1);
        txtValor.setText("");
    }

    private void activeButtonsListas() {
        btnCreateLista.setEnabled(false);
        btnUpdateLista.setEnabled(false);
        btnRemoveLista.setEnabled(false);
        if (comboTipoLista.getSelectedIndex() != -1
                && txtValor.getText().trim().length() != 0) {
            btnCreateLista.setEnabled(true);
        }
        if (currentLista != null) {
            btnRemoveLista.setEnabled(true);
            btnUpdateLista.setEnabled(true);
        }
    }

    private void activeButtonsTemplate() {
        btnCreateTemplate.setEnabled(false);
        btnUpdateTemplate.setEnabled(false);
        btnRemoveTemplate.setEnabled(false);
        if (txtDescripcion.getText().trim().length() != 0) {
            btnCreateTemplate.setEnabled(true);
        }
        if (currentTemplate != null) {
            btnRemoveTemplate.setEnabled(true);
            btnUpdateTemplate.setEnabled(true);
        }
        txtOuput.setText(
              "* Agrupador del Error: " + (comboAgrupador.getSelectedIndex() != -1 ? comboAgrupador.getSelectedItem().toString() : "") + " |\n"
              + "* Causa del Error: " + (comboCausa.getSelectedIndex() != -1 ? comboCausa.getSelectedItem().toString() : "") + " | \n"
              + "* Proceso del Error: " + (comboProceso.getSelectedIndex() != -1 ? comboProceso.getSelectedItem().toString() : "") + " |\n"
              + "* HU Raizal / Mejora: " + txtRaizal.getText() + " | \n"
              + "* Estado Raizal: " + (comboEstado.getSelectedIndex() != -1 ? comboEstado.getSelectedItem().toString() : "") + " | \n"
              + "* Responsable Solucion: " + (comboResponsable.getSelectedIndex() != -1 ? comboResponsable.getSelectedItem().toString() : "") + " | \n"
              + "* Diagnostico: " + txtDiagnostico.getText() + " | \n"
              + "* Accion Ejecutada: " + txtAccion.getText() + " | \n"
              + "* Descripcion de Solucion: " + txtDescripcion.getText() + " |\n"
              + "* Confirmar operatividad del usuario Afectado: " + (comboOperatividad.getSelectedIndex() != -1 ? comboOperatividad.getSelectedItem().toString() : "") + " |\n"
              + "* ID Formulario de Solicitud de Credenciales: " + txtCredenciales.getText() + " |");
    }

    private void updateCurrentLista() throws IncidentException {
        if (currentLista != null) {
            if (txtValor.getText().length() > 2000) {
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

    private void validateChanges() throws IncidentException {
        if (currentLista != null
                && currentLista.getValor().compareTo(txtValor.getText()) != 0) {
            int result = JOptionPane.showConfirmDialog(this, "Existen cambios en la descripción, \n¿desea guardarlos?", "Cambios sin guardar", JOptionPane.YES_NO_OPTION);
            switch (result) {
                case JOptionPane.OK_OPTION:
                    updateCurrentLista();
                    break;
            }
        }
    }

    private void changeIncidentSelection() {
        try {
            validateChanges();
            int idPosition = tableListas.getSelectedRow();
            if (idPosition != -1) {
                idPosition = Integer.parseInt(tableListas.getModel().getValueAt(idPosition, 0).toString());
                currentLista = listaController.findLista(idPosition);
                comboTipoLista.setSelectedItem(currentLista.getNombre());
                txtValor.setText(currentLista.getValor());
            }
            activeButtonsListas();
        } catch (IncidentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchTemplates() {
			List<Template> templatesList = templateController.executeSearchLista(txtSearchTemplate.getText());
			tableTemplates.setModel(aTableController.createModel(templatesList.toArray(), Template.columNames()));
			TableController.cofigureSizeColumns(tableTemplates, Template.columNames());
			currentTemplate = null;
			limpiarTodo();
			activeButtonsTemplate();
    }

    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel4 = new javax.swing.JPanel();
    jSplitPane3 = new javax.swing.JSplitPane();
    jPanel3 = new javax.swing.JPanel();
    btnUpdateTemplate = new javax.swing.JButton();
    btnCreateTemplate = new javax.swing.JButton();
    btnRemoveTemplate = new javax.swing.JButton();
    btnClearTemplate = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    comboAgrupador = new javax.swing.JComboBox<>();
    comboCausa = new javax.swing.JComboBox<>();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    comboProceso = new javax.swing.JComboBox<>();
    txtRaizal = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    comboEstado = new javax.swing.JComboBox<>();
    comboResponsable = new javax.swing.JComboBox<>();
    jScrollPane2 = new javax.swing.JScrollPane();
    txtDiagnostico = new javax.swing.JTextPane();
    jLabel13 = new javax.swing.JLabel();
    jScrollPane3 = new javax.swing.JScrollPane();
    txtAccion = new javax.swing.JTextPane();
    jScrollPane4 = new javax.swing.JScrollPane();
    txtDescripcion = new javax.swing.JTextPane();
    jLabel15 = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    jLabel16 = new javax.swing.JLabel();
    comboOperatividad = new javax.swing.JComboBox<>();
    txtCredenciales = new javax.swing.JTextField();
    jPanel8 = new javax.swing.JPanel();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane6 = new javax.swing.JScrollPane();
    tableTemplates = new javax.swing.JTable();
    jScrollPane1 = new javax.swing.JScrollPane();
    txtOuput = new javax.swing.JTextPane();
    txtSearchTemplate = new javax.swing.JTextField();
    btnSearchTemplate = new javax.swing.JButton();
    btnClearSearchTemplate = new javax.swing.JButton();
    jPanel5 = new javax.swing.JPanel();
    btnUpdateLista = new javax.swing.JButton();
    btnCreateLista = new javax.swing.JButton();
    btnRemoveLista = new javax.swing.JButton();
    btnClearLista = new javax.swing.JButton();
    comboTipoLista = new javax.swing.JComboBox<>();
    jScrollPane7 = new javax.swing.JScrollPane();
    txtValor = new javax.swing.JTextPane();
    btnClearSearchList = new javax.swing.JButton();
    btnSearchList = new javax.swing.JButton();
    txtSearchLista = new javax.swing.JTextField();
    jScrollPane5 = new javax.swing.JScrollPane();
    tableListas = new javax.swing.JTable();

    jSplitPane3.setDividerLocation(370);

    btnUpdateTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
    btnUpdateTemplate.setToolTipText("Actualizar Nota");
    btnUpdateTemplate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnUpdateTemplateActionPerformed(evt);
      }
    });

    btnCreateTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nuevo.png"))); // NOI18N
    btnCreateTemplate.setToolTipText("Crear nueva Nota");
    btnCreateTemplate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateTemplateActionPerformed(evt);
      }
    });

    btnRemoveTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar.png"))); // NOI18N
    btnRemoveTemplate.setToolTipText("Eliminar Nota");
    btnRemoveTemplate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRemoveTemplateActionPerformed(evt);
      }
    });

    btnClearTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/limpiar.png"))); // NOI18N
    btnClearTemplate.setToolTipText("Limpiar formulario");
    btnClearTemplate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearTemplateActionPerformed(evt);
      }
    });

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel1.setText("Agrupador del Error: ");

    comboAgrupador.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboAgrupadorItemStateChanged(evt);
      }
    });

    comboCausa.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboCausaItemStateChanged(evt);
      }
    });

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel2.setText("Causa del error");

    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel3.setText("Proceso del error: ");

    comboProceso.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboProcesoItemStateChanged(evt);
      }
    });

    txtRaizal.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtRaizal.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtRaizalFocusLost(evt);
      }
    });

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel4.setText("HU Raizal / Mejora");

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel5.setText("Estado Raizal");

    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel6.setText("Responsable solucion");

    jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel12.setText("Diagnostico");

    comboEstado.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboEstadoItemStateChanged(evt);
      }
    });

    comboResponsable.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboResponsableItemStateChanged(evt);
      }
    });

    txtDiagnostico.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtDiagnosticoFocusLost(evt);
      }
    });
    jScrollPane2.setViewportView(txtDiagnostico);

    jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel13.setText("Accion ejecutada");

    txtAccion.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtAccionFocusLost(evt);
      }
    });
    jScrollPane3.setViewportView(txtAccion);

    txtDescripcion.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtDescripcionFocusLost(evt);
      }
    });
    jScrollPane4.setViewportView(txtDescripcion);

    jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel15.setText("Descripcion solucion");

    jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel14.setText("Confirmar operatividad");

    jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabel16.setText("ID credenciales");

    comboOperatividad.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboOperatividadItemStateChanged(evt);
      }
    });

    txtCredenciales.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtCredencialesFocusLost(evt);
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
            .addComponent(btnUpdateTemplate)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnCreateTemplate)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnRemoveTemplate)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnClearTemplate)
            .addGap(0, 7, Short.MAX_VALUE))
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtRaizal, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(comboProceso, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(comboCausa, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(comboAgrupador, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
              .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(comboOperatividad, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(comboResponsable, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(comboEstado, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(txtCredenciales))))
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnClearTemplate)
          .addComponent(btnRemoveTemplate)
          .addComponent(btnCreateTemplate)
          .addComponent(btnUpdateTemplate))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(comboAgrupador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(comboCausa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(comboProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(txtRaizal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(comboResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel12)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel13)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel15)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(comboOperatividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCredenciales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    jSplitPane3.setLeftComponent(jPanel3);

    tableTemplates.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableTemplates.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableTemplatesMouseClicked(evt);
      }
    });
    tableTemplates.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        tableTemplatesKeyReleased(evt);
      }
    });
    jScrollPane6.setViewportView(tableTemplates);

    jScrollPane1.setViewportView(txtOuput);

    txtSearchTemplate.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtSearchTemplateKeyPressed(evt);
      }
    });

    btnSearchTemplate.setText("Buscar");
    btnSearchTemplate.setActionCommand("Buscar");
    btnSearchTemplate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSearchTemplateActionPerformed(evt);
      }
    });

    btnClearSearchTemplate.setText("Limpiar");
    btnClearSearchTemplate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearSearchTemplateActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
          .addComponent(jScrollPane1)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(btnClearSearchTemplate)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtSearchTemplate)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearchTemplate)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(txtSearchTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnSearchTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(btnClearSearchTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jTabbedPane1.addTab("PLANTILAS", jPanel1);

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

    btnClearSearchList.setText("Limpiar");
    btnClearSearchList.setToolTipText("");
    btnClearSearchList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearSearchListActionPerformed(evt);
      }
    });

    btnSearchList.setText("Buscar");
    btnSearchList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSearchListActionPerformed(evt);
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

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
          .addComponent(comboTipoLista, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane7)
          .addGroup(jPanel5Layout.createSequentialGroup()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnUpdateLista, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreateLista, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveLista, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearLista, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(btnClearSearchList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchLista)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearchList, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnCreateLista)
          .addComponent(btnUpdateLista)
          .addComponent(btnRemoveLista)
          .addComponent(btnClearLista))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(comboTipoLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnSearchList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(txtSearchLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnClearSearchList))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
        .addContainerGap())
    );

    jTabbedPane1.addTab("LISTAS", jPanel5);

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
      jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel8Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jTabbedPane1)
        .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
      jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel8Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jTabbedPane1)
        .addContainerGap())
    );

    jTabbedPane1.getAccessibleContext().setAccessibleName("LISTAS");

    jSplitPane3.setRightComponent(jPanel8);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane3)
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane3)
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
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
      if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
          try {
              validateChanges();
              searchListas();
          } catch (IncidentException e) {
              JOptionPane.showMessageDialog(this, e.getMessage());
          }
      }
  }//GEN-LAST:event_txtSearchListaKeyPressed

  private void btnSearchListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchListActionPerformed
      try {
          validateChanges();
          searchListas();
      } catch (IncidentException e) {
          JOptionPane.showMessageDialog(this, e.getMessage());
      }
  }//GEN-LAST:event_btnSearchListActionPerformed

  private void btnClearSearchListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchListActionPerformed
      try {
          validateChanges();
          txtSearchLista.setText("");
          searchListas();
      } catch (IncidentException e) {
          JOptionPane.showMessageDialog(this, e.getMessage());
      }
  }//GEN-LAST:event_btnClearSearchListActionPerformed

  private void comboTipoListaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTipoListaItemStateChanged
      activeButtonsListas();
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
      if (JOptionPane.showConfirmDialog(this, "Confirma la eliminación de este registro?") == 0) {
          try {
              listaController.destroy(currentLista.getId());
              searchListas();
          } catch (NonexistentEntityException ex) {
              JOptionPane.showMessageDialog(this, "Error al eliminar nota: \n" + ex.getMessage());
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

  private void txtValorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorKeyReleased
      activeButtonsListas();
  }//GEN-LAST:event_txtValorKeyReleased

    private void btnUpdateTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateTemplateActionPerformed
        currentTemplate.setAgrupador(comboAgrupador.getSelectedItem().toString());
        currentTemplate.setCausa(comboCausa.getSelectedItem().toString());
        currentTemplate.setProceso(comboProceso.getSelectedItem().toString());
        currentTemplate.setRaizal(txtRaizal.getText());
        currentTemplate.setEstadoRaizal(comboEstado.getSelectedItem().toString());
        currentTemplate.setResponsable(comboResponsable.getSelectedItem().toString());
        currentTemplate.setDiagnostico(txtDiagnostico.getText());
        currentTemplate.setAccion(txtAccion.getText());
        currentTemplate.setDescripcion(txtDescripcion.getText());
        currentTemplate.setOperatividad(comboOperatividad.getSelectedItem().toString());
        currentTemplate.setCredenciales(txtCredenciales.getText());
        try {
            templateController.edit(currentTemplate);
        } catch (Exception ex) {
            Logger.getLogger(IncidentsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        searchTemplates();
    }//GEN-LAST:event_btnUpdateTemplateActionPerformed

    private void btnCreateTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateTemplateActionPerformed
        Template newTemplate = new Template();
        newTemplate.setAgrupador(comboAgrupador.getSelectedItem().toString());
        newTemplate.setCausa(comboCausa.getSelectedItem().toString());
        newTemplate.setProceso(comboProceso.getSelectedItem().toString());
        newTemplate.setRaizal(txtRaizal.getText());
        newTemplate.setEstadoRaizal(comboEstado.getSelectedItem().toString());
        newTemplate.setResponsable(comboResponsable.getSelectedItem().toString());
        newTemplate.setDiagnostico(txtDiagnostico.getText());
        newTemplate.setAccion(txtAccion.getText());
        newTemplate.setDescripcion(txtDescripcion.getText());
        newTemplate.setOperatividad(comboOperatividad.getSelectedItem().toString());
        newTemplate.setCredenciales(txtCredenciales.getText());
        templateController.create(newTemplate);
        searchTemplates();
    }//GEN-LAST:event_btnCreateTemplateActionPerformed

    private void btnRemoveTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveTemplateActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Confirma la eliminación de este registro?") == 0) {
            try {
                templateController.destroy(currentTemplate.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(TemplatePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            searchTemplates();
        }
    }//GEN-LAST:event_btnRemoveTemplateActionPerformed

    private void btnClearTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearTemplateActionPerformed
        cargarCombos();
        limpiarTodo();
        searchTemplates();
    }//GEN-LAST:event_btnClearTemplateActionPerformed

		private void loadTemplate(){
			int idPosition = tableTemplates.getSelectedRow();
        if (idPosition != -1) {
					idPosition = Integer.parseInt(tableTemplates.getModel().getValueAt(idPosition, 0).toString());
					currentTemplate = templateController.findTemplate(idPosition);
					comboAgrupador.setSelectedItem(currentTemplate.getAgrupador());
					comboCausa.setSelectedItem(currentTemplate.getCausa());
					comboProceso.setSelectedItem(currentTemplate.getProceso());
					txtRaizal.setText(currentTemplate.getRaizal());
					comboEstado.setSelectedItem(currentTemplate.getEstadoRaizal());
					comboResponsable.setSelectedItem(currentTemplate.getResponsable());
					txtDiagnostico.setText(currentTemplate.getDiagnostico());
					txtAccion.setText(currentTemplate.getAccion());
					txtDescripcion.setText(currentTemplate.getDescripcion());
					comboOperatividad.setSelectedItem(currentTemplate.getOperatividad());
					txtCredenciales.setText(currentTemplate.getCredenciales());
      }
      activeButtonsTemplate();
		}
		
    private void tableTemplatesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTemplatesMouseClicked
      loadTemplate();  
    }//GEN-LAST:event_tableTemplatesMouseClicked

    private void comboAgrupadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboAgrupadorItemStateChanged
        activeButtonsTemplate();
    }//GEN-LAST:event_comboAgrupadorItemStateChanged

    private void comboCausaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboCausaItemStateChanged
        activeButtonsTemplate();
    }//GEN-LAST:event_comboCausaItemStateChanged

    private void comboProcesoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboProcesoItemStateChanged
        activeButtonsTemplate();
    }//GEN-LAST:event_comboProcesoItemStateChanged

    private void txtRaizalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRaizalFocusLost
         activeButtonsTemplate();
    }//GEN-LAST:event_txtRaizalFocusLost

    private void comboEstadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboEstadoItemStateChanged
         activeButtonsTemplate();
    }//GEN-LAST:event_comboEstadoItemStateChanged

    private void comboResponsableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboResponsableItemStateChanged
         activeButtonsTemplate();
    }//GEN-LAST:event_comboResponsableItemStateChanged

    private void txtDiagnosticoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiagnosticoFocusLost
         activeButtonsTemplate();
    }//GEN-LAST:event_txtDiagnosticoFocusLost

    private void txtAccionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccionFocusLost
         activeButtonsTemplate();
    }//GEN-LAST:event_txtAccionFocusLost

    private void txtDescripcionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescripcionFocusLost
         activeButtonsTemplate();
    }//GEN-LAST:event_txtDescripcionFocusLost

    private void comboOperatividadItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboOperatividadItemStateChanged
         activeButtonsTemplate();
    }//GEN-LAST:event_comboOperatividadItemStateChanged

    private void txtCredencialesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCredencialesFocusLost
         activeButtonsTemplate();
    }//GEN-LAST:event_txtCredencialesFocusLost

  private void tableTemplatesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableTemplatesKeyReleased
    loadTemplate();
  }//GEN-LAST:event_tableTemplatesKeyReleased

  private void btnSearchTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchTemplateActionPerformed
    searchTemplates();
  }//GEN-LAST:event_btnSearchTemplateActionPerformed

  private void btnClearSearchTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchTemplateActionPerformed
    txtSearchTemplate.setText("");
    searchTemplates();
  }//GEN-LAST:event_btnClearSearchTemplateActionPerformed

  private void txtSearchTemplateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchTemplateKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      searchTemplates();
    }
  }//GEN-LAST:event_txtSearchTemplateKeyPressed

    private void limpiarTodo() {
        txtOuput.setText("");
        comboAgrupador.setSelectedIndex(-1);
        comboCausa.setSelectedIndex(-1);
        comboProceso.setSelectedIndex(-1);
        txtRaizal.setText("");
        comboEstado.setSelectedIndex(-1);
        comboResponsable.setSelectedIndex(-1);
        txtDiagnostico.setText("");        
        txtAccion.setText("");        
        txtDescripcion.setText("");        
        comboOperatividad.setSelectedIndex(-1);        
        txtCredenciales.setText("");
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnClearLista;
  private javax.swing.JButton btnClearSearchList;
  private javax.swing.JButton btnClearSearchTemplate;
  private javax.swing.JButton btnClearTemplate;
  private javax.swing.JButton btnCreateLista;
  private javax.swing.JButton btnCreateTemplate;
  private javax.swing.JButton btnRemoveLista;
  private javax.swing.JButton btnRemoveTemplate;
  private javax.swing.JButton btnSearchList;
  private javax.swing.JButton btnSearchTemplate;
  private javax.swing.JButton btnUpdateLista;
  private javax.swing.JButton btnUpdateTemplate;
  private javax.swing.JComboBox<String> comboAgrupador;
  private javax.swing.JComboBox<String> comboCausa;
  private javax.swing.JComboBox<String> comboEstado;
  private javax.swing.JComboBox<String> comboOperatividad;
  private javax.swing.JComboBox<String> comboProceso;
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
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JPanel jPanel8;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JScrollPane jScrollPane6;
  private javax.swing.JScrollPane jScrollPane7;
  private javax.swing.JSplitPane jSplitPane3;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JTable tableListas;
  private javax.swing.JTable tableTemplates;
  private javax.swing.JTextPane txtAccion;
  private javax.swing.JTextField txtCredenciales;
  private javax.swing.JTextPane txtDescripcion;
  private javax.swing.JTextPane txtDiagnostico;
  private javax.swing.JTextPane txtOuput;
  private javax.swing.JTextField txtRaizal;
  private javax.swing.JTextField txtSearchLista;
  private javax.swing.JTextField txtSearchTemplate;
  private javax.swing.JTextPane txtValor;
  // End of variables declaration//GEN-END:variables
}
