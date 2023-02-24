import java.io.*;
import java.util.Scanner;


class Tracker {
    
    public static User loadUser(Scanner scan) {
        String name, gender, goal, dob;
        int weight, height, rate;
        User user = null;

        System.out.println("Name: ");
        name = scan.nextLine().toLowerCase();
        String filename = "/Users/" + name + ".ser";
        File file = new File(filename);

        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            user = (User) in.readObject();
            in.close();
            fileIn.close();

        } 
        try {
            String filename = "/Users/" + name + ".ser";
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            user = (User) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Create User: ");
        }


    }


    public static void main(String [] args) {
        Scanner scan = new Scanner(System.in);
    }
}