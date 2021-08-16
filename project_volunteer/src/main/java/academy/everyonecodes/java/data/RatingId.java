package academy.everyonecodes.java.data;

import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {
	private User user;
	private Long eventId;

	RatingId() {}

	public RatingId(User user, Long eventId) {
		this.user = user;
		this.eventId = eventId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RatingId ratingId = (RatingId) o;
		return Objects.equals(user, ratingId.user) && Objects.equals(eventId, ratingId.eventId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, eventId);
	}
}
