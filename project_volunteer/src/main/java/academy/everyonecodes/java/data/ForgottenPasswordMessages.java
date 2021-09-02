package academy.everyonecodes.java.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("passwordforgotten.messages")
public class ForgottenPasswordMessages {
	private String resetEmailSuccessfully;
	private String resetEmailNoUser;
	private String activateTokenSuccessfully;
	private String activateTokenInvalid;
	private String activateTokenFailure;
	private String changePasswordSuccessfully;
	private String changePasswordNoValidToken;
	private String changePasswordNotEnteredSameTwice;



	public ForgottenPasswordMessages() {}



	public String getResetEmailSuccessfully() {
		return resetEmailSuccessfully;
	}

	public void setResetEmailSuccessfully(String resetEmailSuccessfully) {
		this.resetEmailSuccessfully = resetEmailSuccessfully;
	}

	public String getResetEmailNoUser() {
		return resetEmailNoUser;
	}

	public void setResetEmailNoUser(String resetEmailNoUser) {
		this.resetEmailNoUser = resetEmailNoUser;
	}

	public String getActivateTokenSuccessfully() {
		return activateTokenSuccessfully;
	}

	public void setActivateTokenSuccessfully(String activateTokenSuccessfully) {
		this.activateTokenSuccessfully = activateTokenSuccessfully;
	}

	public String getActivateTokenInvalid() {
		return activateTokenInvalid;
	}

	public void setActivateTokenInvalid(String activateTokenInvalid) {
		this.activateTokenInvalid = activateTokenInvalid;
	}

	public String getActivateTokenFailure() {
		return activateTokenFailure;
	}

	public void setActivateTokenFailure(String activateTokenFailure) {
		this.activateTokenFailure = activateTokenFailure;
	}

	public String getChangePasswordSuccessfully() {
		return changePasswordSuccessfully;
	}

	public void setChangePasswordSuccessfully(String changePasswordSuccessfully) {
		this.changePasswordSuccessfully = changePasswordSuccessfully;
	}

	public String getChangePasswordNoValidToken() {
		return changePasswordNoValidToken;
	}

	public void setChangePasswordNoValidToken(String changePasswordNoValidToken) {
		this.changePasswordNoValidToken = changePasswordNoValidToken;
	}

	public String getChangePasswordNotEnteredSameTwice() {
		return changePasswordNotEnteredSameTwice;
	}

	public void setChangePasswordNotEnteredSameTwice(String changePasswordNotEnteredSameTwice) {
		this.changePasswordNotEnteredSameTwice = changePasswordNotEnteredSameTwice;
	}
}
