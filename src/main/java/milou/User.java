package milou;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "read_emails", columnDefinition = "TEXT")
    private String readEmailsStr;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = Utils.formatEmail(email);
        this.password = password;
        this.readEmailsStr = "";
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getReadEmails() {
        if (readEmailsStr == null || readEmailsStr.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(readEmailsStr.split(","));
    }

    public void markEmailAsRead(String emailId) {
        List<String> readEmails = getReadEmails();
        if (!readEmails.contains(emailId)) {
            readEmails.add(emailId);
            this.readEmailsStr = String.join(",", readEmails);
        }
    }

    public boolean hasReadEmail(String emailId) {
        return getReadEmails().contains(emailId);
    }
}