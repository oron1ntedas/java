package bsu.rfe.java.group8.lab1.Slavinski.varA7;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Food[] breakfast = new Food[20];
        int count = 0;
        /*ArrayList<String> list = new ArrayList<>();
        String arg = scan.nextLine();
        while (!(arg.isEmpty())) {
            list.add(arg);
            arg = scan.nextLine();
        }
        */

        int itemsSoFar = 0;
        String[] obj = args;
        int size = args.length;

        int i;
        for(i = 0; i < size; ++i) {
            String arg = obj[i];
            String[] parts = arg.split("/");
        //for (String s : list) {
        //    String[] parts = s.split("/");
            switch (parts[0]) {
                case "Apple" -> breakfast[itemsSoFar] = new Apple(parts[1]);
                case "Cheese" -> breakfast[itemsSoFar] = new Cheese();
                case "IceCream" -> breakfast[itemsSoFar] = new IceCream(parts[1]);
                default -> itemsSoFar--;
            }
            itemsSoFar++;
        }

        countFood(breakfast, new Apple(""));
        countFood(breakfast, new Cheese());
        countFood(breakfast, new IceCream(""));


        for (Food item: breakfast){
            if (item!=null){
                item.consume();
            }
            else break;
        }


        System.out.println("Всего хорошего!");
    }

    public static void countFood(Food[] foods, Food food) {
        int count = 0;
        Food[] obj = foods;
        int size = foods.length;

        for(int i = 0; i < size; ++i) {
            Food f = obj[i];
            if (f != null && f.equals(food)) {
                ++count;
            }
        }

        System.out.println("There are " + count + " products of type " + food.getClass().getSimpleName() + " in the breakfast");
    }
}
