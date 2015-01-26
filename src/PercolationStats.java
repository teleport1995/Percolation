public class PercolationStats {
    private double[] results;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("exception");
        results = new double[T];
        for (int test = 0; test < T; test++) {
            Percolation perc = new Percolation(N);
            int cnt = 0;
            while (true) {
                if (perc.percolates())
                    break;
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);
                if (perc.isOpen(i, j))
                    continue;
                perc.open(i, j);
                cnt++;
            }
            results[test] = (cnt * 1.0) / (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(results);
    }

    public double stddev() {
        return StdStats.stddev(results);
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(results.length);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(results.length);
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(N, T);
        System.out.println("mean                    = " + test.mean());
        System.out.println("stddev                  = " + test.stddev());
        System.out.println("95% confidence interval = " + test.confidenceLo()
                + ", " + test.confidenceHi());
    }
}
