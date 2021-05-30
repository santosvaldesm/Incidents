/*--------------------------------------------------------------------------------
 1. Ejecutar este codigo en Scratchpad,
 2. genera una carpeta en D: con el modelo de productos de claims 
 3. esta carpeta se selecciona al usar 'Cargar Modelo' 
 4. Si algun valor buscado no es encintrado indica que:
    a. Hay que actulizar mediante el boton 'Cargar Modelo'    b. El valor buscado esta retirado(se debe verificarlo en los XML de GW) --------------------------------------------------------------------------------
*/

uses java.io.File
uses java.io.FileOutputStream
uses java.io.PrintWriter
uses java.util.HashMap
uses java.text.SimpleDateFormat
uses java.util.Date
uses java.util.ArrayList
uses gw.api.util.LocaleUtil
uses gw.i18n.ILocale
uses gw.entity.TypeKey
uses gw.entity.ITypeList
uses gw.lang.reflect.TypeSystem

var _esCO : ILocale = LocaleUtil.toLanguage(typekey.LanguageType.TC_ES_CO)
var _enUS : ILocale = LocaleUtil.toLanguage(typekey.LanguageType.TC_EN_US)

var _dateFormat = new SimpleDateFormat("HH:mm:ss")
var _startDate = new Date()
var _pathOut = "D:/" + _startDate + "_outLobModels"
var _writeLobModel : PrintWriter = null
var _writeLob : PrintWriter = null
var _offering = ""
var _lobCode = ""
var _policyType = ""
var _lossType = ""
var _coverageType = ""
var _internalPolicyType = ""
var _policyTab = ""
var _lossPartyType = ""
var _coverageSubType = ""
var _exposureType = ""
var _covTermPattern = ""
var _coverageSubtypeClass = ""
var _limitDeducible = ""
var _lossCauses = ""
var _costCategories = ""
var _mapCovTermsByCoverage : HashMap<String, List<typekey.CovTermPattern>> = null
var _mapCostCategoryByCovTerm : HashMap<String, String> = null
var _mapLossPartyByCoverageSubType : HashMap<String, String> = null
var _mapLossCauseByCoverageType : HashMap<String, String> = null
var _mapOfferingByCov : HashMap<String, String> = null
var _mapLossTypeByLobCode : HashMap<String, String> = null
var _mapInternalPolicyByPolicyType : HashMap<String, String> = null
var _mapPolicyTabByPolicyType : HashMap<String, String> = null

startProcess()

/**/
function startProcess() {
  createDirectories()
  print(timeEvent("Directorios cargados... "))

  generateTypeListByName("OfferingType_Ext")
  generateTypeListByName("LOBCode")
  generateTypeListByName("PolicyType")
  generateTypeListByName("LossType")
  generateTypeListByName("CoverageType")
  generateTypeListByName("InternalPolicyType")
  generateTypeListByName("PolicyTab")
  generateTypeListByName("LossPartyType")
  generateTypeListByName("CoverageSubtype")
  generateTypeListByName("ExposureType")
  generateTypeListByName("CovTermPattern")
  generateTypeListByName("LossCause")
  generateTypeListByName("CostType")
  generateTypeListByNameAndCategory("CostCategory",typekey.CostType)
  print(timeEvent("TypeList cargados... "))

  _mapCovTermsByCoverage         = loadMapCovTermsByCoverage()
  _mapCostCategoryByCovTerm      = loadMapTypeListWithConcatenatedCategory("CostCategory",typekey.CovTermPattern)
  _mapLossPartyByCoverageSubType = loadMapTypeListWithConcatenatedCategory("LossPartyType",typekey.CoverageSubtype)
  _mapLossCauseByCoverageType    = loadMapTypeListWithConcatenatedCategory("LossCause",typekey.CoverageType)
  _mapOfferingByCov              = loadMapTypeListWithConcatenatedCategory(typekey.OfferingType_Ext,"CoverageType")
  _mapLossTypeByLobCode          = loadMapTypeListWithConcatenatedCategory(typekey.LossType,"LOBCode")
  _mapInternalPolicyByPolicyType = loadMapTypeListWithConcatenatedCategory(typekey.InternalPolicyType,"PolicyType")
  _mapPolicyTabByPolicyType      = loadMapTypeListWithConcatenatedCategory(typekey.PolicyTab,"PolicyType")
  print(timeEvent("Mapas cargados... "))

  generateLobModel()
  print(timeEvent("FIN, LobModel cargado "))
}


/**/
function generateLobModel() {
  var header = ("Offering\t" + "LOBCode\t" + "PolicyType\t" + "LossType\t" + "CoverageType\t" + "InternalPolicyType\t" + "PolicyTab\t" + "LossPartyType\t" + "CoverageSubtype\t" + "ExposureType\t" + "CovTermPattern\t" + "CoverageSubtypeClass\t" + "Limite o Deducible\t" + "Causas por cobertura\t" + "Categorias de costo")
  var fileLobModel = new File(_pathOut + "/LobModel.txt")
  fileLobModel.write(header)
  _writeLobModel = new PrintWriter(new FileOutputStream(fileLobModel, true))
  _writeLobModel.write("\n")  
  typekey.LOBCode.getTypeKeys(false).each(\lob -> {
    lob.Categories.whereTypeIs(PolicyType).each(\ polType -> {
      polType.Categories.whereTypeIs(CoverageType).where(\ cov -> !cov.Retired).each(\ cov -> {
        cov.Categories.whereTypeIs(CoverageSubtype).where(\subcov -> !subcov.Retired).each(\ subcov -> {
          var exp = subcov.Categories.whereTypeIs(typekey.ExposureType).first()
          _coverageSubtypeClass = determineCoverageSubtypeClass(subcov)
          _lobCode = lob.Code
          _policyType = polType.Code
          _coverageType = cov.Code
          _coverageSubType = subcov.Code
          _exposureType = exp.Code
          _offering = _mapOfferingByCov.get(cov.Code)
          _lossType = _mapLossTypeByLobCode.get(lob.Code)
          _internalPolicyType = _mapInternalPolicyByPolicyType.get(polType.Code)
          _policyTab = _mapPolicyTabByPolicyType.get(polType.Code)
          _lossPartyType = _mapLossPartyByCoverageSubType.get(subcov.Code)
          _lossCauses = _mapLossCauseByCoverageType.get(cov.Code) ?: ""
          var covTermByCov = _mapCovTermsByCoverage.get(cov.Code)
          var CovTermPatternList = covTermByCov?.where(\covT ->
                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).contains(subcov)) or
                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).IsEmpty and covT.Categories.whereTypeIs(typekey.ExposureType).contains(exp)) or
                  (covT.Categories.whereTypeIs(typekey.CoverageSubtype).IsEmpty and covT.Categories.whereTypeIs(typekey.ExposureType).IsEmpty))
          if(CovTermPatternList.HasElements) {
            CovTermPatternList.each(\covTerm -> {
              _limitDeducible = getLimOrDed(covTerm)
              _covTermPattern = covTerm.Code
              _costCategories = _mapCostCategoryByCovTerm.get(covTerm.Code) ?: ""
              write()
            })
          }else {
            _covTermPattern = ""
            _limitDeducible = ""
            _costCategories = ""
            write()
          }
        })
      })
    })
  })
  _writeLobModel.flush()
  _writeLobModel.close()
}

/**/
function createDirectories() {
  if (!getFile(_pathOut).exists()) {
    getFile(_pathOut).mkdirs()
  }
}

/**/
function getFile(path : String) : File {
  return new File(path)
}
/**/
function timeEvent(valueText : String) : String {
  var currentDate = new Date()
  return valueText + _dateFormat.format(currentDate) + " Duracion " + timeDiff(_startDate,new Date()) + " segundos"
}

/**/
function timeDiff(fechaInicio : Date, fechaTermino : Date) : float {
  return ((fechaTermino.getTime() / 1000) - (fechaInicio.getTime() / 1000))
}

/**/
function getLimOrDed(term : CovTermPattern) : String {
  for (filter in typekey.CovTermPattern.Type.TypeFilters) {
    if (filter.Includes.contains(term)) {
      return filter.Name.replace("_Ext", "")
    }
  }
  return ""
}

/**/
function determineCoverageSubtypeClass(subcov : typekey.CoverageSubtype) : String {
  if (subcov.Code.contains("Art")) {
    return "Articulo"
  }
  if (subcov.Code.contains("Bkt") or subcov.Code.contains("Blanket") or subcov.Code.contains("BKT")) {
    return "Blanket"
  }
  if (subcov.Code.contains("Ubi")) {
    return "Ubicacion"
  }
  return ""
}

/**/
function write() {
  var body = (_offering + "\t" + _lobCode + "\t" + _policyType + "\t" + _lossType + "\t" + _coverageType + "\t" +
      _internalPolicyType + "\t" + _policyTab + "\t" + _lossPartyType + "\t" + _coverageSubType + "\t" + _exposureType + "\t" +
      _covTermPattern + "\t" + _coverageSubtypeClass + "\t" + _limitDeducible + "\t" + _lossCauses + "\t" + _costCategories)
  _writeLobModel.write(body)
  _writeLobModel.write("\n")
}

/**/
function generateTypeListByNameAndCategory(typeListName : String, aTypeTarget : Type<TypeKey>) {
  var aTypeList = TypeSystem.getByFullName("typekey." + typeListName) as ITypeList
  var fileTypeList = new File(_pathOut + "/" + typeListName + ".txt")
  fileTypeList.write("Code\tNameES\tNameUS\t" + typeListName + "\n")
  var writerTypeList = new PrintWriter(new FileOutputStream(fileTypeList, true))
  aTypeList.getTypeKeys(false).each(\ aTypeKey -> {
    writerTypeList.write(aTypeKey.Code + "\t" + aTypeKey.getDisplayName(_esCO) + "\t" + aTypeKey.getDisplayName(_enUS) + "\t" +
        aTypeKey.Categories.whereTypeIs(aTypeTarget)*.Code.join(";") + "\n")
  })
  writerTypeList.flush()
  writerTypeList.close()
}

/**/
function generateTypeListByName(typeListName : String) {
  var aTypeList = TypeSystem.getByFullName("typekey." + typeListName) as ITypeList
  var fileTypeList = new File(_pathOut + "/" + typeListName + ".txt")
  fileTypeList.write("Code\tNameES\tNameUS\n")
  var writerTypeList = new PrintWriter(new FileOutputStream(fileTypeList, true))
  aTypeList.getTypeKeys(false).each(\ aTypeKey -> {
    writerTypeList.write(aTypeKey.Code + "\t" + aTypeKey.getDisplayName(_esCO) + "\t" + aTypeKey.getDisplayName(_enUS) + "\n")
  })
  writerTypeList.flush()
  writerTypeList.close()
}

/**/
function loadMapCovTermsByCoverage() : HashMap<String, List<typekey.CovTermPattern>> {
  var mapCovTermsByCoverage = new HashMap<String, List<typekey.CovTermPattern>>()
  for (aCovTerm in typekey.CovTermPattern.getTypeKeys(false)) {
    var coverageList =  aCovTerm.Categories.whereTypeIs(typekey.CoverageType)
    for (aCoverage in coverageList) {
      var aResultSearch = mapCovTermsByCoverage.get(aCoverage.Code)
      if(aResultSearch == null) {
        aResultSearch = new ArrayList<typekey.CovTermPattern>()
      }
      aResultSearch.add(aCovTerm)
      mapCovTermsByCoverage.put(aCoverage.Code, aResultSearch)
    }
  }
  return mapCovTermsByCoverage
}

/**
* se usa cuando en la categoria esta la clave del mapa y en los typecodes(del primer parametro) estan los valores a concatenar
*/
function loadMapTypeListWithConcatenatedCategory(typeKeyName : String, categoryFilter : Type<TypeKey>) : HashMap<String, String> {
  var mapReturn = new HashMap<String, String>()
  var aTypeKey = TypeSystem.getByFullName("typekey." + typeKeyName) as ITypeList
  for (aTypeCode in aTypeKey.getTypeKeys(false)) {
    var categories =  aTypeCode.Categories.whereTypeIs(categoryFilter)
    for (aCategory in categories) {
      var mapResultSearch = mapReturn.get(aCategory.Code)
      mapReturn.put(aCategory.Code, mapResultSearch == null ? aTypeCode.Code : (mapResultSearch + ";" + aTypeCode.Code))
    }
  }
  return mapReturn
}


/**
* se usa cuando en el typecode(del segundo parametro) esta la clave del mapa y en las categorias estan los valores a concatenar
*/
function loadMapTypeListWithConcatenatedCategory(categoryFilter : Type<TypeKey>, typeKeyName : String) : HashMap<String, String> {
  var mapReturn = new HashMap<String, String>()
  var aTypeKey = TypeSystem.getByFullName("typekey." + typeKeyName) as ITypeList
  for (aTypeCode in aTypeKey.getTypeKeys(false)) {
    var categoriesConcatenate =  aTypeCode.Categories.whereTypeIs(categoryFilter).where(\aCate -> not aCate.Retired)*.Code.join(";")
    mapReturn.put(aTypeCode.Code, categoriesConcatenate)
  }
  return mapReturn
}