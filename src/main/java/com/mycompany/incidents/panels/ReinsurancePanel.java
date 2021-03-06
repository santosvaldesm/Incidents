package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Lista;
import com.mycompany.incidents.entities.Reinsurance;
import com.mycompany.incidents.jpaControllers.ListaJpaController;
import com.mycompany.incidents.jpaControllers.ReinsuranceJpaController;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.TableController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
  


public class ReinsurancePanel extends javax.swing.JPanel {
  
  String strRsult = "";   
  TableController aTableController = new TableController();
  EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");  
  ReinsuranceJpaController reinsuranceController = new ReinsuranceJpaController(factory);  
  ListaJpaController listaController = new ListaJpaController(factory);
  Reinsurance currentReinsurance = null;

  public ReinsurancePanel() {
    initComponents();
    activeButtons();
    searchReinsurances();
    searchErrors();
  }
  
  
  
  private void calculatePayload(){
    txtReinsPayload.setText(
      "      <ric:policyNumber>"  + txtReinsPolicy.getText()   + "</ric:policyNumber>\n" +
      "      <ric:coverableID>"   + txtReinsRisk.getText()     + "</ric:coverableID>\n" +
      "      <ric:coverageCode>"  + txtReinsCoverage.getText() + "</ric:coverageCode>\n" +
      "      <ric:date>"          + txtReinsDate.getText()     + "T14:19:00-05:00</ric:date>\n" +
      "      <ric:subPolicyLine>" + txtReinsSubline.getText()  + "</ric:subPolicyLine>\n" +
      "      <ric:offeringType>"  + txtReinsOffering.getText() + "</ric:offeringType>"
    );
  }
  
  private void calculateTransfer(){    
    txtReinsTransfer.setText(
      "Solicitamos su colaboración con el siguiente caso:" +
      "\nPóliza:       " + txtReinsPolicy.getText() +
      "\nSiniestro:    " + txtReinsClaim.getText() +
      "\nfecha:        " + txtReinsDate.getText() +
      "\nriesgo:       " + txtReinsRisk.getText() +
      "\ncobertura:    " + txtReinsCoverage.getText() +
      "\nhomologación: " + txtReinsHomologation.getText() +
      "\nerror:        " + txtReinsError.getText()
    );      
  }
  
  private void calculateQuery(){
    txtQueryPDNLNF.setText(
      "SELECT --f.CDVALOR, \n" +
      "       t.CDVALOR\n" +
      "FROM   TTRD_MAPAS f, \n" +
      "       TTRD_MAPAS t, \n" +
      "       TTRD_HOMOLOGACION h\n" +
      "WHERE  f.CDAPLICACION = 'CORE_GW' AND --fuente\n" +
      "       t.CDAPLICACION = 'GENERAL_GW' AND --destino\n" +
      "       t.CDTIPO =       'COBERTURA_GW' AND --tipo de homologacion\n" +
      "       t.CDTIPO =       t.CDTIPO AND\n" +
      "       f.cdvalor =      '" + txtReinsCoverage.getText() + "' AND--cobertura a buscar\n" +
      "       f.CDMAPA =       h.CDMAPA AND\n" +
      "       t.CDMAPA = h.CDHOMOLOGADO;"
    );
    String queryStr = "SELECT \n" +
                      "  c.cdramo        || '-' ||\n" +
                      "  c.cdsubramo     || '-' ||\n" +
                      "  c.cdgarantia    || '-' ||\n" +
                      "  c.cdsubgarantia  \n" +
                      "FROM   \n" + 
                      "  CUERPOLIZA cu,\n  COBERTURAS c \n";
    
    String queryStrBan = "SELECT \n" +
                      "  c.cdramo        || '-' ||\n" +
                      "  c.cdsubramo     || '-' ||\n" +
                      "  c.cdgarantia    || '-' ||\n" +
                      "  c.cdsubgarantia  \n" +
                      "FROM   \n" + 
                      "  CUERPOLIZA_BAN cu,\n  COBERTURAS_BAN c \n";
    
    String queryStrWhere =  
              "WHERE \n" +
              "  cu.npoliza = '" + txtReinsPolicy.getText() + "' AND \n" +
              "  cu.npoliza = c.npoliza AND \n" +
              "  cu.cdramo = c.cdramo AND \n" +
              "  c.cdsubramo = '" + txtReinsSubline.getText() + "' AND \n" +
              "  cu.cdsubramo = c.cdsubramo AND \n" +
              "  c.ncertificado= '" + txtReinsRisk.getText() + "' AND \n" +
              "  TO_DATE('" + txtReinsDate.getText() + "', 'YYYY-MM-DD') \n" +
              "  BETWEEN c.fefecto AND c.fvencimiento";
    
    txtQueryBanPDN.setText(queryStrBan + queryStrWhere);
    txtQueryPDN.setText(queryStr + queryStrWhere);
  }
  
  private String clearHomologation(String value){
    return value.replaceAll("\n", "").replaceAll("\r", "");
  }
  
  private void calculateHomologation(){    
    
    String[] splitResultPDNLNF = txtResultPDNLNF.getText().split("\n");
    String[] splitResultPDN = txtResultPDN.getText().split("\n");    
    ArrayList<String> listResultHomologation = new ArrayList<>();
    for(String splitPDNLNF : splitResultPDNLNF){
      for(String splitPDN : splitResultPDN){
        if(splitPDNLNF.contains("-") && splitPDN.contains("-")){
          splitPDNLNF = clearHomologation(splitPDNLNF);
          splitPDN    = clearHomologation(splitPDN);
          if(splitPDNLNF.compareTo(splitPDN)==0){
            listResultHomologation.add(splitPDN);
          }
        }        
      } 
    }
    if(listResultHomologation.isEmpty()){
      txtResultHomologation.setText("No se encontro una Homologacion para la cobertura");
    } else {
      String strResult = "";
      for(String anHomologation : listResultHomologation){
        strResult = strResult + anHomologation + "\n";
      }
      txtResultHomologation.setText(strResult);
    }    
  }
  
  /**/
  public void printAgreementDetail(Element ns3Entry) {
    strRsult = strRsult.concat("\n\tCededShare\t\t\t\t = " + ns3Entry.getElementsByTagName("ns5:CededShare").item(0).getTextContent());
    strRsult = strRsult.concat("\n\tCurrency\t\t\t\t = " + ns3Entry.getElementsByTagName("ns5:Currency").item(0).getTextContent());
    strRsult = strRsult.concat("\n\tExchangeRateAmount\t\t = " + ns3Entry.getElementsByTagName("ns5:ExchangeRateAmount").item(0).getTextContent());
    strRsult = strRsult.concat("\n\tName\t\t\t\t\t = " + ns3Entry.getElementsByTagName("ns5:Name").item(0).getTextContent());
    strRsult = strRsult.concat("\n\tProportionalPercentage\t = " + ns3Entry.getElementsByTagName("ns5:ProportionalPercentage").item(0).getTextContent());
    strRsult = strRsult.concat("\n\tRetainedShare\t\t\t = " + ns3Entry.getElementsByTagName("ns5:RetainedShare").item(0).getTextContent());
    strRsult = strRsult.concat("\n\tType\t\t\t\t\t = " + ns3Entry.getElementsByTagName("ns5:Type").item(0).getTextContent());
  }

  public void validateDecimalPart(String decimalStr,String title){
    if(!decimalStr.toLowerCase().contains(" cop")){
      return;
    }
    if(!decimalStr.contains(".")){
      return;
    }
    String decimalPart = decimalStr.substring(decimalStr.indexOf(".")+1,decimalStr.indexOf(" "));    
    for(int i=0;i<decimalPart.length();i++) {
      if(decimalPart.charAt(i)!='.' &&  decimalPart.charAt(i)!='0'){
        strRsult = strRsult.concat("\n\t- El campo " + title + " con la moneda COP no puede tener mas de 0 digitos a la derecha de la coma decimal");
      }
    }    
  }
  
  /**/
  public void validateRange(String title ,BigDecimal value) {  
    if (value.compareTo(new BigDecimal("0.0"))==-1 ||  
        value.compareTo(new BigDecimal("100.0"))==1) {
      strRsult = strRsult.concat("\n\t- " + title + " tiene valor " + value + " (debe estar en el rango de 0 a 100)");
    }            
  }

  /**/
  public void validateOneHundred(String title, BigDecimal value) {  
    if (new BigDecimal("100.0").compareTo(value) != 0) {
      strRsult = strRsult.concat("\n\t- " + title + " tiene valor " + value + " (siempre debe ser 100)");
    }  
  }

  /**/
  public void validateRepeats(int countNs5, NodeList ns5Entries) {    
    int countRepeats = 0;
    
    Element ns5EntrySource = (Element)ns5Entries.item(countNs5);
    String ns4ReinsSuraCodeSource = ns5EntrySource.getElementsByTagName("ns4:ReinsSuraCode").item(0).getTextContent();
      
    for (int countNs5_2 = 0; countNs5_2 < ns5Entries.getLength(); countNs5_2++) {        
      
      Element ns5Entrytarget = (Element)ns5Entries.item(countNs5_2);
      String ns4ReinsSuraCodeTarget = ns5Entrytarget.getElementsByTagName("ns4:ReinsSuraCode").item(0).getTextContent();
      
      if(ns4ReinsSuraCodeSource.compareTo(ns4ReinsSuraCodeTarget) == 0) {
        countRepeats = countRepeats + 1;
      }
    }
    if(countRepeats > 1) {
      strRsult = strRsult.concat("\n\t- Reasegurador "
          + ((Element)ns5Entries.item(countNs5)).getElementsByTagName("ns4:ReinsSuraCode").item(0).getTextContent()
          + " tiene " + countRepeats + " participaciones en el mismo contrato");
    }  
}

  
  
  private String clearPayload(){   
     // Cadena de caracteres original a sustituir.
    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
    // Cadena de caracteres ASCII que reemplazarán los originales.
    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
    String output = txtPayloadReinsurance.getText();
    for (int i=0; i<original.length(); i++) {
        // Reemplazamos los caracteres especiales.
        output = output.replace(original.charAt(i), ascii.charAt(i));
    }//for i
    return output;    
  }
  
  public boolean isValidatTag(String tagName){
    
    return true;
  }
  
  /**/
  public void validateReinsurance() {  
    strRsult = "";  
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = dbf.newDocumentBuilder();            
      Document document = documentBuilder.parse(
              new ByteArrayInputStream(clearPayload().getBytes()));
            
      document.getDocumentElement().normalize();      
      
      BigDecimal totalProportionalPercentage = new BigDecimal("0.0");
      NodeList ns3Entries = document.getElementsByTagName("ns3:Entry");
      for (int countNs3 = 0; countNs3 < ns3Entries.getLength(); countNs3++) {            
        Element ns3Entry = (Element)ns3Entries.item(countNs3);
        String agreementNumber = ns3Entry.getElementsByTagName("ns5:AgreementNumber").item(0).getTextContent();
        strRsult = strRsult.concat("\n----------------------------------------\n\tAgreementNumber\t\t\t = " + agreementNumber);
        
        BigDecimal ns5ProportionalPercentage = BigDecimal.ZERO;
        if(ns3Entry.getElementsByTagName("ns5:ProportionalPercentage").item(0)==null){
          strRsult = strRsult.concat("\n\t- El contrato no tiene participación(ProportionalPercentage)");          
        }else{
          ns5ProportionalPercentage = new BigDecimal(ns3Entry.getElementsByTagName("ns5:ProportionalPercentage").item(0).getTextContent());
        }        
        
        if(ns3Entry.getElementsByTagName("ns5:TopOfLayer").item(0)!=null){
          String ns5TopOfLayer = ns3Entry.getElementsByTagName("ns5:TopOfLayer").item(0).getTextContent();
          validateDecimalPart(ns5TopOfLayer,"TopOfLayer");
        }
        if(ns3Entry.getElementsByTagName("ns5:AttachmentPoint").item(0)!=null){
          String ns5AttachmentPoint = ns3Entry.getElementsByTagName("ns5:AttachmentPoint").item(0).getTextContent();        
          validateDecimalPart(ns5AttachmentPoint,"AttachmentPoint");
        }
        
        BigDecimal totalTreatyParticipation = new BigDecimal("0.0");
        Element ns5AgreementDetails_Ext = (Element)ns3Entry.getElementsByTagName("ns5:AgreementDetails_Ext").item(0);
        NodeList ns5Entries = ns5AgreementDetails_Ext.getElementsByTagName("ns5:Entry");
        //printAgreementDetail(ns3Entry)
        
        validateRange("Participación de contrato " + agreementNumber + " ",ns5ProportionalPercentage);
        totalProportionalPercentage = totalProportionalPercentage.add(ns5ProportionalPercentage);
        for (int countNs5 = 0; countNs5 < ns5Entries.getLength(); countNs5++) {      
          Element ns5Entry = (Element)ns5Entries.item(countNs5);
          String ns4TreatyParticipation = ns5Entry.getElementsByTagName("ns4:TreatyParticipation").item(0).getTextContent();
          BigDecimal treatyParticipation = new BigDecimal(ns4TreatyParticipation);
          validateRepeats(countNs5,ns5Entries);
          validateRange("Participacion de reasegurador ",treatyParticipation);
          totalTreatyParticipation = totalTreatyParticipation.add(treatyParticipation);
        }
        validateOneHundred("Suma de participaciones de reaseguradores en contrato " + agreementNumber + " ",totalTreatyParticipation);
      }
      strRsult = strRsult.concat("\n----------------------------------------");
      validateOneHundred("El total de la suma de las participaciones de los contratos: ",totalProportionalPercentage);
      
    } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
      strRsult = strRsult + e.getMessage();
    } catch (Exception ex) {
      strRsult = strRsult + ex.getMessage();
    }
    txtResultOfValidation.setText(strRsult);
  }
  
  
  private void searchReinsurances(){
        
    List<Reinsurance> noteList = reinsuranceController.findReinsuranceEntities();        
    tableReinsurances.setModel(aTableController.createModel(noteList.toArray(),Reinsurance.columNames()));        
    TableController.cofigureSizeColumns(tableReinsurances,Reinsurance.columNames());
    entityManagerReinsurance.clear();
    currentReinsurance = null;            
    clearReinsuranceControls();
    activeButtons();
  }
  
  
  private void searchErrors(){
    List<Lista> listaList = listaController.serachByName("ErrorReaseg");    
    comboRaizal.removeAllItems();
    comboRaizal.addItem("");
    for(Lista aLista : listaList){
      comboRaizal.addItem(aLista.getValor());      
    }
  }
  
  private void clearReinsuranceControls(){
    txtReinsCode.setText("");
    txtReinsClaim.setText("");
    txtReinsPolicy.setText("");
    txtReinsDate.setText("");
    txtReinsRisk.setText("");
    txtReinsCoverage.setText("");
    txtReinsSubline.setText("");
    txtReinsOffering.setText("");
    txtReinsHomologation.setText("");
    txtReinsError.setText("");
    txtReinsPayload.setText("");
    txtReinsTransfer.setText("");
    clearHomologationControls();
  }
  
  private void clearHomologationControls(){
    txtResultHomologation.setText("");
    txtQueryPDNLNF.setText("");
    txtQueryBanPDN.setText("");
    txtQueryPDN.setText("");
    txtResultPDNLNF.setText("");
    txtResultPDN.setText("");
  }
  
  private void activeButtons() {
    btnCreateIncident.setEnabled(false);        
    btnUpdateIncident.setEnabled(false);        
    btnRemoveIncident.setEnabled(false);        
    if(txtReinsCode.getText().trim().length()!=0){
        btnCreateIncident.setEnabled(true);
    }        
    if(currentReinsurance!=null){
        btnRemoveIncident.setEnabled(true);
        btnUpdateIncident.setEnabled(true);            
    }                               
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    entityManagerReinsurance = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("Incidents_PU").createEntityManager();
    jSplitPane1 = new javax.swing.JSplitPane();
    jTabbedPane2 = new javax.swing.JTabbedPane();
    jPanel7 = new javax.swing.JPanel();
    jScrollPane3 = new javax.swing.JScrollPane();
    tableReinsurances = new javax.swing.JTable();
    jPanel8 = new javax.swing.JPanel();
    jSplitPane4 = new javax.swing.JSplitPane();
    jPanel11 = new javax.swing.JPanel();
    jLabel13 = new javax.swing.JLabel();
    jScrollPane11 = new javax.swing.JScrollPane();
    txtReinsPayload = new javax.swing.JEditorPane();
    jButton1 = new javax.swing.JButton();
    jPanel12 = new javax.swing.JPanel();
    jLabel14 = new javax.swing.JLabel();
    jScrollPane12 = new javax.swing.JScrollPane();
    txtReinsTransfer = new javax.swing.JEditorPane();
    jPanel9 = new javax.swing.JPanel();
    jPanel14 = new javax.swing.JPanel();
    jScrollPane4 = new javax.swing.JScrollPane();
    txtQueryPDNLNF = new javax.swing.JEditorPane();
    jScrollPane5 = new javax.swing.JScrollPane();
    txtQueryPDN = new javax.swing.JEditorPane();
    jScrollPane14 = new javax.swing.JScrollPane();
    txtQueryBanPDN = new javax.swing.JEditorPane();
    jPanel15 = new javax.swing.JPanel();
    jLabel15 = new javax.swing.JLabel();
    jLabel16 = new javax.swing.JLabel();
    jLabel20 = new javax.swing.JLabel();
    jPanel16 = new javax.swing.JPanel();
    jLabel17 = new javax.swing.JLabel();
    jLabel18 = new javax.swing.JLabel();
    jPanel17 = new javax.swing.JPanel();
    jScrollPane6 = new javax.swing.JScrollPane();
    txtResultPDNLNF = new javax.swing.JEditorPane();
    jScrollPane7 = new javax.swing.JScrollPane();
    txtResultPDN = new javax.swing.JEditorPane();
    jLabel19 = new javax.swing.JLabel();
    btnCalculateQuery = new javax.swing.JButton();
    btnCalculateHomologation = new javax.swing.JButton();
    jScrollPane13 = new javax.swing.JScrollPane();
    txtResultHomologation = new javax.swing.JEditorPane();
    jPanel3 = new javax.swing.JPanel();
    jSplitPane5 = new javax.swing.JSplitPane();
    jPanel4 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    txtPayloadReinsurance = new javax.swing.JEditorPane();
    jPanel13 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    txtResultOfValidation = new javax.swing.JTextArea();
    brnValidateReinsurance = new javax.swing.JButton();
    jPanel6 = new javax.swing.JPanel();
    btnUpdateIncident = new javax.swing.JButton();
    btnCreateIncident = new javax.swing.JButton();
    btnRemoveIncident = new javax.swing.JButton();
    btnClearIncident = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    txtReinsCode = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    txtReinsClaim = new javax.swing.JTextField();
    txtReinsPolicy = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    txtReinsDate = new javax.swing.JTextField();
    jLabel7 = new javax.swing.JLabel();
    txtReinsRisk = new javax.swing.JTextField();
    jLabel9 = new javax.swing.JLabel();
    txtReinsSubline = new javax.swing.JTextField();
    txtReinsOffering = new javax.swing.JTextField();
    jLabel10 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    jScrollPane8 = new javax.swing.JScrollPane();
    txtReinsCoverage = new javax.swing.JEditorPane();
    jLabel11 = new javax.swing.JLabel();
    jScrollPane9 = new javax.swing.JScrollPane();
    txtReinsHomologation = new javax.swing.JEditorPane();
    jLabel12 = new javax.swing.JLabel();
    jScrollPane10 = new javax.swing.JScrollPane();
    txtReinsError = new javax.swing.JEditorPane();
    comboRaizal = new javax.swing.JComboBox<>();

    jPanel7.setLayout(new java.awt.GridLayout(2, 1));

    tableReinsurances.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {

      }
    ));
    tableReinsurances.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tableReinsurancesMouseClicked(evt);
      }
    });
    jScrollPane3.setViewportView(tableReinsurances);

    jPanel7.add(jScrollPane3);

    jSplitPane4.setDividerLocation(300);

    jLabel13.setText("Payload Consumo");

    jScrollPane11.setViewportView(txtReinsPayload);

    jButton1.setText("XML a Campos");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
      jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel11Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel11Layout.createSequentialGroup()
            .addComponent(jLabel13)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel11Layout.setVerticalGroup(
      jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel11Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel13)
          .addComponent(jButton1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane4.setLeftComponent(jPanel11);

    jLabel14.setText("Transferencia");
    jLabel14.setToolTipText("");

    jScrollPane12.setViewportView(txtReinsTransfer);

    javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
    jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(
      jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addGroup(jPanel12Layout.createSequentialGroup()
            .addGap(0, 267, Short.MAX_VALUE)
            .addComponent(jLabel14)))
        .addContainerGap())
    );
    jPanel12Layout.setVerticalGroup(
      jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel12Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel14)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane4.setRightComponent(jPanel12);

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
      jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel8Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane4)
        .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
      jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel8Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane4)
        .addContainerGap())
    );

    jPanel7.add(jPanel8);

    jTabbedPane2.addTab("Historico", jPanel7);

    jPanel14.setLayout(new java.awt.GridLayout(1, 3));

    jScrollPane4.setViewportView(txtQueryPDNLNF);

    jPanel14.add(jScrollPane4);

    jScrollPane5.setViewportView(txtQueryPDN);

    jPanel14.add(jScrollPane5);

    jScrollPane14.setViewportView(txtQueryBanPDN);

    jPanel14.add(jScrollPane14);

    jPanel15.setLayout(new java.awt.GridLayout(1, 3));

    jLabel15.setText("Query PDNLNF");
    jPanel15.add(jLabel15);

    jLabel16.setText("QUERY PDN");
    jPanel15.add(jLabel16);

    jLabel20.setText("QUERY BAN PDN");
    jPanel15.add(jLabel20);

    jPanel16.setLayout(new java.awt.GridLayout(1, 2));

    jLabel17.setText("Resultado PDNLNF");
    jPanel16.add(jLabel17);

    jLabel18.setText("Resultado PDN");
    jPanel16.add(jLabel18);

    jPanel17.setLayout(new java.awt.GridLayout(1, 2));

    jScrollPane6.setViewportView(txtResultPDNLNF);

    jPanel17.add(jScrollPane6);

    jScrollPane7.setViewportView(txtResultPDN);

    jPanel17.add(jScrollPane7);

    jLabel19.setText("Homologación");

    btnCalculateQuery.setText("Crear Consultas");
    btnCalculateQuery.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCalculateQueryActionPerformed(evt);
      }
    });

    btnCalculateHomologation.setText("Determinar Homologación");
    btnCalculateHomologation.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCalculateHomologationActionPerformed(evt);
      }
    });

    jScrollPane13.setViewportView(txtResultHomologation);

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
      jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel9Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel9Layout.createSequentialGroup()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
              .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
              .addComponent(jScrollPane13))
            .addContainerGap())
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCalculateQuery)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnCalculateHomologation)
            .addGap(13, 13, 13))))
    );
    jPanel9Layout.setVerticalGroup(
      jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnCalculateQuery)
          .addComponent(btnCalculateHomologation)
          .addComponent(jLabel19))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jTabbedPane2.addTab("Obtener Homologación", jPanel9);

    jSplitPane5.setDividerLocation(250);

    jLabel1.setText("Payload");

    jScrollPane1.setViewportView(txtPayloadReinsurance);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addGap(0, 0, Short.MAX_VALUE))
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGap(18, 18, 18)
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane5.setLeftComponent(jPanel4);

    jLabel2.setText("Result");

    txtResultOfValidation.setColumns(20);
    txtResultOfValidation.setRows(5);
    jScrollPane2.setViewportView(txtResultOfValidation);

    brnValidateReinsurance.setText("Validate");
    brnValidateReinsurance.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        brnValidateReinsuranceActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
    jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(
      jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel13Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel13Layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(brnValidateReinsurance))
          .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel13Layout.setVerticalGroup(
      jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel13Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(brnValidateReinsurance))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane5.setRightComponent(jPanel13);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane5)
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane5)
        .addContainerGap())
    );

    jTabbedPane2.addTab("Validador", jPanel3);

    jSplitPane1.setRightComponent(jTabbedPane2);

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

    jLabel3.setText("Incidente");

    txtReinsCode.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsCodeFocusLost(evt);
      }
    });

    jLabel4.setText("Reclamo");

    txtReinsClaim.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsClaimFocusLost(evt);
      }
    });

    txtReinsPolicy.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsPolicyFocusLost(evt);
      }
    });

    jLabel5.setText("Póliza");

    jLabel6.setText("Fecha");

    txtReinsDate.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsDateFocusLost(evt);
      }
    });

    jLabel7.setText("Riesgo");

    txtReinsRisk.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsRiskFocusLost(evt);
      }
    });

    jLabel9.setText("Subline");

    txtReinsSubline.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsSublineFocusLost(evt);
      }
    });

    txtReinsOffering.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsOfferingFocusLost(evt);
      }
    });

    jLabel10.setText("Offering");

    jLabel8.setText("Cobertura");

    txtReinsCoverage.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsCoverageFocusLost(evt);
      }
    });
    jScrollPane8.setViewportView(txtReinsCoverage);

    jLabel11.setText("Homologación");

    txtReinsHomologation.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsHomologationFocusLost(evt);
      }
    });
    jScrollPane9.setViewportView(txtReinsHomologation);

    jLabel12.setText("Error");

    txtReinsError.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        txtReinsErrorFocusLost(evt);
      }
    });
    jScrollPane10.setViewportView(txtReinsError);

    comboRaizal.setName(""); // NOI18N
    comboRaizal.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        comboRaizalItemStateChanged(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane8)
          .addComponent(jScrollPane10)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
              .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtReinsPolicy)
              .addComponent(txtReinsCode)
              .addComponent(txtReinsRisk))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtReinsSubline)
              .addComponent(txtReinsDate)
              .addComponent(txtReinsClaim)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtReinsOffering))
          .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel11)
              .addComponent(jLabel8))
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(comboRaizal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(txtReinsCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4)
          .addComponent(txtReinsClaim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtReinsPolicy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5)
          .addComponent(jLabel6)
          .addComponent(txtReinsDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtReinsRisk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel7)
          .addComponent(jLabel9)
          .addComponent(txtReinsSubline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtReinsOffering, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel10))
        .addGap(18, 18, 18)
        .addComponent(jLabel8)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel11)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel12)
          .addComponent(comboRaizal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(33, 33, 33))
    );

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
      jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel6Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(jPanel6Layout.createSequentialGroup()
            .addComponent(btnUpdateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnCreateIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnRemoveIncident)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnClearIncident)))
        .addContainerGap())
    );
    jPanel6Layout.setVerticalGroup(
      jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel6Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(btnClearIncident)
          .addComponent(btnRemoveIncident)
          .addComponent(btnCreateIncident)
          .addComponent(btnUpdateIncident))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(229, Short.MAX_VALUE))
    );

    jSplitPane1.setLeftComponent(jPanel6);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane1)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSplitPane1)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

    private void brnValidateReinsuranceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brnValidateReinsuranceActionPerformed
      validateReinsurance();
    }//GEN-LAST:event_brnValidateReinsuranceActionPerformed

  private void tableReinsurancesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableReinsurancesMouseClicked
    int idPosition = tableReinsurances.getSelectedRow();
    if (idPosition != -1) {
      idPosition = Integer.parseInt(tableReinsurances.getModel().getValueAt(idPosition,0).toString());
      currentReinsurance = reinsuranceController.findReinsurance(idPosition);
      txtReinsCode.setText(currentReinsurance.getCode());
      txtReinsClaim.setText(currentReinsurance.getClaim());
      txtReinsPolicy.setText(currentReinsurance.getPolicy());
      txtReinsDate.setText(currentReinsurance.getClaimDate());
      txtReinsRisk.setText(currentReinsurance.getRisk());
      txtReinsCoverage.setText(currentReinsurance.getCoverage());
      txtReinsSubline.setText(currentReinsurance.getSubline());
      txtReinsOffering.setText(currentReinsurance.getOffering());
      txtReinsHomologation.setText(currentReinsurance.getHomologation());
      txtReinsError.setText(currentReinsurance.getError());
      txtReinsPayload.setText(currentReinsurance.getPayload());
      txtReinsTransfer.setText(currentReinsurance.getTransfer());       
    }    
    clearHomologationControls();
    activeButtons();
  }//GEN-LAST:event_tableReinsurancesMouseClicked

  private void btnClearIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearIncidentActionPerformed
    searchReinsurances();
    searchErrors();
  }//GEN-LAST:event_btnClearIncidentActionPerformed

  private void btnRemoveIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveIncidentActionPerformed
    if(JOptionPane.showConfirmDialog(this, "Confirma la eliminación de este registro?")==0){
      try {
        reinsuranceController.destroy(currentReinsurance.getId());
      } catch (NonexistentEntityException ex) {
        Logger.getLogger(IncidentsPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
      searchReinsurances();
    }
  }//GEN-LAST:event_btnRemoveIncidentActionPerformed

  private void btnCreateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateIncidentActionPerformed
    Reinsurance newReinsurance = new Reinsurance();
    newReinsurance.setCode(txtReinsCode.getText());
    newReinsurance.setClaim(txtReinsClaim.getText());
    newReinsurance.setPolicy(txtReinsPolicy.getText());
    newReinsurance.setClaimDate(txtReinsDate.getText());
    newReinsurance.setRisk(txtReinsRisk.getText());
    newReinsurance.setCoverage(txtReinsCoverage.getText());
    newReinsurance.setSubline(txtReinsSubline.getText());
    newReinsurance.setOffering(txtReinsOffering.getText());
    newReinsurance.setHomologation(txtReinsHomologation.getText());
    newReinsurance.setError(txtReinsError.getText());
    newReinsurance.setPayload(txtReinsPayload.getText());
    newReinsurance.setTransfer(txtReinsTransfer.getText());
    reinsuranceController.create(newReinsurance);
    searchReinsurances();
  }//GEN-LAST:event_btnCreateIncidentActionPerformed

  private void btnUpdateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateIncidentActionPerformed
    currentReinsurance.setCode(txtReinsCode.getText());
    currentReinsurance.setClaim(txtReinsClaim.getText());
    currentReinsurance.setPolicy(txtReinsPolicy.getText());
    currentReinsurance.setClaimDate(txtReinsDate.getText());
    currentReinsurance.setRisk(txtReinsRisk.getText());
    currentReinsurance.setCoverage(txtReinsCoverage.getText());
    currentReinsurance.setSubline(txtReinsSubline.getText());
    currentReinsurance.setOffering(txtReinsOffering.getText());
    currentReinsurance.setHomologation(txtReinsHomologation.getText());
    currentReinsurance.setError(txtReinsError.getText());
    currentReinsurance.setPayload(txtReinsPayload.getText());
    currentReinsurance.setTransfer(txtReinsTransfer.getText());
    try {
      reinsuranceController.edit(currentReinsurance);
    } catch (Exception ex) {
      Logger.getLogger(IncidentsPanel.class.getName()).log(Level.SEVERE, null, ex);
    }
    searchReinsurances();
  }//GEN-LAST:event_btnUpdateIncidentActionPerformed

  private void txtReinsCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsCodeFocusLost
    calculatePayload();
    calculateTransfer();
    activeButtons();
  }//GEN-LAST:event_txtReinsCodeFocusLost

  private void txtReinsClaimFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsClaimFocusLost
    calculatePayload();
    calculateTransfer();
  }//GEN-LAST:event_txtReinsClaimFocusLost

  private void txtReinsPolicyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsPolicyFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsPolicyFocusLost

  private void txtReinsDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsDateFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsDateFocusLost

  private void txtReinsRiskFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsRiskFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsRiskFocusLost

  private void txtReinsSublineFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsSublineFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsSublineFocusLost

  private void txtReinsOfferingFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsOfferingFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsOfferingFocusLost

  private void txtReinsCoverageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsCoverageFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsCoverageFocusLost

  private void txtReinsHomologationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsHomologationFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsHomologationFocusLost

  private void txtReinsErrorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReinsErrorFocusLost
    calculatePayload();    
    calculateTransfer();
  }//GEN-LAST:event_txtReinsErrorFocusLost

  private void btnCalculateQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateQueryActionPerformed
    clearHomologationControls();
    calculateQuery();
  }//GEN-LAST:event_btnCalculateQueryActionPerformed

  private void btnCalculateHomologationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateHomologationActionPerformed
    calculateHomologation();
  }//GEN-LAST:event_btnCalculateHomologationActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    
    String inStr = txtReinsPayload.getText();
    
    String policyNumber =  inStr.substring(inStr.indexOf("<ric:policyNumber>")+18,  inStr.indexOf("</ric:policyNumber>"));
    String coverableID =   inStr.substring(inStr.indexOf("<ric:coverableID>")+17,   inStr.indexOf("</ric:coverableID>"));
    String coverageCode =  inStr.substring(inStr.indexOf("<ric:coverageCode>")+18,  inStr.indexOf("</ric:coverageCode>"));
    String date =          inStr.substring(inStr.indexOf("<ric:date>") + 10,        inStr.indexOf("<ric:date>") + 20);
    String subPolicyLine = inStr.substring(inStr.indexOf("<ric:subPolicyLine>")+19, inStr.indexOf("</ric:subPolicyLine>"));
    String offeringType =  inStr.substring(inStr.indexOf("<ric:offeringType>")+18,  inStr.indexOf("</ric:offeringType>"));
    
    txtReinsCode.setText("");
    txtReinsClaim.setText("");
    txtReinsPolicy.setText(policyNumber);
    txtReinsDate.setText(date);
    txtReinsRisk.setText(coverableID);
    txtReinsSubline.setText(subPolicyLine);
    txtReinsOffering.setText(offeringType);
    txtReinsCoverage.setText(coverageCode);
    
  }//GEN-LAST:event_jButton1ActionPerformed

  private void comboRaizalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboRaizalItemStateChanged
    if(comboRaizal.getSelectedIndex()!= -1){      
      txtReinsError.setText(comboRaizal.getSelectedItem().toString());
      calculatePayload();    
      calculateTransfer();
    }
  }//GEN-LAST:event_comboRaizalItemStateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton brnValidateReinsurance;
  private javax.swing.JButton btnCalculateHomologation;
  private javax.swing.JButton btnCalculateQuery;
  private javax.swing.JButton btnClearIncident;
  private javax.swing.JButton btnCreateIncident;
  private javax.swing.JButton btnRemoveIncident;
  private javax.swing.JButton btnUpdateIncident;
  private javax.swing.JComboBox<String> comboRaizal;
  private javax.persistence.EntityManager entityManagerReinsurance;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel19;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel20;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel11;
  private javax.swing.JPanel jPanel12;
  private javax.swing.JPanel jPanel13;
  private javax.swing.JPanel jPanel14;
  private javax.swing.JPanel jPanel15;
  private javax.swing.JPanel jPanel16;
  private javax.swing.JPanel jPanel17;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel6;
  private javax.swing.JPanel jPanel7;
  private javax.swing.JPanel jPanel8;
  private javax.swing.JPanel jPanel9;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane10;
  private javax.swing.JScrollPane jScrollPane11;
  private javax.swing.JScrollPane jScrollPane12;
  private javax.swing.JScrollPane jScrollPane13;
  private javax.swing.JScrollPane jScrollPane14;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JScrollPane jScrollPane6;
  private javax.swing.JScrollPane jScrollPane7;
  private javax.swing.JScrollPane jScrollPane8;
  private javax.swing.JScrollPane jScrollPane9;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSplitPane jSplitPane4;
  private javax.swing.JSplitPane jSplitPane5;
  private javax.swing.JTabbedPane jTabbedPane2;
  private javax.swing.JTable tableReinsurances;
  private javax.swing.JEditorPane txtPayloadReinsurance;
  private javax.swing.JEditorPane txtQueryBanPDN;
  private javax.swing.JEditorPane txtQueryPDN;
  private javax.swing.JEditorPane txtQueryPDNLNF;
  private javax.swing.JTextField txtReinsClaim;
  private javax.swing.JTextField txtReinsCode;
  private javax.swing.JEditorPane txtReinsCoverage;
  private javax.swing.JTextField txtReinsDate;
  private javax.swing.JEditorPane txtReinsError;
  private javax.swing.JEditorPane txtReinsHomologation;
  private javax.swing.JTextField txtReinsOffering;
  private javax.swing.JEditorPane txtReinsPayload;
  private javax.swing.JTextField txtReinsPolicy;
  private javax.swing.JTextField txtReinsRisk;
  private javax.swing.JTextField txtReinsSubline;
  private javax.swing.JEditorPane txtReinsTransfer;
  private javax.swing.JEditorPane txtResultHomologation;
  private javax.swing.JTextArea txtResultOfValidation;
  private javax.swing.JEditorPane txtResultPDN;
  private javax.swing.JEditorPane txtResultPDNLNF;
  // End of variables declaration//GEN-END:variables
}
