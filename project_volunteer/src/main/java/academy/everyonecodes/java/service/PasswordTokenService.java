package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.PasswordToken;
import academy.everyonecodes.java.data.repositories.PasswordTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordTokenService {
	private final PasswordTokenRepository passwordTokenRepository;



	public PasswordTokenService(PasswordTokenRepository passwordTokenRepository) {
		this.passwordTokenRepository = passwordTokenRepository;
	}



	public PasswordToken save(PasswordToken passwordToken) {
		return passwordTokenRepository.save(passwordToken);
	}

	public Optional<PasswordToken> findByToken(String token) {
		return passwordTokenRepository.findByToken(token);
	}

	public Optional<PasswordToken> findByUser_usernameAndValidAndActive(String username) {
		return passwordTokenRepository.findByUser_usernameAndValidAndActive(username, true, true);
	}

	public List<PasswordToken> findByUser_usernameAndValid(String username) {
		return passwordTokenRepository.findByUser_usernameAndValid(username, true);
	}
}
