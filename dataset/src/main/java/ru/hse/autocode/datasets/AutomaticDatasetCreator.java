package ru.hse.autocode.datasets;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.eclipse.jgit.api.Git;
import org.jetbrains.annotations.NotNull;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.refactoringminer.api.GitService;
import org.refactoringminer.util.GitServiceImpl;
import ru.hse.autocode.csv.SparseCSVBuilder;
import ru.hse.autocode.utils.calcers.*;
import ru.hse.autocode.GlobalConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main cli class for dataset collection
 */
public class AutomaticDatasetCreator {
    public static void main(String[] args) throws Exception {
        String datasetName = "december.csv";
        String repositoriesFileName = "repos.txt";
        run(datasetName, repositoriesFileName);
    }

    private static void run(@NotNull String datasetFileName,
                            @NotNull String repositoriesFileName) throws Exception {
        AutomaticDatasetCreator automaticDatasetCreator = init(datasetFileName);
        System.err.println("Starting dataset collection...");
        List<RepositoryDescription> repositories = new RepositoriesURLExtractor().extractRepositories(repositoriesFileName);
        List<List<HashMap<String, Double>>> resultDataForEachDataset = repositories
                .stream()
                .map(automaticDatasetCreator::runInner)
                .collect(Collectors.toList());

        List<HashMap<String, Double>> fullDataset = new ArrayList<>();
        resultDataForEachDataset.forEach(fullDataset::addAll);

        new DatasetConsumer(datasetFileName, fullDataset).consume();
    }

    /**
     * Runs extraction features on specified git repository
     * @param repo git repo
     * @param repositoryDescription repo description
     * @return features based on repo files
     */
    private List<HashMap<String, Double>> runOnGitRepository(@NotNull final Repository repo,
                                                             @NotNull RepositoryDescription repositoryDescription) {
        List<HashMap<String, Double>> resultData = new ArrayList<>();

        try (Git git = new Git(repo)) {
            Iterable<RevCommit> commits = git.log().all().call();
            Iterator<RevCommit> it = commits.iterator();
            ArrayList<RevCommit> allCommits = new ArrayList<>();
            while (it.hasNext()) {
                allCommits.add(it.next());
            }

            RevCommit commit = allCommits.get(Math.min(allCommits.size() - 1, 300));
            String commitId = commit.getId().getName();

            RevTree tree = commit.getTree();

            System.err.println("Iterating files of " + repositoryDescription.getRepositoryName() + "...");
            try (TreeWalk treeWalk = new TreeWalk(repo)) {
                treeWalk.reset(tree);
                while (treeWalk.next()) {
                    if (treeWalk.isSubtree()) {
                        treeWalk.enterSubtree();
                    } else {

                        HashMap<String, Double> fileRes = handleGitFile(repo, treeWalk.getPathString(), commitId);
                        if (!fileRes.isEmpty()) {
                            resultData.add(fileRes);
                        }

                    }
                }
            }
        } catch (Exception ex) {
            return resultData;
        }

        return resultData;
    }

    /**
     * Method that builds file representation and extracts features
     * @param repo github file repo
     * @param filePath path to file in repo
     * @param commitId id of commit in repo
     * @return file features set
     * @throws Exception if any error occurred
     */
    private HashMap<String, Double> handleGitFile(Repository repo, final String filePath, final String commitId) throws Exception {
        HashMap<String, Double> countNodes = new HashMap<>();
        if (!filePath.endsWith(".java")) {
             return new HashMap<>();
        }

        RevWalk revWalk = new RevWalk(repo);
        ObjectId objectId = repo.resolve(commitId);
        RevCommit commit = revWalk.parseCommit(objectId);
        RevTree tree = commit.getTree();
        TreeWalk treeWalk = new TreeWalk(repo);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.create(filePath));
        if (!treeWalk.next()) {
            return countNodes;
        }
        ObjectId objtId = treeWalk.getObjectId(0);
        ObjectLoader loader = repo.open(objtId);
        InputStream in = loader.openStream();
        StringBuilder allFileBuilder = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while (br.ready()) {
                allFileBuilder.append(br.readLine());
                allFileBuilder.append('\n');
            }

            return calculateFeatures(allFileBuilder.toString());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return countNodes;
    }

    /**
     * Method that calculates all features by file
     * @param fileAsString file string representation
     * @return file features
     */
    private HashMap<String, Double> calculateFeatures(@NotNull String fileAsString) {
        HashMap<String, Double> countNodes = new HashMap<>();
        ImportantNodesInfo info = new ImportantNodesInfo();
        InputStream stream = new ByteArrayInputStream(fileAsString.getBytes(StandardCharsets.UTF_8));
        try {
            CompilationUnit root = JavaParser.parse(stream);
            countNodes.put("connectivity", new ClassMemberSetsGenerator().calculateMethodsConnectivity(root));
            traverseAST(root, countNodes, info);
        } catch (Throwable npe) {
            return countNodes;
        }
        countNodes.put("label", 1.0);

        int importsCount = info.getImports().size();

        double compositeTypesRatio = (double)info.getVarsTypes().stream().filter((vt -> vt.contains("."))).count() / info.getVarsTypes().size();
        double namesLenAvg = info.getVarsNames().stream().mapToInt((String::length)).average().orElse(0);
        double namesStartNotLetterRatio = (double)info
                .getVarsNames()
                .stream()
                .filter((vn -> vn
                        .startsWith("_")))
                .count() /
                info
                        .getVarsNames()
                        .size();

        if (info.getVarsTypes().isEmpty()) {
            compositeTypesRatio = 0;
        }

        if (info.getVarsNames().isEmpty()) {
            namesStartNotLetterRatio = 0;
        }

        List<Set<String>> top = buildTopImports(info);

        countNodes.put("importsCount", (double)importsCount);
        countNodes.put("compositeTypesRatio", compositeTypesRatio);
        countNodes.put("namesLenAvg", namesLenAvg);
        countNodes.put("namesStartNotLetterRatio", namesStartNotLetterRatio);
        for (int i = 0; i < 4; ++i) {
            String topAKey = "top" + i + "a";
            String topCKey = "top" + i + "c";
            double setSize =  top.get(i).size();
            countNodes.put(topCKey, setSize);
            countNodes.put(topAKey, (importsCount == 0) ? 0 : setSize / importsCount);
        }

        return countNodes;
    }

    /**
     * Method that builds top import features
     * @param info important nodes info
     * @return set of top imports by level
     */
    private List<Set<String>> buildTopImports(@NotNull ImportantNodesInfo info) {
        List<Set<String>> top = new ArrayList<>();
        top.add(new HashSet<>());
        top.add(new HashSet<>());
        top.add(new HashSet<>());
        top.add(new HashSet<>());

        for (String imp: info.getImports()) {
            String cleaned = imp.substring("import ".length());
            String[] split = cleaned.split("\\.");
            String merged = "";
            for (int i = 0; i < Math.min(split.length, top.size()); ++i) {
                merged += split[i];
                top.get(i).add(merged);
                merged += ".";
            }
        }

        return top;
    }

    /**
     * Method that traverse over AST of file and collects features
     * @param currentNode current AST node
     * @param countNodes results map
     * @param info important nodes info
     * @throws GitAPIException is any git error occurred
     */
    private void traverseAST(Node currentNode,
                             HashMap<String, Double> countNodes,
                             ImportantNodesInfo info) throws GitAPIException {
        String name = currentNode.getClass().getName();
        String[] parts = name.split("\\.");
        if (parts.length > 0)
            name = parts[parts.length - 1];

        if (name.equals("VariableDeclarationExpr")){
            VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr)currentNode;
            info.getVarsTypes().add(variableDeclarationExpr.getType().toString());
        }

        if (name.equals("VariableDeclaratorId")){
            VariableDeclaratorId variableDeclaratorId = (VariableDeclaratorId)currentNode;
            info.getVarsNames().add(variableDeclaratorId.getName());
        }

        if (name.equals("PackageDeclaration")) {
            info.setPackageString(currentNode.toString());
        }

        if (name.startsWith("ImportDeclaration")) {
            ImportDeclaration importDeclaration = (ImportDeclaration)currentNode;

            info.getImports().add(importDeclaration.toString());
        }

        countNodes.put(name, countNodes.getOrDefault(name, 0.0) + 1);

        for (Node n: currentNode.getChildrenNodes()) {
            traverseAST(n, countNodes, info);
        }
    }

    private static AutomaticDatasetCreator init(@NotNull String datasetFileName) throws IOException {
        SparseCSVBuilder.sharedInstance = new SparseCSVBuilder(datasetFileName, GlobalConfig.nFeatures);
        return new AutomaticDatasetCreator();
    }

    private List<HashMap<String, Double>> runInner(@NotNull RepositoryDescription repositoryDescription) {
        GitService gitService = new GitServiceImpl();
        System.err.println("Starting clone " + repositoryDescription.getRepositoryName() + "...");
        Repository repo = null;
        try {
            repo = gitService
                    .cloneIfNotExists(repositoryDescription.getRepositoryName(), repositoryDescription.getRepositoryURL());
        } catch (Exception e) {
            return new ArrayList<>();
        }

        return runOnGitRepository(repo, repositoryDescription);
    }
}
