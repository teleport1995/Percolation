import java.util.BitSet;

public class Percolation {
    private BitSet a;
    private BitSet full;
    private int n;
    private WeightedQuickUnionUF uf;
    private int[] c = {0, 0, -1, 1};
    private int[] d = {-1, 1, 0, 0};

    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("exception");
        n = N;
        a = new BitSet(N * N);
        full = new BitSet(N * N);
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    private void dfs(int i, int j) {
        full.set(xyto1D(i, j));
        for (int k = 0; k < 4; k++) {
            if (isValid(i + c[k], j + d[k]) && a.get(xyto1D(i+c[k], j+d[k]))
                    && !full.get(xyto1D(i+c[k], j+d[k]))) {
                dfs(i + c[k], j + d[k]);
            }
        }
    }

    public void open(int i, int j) {
        if (!isValid(i, j))
            throw new IndexOutOfBoundsException("exception");
        if (isOpen(i, j))
            return;
        a.set(xyto1D(i, j));
        boolean needtofull = false;
        if (i == 1)
            needtofull = true;
        for (int k = 0; k < 4; k++)
            if (isValid(i + c[k], j + d[k]) && a.get(xyto1D(i+c[k], j+d[k]))) {
                if (full.get(xyto1D(i+c[k], j+d[k])))
                    needtofull = true;
                uf.union(xyto1D(i, j), xyto1D(i + c[k], j + d[k]));
            }
        if (needtofull)
            dfs(i, j);
        if (i == 1)
            uf.union(xyto1D(i, j), n * n);
        if (i == n)
            uf.union(xyto1D(i, j), n * n + 1);
    }

    private boolean isValid(int i, int j) {
        return i >= 1 && i <= n && j >= 1 && j <= n;
    }

    private int xyto1D(int i, int j) {
        return (i - 1) * n + j - 1;
    }

    public boolean isOpen(int i, int j) {
        if (!isValid(i, j))
            throw new IndexOutOfBoundsException("exception");
        return a.get(xyto1D(i, j));
    }

    public boolean isFull(int i, int j) {
        if (!isValid(i, j))
            throw new IndexOutOfBoundsException("exception");
        return full.get(xyto1D(i, j));
    }

    public boolean percolates() {
        return uf.connected(n * n, n * n + 1);
    }


    public static void main(String[] args) {
        int[] c = {0, 0, -1, 1};
        int[] d = {-1, 1, 0, 0};
        for (int i = 0; i < 4; i++)
            System.out.println(c[i] + " " + d[i]);
        Percolation perc = new Percolation(3);
        perc.open(1, 3);
        perc.open(2, 3);
        perc.open(3, 3);
        perc.open(3, 1);
        System.out.println(perc);
    }
}