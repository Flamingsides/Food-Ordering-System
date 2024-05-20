import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class Order
{
	private static final String GREEN = "\033[1;92m";
	private static final String RED = "\033[1;91m";
	private static final String WHITE = "\033[1;97m";
	private static final String RESET = "\033[0m";
	
	
	private double total;
	private String code;
	private Menu menu;

	private ArrayList<Food> order = new ArrayList<>();
	private Date current_date = new Date();
	private DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private Scanner input = new Scanner(System.in);
	private QRCode qr_code = new QRCode();
	
    public Order(Menu menu) //Constructor
    {
    	total = 0;
    	this.menu = menu;
    }

    public void addOrder() //To add a food item into the order
    {
    	System.out.print("ADD: Enter code of food item - [LETTER][NUMBER]\n\n>>");
    	code = input.next();
    	Food item = menu.getItem(code);
    	
    	// Check if item is null
    	if (item == null)
    	{
    		System.out.println("\nInvalid input, item code not found. Please try again.\n");
    		startOrder();
    		return;
    	}
    	order.add(item);
    	total += item.getPrice();
    	
    	System.out.println("\nFood item " + WHITE + code.toUpperCase() + RESET + " : " + item.getItemName() + " successfully added.\n" + 
    						GREEN + item.getPrice() + RESET +" added to the total amount.\n");
    }
    
    public void removeOrder() //To remove a food item from the order
    {
    	System.out.print("REMOVE: Enter code of food item - [LETTER][NUMBER]\n\n>>");
    	code = input.next();
    	Food item = menu.getItem(code);
    	
    	if (item == null)
    	{
    		System.out.println("\nInvalid input, item code not found. Please try again.\n");
    		startOrder();
    		return;
    	}
    	
    	for (int i = 0; i < order.size(); i++)
    	{
    		if (order.get(i).getItemName().equals(item.getItemName()))
    		{
    			order.remove(i);
    			break;
    		}
    	}
    	total -= item.getPrice();
    	
    	System.out.println("\nFood item " + WHITE + code.toUpperCase() + RESET + " : " + item.getItemName() + " successfully removed.\n" + 
    						RED + item.getPrice() + RESET +" deducted from the total amount.\n");
    	
    }
    
    public void currentOrders()
    {    	
    	System.out.println("============================================");
    	
    	for (int i = 0; i < order.size(); i++)
    	{
    		System.out.printf("%-39s", order.get(i).getItemName());
    		System.out.printf("%-4.2f", order.get(i).getPrice());
    		System.out.println();
    	}
    	
    	System.out.println("============================================");
    	System.out.println();
    	
    	System.out.println("Total number of food items: " + order.size() + "\n" + 
				"Total amount: " + decimalFormat.format(total) + "\n");
    	
    }
    
    public String getDate() //To get the date from OS
    {
    	SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE | h:mm:ssa | dd/MM/yyyy");
    	String time = timeFormat.format(current_date);
    	return time.toUpperCase();
    }

    public void printReceipt() //To print the receipt
    {
    	double tax = total * 0.06;
    	double rounding_adjustment = Math.round((total + tax) * 10.0) / 10.0 - (total + tax);
    	int count, loop;
    	Food empty_slot = new Food(" ", 0, null);
    	
    	System.out.println("		     " + "HappyU Cafe\n" +
    						"	     " + getDate() + 
    						"\n==========================================================");
    	System.out.print(	"Food Item                                 Quantity   Price\n");
    	
    	for (Food item : order)
    	{
    		if (item != empty_slot)
    		{
    			System.out.printf("%-41s", item.getItemName());
        		count = 0; 
        		loop = 0;
        		
        		for (int i = 0; i < order.size(); i++)
        		{
        			if (item.getItemName().equals(order.get(i).getItemName()))
        			{	
        				count++;
        				if (loop != 0)
        					order.set(i, empty_slot);
        				loop++;
        			}
        		}
        		
        		System.out.printf("%6s", count);
        		System.out.printf("%11s", "$" + decimalFormat.format(item.getPrice() * count));
        		System.out.println();
    		}
    	}
    	System.out.println("==========================================================");
    	
    	System.out.printf("%58s", "Subtotal: $" + decimalFormat.format(total));
    	System.out.printf("%n%58s", "SST (6%): $" + decimalFormat.format(tax));
    	System.out.printf("%n%58s", "Rounding Adjustment: $" + decimalFormat.format(rounding_adjustment));
    	System.out.printf("%n%58s", "Total: $" + decimalFormat.format(total + tax + rounding_adjustment));
    	System.out.println("\n\n\n\n\n\n\n			 Pay Here:\n");
    	System.out.println(qr_code.Image());
    	total += tax + rounding_adjustment;
    }
    
    public double getTotal()
    {
    	return total;
    }
    
    public void startOrder() //To start the ordering process
    {
    	String choice = "";
    	do {
        	System.out.println("Select one of the options below: ");
        	System.out.printf("%-12s", "   ADD");
        	System.out.print("- Add an order\n");
        	
        	System.out.printf("%-12s", "   REMOVE");
        	System.out.print("- Remove an order\n");
        	
        	System.out.printf("%-12s", "   MENU");
        	System.out.print("- To see the menu\n");
        	
        	System.out.printf("%-12s", "   SHOW");
        	System.out.print("- To show current orders\n");
        	
        	System.out.printf("%-12s", "   DONE");
        	System.out.print("- To complete the order\n\n>> ");
        	choice = input.next();
        	System.out.println();
        	
        	switch(choice.toUpperCase()) 
        	{
        	case "ADD":
        		addOrder();
        		break;
        	case "REMOVE":
        		removeOrder();
        		break;
        	case "MENU":
        		System.out.println(menu);
        		break;
        	case "SHOW":
        		currentOrders();
        		break;
        	case "DONE":
        		printReceipt();
        		break;
        	default:
        		System.out.println("Invalid input, please try again.");
        		break;	
        	}
    	} while(choice.toUpperCase().equals("DONE") == false);
    	
    }
}