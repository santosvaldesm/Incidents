package com.mycompany.incidents.jpaControllers;

import com.mycompany.incidents.entities.Note;
import com.mycompany.incidents.jpaControllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class NoteJpaController implements Serializable {

  public NoteJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Note note) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(note);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Note note) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      note = em.merge(note);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = note.getId();
        if (findNote(id) == null) {
          throw new NonexistentEntityException("The note with id " + id + " no longer exists.");
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
      Note note;
      try {
        note = em.getReference(Note.class, id);
        note.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The note with id " + id + " no longer exists.", enfe);
      }
      em.remove(note);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<Note> findNoteEntities() {
    return findNoteEntities(true, -1, -1);
  }

  public List<Note> findNoteEntities(int maxResults, int firstResult) {
    return findNoteEntities(false, maxResults, firstResult);
  }

  private List<Note> findNoteEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Note.class));
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

  public Note findNote(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Note.class, id);
    } finally {
      em.close();
    }
  }

  public int getNoteCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Note> rt = cq.from(Note.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
  public List<Note> executeSearchIncident(String parameterDescription){
    String hql = "SELECT i FROM Note i ";               
    hql = hql + "WHERE lower(i.typenote) LIKE lower(CONCAT('%',:parameterType,'%'))";
    if(!parameterDescription.trim().isEmpty()){          
      hql = hql + " OR lower(i.description) LIKE lower(CONCAT('%',:parameterDescription,'%'))";           
    }  
    hql = hql + " ORDER BY i.typenote ASC ";
    EntityManager em = getEntityManager();
    Query aQuery = em.createQuery(hql, Note.class);
    if(hql.contains("parameterType")){
        aQuery.setParameter("parameterType",parameterDescription);
    }
    if(hql.contains("parameterDescription")){
        aQuery.setParameter("parameterDescription",parameterDescription);
    }
    return aQuery.getResultList();
  }  
}
