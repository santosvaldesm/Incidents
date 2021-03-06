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
@Table(name = "NOTE")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Note.findAll", query = "SELECT n FROM Note n")
  , @NamedQuery(name = "Note.findById", query = "SELECT n FROM Note n WHERE n.id = :id")
  , @NamedQuery(name = "Note.findByTypenote", query = "SELECT n FROM Note n WHERE n.typenote = :typenote")
  , @NamedQuery(name = "Note.findByDescription", query = "SELECT n FROM Note n WHERE n.description = :description")})
public class Note implements Serializable,PrintableTable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "TYPENOTE")
  private String typenote;
  @Column(name = "DESCRIPTION")
  private String description;

  public Note() {
  }

  public Note(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTypenote() {
    return typenote;
  }

  public void setTypenote(String typenote) {
    this.typenote = typenote;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
    if (!(object instanceof Note)) {
      return false;
    }
    Note other = (Note) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.mycompany.incidents.entities.Note[ id=" + id + " ]";
  } 
  
  @Override
  public String[] getData() {
    return new String[]{getId().toString(),getTypenote()};   
  }
    
  public static String[] columNames(){
    return new String[]{"ID","TIPO "};   
  }

  }
