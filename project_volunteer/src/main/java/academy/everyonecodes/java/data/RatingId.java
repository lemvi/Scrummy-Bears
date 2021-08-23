package academy.everyonecodes.java.data;

import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {
	private User user;
	private Activity activity;

	public RatingId() {}

	public RatingId(User user, Activity activity)
	{
		this.user = user;
		this.activity = activity;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RatingId ratingId = (RatingId) o;
		return Objects.equals(user, ratingId.user) && Objects.equals(activity, ratingId.activity);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(user, activity);
	}
}
