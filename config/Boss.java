import java.util.*;
import java.io.*;
import java.math.*;

class Player {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        System.out.println("yes");
        Random random = new Random(412);

        while (true) {
            int height = in.nextInt();
            for(int i = 0; i < height; ++i) {
                String inp = in.next();
            }

            String ans = "";
            int actionCount = in.nextInt();
            int r = random.nextInt(actionCount);
            for (int i = 0; i < actionCount; ++i) {
                String s = in.next();
                if(r == i) {
                    ans = s;
                }
            }

            System.out.println(ans);
        }
    }
}
