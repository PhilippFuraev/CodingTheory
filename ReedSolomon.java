/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodingTheory;

/**
 *
 * @author furae_000
 */
public class ReedSolomon {

    public static int[] m = {1, 0, 0, 0, 1};
    public int[] g;
    public int[] x;
    public int[] a;
    public int d;
    public int k;
    public int n;
    public int primitiveElement;
    public int mod;
    public int lengthOfFX;
    public int fX;
    public int[] indexOf;
    public int[] c;
    public int[][] gMatr;

    public ReedSolomon(int k) {
        n = 15;
        c = new int[n];
        indexOf = new int[n + 1];
        fX = 19;
        this.k = k;
        for (int i = 31; i >= 0; i--) {
            if (((fX >>> i) & 1) == 1) {
                lengthOfFX = i;
                break;
            }
        }
        a = new int[15];
        d = n - k + 1;
        a[0] = 1;
        indexOf[1] = 0;
        a[1] = 2;
        indexOf[2] = 1;
        a[2] = 4;
        indexOf[4] = 2;
        a[3] = 8;
        indexOf[8] = 3;
        a[4] = 3;
        indexOf[3] = 4;
        a[5] = 6;
        indexOf[6] = 5;
        a[6] = 12;
        indexOf[12] = 6;
        a[7] = 11;
        indexOf[11] = 7;
        a[8] = 5;
        indexOf[5] = 8;
        a[9] = 10;
        indexOf[10] = 9;
        a[10] = 7;
        indexOf[7] = 10;
        a[11] = 14;
        indexOf[14] = 11;
        a[12] = 15;
        indexOf[15] = 12;
        a[13] = 13;
        indexOf[13] = 13;
        a[14] = 9;
        indexOf[9] = 14;
        g = new int[d - 1];
        g = new int[2];
        g[0] = 1;
        g[1] = a[1];
        int[] per = new int[2];
        for (int i = 2; i <= d - 1; i++) {
            per[0] = 1;
            per[1] = a[i];
            g = mulBrackets(g, per);
        }
        gMatr = new int[k][n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n - k + 1; j++) {
                gMatr[i][i + j] = g[n - k - j];
            }
        }

    }

    public int[] mulBrackets(int[] g, int[] sc) {
        for (int i = 0; i < g.length; i++) {
            if (g[i] < 0) {
                g[i] = n + 1 + g[i];
            }
        }
        for (int i = 0; i < sc.length; i++) {
            if (sc[i] < 0) {
                sc[i] = n + 1 + sc[i];
            }
        }
        int[] newG = new int[(g.length - 1) + (sc.length - 1) + 1];
        for (int i = g.length - 1; i >= 0; i--) {
            for (int j = sc.length - 1; j >= 0; j--) {
                newG[i + j] = (newG[i + j] ^ module(multiply(g[i], sc[j])));
                //per = module((newG[i + j] ^ (multiply(g[i], sc[j]))));
            }
        }
        return newG;
    }

    public int[] mulBracketsNotGalua(int[] g, int[] sc) {
        for (int i = 0; i < g.length; i++) {
            if (g[i] < 0) {
                g[i] = n + 1 + g[i];
            }
        }
        for (int i = 0; i < sc.length; i++) {
            if (sc[i] < 0) {
                sc[i] = n + 1 + sc[i];
            }
        }
        int[] newG = new int[(g.length - 1) + (sc.length - 1) + 1];
        for (int i = g.length - 1; i >= 0; i--) {
            for (int j = sc.length - 1; j >= 0; j--) {
                newG[i + j] = (newG[i + j] + module(g[i] * sc[j])) % (n + 1);
                //per = module((newG[i + j] ^ (multiply(g[i], sc[j]))));
            }
        }
        return newG;
    }

    public int[] code(int[] message) {
        int[] codeMessage = new int[n];
        for (int i = 0; i < k; i++) {
            codeMessage[i] = message[i];
        }
        int[] ost = fModule(codeMessage);
        int zeros = n - k - ost.length;
        for (int i = 0; i < ost.length; i++) {
            codeMessage[k + zeros + i] = ost[i];
        }
        return codeMessage;
    }

    /*
    public int[]code(int[]message)
    {
        return mulBrackets(g,message);
    }
     */
    public int module(int message) {
        int moduleMessage = message;
        for (int i = 31; i >= lengthOfFX; i--) {
            if (((moduleMessage >>> i) & 1) == 1) {
                moduleMessage ^= fX << (i - lengthOfFX);
            }
        }
        return moduleMessage;
    }

    public int[] fModule(int[] message) {
        int messageDeg = message.length;
        int[] ost;
        ost = new int[messageDeg];
        int gDeg = g.length;
        int sub = messageDeg - gDeg;
        System.arraycopy(message, 0, ost, 0, message.length);
        //ost = message;
        int nullDeg = 0;
        int[] result = new int[messageDeg];
        System.arraycopy(message, 0, result, 0, message.length);
        while (sub > 0) {
            int[] tmp = new int[sub + 1];
            ost = result;
            tmp[0] = result[0];
            tmp = mulBrackets(g, tmp);
            for (int i = 0; i < ost.length; i++) {
                ost[i] = module(ost[i] ^ tmp[i]);
                //ost[i] = (n+1 + ost[i] - tmp[i])%(n+1);
            }
            nullDeg = 0;
            for (int i = 0; i < ost.length; i++) {
                if (ost[i] == 0) {
                    nullDeg = i + 1;
                } else {
                    break;
                }
            }
            result = new int[ost.length - nullDeg];
            for (int i = 0; i < result.length; i++) {
                result[i] = ost[i + nullDeg];
            }
            ost = new int[ost.length - nullDeg];
            sub = ost.length - gDeg;
        }
        if (sub == 0) {
            ost = result;
            int tmp[] = new int[1];
            tmp[0] = ost[0];
            tmp = mulBrackets(g, tmp);
            for (int i = 0; i < gDeg; i++) {
                ost[i] = (ost[i] ^ tmp[i]);
            }
        }
        nullDeg = 0;
        for (int i = 0; i < ost.length; i++) {
            if (ost[i] == 0) {
                nullDeg = i + 1;
            } else {
                break;
            }
        }
        result = new int[ost.length - nullDeg];
        for (int i = 0; i < result.length; i++) {
            result[i] = ost[i + nullDeg];
        }
        return result;
    }

    public int[] fModule2(int[] message, int[] mod) {
        int messageDeg = message.length;
        int[] ost;
        ost = new int[messageDeg];
        int gDeg = mod.length;
        int sub = messageDeg - gDeg;
        System.arraycopy(message, 0, ost, 0, message.length);
        //ost = message;
        int nullDeg = 0;
        int[] result = new int[messageDeg];
        System.arraycopy(message, 0, result, 0, message.length);
        while (sub > 0) {
            int[] tmp = new int[sub + 1];
            ost = result;
            tmp[0] = result[0];
            tmp = mulBrackets(mod, tmp);
            for (int i = 0; i < ost.length; i++) {
                ost[i] = module(ost[i] ^ tmp[i]);
                //ost[i] = (n+1 + ost[i] - tmp[i])%(n+1);
            }
            nullDeg = 0;
            for (int i = 0; i < ost.length; i++) {
                if (ost[i] == 0) {
                    nullDeg = i + 1;
                } else {
                    break;
                }
            }
            result = new int[ost.length - nullDeg];
            for (int i = 0; i < result.length; i++) {
                result[i] = ost[i + nullDeg];
            }
            ost = new int[ost.length - nullDeg];
            sub = ost.length - gDeg;
        }
        if (sub == 0) {
            ost = result;
            int tmp[] = new int[1];
            tmp[0] = ost[0];
            tmp = mulBrackets(mod, tmp);
            for (int i = 0; i < gDeg; i++) {
                ost[i] = (ost[i] ^ tmp[i]);
            }
        }
        nullDeg = 0;
        for (int i = 0; i < ost.length; i++) {
            if (ost[i] == 0) {
                nullDeg = i + 1;
            } else {
                break;
            }
        }
        result = new int[ost.length - nullDeg];
        for (int i = 0; i < result.length; i++) {
            result[i] = ost[i + nullDeg];
        }
        return result;
    }

    public int multiply(int x, int y) {
        if (x == 0 || y == 0) {
            return 0;
        }
        int tmp1, tmp2;
        tmp1 = 0;
        tmp2 = 0;
        for (int i = 0; i < a.length; i++) {
            if (x == a[i]) {
                tmp1 = i;
            }
            if (y == (a[i])) {
                tmp2 = i;
            }
        }
        return a[(tmp1 + tmp2) % (n)];

    }

    public int multiply2(int x, int y) {
        if (x == 0 || y == 0) {
            return 0;
        }
        int tmp1, tmp2;
        tmp1 = 0;
        tmp2 = 0;
        for (int i = 0; i < a.length; i++) {
            if (x == a[i]) {
                tmp1 = i;
            }
            if (y == (a[i])) {
                tmp2 = i;
            }
        }
        return a[(tmp1 + tmp2) % (n)];

    }

private int BerlekampMassey(int[] s) {
        int L, round, m, d;
        int n = s.length;
        c = new int[n];
        int[] b = new int[n];
        int[] t = new int[n];
        b[0] = c[0] = 1;
        round = 1;
        L = 0;
        while (round < n+1) {
            
            d = s[round - 1];
            //d = 0;
            for (int i = 1; i <= L; i++) {
                if (c[i] != 0) {
                    d ^= a[(indexOf[c[i]] + indexOf[s[round - 1 - i]]) % this.n];
                }
            }
            if (d != 0) {
                for (int i = 0; i < t.length; i++) {
                    if (i > 0) {
                        if (b[i - 1] != 0) {
                            t[i] = c[i] ^ a[(indexOf[d] + indexOf[b[i - 1]]) % this.n];
                        }
                    } else {
                        t[i] = c[i];
                    }
                }
                if (2 * L <= round - 1) {
                    for (int i = 0; i < b.length; i++) {
                        if (c[i] != 0) {
                            b[i] = a[(this.n - indexOf[d] + indexOf[c[i]]) % this.n];
                        }
                    }
                    L = round - L;
                    System.arraycopy(t, 0, c, 0, n);
                } else {
                    System.arraycopy(t, 0, c, 0, n);
                    for (int i = b.length - 1; i >= 1; i--) {
                        b[i] = b[i - 1];
                    }
                    b[0] = 0;
                }
            } else {
                for (int i = b.length - 1; i >= 1; i--) {
                    b[i] = b[i - 1];
                }
                b[0] = 0;
            }
            round++;
        }
        return L;
    }

    public int[] fourney(int[] roots, int[] s) {
        int[] reverseS = new int[s.length];
                int[] derivativeL = new int[c.length];

        for (int i = 0; i < s.length / 2; i++) {
            int tmp = s[i];
            s[i] = s[s.length - 1 - i];
            s[s.length - 1 - i] = tmp;
        }

        for (int i = 0; i < c.length / 2; i++) {
            int tmp = c[i];
            c[i] = c[s.length - 1 - i];
            c[c.length - 1 - i] = tmp;
        }
        System.arraycopy(s, 0, reverseS, 0, s.length);
        System.arraycopy(c, 0, derivativeL, 0, c.length);

        int[] W = mulBrackets(reverseS, c);
        int nkDef = W.length - (n - k);
        for (int i = 0; i < nkDef; i++) {
            W[i] = 0;
        }
        for (int i = derivativeL.length; i > 0; i = i - 2) {
            derivativeL[i - 1] = derivativeL[i - 2];
            derivativeL[i - 2] = 0;
        }
        int Y[] = new int[roots.length];
        for (int i = 0; i < roots.length; i++) {
            int tmp1 = 0;
            int tmp2 = 0;
            for (int j = 0; j < W.length; j++) {
                tmp1 ^= multiply2(W[j], a[((n - roots[i]) * (W.length - 1 - j)) % n]);
            }
            for (int j = 0; j < derivativeL.length; j++) {
                tmp2 ^= multiply2(derivativeL[j], a[((n - roots[i]) * (derivativeL.length - 1 - j)) % n]);
            }
            Y[i] = a[(indexOf[tmp1] - indexOf[tmp2] + n) % n];
        }
        int[] e = new int[n];
        for (int i = 0; i < roots.length; i++) {
            e[n - 1 - roots[i]] = Y[i];
        }
        return e;
    }

    public int[] decode(int[] x) {
        int[] b = new int[n];
        int sub = n - x.length;
        for (int i = 0; i < x.length; i++) {
            b[i + sub] = x[i];
        }

        int[] decodedMessage = new int[n];
        System.arraycopy(b, 0, decodedMessage, 0, n);
        int[] s = new int[2 * ((n - k) / 2)];
        for (int i = 1; i <= s.length; i++) {
            for (int j = 0; j < n; j++) {
                if (b[j] != 0) {
                    s[i - 1] ^= multiply2(b[j], a[(i * (n - 1 - j)) % n]);
                }
            }
        }
        int L = BerlekampMassey(s);
        int[] lArray = toBinaryArray(L);
        int degc;
        for (degc = c.length - 1; degc >= 0; degc--) {
            if (c[degc] != 0) {
                break;
            }
        }
        if (L != degc) {
            System.out.println("Too many mistakes");
        } else {

            int[] roots = new int[L];
            int rootNumber = 0;
            for (int i = 0; i < n; i++) {
                int result = 0;
                for (int j = 0; j < c.length; j++) {
                    if (c[j] != 0) {
                        result ^= multiply2(c[j], a[(i * (c.length - 1 - j)) % n]);
                        if (i == 2) {
                        }
                    }
                }
                if (result == 0) {
                    roots[rootNumber] = i;
                    rootNumber++;
                }
            }

            /*
            int[] roots = new int[L];
            int rootNumber = 0;
            int result = 0;
            for (int i = 0; i < n; i++) {
                result = 0;

                for (int j = 0; j < lArray.length; j++) {
                    result ^= multiply2(lArray[j], a[(i*(n-1-j))%n]);
                }
                if (result == 0) {
                    roots[rootNumber] = i;
                    rootNumber++;
                }
            }
             */
            System.out.print("Mistakes at positions:");
            for (int i = 0; i < rootNumber; i++) {
                System.out.print(" " + (roots[i]));

            }
            System.out.println();
            System.out.print("wrong message: = ");
            for (int i = 0; i < n; i++) {
                System.out.print(b[i] + " ");
            }
            System.out.println();
            int[] errorVector = fourney(roots, s);
            for (int i = 0; i < errorVector.length; i++) {
                if (errorVector[i] != 0) {
                    decodedMessage[i] ^= errorVector[i];
                }
            }
            System.out.print("\ncorrect message: = ");
            for (int i = 0; i < n; i++) {
                System.out.print(decodedMessage[i] + " ");
            }
            System.out.println();
        }
        return decodedMessage;
    }

    public int[] toBinaryArray(int x) {
        int xDeg = 0;
        int[] result;
        for (int i = 31; i >= 0; i--) {
            if (((x >>> i) & 1) == 1) {
                xDeg = i;
                break;
            }
        }
        result = new int[xDeg + 1];
        for (int i = 0; i < xDeg + 1; i++) {
            if ((x & (1 << (i))) != 0) {
                result[xDeg - i] = 1;
            }
        }
        return result;
    }



}
