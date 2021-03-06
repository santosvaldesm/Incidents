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
@Table(name = "LISTA")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Lista.findAll", query = "SELECT l FROM Lista l")
  , @NamedQuery(name = "Lista.findById", query = "SELECT l FROM Lista l WHERE l.id = :id")
  , @NamedQuery(name = "Lista.findByNombre", query = "SELECT l FROM Lista l WHERE l.nombre = :nombre")
  , @NamedQuery(name = "Lista.findByValor", query = "SELECT l FROM Lista l WHERE l.valor = :valor")})
public class Lista implements Serializable,PrintableTable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "NOMBRE")
  private String nombre;
  @Column(name = "VALOR")
  private String valor;  

  public Lista() {
  }

  public Lista(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getValor() {
    return valor;
  }

  public void setValor(String valor) {
    this.valor = valor;
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
    if (!(object instanceof Lista)) {
      return false;
    }
    Lista other = (Lista) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.mycompany.incidents.entities.Lista[ id=" + id + " ]";
  }
  
  @Override
  public String[] getData() {
    return new String[]{getId().toString(),getNombre(),getValor()};   
  }
    
  public static String[] columNames(){
    return new String[]{"ID","NOMBRE","VALOR                                     "};   
  }
  
}
