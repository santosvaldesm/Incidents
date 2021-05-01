package com.mycompany.incidents.entities;

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
@Table(name = "GW_TYPE_CODE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GwTypeCode.findAll", query = "SELECT g FROM GwTypeCode g")
    , @NamedQuery(name = "GwTypeCode.findById", query = "SELECT g FROM GwTypeCode g WHERE g.id = :id")
    , @NamedQuery(name = "GwTypeCode.findByTypeKeyName", query = "SELECT g FROM GwTypeCode g WHERE g.typeKeyName = :typeKeyName")
    , @NamedQuery(name = "GwTypeCode.findTypeCodeByCategoryAndCode", query = "SELECT g FROM GwTypeCode g WHERE g.typeKeyName = :typeKeyName AND g.typeCode = :typeCode")
    , @NamedQuery(name = "GwTypeCode.findByTypeCode", query = "SELECT g FROM GwTypeCode g WHERE g.typeCode = :typeCode")
    , @NamedQuery(name = "GwTypeCode.findByNameEs", query = "SELECT g FROM GwTypeCode g WHERE g.nameEs = :nameEs")
    , @NamedQuery(name = "GwTypeCode.findByNameUs", query = "SELECT g FROM GwTypeCode g WHERE g.nameUs = :nameUs")
    , @NamedQuery(name = "GwTypeCode.findByRetired", query = "SELECT g FROM GwTypeCode g WHERE g.retired = :retired")
    , @NamedQuery(name = "GwTypeCode.findByOtherCategory", query = "SELECT g FROM GwTypeCode g WHERE g.otherCategory = :otherCategory")
    , @NamedQuery(name = "GwTypeCode.findByIsNew", query = "SELECT g FROM GwTypeCode g WHERE g.isNew = :isNew")})
public class GwTypeCode implements Serializable, PrintableTable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TYPE_KEY_NAME")
    private String typeKeyName;
    @Column(name = "TYPE_CODE")
    private String typeCode;
    @Column(name = "NAME_ES")
    private String nameEs;
    @Column(name = "NAME_US")
    private String nameUs;
    @Column(name = "RETIRED")
    private Boolean retired;
    @Column(name = "OTHER_CATEGORY")
    private String otherCategory;
    @Column(name = "IS_NEW")
    private Boolean isNew;

    public GwTypeCode() {
    }

    public GwTypeCode(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeKeyName() {
        return typeKeyName;
    }

    public void setTypeKeyName(String typeKeyName) {
        this.typeKeyName = typeKeyName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getNameEs() {
        return nameEs;
    }

    public void setNameEs(String nameEs) {
        this.nameEs = nameEs;
    }

    public String getNameUs() {
        return nameUs;
    }

    public void setNameUs(String nameUs) {
        this.nameUs = nameUs;
    }

    public Boolean getRetired() {
        return retired;
    }

    public void setRetired(Boolean retired) {
        this.retired = retired;
    }

    public String getOtherCategory() {
        return otherCategory;
    }

    public void setOtherCategory(String otherCategory) {
        this.otherCategory = otherCategory;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
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
        if (!(object instanceof GwTypeCode)) {
            return false;
        }
        GwTypeCode other = (GwTypeCode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.incidents.entities.GwTypeCode[ id=" + id + " ]";
    }

    @Override
    public String[] getData() {
        switch(typeKeyName){
            case "CovTermPattern":
            case "LossCause":
                return new String[]{getTypeCode(), getNameEs(), getOtherCategory()};                
            default:
                return new String[]{getTypeCode(), getNameEs()};                    
        }        
    }

    public static String[] columNames() {        
        return new String[]{"CODIGO    ", "NOMBRE         "};        
    }
    
    public static String[] columNamesWhitType() {        
        return new String[]{"CODIGO    ", "NOMBRE         ", "TIPO     "};        
    }

}
