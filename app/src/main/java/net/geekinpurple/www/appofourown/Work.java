package net.geekinpurple.www.appofourown;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Briannon on 2015-09-30.
 */
public class Work implements Parcelable {
    //region required 2x2 enums
    public interface EnumView {
        int getColor();
        String getSymbol();
    }

    public enum Rating implements EnumView {
        GENERAL (0xFF77a100, "G"),
        TEEN (0xFFe6d200, "T"),
        MATURE (0xFFe87604, "M"),
        EXPLICIT (0xFF990000, "E"),
        NOTRATED (0xFFf9f9f9, "N");

        private final int color;
        private final String symbol;
        Rating(int c, String s) {
            this.color = c;
            this.symbol = s;
        }

        public int getColor() { return color; }
        public String getSymbol() { return symbol; }
    }

    public enum Warnings implements EnumView {
        CHOOSENOTTO (0xFFe87604, "\u203D"),
        YES (0xFF990000, "!"),
        NO (0xFFFFFFFF, " "),
        EXTERNAL(0xFF026197, "\u2297");

        private final int color;
        private final String symbol;
        Warnings(int c, String s) {
            this.color = c;
            this.symbol = s;
        }

        public int getColor() { return color; }
        public String getSymbol() { return symbol; }
    }

    public enum Category implements EnumView {
        FEMSLASH (0xFFa80016, "\u2640"),
        HET (0xFF5e0a3c, "\u26A5"),
        SLASH (0xFF0146ad, "\u2642"),
        GEN (0xFF77a100, "\u2299"),
        OTHER(0xFF000000, "\u2645"),
        MULTI(0xFFe87604, "+"),
        NONE(0xFFFFFFFF, " ");

        private final int color;
        private final String symbol;
        Category(int c, String s) {
            this.color = c;
            this.symbol = s;
        }

        public int getColor() { return color; }
        public String getSymbol() { return symbol; }

    }

    public enum Status implements EnumView {
        NO (0xFF990000, "\u2298"),
        YES (0xFF77a100, "\u2713"),
        UNKNOWN (0xFFFFFFFF, " ");

        private final int color;
        private final String symbol;
        Status(int c, String s) {
            this.color = c;
            this.symbol = s;
        }

        public int getColor() { return color; }
        public String getSymbol() { return symbol; }
    }
    //endregion

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

        String urlString = MainActivity.HOME_URL;
        StringBuffer url = new StringBuffer(urlString);
        this.workUrl = url.append(workLinks.get(0).attr("href")).toString();
        this.author = workLinks.get(1).text();
        this.authorUrl = workLinks.get(1).attr("href");

        Element requiredTags = work.getElementsByClass("required-tags").get(0);

        // Parse for work's Rating
        this.rating = parseTag(Rating.class, requiredTags, "rating");

        // Parse for work's Warnings
        this.warnings = parseTag(Warnings.class, requiredTags, "warnings");

        // Parse for work's Category (Ships)
        this.category = parseTag(Category.class, requiredTags, "category");

        // Parse for work's Status
        this.status = parseTag(Status.class, requiredTags, "iswip");

        // Parse for work's Summary
        Elements sums = work.getElementsByClass("summary");
        if (sums.size() > 0) {
            Element desc = sums.get(0);
            this.summary = desc.text();
        }
        else {
            this.summary = "";
        }
        Log.d("testing", this.summary);

        //Parse for work's Tags
        Elements ships = work.getElementsByClass("relationships");
        if (ships.size() > 0) {
            for (Element e : ships) {

            }
        }
    }

    //region Parcelable implementation
    // Reconstruct the Work from the Parcel
    public Work(Parcel pkg) {
        workUrl = pkg.readString();
        title = pkg.readString();
        author = pkg.readString();
        authorUrl = pkg.readString();
        summary = pkg.readString();

        String temp = pkg.readString();
        rating = Rating.valueOf(temp);

        category = Category.valueOf(pkg.readString());

        temp = pkg.readString();
        warnings = Warnings.valueOf(temp);
        temp = pkg.readString();
        status = Status.valueOf(temp);
    }

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

    //region Utilities
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(title);
        str.append("\n(").append(author).append(")");

        return str.toString();
    }

    public void setView(EnumView e, TextView v) {
        v.setText(e.getSymbol());
        v.setTextColor(Color.WHITE);
        v.setBackgroundColor(e.getColor());
    }

    private <TTag extends Enum> TTag parseTag(Class<TTag> tagClass, Element requiredTags, String tag) {
        Element ele = requiredTags.getElementsByClass(tag).get(0);

        //parse Class
        String cl = ele.attr("class");
        String[] clAr = cl.split(" ");
        cl = clAr[0];
        clAr = cl.split("-");
        cl = clAr[1];

        return (TTag) Enum.valueOf(tagClass, cl.toUpperCase());
    }
    //endregion
}
