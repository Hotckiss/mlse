package ru.hse.autocode.csv;

import ru.hse.autocode.csv.models.Feature;
import ru.hse.autocode.csv.models.ICSVItem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class for building CSV dataset where some items may not be represented.
 */
public class SparseCSVBuilder {
    // Public shared instance of csv builder 
    public static SparseCSVBuilder sharedInstance;

    private final PrintWriter printWriter;
    private final List<ICSVItem> items = new ArrayList<>();
    private final int featuresCount;

    /**
     * Creates CSV dataset builder 
     * @param fileName output file for dataset
     * @param featuresCount total number of features in dataset
     * @throws IOException if IO error occured
     */
    public SparseCSVBuilder(final String fileName, int featuresCount) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        this.printWriter = new PrintWriter(fileWriter);
        this.featuresCount = featuresCount;
    }

    /**
     * Adds new feature to vector of dataset
     * @param feature item which represents feature
     */
    public void addFeature(final ICSVItem feature) {
        this.items.add(feature);
    }

    /**
     * Writes completed vector to dataset
     * @param label vector label in dataset
     */
    public void writeVector(boolean label) {
        if (items.isEmpty()) {
            writeHeader();
            return;
        }

        items.sort(Comparator.comparingInt(ICSVItem::getId));

        int itemsPtr = 0;
        for (int i = 0; i < featuresCount; ++i) {
            if (itemsPtr == items.size()) {
                printWriter.print("0,");
            } else {
                if (items.get(itemsPtr).getId() == i) {
                    printWriter.print(items.get(itemsPtr).getValue() + ",");
                    itemsPtr++;
                } else {
                    printWriter.print("0,");
                }
            }
        }

        printWriter.print(label ? "1\n" : "0\n");

        items.clear();
    }

    private void writeHeader() {
        for (Feature feature: Feature.values()) {
            printWriter.print(feature.getName() + ",");
        }

        printWriter.print("label\n");
    }
}
