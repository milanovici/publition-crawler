package publition.entity;


import org.bson.Document;

import java.time.Instant;

public class Question {

    private final String title;
    private final String question;
    private final int likes;
    private final Instant date;

    public Question(String title, String question, int likes, Instant date) {
        this.title = title;
        this.question = question;
        this.likes = likes;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public int getLikes() {
        return likes;
    }

    public Instant getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", likes=" + likes +
                ", date=" + date +
                '}';
    }

    public static Document toDocument(Question question) {
        Document doc = new Document();
        doc.append("title", question.getTitle());
        doc.append("question", question.getQuestion());
        doc.append("likes", question.getLikes());
        doc.append("date", question.getDate().toString());
        return doc;
    }
}