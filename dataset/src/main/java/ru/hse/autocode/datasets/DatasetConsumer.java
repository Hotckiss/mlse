package ru.hse.autocode.datasets;

import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that finalize and store final dataset
 */
public class DatasetConsumer {
    private final String datasetFileName;
    private final List<HashMap<String, Double>> dataset;

    public DatasetConsumer(@NotNull String datasetFileName,
                           @NotNull List<HashMap<String, Double>> dataset) {
        this.datasetFileName = datasetFileName;
        this.dataset = dataset;
        System.err.println("Dataset size:" + dataset.size());
    }

    /**
     * Runs dataset handling
     * @throws Exception if IO error occurred
     */
    public void consume() throws Exception {
        List<String> typesList = createTypesList();
        BufferedWriter writer = new BufferedWriter(new FileWriter(datasetFileName));
        writeHeader(writer, typesList);

        for (HashMap<String, Double> item: dataset) {
            List<Double> featuresVector = typesList.stream().map(type -> item.getOrDefault(type, 0.0)).collect(Collectors.toList());
            featuresVector.add(item.get("label"));
            writer.write (StringUtils.join(featuresVector, ",") + "\n");
        }

        writer.close();
    }

    private List<String> createTypesList() {
        HashSet<String> types = new HashSet<>();
        for (HashMap<String, Double> item: dataset) {
            types.addAll(item.keySet());
        }

        types.remove("label");
        System.err.println("Features types:" + types);

        return types
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private void writeHeader(@NotNull BufferedWriter writer,
                             @NotNull List<String> typesList) throws IOException {
        String head = StringUtils.join(typesList, ",");
        head += ",label";
        writer.write(head + "\n");
    }
}
