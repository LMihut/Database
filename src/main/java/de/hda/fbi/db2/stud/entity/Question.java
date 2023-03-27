package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
public class Question {
  @Id
  private int questionId = 0;
  private String questionText = "";
  @ManyToOne
  private Category category = null;

  @OneToMany(mappedBy = "question")
  @OrderColumn(name = "reihenfolge")
  private List<Answer> answerList = new ArrayList<>();

  public Question(){

  }

  /** test.
   */
  public Question(int questionId, String questionText, Category category, List<Answer> answerList) {
    this.questionId = questionId;
    this.questionText = questionText;
    this.category = category;
    this.answerList = answerList;
  }

  public int getQuestionId() {
    return questionId;
  }

  public void setQuestionId(int questionId) {
    this.questionId = questionId;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public List<Answer> getAnswerList() {
    return answerList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Question question = (Question) o;
    return questionId == question.questionId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId);
  }

  public void setAnswerList(List<Answer> answerList) {
    this.answerList = answerList;
  }

}
