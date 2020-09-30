package no.ntnu.backend.pentbrukt.Email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Controller;

@Controller
public class MailController {

    private EmailConfig emailConfig;

    public MailController(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public void sendMail() {

        // Build a mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort((this.emailConfig.getPort()));
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        // Build an email
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("melding@pentbrukt.no");
        simpleMailMessage.setTo("user@pentbrukt.no");
        simpleMailMessage.setSubject("Noen kjøpte dingsen din!");
        simpleMailMessage.setText("Noen kjøpte dingsen din! Lorem ipsum lorem ipsun");

        // Send email
        mailSender.send(simpleMailMessage);

    }


}
