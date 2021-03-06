package com.mycompany.incidents.panels;

import com.mycompany.incidents.entities.Closure;
import com.mycompany.incidents.jpaControllers.ClosureJpaController;
import com.mycompany.incidents.otherResources.ClosureTypeEnum;
import com.mycompany.incidents.otherResources.DataBaseController;
import com.mycompany.incidents.otherResources.FinalReportDTO;
import com.mycompany.incidents.otherResources.IncidentsUtil;
import java.io.BufferedReader;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DialogFinancialClosure extends javax.swing.JDialog implements Runnable {

	EntityManagerFactory factory = Persistence.createEntityManagerFactory("Incidents_PU");
	ClosureJpaController closureController = new ClosureJpaController(factory);
	Closure currentClosure = null;

	boolean runProcess = false;
	private Thread h1;

	String nomArchivoSapReserva = "SAP_RESERVAS.csv";
	String nomArchivoGwReserva = "GW_RESERVAS.csv";
	String nomArchivoSapGastos = "SAP_GASTOS.csv";
	String nomArchivoGwGastos = "GW_GASTOS.csv";
	String nomArchivoSapPagos = "SAP_PAGOS.csv";
	String nomArchivoGwPagos = "GW_PAGOS.csv";
	String nomArchivoSapSalvamentos = "SAP_SALVAMENTOS.csv";
	String nomArchivoGwSalvamentos = "GW_SALVAMENTOS.csv";
	String rutaCarpeta = "";

	String[][] headerSapReserva    ={{"MONEDA"},{"CLASE"},{"RAMO"},{"LIZA"},{"REFERENCIA"},{"RES","MD"},{"RES","ML"},{"MD"},{"ML"},{"REA","MD"},{"REA","ML"}};
	String[][] headerSapPago       ={{"MONEDA"},{"CLASE"},{"RAMO"},{"LIZA"},{"REFERENCIA"},{"TEXTO"},{"PAGO","MD"},{"PAGO","ML"},{"REA","MD"},{"REA","ML"}};	
	String[][] headerSapSalvamento ={{"MONEDA"},{"CLASE"},{"RAMO"},{"LIZA"},{"REFERENCIA"},{"TEXTO"},{"SAL","MD"},{"SAL","ML"},{"REA","MD"},{"REA","ML"}};		
	String[][] headerGwReserva     ={{"ID"},{"CLAIMNUMBER"},{"ESMIGRADO"},{"POLICYNUMBER"},{"ID","TRA"},{"TIPO","TRANSACCION"},{"COST","CATEGORY"},{"RAMO","CONTABLE"},{"ESTADO"},{"CREATETIME"},{"PUBLICID","TRA"},{"TRANSACCION","ORIGEN"},{"CLAIMAMOUNT"},{"CEDIDO"},{"RETENIDO"},{"SURA","RETENIDO"},{"VALOR","BRUTO"},{"MONEDA"},{"MOVIMIENTO"},{"ESTADO","CHEQUE"},{"RECALCULADO"},{"DIFERENCIA"},{"REFLECTION"}};				
	String[][] headerGwPago        ={{"ID"},{"TIPO","COASEGURO"},{"CLAIMNUMBER"},{"POLICYNUMBER"},{"RAMO","CONTABLE"},{"COST","CATEGORY"},{"COINSURANCE","EXT"},{"FECHA","SINIESTRO"},{"RECALCULADO"},{"TIPO"},{"NUMERO","TRANSACCION"},{"TRANSACCION","ORIGEN"},{"PAGO","SOLO","SURA"},{"MASIVO"},{"CEDIDO"},{"RETENIDO"},{"SURA","RETENIDO"},{"ESTADO"},{"FECHA","TRANSACCION"},{"VALOR","NETO"},{"MONEDA"},{"VALOR","BRUTO"},{"VALOR","CON","ICM"},{"VALOR","ICM"},{"VALOR","SIN","COASEG"},{"VALOR","BRUTO","SIN","COA"},{"DIFERENCIA"},{"REFLECTION"}};
	String[][] headerGwGasto       ={{"ID"},{"CLAIMNUMBER"},{"POLICYNUMBER"},{"FECHA","SINIESTRO"},{"FECHA","AVISO"},{"TIPO","TRANSACCION"},{"COST","CATEGORY"},{"RAMO","CONTABLE"},{"SUBTYPE"},{"ESTADO"},{"CREATETIME"},{"PUBLICID","TRA"},{"CLAIMAMOUNT"},{"FECHA","CONTABILIZACION"},{"MONEDA"},{"RESERVETYPE"},{"REFERENCEID"},{"PERCENTAJE"},{"SAP","AMOUNT"},{"LIQUIDATIONEXPENSESRESERVE"}};
	String[][] headerGwSalvamento  ={{"ID"},{"CLAIMNUMBER"},{"POLICYNUMBER"},{"RAMO","CONTABLE"},{"RECALCULADO"},{"COINSURANCE","EXT"},{"COST","CATEGORY"},{"FECHA","SINIESTRO"},{"TIPO"},{"NUMERO","TRANSACCION"},{"TRANSACCION","ORIGEN"},{"ESTADO"},{"FECHA","TRANSACCION"},{"CEDIDO"},{"RETENIDO"},{"RETENCION","PURA"},{"VALOR","NETO"},{"VALOR","BRUTO"},{"MONEDA"},{"VALOR","SIN","COASEG"},{"DIFERENCIA"},{"REFLECTION"}};	
	
	String[] fileNamesGw = {nomArchivoGwReserva, nomArchivoGwGastos, nomArchivoGwPagos, nomArchivoGwSalvamentos};
	String[] fileNamesSap = {nomArchivoSapReserva, nomArchivoSapGastos, nomArchivoSapPagos, nomArchivoSapSalvamentos};
	HashMap<ClosureTypeEnum, String> searchCriteria = null;
	HashMap<String, FinalReportDTO> aFinalReportList = new HashMap<>();
	HashMap<String, Boolean> ommitedStatus = new HashMap<>();
	String monetaryLimit = "-";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
	DataBaseController conection = new DataBaseController();
	String lastRowInfo = "";
	int currentBarProgress = 0;
	int currentItemNumber = 0;//cuantos items ha finalizado en el proceso actual 
	int totalItemNumber = 0;//total de items en el proceso actual
	ArrayList<String[]> inconsistencesList=new ArrayList<>();
	int inconsistencesLimit = 1000;
	
	XSSFCellStyle headerStyle;
	XSSFCellStyle cellStyle;
	XSSFCellStyle cellStyleGray;
	
	boolean crueSinRamo = true;

	public DialogFinancialClosure(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		btnStart.setEnabled(false);
		spinnerMaxErrors.setValue(1000);
		spinnerLimit.setValue(1);
		loadOmmitedStatus();
	}
	
	/*
		De no encontrar un estado en esta lista se generara 
		una excepcion para adicionarlo evaluarlo y adicionarlo al HashMap
	*/
	private void loadOmmitedStatus(){
		//Se contabilizan:
		ommitedStatus.put("Anulado",false);
		ommitedStatus.put("Solicitado",false);
		ommitedStatus.put("Legalizado",false);
		ommitedStatus.put("RECIBIDO SAP",false);
		//No se contabilizan
		ommitedStatus.put("Aprobaci??n pendiente",true);
		ommitedStatus.put("En espera de env??o",true);
		ommitedStatus.put("Solicitando",true);
		ommitedStatus.put("Rechazado",true);
		ommitedStatus.put("Denegada",true);//Estado que aparece al anular pago en espera de procesamiento		
	}

	@Override
	public void run() {

		try {
			if (runProcess) {
				progressTotal(100, 0);
				crueSinRamo = !checkRamo.isSelected();
				btnStart.setEnabled(false);
				btnSelectFile.setEnabled(false);
				spinnerLimit.setEnabled(false);
				spinnerMaxErrors.setEnabled(false);
				monetaryLimit = spinnerLimit.getValue().toString();
				inconsistencesLimit = (int)spinnerMaxErrors.getValue();
				printInOutputText("\nINICIO: " + sdf.format(new Date()));
				printInOutputText("\nValidando archivos...");

				IncidentsUtil.validateFile(rutaCarpeta, nomArchivoSapReserva, headerSapReserva);
				IncidentsUtil.validateFile(rutaCarpeta, nomArchivoGwReserva, headerGwReserva);
				IncidentsUtil.validateFile(rutaCarpeta, nomArchivoGwGastos, headerGwGasto);
				IncidentsUtil.validateFile(rutaCarpeta, nomArchivoSapPagos, headerSapPago);
				IncidentsUtil.validateFile(rutaCarpeta, nomArchivoGwPagos, headerGwPago);
				IncidentsUtil.validateFile(rutaCarpeta, nomArchivoSapSalvamentos, headerSapSalvamento);
				IncidentsUtil.validateFile(rutaCarpeta, nomArchivoGwSalvamentos, headerGwSalvamento);
				progressTotal(100, 5);
				printInOutputText("\nLimpiando tablas...");
				regenerateTableClosure();
				progressTotal(100, 10);
				readAndInsertRowsGw();
				readAndInsertRowsSap();
				searchInconsistencies();
				createFinalReport();
				printInOutputText("\nEliminando temporales...");
				regenerateTableClosure();
				printInOutputText("\nFIN: " + sdf.format(new Date()));
				printInOutputText("\nFINALIZO CORRECTAMENTE");
			}
		} catch (FileNotFoundException e1) {
			printInOutputText("\nERROR: FileNotFoundException: Ultimo registro analizado: " + lastRowInfo);
			IncidentsUtil.printStackTrace(e1,outputTxt);
		} catch (IOException e2) {
			IncidentsUtil.printStackTrace(e2,outputTxt);
			printInOutputText("\nERROR: IOException: Ultimo registro analizado: " + lastRowInfo);
		} catch (SQLException ex) {
			printInOutputText("\nERROR: SQLException: Ultimo registro analizado: " + lastRowInfo);
			IncidentsUtil.printStackTrace(ex,outputTxt);
		} catch (Exception e3) {
			printInOutputText("\nERROR: Exception: Ultimo registro analizado: " + lastRowInfo);
			IncidentsUtil.printStackTrace(e3,outputTxt);
		}
		progressTotal(100, 100);
		progressProcess(100, 100);
		runProcess = false;
	}

	
	private void insertInReportDTOList(FinalReportDTO sourceDTO) throws Exception {			
		if(sourceDTO.getOrigen().equals("GW")){//Solo se omiten registros de GW
			Boolean isOmmited = ommitedStatus.get(sourceDTO.getStatus());
			if(isOmmited==null){
				throw new Exception("El estado '"+sourceDTO.getStatus()+"' no ha sido contemplado");
			}
			if(isOmmited){
				return; //no se continua, estado del registro debe ser omitido
			}
		}
		String clave = sourceDTO.getKey();
		FinalReportDTO targetDTO = aFinalReportList.get(clave);
		if (targetDTO == null) {//no se encuentra
			aFinalReportList.put(clave, sourceDTO);
		} else {//si se encuentra
			targetDTO.setValorCien(IncidentsUtil.sumarDoubles(targetDTO.getValorCien(), sourceDTO.getValorCien()));
			targetDTO.setValorReas(IncidentsUtil.sumarDoubles(targetDTO.getValorReas(), sourceDTO.getValorReas()));
			aFinalReportList.put(clave, targetDTO);
		}
	}

	private void createFinalReport() throws IOException {		
		copyTempValuesToFinalValues(); //printConsolidated();		
		joinByOrigen();		             //printConsolidated();
		joinByTipoGasto();		         //printConsolidated();		
		calculateDiferences();				//printConsolidated();		
		createExcelReport();
	}
	
	private void createStyles(XSSFWorkbook anExcelWorbook){
		headerStyle = IncidentsUtil.createHeaderStyle(anExcelWorbook);
	  cellStyle = IncidentsUtil.createCellStyle(anExcelWorbook);		
	  cellStyleGray = IncidentsUtil.createCellStyleGray(anExcelWorbook);		
	}
	

	/**
	 * hay que calcular los campos "diferencias" por cada registro
	 */
	private void calculateDiferences() {
		for (Map.Entry<String, FinalReportDTO> entry : aFinalReportList.entrySet()) {
			FinalReportDTO aDTO = entry.getValue();

			//Encontrar diferencia entre GW y SAP
			switch(aDTO.getTipo()){
				case "PAGO":
					aDTO.setDiferenciaValor(IncidentsUtil.restarDoubles(aDTO.getValorGW(), aDTO.getValorSAP()));
					aDTO.setDiferenciaReaseguro(IncidentsUtil.sumarDoubles(aDTO.getReaseguroGW(), aDTO.getReaseguroSAP()));
					break;
				case "RESERVA":
					aDTO.setDiferenciaValor(IncidentsUtil.sumarDoubles(aDTO.getValorGW(), aDTO.getValorSAP()));
					aDTO.setDiferenciaGastos(IncidentsUtil.sumarDoubles(aDTO.getGastosGW(), aDTO.getGastosSAP()));
					aDTO.setDiferenciaReaseguro(IncidentsUtil.restarDoubles(aDTO.getReaseguroGW(), aDTO.getReaseguroSAP()));
					break;
				case "SALVAMENTO":
					aDTO.setDiferenciaValor(IncidentsUtil.sumarDoubles(aDTO.getValorGW(), aDTO.getValorSAP()));
					aDTO.setDiferenciaReaseguro(IncidentsUtil.restarDoubles(aDTO.getReaseguroGW(), aDTO.getReaseguroSAP()));
					break;
			}		
			
			//Cambiar resultados en null por 0
			aDTO.setDiferenciaGastos(aDTO.getDiferenciaGastos() == null ? 0 : aDTO.getDiferenciaGastos());
			aDTO.setValorGW(aDTO.getValorGW() == null ? 0 : aDTO.getValorGW());
			aDTO.setGastosGW(aDTO.getGastosGW() == null ? 0 : aDTO.getGastosGW());
			aDTO.setReaseguroGW(aDTO.getReaseguroGW() == null ? 0 : aDTO.getReaseguroGW());
			aDTO.setValorSAP(aDTO.getValorSAP() == null ? 0 : aDTO.getValorSAP());
			aDTO.setGastosSAP(aDTO.getGastosSAP() == null ? 0 : aDTO.getGastosSAP());
			aDTO.setReaseguroSAP(aDTO.getReaseguroSAP() == null ? 0 : aDTO.getReaseguroSAP());
			aDTO.setDiferenciaValor(aDTO.getDiferenciaValor() == null ? 0 : aDTO.getDiferenciaValor());
			aDTO.setDiferenciaGastos(aDTO.getDiferenciaGastos() == null ? 0 : aDTO.getDiferenciaGastos());
			aDTO.setDiferenciaReaseguro(aDTO.getDiferenciaReaseguro() == null ? 0 : aDTO.getDiferenciaReaseguro());
			entry.setValue(aDTO);
		}
	}

	/*
		A partir de una Key busca el registro con el cual deber??a unirse	  
	 */
	private FinalReportDTO searchTargetDTO(String sourceKey) {
		String targetKey = "";
		if (sourceKey.contains("SAP")) {
			targetKey = sourceKey.replace("SAP", "GW");
		} else {
			targetKey = sourceKey.replace("GW", "SAP");
		}
		return aFinalReportList.get(targetKey);
	}

	/*
		A partir de una Key de un gasto buscar el DTO tipo reserva para unirse
	 */
	private FinalReportDTO searchReserveDTO(String gastoKey) {
		String reserveKey = gastoKey.replace("GASTO", "RESERVA");
		FinalReportDTO result = aFinalReportList.get(reserveKey);
		if (result != null) {
			return result;
		} else {
			if (reserveKey.contains("SAP")) {
				reserveKey = reserveKey.replace("SAP", "GW");
			} else {
				reserveKey = reserveKey.replace("GW", "SAP");
			}
			return aFinalReportList.get(reserveKey);
		}
	}

	private FinalReportDTO joinElements(FinalReportDTO sourceDTO, FinalReportDTO targetDTO) {
		if (sourceDTO.getOrigen().equals("SAP")) {
			sourceDTO.setValorGW(targetDTO.getValorGW());
			sourceDTO.setReaseguroGW(targetDTO.getReaseguroGW());
		} else {
			sourceDTO.setValorSAP(targetDTO.getValorSAP());
			sourceDTO.setReaseguroSAP(targetDTO.getReaseguroSAP());
		}
		return sourceDTO;
	}

	private FinalReportDTO joinReserveElements(FinalReportDTO reserveDTO, FinalReportDTO gastoDTO) {
		if (gastoDTO.getOrigen().equals("SAP")) {
			reserveDTO.setGastosSAP(gastoDTO.getGastosSAP());
		} else {//es de GW
			reserveDTO.setGastosGW(gastoDTO.getGastosGW());
		}
		return reserveDTO;
	}

	/*
	  Esta en diferente registro parte de SAP y GW hay que unirla	
	 */
	private void joinByOrigen() {
		boolean continueProcces = true;
		while (continueProcces) {
			continueProcces = false;
			for (Map.Entry<String, FinalReportDTO> entry : aFinalReportList.entrySet()) {
				if (!entry.getValue().getTipo().equals("GASTO")) {
					FinalReportDTO sourceDTO = entry.getValue();
					FinalReportDTO targetDTO = searchTargetDTO(sourceDTO.getKey());
					if (targetDTO != null) {
						aFinalReportList.put(sourceDTO.getKey(), joinElements(sourceDTO, targetDTO));
						aFinalReportList.remove(targetDTO.getKey());
						continueProcces = true;
						break;
					}
				}
			}
		}
	}

	/**
	 * Esta en diferente registro parte parte de gastos y reservas hay que unirlo
	 */
	private void joinByTipoGasto() {
		boolean continueProcces = true;
		while (continueProcces) {
			continueProcces = false;
			for (Map.Entry<String, FinalReportDTO> entry : aFinalReportList.entrySet()) {
				if (entry.getValue().getTipo().equals("GASTO")) {
					FinalReportDTO gastoDTO = entry.getValue();
					FinalReportDTO reserveDTO = searchReserveDTO(gastoDTO.getKey());
					if (reserveDTO != null) {
						aFinalReportList.put(reserveDTO.getKey(), joinReserveElements(reserveDTO, gastoDTO));
						aFinalReportList.remove(gastoDTO.getKey());
						continueProcces = true;
						break;
					}
				}
			}
		}
	}

	private void printConsolidated() {
		boolean first = true;
		for (Map.Entry<String, FinalReportDTO> entry : aFinalReportList.entrySet()) {
			if (first) {
				System.out.println("--------------------------------------------------");
				System.out.println(entry.getValue().getHeaders());
				first = false;
			}
			System.out.println(entry.getValue().getSummary());
		}
	}

	/*
	 todos los valores se almacenan en los campos de ValorCien y ValorReas 
	 hay que pasarlos a los campos de GW y de SAP
	 */
	private void copyTempValuesToFinalValues() {
		for (Map.Entry<String, FinalReportDTO> entry : aFinalReportList.entrySet()) {
			FinalReportDTO sourceDTO = entry.getValue();
			if (sourceDTO.getOrigen().equals("SAP")) {
				if (sourceDTO.getTipo().equals("GASTO")) {//se pasa valor100 a gastosSAP
					sourceDTO.setGastosSAP(sourceDTO.getValorCien());
				} else {//se lo almacena en ValorSAP y ReaseguroSAP
					sourceDTO.setValorSAP(sourceDTO.getValorCien());
					sourceDTO.setReaseguroSAP(sourceDTO.getValorReas());
				}
			} else { //Es de GW
				if (sourceDTO.getTipo().equals("GASTO")) {//se pasa valor100 a gastosGW
					sourceDTO.setGastosGW(sourceDTO.getValorCien());
				} else {//se lo almacena en ValorGW y ReaseguroGW
					sourceDTO.setValorGW(sourceDTO.getValorCien());
					sourceDTO.setReaseguroGW(sourceDTO.getValorReas());
				}
			}
		}
	}

	/*
	 Cuando se genere el excel insertar un ultimo registro con los totales en pesos y USD
	*/
	private void createExcelReport() throws FileNotFoundException, IOException {
		String rutaResult = IncidentsUtil.determineUrl(rutaCarpeta, "Consolidado", ".xlsx");		
		XSSFWorkbook anExcelWorbook = new XSSFWorkbook();		
		createStyles(anExcelWorbook);
		XSSFSheet sheetPago = anExcelWorbook.createSheet("PAGOS");
		XSSFSheet sheetReserva = anExcelWorbook.createSheet("RESERVA");
		XSSFSheet sheetSalvamento = anExcelWorbook.createSheet("SALVAMENTO");		
		XSSFSheet sheetInconsistences = anExcelWorbook.createSheet("DIFERENCIAS");		
		XSSFSheet sheetScript = anExcelWorbook.createSheet("SCRIPT");		
		String[] headerPagSalv       = new String[]{"Ramo","Moneda","Valor GW","Reaseguro GW","Valor SAP","Reaseguro SAP","Dif. Valor GW-SAP","Dif. Reaseguro GW-SAP"};
		String[] headerReserva    = new String[]{"Ramo","Moneda","Valor GW","Gastos GW","Reaseguro GW","Valor SAP","Gastos SAP","Reaseguro SAP","Dif. Valor GW-SAP","Dif. Gastos GW-SAP","Dif. Reaseguro GW-SAP"};		
		String[] headerInconsistences   = new String[]{"ID","TIPO ERROR","REFERENCIA","RAMO","CLAIM NUMBER","POLCY NUMBER","MONEDA","ESTADO","FECHA","VALOR 100 GW","REAS GW","VALOR 100 SAP","REAS SAP","DIF 100","DIF REAS","DETALLE"};		
		
		insertDataInExcel(sheetPago,headerPagSalv,"PAGO");
		insertDataInExcel(sheetReserva,headerReserva,"RESERVA");
		insertDataInExcel(sheetSalvamento,headerPagSalv,"SALVAMENTO");
		insertInconsistencesInExcel(sheetInconsistences,headerInconsistences);
		insertScriptInExcel(sheetScript);
		FileOutputStream file = new FileOutputStream(rutaResult);
		anExcelWorbook.write(file);
		file.close();
	}
	
	private void insertDataInExcel(XSSFSheet aSheet, String[] headers, String tipo) {		
		int rowPosition = 0;				
		
		String[] headerTmp = new String[]{tipo+" COP"};
		IncidentsUtil.insertHeader(aSheet,headerTmp,rowPosition++,headers.length,headerStyle);
		IncidentsUtil.insertHeader(aSheet,headers,rowPosition++,1,headerStyle);
		rowPosition = insertDataByCurrency(aSheet,rowPosition,tipo,"COP");				
		rowPosition = rowPosition + 3;
		
		headerTmp = new String[]{tipo+" USD"};
		IncidentsUtil.insertHeader(aSheet,headerTmp,rowPosition++,headers.length,headerStyle);
		IncidentsUtil.insertHeader(aSheet,headers,rowPosition++,1,headerStyle);
		rowPosition = insertDataByCurrency(aSheet,rowPosition,tipo,"USD");		
	}
	
	private void insertScriptInExcel(XSSFSheet aSheet) {		
		String aScript=IncidentsUtil.getText("CruceCierreVsMensajes");
		String references = "";
		for(String[] anInconsistence : inconsistencesList){		  
			String aReference = "";
			if(anInconsistence[1]!=null &&
				 anInconsistence[1].length()!=0 &&
				 !anInconsistence[1].equals("null")){						
				if(crueSinRamo){
					aReference = anInconsistence[1];
				}
				else{
					aReference = anInconsistence[1].substring(0,anInconsistence[1].indexOf("-"));
				}
			}
			String aType = anInconsistence[0].substring(0,anInconsistence[0].indexOf(" "));
			references = references + "  arrayResult.add({"
							+ "\"" + aType + "\","
							+ "\"" + aReference + "\","
							+ "\"" + anInconsistence[2] + "\","
							+ "\"" + anInconsistence[3] + "\","
							+ "\"" + anInconsistence[4] + "\","
							+ "\"" + anInconsistence[5] + "\","
							+ "\"" + anInconsistence[6] + "\","
							+ "\"" + anInconsistence[7] + "\","
							+ "\"" + anInconsistence[8] + "\","
							+ "\"" + anInconsistence[9] + "\","
							+ "\"" + anInconsistence[10] + "\","
							+ "\"" + anInconsistence[11] + "\","
							+ "\"" + anInconsistence[12] + "\","
							+ "\"" + anInconsistence[13] + "\""
							+ "})\n";
		}
		aScript = aScript.replace("  arrayResult.add({\"X\",\"Y\",\"Z\"})", references);
		int rowCount=0;
		String[] aScriptSplit = aScript.split("\n");	
		
		for(String aRow : aScriptSplit){		
			XSSFRow newRow = aSheet.createRow(rowCount++);			
			XSSFCell aCell = newRow.createCell(0);			
			aCell.setCellValue(aRow); 
		}
		
	}
	
	private void insertInconsistencesInExcel(XSSFSheet aSheet,String[] headers) {		
		int rowPosition = 0;
		IncidentsUtil.insertHeader(aSheet,headers,rowPosition++,1,headerStyle);
		for(String[] anInconsistence : inconsistencesList){		  
			XSSFRow newRow = aSheet.createRow(rowPosition++);			
			int colPosition = 0;
			String[] ar1={String.valueOf(rowPosition-1)};
			anInconsistence = (String[])ArrayUtils.addAll(ar1, anInconsistence);
			for(String aValue : anInconsistence){
				XSSFCellStyle aStyle;
				if(colPosition==0 || colPosition==1 || colPosition==13 || colPosition==14){
					aStyle = cellStyleGray;
				}else{
					aStyle = cellStyle;
				}				
				IncidentsUtil.createCellInRow(newRow,colPosition++,aValue,aStyle);
			}			
		}
		
	}
	
	private int insertDataByCurrency(XSSFSheet aSheet,int rowPosition,String tipo,String currency) {
		FinalReportDTO totalsDTO = new FinalReportDTO();
		totalsDTO.setRamo("Total");
		for (Map.Entry<String, FinalReportDTO> entry : aFinalReportList.entrySet()) {			
			FinalReportDTO aDTO = entry.getValue();
			int colPosition = 0;
			if(!aDTO.getTipo().equals(tipo) || !aDTO.getMoneda().equals(currency)){
				continue;
			}			
			XSSFRow newRow = aSheet.createRow(rowPosition++);			
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getRamo(),cellStyle);
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getMoneda(),cellStyle);			
			totalsDTO.setMoneda(aDTO.getMoneda());
			totalsDTO.setTipo(aDTO.getTipo());
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getValorGW(),cellStyle);			
			totalsDTO.setValorGW(IncidentsUtil.sumarDoubles(totalsDTO.getValorGW(),aDTO.getValorGW()));			
			if(aDTO.getTipo().equals("RESERVA")) {
			  IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getGastosGW(),cellStyle);
				totalsDTO.setGastosGW(IncidentsUtil.sumarDoubles(totalsDTO.getGastosGW(),aDTO.getGastosGW()));
			}			
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getReaseguroGW(),cellStyle);
			totalsDTO.setReaseguroGW(IncidentsUtil.sumarDoubles(totalsDTO.getReaseguroGW(),aDTO.getReaseguroGW()));			
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getValorSAP(),cellStyle);
			totalsDTO.setValorSAP(IncidentsUtil.sumarDoubles(totalsDTO.getValorSAP(),aDTO.getValorSAP()));			
			if(aDTO.getTipo().equals("RESERVA")) {
			  IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getGastosSAP(),cellStyle);
				totalsDTO.setGastosSAP(IncidentsUtil.sumarDoubles(totalsDTO.getGastosSAP(),aDTO.getGastosSAP()));
			}			
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getReaseguroSAP(),cellStyle);
			totalsDTO.setReaseguroSAP(IncidentsUtil.sumarDoubles(totalsDTO.getReaseguroSAP(),aDTO.getReaseguroSAP()));			
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getDiferenciaValor(),cellStyleGray);
			totalsDTO.setDiferenciaValor(IncidentsUtil.sumarDoubles(totalsDTO.getDiferenciaValor(),aDTO.getDiferenciaValor()));			
			if(aDTO.getTipo().equals("RESERVA")) {
			  IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getDiferenciaGastos(),cellStyleGray);
				totalsDTO.setDiferenciaGastos(IncidentsUtil.sumarDoubles(totalsDTO.getDiferenciaGastos(),aDTO.getDiferenciaGastos()));
			}			
			IncidentsUtil.createCellInRow(newRow,colPosition++,aDTO.getDiferenciaReaseguro(),cellStyleGray);			
			totalsDTO.setDiferenciaReaseguro(IncidentsUtil.sumarDoubles(totalsDTO.getDiferenciaReaseguro(),aDTO.getDiferenciaReaseguro()));
		}
		if(totalsDTO.getTipo()!=null){//Ingresar los totales
			int colPosition = 0;
			XSSFRow newRow = aSheet.createRow(rowPosition++);			
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getRamo(),headerStyle);
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getMoneda(),headerStyle);			
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getValorGW(),headerStyle);					
			if(totalsDTO.getTipo().equals("RESERVA")) {
				IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getGastosGW(),headerStyle);			
			}			
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getReaseguroGW(),headerStyle);
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getValorSAP(),headerStyle);
			if(totalsDTO.getTipo().equals("RESERVA")) {
				IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getGastosSAP(),headerStyle);
			}			
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getReaseguroSAP(),headerStyle);
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getDiferenciaValor(),headerStyle);
			if(totalsDTO.getTipo().equals("RESERVA")) {
				IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getDiferenciaGastos(),headerStyle);
			}			
			IncidentsUtil.createCellInRow(newRow,colPosition++,totalsDTO.getDiferenciaReaseguro(),headerStyle);					
		}		
		return rowPosition;
	}

	private void printInOutputText(String textToAdd) {
		outputTxt.setText(outputTxt.getText() + textToAdd);
		outputTxt.setCaretPosition(outputTxt.getDocument().getLength());
	}	

	public void readAndInsertRowsGw() throws Exception {
		for (String fileNameGw : fileNamesGw) {
			printInOutputText("\nRegistrando " + fileNameGw + "...");
			progressProcess(100, 0);
			String fileUrl = rutaCarpeta + "\\" + fileNameGw;
			File archivo = new File(fileUrl);
			BufferedReader br = Files.newBufferedReader(archivo.toPath(), StandardCharsets.UTF_8);			
			currentItemNumber = 0;
			String rowInfo = br.readLine();//la primer linea es cabecera                  
			int rowNum = (int) (archivo.length() / 220);//220 bytes es el promedio del tama??o de una linea
			ArrayList<Object> columnIdentifiers = determineColumns(fileNameGw);
			while ((rowInfo = br.readLine()) != null) {
				insertGwInfoInDB(rowInfo, columnIdentifiers);
				progressProcess(rowNum, ++currentItemNumber);
			}
			br.close();
			currentBarProgress = currentBarProgress + 10;
			progressTotal(100, currentBarProgress);
		}
	}

	public void readAndInsertRowsSap() throws Exception {
		for (String fileNameSap : fileNamesSap) {
			printInOutputText("\nRegistrando " + fileNameSap + "...");
			progressProcess(100, 0);
			String fileUrl = fileNameSap.compareTo(nomArchivoSapGastos) == 0 ? 
							 rutaCarpeta + "\\" + nomArchivoSapReserva : //para gastos usar el de reservas
							 rutaCarpeta + "\\" + fileNameSap; 
			File archivo = new File(fileUrl);
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			currentItemNumber = 0;
			String rowInfo = br.readLine();//la primer linea es cabecera                  
			int rowNum = (int) (archivo.length() / 80);//80 bytes es el promedio del tama??o de una linea
			ArrayList<Object> columnIdentifiers = determineColumns(fileNameSap);
			while ((rowInfo = br.readLine()) != null) {
				updateRowInDB(rowInfo, columnIdentifiers);
				progressProcess(rowNum, ++currentItemNumber);
			}
			fr.close();
			br.close();
			currentBarProgress = currentBarProgress + 10;
			progressTotal(100, currentBarProgress);
		}
	}

	private String determineReferenceValue(Object columnIdentifier, String[] rowInfoSplit) {
		if (columnIdentifier == null) {
			return null;
		}
		String value = rowInfoSplit[(int) columnIdentifier]
						.replaceAll("\"", "");
		if (value.contains("*")) {
			return value.substring(0, value.indexOf("*"));
		}
		if (value.length() == 0) {
			return null;
		}
		return value;
	}

	private void insertGwInfoInDB(String rowInfo, ArrayList<Object> columnIdentifiers) throws Exception {
		lastRowInfo = rowInfo;
		String[] rowInfoSplit = rowInfo.split(";");
		FinalReportDTO aDTO = new FinalReportDTO();
		aDTO.setOrigen(columnIdentifiers.get(0).toString());
		aDTO.setTipo(columnIdentifiers.get(1).toString());		
		aDTO.setRamo(IncidentsUtil.determineStringValue(columnIdentifiers.get(4), rowInfoSplit));
		String referencia = determineReferenceValue(columnIdentifiers.get(5), rowInfoSplit);		
		String transactionTime = IncidentsUtil.determineStringValue(columnIdentifiers.get(10), rowInfoSplit);		
		String clave = "";
		if(crueSinRamo){
			clave = referencia;
		}
		else{
		 clave = referencia == null ? null
						: //Si no tiene referencia, la clave queda null 
						referencia.toLowerCase() + "-" + aDTO.getRamo();  //si hay referencia la clave = referencia-ramo              
		}
		aDTO.setValorCien(IncidentsUtil.determineDoubleValue(columnIdentifiers.get(6), rowInfoSplit));
		aDTO.setValorReas(IncidentsUtil.determineDoubleValue(columnIdentifiers.get(7), rowInfoSplit));
		aDTO.setMoneda(IncidentsUtil.determineStringValue(columnIdentifiers.get(8), rowInfoSplit).toUpperCase());
		aDTO.setStatus(IncidentsUtil.determineStringValue(columnIdentifiers.get(9), rowInfoSplit));

		insertInReportDTOList(aDTO);

		currentClosure = closureController.findClosureByReferenciaOrigenTipo(aDTO.getOrigen(), aDTO.getTipo(), clave);
		if (currentClosure != null && clave != null) {
			currentClosure.setValorCienGw(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw())
							+ IncidentsUtil.determineDoubleValue(aDTO.getValorCien()));
			currentClosure.setValorReasGw(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw())
							+ IncidentsUtil.determineDoubleValue(aDTO.getValorReas()));
			closureController.edit(currentClosure);
		} else {//se crea nuevo registro si no existe una clave(referencia-ramo) igual, o el registro no tiene referencia
			Closure newClosure = new Closure();
			newClosure.setOrigen(aDTO.getOrigen());
			newClosure.setTipo(aDTO.getTipo());
			newClosure.setClaimnumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(2), rowInfoSplit));
			newClosure.setPolicynumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(3), rowInfoSplit));
			newClosure.setRamo(aDTO.getRamo());
			newClosure.setReferencia(clave);
			newClosure.setValorCienGw(aDTO.getValorCien());
			newClosure.setValorReasGw(aDTO.getValorReas());
			newClosure.setMoneda(aDTO.getMoneda());
			newClosure.setEstado(IncidentsUtil.determineStringValue(columnIdentifiers.get(9), rowInfoSplit));
			newClosure.setFecha(transactionTime);
			closureController.create(newClosure);
		}
	}
	
	private void updateRowInDB(String rowInfo, ArrayList<Object> columnIdentifiers) throws Exception {
		lastRowInfo = rowInfo;
		//Todos los registros que llegan a esta funcion seran de SAP
		
		String[] rowInfoSplit = rowInfo.split(";");
		FinalReportDTO aDTO = new FinalReportDTO();
		aDTO.setOrigen(columnIdentifiers.get(0).toString());
		aDTO.setTipo(columnIdentifiers.get(1).toString());
		aDTO.setRamo(IncidentsUtil.determineStringValue(columnIdentifiers.get(4), rowInfoSplit));
		String referencia = "";
		if(crueSinRamo){
			referencia = determineReferenceValue(columnIdentifiers.get(5), rowInfoSplit).toLowerCase();
		}else{
			referencia = determineReferenceValue(columnIdentifiers.get(5), rowInfoSplit).toLowerCase() + "-" + aDTO.getRamo();
		}
		aDTO.setValorCien(IncidentsUtil.determineDoubleValue(columnIdentifiers.get(6), rowInfoSplit));//sacado del excel
		aDTO.setValorReas(IncidentsUtil.determineDoubleValue(columnIdentifiers.get(7), rowInfoSplit));//sacado del excel        
		aDTO.setMoneda(IncidentsUtil.determineStringValue(columnIdentifiers.get(8), rowInfoSplit).toUpperCase());
		
		if(referencia.startsWith("#-")) {//ajustes manuales de SAP que se deben omitir (Movimientos SA)
			return;
		}
		insertInReportDTOList(aDTO);

		currentClosure = closureController.findClosureByReferenciaOrigenTipo("GW", aDTO.getTipo(), referencia);
		if (currentClosure != null) {//se encontro registro de GW, actualizarlo                    
			double valorCienSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorCienSap())
							+ IncidentsUtil.determineDoubleValue(aDTO.getValorCien());
			double valorReasSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorReasSap())
							+ IncidentsUtil.determineDoubleValue(aDTO.getValorReas());
			currentClosure.setValorCienSap(valorCienSap);
			currentClosure.setValorReasSap(valorReasSap);
			if (aDTO.getTipo().equals("RESERVA")
							|| aDTO.getTipo().equals("GASTO")
							|| aDTO.getTipo().equals("SALVAMENTO")) {
				currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) + valorCienSap);
				currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) - valorReasSap);
			} else if (aDTO.getTipo().equals("PAGO")) {
				currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) - valorCienSap);
				currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) + valorReasSap);
			}
			closureController.edit(currentClosure);
		} else {//si no encuntra resultados indica que esta en SAP pero no esta en GW

			currentClosure = closureController.findClosureByReferenciaOrigenTipo("SAP", aDTO.getTipo(), referencia);
			if (currentClosure != null) {//exite registro de SAP hay que actualizarlo
				double valorCienSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorCienSap())
								+ IncidentsUtil.determineDoubleValue(aDTO.getValorCien());
				double valorReasSap = IncidentsUtil.determineDoubleValue(currentClosure.getValorReasSap())
								+ IncidentsUtil.determineDoubleValue(aDTO.getValorReas());
				currentClosure.setValorCienSap(valorCienSap);
				currentClosure.setValorReasSap(valorReasSap);
				if (currentClosure.getValorCienGw() != null) {//actualizar diferencia solo si hay registro tambien en gw
					if (aDTO.getTipo().equals("RESERVA")
									|| aDTO.getTipo().equals("GASTO")
									|| aDTO.getTipo().equals("SALVAMENTO")) {
						currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) + valorCienSap);
						currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) - valorReasSap);
					} else if (aDTO.getTipo().equals("PAGO")) {
						currentClosure.setDiferCien(IncidentsUtil.determineDoubleValue(currentClosure.getValorCienGw()) - valorCienSap);
						currentClosure.setDiferReas(IncidentsUtil.determineDoubleValue(currentClosure.getValorReasGw()) + valorReasSap);
					}
				}
				closureController.edit(currentClosure);
			} else {//no existe registro de SAP hay que crearlo
				Closure newClosure = new Closure();
				newClosure.setOrigen(aDTO.getOrigen());
				newClosure.setTipo(aDTO.getTipo());
				newClosure.setClaimnumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(2), rowInfoSplit));
				newClosure.setPolicynumber(IncidentsUtil.determineStringValue(columnIdentifiers.get(3), rowInfoSplit));
				newClosure.setRamo(aDTO.getRamo());
				newClosure.setReferencia(referencia);
				//ValorCienGw va a quedar null
				//ValorReasGw va a quedar null
				newClosure.setValorCienSap(aDTO.getValorCien());
				newClosure.setValorReasSap(aDTO.getValorReas());

				newClosure.setMoneda(IncidentsUtil.determineStringValue(columnIdentifiers.get(8), rowInfoSplit));
				newClosure.setEstado(IncidentsUtil.determineStringValue(columnIdentifiers.get(9), rowInfoSplit));
				closureController.create(newClosure);
			}
		}
	}

	/*
  Dependiendo de cada archivo saber de que columna se saca un dato
	 */
	private ArrayList<Object> determineColumns(String fileName) {
		switch (fileName) {
			case "SAP_RESERVAS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo Ref ReseImpMD ReaImpMD  Moneda Estado
				return new ArrayList<>(Arrays.asList("SAP", "RESERVA", null, 3, 2, 4, 5, 9, 0, null));
			case "GW_RESERVAS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo PuIdTra VlrBruto Cedido Moneda Estado CreaTime
				return new ArrayList<>(Arrays.asList("GW", "RESERVA", 1, 3, 7, 10, 16, 13, 17, 8, 9));
			case "SAP_GASTOS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo Ref GtLiImpMD ReaImpMD Moneda Estado
				return new ArrayList<>(Arrays.asList("SAP", "GASTO", null, 3, 2, 4, 7, null, 0, null));
			case "GW_GASTOS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo Ref liqResExp VlrRea Moneda Estado CreaTime
				return new ArrayList<>(Arrays.asList("GW", "GASTO", 1, 2, 7, 11, 19, null, 14, 9,13));
			case "SAP_PAGOS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo Ref PagImpMD  ReaImpMD Moneda Estado
				return new ArrayList<>(Arrays.asList("SAP", "PAGO", null, 3, 2, 4, 6, 8, 0, null));
			case "GW_PAGOS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo NumTra VlrSinCoa Cedido Moneda Estado CreaTime
				return new ArrayList<>(Arrays.asList("GW", "PAGO", 2, 3, 4, 10, 24, 14, 20, 17,18));
			case "SAP_SALVAMENTOS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo Ref SalImpMD ReaImpMD  Moneda Estado
				return new ArrayList<>(Arrays.asList("SAP", "SALVAMENTO", null, 3, 2, 4, 6, 8, 0, null));
			case "GW_SALVAMENTOS.csv":
				//ORIGEN TIPO Claim# Poli# Ramo NumTra VlrSinCoa Cedido Moneda Estado CreaTime
				return new ArrayList<>(Arrays.asList("GW", "SALVAMENTO", 1, 2, 3, 9, 19, 13, 18, 11,12));
		}
		return null;
	}
	
	private void searchInconsistencies() {
		inconsistencesList = new ArrayList<>();
		
		currentBarProgress = 70;
		progressTotal(100, currentBarProgress);

		insertInconsistencyInFile("GW", "RESERVA", ClosureTypeEnum.diferenciaCienMayorQue);
		insertInconsistencyInFile("GW", "RESERVA", ClosureTypeEnum.diferenciaCienMenorQue);
		insertInconsistencyInFile("GW", "RESERVA", ClosureTypeEnum.diferenciaReaseguroMayorQue);
		insertInconsistencyInFile("GW", "RESERVA", ClosureTypeEnum.diferenciaReaseguroMenorQue);
		insertInconsistencyInFile("GW", "RESERVA", ClosureTypeEnum.cienNoEncontradoEnSap);
		insertInconsistencyInFile("SAP", "RESERVA", ClosureTypeEnum.cienNoEncontradoEnGW);

		insertInconsistencyInFile("GW", "PAGO", ClosureTypeEnum.diferenciaCienMayorQue);
		insertInconsistencyInFile("GW", "PAGO", ClosureTypeEnum.diferenciaCienMenorQue);
		insertInconsistencyInFile("GW", "PAGO", ClosureTypeEnum.diferenciaReaseguroMayorQue);
		insertInconsistencyInFile("GW", "PAGO", ClosureTypeEnum.diferenciaReaseguroMenorQue);
		insertInconsistencyInFile("GW", "PAGO", ClosureTypeEnum.cienNoEncontradoEnSap);
		insertInconsistencyInFile("SAP", "PAGO", ClosureTypeEnum.cienNoEncontradoEnGW);

		insertInconsistencyInFile("GW", "GASTO", ClosureTypeEnum.diferenciaCienMayorQue);
		insertInconsistencyInFile("GW", "GASTO", ClosureTypeEnum.diferenciaCienMenorQue);
		insertInconsistencyInFile("GW", "GASTO", ClosureTypeEnum.diferenciaReaseguroMayorQue);
		insertInconsistencyInFile("GW", "GASTO", ClosureTypeEnum.diferenciaReaseguroMenorQue);
		insertInconsistencyInFile("GW", "GASTO", ClosureTypeEnum.cienNoEncontradoEnSap);
		insertInconsistencyInFile("SAP", "GASTO", ClosureTypeEnum.cienNoEncontradoEnGW);

		insertInconsistencyInFile("GW", "SALVAMENTO", ClosureTypeEnum.diferenciaCienMayorQue);
		insertInconsistencyInFile("GW", "SALVAMENTO", ClosureTypeEnum.diferenciaCienMenorQue);
		insertInconsistencyInFile("GW", "SALVAMENTO", ClosureTypeEnum.diferenciaReaseguroMayorQue);
		insertInconsistencyInFile("GW", "SALVAMENTO", ClosureTypeEnum.diferenciaReaseguroMenorQue);
		insertInconsistencyInFile("GW", "SALVAMENTO", ClosureTypeEnum.cienNoEncontradoEnSap);
		insertInconsistencyInFile("SAP", "SALVAMENTO", ClosureTypeEnum.cienNoEncontradoEnGW);
	}

	private boolean isZeroOrNull(Double value) {
		if (value == null) {
			return true;
		}
		return value == 0;
	}

	private boolean isOmitedItem(Closure aResult) {

		if (aResult.getValorCienGw() == null && isZeroOrNull(aResult.getValorCienSap())
						&& isZeroOrNull(aResult.getValorReasSap())) {//tiene que estar de primera
			return true;
		}
		if (aResult.getEstado() == null) {//las siguietes instricciones no funcionan sin esta validacion
			return false;
		}
		if (aResult.getEstado().contains("Anulado") && aResult.getValorCienSap() == null
						&& aResult.getValorCienGw() == 0 && aResult.getValorReasGw() == 0) {
			return true;
		}
		if (aResult.getEstado().contains("Aprobaci")) {//Apobacion pendiente no se analiza 
			return true;
		}
		return aResult.getEstado().contains("En espera");//En espera de env??o no se analiza 
	}
	
	private void insertInconsistencyInFile(String origen, String tipo, ClosureTypeEnum criterio) {
		progressProcess(100, 0);
		progressTotal(100, ++currentBarProgress);
		printInOutputText("\nAnalizando " + tipo + " - " + criterio.name() + "...");

		if(inconsistencesList.size()>inconsistencesLimit){
			return;
		}
		
		List<Closure> resultList = searchInconsistency(origen, tipo, criterio, monetaryLimit);
		if (resultList.isEmpty()) {
			return;
		} else {
			totalItemNumber = resultList.size();
			currentItemNumber = 0;
		}
		
		for (Closure aResult : resultList) {
			int colNum = 0;
			String[] strRow = new String[14];			
			if(inconsistencesList.size()==inconsistencesLimit){
				strRow[colNum]="ERROR Limite de "+ inconsistencesLimit +" se ha superado, se debe verificar informes y volver a ejectuar.";
				inconsistencesList.add(strRow);	
				return;
			}
			lastRowInfo = IncidentsUtil.determineRowInfo(aResult);
			progressProcess(totalItemNumber, ++currentItemNumber);
			if (isOmitedItem(aResult)) {
				continue;
			}
			strRow[colNum++] = tipo + " " + criterio.name();			
			strRow[colNum++] = aResult.getReferencia();
			strRow[colNum++] = aResult.getRamo();
			strRow[colNum++] = aResult.getClaimnumber();
			strRow[colNum++] = aResult.getPolicynumber();
			strRow[colNum++] = aResult.getMoneda();
			strRow[colNum++] = aResult.getEstado();
			strRow[colNum++] = aResult.getFecha();

			if (tipo.equals("GASTO")) {//gasto no realiza analisis de reaseguro
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getValorCienGw());
				strRow[colNum++] = "NO APLICA";
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getValorCienSap());
				strRow[colNum++] = "NO APLICA";
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getDiferCien());
				strRow[colNum++] = "NO APLICA";
			} else {
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getValorCienGw());
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getValorReasGw());
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getValorCienSap());
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getValorReasSap());
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getDiferCien());
				strRow[colNum++] = IncidentsUtil.determineStringValue(aResult.getDiferReas());
			}			
			inconsistencesList.add(strRow);			
		}
	}

	private List<Closure> searchInconsistency(String origen, String tipo, ClosureTypeEnum typeDif, String limit) {
		searchCriteria = new HashMap<>();
		searchCriteria.put(ClosureTypeEnum.origen, origen);
		searchCriteria.put(ClosureTypeEnum.tipo, tipo);
		searchCriteria.put(typeDif, limit);
		return closureController.findBySearchCriteria(searchCriteria);
	}

	private void regenerateTableClosure() throws SQLException, ClassNotFoundException {
		conection.connect();
		conection.removeTableClosure();
		conection.createTablesClosures();
		conection.disconect();
	}

	private void progressProcess(int total, int actual) {
		this.progressBarProceso.setValue((int) ((actual * 100) / total));
	}

	private void progressTotal(int total, int actual) {
		this.progressBar.setValue((int) ((actual * 100) / total));
	}

	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    outputTxt = new javax.swing.JTextArea();
    jPanel1 = new javax.swing.JPanel();
    spinnerMaxErrors = new javax.swing.JSpinner();
    jLabel2 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    spinnerLimit = new javax.swing.JSpinner();
    btnSelectFile = new javax.swing.JButton();
    btnStart = new javax.swing.JButton();
    jPanel2 = new javax.swing.JPanel();
    jLabel4 = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();
    progressBarProceso = new javax.swing.JProgressBar();
    jLabel3 = new javax.swing.JLabel();
    labelFile = new javax.swing.JLabel();
    checkRamo = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("CIERRE FINANCIERO");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    outputTxt.setEditable(false);
    outputTxt.setColumns(20);
    outputTxt.setRows(5);
    outputTxt.setName(""); // NOI18N
    jScrollPane1.setViewportView(outputTxt);

    jLabel2.setText("Limite Monetario");

    jLabel5.setText("Num Max Errores");

    btnSelectFile.setText("Seleccionar Carpeta");
    btnSelectFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelectFileActionPerformed(evt);
      }
    });

    btnStart.setText("Iniciar");
    btnStart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnStartActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
              .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addComponent(spinnerMaxErrors, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
              .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addComponent(spinnerLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addComponent(btnSelectFile, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(spinnerLimit)
              .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel5)
              .addComponent(spinnerMaxErrors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(9, 9, 9)
            .addComponent(btnSelectFile)
            .addGap(12, 12, 12))))
    );

    btnStart.getAccessibleContext().setAccessibleName("btnStart");
    btnStart.getAccessibleContext().setAccessibleDescription("");

    jLabel4.setText("PROGRESO TOTAL: ");

    progressBar.setStringPainted(true);

    progressBarProceso.setStringPainted(true);

    jLabel3.setText("PROCESO ACTUAL: ");

    checkRamo.setSelected(true);
    checkRamo.setText("Usar Ramo");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(labelFile, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(checkRamo))
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
              .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(progressBarProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap(12, Short.MAX_VALUE)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(progressBarProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(labelFile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(checkRamo)))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
        .addContainerGap())
    );

    setSize(new java.awt.Dimension(798, 619));
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void btnSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFileActionPerformed
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			rutaCarpeta = fileChooser.getSelectedFile().getAbsolutePath();
			labelFile.setText(rutaCarpeta);
			btnStart.setEnabled(true);
		}

  }//GEN-LAST:event_btnSelectFileActionPerformed

  private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
		runProcess = true;
		h1 = new Thread(this);
		h1.start();
  }//GEN-LAST:event_btnStartActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

  }//GEN-LAST:event_formWindowClosing

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DialogFinancialClosure.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>


		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				DialogFinancialClosure dialog = new DialogFinancialClosure(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnSelectFile;
  private javax.swing.JButton btnStart;
  private javax.swing.JCheckBox checkRamo;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labelFile;
  private javax.swing.JTextArea outputTxt;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JProgressBar progressBarProceso;
  private javax.swing.JSpinner spinnerLimit;
  private javax.swing.JSpinner spinnerMaxErrors;
  // End of variables declaration//GEN-END:variables

}
