package bg.autosalon.services;

import bg.autosalon.dao.impl.UserDao;
import bg.autosalon.entities.User;

public class UserService {

    private final UserDao userDao = new UserDao();

    public User login(String email, String password) {
        User user = userDao.findByEmail(email);

        if (user == null) {
            System.out.println("Error: User not found!");
            return null;
        }

        if (!user.getPassword().equals(password)) {
            System.out.println("Error: Invalid password!");
            return null;
        }

        return user;
    }

    public boolean register(User user) {
        if (userDao.findByEmail(user.getEmail()) != null) {
            System.out.println("Error: Email already taken!");
            return false;
        }

        userDao.save(user);
        return true;
    }
}
