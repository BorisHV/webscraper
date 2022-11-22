package Application;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.IOException;

public class WebScraperNotOptimized {

    //private static File fileToBeWritten;
    //private static File imgToBeWritten;
    //static String filteredDirectory;

    public static void successMessage(String created) {
        String message = String.format("Created: '%s'", created);
        System.out.println(message);
    }

    public void scrape(String pathFromCrawler) {

        try {
            int indexToFilterDirectory = pathFromCrawler.lastIndexOf('/');
            int indexToFilterFileName = pathFromCrawler.length();
            String filteredFileName = pathFromCrawler.substring(indexToFilterDirectory + 1, indexToFilterFileName);
            String filteredDirectory ="";

            if(indexToFilterDirectory!=-1) {
                filteredDirectory = pathFromCrawler.substring(0, indexToFilterDirectory);
            }

            // Create URL object
            URL url = new URL("http://books.toscrape.com/" + pathFromCrawler);


                saveDirectory(filteredDirectory);

            if (filteredFileName.endsWith(".jpg")) {
                saveJpg(url, filteredFileName, filteredDirectory);
            } else {
                saveFile(url, filteredFileName, filteredDirectory);
            }
        }

        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        } catch (IOException ie) {
            System.out.println("IOException, empty path");
        }
    }

    private void saveDirectory(String filteredDirectory) {

            File file = new File("ScrapedWebsite", filteredDirectory);
            file.mkdirs();
            successMessage(filteredDirectory);
    }

    private void saveJpg(URL url, String filteredFileName, String filteredDirectory) throws IOException {
        File imgToBeWritten;
        if (filteredDirectory.equals("")) {
            imgToBeWritten = new File("ScrapedWebsite", filteredFileName);
        } else {
            imgToBeWritten = new File("ScrapedWebsite/" + filteredDirectory, filteredFileName);
        }
        BufferedImage image = null;
        image = ImageIO.read(url);
        ImageIO.write(image, "jpg", new File(String.valueOf(imgToBeWritten)));
        successMessage(filteredFileName);
    }

    private void saveFile(URL url, String filteredFileName, String filteredDirectory) throws IOException {
        File fileToBeWritten;
        if (filteredDirectory.equals("")) {
            fileToBeWritten = new File("ScrapedWebsite", filteredFileName);
        } else {
            fileToBeWritten = new File("ScrapedWebsite/" + filteredDirectory, filteredFileName);
        }
        BufferedWriter writer =
                new BufferedWriter(new FileWriter(fileToBeWritten));

        BufferedReader readr =
                new BufferedReader(new InputStreamReader(url.openStream()));

        // read each line from stream till end
        String line;
        while ((line = readr.readLine()) != null) {
            writer.write(line);
        }
        successMessage(filteredFileName);
        readr.close();
        writer.close();
    }
}