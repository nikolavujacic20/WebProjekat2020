package controller;

import com.google.gson.Gson;
import io.CategoriesIO;
import io.OrganizationsIO;
import io.UserIO;
import io.VMIO;
import main.CloudServiceProvider;
import model.*;
import spark.Session;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;

import static spark.Spark.*;

public class OrgController {
    private static final Gson g = new Gson();
    private static final OrgController instance = null;

    public static OrgController getInstance() {
        return Optional.ofNullable(instance).orElseGet(OrgController::new);
    }

    public void init() {
        get("/rest/getAllOrg", (req, res) -> {
            res.type("application/json");
            Session ss = req.session(true);
            User user = ss.attribute("user");
            if (user.getRole() == Role.SUPERADMIN)
                return g.toJson(CloudServiceProvider.organizations);
            else {
                ArrayList<Organization> adminOrg = new ArrayList<Organization>();
                adminOrg.add(user.getOrganization());
                return g.toJson(adminOrg);
            }
        });

        get("/rest/getOrg/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            User u = req.session().attribute("user");
            if (u.getRole() == Role.USER) {
                res.status(403);
                return "Forbidden";
            } else if (u.getRole() == Role.ADMIN) {
                if (!u.getOrganization().getName().equals(name)) {
                    res.status(403);
                    return "Forbidden";
                }
            }
            for (Organization o : CloudServiceProvider.organizations) {
                if (o.getName().equals(name))
                    return g.toJson(o);
            }
            return "OK";
        });


        post("/rest/addOrg", (req, res) -> {
            res.type("application/json");
            Organization data = g.fromJson(req.body(), Organization.class);

            // proveriti da li je ime jedinstveno
            if (utility.Check.OrganizationNameUnique(data.getName())) {
                CloudServiceProvider.organizations.add(data);
                OrganizationsIO.toFile(CloudServiceProvider.organizations);
                res.status(200);
                return "OK";
            }
            res.status(400);
            return "Error";

        });

        put("rest/editOrg/:name", (req, res) -> {
            res.type("application/json");
            Organization data = g.fromJson(req.body(), Organization.class);
            String name = req.params("name");
            for (Organization o : CloudServiceProvider.organizations) {
                if (o.getName().equals(name)) {
                    if (!name.equals(data.getName()) && !utility.Check.OrganizationNameUnique(data.getName())) {
                        res.status(400);
                        return "New name is not unique.";
                    }
                    o.setName(data.getName());
                    o.setDescription(data.getDescription());
                    o.setImagePath(data.getImagePath());
                    break;
                }
            }
            // potrebno upisati i korisnike ponovo jer imaju referencu na organizaciju
            OrganizationsIO.toFile(CloudServiceProvider.organizations);
            UserIO.toFile(CloudServiceProvider.users);
            res.status(200);
            return "OK";

        });


        get("/rest/getAllCat", (req, res) -> {
            res.type("application/json");
            return g.toJson(CloudServiceProvider.categories);
        });

        get("/rest/getCat/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            for (CategoryVM c : CloudServiceProvider.categories) {
                if (c.getName().equals(name))
                    return g.toJson(c);
            }
            return "";
        });

        delete("/rest/deleteCat/:name", (req, res) -> {
            res.type("application/json");
            CategoryVM data = g.fromJson(req.body(), CategoryVM.class);
            String name = req.params("name");
            for (CategoryVM c : CloudServiceProvider.categories) {
                if (c.getName().equals(name)) {
                    // ako postoji vm zakacena, vraca se greska
                    for (VM vm : CloudServiceProvider.vms) {
                        if (vm.getCategory().equals(c)) {
                            res.status(400);
                            return "ERROR: At least one virtual machine has this category.";
                        }
                    }
                    CloudServiceProvider.categories.remove(c);
                    break;
                }
            }
            CategoriesIO.toFile(CloudServiceProvider.categories);
            res.status(200);
            return "OK";
        });

        post("/rest/addCat", (req, res) -> {
            res.type("application/json");
            CategoryVM data = g.fromJson(req.body(), CategoryVM.class);

            // proveriti da li je ime jedinstveno
            if (utility.Check.CategoryNameUnique(data.getName())) {
                CloudServiceProvider.categories.add(data);
                CategoriesIO.toFile(CloudServiceProvider.categories);
                res.status(200);
                return "OK";
            }
            res.status(400);
            return "OK";

        });

        put("rest/editCat/:name", (req, res) -> {
            res.type("application/json");
            CategoryVM data = g.fromJson(req.body(), CategoryVM.class);
            String name = req.params("name");
            for (CategoryVM c : CloudServiceProvider.categories) {
                if (c.getName().equals(name)) {
                    if (!name.equals(data.getName()) && !utility.Check.CategoryNameUnique(data.getName())) {
                        res.status(400);
                        return "New name is not unique.";
                    }
                    c.setName(data.getName());
                    c.setCoreNumber(data.getCoreNumber());
                    c.setRAM(data.getRAM());
                    c.setGPUcore(data.getGPUcore());
                    break;
                }
            }
            CategoriesIO.toFile(CloudServiceProvider.categories);
            VMIO.toFile(CloudServiceProvider.vms);
            res.status(200);
            return "OK";

        });
    }
}
