/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santvamu
 */
@Entity
@Table(name = "INCIDENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Incident.findAll", query = "SELECT i FROM Incident i")
    , @NamedQuery(name = "Incident.findById", query = "SELECT i FROM Incident i WHERE i.id = :id")
    , @NamedQuery(name = "Incident.findByCode", query = "SELECT i FROM Incident i WHERE i.code = :code")
    , @NamedQuery(name = "Incident.findByStatus", query = "SELECT i FROM Incident i WHERE i.status = :status")
    , @NamedQuery(name = "Incident.findByAssigned", query = "SELECT i FROM Incident i WHERE i.assigned = :assigned")
    , @NamedQuery(name = "Incident.findByDescription", query = "SELECT i FROM Incident i WHERE i.description = :description")
    , @NamedQuery(name = "Incident.findByAffected", query = "SELECT i FROM Incident i WHERE i.affected = :affected")})
public class Incident implements Serializable,PrintableTable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "CODE", length = 8)
    private String code;
    @Column(name = "STATUS", length = 100)
    private String status;
    @Column(name = "ASSIGNED", length = 20)
    private String assigned;
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    @Column(name = "AFFECTED", length = 100)
    private String affected;
    @OneToMany(mappedBy = "incident")
    private List<Entry> entryList;

    public Incident() {
    }

    public Incident(Integer id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAffected() {
        return affected;
    }

    public void setAffected(String affected) {
        this.affected = affected;
    }

    @XmlTransient
    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
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
        if (!(object instanceof Incident)) {
            return false;
        }
        Incident other = (Incident) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.incidents.entities.Incident[ id=" + id + " ]";
    }
    
    @Override
    public String[] getData() {
      return new String[]{getId().toString(),getCode(),getStatus(),
                          getAssigned(),getDescription(),getAffected()};   
    }
    
    public static String[] columNames(){
        return new String[]{"ID",
                            "CODIGO   ",
                            "ESTADO                    ",
                            "RESPONSABLE",
                            "PROBLEMA                                                                ",
                            "USUARIO                   "};   
    }
    
}
