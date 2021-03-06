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
@Table(name = "REINSURANCE")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Reinsurance.findAll", query = "SELECT r FROM Reinsurance r")
  , @NamedQuery(name = "Reinsurance.findById", query = "SELECT r FROM Reinsurance r WHERE r.id = :id")
  , @NamedQuery(name = "Reinsurance.findByCode", query = "SELECT r FROM Reinsurance r WHERE r.code = :code")
  , @NamedQuery(name = "Reinsurance.findByClaim", query = "SELECT r FROM Reinsurance r WHERE r.claim = :claim")
  , @NamedQuery(name = "Reinsurance.findByPolicy", query = "SELECT r FROM Reinsurance r WHERE r.policy = :policy")
  , @NamedQuery(name = "Reinsurance.findByClaimDate", query = "SELECT r FROM Reinsurance r WHERE r.claimDate = :claimDate")
  , @NamedQuery(name = "Reinsurance.findByRisk", query = "SELECT r FROM Reinsurance r WHERE r.risk = :risk")
  , @NamedQuery(name = "Reinsurance.findByCoverage", query = "SELECT r FROM Reinsurance r WHERE r.coverage = :coverage")
  , @NamedQuery(name = "Reinsurance.findBySubline", query = "SELECT r FROM Reinsurance r WHERE r.subline = :subline")
  , @NamedQuery(name = "Reinsurance.findByOffering", query = "SELECT r FROM Reinsurance r WHERE r.offering = :offering")
  , @NamedQuery(name = "Reinsurance.findByHomologation", query = "SELECT r FROM Reinsurance r WHERE r.homologation = :homologation")
  , @NamedQuery(name = "Reinsurance.findByError", query = "SELECT r FROM Reinsurance r WHERE r.error = :error")
  , @NamedQuery(name = "Reinsurance.findByPayload", query = "SELECT r FROM Reinsurance r WHERE r.payload = :payload")
  , @NamedQuery(name = "Reinsurance.findByTransfer", query = "SELECT r FROM Reinsurance r WHERE r.transfer = :transfer")})
public class Reinsurance implements Serializable,PrintableTable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "CLAIM")
  private String claim;
  @Column(name = "POLICY")
  private String policy;
  @Column(name = "CLAIM_DATE")
  private String claimDate;
  @Column(name = "RISK")
  private String risk;
  @Column(name = "COVERAGE")
  private String coverage;
  @Column(name = "SUBLINE")
  private String subline;
  @Column(name = "OFFERING")
  private String offering;
  @Column(name = "HOMOLOGATION")
  private String homologation;
  @Column(name = "ERROR")
  private String error;
  @Column(name = "PAYLOAD")
  private String payload;
  @Column(name = "TRANSFER")
  private String transfer;

  public Reinsurance() {
  }

  public Reinsurance(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getClaim() {
    return claim;
  }

  public void setClaim(String claim) {
    this.claim = claim;
  }

  public String getPolicy() {
    return policy;
  }

  public void setPolicy(String policy) {
    this.policy = policy;
  }

  public String getClaimDate() {
    return claimDate;
  }

  public void setClaimDate(String claimDate) {
    this.claimDate = claimDate;
  }

  public String getRisk() {
    return risk;
  }

  public void setRisk(String risk) {
    this.risk = risk;
  }

  public String getCoverage() {
    return coverage;
  }

  public void setCoverage(String coverage) {
    this.coverage = coverage;
  }

  public String getSubline() {
    return subline;
  }

  public void setSubline(String subline) {
    this.subline = subline;
  }

  public String getOffering() {
    return offering;
  }

  public void setOffering(String offering) {
    this.offering = offering;
  }

  public String getHomologation() {
    return homologation;
  }

  public void setHomologation(String homologation) {
    this.homologation = homologation;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getTransfer() {
    return transfer;
  }

  public void setTransfer(String transfer) {
    this.transfer = transfer;
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
    if (!(object instanceof Reinsurance)) {
      return false;
    }
    Reinsurance other = (Reinsurance) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.mycompany.incidents.entities.Reinsurance[ id=" + id + " ]";
  }
  
  @Override
  public String[] getData() {
    return new String[]{getId().toString(),getCode(),getClaim(),getPolicy()
    ,getClaimDate(),getRisk(),getCoverage(),getSubline(),getOffering(),getHomologation(),
    getError(),getPayload(),getTransfer()};      
  }
    
  public static String[] columNames(){
    return new String[]{"ID",
                        "CA          ",
                        "RECLAMO                    ",
                        "POLIZA                     ",
                        "FECHA          ",
                        "RIESGO      ",
                        "COBERTURA         ",
                        "SUBLINE",
                        "OFFERING",
                        "HOMOLOGACION              ",
                        "ERROR                                      ",
                        "PAYLOAD",
                        "TRANSF"};   
  }

  }
