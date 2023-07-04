
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurant {

	private AtomicInteger balance = new AtomicInteger(0);
	private Set<Table> tables = new HashSet<Table>();
	private static Restaurant instance;
	private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
	private final Object tablesLock = new Object();
	
	public static Restaurant getInstance() 
	{
		if (instance == null) 
		{
            synchronized (Restaurant.class) 
            {
                if (instance == null) 
                {
                    instance = new Restaurant();
                }
            }
		}
		
		return instance;
	}
	
	
	
	public void order(int tableNumber) 
	{
	    Table table;

	        table = null;

	        while (table == null) 
	        {
	            // Table not found, wait for it to become available
	            try {
	            	table = getTableById(tableNumber);
	            	if(table == null)
	            	{
	            		synchronized (tablesLock)
	            		{
			                tablesLock.wait(100);
	            		}
	            	}

	            } 
	            catch (InterruptedException e) 
	            {
	                Thread.currentThread().interrupt(); // Restore interrupted status
	                return;
	            }
	        }

	        synchronized (table) // Table found, place the order
    		{
	        	table.addDish();
    		}
	}

	
	public void openTable(int i) 
	{
		while(atomicBoolean.compareAndSet(false,true)) {};
		tables.add(new Table(i));
		atomicBoolean.set(false);
	}
	
	public void closeTable(int i) {
		
		Table t = null;
		while(atomicBoolean.compareAndSet(false,true)) {};
		
		for (Table tmp: tables) {
			if (tmp.getId() == i) {
				t = tmp;
				break;
			}
		}
		tables.remove(t);
		balance.addAndGet(t.numOfDishes());
		atomicBoolean.set(false);
	}
	
	// do not modify this method
	public void print() {
		
		int n_tables = 0;
		int n_dishes = 0;
		
		for (Table t: tables) {
			n_tables++;
			n_dishes += t.numOfDishes();
		}
		
		System.out.println(n_tables + " table(s), " + n_dishes + " dishe(s), " + balance + " balance");
	}
	
	
	private Table getTableById(int tableId) 
	{
        for (Table table : tables) 
        {
            if (table.getId() == tableId)
            {
                return table;
            }
        }
        return null; // Return null if the table is not found
    }
}
