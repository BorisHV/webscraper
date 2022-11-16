package Application;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Application {
    public static void DownloadWebPage(String webpage) {
        try {
            // Create URL object
            URL url = new URL(webpage);
            BufferedReader readr =
                    new BufferedReader(new InputStreamReader(url.openStream()));
            String directories = "catalogue/category/books/paranormal_24/";
            File file = new File(directories);
            file.mkdirs();
            // Enter filename in which you want to download
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter("Page.html"));
            // read each line from stream till end
            String line;
            while ((line = readr.readLine()) != null) {
                writer.write(line);
            }
            readr.close();
            writer.close();
            System.out.println("Successfully Downloaded.");
        }
        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        } catch (IOException ie) {
            System.out.println("IOException raised");
        }
    }
    private static Set<String> findLinks(String url) throws IOException {
        Set<String> links = new HashSet<>();
        Document doc = Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(3000)
                .get();
        Elements elements = doc.select("a[href]");
        for (Element element : elements) {
            links.add(element.attr("href"));
        }
        return links;
    }
    public static void main(String args[])
            throws IOException {
        for (String link : findLinks("http://books.toscrape.com/")) {
            System.out.println(link);
            String url = link;
            DownloadWebPage(url);
        }
    }
}