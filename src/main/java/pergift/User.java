
package pergift;

/**
 *
 * @author delet
 */
public class User {
    private Integer id;
    private String name;
    private String sername;
    private String about;

    public User(Integer id){
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSername() {
        return sername;
    }

    public String getAbout() {
        return about;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSername(String sername) {
        this.sername = sername;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
