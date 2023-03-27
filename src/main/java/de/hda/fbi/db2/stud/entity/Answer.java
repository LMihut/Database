package de.hda.fbi.db2.stud.entity;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int answerid;
  boolean isCorrect = true;
  private String answerText = "";
  @ManyToOne
  @JoinColumn(nullable = false)
  private Question question = null;

  public Answer(){

  }

  public Answer(String answerText) {
    this.answerText = answerText;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Answer answer = (Answer) o;
    return Objects.equals(answerText, answer.answerText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(answerText);
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public boolean isCorrect() {
    return isCorrect;
  }

  public void setCorrect(boolean correct) {
    isCorrect = correct;
  }

}
