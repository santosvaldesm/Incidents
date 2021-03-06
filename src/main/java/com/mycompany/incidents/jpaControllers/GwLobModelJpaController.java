/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.GwLobModel;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.TypeKeysEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author santvamu
 */
public class GwLobModelJpaController implements Serializable {

  public GwLobModelJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(GwLobModel gwLobModel) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(gwLobModel);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(GwLobModel gwLobModel) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      gwLobModel = em.merge(gwLobModel);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = gwLobModel.getId();
        if (findGwLobModel(id) == null) {
          throw new NonexistentEntityException("The gwLobModel with id " + id + " no longer exists.");
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
      GwLobModel gwLobModel;
      try {
        gwLobModel = em.getReference(GwLobModel.class, id);
        gwLobModel.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The gwLobModel with id " + id + " no longer exists.", enfe);
      }
      em.remove(gwLobModel);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<GwLobModel> findGwLobModelEntities() {
    return findGwLobModelEntities(true, -1, -1);
  }

  public List<GwLobModel> findGwLobModelEntities(int maxResults, int firstResult) {
    return findGwLobModelEntities(false, maxResults, firstResult);
  }

  private List<GwLobModel> findGwLobModelEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(GwLobModel.class));
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

  public GwLobModel findGwLobModel(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(GwLobModel.class, id);
    } finally {
      em.close();
    }
  }

  public int getGwLobModelCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<GwLobModel> rt = cq.from(GwLobModel.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
  private String createWhereBySearchCriteria(HashMap<TypeKeysEnum,String> searchCriteria){
    String hql = " WHERE ";
    for (Iterator<Map.Entry<TypeKeysEnum, String>> it = searchCriteria.entrySet().iterator(); it.hasNext();) {
      HashMap.Entry<TypeKeysEnum,String> aCriteria = it.next();
      switch (aCriteria.getKey()) {
        case CoverageType:       hql = hql + "g.coverageType = :"                     + aCriteria.getKey().name();         break;
        case CostCategory:       hql = hql + "g.costCategory LIKE CONCAT('%',:"       + aCriteria.getKey().name()+",'%')"; break;
        case CoverageSubtype:    hql = hql + "g.coverageSubtype = :"                  + aCriteria.getKey().name();         break;
        case CovTermPattern:     hql = hql + "g.covtermPattern = :"                   + aCriteria.getKey().name();         break;
        case ExposureType:       hql = hql + "g.exposureType = :"                     + aCriteria.getKey().name();         break;
        case InternalPolicyType: hql = hql + "g.internalPolicyType LIKE CONCAT('%',:" + aCriteria.getKey().name()+",'%')"; break;
        case LOBCode:            hql = hql + "g.lobCode = :"                          + aCriteria.getKey().name();         break;
        case LossCause:          hql = hql + "g.lossCause LIKE CONCAT('%',:"          + aCriteria.getKey().name()+",'%')"; break;
        case LossPartyType:      hql = hql + "g.lossPartyType LIKE CONCAT('%',:"      + aCriteria.getKey().name()+",'%')"; break;
        case LossType:           hql = hql + "g.lossType = :"                         + aCriteria.getKey().name();         break;
        case OfferingType_Ext:   hql = hql + "g.offering LIKE CONCAT('%',:"           + aCriteria.getKey().name()+",'%')"; break;
        case PolicyTab:          hql = hql + "g.policyTab LIKE CONCAT('%',:"          + aCriteria.getKey().name()+",'%')"; break;
        case PolicyType:         hql = hql + "g.policyType LIKE CONCAT('%',:"         + aCriteria.getKey().name()+",'%')"; break;
      }
      if(it.hasNext()){
        hql = hql + " AND ";
      }
    }
    return hql;
  } 
  
  private void setParametersInQuery(Query lobModelQuery,HashMap<TypeKeysEnum,String> searchCriteria){
    for (Iterator<Map.Entry<TypeKeysEnum, String>> it = searchCriteria.entrySet().iterator(); it.hasNext();) {
      HashMap.Entry<TypeKeysEnum,String> aCriteria = it.next();
      lobModelQuery.setParameter(aCriteria.getKey().name(),aCriteria.getValue());      
    }
  }
  
  public List<GwLobModel> findBySearchCriteria(HashMap<TypeKeysEnum,String> searchCriteria){
    EntityManager em = getEntityManager();        
    String hql = "SELECT g FROM GwLobModel g";    
    hql = hql + createWhereBySearchCriteria(searchCriteria);        
    Query lobModelQuery = em.createQuery(hql, GwLobModel.class);    
    setParametersInQuery(lobModelQuery,searchCriteria);       
    try {
      if(lobModelQuery.getResultList().isEmpty()){
        return new ArrayList<>();
      }
      return lobModelQuery.getResultList();
    } finally {
      em.close();
    }      
  } 
}
