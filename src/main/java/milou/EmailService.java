package milou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmailService {
    private Map<String, Email> emails = new HashMap<>();
    private AuthService authService;

    public EmailService(AuthService authService) {
        this.authService = authService;
    }

    public Email sendEmail(List<String> recipients, String subject, String body) {
        User sender = authService.getCurrentUser();
        if (sender == null) {
            throw new IllegalStateException("You must be logged in to send emails.");
        }

        List<String> formattedRecipients = recipients.stream()
                .map(Utils::formatEmail)
                .collect(Collectors.toList());

        // Create email
        Email email = new Email(sender.getEmail(), formattedRecipients, subject, body);
        emails.put(email.getId(), email);
        return email;
    }

    public Email replyToEmail(String originalEmailId, String body) {
        User sender = authService.getCurrentUser();
        if (sender == null) {
            throw new IllegalStateException("You must be logged in to reply to emails.");
        }

        Email originalEmail = emails.get(originalEmailId);
        if (originalEmail == null) {
            throw new IllegalArgumentException("Email not found.");
        }

        // Check if current user is recipient of the original email
        if (!originalEmail.getRecipients().contains(sender.getEmail()) &&
                !originalEmail.getSender().equals(sender.getEmail())) {
            throw new IllegalArgumentException("You cannot reply to this email.");
        }

        // Create recipients list
        List<String> recipients = new ArrayList<>();
        recipients.add(originalEmail.getSender());
        recipients.addAll(originalEmail.getRecipients().stream()
                .filter(r -> !r.equals(sender.getEmail()))
                .collect(Collectors.toList()));

        // reply email
        Email reply = new Email(
                sender.getEmail(),
                recipients,
                originalEmail.getSubject(),
                body,
                originalEmailId
        );
        emails.put(reply.getId(), reply);
        return reply;
    }

    public Email forwardEmail(String originalEmailId, List<String> newRecipients) {
        User sender = authService.getCurrentUser();
        if (sender == null) {
            throw new IllegalStateException("You must be logged in to forward emails.");
        }

        Email originalEmail = emails.get(originalEmailId);
        if (originalEmail == null) {
            throw new IllegalArgumentException("Email not found.");
        }

        // Check if current user is a recipient of the original email
        if (!originalEmail.getRecipients().contains(sender.getEmail()) &&
                !originalEmail.getSender().equals(sender.getEmail())) {
            throw new IllegalArgumentException("You cannot forward this email.");
        }

        // Format new recipients
        List<String> formattedRecipients = newRecipients.stream()
                .map(Utils::formatEmail)
                .collect(Collectors.toList());

        //forwarded email
        Email forwarded = new Email(
                sender.getEmail(),
                formattedRecipients,
                originalEmail.getSubject(),
                originalEmail.getBody(),
                originalEmailId,
                true
        );
        emails.put(forwarded.getId(), forwarded);
        return forwarded;
    }

    public List<Email> getInboxEmails(User user) {
        return emails.values().stream()
                .filter(e -> e.getRecipients().contains(user.getEmail()))
                .sorted(Comparator.comparing(Email::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Email> getUnreadEmails(User user) {
        return getInboxEmails(user).stream()
                .filter(e -> !user.hasReadEmail(e.getId()))
                .collect(Collectors.toList());
    }
    public List<Email> getSentEmails(User user) {
        return emails.values().stream()
                .filter(e -> e.getSender().equals(user.getEmail()))
                .sorted(Comparator.comparing(Email::getDate).reversed())
                .collect(Collectors.toList());
    }
    public Email getEmailById(String emailId) {
        return emails.get(emailId);
    }
    public boolean canUserReadEmail(User user, String emailId) {
        Email email = emails.get(emailId);
        if (email == null) return false;

        return email.getSender().equals(user.getEmail()) ||
                email.getRecipients().contains(user.getEmail());
    }
}