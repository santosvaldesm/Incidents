/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.Entry;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.incidents.entities.Incident;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author santvamu
 */
public class EntryJpaController implements Serializable {

    public EntryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entry entry) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Incident incident = entry.getIncident();
            if (incident != null) {
                incident = em.getReference(incident.getClass(), incident.getId());
                entry.setIncident(incident);
            }
            em.persist(entry);
            if (incident != null) {
                incident.getEntryList().add(entry);
                incident = em.merge(incident);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entry entry) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entry persistentEntry = em.find(Entry.class, entry.getId());
            Incident incidentOld = persistentEntry.getIncident();
            Incident incidentNew = entry.getIncident();
            if (incidentNew != null) {
                incidentNew = em.getReference(incidentNew.getClass(), incidentNew.getId());
                entry.setIncident(incidentNew);
            }
            entry = em.merge(entry);
            if (incidentOld != null && !incidentOld.equals(incidentNew)) {
                incidentOld.getEntryList().remove(entry);
                incidentOld = em.merge(incidentOld);
            }
            if (incidentNew != null && !incidentNew.equals(incidentOld)) {
                incidentNew.getEntryList().add(entry);
                incidentNew = em.merge(incidentNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entry.getId();
                if (findEntry(id) == null) {
                    throw new NonexistentEntityException("The entry with id " + id + " no longer exists.");
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
            Entry entry;
            try {
                entry = em.getReference(Entry.class, id);
                entry.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entry with id " + id + " no longer exists.", enfe);
            }
            Incident incident = entry.getIncident();
            if (incident != null) {
                incident.getEntryList().remove(entry);
                incident = em.merge(incident);
            }
            em.remove(entry);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entry> findEntryEntities() {
        return findEntryEntities(true, -1, -1);
    }

    public List<Entry> findEntryEntities(int maxResults, int firstResult) {
        return findEntryEntities(false, maxResults, firstResult);
    }

    private List<Entry> findEntryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entry.class));
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

    public Entry findEntry(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entry.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entry> rt = cq.from(Entry.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
