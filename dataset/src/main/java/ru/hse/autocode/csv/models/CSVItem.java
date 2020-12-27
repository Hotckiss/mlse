package ru.hse.autocode.csv.models;

import org.jetbrains.annotations.NotNull;

/**
 * Class that stores csv item
 */
public class CSVItem implements ICSVItem {
    private final Feature type;
    private double value;

    /**
     * Constructs new csv item
     * @param type feature type
     * @param value feature value
     */
    public CSVItem(@NotNull Feature type, double value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public int getId() {
        return type.getId();
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void setValue(double newValue) {
        this.value = newValue;
    }
}
