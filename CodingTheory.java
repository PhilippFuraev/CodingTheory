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
public class CodingTheory{

    public static void main(String[] args) {
        int k=9;
        ReedSolomon r = new ReedSolomon(k);
        
        int[] message = {9,3,1,2,9,0,13,5,7};
        //int[] message = {9,3,1,2,9,0,13,5,7,13,6,14,15,15,3};
       message = r.code(message);
        for (int i = 0; i < message.length; i++) {
            System.out.print(message[i] + " ");
        }
        System.out.println();
        message[0] ^= 2;
       //message[2] +=3;
        int[] e = {2, 0, 6, 0, 0, 14, 0, 0};
        int[] e2 = {4,0,0,0,2,0,0,6};
        //for (int i=0;i<e2.length;i++)
            //message[i]^=e2[i];
        for (int i = 0; i < message.length; i++) {
            System.out.print(message[i] + " ");
        }
        System.out.println();
        int[] ost = r.fModule(message);
        r.decode(message);
        
        
        McEliece mc = new McEliece (r.gMatr,15,k, ((15-k)/2), r);
        int [] encrypt = mc.encrypt(message);
        int [] mess = mc.decrypt(encrypt);


    }

}
