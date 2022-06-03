package usbot;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class URLPool {
    private final LinkedList<URLDepthPair> poolOfURL;
    private final LinkedList<URLDepthPair> processedURL;

    private static int MAX_DEPTH;

    private static int processesCounter = 0;

    URLPool(int maxDepth) {
        poolOfURL = new LinkedList<URLDepthPair>();
        processedURL = new LinkedList<URLDepthPair>();
        MAX_DEPTH = maxDepth;
    }

    public synchronized int getProcessesCounter() {
        return processesCounter;
    }

    public URLDepthPair getURL() throws InterruptedException {
        synchronized (poolOfURL) {
            if (poolOfURL.isEmpty()) {
                processesCounter++;
                poolOfURL.wait();
                processesCounter--;
            }

            URLDepthPair firstUrlBufferPair = poolOfURL.getFirst();
            synchronized (processedURL) {
                processedURL.add(firstUrlBufferPair);
            }

            poolOfURL.removeFirst();
            return firstUrlBufferPair;
        }
    }

    public void putURL(URLDepthPair pair) {
        synchronized (poolOfURL) {
            if (pair.getSearchDepth() < MAX_DEPTH && !processedURL.contains(pair)) {
                poolOfURL.add(pair);
                if (processesCounter > 0)
                    poolOfURL.notify();
            } else {
                synchronized (processedURL) {
                    processedURL.add(pair);
                }
            }
        }
    }

    public void getSites() {
        System.out.println("All sites:");

        for (URLDepthPair currentPair : processedURL) {
            System.out.println(currentPair.toString());
        }
    }
}
