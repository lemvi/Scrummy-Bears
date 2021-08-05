package academy.everyonecodes.java.data;

import javax.persistence.*;

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
}
