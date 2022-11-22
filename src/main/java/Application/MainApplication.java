package Application;

import java.time.Duration;
import java.time.LocalTime;

public class MainApplication {
    
    public static void main(String[] args) {
        
        LocalTime startTime = LocalTime.now();

//        WebCrawlerNotOptimized crawler = new WebCrawlerNotOptimized();
//        crawler.findAndScrapePaths("http://books.toscrape.com/");
        
        WebCrawler crawler = new WebCrawlerOptimized();
        crawler.crawl("http://books.toscrape.com/");
        
        LocalTime endTime = LocalTime.now();
        
        System.out.println("Finished scraping after " + getDurationAsString(startTime, endTime));
    }
    
    private static String getDurationAsString(LocalTime startTime, LocalTime endTime) {
        
        Duration duration = Duration.between(startTime, endTime);
        
        long h = duration.toHours();
        long min = duration.toMinutesPart();
        long sec = duration.toSecondsPart();
        
        return String.format("%02d h %02d min %02d sec", h, min, sec);
    }
    
}
