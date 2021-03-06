/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.GwHomologGeneralToCore;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.HomologationSearchTypeEnum;
import com.mycompany.incidents.otherResources.HomologationTypeEnum;
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
public class GwHomologGeneralToCoreJpaController implements Serializable {

  public GwHomologGeneralToCoreJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(GwHomologGeneralToCore gwHomologGeneralToCore) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(gwHomologGeneralToCore);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(GwHomologGeneralToCore gwHomologGeneralToCore) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      gwHomologGeneralToCore = em.merge(gwHomologGeneralToCore);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = gwHomologGeneralToCore.getId();
        if (findGwHomologGeneralToCore(id) == null) {
          throw new NonexistentEntityException("The gwHomologGeneralToCore with id " + id + " no longer exists.");
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
      GwHomologGeneralToCore gwHomologGeneralToCore;
      try {
        gwHomologGeneralToCore = em.getReference(GwHomologGeneralToCore.class, id);
        gwHomologGeneralToCore.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The gwHomologGeneralToCore with id " + id + " no longer exists.", enfe);
      }
      em.remove(gwHomologGeneralToCore);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<GwHomologGeneralToCore> findGwHomologGeneralToCoreEntities() {
    return findGwHomologGeneralToCoreEntities(true, -1, -1);
  }

  public List<GwHomologGeneralToCore> findGwHomologGeneralToCoreEntities(int maxResults, int firstResult) {
    return findGwHomologGeneralToCoreEntities(false, maxResults, firstResult);
  }

  private List<GwHomologGeneralToCore> findGwHomologGeneralToCoreEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(GwHomologGeneralToCore.class));
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

  public GwHomologGeneralToCore findGwHomologGeneralToCore(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(GwHomologGeneralToCore.class, id);
    } finally {
      em.close();
    }
  }

  public int getGwHomologGeneralToCoreCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<GwHomologGeneralToCore> rt = cq.from(GwHomologGeneralToCore.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
  public List<GwHomologGeneralToCore> executeSearchHomologations(String valeToSearch, 
          HomologationTypeEnum homologationType,HomologationSearchTypeEnum searchType, JTextField aTextField){    
    String hql = "SELECT h FROM GwHomologGeneralToCore h ";         
    if(valeToSearch.trim().length()==0 && homologationType == HomologationTypeEnum.ALL){
       aTextField.setText("Para el tipo de homologación todos el valor a buscar no debe estar vacio");
       return new ArrayList<>();
    }
    if(valeToSearch.trim().length()==0 && homologationType != HomologationTypeEnum.ALL){
      hql = hql + " WHERE h.homologationName = :homologationName";
    } else {
      switch(searchType){
        case ValorEcosistema:
          switch(homologationType){
            case ALL:
              hql = hql + " WHERE lower(h.source) LIKE lower(CONCAT('%',:valeToSearch,'%'))";
              break;
            default:              
              hql = hql + " WHERE lower(h.source) LIKE lower(CONCAT('%',:valeToSearch,'%'))";
              hql = hql + " AND h.homologationName = :homologationName";
          }
        break;
        case ValorCore:
          switch(homologationType){
            case ALL:
              hql = hql + " WHERE lower(h.target) LIKE lower(CONCAT('%',:valeToSearch,'%'))";              
              break;
            default:              
              hql = hql + " WHERE lower(h.target) LIKE lower(CONCAT('%',:valeToSearch,'%'))";
              hql = hql + " AND h.homologationName = :homologationName";
          }
        break;          
      }
    }
       
    EntityManager em = getEntityManager();
    Query aQuery = em.createQuery(hql, GwHomologGeneralToCore.class);
    if(hql.contains("valeToSearch")){
        aQuery.setParameter("valeToSearch",valeToSearch);
    }
    if(hql.contains("homologationName")){
        aQuery.setParameter("homologationName",homologationType.name());
    }
    List<GwHomologGeneralToCore> result = aQuery.getResultList();
    aTextField.setText("Encontrados: " + result.size());
    return result;
  }
  
}
