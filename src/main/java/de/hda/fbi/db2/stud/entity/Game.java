package de.hda.fbi.db2.stud.entity;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;

@Entity
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence1")
  @SequenceGenerator(name = "post_sequence1", sequenceName = "post_sequence1",
      allocationSize = 1000)
  private int gameid = 0;

  private Timestamp startTime;
  private Timestamp endTime;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "player")
  private Player player = null;

  @ElementCollection
  @CollectionTable(name = "gameQuestion")
  @MapKeyColumn(name = "questionid")
  private Map<Question, Boolean> questionAnswer = new HashMap<Question, Boolean>();

  public Game() {
    this.startTime = new Timestamp(System.currentTimeMillis());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Game game = (Game) o;
    return gameid == game.gameid;
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameid);
  }

  public void setplayer(Player p) {
    this.player = p;
  }

  public Player getplayer() {
    return player;
  }

  public void setStartTime(Timestamp t) {
    this.startTime = new Timestamp(t.getTime());
  }

  /**
   * Deep copy of startGame in order to avoid mutation of the variable.
   * @return a starGame member Variable
   */
  public Timestamp getStartTime() {
    Timestamp startValue;
    startValue = startTime;
    return startValue;
  }

  public void setEndTime(Timestamp t) {
    this.endTime = new Timestamp(t.getTime());
  }

  /**
   * Deep copy of startGame in order to avoid mutation of the variable.
   * @return a endGame member Variable
   */
  public Timestamp getEndTime() {
    Timestamp startValue;
    startValue = endTime;
    return startValue;
  }

  /**
   * safes the position of given answere in the List of the Question Object.
   * @param q question object
   * @param a to safe answer
   *
   */
  public void addAnswer(Question q, Answer a) {
    this.questionAnswer.put(q, a.isCorrect());
  }

  public Map<Question, Boolean> getAnswer() {
    return this.questionAnswer;
  }
}

