package academy.everyonecodes.java.data;

public class RatingBuilder {
    private User user;
    private Activity activity;
    private int ratingValue;
    private String feedback;

    public RatingBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public RatingBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public RatingBuilder setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
        return this;
    }

    public RatingBuilder setFeedback(String feedback) {
        this.feedback = feedback;
        return this;
    }

    public Rating createRating() {
        return new Rating(user, activity, ratingValue, feedback);
    }
}