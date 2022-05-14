package edu.uoc.epcsd.notification.services;

import edu.uoc.epcsd.notification.pojos.Category;
import edu.uoc.epcsd.notification.pojos.Show;
import edu.uoc.epcsd.notification.pojos.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NotificationService {
    @Autowired
    private UserService userService;    // mock service

    public void notifyShowCreation(Show show) {

        Category category = show.getCategory();
        for (User user : userService.getUsersByFavouriteCategory(category)) {
            notifyUser(user, show);
        }
    }

    public Iterable<User> notifyShowById(Long showId) {
        Show show = new Show();
        show.setId(showId);

        Category category = show.getCategory();
        Iterable<User> users = userService.getUsersByFavouriteCategory(category);
        for (User user : users) {
            notifyUser(user, show);
        }
        return users;
    }

    // mock notification
    private void notifyUser(User user, Show show) {
        // send email / push notification / etc.
        log.info("Show \"" + show.getName() + "\" added!. Notifying the user \"" + user.getFullName() + "\"");
    }
}
