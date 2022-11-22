package Application;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainApplication {


    public static void main(String[] args) {

        WebCrawlerNotOptimized crawler = new WebCrawlerNotOptimized();
        Runnable runnableWebCrawler = () -> crawler.findAndScrapePaths("http://books.toscrape.com/");
        setUpAThreadPoolAndStart(runnableWebCrawler);
    }

    private static void setUpAThreadPoolAndStart(Runnable runnable){
        LocalTime startTime = LocalTime.now();
        LocalTime endTime;
        int numberOfThreads = 16;
        ExecutorService crawlerPool = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            crawlerPool.execute(runnable);
        }
        crawlerPool.shutdown();

        while (!crawlerPool.isTerminated()) {
            Thread.yield();
        }
        endTime = LocalTime.now();
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
