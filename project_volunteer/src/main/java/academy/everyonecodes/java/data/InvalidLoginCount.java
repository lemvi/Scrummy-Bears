package academy.everyonecodes.java.data;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class InvalidLoginCount {

	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private User user;

	private int invalidAttempts;


	InvalidLoginCount() {}

	public InvalidLoginCount(User user, int invalidAttempts) {
		this.user = user;
		this.invalidAttempts = invalidAttempts;
	}


	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public int getInvalidAttempts() {
		return invalidAttempts;
	}

	public void setInvalidAttempts(int invalidAttempts) {
		this.invalidAttempts = invalidAttempts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InvalidLoginCount that = (InvalidLoginCount) o;
		return invalidAttempts == that.invalidAttempts && Objects.equals(id, that.id) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, user, invalidAttempts);
	}
}
