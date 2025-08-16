package milou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {
    private Map<String, User> users = new HashMap<>();
    private User currentUser;

    public boolean register(String name, String email, String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        String formattedEmail = Utils.formatEmail(email);
        if (users.containsKey(formattedEmail)) {
            throw new IllegalArgumentException("Email already exists. Please choose another one.");
        }
        User newUser = new User(name, formattedEmail, password);
        users.put(formattedEmail, newUser);
        return true;
    }
    public boolean login(String email, String password) {
        String formattedEmail = Utils.formatEmail(email);
        User user = users.get(formattedEmail);

        if (user == null) {
            throw new IllegalArgumentException("User not found. Please check your email.");
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password. Please try again.");
        }

        currentUser = user;
        return true;
    }
    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isEmailTaken(String email) {
        return users.containsKey(Utils.formatEmail(email));
    }
}