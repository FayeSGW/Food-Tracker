
class Test3 {
    public static void main(String [] args) {
        Food apple = new Food("Apple", 100, "g", 47.0, 0.1, 0.0, 10.0, 6.8, 2.5, 0.3, 0.0);
        //System.out.println(apple.showName());

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


    
    }
}