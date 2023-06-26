
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurant {

	private AtomicInteger balance = new AtomicInteger(0); // you can use AtomicInteger instead
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
		/*
		 * 1) you might get concurrent modification exception.
		 * 2) do not close too tight!
		 * 3) this method is BLOCKING until a corresponding table exists...
		 * 4) blocked thread must be informed when the program terminates... !!
		 */
	    Table table;

	    synchronized (tablesLock) 
	    {
	        table = getTableById(tableNumber);

	        while (table == null) 
	        {
	            // Table not found, wait for it to become available
	            try {
	                tablesLock.wait(100);
	                table = getTableById(tableNumber); // Recheck the table after waking up
	            } 
	            catch (InterruptedException e) 
	            {
	                // Handle interrupted exception
	            	//Thread.currentThread().notify();//TODO - figure out if we need this
	                Thread.currentThread().interrupt(); // Restore interrupted status
	                return; // Exit the method
	            }
	        }
	    }

	    // Table found, place the order
	    table.addDish();
	    System.out.println("Order placed for table " + tableNumber);//TODO - delete at end
	}

	
	public void openTable(int i) 
	{
		while(atomicBoolean.compareAndSet(false,true)) {};
		tables.add(new Table(i));
		atomicBoolean.set(false);
	}
	
	/*
	 * a corresponding table is guaranteed to exist.
	 * this implementation is good under SERIAL PROGRAM,
	 * however, in this assignment you might get ConcurrentModificationException -
	 * avoid it by LOCK-FREE IMPLEMENTATION
	 */
	public void closeTable(int i) {
		
		Table t = null;
		while(atomicBoolean.compareAndSet(false,true)) {};
		
		for (Table tmp: tables) {
			if (tmp.getId() == i) {
				t = tmp;
				break;
			}
		}

		System.out.println("Remove table number " + t.getId());
		tables.remove(t);
		balance.addAndGet(t.numOfDishes()); //TODO - add and get or add and update
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
