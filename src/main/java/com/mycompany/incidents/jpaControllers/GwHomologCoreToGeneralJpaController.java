/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.GwHomologCoreToGeneral;
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
public class GwHomologCoreToGeneralJpaController implements Serializable {

  public GwHomologCoreToGeneralJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(GwHomologCoreToGeneral gwHomologCoreToGeneral) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(gwHomologCoreToGeneral);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(GwHomologCoreToGeneral gwHomologCoreToGeneral) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      gwHomologCoreToGeneral = em.merge(gwHomologCoreToGeneral);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = gwHomologCoreToGeneral.getId();
        if (findGwHomologCoreToGeneral(id) == null) {
          throw new NonexistentEntityException("The gwHomologCoreToGeneral with id " + id + " no longer exists.");
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
      GwHomologCoreToGeneral gwHomologCoreToGeneral;
      try {
        gwHomologCoreToGeneral = em.getReference(GwHomologCoreToGeneral.class, id);
        gwHomologCoreToGeneral.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The gwHomologCoreToGeneral with id " + id + " no longer exists.", enfe);
      }
      em.remove(gwHomologCoreToGeneral);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<GwHomologCoreToGeneral> findGwHomologCoreToGeneralEntities() {
    return findGwHomologCoreToGeneralEntities(true, -1, -1);
  }

  public List<GwHomologCoreToGeneral> findGwHomologCoreToGeneralEntities(int maxResults, int firstResult) {
    return findGwHomologCoreToGeneralEntities(false, maxResults, firstResult);
  }

  private List<GwHomologCoreToGeneral> findGwHomologCoreToGeneralEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(GwHomologCoreToGeneral.class));
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

  public GwHomologCoreToGeneral findGwHomologCoreToGeneral(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(GwHomologCoreToGeneral.class, id);
    } finally {
      em.close();
    }
  }

  public int getGwHomologCoreToGeneralCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<GwHomologCoreToGeneral> rt = cq.from(GwHomologCoreToGeneral.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
  public List<GwHomologCoreToGeneral> executeSearchHomologations(String valeToSearch, 
          HomologationTypeEnum homologationType,HomologationSearchTypeEnum searchType, JTextField aTextField){
    String hql = "SELECT h FROM GwHomologCoreToGeneral h ";         
    if(valeToSearch.trim().length()==0 && homologationType == HomologationTypeEnum.ALL){
       aTextField.setText("Para el tipo de homologación todos el valor a buscar no debe estar vacio");
       return new ArrayList<>();
    }
    if(valeToSearch.trim().length()==0 && homologationType != HomologationTypeEnum.ALL){
      hql = hql + " WHERE h.homologationName = :homologationName";
    } else {
      switch(searchType){
        case ValorCore:
          switch(homologationType){
            case ALL:
              hql = hql + " WHERE lower(h.source) LIKE lower(CONCAT('%',:valeToSearch,'%'))";
              break;
            default:              
              hql = hql + " WHERE lower(h.source) LIKE lower(CONCAT('%',:valeToSearch,'%'))";
              hql = hql + " AND h.homologationName = :homologationName";
          }
        break;
        case ValorEcosistema:
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
    Query aQuery = em.createQuery(hql, GwHomologCoreToGeneral.class);
    if(hql.contains("valeToSearch")){
        aQuery.setParameter("valeToSearch",valeToSearch);
    }
    if(hql.contains("homologationName")){
        aQuery.setParameter("homologationName",homologationType.name());
    }
    List<GwHomologCoreToGeneral> result = aQuery.getResultList();
    aTextField.setText("Encontrados: " + result.size());
    return result;
  }
  
}
