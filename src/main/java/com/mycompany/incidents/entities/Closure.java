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
@Table(name = "CLOSURE")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Closure.findAll", query = "SELECT c FROM Closure c")
  , @NamedQuery(name = "Closure.findById", query = "SELECT c FROM Closure c WHERE c.id = :id")
  , @NamedQuery(name = "Closure.findByOrigen", query = "SELECT c FROM Closure c WHERE c.origen = :origen")
  , @NamedQuery(name = "Closure.findByTipo", query = "SELECT c FROM Closure c WHERE c.tipo = :tipo")
  , @NamedQuery(name = "Closure.findByClaimnumber", query = "SELECT c FROM Closure c WHERE c.claimnumber = :claimnumber")
  , @NamedQuery(name = "Closure.findByPolicynumber", query = "SELECT c FROM Closure c WHERE c.policynumber = :policynumber")
  , @NamedQuery(name = "Closure.findByRamo", query = "SELECT c FROM Closure c WHERE c.ramo = :ramo")
  , @NamedQuery(name = "Closure.findByReferencia", query = "SELECT c FROM Closure c WHERE c.referencia = :referencia")
  , @NamedQuery(name = "Closure.findByReferenciaOrigenTipo", query = "SELECT c FROM Closure c WHERE c.referencia = :referencia AND c.tipo = :tipo AND c.origen = :origen")
  , @NamedQuery(name = "Closure.findByValorCienGw", query = "SELECT c FROM Closure c WHERE c.valorCienGw = :valorCienGw")
  , @NamedQuery(name = "Closure.findByValorReasGw", query = "SELECT c FROM Closure c WHERE c.valorReasGw = :valorReasGw")
  , @NamedQuery(name = "Closure.findByValorCienSap", query = "SELECT c FROM Closure c WHERE c.valorCienSap = :valorCienSap")
  , @NamedQuery(name = "Closure.findByValorReasSap", query = "SELECT c FROM Closure c WHERE c.valorReasSap = :valorReasSap")
  , @NamedQuery(name = "Closure.findByMoneda", query = "SELECT c FROM Closure c WHERE c.moneda = :moneda")
  , @NamedQuery(name = "Closure.findByEstado", query = "SELECT c FROM Closure c WHERE c.estado = :estado")
  , @NamedQuery(name = "Closure.findByDiferCien", query = "SELECT c FROM Closure c WHERE c.diferCien = :diferCien")
  , @NamedQuery(name = "Closure.findByDiferReas", query = "SELECT c FROM Closure c WHERE c.diferReas = :diferReas")})
public class Closure implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "ORIGEN")
  private String origen;
  @Column(name = "TIPO")
  private String tipo;
  @Column(name = "CLAIMNUMBER")
  private String claimnumber;
  @Column(name = "POLICYNUMBER")
  private String policynumber;
  @Column(name = "RAMO")
  private String ramo;
  @Column(name = "REFERENCIA")
  private String referencia;
  @Column(name = "ROW_TXT")
  private String rowTxt;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "VALOR_CIEN_GW")
  private Double valorCienGw;
  @Column(name = "VALOR_REAS_GW")
  private Double valorReasGw;
  @Column(name = "VALOR_CIEN_SAP")
  private Double valorCienSap;
  @Column(name = "VALOR_REAS_SAP")
  private Double valorReasSap;
  @Column(name = "MONEDA")
  private String moneda;
  @Column(name = "ESTADO")
  private String estado;
  @Column(name = "DIFER_CIEN")
  private Double diferCien;
  @Column(name = "DIFER_REAS")
  private Double diferReas;

  public Closure() {
  }

  public Closure(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getOrigen() {
    return origen;
  }

  public void setOrigen(String origen) {
    this.origen = origen;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getClaimnumber() {
    return claimnumber;
  }

  public void setClaimnumber(String claimnumber) {
    this.claimnumber = claimnumber;
  }

  public String getPolicynumber() {
    return policynumber;
  }

  public void setPolicynumber(String policynumber) {
    this.policynumber = policynumber;
  }

  public String getRamo() {
    return ramo;
  }

  public void setRamo(String ramo) {
    this.ramo = ramo;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }
  
  public String getRowTxt() {
    return rowTxt;
  }

  public void setRowTxt(String rowTxt) {
    this.rowTxt = rowTxt;
  }

  public Double getValorCienGw() {
    return valorCienGw;
  }

  public void setValorCienGw(Double valorCienGw) {
    this.valorCienGw = valorCienGw;
  }

  public Double getValorReasGw() {
    return valorReasGw;
  }

  public void setValorReasGw(Double valorReasGw) {
    this.valorReasGw = valorReasGw;
  }

  public Double getValorCienSap() {
    return valorCienSap;
  }

  public void setValorCienSap(Double valorCienSap) {
    this.valorCienSap = valorCienSap;
  }

  public Double getValorReasSap() {
    return valorReasSap;
  }

  public void setValorReasSap(Double valorReasSap) {
    this.valorReasSap = valorReasSap;
  }

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public Double getDiferCien() {
    return diferCien;
  }

  public void setDiferCien(Double diferCien) {
    this.diferCien = diferCien;
  }

  public Double getDiferReas() {
    return diferReas;
  }

  public void setDiferReas(Double diferReas) {
    this.diferReas = diferReas;
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
    if (!(object instanceof Closure)) {
      return false;
    }
    Closure other = (Closure) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.mycompany.incidents.entities.Closure[ id=" + id + " ]";
  }
  
}
