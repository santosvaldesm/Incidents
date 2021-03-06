/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.GwEcosystemCoverages;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.EcosystemSearchTypeEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.JTextField;

/**
 *
 * @author santvamu
 */
public class GwEcosystemCoveragesJpaController implements Serializable {

  public GwEcosystemCoveragesJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(GwEcosystemCoverages gwEcosystemCoverages) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(gwEcosystemCoverages);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(GwEcosystemCoverages gwEcosystemCoverages) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      gwEcosystemCoverages = em.merge(gwEcosystemCoverages);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = gwEcosystemCoverages.getId();
        if (findGwEcosystemCoverages(id) == null) {
          throw new NonexistentEntityException("The gwEcosystemCoverages with id " + id + " no longer exists.");
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
      GwEcosystemCoverages gwEcosystemCoverages;
      try {
        gwEcosystemCoverages = em.getReference(GwEcosystemCoverages.class, id);
        gwEcosystemCoverages.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The gwEcosystemCoverages with id " + id + " no longer exists.", enfe);
      }
      em.remove(gwEcosystemCoverages);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<GwEcosystemCoverages> findGwEcosystemCoveragesEntities() {
    return findGwEcosystemCoveragesEntities(true, -1, -1);
  }

  public List<GwEcosystemCoverages> findGwEcosystemCoveragesEntities(int maxResults, int firstResult) {
    return findGwEcosystemCoveragesEntities(false, maxResults, firstResult);
  }

  private List<GwEcosystemCoverages> findGwEcosystemCoveragesEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(GwEcosystemCoverages.class));
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

  public GwEcosystemCoverages findGwEcosystemCoverages(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(GwEcosystemCoverages.class, id);
    } finally {
      em.close();
    }
  }

  public int getGwEcosystemCoveragesCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<GwEcosystemCoverages> rt = cq.from(GwEcosystemCoverages.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
  public List<GwEcosystemCoverages> executeSearchEcosystemCoverages(String valeToSearch, 
          EcosystemSearchTypeEnum type, JTextField aTextField){
    String hql = "SELECT h FROM GwEcosystemCoverages h ";         
    if(valeToSearch.trim().length()==0){
       aTextField.setText("Se debe especificar el valor a buscar");
       return new ArrayList<>();
    }    
    switch(type){
      case Homologacion:        
            hql = hql + " WHERE lower(CONCAT(h.ramo,'-',h.subramo,'-',h.garantia,'-',h.subgarantia)) LIKE lower(CONCAT('%',:valeToSearch,'%'))";                
      break;
      case RamoContable:        
            hql = hql + " WHERE lower(h.ramoContable) LIKE lower(CONCAT('%',:valeToSearch,'%'))";                
      break;
      case Cobertura:
            hql = hql + " WHERE lower(h.coverageName) LIKE lower(CONCAT('%',:valeToSearch,'%'))";                          
      break;      
      case Producto:
            hql = hql + " WHERE lower(h.product) LIKE lower(CONCAT('%',:valeToSearch,'%'))";              
      break;      
    }
       
    EntityManager em = getEntityManager();
    Query aQuery = em.createQuery(hql, GwEcosystemCoverages.class);
    if(hql.contains("valeToSearch")){
        aQuery.setParameter("valeToSearch",valeToSearch);
    }
    List<GwEcosystemCoverages> result = aQuery.getResultList();
    aTextField.setText("Encontrados: " + result.size());
    return result;
  }  
}
