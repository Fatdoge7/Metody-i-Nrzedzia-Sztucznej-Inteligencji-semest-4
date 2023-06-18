import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Save {
    private static final String[] fileNames = {"Current_Value.txt", "Best_Value.txt", "Global_Best_Value.txt"};

    public static void clearData() {
        for (String fileName : fileNames) {
            try {
                File file = new File(fileName);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.close();
            }
            catch (IOException error) {
                error.printStackTrace();
            }
        }
    }


    public static void saveData(int numExecutions, Island civilization) {
        Timer timer = new Timer();
        timer.start();
        for (int i = 1; i <= numExecutions; i++) {
            double[] data;
            String record = "";
            data = civilization.start();
            try {
                FileWriter currentFileWriter = new FileWriter(fileNames[0], true);
                FileWriter bestFileWriter = new FileWriter(fileNames[1], true);
                FileWriter globalFileWriter = new FileWriter(fileNames[2], true);
                String currentRecord = "";
                String bestRecord = "";
                String globalRecord = "";

                for (int j = 0; j < data.length; j += 3) {
                    currentRecord += String.valueOf(data[j]) + ";";
                }

                for (int j = 1; j < data.length; j += 3) {
                    bestRecord += String.valueOf(data[j]) + ";";
                }

                for (int j = 2; j < data.length; j += 3) {
                    globalRecord += String.valueOf(data[j]) + ";";
                }

                currentFileWriter.write(currentRecord + "\n");
                bestFileWriter.write(bestRecord + "\n");
                globalFileWriter.write(globalRecord + "\n");
                currentFileWriter.close();
                bestFileWriter.close();
                globalFileWriter.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
            System.out.println("Test No: " + i);
            timer.checkTimer();
        }
        System.out.println("Completed");
        timer.checkTimer();
        timer.stop();
    }
}
