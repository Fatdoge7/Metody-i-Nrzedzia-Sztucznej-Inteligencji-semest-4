import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    private int[][] population;
    static Island variable;

    public Population(int[][] population, Island variable) {
        this.population = population;
        Population.variable = variable;
    }

    public static int[] generate(int length) {
        Random random = new Random();
        int[] tab = new int[length];

        for (int i = 0; i < length; i++) {
            tab[i] = random.nextInt(2);
        }
        return tab;
    }

    public static double decode(int[] chromosome, double lowerBound, double upperBound, int start, int end) {
        int decimal = 0;

        for (int i = start; i < end; i++) {
            decimal += chromosome[i] * Math.pow(2, end - i - 1);
        }
        return lowerBound + decimal * (upperBound - lowerBound) / (Math.pow(2, end - start) - 1);
    }

    public static void mutate(int[] table, double pm) {
        Random rand = new Random();

        for (int i = 0; i < table.length; i++) {
            if (rand.nextDouble() <= pm) {
                if (table[i] == 1) {
                    table[i] = 0;
                } else {
                    table[i] = 1;
                }
            }
        }
    }

    public double[] evolve() {
        Random rand = new Random();
        double sum = 0;
        double[] avg = {0, 0};

        for (int i = 0; i < variable.islandSize; i++) {
            Random random = new Random();
            int[] parentIndex1 = population[i];
            int[] parentIndex2 = population[random.nextInt(variable.islandSize)];
            double r = rand.nextDouble();
            int[] child;

            if (r <= variable.crossoverCoefficient) {
                child = crossover(parentIndex1, parentIndex2, variable.numVariablesInFunction);
            } else {
                child = population[i];
            }
            mutate(child, variable.mutationCoefficient);
            population[i] = child;
            double currentFitness = Rastrigin.value(child);
            sum += currentFitness;
        }
        avg[0] = average(sum, variable.islandSize);
        avg[1] = getBestFitness();
        return avg;
    }

    public static int[] crossover(int[] parent1, int[] parent2, int numPoints) {
        int length = Math.min(parent1.length, parent2.length);
        Random random = new Random();
        List<Integer> crossoverPoints = new ArrayList<>();

        for (int i = 0; i < numPoints; i++) {
            int point = random.nextInt(length - 1) + 1;
            crossoverPoints.add(point);
        }
        crossoverPoints.sort(null);
        int[] child = new int[length];
        boolean currentParentIs1 = true;
        int lastPoint = 0;

        for (int point : crossoverPoints) {
            if (currentParentIs1) {

                if (point - lastPoint >= 0) System.arraycopy(parent1, lastPoint, child, lastPoint, point - lastPoint);
            } else {
                if (point - lastPoint >= 0) System.arraycopy(parent2, lastPoint, child, lastPoint, point - lastPoint);
            }
            currentParentIs1 = !currentParentIs1;
            lastPoint = point;
        }

        if (currentParentIs1) {
            if (length - lastPoint >= 0) System.arraycopy(parent1, lastPoint, child, lastPoint, length - lastPoint);
        } else {
            if (length - lastPoint >= 0) System.arraycopy(parent2, lastPoint, child, lastPoint, length - lastPoint);
        }
        return child;
    }

    public double getBestFitness() {
        double bestFitness = Double.MAX_VALUE;

        for (int[] individual : population) {
            double fitness = Rastrigin.value(individual);

            if (fitness < bestFitness) {
                bestFitness = fitness;
            }
        }
        return bestFitness;
    }


    public static int precision(double ai, double bi, int di) {
        double x = bi - ai;

        for (int i = 0; i < di; i++) {
            x = x * 10;
        }
        return (int) x;
    }

    public static int bitsNeeded(int x) {
        int y = (int) (Math.ceil(Math.log(x) / Math.log(2)));
        return y;
    }

    public double average(double value, int count) {
        return value / count;
    }

    public void addIndividual(int index, int[] individual) {
        population[index] = individual;
    }

    public int[] getIndividual(int index) {
        return population[index];
    }
}
