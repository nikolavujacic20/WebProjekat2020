package io;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import main.CloudServiceProvider;
import model.Organization;
import model.Role;
import model.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserIO {

    private static ArrayList<String[]> data;

    public static void setData(ArrayList<User> list) {
        data = new ArrayList<String[]>();
        data.add(new String[]{"email", "name", "lastName", "password", "organization", "role"});
        for (User obj : list) {
            if (obj.getOrganization().getName().equals("none")) {
                data.add(new String[]{obj.getEmail(), obj.getName(), obj.getLastName(), obj.getPassword(), "", "" + obj.getRole()});
            } else {
                data.add(new String[]{obj.getEmail(), obj.getName(), obj.getLastName(), obj.getPassword(), obj.getOrganization().getName(), "" + obj.getRole()});
            }

        }
    }

    public static void fromFile() throws NumberFormatException, IOException {
        String csvFileName = "././data/users.csv";
        CSVReader reader = new CSVReader(new FileReader(csvFileName), ',', '"', 1);
        String[] row = null;

        while ((row = reader.readNext()) != null) {
            String email = row[0];
            String name = row[1];
            String lastname = row[2];
            String password = row[3];
            Role role = Role.valueOf(row[5]);

            User u = new User(email, name, lastname, password, new Organization("none"), role);
            CloudServiceProvider.users.add(u);

        }
        reader.close();

    }

    public static void toFile(ArrayList<User> list) throws IOException {
        String csvNewFile = "././data/users.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvNewFile));
        setData(list);
        writer.writeAll(data);
        writer.close();

    }

}
