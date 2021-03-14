package two.sofya.karp.model;

public class User {
  private Integer id;
  private String title;
  private String context;

  public User(Integer id, String title, String context) {
    this.id = id;
    this.title = title;
    this.context = context;
  }

  public User(){
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }
}
