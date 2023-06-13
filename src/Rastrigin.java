public class Rastrigin {
    public static double fun(double[] x) {
        int n = x.length;
        double A = 10.0;
        double sum1 = 0.0;
        double sum2 = 0.0;

        for (double v : x) {
            sum1 += v * v;
            sum2 += Math.cos(2 * Math.PI * v);
        }
        return A * n + sum1 - A * sum2;
    }

    public static double value(int[] population) {
        double[] x = new double[Population.variable.numVariablesInFunction];
        int end = 0;
        int length = 0;

        for (int start = 0; start < (Population.variable.numVariablesInFunction * 19) - 19; start += 19) {
            end += 19;
            x[length] = Population.decode(population, Island.lowerBound, Island.upperBound, start, end);
            length++;
        }
        return fun(x);
    }
}
