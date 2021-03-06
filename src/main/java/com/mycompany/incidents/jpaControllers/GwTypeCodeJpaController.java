package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.GwLobModel;
import com.mycompany.incidents.entities.GwTypeCode;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import com.mycompany.incidents.otherResources.TypeKeysEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GwTypeCodeJpaController implements Serializable {

  public GwTypeCodeJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(GwTypeCode gwTypeCode) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(gwTypeCode);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(GwTypeCode gwTypeCode) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      gwTypeCode = em.merge(gwTypeCode);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = gwTypeCode.getId();
        if (findGwTypeCode(id) == null) {
          throw new NonexistentEntityException("The gwTypeCode with id " + id + " no longer exists.");
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
      GwTypeCode gwTypeCode;
      try {
        gwTypeCode = em.getReference(GwTypeCode.class, id);
        gwTypeCode.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The gwTypeCode with id " + id + " no longer exists.", enfe);
      }
      em.remove(gwTypeCode);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<GwTypeCode> findGwTypeCodeEntities() {
    return findGwTypeCodeEntities(true, -1, -1);
  }

  public List<GwTypeCode> findGwTypeCodeEntities(int maxResults, int firstResult) {
    return findGwTypeCodeEntities(false, maxResults, firstResult);
  }

  private List<GwTypeCode> findGwTypeCodeEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(GwTypeCode.class));
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

  public GwTypeCode findGwTypeCode(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(GwTypeCode.class, id);
    } finally {
      em.close();
    }
  }

  public int getGwTypeCodeCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<GwTypeCode> rt = cq.from(GwTypeCode.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
  public List<GwTypeCode> findAllTypeCodesByCategory(TypeKeysEnum category){
    EntityManager em = getEntityManager();        
    TypedQuery<GwTypeCode> typeCodeQuery = em.createNamedQuery("GwTypeCode.findByTypeKeyName", GwTypeCode.class);
    typeCodeQuery.setParameter("typeKeyName",category.name());    
    try {
      if(typeCodeQuery.getResultList().isEmpty()){
        return new ArrayList<>();
      }
      return typeCodeQuery.getResultList();
    } finally {
      em.close();
    }      
  }
  
  public GwTypeCode findTypeCodeByCategoryAndCode(TypeKeysEnum category,String code){
    EntityManager em = getEntityManager();        
    TypedQuery<GwTypeCode> typeCodeQuery = em.createNamedQuery("GwTypeCode.findTypeCodeByCategoryAndCode", GwTypeCode.class);
    typeCodeQuery.setParameter("typeKeyName",category.name());    
    typeCodeQuery.setParameter("typeCode",code);    
    try {
      if(typeCodeQuery.getResultList().isEmpty()){
        return null;
      }
      return typeCodeQuery.getSingleResult();
    } finally {
      em.close();
    }      
  }
  
  public String getColumnForLobByCategory(GwLobModel aLob,TypeKeysEnum category){
    switch(category){
      case CoverageType:       return aLob.getCoverageType();        
      case CostCategory:       return aLob.getCostCategory();
      case CoverageSubtype:    return aLob.getCoverageSubtype();
      case CovTermPattern:     return aLob.getCovtermPattern();
      case ExposureType:       return aLob.getExposureType();
      case InternalPolicyType: return aLob.getInternalPolicyType();
      case LOBCode:            return aLob.getLobCode();
      case LossCause:          return aLob.getLossCause();
      case LossPartyType:      return aLob.getLossPartyType();
      case LossType:           return aLob.getLossType();
      case OfferingType_Ext:   return aLob.getOffering();
      case PolicyTab:          return aLob.getPolicyTab();
      case PolicyType:         return aLob.getPolicyType();        
    }
    return "";
  }
  
  public boolean isFoundTypeCodeInGwTypeCodeList(List<GwTypeCode> aList, String aCode){
    for(GwTypeCode aTypeCode : aList) {
      if(aTypeCode.getTypeCode().compareTo(aCode)==0){
        return true;
      }
    }
    return false;    
  }
  /* 
  * transforma List<GwLobModel> a List<GwTypeCode> sin repetidos
  */
  public List<GwTypeCode> createFilteredTypeCodeList(List<GwLobModel> listWithRepeat,TypeKeysEnum category) {
    List<GwTypeCode> resultList = new ArrayList<>();
    for(GwLobModel aPosibleRepeat : listWithRepeat){
      String aSearchCode2 = getColumnForLobByCategory(aPosibleRepeat,category);
      String[] aSplitSearchCode = aSearchCode2.split(";");
      for(String aSearchCode : aSplitSearchCode) {
        if(aSearchCode.compareTo("-")==0){
          continue;
        }
        boolean isFound = isFoundTypeCodeInGwTypeCodeList(resultList,aSearchCode);      
        if(!isFound && !aSearchCode.isEmpty()){
          resultList.add(findTypeCodeByCategoryAndCode(category,aSearchCode));
        }
      }      
    } 
    return resultList;
  }
}
