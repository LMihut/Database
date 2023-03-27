package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({
    @NamedQuery(name = "Category.getAllCategories",
        query = "select m from Category m")
})
@Entity
public class Category {
  @Column (unique = true)
  private String name;
  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private int categoryId;
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Question> questionList = new ArrayList<>();

  /*
  Constructors
  */
  public Category(){

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return categoryId == category.categoryId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoryId);
  }

  public Category(String categoryName) {
    this.name = categoryName;
  }

  public String getName() {
    return name;
  }

  public void setName(String categoryName) {
    this.name = categoryName;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public List<Question> getQuestionList() {
    return questionList;
  }

  public void setQuestionList(List<Question> questionList) {
    this.questionList = questionList;
  }

  public void addToQuestionList(Question question) {
    this.questionList.add(question);
  }

}
