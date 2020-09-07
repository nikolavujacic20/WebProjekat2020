package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.OrganizationsIO;
import io.UserIO;
import main.CloudServiceProvider;
import model.Organization;
import model.Role;
import model.User;
import spark.Session;
import utility.Logging;

import java.util.ArrayList;
import java.util.Optional;

import static spark.Spark.*;

public class UsersController {
    private static final Gson g = new Gson();
    private static final UsersController instance = null;

    public static UsersController getInstance() {
        return Optional.ofNullable(instance).orElseGet(UsersController::new);
    }

    public void init() {
        get("rest/getAllUser", (req, res) -> {
            res.type("application/json");
            Session ss = req.session(true);
            User user = ss.attribute("user");
            if (user.getRole() == Role.SUPERADMIN)
                return g.toJson(CloudServiceProvider.users);
            else {
                ArrayList<User> adminUsers = new ArrayList<User>();
                for (User u : CloudServiceProvider.users)
                    if (u.getOrganization().getName().equals(user.getOrganization().getName())) {
                        adminUsers.add(u);
                    }
                return g.toJson(adminUsers);
            }
        });

        get("rest/getUser/:email", (req, res) -> {
            res.type("application/json");
            String email = req.params("email");
            User u = req.session().attribute("user");
            if (u.getRole() == Role.USER) {
                res.status(403);
                return "Forbidden";
            } else if (u.getRole() == Role.ADMIN) {
                for (User u1 : CloudServiceProvider.users) {
                    // ako je to taj user ali nije iz iste organizacije
                    if (u1.getEmail().equals(email) & !u1.getOrganization().getName().equals(u.getOrganization().getName())) {
                        res.status(403);
                        return "Forbidden";
                    }
                }
            }
            for (User user : CloudServiceProvider.users) {
                if (user.getEmail().equals(email))
                    return g.toJson(user);
            }
            return "";
        });

        put("rest/editUser/:email", (req, res) -> {
            res.type("application/json");
            User data = g.fromJson(req.body(), User.class);
            String email = req.params("email");
            for (User u : CloudServiceProvider.users) {
                if (u.getEmail().equals(email)) {
                    u.setName(data.getName());
                    u.setLastName(data.getLastName());
                    u.setPassword(data.getPassword());
                    u.setRole(data.getRole());
                    break;
                }
            }
            UserIO.toFile(CloudServiceProvider.users);
            return "";
        });

        delete("rest/deleteUser/:email", (req, res) -> {
            res.type("application/json");
            String email = req.params("email");

            Session ss = req.session(true);
            User user = ss.attribute("user");

            if (user.getEmail().equals(email)) {
                res.status(400);
                return "You cannot delete yourself";
            }

            for (User u : CloudServiceProvider.users) {
                if (u.getEmail().equals(email)) {
                    CloudServiceProvider.users.remove(u);
                    for (Organization o : CloudServiceProvider.organizations) {
                        o.getUsers().remove(u);
                    }
                    break;
                }
            }
            UserIO.toFile(CloudServiceProvider.users);
            OrganizationsIO.toFile(CloudServiceProvider.organizations);
            return "OK";
        });

        post("rest/addUser", (req, res) -> {
            res.type("application/json");
            User data = g.fromJson(req.body(), User.class);
            Session ss = req.session(true);
            User currentUser = ss.attribute("user");
            if (utility.Check.UserUnique(data.getEmail())) {
                if (currentUser.getRole() == Role.ADMIN) {
                    data.setOrganization(currentUser.getOrganization());

                }
                for (Organization o : CloudServiceProvider.organizations) {
                    if (o.getName().equals(data.getOrganization().getName())) {
                        data.setOrganization(o);
                        CloudServiceProvider.users.add(data);
                        o.addUser(data);
                        res.status(200);
                        UserIO.toFile(CloudServiceProvider.users);
                        OrganizationsIO.toFile(CloudServiceProvider.organizations);
                        return "OK";
                    }
                }
            }
            res.status(400);
            return "NIJE OK";
        });
        get("/rest/getRole", (req, res) -> {
            Session ss = req.session(true);
            User user = ss.attribute("user");

            if (user == null) {
                res.status(400);
                return "";
            } else {
                res.status(200);
                return user.getRole().toString();
            }
        });

        get("rest/getAccount", (req, res) -> {
            Session ss = req.session(true);
            User user = ss.attribute("user");

            if (user == null) {
                res.status(400);
                return "";
            } else {
                res.status(200);
                return g.toJson(user);
            }

        });

        delete("rest/deleteAcc/:id", (req, res) -> {
            res.type("application/json");
            String email = req.params("id");

            for (User u : CloudServiceProvider.users) {
                if (u.getEmail().equals(email)) {
                    CloudServiceProvider.users.remove(u);
                    for (Organization o : CloudServiceProvider.organizations) {
                        o.getUsers().remove(u);
                    }
                    req.session(true).invalidate();
                    break;
                }
            }
            UserIO.toFile(CloudServiceProvider.users);
            OrganizationsIO.toFile(CloudServiceProvider.organizations);
            return "OK";

        });

        put("rest/editAcc/:id", (req, res) -> {
            res.type("application/json");
            User data = g.fromJson(req.body(), User.class);
            String email = req.params("id");
            for (User u : CloudServiceProvider.users) {
                if (u.getEmail().equals(email)) {
                    if (!email.equals(data.getEmail()) && !utility.Check.UserUnique(data.getEmail())) {
                        res.status(400);
                        return "New email already has an account.";
                    }
                    u.setEmail(data.getEmail());
                    u.setName(data.getName());
                    u.setLastName(data.getLastName());
                    u.setPassword(data.getPassword());

                    break;
                }
            }
            UserIO.toFile(CloudServiceProvider.users);
            OrganizationsIO.toFile(CloudServiceProvider.organizations);
            res.status(200);
            return "OK";

        });

        get("rest/testSuperadmin", (req, res) -> {
            User u = req.session().attribute("user");
            if (u == null) {
                res.status(403);
                return "";
            }
            if (u.getRole() == Role.USER || u.getRole() == Role.ADMIN) {
                res.status(403);
                return "";
            }
            res.status(200);
            return "OK";
        });

        get("rest/testAdmin", (req, res) -> {
            User u = req.session().attribute("user");
            if (u == null) {
                res.status(403);
                return "";
            }
            if (u.getRole() == Role.USER || u.getRole() == Role.SUPERADMIN) {
                res.status(403);
                return "";
            }
            res.status(200);
            return "OK";
        });

        get("rest/testSuperadminAdmin", (req, res) -> {
            User u = req.session().attribute("user");
            if (u == null) {
                res.status(403);
                return "";
            }
            if (u.getRole() == Role.USER) {
                res.status(403);
                return "";
            }
            res.status(200);
            return "OK";
        });
    }
}
