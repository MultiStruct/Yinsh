import java.util.Random;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        int turn = 0;
        System.out.println("yes");
        while (true) {

            ++turn;
            int height = in.nextInt();
            for(int i = 0; i < height; ++i) {
                String inp = in.next();
            }

            String ans = "";
            int actionCount = in.nextInt();
            //412
            int r = new Random(412).nextInt(actionCount);
            for (int i = 0; i < actionCount; ++i) {
                String s = in.next();
                if(r == i) {
                    ans = s;
                }
            }

            if(turn == 1 && id == 2) {
                System.out.println("STEAL");
            } else
                System.out.println(ans+";");
        }
    }
}
