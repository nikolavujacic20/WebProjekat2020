package io;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import main.CloudServiceProvider;
import model.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class OrganizationsIO {
    private static ArrayList<String[]> data;

    public static void setData(ArrayList<Organization> list) {
        data = new ArrayList<String[]>();
        data.add(new String[]{"name", "description", "imagePath", "users", "resources"});
        for (Organization obj : list) {
            data.add(new String[]{obj.getName(), obj.getDescription(), obj.getImagePath(), usersToString(obj.getUsers()), resourcesToString(obj.getResources())});
        }
    }

    //format je name1;name2
    private static String resourcesToString(ArrayList<Resource> resources) {
        if (resources.size() != 0) {
            String retVal = "";
            for (Resource r : resources) {
                retVal += r.getName() + ";";
            }
            return retVal.substring(0, retVal.length() - 1);
        }
        return "";

    }

    // format je mail1;mail2;mail3
    private static String usersToString(ArrayList<User> users) {
        if (users.size() != 0) {
            String retVal = "";
            for (User u : users) {
                retVal += u.getEmail() + ";";
            }
            return retVal.substring(0, retVal.length() - 1);
        }
        return "";
    }


    public static void toFile(ArrayList<Organization> list) throws IOException {
        String csvNewFile = "././data/organizations.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvNewFile));
        setData(list);
        writer.writeAll(data);
        writer.close();

    }

    public static void fromFile() throws NumberFormatException, IOException {
        String csvFileName = "././data/organizations.csv";
        CSVReader reader = new CSVReader(new FileReader(csvFileName), ',', '"', 1);
        String[] row = null;

        while ((row = reader.readNext()) != null) {
            String name = row[0];
            String description = row[1];
            String imagePath = row[2];
            ArrayList<User> users = usersFromString(row[3]);
            ArrayList<Resource> resources = resourcesFromString(row[4]);

            Organization o = new Organization(name, description, imagePath, users, resources);
            for (User u : users) {
                u.setOrganization(o);
            }
            CloudServiceProvider.organizations.add(o);

        }
        reader.close();

    }

    private static ArrayList<Resource> resourcesFromString(String row) {
        ArrayList<Resource> retVal = new ArrayList<Resource>();
        String[] tokens = row.split(";");
        for (String s : tokens) {
            for (VM vm : CloudServiceProvider.vms) {
                if (vm.getName().equals(s)) {
                    retVal.add(vm);
                }
            }
            for (Disc d : CloudServiceProvider.discs) {
                if (d.getName().equals(s)) {
                    retVal.add(d);
                }
            }
        }
        return retVal;
    }

    private static ArrayList<User> usersFromString(String row) {
        ArrayList<User> retVal = new ArrayList<User>();
        String[] tokens = row.split(";");
        for (String s : tokens) {
            for (User u : CloudServiceProvider.users) {
                if (u.getEmail().equals(s)) {
                    retVal.add(u);
                }
            }

        }
        return retVal;
    }

}
