package ru.hse.kirilenko.refactorings.legacy;

import org.apache.commons.lang3.StringUtils;
import ru.hse.kirilenko.refactorings.RefactoringsExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class DatasetPostprocessor {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("f.csv");
        Scanner sc = new Scanner(file);
        File file1 = new File("t.csv");
        Scanner sc1 = new Scanner(file1);



        Random r = new Random();
        try(FileWriter fileWriterTrain = new FileWriter("fv_train.csv");
            FileWriter fileWriterTest = new FileWriter("fv_test.csv")) {
            final PrintWriter printWriterTrain = new PrintWriter(fileWriterTrain);
            final PrintWriter printWriterTest = new PrintWriter(fileWriterTest);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (r.nextDouble() < 0.8) {
                    printWriterTrain.println(line);
                } else {
                    printWriterTest.println(line);
                }
            }
            while (sc1.hasNextLine()) {
                String line = sc1.nextLine();
                if (r.nextDouble() < 0.8) {
                    printWriterTrain.println(line);
                } else {
                    printWriterTest.println(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private static void fixDelimiter() throws FileNotFoundException {
        File file = new File("all_new_false_dataset.csv");
        Scanner sc = new Scanner(file);

        int cnt = 0;
        try(FileWriter fileWriter = new FileWriter("all_new_false_dataset_fixed.csv")) {
            final PrintWriter printWriter = new PrintWriter(fileWriter);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String fixed = StringUtils.replace(line, ";", ",");
                printWriter.println(fixed);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void postprocess() throws FileNotFoundException {
        File file = new File("csvResults_trueDataset.csv");
        Scanner sc = new Scanner(file);
        HashSet<String> diffsSet = new HashSet<>();
        int total = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            diffsSet.add(line);
            total++;
        }

        System.out.println("UNIQUE LINES: " + (double)diffsSet.size() / total);
    }

    public static void smartPostprocess() throws FileNotFoundException {
        File file = new File("csvResults_trueDataset.csv");
        Scanner sc = new Scanner(file);
        HashMap<String, Integer> diffsSet = new HashMap<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            diffsSet.merge(line, 1, Integer::sum);
        }


        try(FileWriter fileWriter = new FileWriter("csvResults_Unique.csv")) {
            final PrintWriter printWriter = new PrintWriter(fileWriter);
            for (Map.Entry<String, Integer> entry: diffsSet.entrySet()) {
                String old = entry.getKey();
                //old += entry.getValue();
                printWriter.println(old);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void fixCsv(int N) throws FileNotFoundException {
        File file = new File("csvResults_falseDataset2.csv");
        Scanner sc = new Scanner(file);
        HashMap<String, Integer> diffsSet = new HashMap<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            line = line.replace(";", ",");

            int m = StringUtils.countMatches(line, ',');
            if (m != N) {
                System.out.println(m);
            } else {
                diffsSet.merge(line, 1, Integer::sum);
            }


        }
        //int cnt = diffsSet.entrySet().size();
        //Random r = new Random();
        //int targetSize = 1600;
        //double prob = (double)targetSize / cnt;
        String head = "";
        for (int i = 0; i < N; i++) {
            head += i;
            if (i == N - 1) {
                head += ",label";
            } else {
                head += ",";
            }
        }

        try(FileWriter fileWriter = new FileWriter("ress.csv")) {
            final PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(head);
            for (Map.Entry<String, Integer> entry: diffsSet.entrySet()) {
                String old = entry.getKey();
                //old += entry.getValue();
                //if (r.nextDouble() < prob) {
                    printWriter.println(old);
                //}

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
