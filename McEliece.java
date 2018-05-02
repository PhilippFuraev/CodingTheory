/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodingTheory;

import java.util.Random;

public class McEliece {

    int[][] G, S, P, nG;
    int[] z;
    int n, k, t;
    ReedSolomon solomon;

    public McEliece(int[][] G, int n, int k, int t, ReedSolomon solomon) {
        this.G = G;
        this.n = n;
        this.k = k;
        this.t = t;
        this.solomon = solomon;
        S = new int[k][k];
        Random rnd = new Random();
        do {
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    S[i][j] = rnd.nextInt(2);
                }
            }
        } while (determinant(S, k) == 0);
        P = new int[n][n];
        int[] taken = new int[n];
        for (int i = 0; i < n; i++) {
            int pos = rnd.nextInt(n - i);
            for (int j = 0; j < n; j++) {
                if (taken[j] != 0) {
                    pos++;
                    continue;
                }
                if (j == pos) {
                    P[i][j] = 1;
                    taken[j] = 1;
                }
            }
        }
        System.out.println("G:");
        for (int i = 0; i < G.length; i++) {
            for (int j = 0; j < G[0].length; j++) {
                System.out.print(G[i][j]);
            }
            System.out.println();
        }

        System.out.println("S:");
        for (int i = 0; i < S.length; i++) {
            for (int j = 0; j < S[0].length; j++) {
                System.out.print(S[i][j]);
            }
            System.out.println();
        }

        System.out.println("P:");
        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < P[0].length; j++) {
                System.out.print(P[i][j]);
            }
            System.out.println();
        }
        nG = product(product(S, G), P);
    }

    public class PublicKey {

        public int[][] nG;
        public int t;

        public PublicKey(int[][] nG, int t) {
            this.nG = nG;
            this.t = t;
        }
    }

    public class PrivateKey {

        public int[][] S, G, P;

        public PrivateKey(int[][] S, int[][] G, int[][] P) {
            this.S = S;
            this.G = G;
            this.P = P;
        }
    }

    public PublicKey getPubK() {
        return new PublicKey(nG, t);
    }

    public PrivateKey getPrivK() {
        return new PrivateKey(S, G, P);
    }

    public McEliece(PublicKey pubK) {
        this.nG = pubK.nG;
        this.t = pubK.t;
        n = nG[0].length;
        k = nG.length;
    }

    public McEliece(PrivateKey privK, ReedSolomon solomon) {
        this.S = privK.S;
        this.G = privK.G;
        this.P = privK.P;
        n = G[0].length;
        k = G.length;
        this.solomon = solomon;
    }

    public int[] encrypt(int[] m) {
        int[][] buf = new int[1][k];
        System.arraycopy(m, 0, buf[0], 0, k);
        buf = product(buf, nG);

        int[] c = buf[0];
        for (int i=0;i<c.length;i++)
            System.out.print(c[i]);
        z = new int[n];
        Random rnd = new Random();
        for (int i = 0; i < t; i++) {
            int pos = rnd.nextInt(n - i);
            for (int j = 0; j < n; j++) {
                if (z[j] != 0) {
                    pos++;
                    continue;
                }
                if (j == pos) {
                    z[j] = rnd.nextInt(n);
                }
            }
        }
        for (int i=0;i<c.length;i++)
            c[i]^=z[i];
        return c;
    }

    public int[] decrypt(int[] c) {
        int[][] rP = transpose(P);
        int[][] buf = new int[1][n];
        System.arraycopy(c, 0, buf[0], 0, n);
        buf = product(buf, rP);

        int[] nc = buf[0];
        /*
        for (int i=0;i<n;i++)
            System.out.print(buf[0][i] + " ");
         */
        nc = solomon.decode(nc);
        for (int i = 0; i < nc.length; i++) {
            System.out.print(nc[i] + " ");
        }
        System.out.println("codeword");
        /*
        int[][] rS = reverseMatrix(S);
        buf = new int[1][k];
        System.arraycopy(nc, 0, buf[0], 0, k);
        buf = product(buf, rS);
        int[] m = new int[k];
        System.arraycopy(buf[0], 0, m, 0, k);
        for (int i=0;i<m.length;i++)
            System.out.print(m[i] + " ");
        
         */
        return nc;
    }

    public static int determinant(int A[][], int N) {
        int res;
        switch (N) {
            case 1:
                res = A[0][0];
                break;
            case 2:
                res = A[0][0] * A[1][1] - A[1][0] * A[0][1];
                break;
            default:
                res = 0;
                for (int j = 0; j < N; j++) {
                    int[][] m = generateSubArray(A, N, 0, j);
                    res += Math.pow(-1.0, j) * A[0][j] * determinant(m, N - 1);
                }
                break;
        }
        return res;
    }

    public static int[][] generateSubArray(int A[][], int N, int i1, int j1) {
        int[][] m = new int[N - 1][N - 1];
        int i2 = 0;
        for (int i = 0; i < N; i++) {
            if (i == i1) {
                continue;
            }
            int j2 = 0;
            for (int j = 0; j < N; j++) {
                if (j == j1) {
                    continue;
                }
                m[i2][j2] = A[i][j];
                j2++;
            }
            i2++;
        }
        return m;
    }

    public int[][] product(int[][] m1, int[][] m2) {
        int[][] result = new int[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    result[i][j] += m1[i][k] * m2[k][j];
                }
                result[i][j] = (result[i][j] + 100) % n;
            }
        }
        return result;
    }

    public int[][] reverseMatrix(int[][] tm) {
        int[][] m = transpose(tm);
        int det = determinant(m, m.length);
        int[][] result = new int[m.length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                result[i][j] = (int) Math.pow(-1.0, i + j) * determinant(generateSubArray(m, m.length, i, j), m.length - 1) / det;
            }
        }
        return result;
    }

    public int[][] transpose(int[][] m) {
        int[][] result = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                result[j][i] = m[i][j];
            }
        }
        return result;
    }
}
