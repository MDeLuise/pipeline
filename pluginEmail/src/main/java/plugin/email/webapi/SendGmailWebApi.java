package plugin.email.webapi;

import com.sun.mail.smtp.SMTPTransport;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import md.dev.pipeline.exception.PipelineGenericException;
import md.dev.webapi.AbstractWebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import plugin.email.action.gmail.authentication.OAuth2Authenticator;
import plugin.email.action.gmail.authentication.OAuth2TokenHandler;

import java.io.UnsupportedEncodingException;

public class SendGmailWebApi extends AbstractWebApi {
    private String clientId;
    private String clientSecret;
    private String to;
    private String from;
    private String subject;
    private String text;
    private OAuth2TokenHandler tokenHandler;


    @Override
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    public WebApiResponse perform() {
        final String TOKEN = tokenHandler.getToken();
        OAuth2Authenticator.initialize();
        SMTPTransport smtp;
        try {
            smtp = OAuth2Authenticator.connectToSmtp(
                    "smtp.gmail.com",
                    587,
                    from,
                    TOKEN,
                    false // true to debug enable
            );
        } catch (Exception e) {
            LOG.error("error while creating smtp object", e);
            throw new PipelineGenericException(e.getMessage());
        }
        try {
            MimeMessage message = new MimeMessage(OAuth2Authenticator.session);
            message.setFrom(new InternetAddress(from, from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(text, "text/html");
            smtp.sendMessage(message, new Address[]{new InternetAddress(to)});
        } catch (UnsupportedEncodingException | MessagingException e) {
            LOG.error("error while creating message to send", e);
            throw new PipelineGenericException(e.getMessage());
        }

        return new WebApiResponse(200, "");
    }

    @Override
    public void configure(WebApiConfiguration configuration) {
        if (configuration.contains("clientId")) {
            clientId = (String) configuration.get("clientId");
        }
        if (configuration.contains("clientSecret")) {
            clientSecret = (String) configuration.get("clientSecret");
        }
        if (configuration.contains("to")) {
            to = (String) configuration.get("to");
        }
        if (configuration.contains("from")) {
            from = (String) configuration.get("from");
        }
        if (configuration.contains("subject")) {
            subject = (String) configuration.get("subject");
        }
        if (configuration.contains("text")) {
            text = (String) configuration.get("text");
        }
        if (configuration.contains("tokenHandler")) {
            tokenHandler = (OAuth2TokenHandler) configuration.get("tokenHandler");
        }
    }
}
