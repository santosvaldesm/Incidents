/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.otherResources;

import com.mycompany.incidents.entities.Closure;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;

/**
 *
 * @author santvamu
 */
public class IncidentsUtil {
    
  static DecimalFormat df = new DecimalFormat("#.#");
  
  static {
    df.setMaximumFractionDigits(3);
  }  
  
  public static void validateFile(String rutaCarp, String fileName, String header) throws Exception {
    File fileEco = new File(rutaCarp+"\\"+fileName);                            
    if (!fileEco.exists()) {// Si el archivo no existe es creado
      throw new Exception("\nEn la carpeta seleccionada deben existir archivo: " + fileName );
    }    
    FileReader fr = new FileReader (fileEco);
    BufferedReader br = new BufferedReader(fr);          
    String lineaStr=br.readLine();//la primer linea es cabecera     
    fr.close();
    br.close();
    if(lineaStr.compareTo(header)!=0){
      throw new Exception("\nLa cabecera del archivo " + fileName + " no coresponde, revisar la ayuda" );
    }      
  }  
  
  public static String determineUrl(String rutaInicial,String name){     
    int version = 1;
    boolean continuar = true;
    String rutaFinal = "";
    while(continuar) {
      rutaFinal = rutaInicial + "\\" + name + "_v" + version+".csv";
      File file = new File(rutaFinal);                
      if (!file.exists()) {// Si el archivo no existe es creado
        continuar=false;                
      }
      version++;
    }
    return rutaFinal;
  }
  
  public static String determineStringValue(String aValue){
    return aValue == null ? "#N/A" : aValue ;
  }
  
  public static String determineStringValue(Double aValue){
    return aValue == null ? "#N/A" : df.format(aValue) ;
  }
  
  public static String determineStringValue(Object columnIdentifier,String[] rowInfoSplit) {
    return columnIdentifier == null? null : rowInfoSplit[((Number)columnIdentifier).intValue()].replaceAll("\"", "");
  }  
  
  public static Double determineDoubleValue(Object columnIdentifier,String[] rowInfoSplit){
    return columnIdentifier == null? null: Double.parseDouble(rowInfoSplit[((Number)columnIdentifier).intValue()].replaceAll("\"", ""));
  }
  
  public static Double determineDoubleValue(Double aValue){
    return aValue == null ? 0 : aValue;
  } 
  
  public static String determineRowInfo(Closure aResult) {
    return  "ID("         + aResult.getId()     + 
            ") ORIGEN("   + aResult.getOrigen() + 
            ") TIPO("     + aResult.getTipo() + 
            ") NUM_RECL(" + aResult.getClaimnumber() +
            ") POLIZA("   + aResult.getPolicynumber() + 
            ") RAMO("     + aResult.getRamo() + 
            ") REFERENC(" + aResult.getReferencia() + 
            ") ROW_INFO(" + aResult.getRowTxt() + 
            ") CIEN_GW("  + aResult.getValorCienGw() + 
            ") REAS_GW("  + aResult.getValorReasGw() + 
            ") CIEN_SAP(" + aResult.getValorCienSap() + 
            ") REAS_SAP(" + aResult.getValorReasSap() + 
            ") MONEDA("   + aResult.getMoneda() + 
            ") ESTADO("   + aResult.getEstado() + 
            ") DIF_CIEN(" + aResult.getDiferCien() +
            ") DIF REAS(" + aResult.getDiferReas() + ")";
  }
  
}
