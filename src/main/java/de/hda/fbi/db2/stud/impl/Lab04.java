package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab04MassData;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class Lab04 extends Lab04MassData {

  @Override
  public void createMassData() {
    List<Player> myArmy = new ArrayList<Player>();
    for (int i = 0; i < 10000; i++) {
      Player soldier = new Player();
      soldier.setPlayerName(Integer.toString(i));
      myArmy.add(soldier);
    }

    List<Game> fun = new ArrayList<Game>();
    for (Player p : myArmy) {
      Random day = new Random();
      int intplayDay = day.nextInt(31) + 1;
      String startFrame = Integer.toString(intplayDay);

      for (int i = 0; i < 100; i++) {
        Random base = new Random();
        List<Category> gameCat = new ArrayList<Category>();
        for (int x = 0; x < 7; x++) {
          int randomCat = base.nextInt(51);
          while (gameCat.contains(lab01Data.getCategories().get(randomCat))) {
            randomCat = base.nextInt(51);
          }
          gameCat.add(lab01Data.getCategories().get(randomCat));
        }

        List<Question> gameQuestions = new ArrayList<Question>();
        gameQuestions = (List<Question>) lab03Game.getQuestions(gameCat, 4);

        Game newGame = (Game) lab03Game.createGame(p, gameQuestions);

        for (Map.Entry<Question, Boolean> entry : newGame.getAnswer().entrySet()) {
          int randomIndex = base.nextInt(1 + 1);
          if (randomIndex == 0) {
            entry.setValue(false);
          } else {
            entry.setValue(true);
          }
        }

        long offset = Timestamp.valueOf("2020-01-" + startFrame + " 00:00:00").getTime();
        long end = Timestamp.valueOf("2020-01-" + startFrame + " 23:59:59").getTime();
        long diff = end - offset + 1;
        Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
        newGame.setStartTime(rand);
        rand.setTime(rand.getTime() + TimeUnit.MINUTES.toMillis(10));
        newGame.setEndTime(rand);
        fun.add(newGame);
      }
    }

    EntityManager em3 = lab02EntityManager.getEntityManager();
    EntityTransaction tx = null;
    try {

      tx = em3.getTransaction();
      long startTime = System.nanoTime();
      tx.begin();
      for (int i = 0; i < myArmy.size(); i++) {
        em3.persist(myArmy.get(i));
        if (i % 1000 == 0) {
          em3.flush();
          em3.clear();
        }
      }

      tx.commit();
      tx.begin();

      for (int i = 0; i < fun.size(); i++) {
        fun.get(i).setplayer(em3.merge(fun.get(i).getplayer()));
        em3.persist(fun.get(i));
        if (i % 1000 == 0) {
          em3.flush();
          em3.clear();
        }
      }
      tx.commit();

      long time = (System.nanoTime() - startTime);

      System.out.println("it took (s): " + Long.toString(time / 1000000000));

      //testen ob alles da ist
      tx.begin();
      TypedQuery<Long> query = em3.createQuery("SELECT COUNT(c) FROM Player c", Long.class);
      long playerCount = query.getSingleResult();
      System.out.println("player size (Database): " + playerCount);

      query = em3.createQuery("SELECT COUNT(c) FROM Game c", Long.class);
      long gameCount = query.getSingleResult();
      System.out.println("game size (Database): " + gameCount);
      tx.commit();

    } catch (RuntimeException e) {
      if (tx != null && tx.isActive()) {
        tx.rollback();
      }
      throw e;
    } finally {
      if (em3 != null) {
        em3.close();
      }
    }
  }
}

