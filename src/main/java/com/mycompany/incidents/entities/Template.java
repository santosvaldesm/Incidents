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
@Table(name = "TEMPLATE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Template.findAll", query = "SELECT t FROM Template t")
    , @NamedQuery(name = "Template.findById", query = "SELECT t FROM Template t WHERE t.id = :id")
    , @NamedQuery(name = "Template.findByAgrupador", query = "SELECT t FROM Template t WHERE t.agrupador = :agrupador")
    , @NamedQuery(name = "Template.findByCausa", query = "SELECT t FROM Template t WHERE t.causa = :causa")
    , @NamedQuery(name = "Template.findByProceso", query = "SELECT t FROM Template t WHERE t.proceso = :proceso")
    , @NamedQuery(name = "Template.findByRaizal", query = "SELECT t FROM Template t WHERE t.raizal = :raizal")
    , @NamedQuery(name = "Template.findByEstadoRaizal", query = "SELECT t FROM Template t WHERE t.estadoRaizal = :estadoRaizal")
    , @NamedQuery(name = "Template.findByResponsable", query = "SELECT t FROM Template t WHERE t.responsable = :responsable")
    , @NamedQuery(name = "Template.findByDiagnostico", query = "SELECT t FROM Template t WHERE t.diagnostico = :diagnostico")
    , @NamedQuery(name = "Template.findByAccion", query = "SELECT t FROM Template t WHERE t.accion = :accion")
    , @NamedQuery(name = "Template.findByDescripcion", query = "SELECT t FROM Template t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "Template.findByOperatividad", query = "SELECT t FROM Template t WHERE t.operatividad = :operatividad")
    , @NamedQuery(name = "Template.findByCredenciales", query = "SELECT t FROM Template t WHERE t.credenciales = :credenciales")})
public class Template implements Serializable,PrintableTable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "AGRUPADOR")
    private String agrupador;
    @Column(name = "CAUSA")
    private String causa;
    @Column(name = "PROCESO")
    private String proceso;
    @Column(name = "RAIZAL")
    private String raizal;
    @Column(name = "ESTADO_RAIZAL")
    private String estadoRaizal;
    @Column(name = "RESPONSABLE")
    private String responsable;
    @Column(name = "DIAGNOSTICO")
    private String diagnostico;
    @Column(name = "ACCION")
    private String accion;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "OPERATIVIDAD")
    private String operatividad;
    @Column(name = "CREDENCIALES")
    private String credenciales;

    public Template() {
    }

    public Template(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgrupador() {
        return agrupador;
    }

    public void setAgrupador(String agrupador) {
        this.agrupador = agrupador;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getRaizal() {
        return raizal;
    }

    public void setRaizal(String raizal) {
        this.raizal = raizal;
    }

    public String getEstadoRaizal() {
        return estadoRaizal;
    }

    public void setEstadoRaizal(String estadoRaizal) {
        this.estadoRaizal = estadoRaizal;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getOperatividad() {
        return operatividad;
    }

    public void setOperatividad(String operatividad) {
        this.operatividad = operatividad;
    }

    public String getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(String credenciales) {
        this.credenciales = credenciales;
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
        if (!(object instanceof Template)) {
            return false;
        }
        Template other = (Template) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.incidents.entities.Template[ id=" + id + " ]";
    }
    
    /*
    currentTemplate.setAgrupador(comboAgrupador.getSelectedItem().toString());
        currentTemplate.setCausa(comboCausa.getSelectedItem().toString());
        currentTemplate.setProceso(comboProceso.getSelectedItem().toString());
        currentTemplate.setRaizal(txtRaizal.getText());
        currentTemplate.setEstadoRaizal(comboEstado.getSelectedItem().toString());
        currentTemplate.setResponsable(comboResponsable.getSelectedItem().toString());
        currentTemplate.setDiagnostico(txtDiagnostico.getText());
        currentTemplate.setAccion(txtAccion.getText());
        currentTemplate.setDescripcion(txtDescripcion.getText());
        currentTemplate.setOperatividad(comboOperatividad.getSelectedItem().toString());
        currentTemplate.setCredenciales(txtCredenciales.getText());
    */
    @Override
    public String[] getData() {
    return new String[]{getId().toString(),getAgrupador(),getCausa(),getProceso()
    ,getRaizal(),getEstadoRaizal(),getResponsable(),getDiagnostico(),getAccion(),getDescripcion(),
    getOperatividad(),getCredenciales()};      
  }
    
  public static String[] columNames(){
    return new String[]{"ID",
                        "AGRUPADOR             ",
                        "CAUSA                 ",
                        "PROCESO               ",
                        "RAIZAL                ",
                        "ESTADO                ",
                        "RESPONSABLE           ",
                        "DIAGNOSTICO                                                                                    ",
                        "ACCION                                                                           ",
                        "DESCIPCION                                                                       ",
                        "OPER",
                        "CRED"};   
  }
    
}
