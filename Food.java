public class Food
{
	private String itemName;
	private double price;
	private String category;
	
	
    public Food(String itemName, double price, String category)
    {
    	this.itemName = itemName;
    	this.price = price;
    	this.category = category;
    }
    public String getItemName()
    {
    	return itemName;
    	
    }
    
    public double getPrice()
    {
    	return price;
    }
    
    public String getCategory()
    {
    	return this.category;
    }
    
    public String toString()
    {
    	return itemName + ": " + price;
    }
}
