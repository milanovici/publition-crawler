package publition.entity;


import org.bson.Document;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Question {

    private final String title;
    private final String question;
    private final int likes;
    private final Instant date;
    private final String id;

    public Question(String id, String title, String question, int likes, Instant date) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", likes=" + likes +
                ", date=" + date +
                ", id='" + id + '\'' +
                '}';
    }

    public static Document toDocument(Question question) {
        Document doc = new Document();
        doc.append("id", question.getId());
        doc.append("title", question.getTitle());
        doc.append("title.word", tokenize(question.getTitle()));
        doc.append("question", question.getQuestion());
        doc.append("question.word", tokenize(question.getQuestion()));
        doc.append("likes", question.getLikes());
        doc.append("date", question.getDate().toString());
        return doc;
    }

    private static String tokenize(String input) {
        return Stream.of(input.split(" "))
                .map(String::toLowerCase)
                .distinct()
                .filter(w -> !w.isBlank())
                .filter(w -> !ignoredWords.contains(w))
                .map(w -> w.replaceAll("\\p{Punct}", ""))
                .collect(Collectors.joining(","));
    }

    public static List<String> ignoredWords = Stream.of(
            "of","with","at","from","into","during","including","until","against","among","throughout","despite",
            "towards","upon","concerning","to","in","for","on","by","about","like","through","over","before",
            "between","after","since","without","under","within","along","following","across","behind","beyond",
            "plus","except","but","up","out","around","down","off","above","near","and","that","or","as","if","when",
            "than","because","while","where","so","though","whether","although","nor","once","unless","now","your",
            "I","they","their","we","who","them","its","our","my","those","he","us","her","something","me","yourself",
            "someone","everything","itself","everyone","themselves","anyone","him","whose","myself","everybody",
            "ourselves","himself","somebody","yours","herself","whoever","you","it","this","what","which","these",
            "his","she","lot","anything","whatever","nobody","none","mine","anybody","some","there","all","another",
            "same","certain","nothing","self","nowhere","whom","why","much","the","a","an","also","be","can","come",
            "could","day","do","even","find","first","get","give","go","have","here","how","just","know","look","make",
            "man","many","more","new","no","not","one","only","other","people","say","see","take","tell","then","thing",
            "think","time","two","use","very","want","way","well","will","would","year"
    )
            .map(String::toLowerCase)
            .collect(Collectors.toList());
}
