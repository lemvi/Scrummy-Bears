package academy.everyonecodes.java.data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@IdClass(RatingId.class)
public class Rating {

	@Id
	@MapsId
	@ManyToOne
	@JoinColumn
	private User user;

	@Id
	@MapsId
	@ManyToOne
	@JoinColumn
	private Activity activity;

	@NotNull
	@Min(1)
	@Max(5)
	private int rating;

	@Size(max = 800, message = "Maximum of 800 characters!")
	private String feedback;

	public Rating() {}

	public Rating(User user, Activity activity, int rating, String feedback)
	{
		this.user = user;
		this.activity = activity;
		this.rating = rating;
		this.feedback = feedback;
	}

	public Rating(int rating) {
		this.rating = rating;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Activity getActivity()
	{
		return activity;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public int getRating()
	{
		return rating;
	}

	public void setRating(int rating)
	{
		this.rating = rating;
	}

	public String getFeedback()
	{
		return feedback;
	}

	public void setFeedback(String feedback)
	{
		this.feedback = feedback;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Rating rating1 = (Rating) o;
		return rating == rating1.rating && Objects.equals(user, rating1.user) && Objects.equals(activity, rating1.activity) && Objects.equals(feedback, rating1.feedback);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(user, activity, rating, feedback);
	}

	@Override
	public String toString()
	{
		return "Rating{" +
				"user=" + user +
				", activity=" + activity +
				", rating=" + rating +
				", feedback='" + feedback + '\'' +
				'}';
	}
}
