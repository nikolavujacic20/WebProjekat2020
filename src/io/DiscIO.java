package io;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import main.CloudServiceProvider;
import model.Disc;
import model.DiscType;
import model.VM;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DiscIO {
    private static ArrayList<String[]> data;

    public static void setData(ArrayList<Disc> list) {
        data = new ArrayList<String[]>();
        data.add(new String[]{"name", "type", "capacity", "virtualMachine"});
        for (Disc obj : list) {
            data.add(new String[]{obj.getName(), "" + obj.getType(), "" + obj.getCapacity(), obj.getVirtualMachine().getName()});
        }
    }

    public static void fromFile() throws NumberFormatException, IOException {
        String csvFileName = "././data/discs.csv";
        CSVReader reader = new CSVReader(new FileReader(csvFileName), ',', '"', 1);
        String[] row = null;

        while ((row = reader.readNext()) != null) {
            String name = row[0];
            DiscType type = DiscType.valueOf(row[1]);
            int capacity = Integer.parseInt(row[2]);
            Disc d = new Disc(name, type, capacity, new VM());
            CloudServiceProvider.discs.add(d);

        }
        reader.close();

    }

    public static void toFile(ArrayList<Disc> list) throws IOException {
        String csvNewFile = "././data/discs.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvNewFile));
        setData(list);
        writer.writeAll(data);
        writer.close();

    }
}
