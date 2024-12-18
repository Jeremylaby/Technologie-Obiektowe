package app;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CrawlerApp {

    public static final String SCRAPER_API_KEY = "f8a8a55046f67fa801bf22889838c40c";

    private static final List<String> TOPICS = List.of("Gym","Bike","Dark Souls 3",  "Slay the spire");


    public static void main(String[] args) throws IOException {
        PhotoCrawler photoCrawler = new PhotoCrawler();
        photoCrawler.resetLibrary();
////        photoCrawler.downloadPhotoExamples();
//        photoCrawler.downloadPhotosForQuery(TOPICS.get(0));
        photoCrawler.downloadPhotosForMultipleQueries(TOPICS);


    }
}