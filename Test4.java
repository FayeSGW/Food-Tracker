class Test4 {
    public static void main (String [] args) {
        User me = new User("Faye", "F", 81, 165, "24.07.1989", "loss", 1);
        Database data = me.accessDatabase();
        Diary diary = me.accessDiary();

        data.addFood("Eple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0, null);
        data.addFood("Apple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0, null);
        //data.addFood("Eple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0, null);
        data.delete("Eple");

        Recipe rec = data.addRecipe("Check", 6);
        rec.addIngredient("Eple", 100);
        rec.addIngredient("Apple", 10);

        System.out.println(rec);

        Food apple = (Food) data.findItem("Apple");
        apple.addFoodType("fruit");

        for (String type: rec.showFoodTypes()) {
            System.out.print(type);
        }

        apple.removeFoodType("fruit");

        System.out.println("+");
        for (String type: rec.showFoodTypes()) {
            System.out.print(type);
        }


    }
}