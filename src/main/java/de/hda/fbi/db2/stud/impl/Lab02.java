package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab02EntityManager;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Lab02 extends Lab02EntityManager {

  private EntityManagerFactory emf = null;
  private EntityManager em = null;
  EntityTransaction tx = null;

  @Override
  public void persistData() {
    try {
      em = this.getEntityManager();
      tx = em.getTransaction();
      tx.begin();

      List<Category> categories = lab01Data.getCategories();
      List<Question> questions = lab01Data.getQuestions();

      for (Category c : categories) {
        em.persist(c);
        for (Question q : questions) {
          em.persist(q);
          em.persist(q.getAnswerList().get(0));
          em.persist(q.getAnswerList().get(1));
          em.persist(q.getAnswerList().get(2));
          em.persist(q.getAnswerList().get(3));
        }
      }

      tx.commit();
    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        tx.rollback();
      }
      throw e;
    } finally {
      if (em != null) {
        em.close();
      }
      emf.close();
    }
  }

  @Override
  public EntityManager getEntityManager() {
    //Create the EntityManagerFactory
    emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
    //Create a new EntityManager
    em = emf.createEntityManager();
    return em;
  }
}
