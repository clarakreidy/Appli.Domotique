public class Room {
    private Integer Id;
    private String Url;
    private String Name;

    public Room(Integer id, String url, String name) {
        Id = id;
        Url = url;
        Name = name;
    }

    public Integer getId() {
        return Id;
    }

    public String getUrl() {
        return Url;
    }

    public String getName() {
        return Name;
    }
}
