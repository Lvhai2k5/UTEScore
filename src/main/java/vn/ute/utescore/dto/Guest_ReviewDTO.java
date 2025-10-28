package vn.ute.utescore.dto;
import java.time.LocalDate;

public class Guest_ReviewDTO {
	private Long id;
    private String userName;
    private int userAge;
    private double rating;
    private String content;
    private String image;
    private LocalDate date;

    public Guest_ReviewDTO() {}

    public Guest_ReviewDTO(Long id, String userName, int userAge,
                     double rating, String content, String image, LocalDate date) {
        this.id = id;
        this.userName = userName;
        this.userAge = userAge;
        this.rating = rating;
        this.content = content;
        this.image = image;
        this.date = date;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public int getUserAge() { return userAge; }
    public void setUserAge(int userAge) { this.userAge = userAge; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

}
