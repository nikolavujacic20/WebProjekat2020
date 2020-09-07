package io;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import main.CloudServiceProvider;
import model.CategoryVM;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CategoriesIO {
    private static ArrayList<String[]> data;

    public static void setData(ArrayList<CategoryVM> list) {
        data = new ArrayList<String[]>();
        data.add(new String[]{"name", "coreNumber", "RAM", "GPUcore"});
        for (CategoryVM obj : list) {
            data.add(new String[]{obj.getName(), "" + obj.getCoreNumber(), "" + obj.getRAM(), "" + obj.getGPUcore()});
        }
    }

    public static void fromFile() throws NumberFormatException, IOException {
        String csvFileName = "././data/categories.csv";
        CSVReader reader = new CSVReader(new FileReader(csvFileName), ',', '"', 1);
        String[] row = null;

        while ((row = reader.readNext()) != null) {
            String name = row[0];
            int coreNumber = Integer.parseInt(row[1]);
            int RAM = Integer.parseInt(row[2]);
            int GPUcore = Integer.parseInt(row[3]);
            CategoryVM cvm = new CategoryVM(name, coreNumber, RAM, GPUcore);
            CloudServiceProvider.categories.add(cvm);

        }
        reader.close();

    }

    public static void toFile(ArrayList<CategoryVM> list) throws IOException {
        String csvNewFile = "././data/categories.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvNewFile));
        setData(list);
        writer.writeAll(data);
        writer.close();

    }
}
