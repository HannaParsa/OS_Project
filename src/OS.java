import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Scanner;

public class OS {

    public static int frame;

    public static Queue<Integer> fifo = new LinkedList<>();
    public static int fifo_ctr = 0;
    public static int fifo_fault = 0;

    public static Deque<Integer> lru = new LinkedList<>();
    public static HashSet<Integer> hashSet = new HashSet<>();
    public static int lru_fault = 0;

    public static Queue<Integer> s_help = new LinkedList<>();
    public static Queue<Integer> s_help2 = new LinkedList<>();
    public static HashMap<Integer, Integer> second_chance = new HashMap<>();
    public static int chance_fault = 0;

    // fifo algorithm
    public static void fifoCheck(int num) {
        if (fifo_ctr == frame) {
            if (!fifo.contains(num)) {
                fifo.remove();
                fifo.add(num);
                fifo_fault++;
            }
        } else {
            fifo.add(num);
            fifo_fault++;
            fifo_ctr++;
        }
    }

    // lru algorithm
    public static void lruCheck(int num) {
        if (!hashSet.contains(num)) {
            lru_fault++;
            if (lru.size() == frame) {
                int last = lru.removeLast();
                hashSet.remove(last);
            }
        } else {
            lru.remove(num);
        }
        lru.push(num);
        hashSet.add(num);
    }

    // second-chance algorithm
    public static void sChanceCheck(int num) {
        if (!second_chance.containsKey(num)) {
            if (second_chance.size() == frame) {
                second_chance.remove(s_help.poll());
                s_help.add(num);
                second_chance.put(num, 0);
            } else {
                second_chance.put(num, 0);
                s_help.add(num);
            }
            chance_fault++;
        } else {
            second_chance.replace(num, 1);
            for (Integer item : s_help) {
                if (item != num) {
                    s_help2.add(item);
                }
            }
            s_help2.add(num);
            s_help.clear();
            s_help.addAll(s_help2);
            s_help2.clear();
        }
    }

    public static void main(String[] args) throws IOException {
        int num;
        //String lruString="";

        Scanner scanner = new Scanner(System.in);
        frame = 3;
        System.out.println("frame number is: " + frame);
      /*System.out.println("enter the numbers");
        count = scanner.nextInt();*/
        num = scanner.nextInt();
        while (num != -1) {
            fifoCheck(num);
            lruCheck(num);
            sChanceCheck(num);
            //Iterator<Integer> itr = lru.iterator();
            //lruString =lruString+", "+ itr.next();
            System.out.println(fifo);
            //System.out.println(lruString);
            System.out.println(hashSet);
            System.out.println(second_chance);
            System.out.println("_________________");
            num = scanner.nextInt();
        }
        System.out.println("LRU:<" + lru_fault + ">," + "FIFO:<" + fifo_fault + ">," + "Second-chance:<" + chance_fault + ">");
    }
}