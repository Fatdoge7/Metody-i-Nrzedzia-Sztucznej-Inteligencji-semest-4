import java.util.Random;

public class Island {

    public double crossoverCoefficient = 0.6; // współczynnik krzyżowania
    public double mutationCoefficient = 0.9; // współczynnik mutacji
    public static double lowerBound = -5.05; // dolna granica funkcji
    public static double upperBound = 5.05; // górna granica funkcji
    public int migrationInterval = 10; // czestotliowść migracjii
    public double globalBest = Double.MAX_VALUE;
    public int numExecutions;
    public int numIslands;
    public int islandSize;
    public int numVariablesInFunction;
    private int EV = 10000;
    private Population[] island;

    public Island(int numIslands, int islandSize, int numVariablesInFunction, int numExecutions) {
        this.numIslands = numIslands;
        this.islandSize = islandSize;
        this.numVariablesInFunction = numVariablesInFunction;
        this.island = new Population[numIslands];
        this.numExecutions = numExecutions;
    }

    public double[] start() {
        double[] bestFitness = new double[numIslands];
        double[] record = new double[((EV / 50)+1) * 3];
        int recordIndex = 0;

        for (int i = 0; i < numIslands; i++) {
            bestFitness[i] = Double.MAX_VALUE;
        }
        generatePopulation(islandSize);

        for (int Generations = 0; Generations <= EV; Generations++)
        {
            double currentAverage = 0;
            double bestAverage = 0;

            for (int IslandIndex = 0; IslandIndex < numIslands; IslandIndex++)
            {
                double[] currentValue = {0, 0};
                Population currentPopulation = island[IslandIndex];
                currentValue = currentPopulation.evolve();
                currentAverage += currentValue[0];

                if (Generations % 50 == 0) {

                    if (bestFitness[IslandIndex] > currentValue[1]) {
                        bestFitness[IslandIndex] = currentValue[1];
                    }
                }
            }
            currentAverage /= numIslands;

            if (Generations % migrationInterval == 0) {
                migrate();
            }

            if (Generations % 50 == 0) {
                if (globalBest > findGlobalBestFitness()) {
                    globalBest = findGlobalBestFitness();
                }
                bestAverage = calculateAverageBestFitness(bestFitness);

                record[recordIndex] = currentAverage;
                record[recordIndex + 1] = bestAverage;
                record[recordIndex + 2] = globalBest;
                recordIndex += 3;
            }
        }
        return record;
    }

    private void migrate() {
        Random random = new Random();

        for (int islandIndex = 0; islandIndex < numIslands; islandIndex++)
        {

            int destinationIslandIndex = (islandIndex + 1) % numIslands;
            int migrationIndex = random.nextInt(islandSize);
            int[] migratingIndividual = island[islandIndex].getIndividual(migrationIndex);
            int[] expelledFromIsland = island[destinationIslandIndex].getIndividual(migrationIndex);
            swapIsland(migratingIndividual, destinationIslandIndex, expelledFromIsland, islandIndex, migrationIndex);
        }
    }

    private void swapIsland(int[] individual1, int destination, int[] individual2, int exchange, int migrationIndex) {
        island[destination].addIndividual(migrationIndex, individual1);
        island[exchange].addIndividual(migrationIndex, individual2);

    }

    private double findGlobalBestFitness() {
        double globalBestFitness = Double.MAX_VALUE;

        for (Population currentIsland : island) {
            double bestFitness = currentIsland.getBestFitness();
            if (bestFitness < globalBestFitness) {
                globalBestFitness = bestFitness;
            }
        }
        return globalBestFitness;
    }

    public double calculateAverageBestFitness(double[] array) {
        double average = 0;

        for (double v : array) {
            average += v;
        }
        average /= array.length;
        return average;
    }

    public void generatePopulation(int N) {
        for (int islandSize = 0; islandSize < numIslands; islandSize++) {
            int[][] population = new int[N][19 * numVariablesInFunction];
            int temp[];
            double x[] = new double[numVariablesInFunction];

            for (int i = 0; i < N; i++) {
                temp = Population.generate(numVariablesInFunction * Population.bitsNeeded(Population.precision(-2, 2, 5)));
                int end = 0;
                int count = 0;
                int length = 0;

                for (int start = 0; start < (temp.length); start += 19) {
                    end += 19;
                    x[length] = Population.decode(temp, lowerBound, upperBound, start, end);
                    length++;
                }

                while (!(count == numVariablesInFunction)) {
                    while (!(x[count] >= lowerBound && x[count] <= upperBound)) {
                        end = 0;
                        length = 0;
                        temp = Population.generate(numVariablesInFunction * Population.bitsNeeded(Population.precision(-2, 2, 5)));
                        for (int start_nap = 0; start_nap < (temp.length); start_nap += 19) {
                            end += 19;
                            x[length] = Population.decode(temp, lowerBound, upperBound, start_nap, end);
                            length++;
                        }
                        count = 0;
                    }
                    count++;
                }
                population[i] = temp;
            }
            island[islandSize] = new Population(population, this);
        }
    }
}

