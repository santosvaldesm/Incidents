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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author santvamu
 */
public class IncidentsUtil {

	static DecimalFormat df = new DecimalFormat("#.#");
	static HashMap<String,String> textsHasMap = new HashMap<>();
	
	private static void createTexts(){
		readText("textoCierreCoaseguroAyuda.txt");
		readText("textoCierreFinancieroAyuda.txt");
		readText("textoCierreFinancieroGastos.txt");
		readText("textoCierreFinancieroPagos.txt");
		readText("textoCierreFinancieroReservas.sql");
		readText("textoCierreFinancieroSalvamentos.txt");
		readText("textoCierreReaseguroAyuda.txt");
		readText("textoCierreReaseguroCuentas.txt");
		readText("textoCierreReaseguroGW.txt");
		readText("textoCierreReaseguroValidaciones.txt");
		readText("textoConsultaPolizaBeneficiarios.sql");
		readText("textoConsultaPolizaCoaseguradores.sql");
		readText("textoConsultaPolizaCobertura.sql");
		readText("textoConsultaPolizaConsultasComunes.sql");
		readText("textoConsultaPolizaDirecciones.sql");
		readText("textoConsultaPolizaTerminos.sql");
		readText("textoCruceCierreVsMensajes.txt");
		readText("textoExcelExportHelp.txt");
		readText("textoHomologationsHelp.sql");
		readText("textoProductModelHelp.gs");		
	}
	
	private static void readText(String txtFileName){
		try {
			ClassLoader classLoader = ClassLoader.getSystemClassLoader();			
			String keyName = txtFileName.substring(5,txtFileName.indexOf("."));
      InputStream in = classLoader.getResourceAsStream(txtFileName);			
      BufferedReader bf = new BufferedReader(new InputStreamReader(in));                  			
			String contentText = "";
      String lineaStr = "";
			while((lineaStr = bf.readLine())!=null){				
				contentText = contentText + "\n" + lineaStr;				
			}
			textsHasMap.put(keyName, contentText);
		}
    catch (IOException ex) {      
    }
	}	
		
	static {
		df.setMaximumFractionDigits(3);		
		createTexts();
	}
	
	public static String getText(String nameText){
		String strReturn = textsHasMap.get(nameText);
		return strReturn == null ? "Texto no encontrado" : strReturn;
	}
	
	public static void setBorder(XSSFCellStyle aStyle) {
		aStyle.setBorderBottom(BorderStyle.THIN);
		aStyle.setBorderTop(BorderStyle.THIN);
		aStyle.setBorderRight(BorderStyle.THIN);
		aStyle.setBorderLeft(BorderStyle.THIN);
	}
	
	public static void setColorFill(XSSFCellStyle aStyle,byte red,byte blue,byte green) {
		byte[] rgb = new byte[]{red, blue, green};
		aStyle.setFillForegroundColor(new XSSFColor(rgb, null));
		aStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}
	
	public static XSSFCellStyle createHeaderStyle(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = (XSSFCellStyle)anExcelWorbook.createCellStyle();
		Font font = anExcelWorbook.createFont();
		font.setBold(true);
		aStyle.setFont(font);		
		setBorder(aStyle);
		setColorFill(aStyle, (byte)221, (byte)235, (byte)247);		
		return aStyle;
	}
	
	public static XSSFCellStyle createCellStyle(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = anExcelWorbook.createCellStyle();		
		setBorder(aStyle);
		return aStyle;
	}
	
	public static XSSFCellStyle createCellStyleYellow(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = anExcelWorbook.createCellStyle();		
		setBorder(aStyle);
		setColorFill(aStyle,(byte)255, (byte)242, (byte)204);		
		return aStyle;
	}
	
	public static XSSFCellStyle createCellStyleGreen(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = anExcelWorbook.createCellStyle();		
		setBorder(aStyle);
		setColorFill(aStyle,(byte)226, (byte)239, (byte)218);		
		return aStyle;
	}
	
	public static XSSFCellStyle createCellStyleGray(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = anExcelWorbook.createCellStyle();		
		setBorder(aStyle);
		setColorFill(aStyle,(byte)214, (byte)220, (byte)228);		
		return aStyle;
	}
	
	public static void createCellInRow(XSSFRow aRow,int cellNum,
					                     Object obj,XSSFCellStyle aStyle){
    XSSFCell aCell = aRow.createCell(cellNum);
		aCell.setCellStyle(aStyle);
    if (obj instanceof String) { 
      aCell.setCellValue((String) obj); 
    } else if (obj instanceof Boolean) { 
      aCell.setCellValue((Boolean) obj); 
    } else if (obj instanceof Date) { 
      aCell.setCellValue((Date) obj); 
    } else if (obj instanceof Double) { 
      aCell.setCellValue((Double) obj); 
    }      
  }
	
	public static void insertHeader(XSSFSheet aSheet, String[] headers,
					                  int rowPosition,int sizeCell, XSSFCellStyle aStyle){
		XSSFRow headerRow = aSheet.createRow(rowPosition++);
		if(sizeCell>1){			
			aSheet.addMergedRegion(new CellRangeAddress(rowPosition-1,rowPosition-1,0,sizeCell-1)); 
		}
		for (int i = 0; i < headers.length; ++i) {			
			IncidentsUtil.createCellInRow(headerRow,i,headers[i],aStyle);			
		}		
	}
	
	public static void printStackTrace(Exception e3,JTextArea outputTxt) {
		String textToAdd = "\n" + e3.toString();
		StackTraceElement[] elements = e3.getStackTrace();
		for (StackTraceElement element : elements) {
			textToAdd = textToAdd + "\n" + element.toString();
		}
		outputTxt.setText(outputTxt.getText() + textToAdd);
		outputTxt.setCaretPosition(outputTxt.getDocument().getLength());
	}

	public static void validateFile(String rutaCarp, String fileName, String header) throws Exception {
		File fileEco = new File(rutaCarp + "\\" + fileName);
		if (!fileEco.exists()) {// Si el archivo no existe es creado
			throw new Exception("\nEn la carpeta seleccionada deben existir archivo: " + fileName);
		}
		FileReader fr = new FileReader(fileEco);
		BufferedReader br = new BufferedReader(fr);
		String lineaStr = br.readLine();//la primer linea es cabecera     
		fr.close();
		br.close();
		if (lineaStr.compareTo(header) != 0) {
			throw new Exception("\nLa cabecera del archivo " + fileName + " no coresponde, revisar la ayuda");
		}
	}
	
	public static void validateFile(String rutaCarp, String fileName, String[][] expectedHeader) throws Exception {
		File fileEco = new File(rutaCarp + "\\" + fileName);
		if (!fileEco.exists()) {// Si el archivo no existe es creado
			throw new Exception("\nEn la carpeta seleccionada deben existir archivo: " + fileName);
		}
		FileReader fr = new FileReader(fileEco);
		BufferedReader br = new BufferedReader(fr);		
		String foundHeader = br.readLine();//la primer linea es cabecera     
		fr.close();
		br.close();
		
		foundHeader = foundHeader.replaceAll("\"", "");
		foundHeader = foundHeader.toUpperCase();
		String[] foundHeaderSplit = foundHeader.split(";");
		if(expectedHeader.length != foundHeaderSplit.length){
			throw new Exception("\nSe esperaban " + expectedHeader.length + 
							            " columnas, pero el archivo " + fileName + " tiene " + 
							            foundHeaderSplit.length +" columnas, revisar la ayuda");
		}
		
		for(int i=0; i < expectedHeader.length;i++){
			String[] expectedValues = expectedHeader[i];
			for(int j=0; j < expectedValues.length;j++){
				if(!foundHeaderSplit[i].contains(expectedValues[j].toUpperCase())){
				  throw new Exception("\nEn el archivo " + fileName + " columna  " + (i+1)
									  + " se esperaba que contenga" + Arrays.toString(expectedValues) + 
										" pero se encontro [" + foundHeaderSplit[i] + "]");	
				}
		  }	
		}
	}

	public static String determineUrl(String rutaInicial, String name,String ext) {
		int version = 1;
		boolean continuar = true;
		String rutaFinal = "";
		while (continuar) {
			rutaFinal = rutaInicial + "\\" + name + "_v" + version + ext;
			File file = new File(rutaFinal);
			if (!file.exists()) {// Si el archivo no existe es creado
				continuar = false;
			}
			version++;
		}
		return rutaFinal;
	}

	public static String determineStringValue(String aValue) {
		return aValue == null ? "#N/A" : aValue;
	}

	public static String determineStringValue(Double aValue) {
		return aValue == null ? "#N/A" : df.format(aValue);
	}

	public static String determineStringValue(Object columnIdentifier, String[] rowInfoSplit) {
		return columnIdentifier == null ? null : rowInfoSplit[((Number) columnIdentifier).intValue()].replaceAll("\"", "");
	}
	
	public static  Double determineDoubleValue(String aValue) {
    return aValue.length() == 0 ? null: Double.parseDouble(aValue.replaceAll(",", "."));
  }

	public static Double determineDoubleValue(Object columnIdentifier, String[] rowInfoSplit) {
		return columnIdentifier == null ? null : Double.parseDouble(rowInfoSplit[((Number) columnIdentifier).intValue()].replaceAll("\"", "").replaceAll(",", "."));
	}

	public static Double determineDoubleValue(Double aValue) {
		return aValue == null ? 0 : aValue;
	}

	public static Double restarDoubles(Double first, Double second) {
		if (first == null && second == null) {
			return null;
		}
		if (first == null) {
			return second * -1;
		}
		if (second == null) {
			return first;
		}
		return first - second;
	}

	public static Double sumarDoubles(Double first, Double second) {
		if (first == null && second == null) {
			return null;
		}
		if (first == null) {
			return second;
		}
		if (second == null) {
			return first;
		}
		return first + second;
	}

	public static String determineRowInfo(Closure aResult) {
		return "ID(" + aResult.getId()
						+ ") ORIGEN(" + aResult.getOrigen()
						+ ") TIPO(" + aResult.getTipo()
						+ ") NUM_RECL(" + aResult.getClaimnumber()
						+ ") POLIZA(" + aResult.getPolicynumber()
						+ ") RAMO(" + aResult.getRamo()
						+ ") REFERENC(" + aResult.getReferencia()
						+ ") ROW_INFO(" + aResult.getRowTxt()
						+ ") CIEN_GW(" + aResult.getValorCienGw()
						+ ") REAS_GW(" + aResult.getValorReasGw()
						+ ") CIEN_SAP(" + aResult.getValorCienSap()
						+ ") REAS_SAP(" + aResult.getValorReasSap()
						+ ") MONEDA(" + aResult.getMoneda()
						+ ") ESTADO(" + aResult.getEstado()
						+ ") DIF_CIEN(" + aResult.getDiferCien()
						+ ") DIF REAS(" + aResult.getDiferReas() + ")";
	}
}
