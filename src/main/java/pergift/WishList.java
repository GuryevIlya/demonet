package pergift;

/**
 *
 * @author delet
 */
public class WishList {
    private int userVKId;
    private String href;
    private int postDate;

    public int getUserVKId() {
        return userVKId;
    }

    public String getHref() {
        return href;
    }

    public long getPostDate() {
        return postDate;
    }

    public void setUserVKId(int userVKId) {
        this.userVKId = userVKId;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setPostDate(int postDate) {
        this.postDate = postDate;
    }
    
    
}
