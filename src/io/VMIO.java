package io;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import main.CloudServiceProvider;
import model.Activity;
import model.CategoryVM;
import model.Disc;
import model.VM;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class VMIO {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static ArrayList<String[]> data;

    public static void setData(ArrayList<VM> list) {
        data = new ArrayList<String[]>();
        data.add(new String[]{"name", "category", "discs", "activities"});
        for (VM obj : list) {
            data.add(new String[]{obj.getName(), obj.getCategory().getName(),
                    discsToString(obj.getDiscs()), activitiesToString(obj.getActivities())});
        }
    }

    // format je "uklj;isklj|uklj;isklj"
    private static String activitiesToString(ArrayList<Activity> activities) {
        String retVal = "";
        for (Activity a : activities) {
            if (a.getTurnOff() != null)
                retVal += sdf.format(a.getTurnOn()) + ";" + sdf.format(a.getTurnOff()) + "|";
            else
                retVal += sdf.format(a.getTurnOn()) + ";" + "" + "|";
        }
        if (retVal.equals(""))
            return "";
        else
            return retVal.substring(0, retVal.length() - 1);
    }

    // format je d1;d2;d3
    private static String discsToString(ArrayList<Disc> discs) {
        String retVal = "";
        for (Disc d : discs) {
            retVal += d.getName() + ";";
        }
        if (retVal.equals(""))
            return retVal;
        return retVal.substring(0, retVal.length() - 1);
    }

    public static void toFile(ArrayList<VM> list) throws IOException {
        String csvNewFile = "././data/vms.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvNewFile));
        setData(list);
        writer.writeAll(data);
        writer.close();

    }

    public static void fromFile() throws NumberFormatException, IOException, ParseException {
        String csvFileName = "././data/vms.csv";
        CSVReader reader = new CSVReader(new FileReader(csvFileName), ',', '"', 1);
        String[] row = null;

        while ((row = reader.readNext()) != null) {
            String name = row[0];
            CategoryVM cat = null;
            for (CategoryVM c : CloudServiceProvider.categories) {
                if (c.getName().equals(row[1])) {
                    cat = c;
                    break;
                }
            }
            ArrayList<Disc> discs = dicscFromString(row[2]);
            ArrayList<Activity> activities = actFromString(row[3]);
            VM vm = new VM(name, cat, discs, activities);

            for (Disc d : discs) {
                d.setVirtualMachine(vm);
            }
            CloudServiceProvider.vms.add(vm);

        }
        reader.close();

    }

    private static ArrayList<Activity> actFromString(String row) throws ParseException {
        ArrayList<Activity> retVal = new ArrayList<Activity>();
        String[] pairs = row.split("\\|");
        for (String s : pairs) {
            String[] pair = s.split(";");

            if (pair[0].equals(""))
                return retVal;
            else {
                Activity a;
                if (pair.length > 1)
                    a = new Activity(sdf.parse(pair[0]), sdf.parse(pair[1]));
                else
                    a = new Activity(sdf.parse(pair[0]));

                retVal.add(a);
            }
        }

        return retVal;
    }

    private static ArrayList<Disc> dicscFromString(String row) {
        ArrayList<Disc> retVal = new ArrayList<Disc>();
        String[] tokens = row.split(";");
        for (String s : tokens) {
            for (Disc d : CloudServiceProvider.discs) {
                if (d.getName().equals(s)) {
                    retVal.add(d);
                }
            }
        }
        return retVal;
    }

}
