package ru.hse.autocode.csv.models;

/**
 * CSV storage item
 */
public interface ICSVItem {
    /**
     * Get id of csv item
     * @return feature id
     */
    int getId();

    /**
     * get value of csv item
     * @return feature value
     */
    double getValue();

    /**
     * Sets feature value
     * @param newValue new feature value
     */
    void setValue(double newValue);
}
