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
@Table(name = "GW_HOMOLOG_GENERAL_TO_CORE")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "GwHomologGeneralToCore.findAll", query = "SELECT g FROM GwHomologGeneralToCore g")
  , @NamedQuery(name = "GwHomologGeneralToCore.findById", query = "SELECT g FROM GwHomologGeneralToCore g WHERE g.id = :id")
  , @NamedQuery(name = "GwHomologGeneralToCore.findByHomologationName", query = "SELECT g FROM GwHomologGeneralToCore g WHERE g.homologationName = :homologationName")
  , @NamedQuery(name = "GwHomologGeneralToCore.findBySource", query = "SELECT g FROM GwHomologGeneralToCore g WHERE g.source = :source")
  , @NamedQuery(name = "GwHomologGeneralToCore.findByTarget", query = "SELECT g FROM GwHomologGeneralToCore g WHERE g.target = :target")})
public class GwHomologGeneralToCore implements Serializable,PrintableTable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "HOMOLOGATION_NAME")
  private String homologationName;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "TARGET")
  private String target;

  public GwHomologGeneralToCore() {
  }

  public GwHomologGeneralToCore(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getHomologationName() {
    return homologationName;
  }

  public void setHomologationName(String homologationName) {
    this.homologationName = homologationName;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
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
    if (!(object instanceof GwHomologGeneralToCore)) {
      return false;
    }
    GwHomologGeneralToCore other = (GwHomologGeneralToCore) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.mycompany.incidents.entities.GwHomologGeneralToCore[ id=" + id + " ]";
  }
  
  @Override
  public String[] getData() {//todos los datos de la tabla
    return new String[]{getId().toString(),getHomologationName(),getSource(),getTarget()};
  }
    
  public static String[] columNames(){
      return new String[]{"ID",
                          "TIPO HOMOLOGACION",
                          "VALOR ECOSISTEMA ",
                          "VALOR CORE "};   
  }
  
}
