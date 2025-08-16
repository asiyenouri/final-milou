package milou;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "recipients", nullable = false, columnDefinition = "TEXT")
    private String recipientsStr;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "original_email_id")
    private String originalEmailId;

    @Column(name = "is_reply")
    private boolean isReply;

    @Column(name = "is_forward")
    private boolean isForward;

    public Email() {}

    public Email(String sender, List<String> recipients, String subject, String body) {
        this.id = Utils.generateRandomId(6);
        this.sender = sender;
        this.recipientsStr = String.join(",", recipients);
        this.subject = subject;
        this.body = body;
        this.date = new Date();
        this.isReply = false;
        this.isForward = false;
    }

    //replies
    public Email(String sender, List<String> recipients, String subject, String body, String originalEmailId) {
        this(sender, recipients, subject, body);
        this.originalEmailId = originalEmailId;
        this.isReply = true;
    }

    //forwards
    public Email(String sender, List<String> recipients, String subject, String body, String originalEmailId, boolean isForward) {
        this(sender, recipients, subject, body);
        this.originalEmailId = originalEmailId;
        this.isForward = isForward;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public List<String> getRecipients() {
        if (recipientsStr == null || recipientsStr.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(recipientsStr.split(","));
    }

    public void setRecipients(List<String> recipients) {
        this.recipientsStr = String.join(",", recipients);
    }

    public String getRecipientsAsString() {
        return recipientsStr;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getOriginalEmailId() { return originalEmailId; }
    public void setOriginalEmailId(String originalEmailId) { this.originalEmailId = originalEmailId; }

    public boolean isReply() { return isReply; }
    public void setReply(boolean reply) { isReply = reply; }

    public boolean isForward() { return isForward; }
    public void setForward(boolean forward) { isForward = forward; }

    public String getFormattedSubject() {
        if (isReply) {
            return "[Re] " + subject;
        } else if (isForward) {
            return "[Fw] " + subject;
        }
        return subject;
    }
}