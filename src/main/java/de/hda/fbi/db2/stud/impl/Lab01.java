package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab01Data;
import de.hda.fbi.db2.stud.entity.Answer;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lab01 extends Lab01Data {
  private List<Question> questions = new ArrayList<>();
  private List<Category> categories = new ArrayList<>();
  private int counter = categories.size();

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

  @Override
  public List<Category> getCategories() {
    return categories;
  }

  @Override
  public void loadCsvFile(List<String[]> additionalCsvLines) {
    //Liste mit Kategorie Namen
    Map<String,Integer> catMap = new HashMap<>();
    // bearbeiten der einzelnen zeilen
    for (int i = 1; i < additionalCsvLines.size(); i++) {
      // neues question object
      Question question = new Question();

      //check ob die categorie schon gibt
      Integer position = catMap.get(additionalCsvLines.get(i)[7]);
      //falls nein -> neue categorie
      if (position == null) {
        catMap.put(additionalCsvLines.get(i)[7], counter);
        counter += 1;
        Category category = new Category();
        category.setCategoryId(i);
        category.setName(additionalCsvLines.get(i)[7]);
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);
        category.setQuestionList(questionList);
        question.setCategory(category);
        categories.add(category);
        //falls doch -> anhängen der neuen frage und referenz bei frage eintragen
      } else {
        question.setCategory(categories.get(position));
        categories.get(position).addToQuestionList(question);
      }

      //Fragen mit infos füllen id/Text
      question.setQuestionId(Integer.parseInt(additionalCsvLines.get(i)[0]));
      question.setQuestionText(additionalCsvLines.get(i)[1]);

      //4 neue antowrten
      Answer answer1 = new Answer();
      Answer answer2 = new Answer();
      Answer answer3 = new Answer();
      Answer answer4 = new Answer();
      List<Answer> answerList = new ArrayList<>(
          List.of(answer1,
              answer2,
              answer3,
              answer4));
      //Fragen ihre neuen antworten als Liste geben
      question.setAnswerList(answerList);

      //"richtige Antwort" index speichern
      int trueAnswer = Integer.parseInt(additionalCsvLines.get(i)[6]);
      //Antworten mit infos füllen
      answer1.setQuestion(question);
      answer1.setAnswerText(additionalCsvLines.get(i)[2]);
      answer1.setCorrect(1 == trueAnswer);
      answer2.setQuestion(question);
      answer2.setAnswerText(additionalCsvLines.get(i)[3]);
      answer2.setCorrect(2 == trueAnswer);
      answer3.setQuestion(question);
      answer3.setAnswerText(additionalCsvLines.get(i)[4]);
      answer3.setCorrect(3 == trueAnswer);
      answer4.setQuestion(question);
      answer4.setAnswerText(additionalCsvLines.get(i)[5]);
      answer4.setCorrect(4 == trueAnswer);

      questions.add(question);
      
      System.out.print(question.getCategory().getName() + "\n");
      System.out.print(question.getQuestionText() + "\n");
      System.out.print(answer1.getAnswerText() + "\n");
      System.out.print(answer2.getAnswerText() + "\n");
      System.out.print(answer3.getAnswerText() + "\n");
      System.out.print(answer4.getAnswerText() + "\n");
      System.out.print("Right Answer: " + trueAnswer + "\n\n");
    }

    System.out.print("categories: " + categories.size() + "\n");
    System.out.print("questions: " + questions.size() + "\n");

  }

}
