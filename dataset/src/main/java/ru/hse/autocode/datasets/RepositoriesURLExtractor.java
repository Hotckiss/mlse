package ru.hse.autocode.datasets;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that extracts repositories info from file
 */
public class RepositoriesURLExtractor {
    /**
     * Method that extracts repositories info from file
     * @param repositoriesFileName path to repos file
     * @return List with each repo info
     * @throws IOException if IO error occurred
     */
    public List<RepositoryDescription> extractRepositories(@NotNull String repositoriesFileName) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(
                        new File(repositoriesFileName)
                )
        )) {
            return br
                    .lines()
                    .map(RepositoryDescription::new)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            System.err.println("Unable to read repositories!");
            System.err.println(ex.getMessage());
            throw ex;
        }
    }
}
