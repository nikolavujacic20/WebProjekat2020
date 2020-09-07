package controller;

import com.google.gson.Gson;
import io.DiscIO;
import io.OrganizationsIO;
import io.VMIO;
import main.CloudServiceProvider;
import model.*;
import spark.Session;
import utility.Check;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static spark.Spark.*;

public class VMController {
    private static final Gson g = new Gson();
    private static final VMController instance = null;

    public static VMController getInstance() {
        return Optional.ofNullable(instance).orElseGet(VMController::new);
    }

    public void init() {
        get("/rest/getAllVM", (req, res) -> {
            res.type("application/json");
            Session ss = req.session(true);
            User user = ss.attribute("user");
            if (user.getRole()!=null &&  user.getRole()== Role.SUPERADMIN)
                return g.toJson(CloudServiceProvider.vms);
            else {
                ArrayList<VM> adminVMS = new ArrayList<VM>();
                for (Resource v : user.getOrganization().getResources()) {
                    if (v instanceof VM) {
                        adminVMS.add((VM) v);
                    }
                }
                return g.toJson(adminVMS);
            }

        });

        get("rest/getVM/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            User u = req.session().attribute("user");
            if (u.getRole() == Role.ADMIN || u.getRole() == Role.USER) {
                for (VM vm : CloudServiceProvider.vms) {
                    if (vm.getName().equals(name)) {
                        if (!u.getOrganization().getResources().contains(vm)) {
                            res.status(403);
                            return "Forbidden";
                        }
                    }
                }
            }
            for (VM v : CloudServiceProvider.vms) {
                if (v.getName().equals(name))
                    return g.toJson(v);
            }
            return "";
        });

        put("rest/editVM/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            VMRetVal data = g.fromJson(req.body(), VMRetVal.class);
            if (!data.getName().equals(name) && !Check.VMNameUnique(data.getName())) {
                res.status(400);
                return "New name is not unique";
            }
            for (VM v : CloudServiceProvider.vms) {

                if (v.getName().equals(name)) {
                    v.setName(data.getName());
                    for (CategoryVM cvm : CloudServiceProvider.categories) {
                        if (cvm.getName().equals(data.getCategory().getName())) {
                            v.setCategory(cvm);
                            break;
                        }
                    }
                    ArrayList<Disc> remove = new ArrayList<Disc>();
                    for (Disc d : v.getDiscs()) {
                        boolean r = true;
                        for (Disc d2 : data.getDiscs()) { // ovo su diskovi koji su ostali
                            if (d2.getName().equals(d.getName())) {
                                r = false;
                                break;
                            }
                        }
                        if (r) {
                            remove.add(d);
                        }
                    }

                    for (Disc d : CloudServiceProvider.discs) {
                        for (Disc d2 : remove) {
                            if (d.getName().equals(d2.getName())) {
                                d.setVirtualMachine(new VM());
                                break;
                            }
                        }
                    }

                    if (v.getDiscs().size() != data.getDiscs().size()) {
                        v.setDiscs(data.getDiscs());
                    }

                }
            }

            VMIO.toFile(CloudServiceProvider.vms);
            DiscIO.toFile(CloudServiceProvider.discs);
            OrganizationsIO.toFile(CloudServiceProvider.organizations);
            return "";
        });

        delete("rest/deleteVM/:name", (req, res) -> {
            res.type("application/json");
            VM data = g.fromJson(req.body(), VM.class);
            String name = req.params("name");
            for (VM v : CloudServiceProvider.vms) {
                if (v.getName().equals(name)) {
                    for (Disc d : CloudServiceProvider.discs) {
                        if (v.getDiscs().contains(d)) {
                            d.setVirtualMachine(new VM());
                            break;
                        }
                    }
                    for (Organization o : CloudServiceProvider.organizations) {
                        o.getResources().remove(v);
                    }
                    CloudServiceProvider.vms.remove(v);
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

        post("rest/addVM", (req, res) -> {
            res.type("application/json");
            VMData data = g.fromJson(req.body(), VMData.class);
            VM newVM = new VM();
            if (!Check.VMNameUnique(data.getName())) {
                res.status(400);
                return "VM name is not unique";
            }
            newVM.setName(data.getName());
            for (CategoryVM cat : CloudServiceProvider.categories) {
                if (cat.getName().equals(data.getCategory())) {
                    newVM.setCategory(cat);
                    break;
                }
            }
            for (Disc d : CloudServiceProvider.discs) {
                for (String dname : data.getCheckedDiscs()) {
                    if (d.getName().equals(dname)) {
                        newVM.getDiscs().add(d);
                        d.setVirtualMachine(newVM);
                        break;
                    }
                }
            }
            if (!data.getOrg().equals("")) {
                for (Organization o : CloudServiceProvider.organizations) {
                    if (o.getName().equals(data.getOrg())) {
                        o.getResources().add(newVM);
                        break;
                    }
                }
            } else {
                Session ss = req.session(true);
                User currentUser = ss.attribute("user");
                currentUser.getOrganization().getResources().add(newVM);

            }
            CloudServiceProvider.vms.add(newVM);
            DiscIO.toFile(CloudServiceProvider.discs);
            VMIO.toFile(CloudServiceProvider.vms);
            OrganizationsIO.toFile(CloudServiceProvider.organizations);
            res.status(200);
            return "Successfully added new vm";
        });

        put("rest/searchVMs", (req, res) -> {
            res.type("application/json");
            VMSearch data = g.fromJson(req.body(), VMSearch.class);
            ArrayList<VM> result1 = new ArrayList<VM>();
            ArrayList<VM> result2 = new ArrayList<VM>();
            ArrayList<VM> result3 = new ArrayList<VM>();
            ArrayList<VM> result4 = new ArrayList<VM>();

            ArrayList<VM> finalResult = new ArrayList<VM>();

            if (!data.getName().equals("")) {
                for (VM v : CloudServiceProvider.vms) {
                    if (v.getName().contains(data.getName()))
                        result1.add(v);
                }
            } else {
                result1 = CloudServiceProvider.vms;
            }

            for (VM v : CloudServiceProvider.vms) {
                if (data.getCoreNumberTo() == 0) {
                    if (v.getCategory().getCoreNumber() > data.getCoreNumberFrom())
                        result2.add(v);

                } else {
                    if ((v.getCategory().getCoreNumber() > data.getCoreNumberFrom()) & (v.getCategory().getCoreNumber() < data.getCoreNumberTo()))
                        result2.add(v);
                }
            }

            for (VM v : CloudServiceProvider.vms) {
                if (data.getRAMTo() == 0) {
                    if (v.getCategory().getRAM() > data.getRAMFrom())
                        result3.add(v);

                } else {
                    if ((v.getCategory().getRAM() > data.getRAMFrom()) & (v.getCategory().getRAM() < data.getRAMTo()))
                        result3.add(v);
                }
            }

            for (VM v : CloudServiceProvider.vms) {
                if (data.getGPUTo() == 0) {
                    if (v.getCategory().getGPUcore() > data.getGPUFrom())
                        result4.add(v);

                } else {
                    if ((v.getCategory().getGPUcore() > data.getGPUFrom()) & (v.getCategory().getGPUcore() < data.getGPUTo()))
                        result4.add(v);
                }
            }

            for (VM vm : CloudServiceProvider.vms) {
                if (result1.contains(vm) && result2.contains(vm) && result3.contains(vm) && result4.contains(vm))
                    finalResult.add(vm);
            }

            res.status(200);
            return g.toJson(finalResult);

        });

        put("rest/changeStatus/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            for (VM v : CloudServiceProvider.vms) {
                if (v.getName().equals(name)) {
                    if (v.getActivities().size() != 0) {
                        if (v.getActivities().get(v.getActivities().size() - 1).getTurnOff() == null) {
                            v.getActivities().get(v.getActivities().size() - 1).setTurnOff(new Date());
                            res.status(200);
                            VMIO.toFile(CloudServiceProvider.vms);
                            return "Status  changed";
                        } else {
                            v.getActivities().add(new Activity(new Date()));
                            res.status(200);
                            VMIO.toFile(CloudServiceProvider.vms);
                            return "Status  changed";
                        }
                    } else {
                        v.getActivities().add(new Activity(new Date()));
                        res.status(200);
                        VMIO.toFile(CloudServiceProvider.vms);
                        return "Status  changed";
                    }

                }
            }

            res.status(400);
            return "VM not found";
        });

        get("rest/getStatus/:name", (req, res) -> {
            res.type("application/json");
            String name = req.params("name");
            for (VM v : CloudServiceProvider.vms) {
                if (v.getName().equals(name)) {
                    if (v.getActivities().size() != 0) {
                        if (v.getActivities().get(v.getActivities().size() - 1).getTurnOff() != null) {
                            res.status(200);
                            return g.toJson(false);
                        } else {
                            res.status(200);
                            return g.toJson(true);
                        }
                    } else {
                        res.status(200);
                        return g.toJson(false);
                    }
                }
            }
            res.status(400);
            return "Error";
        });
    }
}
