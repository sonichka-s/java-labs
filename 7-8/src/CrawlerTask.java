package usbot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTask implements Runnable {
    private static URLPool urlPool = null;
    private URLDepthPair currentPair;

    private static final String URL_PREFIX = "https://";
    private static final Pattern prefixPattern = Pattern.compile("^https://");

    private static final Pattern hrefPattern = Pattern.compile("<a href=\"([^\"]+)\"");

    private static final Pattern urlPattern = Pattern.compile("(?<=\")([^\"]+)(?=\".*)");

    private static final Pattern relPattern = Pattern.compile("/.*");

    CrawlerTask(URLPool gotPool) {
        urlPool = gotPool;
    }

    private void socketProcess(URLDepthPair urlDepthPair) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) urlDepthPair.getUrl().openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(100000);
        connection.setRequestMethod("GET");

        if (HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
            return;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            inputStreamHandler(bufferedReader);
        } finally {
            connection.disconnect();
        }
    }

    private void inputStreamHandler(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();

        while (line != null) {
            LinkedList<String> lineUrlPool;
            lineUrlPool = hrefHandler(line);

            if (lineUrlPool.isEmpty()) {
                line = bufferedReader.readLine();
                continue;
            }

            for (String url : lineUrlPool) {
                if (nonRelSiteHandler(url)) {
                    urlPool.putURL(new URLDepthPair(url, currentPair.getSearchDepth() + 1));
                } //else if (relSiteHandler(url)) {
//                    urlPool.putURL(new URLDepthPair(URL_PREFIX + currentPair.getHost() + url,
//                            currentPair.getSearchDepth() + 1));
//                }
            }

            line = bufferedReader.readLine();
        }
    }

    private static LinkedList<String> hrefHandler(String line) {
        Matcher hrefPatternMatcher = hrefPattern.matcher(line);

        LinkedList<String> urlList = new LinkedList<String>();
        while (hrefPatternMatcher.find()) {
            String url = urlHandler(hrefPatternMatcher.group());
            if (url != null) {
                urlList.add(url);
            }
        }

        return urlList;
    }

    private static boolean nonRelSiteHandler(String url) {
        Matcher prefixPatternMatcher = prefixPattern.matcher(url);
        return prefixPatternMatcher.find();
    }

    private static boolean relSiteHandler(String urlPart) {
        Matcher relMatcher = relPattern.matcher(urlPart);
        return relMatcher.find();
    }

    private static String urlHandler(String refLine) {
        Matcher urlPatternMatcher = urlPattern.matcher(refLine);
        String url = null;

        if (urlPatternMatcher.find()) {
            url = urlPatternMatcher.group();
        }

        return url;
    }

    @Override
    public void run() {
        while (true) {
            try {
                currentPair = urlPool.getURL();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                socketProcess(currentPair);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
