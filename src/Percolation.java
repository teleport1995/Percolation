public class Percolation {

    private class MyBitSet {
        private byte[] a;

        public MyBitSet(int N) { a = new byte[(N - 1) / 8 + 1]; }

        public boolean get(int x) {
            return (a[x >> 3] & (1 << (x & 7))) != 0;
        }

        public void set(int x) {
            a[x >> 3] |= 1 << (x & 7);
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
    private MyBitSet bottom;
    private int n;
    private WeightedQuickUnionUF uf;

    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("exception");
        n = N;
        a = new MyBitSet(n * n);
        bottom = new MyBitSet(n * n + 1);
        for (int i = 1; i <= n; i++)
            bottom.set(xyto1D(n, i));
        uf = new WeightedQuickUnionUF(n * n + 1);
    }

    /*private void dfs(int i, int j, int[] c, int [] d) {
        full.set(xyto1D(i, j));
        if (i == n)
            percolates = true;
        for (int k = 0; k < 4; k++) {
            if (isValid(i + c[k], j + d[k]) && a.get(xyto1D(i+c[k], j+d[k]))
                    && !full.get(xyto1D(i+c[k], j+d[k]))) {
                dfs(i + c[k], j + d[k], c, d);
            }
        }
    }*/

    private void connect(int i, int j) {
        int ni = uf.find(i), nj = uf.find(j);
        uf.union(ni, nj);
        if (bottom.get(ni) || bottom.get(nj))
            bottom.set(uf.find(ni));
    }

    public void open(int i, int j) {
        if (!isValid(i, j))
            throw new IndexOutOfBoundsException("exception");
        if (isOpen(i, j))
            return;
        int[] c = {0, 0, -1, 1};
        int[] d = {-1, 1, 0, 0};
        a.set(xyto1D(i, j));
        if (i == 1)
            connect(xyto1D(i, j), n * n);
        for (int k = 0; k < 4; k++)
            if (isValid(i + c[k], j + d[k]) && a.get(xyto1D(i+c[k], j+d[k]))) {
                connect(xyto1D(i, j), xyto1D(i + c[k], j + d[k]));
            }
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
        return uf.connected(xyto1D(i, j), n * n);
    }

    public boolean percolates() {
        return bottom.get(uf.find(n * n));
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