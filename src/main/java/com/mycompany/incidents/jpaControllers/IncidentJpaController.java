/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.incidents.entities.Entry;
import com.mycompany.incidents.entities.Incident;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author santvamu
 */
public class IncidentJpaController implements Serializable {

  public IncidentJpaController(EntityManagerFactory emf) {
      this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
      return emf.createEntityManager();
  }

  public void create(Incident incident) {
    if (incident.getEntryList() == null) {
        incident.setEntryList(new ArrayList<Entry>());
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      List<Entry> attachedEntryList = new ArrayList<Entry>();
      for (Entry entryListEntryToAttach : incident.getEntryList()) {
        entryListEntryToAttach = em.getReference(entryListEntryToAttach.getClass(), entryListEntryToAttach.getId());
        attachedEntryList.add(entryListEntryToAttach);
      }
      incident.setEntryList(attachedEntryList);
      em.persist(incident);
      for (Entry entryListEntry : incident.getEntryList()) {
        Incident oldIncidentOfEntryListEntry = entryListEntry.getIncident();
        entryListEntry.setIncident(incident);
        entryListEntry = em.merge(entryListEntry);
        if (oldIncidentOfEntryListEntry != null) {
          oldIncidentOfEntryListEntry.getEntryList().remove(entryListEntry);
          oldIncidentOfEntryListEntry = em.merge(oldIncidentOfEntryListEntry);
        }
      }
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Incident incident) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      Incident persistentIncident = em.find(Incident.class, incident.getId());
      List<Entry> entryListOld = persistentIncident.getEntryList();
      List<Entry> entryListNew = incident.getEntryList();
      List<Entry> attachedEntryListNew = new ArrayList<Entry>();
      for (Entry entryListNewEntryToAttach : entryListNew) {
        entryListNewEntryToAttach = em.getReference(entryListNewEntryToAttach.getClass(), entryListNewEntryToAttach.getId());
        attachedEntryListNew.add(entryListNewEntryToAttach);
      }
      entryListNew = attachedEntryListNew;
      incident.setEntryList(entryListNew);
      incident = em.merge(incident);
      for (Entry entryListOldEntry : entryListOld) {
        if (!entryListNew.contains(entryListOldEntry)) {
          entryListOldEntry.setIncident(null);
          entryListOldEntry = em.merge(entryListOldEntry);
        }
      }
      for (Entry entryListNewEntry : entryListNew) {
        if (!entryListOld.contains(entryListNewEntry)) {
          Incident oldIncidentOfEntryListNewEntry = entryListNewEntry.getIncident();
          entryListNewEntry.setIncident(incident);
          entryListNewEntry = em.merge(entryListNewEntry);
          if (oldIncidentOfEntryListNewEntry != null && !oldIncidentOfEntryListNewEntry.equals(incident)) {
            oldIncidentOfEntryListNewEntry.getEntryList().remove(entryListNewEntry);
            oldIncidentOfEntryListNewEntry = em.merge(oldIncidentOfEntryListNewEntry);
          }
        }
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = incident.getId();
        if (findIncident(id) == null) {
          throw new NonexistentEntityException("The incident with id " + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(Integer id) throws NonexistentEntityException {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      Incident incident;
      try {
        incident = em.getReference(Incident.class, id);
        incident.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The incident with id " + id + " no longer exists.", enfe);
      }
      List<Entry> entryList = incident.getEntryList();
      for (Entry entryListEntry : entryList) {
        entryListEntry.setIncident(null);
        entryListEntry = em.merge(entryListEntry);
      }
      em.remove(incident);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<Incident> findIncidentEntities() {
    return findIncidentEntities(true, -1, -1);
  }

  public List<Incident> findIncidentEntities(int maxResults, int firstResult) {
    return findIncidentEntities(false, maxResults, firstResult);
  }

  private List<Incident> findIncidentEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Incident.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
      em.close();
    }
  }

  public Incident findIncident(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Incident.class, id);
    } finally {
      em.close();
    }
  }

  public int getIncidentCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Incident> rt = cq.from(Incident.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  public Incident findIncidentByIncidentCode(String parameter) {      
    EntityManager em = getEntityManager();        
    TypedQuery<Incident> consultaIncidentes = 
            em.createNamedQuery("Incident.findByCode", Incident.class);
    consultaIncidentes.setParameter("code",parameter);
    if(consultaIncidentes.getResultList().isEmpty()){
      return null;
    }
    try {
      return consultaIncidentes.getResultList().get(0);
    } finally {
      em.close();
    }      
  }

  public List<Incident> executeSearchIncident(String parameter,int findMode) {
    String hql = "SELECT i FROM Incident i ";                
    if(!parameter.trim().isEmpty()){
      if(findMode == 1){
        hql = hql + "WHERE lower(i.code) LIKE lower(CONCAT('%',:parameter,'%'))";
      }
      if(findMode == 2){
        hql = hql + "WHERE lower(i.status) LIKE lower(CONCAT('%',:parameter,'%'))";
      }
      if(findMode == 3){
        hql = hql + "WHERE lower(i.assigned) LIKE lower(CONCAT('%',:parameter,'%'))";
      }
      if(findMode == 4){
        hql = hql + "WHERE lower(i.description) LIKE lower(CONCAT('%',:parameter,'%'))";
      }
      if(findMode == 5){
        hql = hql + "WHERE lower(i.affected) LIKE lower(CONCAT('%',:parameter,'%'))";
      }
    }      
    EntityManager em = getEntityManager();
    Query aQuery = em.createQuery(hql, Incident.class);
    if(hql.contains("WHERE")){
      aQuery.setParameter("parameter",parameter);
    }        
    try {
      return aQuery.getResultList();
    } finally {
      em.close();
    }
  }

  public List<Incident> executeSearchIncidentByEntry(String parameter) {
    String hql = "SELECT DISTINCT i FROM Incident i, Entry e "
               + " WHERE e.incident = i AND "
               + " lower(e.entry) LIKE lower(CONCAT('%',:parameter,'%'))";
    EntityManager em = getEntityManager();
    Query aQuery = em.createQuery(hql, Incident.class);        
    aQuery.setParameter("parameter",parameter);
    
    try {
      return aQuery.getResultList();
    } finally {
      em.close();
    }
  }    
}
