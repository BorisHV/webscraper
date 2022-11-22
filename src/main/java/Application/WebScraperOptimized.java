package Application;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class WebScraperOptimized implements WebScraper {
	
	private File fileToBeWritten;
	
	private File imgToBeWritten;
	
	private String filteredDirectory;
	
	@Override
	public void scrape(String pathFromCrawler, String initialUrl) {
		
		try {
			System.out.println("Scraping page: " + pathFromCrawler);
			// Create URL object
			URL url = new URL(pathFromCrawler);
			
			String shortenedPath = pathFromCrawler.substring(initialUrl.length());
			
			String filteredFileName = saveDirectory(shortenedPath);
			
			if (filteredFileName.endsWith(".jpg")) {
				saveJpg(url, filteredFileName);
			} else {
				saveFile(url, filteredFileName);
			}
		}
		
		// Exceptions
		catch (MalformedURLException mue) {
			System.out.println("Malformed URL Exception raised");
		} catch (IOException ie) {
			System.out.println("IOException, empty path");
		}
	}
	
	private String saveDirectory(String pathFromCrawler) {
		
		if (pathFromCrawler.isEmpty()) {
			filteredDirectory = "";
			return "";
		}
		
		int indexToFilterDirectory = pathFromCrawler.lastIndexOf('/');
		int indexToFilterFileName = pathFromCrawler.length();
		
		String filteredFileName = pathFromCrawler.substring(indexToFilterDirectory + 1, indexToFilterFileName);
		
		try {
			filteredDirectory = pathFromCrawler.substring(0, indexToFilterDirectory);
			File file = new File("ScrapedWebsite", filteredDirectory);
			file.mkdirs();
			showSuccessMessage(filteredDirectory);
		} catch (StringIndexOutOfBoundsException sioobe) {
			//If there's no directory
			System.out.println("StringIndexOutOfBoundsException = No directory needed.");
			filteredDirectory = "";
		}
		return filteredFileName;
	}
	
	private void saveJpg(URL url, String filteredFileName) throws IOException {
		
		if (filteredDirectory.equals("")) {
			imgToBeWritten = new File("ScrapedWebsite", filteredFileName);
		} else {
			imgToBeWritten = new File("ScrapedWebsite/" + filteredDirectory, filteredFileName);
		}
		BufferedImage image = null;
		image = ImageIO.read(url);
		ImageIO.write(image, "jpg", new File(String.valueOf(imgToBeWritten)));
		showSuccessMessage(filteredFileName);
	}
	
	private void saveFile(URL url, String filteredFileName) throws IOException {
		
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
		showSuccessMessage(filteredFileName);
		readr.close();
		writer.close();
	}
	
	private void showSuccessMessage(String created) {
		
		String message = String.format("Created: '%s'", created);
		System.out.println(message);
	}
	
}
