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
import java.util.Date;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author santvamu
 */
public class IncidentsUtil {

	static DecimalFormat df = new DecimalFormat("#.#");

	static {
		df.setMaximumFractionDigits(3);
	}
	
	public static XSSFCellStyle createHeaderStyle(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = (XSSFCellStyle)anExcelWorbook.createCellStyle();
		Font font = anExcelWorbook.createFont();
		font.setBold(true);
		aStyle.setFont(font);		
		aStyle.setBorderBottom(BorderStyle.THIN);
		aStyle.setBorderTop(BorderStyle.THIN);
		aStyle.setBorderRight(BorderStyle.THIN);
		aStyle.setBorderLeft(BorderStyle.THIN);
		byte[] rgb = new byte[]{(byte)221, (byte)235, (byte)247};
		aStyle.setFillForegroundColor(new XSSFColor(rgb, null));
		aStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return aStyle;
	}
	
	public static XSSFCellStyle createCellStyle(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = anExcelWorbook.createCellStyle();		
		aStyle.setBorderBottom(BorderStyle.THIN);
		aStyle.setBorderTop(BorderStyle.THIN);
		aStyle.setBorderRight(BorderStyle.THIN);
		aStyle.setBorderLeft(BorderStyle.THIN);
		return aStyle;
	}
	
	public static XSSFCellStyle createCellStyleGray(XSSFWorkbook anExcelWorbook) {
		XSSFCellStyle aStyle = anExcelWorbook.createCellStyle();		
		aStyle.setBorderBottom(BorderStyle.THIN);
		aStyle.setBorderTop(BorderStyle.THIN);
		aStyle.setBorderRight(BorderStyle.THIN);
		aStyle.setBorderLeft(BorderStyle.THIN);
		byte[] rgb = new byte[]{(byte)214, (byte)220, (byte)228};
		aStyle.setFillForegroundColor(new XSSFColor(rgb, null));
		aStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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

	public static String getModelHelpString() {
		return "/*--------------------------------------------------------------------------------\n"
						+ " 1. Ejecutar este codigo en Scratchpad,\n"
						+ " 2. genera una carpeta en D: con el modelo de productos de claims \n"
						+ " 3. esta carpeta se selecciona al usar 'Cargar Modelo' \n"
						+ " 4. Si algun valor buscado no es encintrado indica que:\n"
						+ "    a. Hay que actulizar mediante el boton 'Cargar Modelo'"
						+ "    b. El valor buscado esta retirado(se debe verificarlo en los XML de GW)"
						+ " --------------------------------------------------------------------------------\n"
						+ "*/\n"
						+ "\n"
						+ "uses java.io.File\n"
						+ "uses java.io.FileOutputStream\n"
						+ "uses java.io.PrintWriter\n"
						+ "uses java.util.HashMap\n"
						+ "uses java.text.SimpleDateFormat\n"
						+ "uses java.util.Date\n"
						+ "uses java.util.ArrayList\n"
						+ "uses gw.api.util.LocaleUtil\n"
						+ "uses gw.i18n.ILocale\n"
						+ "uses gw.entity.TypeKey\n"
						+ "uses gw.entity.ITypeList\n"
						+ "uses gw.lang.reflect.TypeSystem\n"
						+ "\n"
						+ "var _esCO : ILocale = LocaleUtil.toLanguage(typekey.LanguageType.TC_ES_CO)\n"
						+ "var _enUS : ILocale = LocaleUtil.toLanguage(typekey.LanguageType.TC_EN_US)\n"
						+ "\n"
						+ "var _dateFormat = new SimpleDateFormat(\"HH:mm:ss\")\n"
						+ "var _startDate = new Date()\n"
						+ "var _pathOut = \"D:/\" + _startDate + \"_outLobModels\"\n"
						+ "var _writeLobModel : PrintWriter = null\n"
						+ "var _writeLob : PrintWriter = null\n"
						+ "var _offering = \"\"\n"
						+ "var _lobCode = \"\"\n"
						+ "var _policyType = \"\"\n"
						+ "var _lossType = \"\"\n"
						+ "var _coverageType = \"\"\n"
						+ "var _internalPolicyType = \"\"\n"
						+ "var _policyTab = \"\"\n"
						+ "var _lossPartyType = \"\"\n"
						+ "var _coverageSubType = \"\"\n"
						+ "var _exposureType = \"\"\n"
						+ "var _covTermPattern = \"\"\n"
						+ "var _coverageSubtypeClass = \"\"\n"
						+ "var _limitDeducible = \"\"\n"
						+ "var _lossCauses = \"\"\n"
						+ "var _costCategories = \"\"\n"
						+ "var _mapCovTermsByCoverage : HashMap<String, List<typekey.CovTermPattern>> = null\n"
						+ "var _mapCostCategoryByCovTerm : HashMap<String, String> = null\n"
						+ "var _mapLossPartyByCoverageSubType : HashMap<String, String> = null\n"
						+ "var _mapLossCauseByCoverageType : HashMap<String, String> = null\n"
						+ "var _mapOfferingByCov : HashMap<String, String> = null\n"
						+ "var _mapLossTypeByLobCode : HashMap<String, String> = null\n"
						+ "var _mapInternalPolicyByPolicyType : HashMap<String, String> = null\n"
						+ "var _mapPolicyTabByPolicyType : HashMap<String, String> = null\n"
						+ "\n"
						+ "startProcess()\n"
						+ "\n"
						+ "/**/\n"
						+ "function startProcess() {\n"
						+ "  createDirectories()\n"
						+ "  print(timeEvent(\"Directorios cargados... \"))\n"
						+ "\n"
						+ "  generateTypeListByName(\"OfferingType_Ext\")\n"
						+ "  generateTypeListByName(\"LOBCode\")\n"
						+ "  generateTypeListByName(\"PolicyType\")\n"
						+ "  generateTypeListByName(\"LossType\")\n"
						+ "  generateTypeListByName(\"CoverageType\")\n"
						+ "  generateTypeListByName(\"InternalPolicyType\")\n"
						+ "  generateTypeListByName(\"PolicyTab\")\n"
						+ "  generateTypeListByName(\"LossPartyType\")\n"
						+ "  generateTypeListByName(\"CoverageSubtype\")\n"
						+ "  generateTypeListByName(\"ExposureType\")\n"
						+ "  generateTypeListByName(\"CovTermPattern\")\n"
						+ "  generateTypeListByName(\"LossCause\")\n"
						+ "  generateTypeListByName(\"CostType\")\n"
						+ "  generateTypeListByNameAndCategory(\"CostCategory\",typekey.CostType)\n"
						+ "  print(timeEvent(\"TypeList cargados... \"))\n"
						+ "\n"
						+ "  _mapCovTermsByCoverage         = loadMapCovTermsByCoverage()\n"
						+ "  _mapCostCategoryByCovTerm      = loadMapTypeListWithConcatenatedCategory(\"CostCategory\",typekey.CovTermPattern)\n"
						+ "  _mapLossPartyByCoverageSubType = loadMapTypeListWithConcatenatedCategory(\"LossPartyType\",typekey.CoverageSubtype)\n"
						+ "  _mapLossCauseByCoverageType    = loadMapTypeListWithConcatenatedCategory(\"LossCause\",typekey.CoverageType)\n"
						+ "  _mapOfferingByCov              = loadMapTypeListWithConcatenatedCategory(typekey.OfferingType_Ext,\"CoverageType\")\n"
						+ "  _mapLossTypeByLobCode          = loadMapTypeListWithConcatenatedCategory(typekey.LossType,\"LOBCode\")\n"
						+ "  _mapInternalPolicyByPolicyType = loadMapTypeListWithConcatenatedCategory(typekey.InternalPolicyType,\"PolicyType\")\n"
						+ "  _mapPolicyTabByPolicyType      = loadMapTypeListWithConcatenatedCategory(typekey.PolicyTab,\"PolicyType\")\n"
						+ "  print(timeEvent(\"Mapas cargados... \"))\n"
						+ "\n"
						+ "  generateLobModel()\n"
						+ "  print(timeEvent(\"FIN, LobModel cargado \"))\n"
						+ "}\n"
						+ "\n"
						+ "\n"
						+ "/**/\n"
						+ "function generateLobModel() {\n"
						+ "  var header = (\"Offering\\t\" + \"LOBCode\\t\" + \"PolicyType\\t\" + \"LossType\\t\" + \"CoverageType\\t\" + \"InternalPolicyType\\t\" + \"PolicyTab\\t\" + \"LossPartyType\\t\" + \"CoverageSubtype\\t\" + \"ExposureType\\t\" + \"CovTermPattern\\t\" + \"CoverageSubtypeClass\\t\" + \"Limite o Deducible\\t\" + \"Causas por cobertura\\t\" + \"Categorias de costo\")\n"
						+ "  var fileLobModel = new File(_pathOut + \"/LobModel.txt\")\n"
						+ "  fileLobModel.write(header)\n"
						+ "  _writeLobModel = new PrintWriter(new FileOutputStream(fileLobModel, true))\n"
						+ "  _writeLobModel.write(\"\\n\")  \n"
						+ "  typekey.LOBCode.getTypeKeys(false).each(\\lob -> {\n"
						+ "    lob.Categories.whereTypeIs(PolicyType).each(\\ polType -> {\n"
						+ "      polType.Categories.whereTypeIs(CoverageType).where(\\ cov -> !cov.Retired).each(\\ cov -> {\n"
						+ "        cov.Categories.whereTypeIs(CoverageSubtype).where(\\subcov -> !subcov.Retired).each(\\ subcov -> {\n"
						+ "          var exp = subcov.Categories.whereTypeIs(typekey.ExposureType).first()\n"
						+ "          _coverageSubtypeClass = determineCoverageSubtypeClass(subcov)\n"
						+ "          _lobCode = lob.Code\n"
						+ "          _policyType = polType.Code\n"
						+ "          _coverageType = cov.Code\n"
						+ "          _coverageSubType = subcov.Code\n"
						+ "          _exposureType = exp.Code\n"
						+ "          _offering = _mapOfferingByCov.get(cov.Code)\n"
						+ "          _lossType = _mapLossTypeByLobCode.get(lob.Code)\n"
						+ "          _internalPolicyType = _mapInternalPolicyByPolicyType.get(polType.Code)\n"
						+ "          _policyTab = _mapPolicyTabByPolicyType.get(polType.Code)\n"
						+ "          _lossPartyType = _mapLossPartyByCoverageSubType.get(subcov.Code)\n"
						+ "          _lossCauses = _mapLossCauseByCoverageType.get(cov.Code) ?: \"\"\n"
						+ "          var covTermByCov = _mapCovTermsByCoverage.get(cov.Code)\n"
						+ "          var CovTermPatternList = covTermByCov?.where(\\covT ->\n"
						+ "                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).contains(subcov)) or\n"
						+ "                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).IsEmpty and covT.Categories.whereTypeIs(typekey.ExposureType).contains(exp)) or\n"
						+ "                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).IsEmpty and covT.Categories.whereTypeIs(typekey.ExposureType).IsEmpty))\n"
						+ "          if(CovTermPatternList.HasElements) {\n"
						+ "            CovTermPatternList.each(\\covTerm -> {\n"
						+ "              _limitDeducible = getLimOrDed(covTerm)\n"
						+ "              _covTermPattern = covTerm.Code\n"
						+ "              _costCategories = _mapCostCategoryByCovTerm.get(covTerm.Code) ?: \"\"\n"
						+ "              write()\n"
						+ "            })\n"
						+ "          }else {\n"
						+ "            _covTermPattern = \"\"\n"
						+ "            _limitDeducible = \"\"\n"
						+ "            _costCategories = \"\"\n"
						+ "            write()\n"
						+ "          }\n"
						+ "        })\n"
						+ "      })\n"
						+ "    })\n"
						+ "  })\n"
						+ "  _writeLobModel.flush()\n"
						+ "  _writeLobModel.close()\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function createDirectories() {\n"
						+ "  if (!getFile(_pathOut).exists()) {\n"
						+ "    getFile(_pathOut).mkdirs()\n"
						+ "  }\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function getFile(path : String) : File {\n"
						+ "  return new File(path)\n"
						+ "}\n"
						+ "/**/\n"
						+ "function timeEvent(valueText : String) : String {\n"
						+ "  var currentDate = new Date()\n"
						+ "  return valueText + _dateFormat.format(currentDate) + \" Duracion \" + timeDiff(_startDate,new Date()) + \" segundos\"\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function timeDiff(fechaInicio : Date, fechaTermino : Date) : float {\n"
						+ "  return ((fechaTermino.getTime() / 1000) - (fechaInicio.getTime() / 1000))\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function getLimOrDed(term : CovTermPattern) : String {\n"
						+ "  for (filter in typekey.CovTermPattern.Type.TypeFilters) {\n"
						+ "    if (filter.Includes.contains(term)) {\n"
						+ "      return filter.Name.replace(\"_Ext\", \"\")\n"
						+ "    }\n"
						+ "  }\n"
						+ "  return \"\"\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function determineCoverageSubtypeClass(subcov : typekey.CoverageSubtype) : String {\n"
						+ "  if (subcov.Code.contains(\"Art\")) {\n"
						+ "    return \"Articulo\"\n"
						+ "  }\n"
						+ "  if (subcov.Code.contains(\"Bkt\") or subcov.Code.contains(\"Blanket\") or subcov.Code.contains(\"BKT\")) {\n"
						+ "    return \"Blanket\"\n"
						+ "  }\n"
						+ "  if (subcov.Code.contains(\"Ubi\")) {\n"
						+ "    return \"Ubicacion\"\n"
						+ "  }\n"
						+ "  return \"\"\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function write() {\n"
						+ "  var body = (_offering + \"\\t\" + _lobCode + \"\\t\" + _policyType + \"\\t\" + _lossType + \"\\t\" + _coverageType + \"\\t\" +\n"
						+ "      _internalPolicyType + \"\\t\" + _policyTab + \"\\t\" + _lossPartyType + \"\\t\" + _coverageSubType + \"\\t\" + _exposureType + \"\\t\" +\n"
						+ "      _covTermPattern + \"\\t\" + _coverageSubtypeClass + \"\\t\" + _limitDeducible + \"\\t\" + _lossCauses + \"\\t\" + _costCategories)\n"
						+ "  _writeLobModel.write(body)\n"
						+ "  _writeLobModel.write(\"\\n\")\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function generateTypeListByNameAndCategory(typeListName : String, aTypeTarget : Type<TypeKey>) {\n"
						+ "  var aTypeList = TypeSystem.getByFullName(\"typekey.\" + typeListName) as ITypeList\n"
						+ "  var fileTypeList = new File(_pathOut + \"/\" + typeListName + \".txt\")\n"
						+ "  fileTypeList.write(\"Code\\tNameES\\tNameUS\\t\" + typeListName + \"\\n\")\n"
						+ "  var writerTypeList = new PrintWriter(new FileOutputStream(fileTypeList, true))\n"
						+ "  aTypeList.getTypeKeys(false).each(\\ aTypeKey -> {\n"
						+ "    writerTypeList.write(aTypeKey.Code + \"\\t\" + aTypeKey.getDisplayName(_esCO) + \"\\t\" + aTypeKey.getDisplayName(_enUS) + \"\\t\" +\n"
						+ "        aTypeKey.Categories.whereTypeIs(aTypeTarget)*.Code.join(\";\") + \"\\n\")\n"
						+ "  })\n"
						+ "  writerTypeList.flush()\n"
						+ "  writerTypeList.close()\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function generateTypeListByName(typeListName : String) {\n"
						+ "  var aTypeList = TypeSystem.getByFullName(\"typekey.\" + typeListName) as ITypeList\n"
						+ "  var fileTypeList = new File(_pathOut + \"/\" + typeListName + \".txt\")\n"
						+ "  fileTypeList.write(\"Code\\tNameES\\tNameUS\\n\")\n"
						+ "  var writerTypeList = new PrintWriter(new FileOutputStream(fileTypeList, true))\n"
						+ "  aTypeList.getTypeKeys(false).each(\\ aTypeKey -> {\n"
						+ "    writerTypeList.write(aTypeKey.Code + \"\\t\" + aTypeKey.getDisplayName(_esCO) + \"\\t\" + aTypeKey.getDisplayName(_enUS) + \"\\n\")\n"
						+ "  })\n"
						+ "  writerTypeList.flush()\n"
						+ "  writerTypeList.close()\n"
						+ "}\n"
						+ "\n"
						+ "/**/\n"
						+ "function loadMapCovTermsByCoverage() : HashMap<String, List<typekey.CovTermPattern>> {\n"
						+ "  var mapCovTermsByCoverage = new HashMap<String, List<typekey.CovTermPattern>>()\n"
						+ "  for (aCovTerm in typekey.CovTermPattern.getTypeKeys(false)) {\n"
						+ "    var coverageList =  aCovTerm.Categories.whereTypeIs(typekey.CoverageType)\n"
						+ "    for (aCoverage in coverageList) {\n"
						+ "      var aResultSearch = mapCovTermsByCoverage.get(aCoverage.Code)\n"
						+ "      if(aResultSearch == null) {\n"
						+ "        aResultSearch = new ArrayList<typekey.CovTermPattern>()\n"
						+ "      }\n"
						+ "      aResultSearch.add(aCovTerm)\n"
						+ "      mapCovTermsByCoverage.put(aCoverage.Code, aResultSearch)\n"
						+ "    }\n"
						+ "  }\n"
						+ "  return mapCovTermsByCoverage\n"
						+ "}\n"
						+ "\n"
						+ "/**\n"
						+ "* se usa cuando en la categoria esta la clave del mapa y en los typecodes(del primer parametro) estan los valores a concatenar\n"
						+ "*/\n"
						+ "function loadMapTypeListWithConcatenatedCategory(typeKeyName : String, categoryFilter : Type<TypeKey>) : HashMap<String, String> {\n"
						+ "  var mapReturn = new HashMap<String, String>()\n"
						+ "  var aTypeKey = TypeSystem.getByFullName(\"typekey.\" + typeKeyName) as ITypeList\n"
						+ "  for (aTypeCode in aTypeKey.getTypeKeys(false)) {\n"
						+ "    var categories =  aTypeCode.Categories.whereTypeIs(categoryFilter)\n"
						+ "    for (aCategory in categories) {\n"
						+ "      var mapResultSearch = mapReturn.get(aCategory.Code)\n"
						+ "      mapReturn.put(aCategory.Code, mapResultSearch == null ? aTypeCode.Code : (mapResultSearch + \";\" + aTypeCode.Code))\n"
						+ "    }\n"
						+ "  }\n"
						+ "  return mapReturn\n"
						+ "}\n"
						+ "\n"
						+ "\n"
						+ "/**\n"
						+ "* se usa cuando en el typecode(del segundo parametro) esta la clave del mapa y en las categorias estan los valores a concatenar\n"
						+ "*/\n"
						+ "function loadMapTypeListWithConcatenatedCategory(categoryFilter : Type<TypeKey>, typeKeyName : String) : HashMap<String, String> {\n"
						+ "  var mapReturn = new HashMap<String, String>()\n"
						+ "  var aTypeKey = TypeSystem.getByFullName(\"typekey.\" + typeKeyName) as ITypeList\n"
						+ "  for (aTypeCode in aTypeKey.getTypeKeys(false)) {\n"
						+ "    var categoriesConcatenate =  aTypeCode.Categories.whereTypeIs(categoryFilter).where(\\aCate -> not aCate.Retired)*.Code.join(\";\")\n"
						+ "    mapReturn.put(aTypeCode.Code, categoriesConcatenate)\n"
						+ "  }\n"
						+ "  return mapReturn\n"
						+ "}";
	}

	public static String getHomologationsHelpString() {
		return "--HOMOLOGACIONES EXISTENTES DE GENERAL A GW (INSTANCIA PDNLNF)---------------------- 14020\n"
						+ "--El archivo  creado debe tener el nombre: general_core.csv\n"
						+ "SELECT \n"
						+ "       t.CDTIPO       AS homologation,\n"
						+ "       f.CDAPLICACION AS source,\n"
						+ "       t.CDAPLICACION AS target,\n"
						+ "       f.CDVALOR AS source_value, \n"
						+ "       t.CDVALOR AS target_value\n"
						+ "FROM   \n"
						+ "       TTRD_MAPAS f, \n"
						+ "       TTRD_MAPAS t, \n"
						+ "       TTRD_HOMOLOGACION h\n"
						+ "WHERE  f.CDAPLICACION = 'GENERAL_GW'       AND --fuente\n"
						+ "       t.CDAPLICACION = 'CORE_GW'          AND --destino\n"
						+ "       t.CDTIPO IN (\n"
						+ "           'APIGATEWAY_PRODUCT_TYPE_CODE_FROM_OFFERING_TYPE_GW',\n"
						+ "           'COBERTURA_GW',\n"
						+ "           'COVERAGE_FROM_OFFERING_COVERAGE_ARTICLE_GW',\n"
						+ "           'GARANTIA_SUBGARANTIA_EMPRESARIAL_GW',\n"
						+ "           'LINE_FROM_OFFERING_TYPE_GW',\n"
						+ "           'LOSS_CAUSE_FROM_SERVICES_GW',\n"
						+ "           'LOSS_TYPE_FROM_LINE_SUBLINE_GW',\n"
						+ "           'OFFERING_TYPE_FROM_LINE_SUBLINE_GW',\n"
						+ "           'POLICY_TYPE_FROM_LINE_SUBLINE_GW',\n"
						+ "           'REINSURANCECONTRACT_FROM_OFFERING_COVERAGE_GW',\n"
						+ "           'TERMINO_GW')  AND --tipo de homologacion\n"
						+ "       t.CDTIPO       = t.CDTIPO           AND\n"
						+ "       f.CDMAPA       = h.CDMAPA           AND\n"
						+ "       t.CDMAPA       = h.CDHOMOLOGADO;\n"
						+ "\n"
						+ "--HOMOLOGACIONES EXISTENTES DE GW A GENERAL (INSTANCIA PDNLNF)---------------------- 10380\n"
						+ "--El archivo  creado debe tener el nombre: core_general.csv\n"
						+ "\n"
						+ "SELECT \n"
						+ "       t.CDTIPO       AS homologation,\n"
						+ "       f.CDAPLICACION AS source,\n"
						+ "       t.CDAPLICACION AS target,\n"
						+ "       f.CDVALOR AS source_value, \n"
						+ "       t.CDVALOR AS target_value\n"
						+ "FROM   TTRD_MAPAS f, \n"
						+ "       TTRD_MAPAS t, \n"
						+ "       TTRD_HOMOLOGACION h\n"
						+ "WHERE  f.CDAPLICACION = 'CORE_GW'       AND --fuente\n"
						+ "       t.CDAPLICACION = 'GENERAL_GW'    AND --destino\n"
						+ "       t.CDTIPO IN (\n"
						+ "           'APIGATEWAY_PRODUCT_TYPE_CODE_FROM_OFFERING_TYPE_GW',\n"
						+ "           'COBERTURA_GW',\n"
						+ "           'COVERAGE_FROM_OFFERING_COVERAGE_ARTICLE_GW',\n"
						+ "           'GARANTIA_SUBGARANTIA_EMPRESARIAL_GW',\n"
						+ "           'LINE_FROM_OFFERING_TYPE_GW',\n"
						+ "           'LOSS_CAUSE_FROM_SERVICES_GW',\n"
						+ "           'LOSS_TYPE_FROM_LINE_SUBLINE_GW',\n"
						+ "           'OFFERING_TYPE_FROM_LINE_SUBLINE_GW',\n"
						+ "           'POLICY_TYPE_FROM_LINE_SUBLINE_GW',\n"
						+ "           'REINSURANCECONTRACT_FROM_OFFERING_COVERAGE_GW',\n"
						+ "           'TERMINO_GW')  AND --tipo de homologacion\n"
						+ "       t.CDTIPO       = t.CDTIPO           AND\n"
						+ "       f.CDMAPA       = h.CDMAPA           AND\n"
						+ "       t.CDMAPA       = h.CDHOMOLOGADO;\n"
						+ "\n"
						+ "--LISTADO DE TODAS LAS COBERTURAS EXISTENTES EN ECOSISTEMA (INSTANCIA PDN)---------------------- 11326\n"
						+ "--El archivo  creado debe tener el nombre: coberturas_ecosistema.csv\n"
						+ "SELECT \n"
						+ "       C.CDRAMO        AS RAMO,\n"
						+ "       C.CDSUBRAMO     AS SUBRAMO,\n"
						+ "       C.CDGARANTIA    AS GARANTIA,\n"
						+ "       C.CDSUBGARANTIA AS SUBGARANTIA,\n"
						+ "       DECODE(SUBSTR(C.CDRAMO_CONTABLE, 0, 1), '1', C.CDRAMO_CONTABLE, C.CDRAMO_HOST) AS RAMO_CONTABLE,\n"
						+ "       P.DSALIAS_1 AS PRODUCTO,\n"
						+ "       C.DSALIAS_2 AS COBERTURA\n"
						+ "FROM \n"
						+ "       DIC_ALIAS_COBERTURAS C, \n"
						+ "       OPS$CORP.DIC_ALIAS_RS P\n"
						+ "WHERE   \n"
						+ "       P.CDRAMO    = C.CDRAMO AND\n"
						+ "       P.CDSUBRAMO = C.CDSUBRAMO";
	}

}
