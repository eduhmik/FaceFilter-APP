package com.example.facefilter.model;

public class TrendingFilters {
    private String id;
    private String title;
    private String embedUrl;

    public String getEmbedUrl() {
        return embedUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCannonical() {
        return cannonical;
    }

    public String getUrl() {
        return url;
    }

    private String cannonical;
    private String url;
}
