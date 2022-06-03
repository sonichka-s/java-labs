package usbot;
import java.net.MalformedURLException;
import java.net.URL;

public class URLDepthPair {
    private final URL url;
    private final int searchDepth;

    public URLDepthPair(String url, int searchDepth) throws MalformedURLException {
        this.url = new URL(url);
        this.searchDepth = searchDepth;
    }

    public URL getUrl() {
        return url;
    }

    public String getStringUrl() {
        return this.url.toString();
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public String getProtocol() {
        return url.getProtocol();
    }

    public String getHost() {
        return url.getHost();
    }

    public String getDirectory() {
        return url.getPath();
    }

    @Override
    public int hashCode() {
        return 29 * url.toString().length() + searchDepth;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || this.getClass() != obj.getClass())
            return false;

        URLDepthPair secObj = (URLDepthPair) obj;
        return (this.hashCode() == secObj.hashCode());
    }

    @Override
    public String toString() {
        return (url.toString() + " " + searchDepth);
    }
}