package publition;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import publition.entity.Question;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Main {

    static final DateTimeFormatter FMT = new DateTimeFormatterBuilder()
            .appendPattern("MMM dd, yyyy")
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.of("Europe/Berlin"));

    public static void main(String[] args) {
        ConnectionString connectionString = new ConnectionString(args[0]);
        MongoClient mongoClient = MongoClients.create(connectionString);
        String url = "https://publit.io/community/help?page=1&filter=newest";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numOfQuestions = Integer.parseInt(doc.select("question-filter").attr("count"));
        int numOfPages = args.length > 1 ? Integer.parseInt(args[1]) : (int) Math.ceil((double) numOfQuestions / 10);

        for (int page = 2; page < numOfPages + 2; ++page) {
            Elements questionLinkElements = doc.select("a.accordion__title");
            for (Element questionLinkElement : questionLinkElements) {
                Document questionDocument = null;
                try {
                    questionDocument = Jsoup.connect("https://publit.io" + questionLinkElement.attr("href")).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String title = questionDocument.select("h2.faq__title").text();
                String question = questionDocument.select("div.faq__question").text();
                String likes = questionDocument.select("div.likes").select("like-button").attr("like-count");
                String date = questionDocument.select("div.blog-intro__date").get(0).text();
                Instant inst = FMT.parse(date, Instant::from);
                String id = questionDocument.baseUri().split("/")[questionDocument.baseUri().split("/").length - 1];
                Question q = new Question(id, title, question, Integer.parseInt(likes), inst);
                System.out.println(q);
                mongoClient.getDatabase(connectionString.getDatabase()).getCollection("publitio").insertOne(Question.toDocument(q));
            }
            url = "https://publit.io/community/help?page=" + page + "&filter=newest";
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
