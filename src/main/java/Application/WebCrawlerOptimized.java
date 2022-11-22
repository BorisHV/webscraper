package Application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebCrawlerOptimized implements WebCrawler {
	
	private ConcurrentLinkedQueue<String> queue;
	
	private Set<String> visited;
	
	public WebCrawlerOptimized() {
		
		File baseFolder = new File("ScrapedWebsite");
		baseFolder.mkdirs();
	}
	
	@Override
	public void crawl(String initialUrl) {
		
		queue = new ConcurrentLinkedQueue<>();
		visited = new HashSet<>();
		
		queue.add(initialUrl);

		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		while (!queue.isEmpty()) {
			String nextUrl = queue.remove();
			if (!visited.contains(nextUrl)) {
				
				List<String> urls = getUrls(nextUrl);
				
				executor.execute(() -> scrape(nextUrl, initialUrl));
				
				visited.add(nextUrl);
				for (String url : urls) {
					if (!queue.contains(url) && !visited.contains(url) && url.startsWith(initialUrl))
						queue.add(url);
				}
				
				System.out.println("Visited size: " + visited.size());
			}
		}
		
		executor.shutdown();
		try {
			if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
			System.out.println("Exception during executor shutdown: " + e.getMessage());
		}
		
	}
	
	private void scrape(String pathFromCrawler, String initialUrl) {
		
		new WebScraperOptimized().scrape(pathFromCrawler, initialUrl);
	}
	
	
	private List<String> getUrls(String url) {
		
		List<String> list = new ArrayList<>();
//		if (url.endsWith("jpg")) {
//			return list;
//		}
		
		try {
			Document doc = Jsoup.connect(url).ignoreContentType(true).get();
			Elements availableImgsOnPage = doc.select("img[src]");
			Elements availableCssOnPage = doc.select("link[href]");
			Elements availableScriptOnPage = doc.select("script[src]");
			Elements availableLinksOnPage = doc.select("a[href]");
			
			for (Element ele1 : availableImgsOnPage)
				list.add(ele1.attr("abs:src"));
			for (Element ele2 : availableCssOnPage)
				list.add(ele2.attr("abs:href"));
			for (Element ele3 : availableScriptOnPage)
				list.add(ele3.attr("abs:src"));
			for (Element ele4 : availableLinksOnPage)
				list.add(ele4.attr("abs:href"));
		} catch (IOException e) {
			System.err.println("For '" + url + "': " + e.getMessage());
		}
		
		return list;
	}
	
}
