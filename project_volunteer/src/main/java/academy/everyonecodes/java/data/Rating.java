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
	private int ratingValue;

	@Size(max = 800, message = "Maximum of 800 characters!")
	private String feedback;

	public Rating() {}

	public Rating(User user, Activity activity, int ratingValue, String feedback)
	{
		this.user = user;
		this.activity = activity;
		this.ratingValue = ratingValue;
		this.feedback = feedback;
	}

	public Rating(int ratingValue) {
		this.ratingValue = ratingValue;
	}

	public Rating(int ratingValue, String feedback) {
		this.ratingValue = ratingValue;
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

	public int getRatingValue()
	{
		return ratingValue;
	}

	public void setRatingValue(int ratingValue)
	{
		this.ratingValue = ratingValue;
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
		return getRatingValue() == rating1.getRatingValue() && Objects.equals(userId, rating1.userId) && Objects.equals(activityId, rating1.activityId) && Objects.equals(getUser(), rating1.getUser()) && Objects.equals(getActivity(), rating1.getActivity()) && Objects.equals(getFeedback(), rating1.getFeedback());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, activityId, getUser(), getActivity(), getRatingValue(), getFeedback());
	}
}
