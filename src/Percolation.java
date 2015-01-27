public class Percolation {

    private class MyBitSet {
        private byte[] a;

        public MyBitSet(int N) {
            a = new byte[(N - 1) / 8 + 1];
        }

        public boolean get(int x) {
            return (a[x / 8] & (1 << (x % 8))) != 0;
        }

        public void set(int x) {
            a[x / 8] |= 1 << (x % 8);
        }

    }

    private class MyUF {
        private int[] id;

        public MyUF(int N) {
            id = new int[N];
            for (int i = 0; i < N; i++) {
                id[i] = i;
            }
        }
        public int find(int p) {
            if (id[p] == p)
                return p;
            id[p] = find(id[p]);
            return id[p];
        }
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            id[rootQ] = id[rootP];
        }
    }

    private MyBitSet a;
    private MyBitSet full;
    private int n;
    private MyUF uf;
    private WeightedQuickUnionUF dop;

    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("exception");
        n = N;
        a = new MyBitSet(N * N);
        full = new MyBitSet(N * N);
        uf = new MyUF(n * n + 2);
        dop = new WeightedQuickUnionUF(2);
    }

    private void dfs(int i, int j, int[] c, int [] d) {
        full.set(xyto1D(i, j));
        for (int k = 0; k < 4; k++) {
            if (isValid(i + c[k], j + d[k]) && a.get(xyto1D(i+c[k], j+d[k]))
                    && !full.get(xyto1D(i+c[k], j+d[k]))) {
                dfs(i + c[k], j + d[k], c, d);
            }
        }
    }

    public void open(int i, int j) {
        if (!isValid(i, j))
            throw new IndexOutOfBoundsException("exception");
        if (isOpen(i, j))
            return;
        dop.union(0, 1);
        int[] c = {0, 0, -1, 1};
        int[] d = {-1, 1, 0, 0};
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
            dfs(i, j, c, d);
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
        dop.connected(0, 1);
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