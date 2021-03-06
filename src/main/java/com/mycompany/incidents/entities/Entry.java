/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santvamu
 */
@Entity
@Table(name = "ENTRY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entry.findAll", query = "SELECT e FROM Entry e")
    , @NamedQuery(name = "Entry.findById", query = "SELECT e FROM Entry e WHERE e.id = :id")
    , @NamedQuery(name = "Entry.findByEntry", query = "SELECT e FROM Entry e WHERE e.entry = :entry")
    , @NamedQuery(name = "Entry.findByEntrydate", query = "SELECT e FROM Entry e WHERE e.entrydate = :entrydate")})
public class Entry implements Serializable,PrintableTable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "ENTRY", length = 1500)
    private String entry;
    @Column(name = "ENTRYDATE")
    @Temporal(TemporalType.DATE)
    private Date entrydate;
    @JoinColumn(name = "INCIDENT", referencedColumnName = "ID")
    @ManyToOne
    private Incident incident;

    public Entry() {
    }

    public Entry(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Date getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(Date entrydate) {
        this.entrydate = entrydate;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
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
        if (!(object instanceof Entry)) {
            return false;
        }
        Entry other = (Entry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.incidents.entities.Entry[ id=" + id + " ]";
    }

    @Override
    public String[] getData() {
      return new String[]{getId().toString(),getEntrydate().toString(),getEntry()};   
    }
    
    public static String[] columNames(){
        return new String[]{"ID","FECHA     ","DESCRIPCION                    "};   
    }
    
    
    
}
