/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.Closure;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.ClosureTypeEnum;
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
public class ClosureJpaController implements Serializable {

  public ClosureJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Closure closure) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(closure);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Closure closure) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      closure = em.merge(closure);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = closure.getId();
        if (findClosure(id) == null) {
          throw new NonexistentEntityException("The closure with id " + id + " no longer exists.");
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
      Closure closure;
      try {
        closure = em.getReference(Closure.class, id);
        closure.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The closure with id " + id + " no longer exists.", enfe);
      }
      em.remove(closure);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<Closure> findClosureEntities() {
    return findClosureEntities(true, -1, -1);
  }

  public List<Closure> findClosureEntities(int maxResults, int firstResult) {
    return findClosureEntities(false, maxResults, firstResult);
  }

  private List<Closure> findClosureEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Closure.class));
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

  public Closure findClosure(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Closure.class, id);
    } finally {
      em.close();
    }
  }

  public int getClosureCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Closure> rt = cq.from(Closure.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
  public Closure findClosureByReferenciaOrigenTipo(String origen,String tipo,String referencia){
    EntityManager em = getEntityManager();        
    TypedQuery<Closure> consultaClosure = em.createNamedQuery("Closure.findByReferenciaOrigenTipo", Closure.class);
    consultaClosure.setParameter("origen",origen);
    consultaClosure.setParameter("tipo",tipo);
    consultaClosure.setParameter("referencia",referencia);
    if(consultaClosure.getResultList().isEmpty()){
      return null;
    }
    try {
      return consultaClosure.getResultList().get(0);
    } finally {
      em.close();
    }    
  }
  
  public List<Closure> findBySearchCriteria(HashMap<ClosureTypeEnum,String> searchCriteria) {
    EntityManager em = getEntityManager();        
    String hql = "SELECT c FROM Closure c";    
    hql = hql + createWhereBySearchCriteria(searchCriteria);        
    Query closureQuery = em.createQuery(hql, Closure.class);    
    setParametersInQuery(closureQuery,searchCriteria);       
    try {
      if(closureQuery.getResultList().isEmpty()){
        return new ArrayList<>();
      }
      return closureQuery.getResultList();
    } finally {
      em.close();
    }
  }
  
  private void setParametersInQuery(Query closureQuery,HashMap<ClosureTypeEnum,String> searchCriteria){
    for (Iterator<Map.Entry<ClosureTypeEnum, String>> it = searchCriteria.entrySet().iterator(); it.hasNext();) {
      HashMap.Entry<ClosureTypeEnum,String> aCriteria = it.next();
      switch(aCriteria.getKey()){
        case origen:
        case referencia:
        case tipo:
        case moneda:        
          closureQuery.setParameter(aCriteria.getKey().name(),aCriteria.getValue());      
          break;        
        case diferenciaCienMenorQue:
        case diferenciaReaseguroMenorQue:
        case AbsMenorQue:
        case TrmMenorQue:
          double aValueNegative = Double.parseDouble(aCriteria.getValue()) * -1;
          closureQuery.setParameter(aCriteria.getKey().name(),aValueNegative);      
          break;        
        case diferenciaCienMayorQue:  
        case diferenciaReaseguroMayorQue:  
        case AbsMayorQue:
        case TrmMayorQue:  
        case conMasDeUnaCoinicdenciaEnGw:
        case conMasDeUnaCoinicdenciaEnEco:  
          double aValue = Double.parseDouble(aCriteria.getValue());
          closureQuery.setParameter(aCriteria.getKey().name(),aValue);      
          break;        
        default:
      }
      
    }
  }
  
  private String createWhereBySearchCriteria(HashMap<ClosureTypeEnum,String> searchCriteria){
    String hql = " WHERE ";
    for (Iterator<Map.Entry<ClosureTypeEnum, String>> it = searchCriteria.entrySet().iterator(); it.hasNext();) {
      HashMap.Entry<ClosureTypeEnum,String> aCriteria = it.next();
      switch (aCriteria.getKey()) {
        case origen:   
          hql = hql + "c.origen LIKE :" + aCriteria.getKey().name();         
          break;
        case tipo:   
          hql = hql + "c.tipo LIKE :" + aCriteria.getKey().name(); 
          break;
        case referencia:  
          hql = hql + "c.referencia LIKE :" + aCriteria.getKey().name();         
          break; 
        case moneda:  
          hql = hql + "c.moneda LIKE :" + aCriteria.getKey().name();         
          break; 
        case AbsMayorQue:
        case diferenciaCienMayorQue:  
          hql = hql + "c.diferCien > :" + aCriteria.getKey().name();
          break;
        case AbsMenorQue:
        case diferenciaCienMenorQue:  
          hql = hql + "c.diferCien < :" + aCriteria.getKey().name();
          break;
        case faltanteEnECO:  
        case cienNoEncontradoEnGW:
        case cienNoEncontradoEnSap:  
          hql = hql + "c.diferCien is null";
          break;
        case TrmMayorQue:
        case diferenciaReaseguroMayorQue:  
          hql = hql + "c.diferReas > :" + aCriteria.getKey().name();
          break;
        case TrmMenorQue:
        case diferenciaReaseguroMenorQue:  
          hql = hql + "c.diferReas < :" + aCriteria.getKey().name();
          break;
        case faltanteEnSAP:
        case reaseguroNoEncontradoEnGW: 
        case reaseguroNoEncontradoenSAP:  
          hql = hql + "c.diferReas is null";
          break;
        case NoEncontradaEnGw:
          hql = hql + "c.valorCienGw is null";
          break;
        case NoEncontradaEnEco:
          hql = hql + "c.valorCienSap is null";
          break;
        case conMasDeUnaCoinicdenciaEnGw:
          hql = hql + "c.valorCienGw > :" + aCriteria.getKey().name();
          break;
        case conMasDeUnaCoinicdenciaEnEco:
          hql = hql + "c.valorCienSap > :" + aCriteria.getKey().name();
          break;        
      }
      if(it.hasNext()){
        hql = hql + " AND ";
      }
    }
    return hql;
  }
  
}
