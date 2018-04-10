package nitezh.ministock.dataaccess;

/**
 * Created by nicholasfong on 2018-04-04.
 */

public class RssItem {
    private String title;
    private String description;
    private String link;

    public RssItem(){
        this.title = "";
        this.description = "";
        this.link = "";
    }

    public RssItem(String title, String desc, String link){
        this.title = title;
        this.description = desc;
        this.link = link;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getLink(){
        return this.link;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String desc){
        this.description = desc;
    }
    public void setLink(String link){
        this.link = link;
    }
    public String toString(){
        return this.title + " "
                + this.description + " "
                + this.link + " ";
    }
}
