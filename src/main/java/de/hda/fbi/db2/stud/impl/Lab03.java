package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class Lab03 extends Lab03Game {

  boolean ifNewPlayer = false;

  @Override
  public Object getOrCreatePlayer(String playerName) {
    EntityManager em3 = lab02EntityManager.getEntityManager();
    EntityTransaction tx = em3.getTransaction();
    try {
      tx.begin();
      Player player = (Player) em3.createNamedQuery("Player.findPlayer")
          .setParameter("name", playerName).getSingleResult();
      tx.commit();
      em3.close();
      return player;
    } catch (NoResultException enr) {
      tx.commit();
      Player newPlayer = new Player();
      newPlayer.setPlayerName(playerName);
      ifNewPlayer = true;
      em3.close();
      return newPlayer;
    }
  }

  @Override
  public Object interactiveGetOrCreatePlayer() {
    Scanner input = new Scanner(System.in, "UTF-8");

    System.out.println("Please enter your Playername or create a new Player if you are a new user");
    String playerName = "";
    while (playerName.length() == 0 || !playerName.matches("[a-zA-Z0-9]*")) {
      playerName = "";
      System.out.print("Playername (only a-z, A-Z, 0-9): ");
      playerName = input.nextLine();
    }
    System.out.println("\n");
    return getOrCreatePlayer(playerName);
  }

  @Override
  public List<?> getQuestions(List<?> categories, int amountOfQuestionsForCategory) {
    List<Question> questionList = new ArrayList<Question>();
    List<Category> categories1 =  ((List<Category>) (List<?>) categories);
    for (Category c : categories1) {
      List<Question> pool = new ArrayList<>(c.getQuestionList());
      for (int i = 0; i < pool.size() && i < amountOfQuestionsForCategory; i++) {
        questionList.add(pool.get(0));
        pool.remove(0);
        Collections.shuffle(pool);
      }
    }
    return questionList;
  }

  @Override
  public List<?> interactiveGetQuestions() {
    System.out.println("Please enter at least 2 Categories you wish to be tested on");
    System.out.println("If you are done selecting just hit ENTER");
    Map<Integer, Category> categories = new HashMap<>();

    List result = lab02EntityManager.getEntityManager()
        .createNamedQuery("Category.getAllCategories").getResultList();
    Map<Integer, Category> allCatgories = new HashMap<>();

    for (Iterator i = result.iterator(); i.hasNext(); ) {
      Category category = (Category) i.next();
      allCatgories.put(category.getCategoryId(), category);
    }

    for (int i = 1; i <= allCatgories.size(); i++) {
      Category category = allCatgories.get(i);
      System.out.println(category.getCategoryId() + ". " + category.getName());
    }
    String input = "";
    boolean cancel = true;
    Scanner in = new Scanner(System.in, "UTF-8");
    int chosenIndex = -1;
    while (cancel) {
      System.out.print("Index: ");
      input = in.nextLine();
      if (input.equals("")) {
        if (categories.size() >= 2) {
          cancel = false;
          continue;
        } else {
          System.out.println("You have to choose at least 2 different catgories.\n"
              + "At the moment you have chosen " + categories.size() + " categories.");
          continue;
        }
      }

      try {
        chosenIndex = Integer.parseInt(input);
        if (chosenIndex > allCatgories.size() || chosenIndex < 1) {
          System.out.println("Choose a number between 1 - " + allCatgories.size()
              + ".");
          continue;
        }
      } catch (NumberFormatException e) {
        System.out.println("Give a number as input.");
        continue;
      }

      if (categories.containsKey(chosenIndex)) {
        System.out.println("This category was already chosen.");
      } else {
        categories.put(chosenIndex, allCatgories.get(chosenIndex));
      }
    }

    System.out.println("Choose the number of questions you want to answer.");
    int numberOfQuestions = -1;
    System.out.print("Number: ");
    while (numberOfQuestions == -1) {
      try {
        numberOfQuestions = Integer.parseInt(in.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Choose a number value.");
        numberOfQuestions = -1;
      }
    }

    List<Category> cat = new ArrayList<>();
    for (Map.Entry<Integer, Category> entry : categories.entrySet()) {
      cat.add(entry.getValue());
    }

    return getQuestions(cat, numberOfQuestions);
  }

  @Override
  public Object createGame(Object player, List<?> questions) {
    Game newGame = new Game();
    newGame.setplayer((Player)player);
    //((Player)player).setPlayedGames(newGame);
    List<Question> questions1 = ((List<Question>) (List<?>) questions);
    for (Question q : questions1) {
      newGame.addAnswer(q, q.getAnswerList().get(0));
    }
    return newGame;
  }

  @Override
  public void playGame(Object game) {
    Random rand = new Random();

    Game playedGame = (Game) game;
    for (Map.Entry<Question, Boolean> entry : playedGame.getAnswer().entrySet()) {
      int randomIndex = rand.nextInt(1 + 1);
      if (randomIndex == 0) {
        entry.setValue(false);
      } else {
        entry.setValue(true);
      }
    }
    playedGame.setEndTime(new Timestamp(System.currentTimeMillis()));
  }

  @Override
  public void interactivePlayGame(Object game) {
    //playGame(game);
    Game playedGame = (Game) game;
    System.out.println("\nAnswer by inserting the Index 0-3");
    for (Map.Entry<Question, Boolean> entry : playedGame.getAnswer().entrySet()) {
      System.out.println(entry.getKey().getQuestionText() + "\n");
      System.out.println("0: " + entry.getKey().getAnswerList().get(0).getAnswerText() + "\n");
      System.out.println("1: " + entry.getKey().getAnswerList().get(1).getAnswerText() + "\n");
      System.out.println("2: " + entry.getKey().getAnswerList().get(2).getAnswerText() + "\n");
      System.out.println("3: " + entry.getKey().getAnswerList().get(3).getAnswerText() + "\n");

      String input = "";
      while (true) {
        System.out.print("Index chosen answer: ");
        Scanner in = new Scanner(System.in, "UTF-8");
        input = in.nextLine();
        System.out.print("\n");
        if (input.equals("0") || input.equals("1") || input.equals("2") || input.equals("3")) {
          int chosen = Integer.parseInt(input);
          entry.setValue(entry.getKey().getAnswerList().get(chosen).isCorrect());
          System.out.print("The answer is: "
              + entry.getKey().getAnswerList().get(chosen).isCorrect() + "\n\n");
          break;
        } else {
          System.out.print("You must answer all questions, pls input a valid index (0-3)\n");
        }
      }

    }
    playedGame.setEndTime(new Timestamp(System.currentTimeMillis()));
  }

  @Override
  public void persistGame(Object game) {
    EntityManager em3 = lab02EntityManager.getEntityManager();
    EntityTransaction tx = null;
    Game persistGame = (Game) game;
    try {
      tx = em3.getTransaction();
      tx.begin();
      if (ifNewPlayer == false) {
        Player merged = em3.merge(persistGame.getplayer());
        persistGame.setplayer(merged);
      }
      em3.persist(persistGame);
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
