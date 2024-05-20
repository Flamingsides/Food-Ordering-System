import java.util.ArrayList;
import java.util.Scanner;

// Used to access a local file
import java.io.File;
import java.io.FileNotFoundException;

public class Menu
{
	/** Constants **/
	// Look for csv here if path not supplied
	private static final String DEFAULT_MENU_PATH = "./menu.csv";
	private static final String DEFAULT_CAT_PATH = "./categories.csv";
	
	// Each row is separated by a newline and each element by a comma
	private static final String ROW_DELIM = "\n";
	private static final String DELIM = ",";
	
	// ANSI Colour codes 
	private static final String ANSI_RESET = "\033[0m";
	private static final String ANSI_RED = "\033[0;31m";
	private static final String ANSI_CYAN = "\033[0;36m";
	private static final String ANSI_BLACK = "\033[0;30m";
	private static final String ANSI_BLACK_BOLD = "\033[1;30m";
	private static final String ANSI_BLACK_UNDERLINED = "\033[4;30m";
	private static final String ANSI_WHITE_BACKGROUND = "\033[47m";
	
	private ArrayList<ArrayList<String>> foodCategories;
	private ArrayList<ArrayList<Food>> menu;
	
	/** Constructors **/
	public Menu()
	{
		int catStatus = loadCategories(new File(DEFAULT_CAT_PATH));
		int menuStatus = loadMenu(new File(DEFAULT_MENU_PATH));
		
		if (menuStatus != 0 || catStatus != 0)
		{
			System.out.println(ANSI_RED + "ERROR: MENU OR CATEGORY FILES COULD NOT BE LOADED" + ANSI_RESET);
			System.out.println(ANSI_CYAN + "DETAILS FOR DEVELOPERS:");
			System.out.println("LOADING MENU STATUS: " + menuStatus);
			System.out.println("LOADING CATEGORIES STATUS: " + catStatus + ANSI_RESET);
		}
	}
	
    public Menu(String pathToMenu, String pathToCategories) //Constructor
    {
    	int catStatus = loadCategories(new File(pathToCategories));
    	int menuStatus = loadMenu(new File(pathToMenu));
		
		if (menuStatus != 0 || catStatus != 0)
		{
			System.out.println(ANSI_RED + "ERROR: MENU OR CATEGORY FILES COULD NOT BE LOADED" + ANSI_RESET);
			System.out.println(ANSI_CYAN + "DETAILS FOR DEVELOPERS:");
			System.out.println("LOADING MENU STATUS: " + menuStatus);
			System.out.println("LOADING CATEGORIES STATUS: " + catStatus + ANSI_RESET);
		}
    }
    
    /** Methods to load csv data **/
    // Loads all available categories into foodCategories
    @SuppressWarnings({ "resource", "null" })
	public int loadCategories(File catFile)
    {
    	// Data Validation: Presence check: catFile.exists() is false if file non-existent
    	if (!catFile.exists())
    	{
    		System.out.println(ANSI_RED + "\nERROR 01: CATEGORIES FILE NOT FOUND AT SPECIFIED PATH\n" + ANSI_RESET);
    		System.out.println(ANSI_CYAN + "\nTip: Ensure your categories file is at" + DEFAULT_CAT_PATH + ANSI_RESET);
    		System.out.println("\nDETAILS FOR DEVELOPER: ");
    		
    		return 1;
    	}
    	
    	// Initialisation; adding two ArrayLists
    	foodCategories = new ArrayList<ArrayList<String>>();
    	foodCategories.add(new ArrayList<String>()); // ArrayList to store codes
    	foodCategories.add(new ArrayList<String>()); // ArrayList to store category names
    	
    	// Possibly throws FileNotFoundException
    	Scanner data = null;
    	try
    	{    		
    		data = new Scanner(catFile).useDelimiter(ROW_DELIM);
    	}
    	catch (FileNotFoundException e)
    	{
    		data.close();
    		
    		System.out.println(ANSI_RED + "\nERROR 01: CATEGORIES FILE NOT FOUND AT SPECIFIED PATH\n" + ANSI_RESET);
    		System.out.println(ANSI_CYAN + "\nTip: Ensure your categories file is at" + DEFAULT_CAT_PATH + ANSI_RESET);
    		
    		System.out.println("\nDETAILS FOR DEVELOPER: ");
    		e.printStackTrace();
    		
    		return 1;
    	}
    	
    	// Populate foodCategories array
    	while (data.hasNext())
    	{
    		String[] nextItem = data.next().split(DELIM);
    		
    		// Update codes ArrayList in foodCategories
    		ArrayList<String> codes = foodCategories.get(0);
    		codes.add(nextItem[0]);
    		foodCategories.set(0, codes);
    		
    		// Update category names ArrayList in foodCategories
    		ArrayList<String> categories = foodCategories.get(1);
    		categories.add(nextItem[1]);
    		foodCategories.set(1, categories);
    	}
    	
    	// Clean up
    	data.close();
    	
    	// Success
    	return 0;
    }
    
    @SuppressWarnings({ "resource", "null" })
	public int loadMenu(File menuFile)
    {
    	// Data Validation: Presence check: menuFile.exists() is false if file non-existent
    	if (!menuFile.exists())
    	{
    		System.out.println(ANSI_RED + "\nERROR 01: MENU FILE NOT FOUND AT SPECIFIED PATH\n" + ANSI_RESET);
    		System.out.println(ANSI_CYAN + "\nTip: Ensure your menu file is at" + DEFAULT_MENU_PATH + ANSI_RESET);
    		
    		return 1;
    	}
    	
    	// Initialise
    	menu = new ArrayList<ArrayList<Food>>();
    	
    	// Add as many arrays as categories
    	for (int i = 0, n = foodCategories.get(0).size(); i < n; i++)
    	{
    		menu.add(new ArrayList<Food>());
    	}
    	
    	// Possibility of file not existing necessitates try-catch block
    	Scanner data = null;
    	try
    	{
    		data = new Scanner(menuFile).useDelimiter(ROW_DELIM);
    	}
    	catch (FileNotFoundException e)
    	{
    		data.close();
    		
    		System.out.println(ANSI_RED + "\nERROR 01: MENU FILE NOT FOUND AT SPECIFIED PATH\n" + ANSI_RESET);
    		System.out.println(ANSI_CYAN + "\nTip: Ensure your menu file is at" + DEFAULT_MENU_PATH + ANSI_RESET);
    		
    		System.out.println("\nDETAILS FOR DEVELOPER: ");
    		e.printStackTrace();
    		
    		// Error
    		return 1;
    	}
    	
    	// Get indices that correspond to Item, Price, and Category columns
    	String[] headers = data.next().split(DELIM);
    	int i_Item = indexOf(headers, "Item");
    	int i_Price = indexOf(headers, "Price");
    	int i_Category = indexOf(headers, "Category");
    	
    	// Data Validation: Presence Check: indexOf() returns -1 if item not found
    	if (i_Item == -1 || i_Price == -1 || i_Category == -1)
    	{
    		System.out.println(ANSI_RED + "\nERROR 02: ONE OR MORE HEADERS NOT FOUND IN MENU\n" + ANSI_RESET);
    		System.out.println(ANSI_CYAN + "\nTip: Ensure your menu file begins with the following headers:\n");
    		System.out.println("\tItem\n\tPrice\n\tCategory" + ANSI_RESET);
    		
    		return 2;
    	}
    	
    	// Stores next available row in file
    	String[] nextItem;
    	while (data.hasNext())
    	{
    		nextItem = data.next().split(DELIM);
    		
    		// Get individual elements from rawData
    		String itemName = nextItem[i_Item].replaceAll("\\P{Print}", "");
    		double price = Double.valueOf(nextItem[i_Price].replaceAll("\\P{Print}", ""));
    		String category = nextItem[i_Category].replaceAll("\\P{Print}", "");
    		
    		// Create Food object
    		Food item = new Food(itemName, price, category);
    		
    		// Get appropriate ArrayList and add new food item to it
    		int index = foodCategories.get(0).indexOf(category);
    		ArrayList<Food> array = menu.get(index);
    		array.add(item);
    		
    		// Replace old ArrayList with new ArrayList
    		menu.set(index, array);
    	}
    	
    	// Clean up
    	data.close();
    	
    	// Success
    	return 0;
    }

    /** Other Methods **/
    public static int indexOf(String[] arr, String element)
    {
    	int index = -1;
    	
    	for (int i = 0; i < arr.length; i++)
    	{
    		// replaceAll() used to get rid of all non-printable characters 
    		if (element.equals(arr[i].replaceAll("\\P{Print}","")))
    		{
    			index = i;
    			break;
    		}
    	}
    	
    	return index;
    }
    
    // Uses item ID to locate item in menu
    // Returns null if not found
    public Food getItem(String id)
    {
    	// Data Validation - Length Check: Minimum length for id is 2 (1 character and 1 digit)
    	if (id.length() < 2)
    	{
    		return null;
    	}
    	
    	// The letters part of the ID is stored in code and the numbers in number;
    	String code = "";
    	int num = -1;
    	
    	// Get as many letters as found in the beginning of the String
    	int count = 0;
    	char current = id.charAt(count); // x is just a placeholder
    	while (Character.isAlphabetic(current))
    	{
    		code += current;
    		
    		count++;
    		
    		// Data Validation
    		// If end of string crossed, id contains no numbers
    		// Numbers are essential in the id, return null
    		if (count >= id.length())
    		{
    			return null;
    		}
    		
    		// Update current
    		current = id.charAt(count);
    	}
    	
    	// The rest of the ID should be numbers
    	num = Integer.valueOf(id.substring(count));
    	
    	// Get the index of the desired category Array in menu
    	int categoryArr = foodCategories.get(0).indexOf(code.toUpperCase());
    	
    	// Data Validation
    	if (categoryArr == -1 || num > menu.get(categoryArr).size())
    	{
    		return null;
    	}
    	
    	Food food = menu.get(categoryArr).get(num - 1);
    	
    	return food;
    }
    
    public String getID(Food item)
    {
    	// The category code is already stored within the Food item
    	String code = item.getCategory();
    	
    	// Assume item is not present until proven otherwise (-1)
    	int num = -1;
    	
    	// Get relevant array based on category
    	ArrayList<Food> catFood = menu.get(foodCategories.get(0).indexOf(code));
    	
    	// Linear Search for item with same name (names are unique in the menu)
    	for (int i = 0, n = catFood.size(); i < n; i++)
    	{
    		if (catFood.get(i).getItemName() == item.getItemName())
    		{
    			num = i + 1;
    			break;
    		}
    	}
    	
    	if (num == -1)
    	{
    		return "ITEM DOES NOT EXIST";
    	}
    	else
    	{
    		return code + num;
    	}
    }
    
    public String toString()
    {
    	int itemIDWidth = 6; // Left justified
    	int menuWidth = 50 + itemIDWidth;
    	int itemMargins = 5;
    	
    	String title = "WELCOME TO HAPPY CAFE!";
    	
    	// Variable to keep track of the number of spaces before each line
    	int numSpaces = 0;
 
    	// String representation of menu
    	String menuTxt = ANSI_WHITE_BACKGROUND + ANSI_BLACK_BOLD;
    	
    	// Decorative top horizontal line
    	for (int i = 0; i < menuWidth; i++)
    	{
    		menuTxt += "~";
    	}
    	
    	// Add title
    	numSpaces = (menuWidth - title.length()) / 2;
    	
    	menuTxt += "\n" + String.format("%-" + numSpaces + "s", "|");
    	menuTxt += title;
    	// If total length is lesser than menu's total width, add 1
    	numSpaces += (2 * numSpaces + title.length()) < menuWidth ? 1 : 0;
    	menuTxt += String.format("%" + numSpaces + "s", "|") + "\n";
    	
    	// Decorative bottom horizontal line
    	for (int i = 0; i < menuWidth; i++)
    	{
    		menuTxt += "~";
    	}
    	menuTxt += "\n";
    	
    	// Prepare of following text
    	menuTxt += ANSI_RESET + ANSI_WHITE_BACKGROUND;
    	
    	// For every category
    	for (int i = 0, n = foodCategories.get(0).size(); i < n; i++)
    	{
    		/** Adding Category Name **/
    		String category = foodCategories.get(1).get(i).strip();
    		
    		// Calculates number of spaces to print before and after each category heading.
    		numSpaces = (menuWidth - category.length()) / 2;
    		
    		// New line
    		menuTxt += String.format("%" + menuWidth + "s", "") + "\n";
    		
    		// Add spaces
    		menuTxt += String.format("%" + numSpaces + "s", "");
    		// Add category underlined
    		menuTxt += ANSI_BLACK_UNDERLINED;
    		menuTxt += category;
    		menuTxt += ANSI_RESET + ANSI_WHITE_BACKGROUND;
    		
    		// If numSpaces and category.length() are less than the menu's total width, add 1
    		numSpaces += (2 * numSpaces + category.length()) < menuWidth ? 2 : 1;
    		menuTxt += String.format("%" + numSpaces + "s", "\n");
    		
    		// New line
    		menuTxt += String.format("%" + menuWidth + "s", "") + "\n";
    		
    		// Change text colour to black
    		menuTxt += ANSI_BLACK + ANSI_WHITE_BACKGROUND;
    		
    		// Add Food items
    		for (Food item : this.menu.get(i))
    		{
    			String id = String.format("%" + itemIDWidth + "s", getID(item));
    			String name = item.getItemName();
    			String price = "$" + String.format("%.2f", item.getPrice());
    			int numDots = menuWidth - itemIDWidth - itemMargins - name.length() - price.length();
    			
    			String itemStr = name;
    			for (int j = 0; j < numDots; j++)
    			{
    				itemStr += ".";
    			}
    			itemStr += price;
    			
    			numSpaces = (menuWidth  - itemIDWidth - itemStr.length()) / 2;
    			
    			menuTxt += ANSI_BLACK_BOLD + id + ANSI_BLACK + ANSI_WHITE_BACKGROUND;
    			menuTxt += String.format("%" + numSpaces + "s", "");
    			menuTxt += itemStr;
    			numSpaces += itemStr.length() % 2 == 0 ? 0 : 1;
    			menuTxt += String.format("%" + numSpaces + "s", "") + "\n";
    			
    		}
    	}
    	// Rest formatting
    	menuTxt += ANSI_RESET;
    	return menuTxt;
    }
}
