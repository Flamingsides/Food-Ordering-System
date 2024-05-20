import java.util.Scanner;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class HappyUFoodOrdering
{
    private Menu menu;
    private Scanner in;
    private ArrayList <Double> revenue = new ArrayList<>();
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    
    private static final String ANSI_GREY = "\033[38;2;126;140;163m";
    
    
    // Constructor
    public HappyUFoodOrdering()
    {
    	in = new Scanner(System.in);
    	menu = new Menu();
    }
    
    // The main function equivalent
    public void run()
    {
    	// Assume user wants to order at least once
    	boolean wantOrder = true;
    	
    	while(wantOrder)
    	{
    		// Take new order
    		Order order = new Order(menu);
    		order.startOrder();
    		
    		// Tally revenue
    		revenue.add(order.getTotal());
    		
    		// Ask if user would like to order again
    		System.out.print("\nWould you like to order again?\n>> ");
    		String response = in.next().strip().toLowerCase();
    		String[] validResponses = {"y", "n", "yes", "no"};
    		
    		// Data Validation: Code Check
    		while (!CodeCheck(response, validResponses))
    		{
    			System.out.print("Invalid Response. Choose from [Y, N].");
    			System.out.print("Would you like to order again?\n>> ");
        		response = in.next();
    		}
    		
    		// Update wantOrder
    		if (response.equals("y") || response.equals("yes"))
    		{
    			wantOrder = true;
    		}
    		else 
    		{
    			wantOrder = false;
    		}
    	}
    	
    	/** End of Program **/
    	// Print revenue
    	System.out.println("\nThanks for using Happy Cafe's Online Food Ordering System!");
    	System.out.println("\nHere is the total revenue made in the previous session: ");
    	printRevenue();
    	
    	// End
    	System.out.println("\nDo come again!");
    	System.out.println(ANSI_GREY + "Enter to end...");
    	in.nextLine();
    }
    
    // A function that runs a code check; checks whether a value is within a valid set of responses
    private boolean CodeCheck(String response, String[] validResponses)
    {
    	boolean valid = false;
    	
    	for (String r : validResponses)
    	{
    		if (response.equals(r))
    		{
    			valid = true;
    		}
    	}
    	
    	return valid;
    }

    //To show the amount of revenue earned in a day. Essentially looks like a table.
    public void printRevenue()
    {
    	double total_revenue = 0;
    	System.out.print("\n==============================================");
    	
    	for (int i = 1; i <= revenue.size(); i++)
    	{
    		System.out.printf("%n%-40s", "Order " + i );
    		System.out.printf("%3s", "$" + decimalFormat.format(revenue.get(i-1)));
    		total_revenue+=revenue.get(i-1);
    	}
    	System.out.println("\n==============================================");
    	System.out.printf("%46s", "Total: $" + decimalFormat.format(total_revenue));
    }
}
