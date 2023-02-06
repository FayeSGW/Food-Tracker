import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

class RecipeDatabase {
    Scanner input = new Scanner(System.in);
    private String name;
    private HashMap<String, Recipe> recipeList = new HashMap<>();
    
    public RecipeDatabase(String name) {
        this.name = name;
        this.recipeList = recipeList;
    }

    public Recipe addRecipe(String name, int servings) {
        //if (addRecipeCheck(name)) {
         //   Recipe recipe = new Recipe(name, servings);
         //   this.recipeList.put(name, recipe);
        //}
        if (!addRecipeCheck(name)) {
            System.out.println("OK!");
        }
        Recipe recipe = new Recipe(name, servings);
        this.recipeList.put(name, recipe);
        return recipe;
    }

    public Boolean addRecipeCheck(String name) {
        Boolean True = true;
        Boolean False = false;
        if (this.recipeList.containsKey(name)) {
            System.out.println("There is already a recipe with this name. ");
            System.out.println(this.recipeList.get(name));
            System.out.println("Would you like to overwrite this recipe? ");
            String answer = input.nextLine().toUpperCase();
            if (answer.equals("N")) {
                return False;
            }
        }
        return True;
    }

    public void deleteRecipe(String name) {
        this.recipeList.remove(name);
    }

    public String searchDatabase(String name) {
        ArrayList<String> result = new ArrayList<>();
        for (String recipe: this.recipeList.keySet()) {
            if (recipe.contains(name)) {
                result.add(recipe);
            }
        }
        String search = String.join(",", result);
        return search;
    }

    public Recipe addFromDatabase(String name) {
        if (!this.recipeList.containsKey(name)) {
            System.out.println("Recipe not in database!");
        }
        Recipe recipe = this.recipeList.get(name);
        return recipe;
    }

    

    public String toString() {
        ArrayList<String> list = new ArrayList<>();
        for (String recipe: this.recipeList.keySet()) {
            list.add(recipe);
        }
        String lst = String.join(",", list);
        return lst;
    }
}