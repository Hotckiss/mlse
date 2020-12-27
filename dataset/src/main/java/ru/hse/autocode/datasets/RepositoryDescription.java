package ru.hse.autocode.datasets;

import org.jetbrains.annotations.NotNull;

/**
 * Class that represents repository info
 */
public class RepositoryDescription {
    private final String repositoryName;
    private final String repositoryURL;

    public RepositoryDescription(@NotNull String repositoryName) {
        this.repositoryName = repositoryName;
        this.repositoryURL = String.format("https://github.com/%s.git", repositoryName);
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getRepositoryURL() {
        return repositoryURL;
    }
}
