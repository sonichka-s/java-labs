package usbot;
import java.io.IOException;
import java.util.LinkedList;

public class Main extends Thread {
    public static URLPool urlPool;

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 3)
            System.out.println("BAD FORMAT. Required format\njava Crawler <URL> <search depth> <streams number>");

        String firstURL = null;
        int maxDepth = 0, streamsNum = 0;

        try {
            firstURL = args[0];
            maxDepth = Integer.parseInt(args[1]);
            streamsNum = Integer.parseInt(args[2]);
        } catch (NumberFormatException nfe) {
            System.out.println("The second and third arguments must be an integer. Required format\njava" +
                    " Crawler <String> <int> <int>");
            System.exit(1);
        }

        if (maxDepth == 0 || streamsNum == 0) {
            System.out.println("All sites:\n" +
                    firstURL + " " + 0);

            System.exit(1);
        }

        urlPool = new URLPool(maxDepth);
        urlPool.putURL(new URLDepthPair(firstURL, 0));

        LinkedList<Thread> streams = new LinkedList<>();

        for (int i = 0; i < streamsNum; ++i) {
            streams.add(new Thread(new CrawlerTask(urlPool)));
        }

        for (Thread stream : streams) {
            stream.start();
        }

        while (urlPool.getProcessesCounter() != streamsNum) {
            sleep(1000);
        }

        urlPool.getSites();
        System.exit(0);
    }
}