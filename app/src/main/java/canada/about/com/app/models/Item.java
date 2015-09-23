package canada.about.com.app.models;

/**
 * Created by user on 22/9/2015.
 */
public class Item {

    private String title;
    private String slugTitle;
    private String urlThumb;

    public String getUrlThumb() {
        return urlThumb;
    }

    public String getTitle() {
        return title;
    }

    public String getSlugTitle() {
        return slugTitle;
    }

    public Item(String title, String slugTitle, String urlThumb) {
        this.title = title;
        this.slugTitle = slugTitle;
        this.urlThumb = urlThumb;
    }

}
