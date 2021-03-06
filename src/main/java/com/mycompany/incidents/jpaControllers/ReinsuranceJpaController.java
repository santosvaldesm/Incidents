/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.Reinsurance;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author santvamu
 */
public class ReinsuranceJpaController implements Serializable {

  public ReinsuranceJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Reinsurance reinsurance) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(reinsurance);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Reinsurance reinsurance) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      reinsurance = em.merge(reinsurance);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = reinsurance.getId();
        if (findReinsurance(id) == null) {
          throw new NonexistentEntityException("The reinsurance with id " + id + " no longer exists.");
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
      Reinsurance reinsurance;
      try {
        reinsurance = em.getReference(Reinsurance.class, id);
        reinsurance.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The reinsurance with id " + id + " no longer exists.", enfe);
      }
      em.remove(reinsurance);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<Reinsurance> findReinsuranceEntities() {
    return findReinsuranceEntities(true, -1, -1);
  }

  public List<Reinsurance> findReinsuranceEntities(int maxResults, int firstResult) {
    return findReinsuranceEntities(false, maxResults, firstResult);
  }

  private List<Reinsurance> findReinsuranceEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Reinsurance.class));
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

  public Reinsurance findReinsurance(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Reinsurance.class, id);
    } finally {
      em.close();
    }
  }

  public int getReinsuranceCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Reinsurance> rt = cq.from(Reinsurance.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
}
