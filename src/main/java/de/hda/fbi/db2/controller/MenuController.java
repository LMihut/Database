package de.hda.fbi.db2.controller;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.swing.text.StyledEditorKit.StyledTextAction;

/**
 * MenuController Created by l.koehler on 05.08.2019.
 */
public class MenuController {

  private Controller controller;

  public MenuController(Controller controller) {
    this.controller = controller;
  }

  /**
   * shows the menu.
   */
  public void showMenu() {
    do {
      System.out.println("Choose your Destiny?");
      System.out.println("--------------------------------------");
      System.out.println("1: Re-read csv");
      System.out.println("2: Play test");
      System.out.println("3: Create mass data");
      System.out.println("4: Analyze data");
      System.out.println("0: Quit");
    } while (readInput());
  }

  private boolean readInput() {
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      String input = reader.readLine();
      if (input == null) {
        return true;
      }
      switch (input) {
        case "0":
          return false;
        case "1":
          readCsv();
          break;
        case "2":
          playTest();
          break;
        case "3":
          createMassData();
          break;
        case "4":
          analyzeData();
          break;
        default:
          System.out.println("Input Error");
          break;
      }

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void analyzeData() {
    Scanner in = new Scanner(System.in, "UTF-8");
    Timestamp start = null;
    Timestamp end = null;
    try {
      System.out.println("Input a StartTime and EndTime (dd) and confirm both with Enter:");
      String startTime = in.nextLine();
      String endtTime = in.nextLine();
      start = Timestamp.valueOf("2020-01-" + startTime + " 00:00:00");
      end = Timestamp.valueOf("2020-01-" + endtTime + " 23:59:59");
    } catch (Exception e) {
      System.out.println(e);
    }
    //first Query ->all players that played in given timeframe
    EntityManager em4 = controller.getLab02EntityManager().getEntityManager();
    String query1 = "select distinct g.player from Game g "
        + "where g.startTime between :start and :end order by g.gameid";
    List<?> result = em4.createQuery(query1)
        .setParameter("start", start).setParameter("end", end).getResultList();
    System.out.println("\n\n Query1: ");
    System.out.println("All players that played in given timeframe (" + start + " " + end + ") : ");
    for (Object o : result) {
      System.out.println("Player Name: " + ((Player) o).getPlayerName());
    }

    //secound Query ->one random player and his games and number of correct answers
    Random base = new Random();
    String rndPlayer = Integer.toString(base.nextInt(10000));
    Query query2a = em4.createQuery("Select distinct g.gameid, g.startTime, count(KEY(q)),  "
        + "sum(case when VALUE(q) = True then 1 else 0 END) as correctAnswerCount "
        + "from Game g join g.questionAnswer q "
        + "where g.player.playerName = :rndPlayer "
        + "group by g.gameid ");
    query2a.setParameter("rndPlayer", rndPlayer);
    System.out.println("\n\n Query2: ");
    System.out.println("Choosen player name: " + rndPlayer);

    long startTime = System.nanoTime();
    List playL = query2a.getResultList();
    long time = (System.nanoTime() - startTime);
    System.out.println("it took (ms): " + Long.toString(time / 1000000));

    System.out.println("The following player has played these games:");
    for (Iterator it = playL.iterator(); it.hasNext(); ) {
      Object[] element = (Object[]) it.next();
      if (element.length > 0) {
        System.out.println("Game id: " + element[0].toString()
            + " date: " + element[1].toString()
            + " amount of questions pro game: " + element[2].toString()
            + " correct answer: " + element[3].toString());
      }
    }

    //third Query ->all Players, Nr of played games - desc
    System.out.println("\n\n Query3: ");
    /*String query3a = "select p.playerName, count(g) as gamecount from Player p, Game g "
        + "where p.playerid = g.player.playerid group by p.playerName order by gamecount desc";*/
    String query3="select g.player.playerName, count(g) as gamecount from Game g "
        + "group by g.player.playerName order by gamecount desc";

    startTime = System.nanoTime();
    List<Object[]> playerAndGames = em4.createQuery(query3).getResultList();
    time = (System.nanoTime() - startTime);
    System.out.println("it took (ms): " + Long.toString(time / 1000000));

    System.out.println("\nAll players & number of games: ");
    for (Object[] o : playerAndGames) {
      System.out.println("Player: " + o[0].toString() + " Nr. of Games: " + o[1].toString());
    }

    //fourth Query ->all categorys sort by choice
    System.out.println("\n\n Query4: ");

    startTime = System.nanoTime();
    List resultQ4 = em4.createQuery("select KEY(q).category as category, count(category) as c "
        + "from Game g join g.questionAnswer q group by category order by c").getResultList();
    time = (System.nanoTime() - startTime);
    System.out.println("it took (ms): " + Long.toString(time / 1000000));

    //"select KEY(q).category as category, count(category) as c from game g join g.questionAnswer q group by category order by c"
    for (Iterator it = resultQ4.iterator(); it.hasNext(); ) {
      Object[] element = (Object[]) it.next();
      System.out.println("Questions with the categoryID " + element[0].toString()
          + " has been " + element[1].toString()
          + " times asked");
    }
  }

  private void createMassData() {
    controller.createMassData();
  }

  private void playTest() {
    Lab03Game gameController = controller.getLab03Game();
    Object player = gameController.interactiveGetOrCreatePlayer();
    List<?> questions = gameController.interactiveGetQuestions();
    Object game = gameController.createGame(player, questions);
    gameController.interactivePlayGame(game);
    gameController.persistGame(game);
  }

  private void readCsv() {
    controller.readCsv();
  }
}
