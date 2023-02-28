
class Test3 {
    public static void main(String [] args) {
        /*Food apple = new Food("Apple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0);
        //System.out.println(apple.showName());
        System.out.println(apple);

        Recipe rec = new Recipe("Test", 3);
        //System.out.println(rec.showName());

        Database data = new Database("Data");
        data.addFood("Eple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0);
        //System.out.println(data);  
        //data.addFood("Eple", 100, "g", 470, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0);
        //System.out.println(data.searchDatabase("e"));
        data.addFood("Apple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0);        


        Recipe reci = data.addRecipe("Check", 6);
        //System.out.println(data.searchDatabase("e"));
        
        Recipe r = (Recipe) data.findItem("Check");


        data.addFromDatabase("Eple");
        System.out.println(data);

        reci.addIngredient("Eple", 100, data);
        System.out.println(reci);
        r.addIngredient("Apple", 50, data);

        rec.addIngredient("Check", 1, data);
        System.out.println(rec);

        Meal bfast = new Meal("Breakfast", "28.02.03");
        bfast.add("Apple", 100, data);
        bfast.add("Check", 3, data);
        System.out.println(bfast);

        bfast.edit("Apple", 150);
        bfast.add("Check", 1, data);
        System.out.println(bfast);

        bfast.remove("Check");
        System.out.println(bfast);
        */
        
        User me = new User("Faye", "F", 82, 165, "24.07.1989", "loss", 0.5);
        Database dat = me.accessDatabase();
        dat.addFood("Eple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0);
        dat.addFood("Apple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0);        
        Recipe reci = dat.addRecipe("Check", 6);
        reci.addIngredient("Eple", 100, dat);


        Day today = new Day("28.02.23", me);
        System.out.println(today);

        today.addFood("breakfast", "Apple", 50, dat);
        today.addFood("snacks", "Check", 2, dat);
        System.out.println(today);

        today.clearMeal("Breakfast");
        System.out.println(today);

        today.addExercise("Run", 30, 400);
        System.out.println(today);
    

    }
}