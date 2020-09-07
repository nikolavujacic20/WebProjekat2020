package controller;

import com.google.gson.Gson;
import io.DiscIO;
import io.OrganizationsIO;
import io.VMIO;
import main.CloudServiceProvider;
import model.*;
import spark.Session;
import utility.Check;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;
import static spark.Spark.get;

public class DiscController {
    private static final Gson g = new Gson();
    private static final DiscController instance = null;
    private static final DecimalFormat df2 = new DecimalFormat("#.##");

    public static DiscController getInstance() {
        return Optional.ofNullable(instance).orElseGet(DiscController::new);
    }

    public void init() {
        get("/rest/getAllDiscs", (req, res) -> {
            res.type("application/json");
            Session ss = req.session(true);
            User user = ss.attribute("user");
            if (user.getRole() == Role.SUPERADMIN)
                return g.toJson(CloudServiceProvider.discs);
            else {
                ArrayList<Disc> adminDiscs = new ArrayList<Disc>();
                for (Resource r : user.getOrganization().getResources()) {
                    if (r instanceof Disc) {
                        adminDiscs.add((Disc) r);
                    }
                }
                return g.toJson(adminDiscs);
            }
        });

        get("/rest/getDisc/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            User u = req.session().attribute("user");
            if (u.getRole() == Role.ADMIN || u.getRole() == Role.USER) {
                for (Disc disc : CloudServiceProvider.discs) {
                    if (disc.getName().equals(name)) {
                        if (!u.getOrganization().getResources().contains(disc)) {
                            res.status(403);
                            return "Forbidden";
                        }
                    }
                }
            }
            for (Disc d : CloudServiceProvider.discs) {
                if (d.getName().equals(name))
                    return g.toJson(d);
            }
            return "";
        });

        put("rest/editDisc/:name", (req, res) -> {
            res.type("application/json");
            Disc data = g.fromJson(req.body(), Disc.class);
            String name = req.params("name");
            if (!data.getName().equals(name) && !Check.DiscNameUnique(data.getName())) {
                res.status(400);
                return "New name is not unique";
            }
            for (Disc d : CloudServiceProvider.discs) {
                // ako je menjano ime i nije jedinstveno

                if (d.getName().equals(name)) {
                    d.setName(data.getName());
                    d.setCapacity(data.getCapacity());
                    d.setType(data.getType());
                    if (!d.getVirtualMachine().getName().equals(data.getVirtualMachine().getName())) {
                        d.getVirtualMachine().getDiscs().remove(d);
                        for (VM v : CloudServiceProvider.vms) {
                            if (v.getName().equals(data.getVirtualMachine().getName())) {
                                v.getDiscs().add(d);
                                d.setVirtualMachine(v);
                                break;
                            }
                        }
                    }
                    DiscIO.toFile(CloudServiceProvider.discs);
                    VMIO.toFile(CloudServiceProvider.vms);
                    OrganizationsIO.toFile(CloudServiceProvider.organizations); // zbog imena diska
                    break;
                }
            }
            return "";
        });

        delete("/rest/deleteDisc/:name", (req, res) -> {
            res.type("application/json");
            Disc data = g.fromJson(req.body(), Disc.class);
            String name = req.params("name");
            for (Disc d : CloudServiceProvider.discs) {
                if (d.getName().equals(name)) {
                    for (VM v : CloudServiceProvider.vms) {
                        if (v.getDiscs().contains(d)) {
                            v.getDiscs().remove(d);
                            break;
                        }
                    }
                    for (Organization o : CloudServiceProvider.organizations) {
                        o.getResources().remove(d);
                    }
                    CloudServiceProvider.discs.remove(d);
                    DiscIO.toFile(CloudServiceProvider.discs);
                    VMIO.toFile(CloudServiceProvider.vms);
                    OrganizationsIO.toFile(CloudServiceProvider.organizations);
                    res.status(200);
                    return "OK";
                }
            }
            res.status(400);
            return "Error.";
        });

        post("rest/addDisc", (req, res) -> {
            res.type("application/json");
            Disc data = g.fromJson(req.body(), Disc.class);
            System.out.println(data);
            Session ss = req.session(true);
            User currentUser = ss.attribute("user");
            if (utility.Check.DiscNameUnique(data.getName())) {
                // dodajemo disk virt masini, zatim i organizaciji kojoj pripada ta vm
                // proveriti da li treba na ovaj nacin ili neka druga logika
                for (VM v : CloudServiceProvider.vms) {
                    if (v.getName().equals(data.getVirtualMachine().getName())) {
                        data.setVirtualMachine(v);
                        v.getDiscs().add(data);
                        for (Organization o : CloudServiceProvider.organizations) {
                            if (o.getResources().contains(v)) {
                                o.getResources().add(data);
                                break;
                            }
                        }
                        CloudServiceProvider.discs.add(data);
                        DiscIO.toFile(CloudServiceProvider.discs);
                        VMIO.toFile(CloudServiceProvider.vms);
                        OrganizationsIO.toFile(CloudServiceProvider.organizations);
                        res.status(200);
                        return "OK";
                    }
                }
            }
            res.status(400);
            return "Name is not unique.";
        });


        get("rest/getDiscs/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            for (VM v : CloudServiceProvider.vms) {
                if (v.getName().equals(name))
                    return g.toJson(v.getDiscs());
            }
            return "";
        });


        get("rest/getEmptyDiscs", (req, res) -> {
            res.type("application/json");
            ArrayList<Disc> emptyDiscs = new ArrayList<Disc>();
            for (Disc d : CloudServiceProvider.discs) {
                if (d.getVirtualMachine().getName().equals(""))
                    emptyDiscs.add(d);
            }
            return g.toJson(emptyDiscs);
        });

        put("rest/searchDiscs", (req, res) -> {
            res.type("application/json");
            DiscSearch data = g.fromJson(req.body(), DiscSearch.class);
            ArrayList<Disc> result = new ArrayList<Disc>();

            for (Disc d : CloudServiceProvider.discs) {
                if (!data.getName().equals("")) {
                    if (d.getName().contains(data.getName())) {
                        if (data.getTo() != 0) {
                            if (d.getCapacity() > data.getFrom() & d.getCapacity() < data.getTo()) {
                                result.add(d);
                            }
                        } else {
                            if (d.getCapacity() > data.getFrom())
                                result.add(d);
                        }
                    }
                } else {
                    if (data.getTo() != 0) {
                        if (d.getCapacity() > data.getFrom() & d.getCapacity() < data.getTo()) {
                            result.add(d);
                        }
                    } else {
                        if (d.getCapacity() > data.getFrom())
                            result.add(d);
                    }
                }
            }

            res.status(200);
            return g.toJson(result);

        });

        put("rest/monthlyCheck", (req, res) -> {
            res.type("application/json");
            DateSearch data = Check.getDate(req.body());
            ArrayList<MonthlyCheck> retVal = new ArrayList<MonthlyCheck>();
            User user = req.session().attribute("user");

            long diffInMillies = Math.abs(data.getEnd().getTime() - data.getStart().getTime());
            double days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);


            for (Resource r : user.getOrganization().getResources()) {
                if (r instanceof Disc) {
                    MonthlyCheck mc = new MonthlyCheck();
                    mc.setName(r.getName());
                    mc.setPrice(days * Check.getDiscPrice((Disc) r));
                    mc.setPrice(Double.parseDouble(df2.format(mc.getPrice())));
                    retVal.add(mc);
                } else {
                    MonthlyCheck mc = new MonthlyCheck();
                    mc.setPrice(0);
                    for (Activity a : ((VM) r).getActivities()) {
                        if (a.getTurnOn().after(data.getStart())) {
                            if (a.getTurnOff() == null) {

                                diffInMillies = Math.abs(data.getEnd().getTime() - a.getTurnOn().getTime());
                                days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                // upada u granica
                                mc.setName(r.getName());
                                mc.add(days * Check.getVMPrice((VM) r));
                            } else if (a.getTurnOff().before(data.getEnd())) {
                                diffInMillies = Math.abs(a.getTurnOff().getTime() - a.getTurnOn().getTime());
                                days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                // upada u granica
                                mc.setName(r.getName());
                                mc.add(days * Check.getVMPrice((VM) r));


                            } else {
                                if (a.getTurnOn().before(data.getEnd())) {
                                    diffInMillies = Math.abs(data.getEnd().getTime() - a.getTurnOn().getTime());
                                    days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                    mc.setName(r.getName());
                                    mc.add(days * Check.getVMPrice((VM) r));
                                }
                            }
                        } else {
                            if (a.getTurnOff() == null) {
                                diffInMillies = Math.abs(data.getEnd().getTime() - data.getStart().getTime());
                                days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                mc.setName(r.getName());
                                mc.add(days * Check.getVMPrice((VM) r));
                            } else if (a.getTurnOff().after(data.getStart()) & a.getTurnOff().before(data.getEnd())) {
                                // turnOff after getStart and before getEnd
                                // od get start do turn off
                                diffInMillies = Math.abs(a.getTurnOff().getTime() - data.getStart().getTime());
                                days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                mc.setName(r.getName());
                                mc.add(days * Check.getVMPrice((VM) r));
                            }


                        }
                    }

                    mc.setPrice(Double.parseDouble(df2.format(mc.getPrice())));
                    if (mc.getPrice() != 0)
                        retVal.add(mc);

                }
            }

            res.status(200);
            return g.toJson(retVal);
        });
    }
}
