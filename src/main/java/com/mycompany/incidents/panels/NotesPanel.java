package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Note;
import com.mycompany.incidents.jpaControllers.NoteJpaController;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.IncidentException;
import com.mycompany.incidents.otherResources.NoteTypeEnum;
import com.mycompany.incidents.otherResources.TableController;
import com.mycompany.incidents.otherResources.TextRichHelper;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class NotesPanel extends javax.swing.JPanel {

	TableController aTableController = new TableController();
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
	NoteJpaController noteController = new NoteJpaController(factory);
	int txtNoteId = 0;
	Note currentNote = null;
	TextRichHelper aTextRichHelper = null;

	public NotesPanel() {
		initComponents();
		activeButtons();

		aTextRichHelper = new TextRichHelper(txtNoteDescription);
		txtNoteDescription.setStyledDocument(aTextRichHelper.doc);

		searchNotes();
	}

	public DefaultComboBoxModel<String> getTypesOfNote() {
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
		NoteTypeEnum[] values = NoteTypeEnum.values();
		for (NoteTypeEnum value : values) {
			comboModel.addElement(value.toString());
		}
		return comboModel;
	}

	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    entityManagerNotes = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("Incidents_PU").createEntityManager();
    jSplitPane1 = new javax.swing.JSplitPane();
    jPanel1 = new javax.swing.JPanel();
    txtSearchNote = new javax.swing.JTextField();
    btnClearSearch = new javax.swing.JButton();
    btnSearch = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    tableNotes = new javax.swing.JTable();
    jPanel3 = new javax.swing.JPanel();
    btnUpdateIncident = new javax.swing.JButton();
    btnCreateIncident = new javax.swing.JButton();
    btnRemoveIncident = new javax.swing.JButton();
    btnClearIncident = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jScrollPane3 = new javax.swing.JScrollPane();
    txtNoteDescription = new javax.swing.JTextPane();
    txtNoteDescription = new JTextPane(){
      @Override
      public boolean getScrollableTracksViewportWidth() {
        return getUI().getPreferredSize(this).width
        <= getParent().getSize().width;
      }
    };
    txtSearchedText = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    txtNoteTitle = new javax.swing.JTextField();

    txtSearchNote.setToolTipText("");
    txtSearchNote.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtSearchNoteKeyPressed(evt);
      }
    });

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

    tableNotes.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableNotes.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableNotesMouseClicked(evt);
      }
    });
    tableNotes.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        tableNotesKeyReleased(evt);
      }
    });
    jScrollPane1.setViewportView(tableNotes);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtSearchNote)
              .addComponent(btnClearSearch))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(btnClearSearch)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtSearchNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane1.setLeftComponent(jPanel1);

    btnUpdateIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
    btnUpdateIncident.setToolTipText("Actualizar Nota");
    btnUpdateIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnUpdateIncidentActionPerformed(evt);
      }
    });

    btnCreateIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nuevo.png"))); // NOI18N
    btnCreateIncident.setToolTipText("Crear nueva Nota");
    btnCreateIncident.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateIncidentActionPerformed(evt);
      }
    });

    btnRemoveIncident.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar.png"))); // NOI18N
    btnRemoveIncident.setToolTipText("Eliminar Nota");
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

    jLabel1.setText("Título");

    jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    txtNoteDescription.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtNoteDescriptionKeyReleased(evt);
      }
    });
    jScrollPane3.setViewportView(txtNoteDescription);

    txtSearchedText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtSearchedTextKeyReleased(evt);
      }
    });

    jLabel3.setText("Buscar");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane3)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(btnUpdateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnCreateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnRemoveIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnClearIncident)
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtNoteTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtSearchedText, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(btnUpdateIncident, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnCreateIncident, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnRemoveIncident, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(btnClearIncident, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(txtSearchedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3)
          .addComponent(txtNoteTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane1.setRightComponent(jPanel3);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jSplitPane1)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jSplitPane1)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

	private void searchNotes() {
		List<Note> noteList = noteController.executeSearchIncident(txtSearchNote.getText());
		tableNotes.setModel(aTableController.createModel(noteList.toArray(), Note.columNames()));
		TableController.cofigureSizeColumns(tableNotes, Note.columNames());
		entityManagerNotes.clear();
		currentNote = null;
		clearNoteControls();
		activeButtons();
		setSearchActions();
	}

	private void setSearchActions() {
		txtSearchedText.setText(txtSearchNote.getText());
		aTextRichHelper.searchedText = aTextRichHelper.normalizeText(txtSearchedText.getText());
		aTextRichHelper.applyStyles();
	}

	private void clearNoteControls() {
		txtNoteDescription.setText("");
		txtNoteTitle.setText("");
	}

	private void activeButtons() {
		btnCreateIncident.setEnabled(false);
		btnUpdateIncident.setEnabled(false);
		btnRemoveIncident.setEnabled(false);
		if (txtNoteDescription.getText().trim().length() != 0
						&& txtNoteTitle.getText().trim().length() != 0) {
			btnCreateIncident.setEnabled(true);
		}
		if (currentNote != null) {
			btnRemoveIncident.setEnabled(true);
			btnUpdateIncident.setEnabled(true);
		}
	}

	private void updateCurrentIncident() throws IncidentException {
		if (currentNote != null) {
			if (txtNoteDescription.getText().length() > 20000) {
				throw new IncidentException("El numero de caracteres de la descripción supera el limite de 20.000 caracteres");
			}
			currentNote.setTypenote(txtNoteTitle.getText());
			currentNote.setDescription(txtNoteDescription.getText());
			try {
				noteController.edit(currentNote);
			} catch (Exception ex) {
				throw new IncidentException("Error al editar la nota\n" + ex.getMessage());
			}
		}
	}

	private void validateChanges() throws IncidentException {
		if (currentNote != null
						&& currentNote.getDescription().compareTo(txtNoteDescription.getText()) != 0) {
			int result = JOptionPane.showConfirmDialog(this, "Existen cambios en la descripción, \n¿desea guardarlos?", "Cambios sin guardar", JOptionPane.YES_NO_OPTION);
			switch (result) {
				case JOptionPane.OK_OPTION:
					updateCurrentIncident();
					break;
			}
		}
	}

	private String validateASCII(String input) {
		StringBuilder myString = new StringBuilder(input);
		int sizeString = myString.length();
		for (int i = 0; i < sizeString; i++) {
			if ((int) myString.charAt(i) > 255) {
				myString.deleteCharAt(i);
				sizeString--;
				i--;
			}
		}
		return myString.toString();
	}

	private void changeIncidentSelection() {
		try {
			validateChanges();
			int idPosition = tableNotes.getSelectedRow();
			if (idPosition != -1) {
				idPosition = Integer.parseInt(tableNotes.getModel().getValueAt(idPosition, 0).toString());
				currentNote = noteController.findNote(idPosition);
				txtNoteTitle.setText(currentNote.getTypenote());
				txtNoteDescription.setText(validateASCII(currentNote.getDescription()));
			}
			aTextRichHelper.applyStyles();//TODO: activar cuando se complete
			activeButtons();
		} catch (IncidentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

  private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
		try {
			validateChanges();
			searchNotes();
		} catch (IncidentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

  }//GEN-LAST:event_btnSearchActionPerformed

  private void tableNotesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableNotesMouseClicked
		changeIncidentSelection();
  }//GEN-LAST:event_tableNotesMouseClicked

  private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
		try {
			validateChanges();
			txtSearchNote.setText("");
			searchNotes();
		} catch (IncidentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
  }//GEN-LAST:event_btnClearSearchActionPerformed

  private void txtSearchNoteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchNoteKeyPressed
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				validateChanges();
				searchNotes();
			} catch (IncidentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
  }//GEN-LAST:event_txtSearchNoteKeyPressed

  private void btnClearIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearIncidentActionPerformed
		try {
			validateChanges();
			searchNotes();
		} catch (IncidentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
  }//GEN-LAST:event_btnClearIncidentActionPerformed

  private void btnRemoveIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveIncidentActionPerformed
		if (JOptionPane.showConfirmDialog(this, "Confirma la eliminación de este registro?") == 0) {
			try {
				noteController.destroy(currentNote.getId());
				searchNotes();
			} catch (NonexistentEntityException ex) {
				JOptionPane.showMessageDialog(this, "Error al eliminar nota: \n" + ex.getMessage());
			}
		}
  }//GEN-LAST:event_btnRemoveIncidentActionPerformed

  private void btnCreateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateIncidentActionPerformed
		Note newNote = new Note();
		newNote.setTypenote(txtNoteTitle.getText());
		newNote.setDescription(txtNoteDescription.getText());
		noteController.create(newNote);
		searchNotes();
  }//GEN-LAST:event_btnCreateIncidentActionPerformed

  private void btnUpdateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateIncidentActionPerformed
		try {
			updateCurrentIncident();
		} catch (IncidentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
  }//GEN-LAST:event_btnUpdateIncidentActionPerformed

  private void txtSearchedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchedTextKeyReleased
		aTextRichHelper.searchedText = aTextRichHelper.normalizeText(txtSearchedText.getText());
		aTextRichHelper.applyStyles();
  }//GEN-LAST:event_txtSearchedTextKeyReleased

  private void txtNoteDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoteDescriptionKeyReleased
		activeButtons();
		aTextRichHelper.applyStyles();
  }//GEN-LAST:event_txtNoteDescriptionKeyReleased

  private void tableNotesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableNotesKeyReleased
		try {
			validateChanges();
			changeIncidentSelection();
		} catch (IncidentException e) {
			JOptionPane.showConfirmDialog(this, e.getMessage());
		}
  }//GEN-LAST:event_tableNotesKeyReleased

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnClearIncident;
  private javax.swing.JButton btnClearSearch;
  private javax.swing.JButton btnCreateIncident;
  private javax.swing.JButton btnRemoveIncident;
  private javax.swing.JButton btnSearch;
  private javax.swing.JButton btnUpdateIncident;
  private javax.persistence.EntityManager entityManagerNotes;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTable tableNotes;
  private javax.swing.JTextPane txtNoteDescription;
  private javax.swing.JTextField txtNoteTitle;
  private javax.swing.JTextField txtSearchNote;
  private javax.swing.JTextField txtSearchedText;
  // End of variables declaration//GEN-END:variables
}
