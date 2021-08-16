package academy.everyonecodes.java.data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@IdClass(RatingId.class)
public class Rating {

	@Id
	@MapsId
	@ManyToOne
	@JoinColumn
	private User user;

	@Id
	private Long eventId;

	@Min(1)
	@Max(5)
	private int rating;


	Rating() {}

	public Rating(int rating) {
		this.rating = rating;
	}

	public Long getUserId() {
		return user.getId();
	}

	public Long getEventId() {
		return eventId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
