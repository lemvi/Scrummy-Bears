package academy.everyonecodes.java.data;

import javax.persistence.*;

@Entity
public class InvalidLoginCount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	private User user;

	private int invalidAttempts;


	InvalidLoginCount() {}


	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public int getInvalidAttempts() {
		return invalidAttempts;
	}
}
