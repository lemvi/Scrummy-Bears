package academy.everyonecodes.java.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


@Configuration
public class MailServerConfiguration {

    private String host;
    private int port;
    private String username;
    private String password;

    public MailServerConfiguration(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    //@Value("#{${spring.mail.properties}}")
    //private Map<String,String> properties;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);


        Properties props = mailSender.getJavaMailProperties();
        /*
        var r = properties.entrySet().stream()

                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

         */

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
