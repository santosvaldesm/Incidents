package com.mycompany.incidents.otherResources;

import java.sql.*;
import javax.swing.JOptionPane;

public class DataBaseController {    
  public Connection conn;
  public boolean mostrarErrores = true;
  Statement st;
  ResultSet rs;
  String msj = "";
  
    
  public void DataBaseController() {
  }  

  public void connect() throws SQLException, ClassNotFoundException {
    try {            
      String databaseName = "incidentesDB";
      String driver = "org.apache.derby.jdbc.EmbeddedDriver";
      String dbParam = "create=true"; //crear base de datos si no existe
      //String dbParam = "shutdown=true"; //usar esta linea para que la desconecte(cuando aparece base de datos en uso)
                                        //no usar esta linea con base de datos real por que elimina los datos
      String connectionURL = "jdbc:derby:" + databaseName + ";" + dbParam;
      Class.forName(driver);
      conn = DriverManager.getConnection(connectionURL);
      System.out.println("Conexion realizada a: " + connectionURL + " ... OK");            
    } catch (ClassNotFoundException e) {     
      throw new ClassNotFoundException("Driver no encontrado \n" + e.getMessage());            
    } catch(SQLException e){
      throw new SQLException("Base de datos en uso \n" + e.getMessage());            
    }
  }

  public void disconect() throws SQLException {    
    if (conn != null) {      
      if (!conn.isClosed()) {
        System.out.println("Conexion cerrada " + conn.getMetaData().getURL());
        conn.close();            
      }      
    }
  }
  
  public void connectOrCreateDB() throws SQLException {
    mostrarErrores = false;    
    try{
      connect();
    }
    catch(ClassNotFoundException | SQLException e){
      JOptionPane.showMessageDialog(null,e.getLocalizedMessage(),this.getClass().getName(), JOptionPane.ERROR_MESSAGE);
      disconect();      
      System.exit(0);
    }    
    ResultSet rS = conn.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
    if(false == rS.next()){      
      createTables();
    }  
    disconect();
  }
  
  private void createTables() {    
    executeNonQuery("CREATE TABLE INCIDENT( " +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT INCIDENTE_PK PRIMARY KEY," +
                    "        CODE VARCHAR(8)," +
                    "        STATUS VARCHAR(100)," +
                    "        ASSIGNED VARCHAR(20)," +
                    "        DESCRIPTION VARCHAR(1000)," +
                    "        AFFECTED VARCHAR(100))");
    executeNonQuery("CREATE TABLE ENTRY (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT ENTRY_PK PRIMARY KEY ," +
                    "        INCIDENT INTEGER," +
                    "        ENTRY VARCHAR(1500)," +
                    "        ENTRYDATE DATE)");
    executeNonQuery("ALTER TABLE APP.ENTRY" +
                    "        ADD FOREIGN KEY (INCIDENT)" +
                    "        REFERENCES APP.INCIDENT(ID)");
    executeNonQuery("CREATE TABLE NOTE (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT NOTE_PK PRIMARY KEY," +
                    "        TYPENOTE  VARCHAR(200)," +
                    "        DESCRIPTION  VARCHAR(20000))");
    executeNonQuery("CREATE TABLE REINSURANCE( " +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT REINSURANCE_PK PRIMARY KEY," +
                    "        CODE VARCHAR(10)," +
                    "        CLAIM VARCHAR(20)," +
                    "        POLICY VARCHAR(20)," +
                    "        CLAIM_DATE VARCHAR(20)," +
                    "        RISK VARCHAR(20)," +
                    "        COVERAGE VARCHAR(200)," +
                    "        SUBLINE VARCHAR(10)," +
                    "        OFFERING VARCHAR(50)," +
                    "        HOMOLOGATION VARCHAR(200)," +
                    "        ERROR VARCHAR(300)," +
                    "        PAYLOAD VARCHAR(500)," +
                    "        TRANSFER VARCHAR(500))"); 
        executeNonQuery("CREATE TABLE TEMPLATE( " +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT TEMPLATE_PK PRIMARY KEY," +
                    "        AGRUPADOR    VARCHAR(200)," +
                    "        CAUSA         VARCHAR(200)," +
                    "        PROCESO       VARCHAR(200)," +
                    "        RAIZAL        VARCHAR(100)," +
                    "        ESTADO_RAIZAL VARCHAR(100)," +
                    "        RESPONSABLE   VARCHAR(200)," +
                    "        DIAGNOSTICO   VARCHAR(3000)," +
                    "        ACCION        VARCHAR(3000)," +
                    "        DESCRIPCION   VARCHAR(3000)," +
                    "        OPERATIVIDAD  VARCHAR(100)," +
                    "        CREDENCIALES  VARCHAR(100))"); 
    
    
    createTablesGW();
    createTablesEcosystem();
    createTablesClosures();
    createTableLista();
  }  
  
  public void removeTableClosure(){        
    executeNonQuery("DROP TABLE CLOSURE");    
  }
  
  
  public void removeTableLista(){        
    executeNonQuery("DROP TABLE LISTA");    
  }
  
  public void removeTablesGW(){        
    executeNonQuery("DROP TABLE gw_lob_Model");
    executeNonQuery("DROP TABLE gw_type_code");
  } 
  
  
  public void removeTablesEcosystem(){            
    executeNonQuery("DROP TABLE gw_homolog_general_to_core");    
    executeNonQuery("DROP TABLE gw_homolog_core_to_general");
    executeNonQuery("DROP TABLE gw_ecosystem_coverages");
  }
  
  public void createTablesGW(){    
    executeNonQuery("CREATE TABLE gw_lob_Model(" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT gw_lob_model_PK PRIMARY KEY," +
                    "        offering                VARCHAR(150)," +
                    "        lob_code                VARCHAR(50)," +
                    "        policy_type             VARCHAR(50)," +
                    "        loss_type               VARCHAR(50)," +
                    "        coverage_type           VARCHAR(100)," +
                    "        internal_policy_type    VARCHAR(50)," +
                    "        policy_tab              VARCHAR(100)," +
                    "        loss_party_Type         VARCHAR(50)," +
                    "        coverage_subtype        VARCHAR(100)," +
                    "        exposure_type           VARCHAR(50)," +
                    "        covTerm_pattern         VARCHAR(100)," +
                    "        coverage_subtype_class  VARCHAR(20)," +
                    "        limit_or_Deductible     VARCHAR(50)," +
                    "        loss_cause              VARCHAR(2000)," +            
                    "        cost_category           VARCHAR(1400))");    
        
    executeNonQuery("CREATE TABLE gw_type_code (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT gw_type_key_PK PRIMARY KEY," +
                    "        type_key_name    VARCHAR(100)," +                    
                    "        type_code       VARCHAR(100)," +                    
                    "        name_es         VARCHAR(300)," +
                    "        name_us         VARCHAR(300)," +
                    "        retired         BOOLEAN," +
                    "        other_category  VARCHAR(300)," +
                    "        is_new          BOOLEAN)");     
  }
  
  public void createTablesEcosystem() {    
    executeNonQuery("CREATE TABLE gw_homolog_general_to_core (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT gw_homologations_general_core_PK PRIMARY KEY," +
                    "        homologation_name    VARCHAR(100)," +                    
                    "        source       VARCHAR(100)," +                    
                    "        target         VARCHAR(300))");     
  
    executeNonQuery("CREATE TABLE gw_homolog_core_to_general (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT gw_homologations_core_general_PK PRIMARY KEY," +
                    "        homologation_name    VARCHAR(100)," +                    
                    "        source       VARCHAR(100)," +                    
                    "        target         VARCHAR(300))");     
  
    executeNonQuery("CREATE TABLE gw_ecosystem_coverages (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT gw_ecosystem_coverages_PK PRIMARY KEY," +
                    "        ramo            VARCHAR(3)," +
                    "        subramo         VARCHAR(3)," +
                    "        garantia        VARCHAR(3)," +
                    "        subgarantia     VARCHAR(3)," +
                    "        ramo_contable   VARCHAR(3)," +
                    "        product         VARCHAR(100)," +
                    "        coverage_name   VARCHAR(100))");     
    
  }
  
  public void createTablesClosures() {    
    executeNonQuery("CREATE TABLE CLOSURE (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT CLOSURE_PK PRIMARY KEY," +
                    "        ORIGEN         VARCHAR(10)," +                    
                    "        TIPO           VARCHAR(20)," +                    
                    "        CLAIMNUMBER    VARCHAR(200)," +                    
                    "        POLICYNUMBER   VARCHAR(20)," +                    
                    "        RAMO           VARCHAR(5)," +                    
                    "        REFERENCIA     VARCHAR(300)," +                    
                    "        ROW_TXT        VARCHAR(1000)," +                    
                    "        VALOR_CIEN_GW  DOUBLE," +                    
                    "        VALOR_REAS_GW  DOUBLE," +                    
                    "        VALOR_CIEN_SAP DOUBLE," +                    
                    "        VALOR_REAS_SAP DOUBLE," +                    
                    "        MONEDA         VARCHAR(10)," +                    
                    "        ESTADO         VARCHAR(30)," +
                    "        DIFER_CIEN     DOUBLE," +
                    "        DIFER_REAS     DOUBLE)" );  
    
    executeNonQuery("CREATE INDEX ClosuresIndex ON CLOSURE(referencia)");
  }
    
  public void createTableLista() {    
    executeNonQuery("CREATE TABLE LISTA (" +
                    "        ID INTEGER NOT NULL" +
                    "        GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    "        CONSTRAINT LISTA_PK PRIMARY KEY," +                    
                    "        NOMBRE           VARCHAR(20)," +                    
                    "        VALOR          VARCHAR(2000))" );  
    
   
  }

  public ResultSet executeQuery(String query) throws SQLException {          
    st = conn.createStatement();
    rs = st.executeQuery(query);
    return rs;
  }   

  public int executeNonQuery(String query) {
      msj = "";
      PreparedStatement stmt = null;
      int reg;
      reg = 0;
      try {
          if (conn != null) {
              stmt = conn.prepareStatement(query);
              reg = stmt.executeUpdate();
              stmt.close();
          }
      } catch (SQLException ex) {
          msj = "ERROR: " + ex.getMessage();
          System.out.println("Error: " + ex.toString());
          if (mostrarErrores) {
              JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
      }
      return reg;
  }

  // insertar
  public String insert(String Tabla, String elementos, String registro) {
      msj = "";
      int reg = 1;
      String exito;
      try {
          if (conn != null) {
              st = conn.createStatement();
              st.execute("INSERT INTO " + Tabla + " (" + elementos + ") VALUES (" + registro + ")");
              if (reg > 0) {
                  exito = "true";
              } else {
                  exito = "false";
              }
              st.close();
          } else {
              exito = "false";
              msj = "ERROR: There don't exist connection...";
          }
      } catch (SQLException ex) {
          if (mostrarErrores) {
              JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
          System.out.println("numero: " + ex.getErrorCode());
          exito = ex.getMessage();
          msj = "ERROR: " + ex.getMessage();
      }
      return exito;
  }

  // insertar
  public String insertValues(String Tabla, String valores) {
      msj = "";
      int reg = 1;
      String exito;
      try {
          if (conn != null) {
              st = conn.createStatement();
              st.execute("INSERT INTO " + Tabla + " VALUES (" + valores + ")");
              if (reg > 0) {
                  exito = "true";
              } else {
                  exito = "false";
              }
              st.close();
          } else {
              exito = "false";
              msj = "ERROR: There don't exist connection...";
          }
      } catch (SQLException ex) {
          if (mostrarErrores) {
              JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
          System.out.println("numero: " + ex.getErrorCode());
          exito = ex.getMessage();
          msj = "ERROR: " + ex.getMessage();
      }
      return exito;
  }

  // eliminar
  public void removeValues(String Tabla, String condicion) {
      msj = "";
      PreparedStatement stmt = null;
      int reg;
      try {
          if (conn != null) {
              stmt = conn.prepareStatement("DELETE FROM " + Tabla + " WHERE " + condicion);
              reg = stmt.executeUpdate();
              stmt.close();
          } else {
              if (mostrarErrores) {
                  JOptionPane.showMessageDialog(null, "No hay conexion", "Error", JOptionPane.ERROR_MESSAGE);
              }
              msj = "ERROR: There don't exist connection";
          }
      } catch (SQLException ex) {
          if (mostrarErrores) {
              JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
          msj = "ERROR: " + ex.getMessage();
      }
  }

  // modificar
  public void executeUpdate(String Tabla, String campos, String donde) {
      msj = "";
      PreparedStatement stmt = null;
      int reg;
      try {
          if (conn != null) {
              stmt = conn.prepareStatement("UPDATE " + Tabla + " SET " + campos + " WHERE " + donde);
              reg = stmt.executeUpdate();
              stmt.close();
          } else {
              if (mostrarErrores) {
                  JOptionPane.showMessageDialog(null, "No hay conexion", "Error", JOptionPane.ERROR_MESSAGE);
              }
              msj = "ERROR: There don't exist connection";
          }
      } catch (SQLException ex) {
          if (mostrarErrores) {
              JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
          msj = "ERROR: " + ex.getMessage();
      }
  }

  public String getMessage() {
      return msj;
  }

  public void setMessage(String mens) {
      msj = mens;
  }

  

}
