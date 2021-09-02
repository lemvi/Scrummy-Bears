package academy.everyonecodes.java.data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PasswordToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
	private User user;

	private LocalDateTime creationTime;

	private boolean valid;

	private boolean active;



	public PasswordToken() {}

	public PasswordToken(String token, User user, LocalDateTime creationTime) {
		this.token = token;
		this.user = user;
		this.creationTime = creationTime;
		this.valid = true;
		this.active = false;
	}



	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public User getUser() {
		return user;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
