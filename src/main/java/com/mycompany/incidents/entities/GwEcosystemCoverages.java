/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author santvamu
 */
@Entity
@Table(name = "GW_ECOSYSTEM_COVERAGES")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "GwEcosystemCoverages.findAll", query = "SELECT g FROM GwEcosystemCoverages g")
  , @NamedQuery(name = "GwEcosystemCoverages.findById", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.id = :id")
  , @NamedQuery(name = "GwEcosystemCoverages.findByRamo", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.ramo = :ramo")
  , @NamedQuery(name = "GwEcosystemCoverages.findBySubramo", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.subramo = :subramo")
  , @NamedQuery(name = "GwEcosystemCoverages.findByGarantia", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.garantia = :garantia")
  , @NamedQuery(name = "GwEcosystemCoverages.findBySubgarantia", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.subgarantia = :subgarantia")
  , @NamedQuery(name = "GwEcosystemCoverages.findByRamoContable", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.ramoContable = :ramoContable")
  , @NamedQuery(name = "GwEcosystemCoverages.findByProduct", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.product = :product")
  , @NamedQuery(name = "GwEcosystemCoverages.findByCoverageName", query = "SELECT g FROM GwEcosystemCoverages g WHERE g.coverageName = :coverageName")})
public class GwEcosystemCoverages implements Serializable,PrintableTable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "RAMO")
  private String ramo;
  @Column(name = "SUBRAMO")
  private String subramo;
  @Column(name = "GARANTIA")
  private String garantia;
  @Column(name = "SUBGARANTIA")
  private String subgarantia;
  @Column(name = "RAMO_CONTABLE")
  private String ramoContable;
  @Column(name = "PRODUCT")
  private String product;
  @Column(name = "COVERAGE_NAME")
  private String coverageName;

  public GwEcosystemCoverages() {
  }

  public GwEcosystemCoverages(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getRamo() {
    return ramo;
  }

  public void setRamo(String ramo) {
    this.ramo = ramo;
  }

  public String getSubramo() {
    return subramo;
  }

  public void setSubramo(String subramo) {
    this.subramo = subramo;
  }

  public String getGarantia() {
    return garantia;
  }

  public void setGarantia(String garantia) {
    this.garantia = garantia;
  }

  public String getSubgarantia() {
    return subgarantia;
  }

  public void setSubgarantia(String subgarantia) {
    this.subgarantia = subgarantia;
  }

  public String getRamoContable() {
    return ramoContable;
  }

  public void setRamoContable(String ramoContable) {
    this.ramoContable = ramoContable;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getCoverageName() {
    return coverageName;
  }

  public void setCoverageName(String coverageName) {
    this.coverageName = coverageName;
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
    if (!(object instanceof GwEcosystemCoverages)) {
      return false;
    }
    GwEcosystemCoverages other = (GwEcosystemCoverages) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.mycompany.incidents.entities.GwEcosystemCoverages[ id=" + id + " ]";
  }
  
  @Override
  public String[] getData() {//todos los datos de la tabla
    return new String[]{getId().toString(),getRamo(),getSubramo(),getGarantia(),getSubgarantia(),getRamoContable(),getProduct(),getCoverageName()};
  }
    
  public static String[] columNames(){
      return new String[]{"ID  ",
                          "RAMO         ",
                          "SUBRAMO      ",
                          "GARANTIA     ",
                          "SUBGARANTIA   ",
                          "RAMO CONTABLE ",
                          "PRODUCTO                          ",
                          "COBERTURA                         "};   
  }
  
}
