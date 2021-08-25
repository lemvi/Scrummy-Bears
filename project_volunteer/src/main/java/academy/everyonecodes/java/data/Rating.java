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
	@Column(name = "user_id", insertable = false, updatable = false)
	private Long userId;

	@Id
	@Column(name = "activity_id", insertable = false, updatable = false)
	private Long activityId;

	@MapsId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;


	@MapsId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_id")
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Rating rating1 = (Rating) o;
		return getRating() == rating1.getRating() && Objects.equals(userId, rating1.userId) && Objects.equals(activityId, rating1.activityId) && Objects.equals(getUser(), rating1.getUser()) && Objects.equals(getActivity(), rating1.getActivity()) && Objects.equals(getFeedback(), rating1.getFeedback());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, activityId, getUser(), getActivity(), getRating(), getFeedback());
	}
}
