public class Main {

    public static void main(String[] args) {

        int numVariablesInFunction = 10; //n = 10, 20, 30
        int islandSize = 100; //xP = 100, 200
        int numIslands = 6; //L.wysp = 6
        int numExecutions = 30; //reps = 30

        Save.clearData();

        Island civilization = new Island(numIslands, islandSize, numVariablesInFunction, numExecutions);

        Save.saveData(numExecutions, civilization);
    }
}
