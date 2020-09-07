package main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controller.DiscController;
import controller.OrgController;
import controller.UsersController;
import controller.VMController;
import io.*;
import model.*;
import spark.Session;
import utility.Check;
import utility.Logging;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

public class CloudServiceProvider {

    public static ArrayList<User> users = new ArrayList<User>();
    public static ArrayList<Organization> organizations = new ArrayList<Organization>();
    public static ArrayList<VM> vms = new ArrayList<VM>();
    public static ArrayList<Disc> discs = new ArrayList<Disc>();
    public static ArrayList<CategoryVM> categories = new ArrayList<CategoryVM>();
    private static final Gson g = new Gson();


    public static void main(String[] args) throws IOException {
        port(8080);
        staticFiles.externalLocation(new File("./static").getCanonicalPath());

        // Ucitavanje podataka iz fajlova
        // User superadmin = new User("super@admin.com", "Super", "Admin", "superadmin", null, Role.SUPERADMIN);
        // users.add(superadmin);

        CategoriesIO.fromFile();
        DiscIO.fromFile();
        try {
            VMIO.fromFile();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // dodamo odgovarajucu kategoriju i diskove, a diskovima odg virt.masinu
        UserIO.fromFile();
        OrganizationsIO.fromFile();

        VMController.getInstance().init();
        UsersController.getInstance().init();
        DiscController.getInstance().init();
        OrgController.getInstance().init();



        post("/rest/login", (req, res) -> {
            res.type("application/json");
            String text = req.body(); // ovde imamo {"email":"nesto","password":"nesto"}
            JsonObject jsonObject = new JsonObject();
            User u = Logging.checkLogIn(text);
            if (u != null) {
                Session ss = req.session(true);
                User user = ss.attribute("user");
                if (user == null) {
                    user = u;
                    ss.attribute("user", user);
                }
                res.status(200);
                return jsonObject;
            }
            res.status(400);
            return jsonObject;
        });

        get("/rest/logout", (req, res) -> {
            res.type("application/json");
            Session ss = req.session(true);
            User user = ss.attribute("user");

            if (user != null) {
                ss.attribute("user", null);
                ss.invalidate();
            }
            return true;
        });

        get("/rest/testlogin", (req, res) -> {
            Session ss = req.session(true);
            User user = ss.attribute("user");

            if (user == null) {
                res.status(400);
                return "No user logged in.";
            } else {
                res.status(200);
                return user.getEmail();
            }
        });

    }

}
