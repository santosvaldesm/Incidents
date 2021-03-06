package com.mycompany.incidents.entities;

import com.mycompany.incidents.otherResources.TypeKeysEnum;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "GW_LOB_MODEL")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "GwLobModel.findAll", query = "SELECT g FROM GwLobModel g")
  , @NamedQuery(name = "GwLobModel.findById", query = "SELECT g FROM GwLobModel g WHERE g.id = :id")        
  , @NamedQuery(name = "GwLobModel.findByOffering", query = "SELECT g FROM GwLobModel g WHERE g.offering LIKE :offering")        
  , @NamedQuery(name = "GwLobModel.findByLobCode", query = "SELECT g FROM GwLobModel g WHERE g.lobCode = :lobCode")
  , @NamedQuery(name = "GwLobModel.findByPolicyType", query = "SELECT g FROM GwLobModel g WHERE g.policyType = :policyType")
  , @NamedQuery(name = "GwLobModel.findByLossType", query = "SELECT g FROM GwLobModel g WHERE g.lossType = :lossType")
  , @NamedQuery(name = "GwLobModel.findByCoverageType", query = "SELECT g FROM GwLobModel g WHERE g.coverageType = :coverageType")
  , @NamedQuery(name = "GwLobModel.findByInternalPolicyType", query = "SELECT g FROM GwLobModel g WHERE g.internalPolicyType = :internalPolicyType")
  , @NamedQuery(name = "GwLobModel.findByPolicyTab", query = "SELECT g FROM GwLobModel g WHERE g.policyTab = :policyTab")
  , @NamedQuery(name = "GwLobModel.findByLossPartyType", query = "SELECT g FROM GwLobModel g WHERE g.lossPartyType = :lossPartyType")
  , @NamedQuery(name = "GwLobModel.findByCoverageSubtype", query = "SELECT g FROM GwLobModel g WHERE g.coverageSubtype = :coverageSubtype")
  , @NamedQuery(name = "GwLobModel.findByExposureType", query = "SELECT g FROM GwLobModel g WHERE g.exposureType = :exposureType")
  , @NamedQuery(name = "GwLobModel.findByCovtermPattern", query = "SELECT g FROM GwLobModel g WHERE g.covtermPattern = :covtermPattern")
  , @NamedQuery(name = "GwLobModel.findByCoverageSubtypeClass", query = "SELECT g FROM GwLobModel g WHERE g.coverageSubtypeClass = :coverageSubtypeClass")
  , @NamedQuery(name = "GwLobModel.findByLimitOrDeductible", query = "SELECT g FROM GwLobModel g WHERE g.limitOrDeductible = :limitOrDeductible")
  , @NamedQuery(name = "GwLobModel.findByLossCause", query = "SELECT g FROM GwLobModel g WHERE g.lossCause = :lossCause")
  , @NamedQuery(name = "GwLobModel.findByCostCategory", query = "SELECT g FROM GwLobModel g WHERE g.costCategory = :costCategory")})
public class GwLobModel implements Serializable,PrintableTable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "OFFERING")
  private String offering;
  @Column(name = "LOB_CODE")
  private String lobCode;
  @Column(name = "POLICY_TYPE")
  private String policyType;
  @Column(name = "LOSS_TYPE")
  private String lossType;
  @Column(name = "COVERAGE_TYPE")
  private String coverageType;
  @Column(name = "INTERNAL_POLICY_TYPE")
  private String internalPolicyType;
  @Column(name = "POLICY_TAB")
  private String policyTab;
  @Column(name = "LOSS_PARTY_TYPE")
  private String lossPartyType;
  @Column(name = "COVERAGE_SUBTYPE")
  private String coverageSubtype;
  @Column(name = "EXPOSURE_TYPE")
  private String exposureType;
  @Column(name = "COVTERM_PATTERN")
  private String covtermPattern;
  @Column(name = "COVERAGE_SUBTYPE_CLASS")
  private String coverageSubtypeClass;
  @Column(name = "LIMIT_OR_DEDUCTIBLE")
  private String limitOrDeductible;
  @Column(name = "LOSS_CAUSE")
  private String lossCause;
  @Column(name = "COST_CATEGORY")
  private String costCategory;

  public GwLobModel() {
  }

  public GwLobModel(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getOffering() {
    return offering;
  }

  public void setOffering(String offering) {
    this.offering = offering;
  }

  public String getLobCode() {
    return lobCode;
  }

  public void setLobCode(String lobCode) {
    this.lobCode = lobCode;
  }

  public String getPolicyType() {
    return policyType;
  }

  public void setPolicyType(String policyType) {
    this.policyType = policyType;
  }

  public String getLossType() {
    return lossType;
  }

  public void setLossType(String lossType) {
    this.lossType = lossType;
  }

  public String getCoverageType() {
    return coverageType;
  }

  public void setCoverageType(String coverageType) {
    this.coverageType = coverageType;
  }

  public String getInternalPolicyType() {
    return internalPolicyType;
  }

  public void setInternalPolicyType(String internalPolicyType) {
    this.internalPolicyType = internalPolicyType;
  }

  public String getPolicyTab() {
    return policyTab;
  }

  public void setPolicyTab(String policyTab) {
    this.policyTab = policyTab;
  }

  public String getLossPartyType() {
    return lossPartyType;
  }

  public void setLossPartyType(String lossPartyType) {
    this.lossPartyType = lossPartyType;
  }

  public String getCoverageSubtype() {
    return coverageSubtype;
  }

  public void setCoverageSubtype(String coverageSubtype) {
    this.coverageSubtype = coverageSubtype;
  }

  public String getExposureType() {
    return exposureType;
  }

  public void setExposureType(String exposureType) {
    this.exposureType = exposureType;
  }

  public String getCovtermPattern() {
    return covtermPattern;
  }

  public void setCovtermPattern(String covtermPattern) {
    this.covtermPattern = covtermPattern;
  }

  public String getCoverageSubtypeClass() {
    return coverageSubtypeClass;
  }

  public void setCoverageSubtypeClass(String coverageSubtypeClass) {
    this.coverageSubtypeClass = coverageSubtypeClass;
  }

  public String getLimitOrDeductible() {
    return limitOrDeductible;
  }

  public void setLimitOrDeductible(String limitOrDeductible) {
    this.limitOrDeductible = limitOrDeductible;
  }

  public String getLossCause() {
    return lossCause;
  }

  public void setLossCause(String lossCause) {
    this.lossCause = lossCause;
  }

  public String getCostCategory() {
    return costCategory;
  }

  public void setCostCategory(String costCategory) {
    this.costCategory = costCategory;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof GwLobModel)) {
      return false;
    }
    GwLobModel other = (GwLobModel) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.mycompany.incidents.entities.GwLobModel[ id=" + id + " ]";
  }
  
  @Override
  public String[] getData() {//todos los datos de la tabla
    return new String[]{getId().toString(),getOffering(),getLobCode()};
  }
    
  public static String[] columNames(){
      return new String[]{"ID",
                          "OFFERING   ",
                          "LOB_CODE                    "};   
  }
  /*
  public String[] getData(TypeKeysEnum category) {//solo las columnas requeridas por categoria
    
    switch(category){
      case CoverageType:
        return new String[]{getId().toString(),getCoverageType()};
      default:
        return new String[]{};        
    }
    
    
  }
    
  public static String[] columNames(TypeKeysEnum category){
    switch(category){
      case CoverageType:
        return new String[]{"ID","COBERTURA   "};   
      default:
        return new String[]{};          
    }
      
  }
 */

}
