import java.io.*;
import java.util.Scanner;


class Tracker {
    
    public static void loadUser(Scanner scan) {
        String name, gender, goal, dob;
        int weight, height, rate;
        User user = null;
        Diary diary;
        Database data;

        System.out.println("Name: ");
        name = scan.nextLine().toLowerCase();
        String userFile = "/Users/" + name + "/user.ser";
        String dataFile = "/Users/" + name + "/data.ser";
        String diaryFile = "/Users/" + name + "/diary.ser";
        File usfile = new File(userFile);
        File dafile = new File(dataFile);
        File difile = new File(diaryFile);

        if (usfile.exists()) {
            try {
                FileInputStream usfileIn = new FileInputStream(usfile);
                ObjectInputStream in = new ObjectInputStream(usfileIn);
                user = (User) in.readObject();
                FileInputStream dafileIn = new FileInputStream(dafile);
                ObjectInputStream in2 = new ObjectInputStream(dafileIn);
                data = (Database) in2.readObject();
                FileInputStream difileIn = new FileInputStream(difile);
                ObjectInputStream in3 = new ObjectInputStream(difileIn);
                diary = (Diary) in3.readObject();
                in3.close(); difileIn.close(); 
                in2.close(); dafileIn.close();
                in.close(); usfileIn.close();
            } catch (IOException | ClassNotFoundException e) {};
            
        } 
        try {
            String filename = "/Users/" + name + ".ser";
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            user = (User) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Create User: ");
        }


    }


    public static void main(String [] args) {
        Scanner scan = new Scanner(System.in);
    }
}