package milou;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Services {
    public User signUp(String name, String email, String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }
        email = Utils.formatEmail(email);
        if (findUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User user = new User(name, email, password);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            return user;
        }
    }

    public User login(String email, String password) {
        email = Utils.formatEmail(email);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email AND password = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.uniqueResult();
        }
    }

    public User findUserByEmail(String email) {
        email = Utils.formatEmail(email);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }

    public Email sendEmail(String senderEmail, List<String> recipients, String subject, String body) {
        recipients = recipients.stream().map(Utils::formatEmail).collect(Collectors.toList());
        Email email = new Email(senderEmail, recipients, subject, body);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(email);
            tx.commit();
            return email;
        }
    }

    public Email replyToEmail(String senderEmail, String originalEmailId, String body) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Email originalEmail = session.get(Email.class, originalEmailId);
            if (originalEmail == null) {
                throw new IllegalArgumentException("Original email not found.");
            }
            List<String> recipients = new ArrayList<>(originalEmail.getRecipients());
            if (!recipients.contains(senderEmail)) {
                recipients.add(originalEmail.getSender());
            } else {
                recipients.remove(senderEmail);
                recipients.add(originalEmail.getSender());
            }
            Email replyEmail = new Email(senderEmail, recipients, originalEmail.getSubject(), body, originalEmailId);
            Transaction tx = session.beginTransaction();
            session.save(replyEmail);
            session.get(User.class, findUserByEmail(senderEmail).getId()).markEmailAsRead(originalEmailId);
            tx.commit();
            return replyEmail;
        }
    }

    public Email forwardEmail(String senderEmail, String originalEmailId, List<String> recipients) {
        recipients = recipients.stream().map(Utils::formatEmail).collect(Collectors.toList());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Email originalEmail = session.get(Email.class, originalEmailId);
            if (originalEmail == null) {
                throw new IllegalArgumentException("Original email not found.");
            }
            Email forwardEmail = new Email(senderEmail, recipients, originalEmail.getSubject(), originalEmail.getBody(), originalEmailId, true);
            Transaction tx = session.beginTransaction();
            session.save(forwardEmail);
            session.get(User.class, findUserByEmail(senderEmail).getId()).markEmailAsRead(originalEmailId);
            tx.commit();
            return forwardEmail;
        }
    }

    public List<Email> getUnreadEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = findUserByEmail(userEmail);
            if (user == null) return Collections.emptyList();
            Query<Email> query = session.createQuery(
                    "FROM Email WHERE sender = :userEmail OR recipientsStr LIKE :pattern",
                    Email.class);
            query.setParameter("userEmail", userEmail);
            query.setParameter("pattern", "%" + userEmail + "%");

            List<Email> emails = query.list().stream()
                    .filter(email -> !user.hasReadEmail(email.getId()))
                    .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate()))
                    .collect(Collectors.toList());
            return emails;
        }
    }

    public List<Email> getAllEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Email> query = session.createQuery(
                    "FROM Email WHERE sender = :userEmail OR recipientsStr LIKE :pattern",
                    Email.class);
            query.setParameter("userEmail", userEmail);
            query.setParameter("pattern", "%" + userEmail + "%");
            return query.list().stream()
                    .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate()))
                    .collect(Collectors.toList());
        }
    }

    public List<Email> getSentEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Email> query = session.createQuery("FROM Email WHERE sender = :userEmail", Email.class);
            query.setParameter("userEmail", userEmail);
            return query.list().stream()
                    .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate()))
                    .collect(Collectors.toList());
        }
    }

    public Email getEmailByCode(String emailId, String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Email email = session.get(Email.class, emailId);
            if (email == null) {
                throw new IllegalArgumentException("Email not found with ID: " + emailId);
            }

            boolean canRead = email.getSender().equals(userEmail) ||
                    email.getRecipients().contains(userEmail);

            if (!canRead) {
                throw new IllegalArgumentException("You don't have permission to read this email.");
            }

            Transaction tx = session.beginTransaction();
            try {
                User user = findUserByEmail(userEmail);
                if (user != null) {
                    user.markEmailAsRead(emailId);
                    session.update(user);
                    tx.commit();
                }
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }

            return email;
        }
    }

    public void deleteEmail(String emailId, String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Email email = session.get(Email.class, emailId);
            if (email == null || (!email.getSender().equals(userEmail) && !email.getRecipients().contains(userEmail))) {
                throw new IllegalArgumentException("You cannot delete this email.");
            }
            Transaction tx = session.beginTransaction();
            session.delete(email);
            tx.commit();
        }
    }
}