package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
@NamedQueries({
    @NamedQuery(name = "Player.findPlayer",
        query = "select m from Player m where m.playerName = :name")
})
public class Player {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
  @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1000)
  private int playerid = 0;
  @Column(unique = true)
  private String playerName = "";
  //@OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  //private List<Game> playedGames = new ArrayList<>();

  public Player(){

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return playerid == player.playerid;
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerid);
  }

  public void setPlayerName(String s) {
    this.playerName = s;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public int getPlayerId() {
    return this.playerid;
  }

  //public void setPlayedGames(Game g) {
  //  this.playedGames.add(g);
  //}

  //public List<Game> getPlayedGames() {
  //  return this.playedGames;
  //}
}
