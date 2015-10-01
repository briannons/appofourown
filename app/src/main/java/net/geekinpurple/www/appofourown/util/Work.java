package net.geekinpurple.www.appofourown.util;

import android.util.Log;

import net.geekinpurple.www.appofourown.MainActivity;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Briannon on 2015-09-30.
 */
public class Work {
    public static enum Rating {G, T, M, E, N}
    public static enum Category {FF, FM, MM, GEN, OTHER, MULTI, NONE}
    public static enum Warnings {UNSPECIFIED, YES, NONE, EXTERNAL}
    public static enum Status {WIP, COMPLETE, UNKNOWN}

    private String workUrl;
    private String title;
    private String author;
    private String authorUrl;
    private String summary;
    private Rating rating;
    private Category category;
    private Warnings warnings;
    private Status state;

    public Work() {}
    public Work(Element work) {
        Elements workLinks = work.getElementsByTag("a");
        this.title = workLinks.get(0).text(); // work title

        StringBuffer url = new StringBuffer(MainActivity.homeUrl);
        this.workUrl = url.append(workLinks.get(0).attr("href")).toString();
        this.author = workLinks.get(1).text();
        this.authorUrl = workLinks.get(1).attr("href");

        String rate = work.getElementsByClass("rating").get(0).text();
        switch (rate) {
            case "General Audiences":
                this.rating = Rating.G;
                break;
            case "Teen And Up Audiences":
                this.rating = Rating.T;
                break;
            case "Mature":
                this.rating = Rating.M;
                break;
            case "Explicit: only suitable for adults":
                this.rating = Rating.E;
                break;
            default:
                this.rating = Rating.N;
                break;
        }
        Log.d("rating", this.rating.toString());

        String cat = work.getElementsByClass("category").get(0).text();
        switch (cat) {
            case "F/F":
                this.category = Category.FF;
                break;
            case "F/M":
                this.category = Category.FM;
                break;
            case "M/M":
                this.category = Category.MM;
                break;
            case "Gen":
                this.category = Category.GEN;
                break;
            case "No category":
                this.category = Category.NONE;
                break;
            case "Other":
                this.category = Category.OTHER;
                break;
            default:
                this.category = Category.MULTI;
                break;
        }
        Log.d("category", this.category.toString());

        Element desc = work.getElementsByClass("summary").get(0);
        this.summary = desc.text();
    }

    @Override
    public String toString() {
       /* StringBuffer str = new StringBuffer(title);
        str.append("\n").append(author).append("\n");
        str.append(summary);*/


        return this.title;
    }
}
