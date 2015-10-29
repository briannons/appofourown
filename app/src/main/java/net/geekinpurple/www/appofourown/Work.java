package net.geekinpurple.www.appofourown;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Briannon on 2015-09-30.
 */
public class Work implements Parcelable {
    public static enum Rating {G, T, M, E, N}
    public static enum Category {FF, FM, MM, GEN, OTHER, MULTI, NONE}
    public static enum Warnings {UNSPECIFIED, YES, NONE, EXTERNAL}
    public static enum Status {WIP, COMPLETE, UNKNOWN}

    protected String workUrl;
    protected String title;
    protected String author;
    protected String authorUrl;
    protected String summary;
    protected Rating rating;
    protected Category category;
    protected Warnings warnings;
    protected Status status;

    public Work(Element work) {
        Elements workLinks = work.getElementsByTag("a");
        this.title = workLinks.get(0).text(); // work title

        String urlString = MainActivity.homeUrl;
        StringBuffer url = new StringBuffer(urlString);
        this.workUrl = url.append(workLinks.get(0).attr("href")).toString();
        this.author = workLinks.get(1).text();
        this.authorUrl = workLinks.get(1).attr("href");

        // Parse for work's Rating
        String rate = work.getElementsByClass("rating").get(0).text();
        setRating(rate);

        // Parse for work's Category (Ships)
        String cat = work.getElementsByClass("category").get(0).text();
        setCategory(cat);

        // Parse for work's Warnings
        Elements warnings = work.getElementsByClass("warnings");
        for (Element warning : warnings) {
            String warn = warnings.get(0).toString();
            Log.d(title, warn);
        }

        /* String warn = warnings.get(0).toString();
        Log.d("testing", warn);
        switch (warn) {
            case "warning-no":
                this.warnings = Warnings.NONE;
                break;
            case "warning-yes":
                this.warnings = Warnings.YES;
                break;
            case "warning-choosenotto":
                this.warnings = Warnings.UNSPECIFIED;
                break;
            default:
                this.warnings = Warnings.EXTERNAL;
                break;
        } */
        this.warnings = Warnings.EXTERNAL;

        // Parse for work's Status
        this.status = Status.UNKNOWN;

        Element desc = work.getElementsByClass("summary").get(0);
        this.summary = desc.text();
    }

    // Reconstruct the Work from the Parcel
    public Work(Parcel pkg) {
        workUrl = pkg.readString();
        title = pkg.readString();
        author = pkg.readString();
        authorUrl = pkg.readString();
        summary = pkg.readString();

        String temp = pkg.readString();
        rating = Rating.valueOf(temp);
        Log.d("testing", rating.toString());

        category = Category.valueOf(pkg.readString());
        Log.d("testing", category.toString());

        temp = pkg.readString();
        warnings = Warnings.valueOf(temp);
        temp = pkg.readString();
        status = Status.valueOf(temp);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer(title);
        str.append("\n(").append(author).append(")");

        return str.toString();
    }

    //region Ratings
    protected void setRating(String rate) {
        switch (rate.toLowerCase()) {
            case "general audiences":
                this.rating = Rating.G;
                break;
            case "teen and up audiences":
                this.rating = Rating.T;
                break;
            case "mature":
                this.rating = Rating.M;
                break;
            case "explicit: only suitable for adults":
                this.rating = Rating.E;
                break;
            default:
                this.rating = Rating.N;
                break;
        }
    }

    protected int getRatingColor()
    {
        // {G, T, M, E, N}
        switch (rating) {
            case G:
                return 0xFF77a100;
            case T:
                return 0xFFe6d200;
            case M:
                return 0xFFe87604;
            case E:
                return 0xFF990000;
            default:
                return 0xFFf9f9f9;
        }
    }
    //endregion

    //region Category
    protected void setCategory(String cat) {
        switch (cat.toUpperCase()) {
            case "F/F":
                this.category = Category.FF;
                break;
            case "F/M":
                this.category = Category.FM;
                break;
            case "M/M":
                this.category = Category.MM;
                break;
            case "GEN":
                this.category = Category.GEN;
                break;
            case "NO CATEGORY":
                this.category = Category.NONE;
                break;
            case "OTHER":
                this.category = Category.OTHER;
                break;
            default:
                this.category = Category.MULTI;
                break;
        }
    }

    protected int getCategoryColor() {
        switch (category) {
            case FF:
                return 0xFFa80016;
            case FM:
                return 0xFF5e0a3c;
            case MM:
                return 0xFF0146ad;
            case GEN:
                return 0xFF77a100;
            case OTHER:
                return 0xFF000000;
            case MULTI:
                return 0xFFe87604;
            default:
                return 0xFFFFFFFF;
        }
    }

    protected String getCategorySymbol() {
        switch (category) {
            case FF:
                return "\u2640";
            case FM:
                return "\u26A5";
            case MM:
                return "\u2642";
            case GEN:
                return "\u2299";
            case OTHER:
                return "\u2645";
            case MULTI:
                return "+";
            default:
                return "";
        }
    }
    //endregion

    //region Warnings
    protected int getWarningsColor()
    {
        switch (warnings) {
            case UNSPECIFIED:
                return 0xFFe87604;
            case YES:
                return 0xFF990000;
            case EXTERNAL:
                return 0xFF026197;
            default:
                return 0xFFFFFFFF;
        }
    }

    protected String getWarningsSymbol()
    {
        switch (warnings) {
            case UNSPECIFIED:
                return "\u203D";
            case YES:
                return "!";
            case EXTERNAL:
                return "\u2297";
            default:
                return "";
        }
    }
    //endregion

    //region Status
    protected int getStatusColor() {
        switch (status) {
            case WIP:
                return 0xFF990000;
            case COMPLETE:
                return 0xFF77a100;
            default:
                return 0xFFFFFFFF;
        }
    }

    protected String getStatusSymbol() {
        switch (status) {
            case WIP:
                return "\u2298";
            case COMPLETE:
                return "\u2713";
            default:
                return "";
        }
    }
    //endregion

    //region Parcelable implementation
    @Override
    public int describeContents() {
        return hashCode();
    }

    // Construct the Parcel - must align in order w/ the reconstruct order
    @Override
    public void writeToParcel(Parcel pkg, int flags) {
        pkg.writeString(workUrl);
        pkg.writeString(title);
        pkg.writeString(author);
        pkg.writeString(authorUrl);
        pkg.writeString(summary);
        pkg.writeString(rating.name());
        pkg.writeString(category.name());
        pkg.writeString(warnings.name());
        pkg.writeString(status.name());
    }

    public static final Parcelable.Creator<Work> CREATOR = new Parcelable.Creator<Work>() {

        @Override
        public Work createFromParcel(Parcel parcel) {
            return new Work(parcel);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };
    //endregion
}
