package academy.everyonecodes.java.data.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

public class PasswordDTO {

	@NotEmpty
	@Size(min = 1, max = 30, message = "Must have 1-30 characters")
	private String username;

	@NotEmpty
	@Size(min = 1, max = 100, message = "Must have 1-100 characters")
	private String newPassword;

	@NotEmpty
	@Size(min = 1, max = 100, message = "Must have 1-100 characters")
	private String newPasswordRepeat;



	public PasswordDTO() {}

	public PasswordDTO(String username, String newPassword, String newPasswordRepeat) {
		this.username = username;
		this.newPassword = newPassword;
		this.newPasswordRepeat = newPasswordRepeat;
	}



	public String getUsername() {
		return username;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getNewPasswordRepeat() {
		return newPasswordRepeat;
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PasswordDTO that = (PasswordDTO) o;
		return Objects.equals(username, that.username) && Objects.equals(newPassword, that.newPassword) && Objects.equals(newPasswordRepeat, that.newPasswordRepeat);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, newPassword, newPasswordRepeat);
	}
}
