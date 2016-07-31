package ar.com.thomas.mydailynews.model;

/**
 * Created by alejandrothomas on 6/26/16.
 */
public class RSSFeedCategory {
    private String categoryName;
    private String objectId;

    public RSSFeedCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public RSSFeedCategory() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
