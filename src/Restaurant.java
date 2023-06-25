
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurant {

	private AtomicInteger balance = new AtomicInteger(0); // you can use AtomicInteger instead
	private Set<Table> tables = new HashSet<Table>();
	private static Restaurant instance;
	private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
	/*
	 * this implementation of getInstance return null and obviously wrong.
	 * implement this class as a thread-safe singleton
	 */
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
	
	public void order(int i) 
	{
		/*
		 * 1) you might get concurrent modification exception.
		 * 2) do not close too tight!
		 * 3) this method is BLOCKING until a corresponding table exists...
		 * 4) blocked thread must be informed when the program terminates... 
		 */
		
		while(instance.getTableById(i) == null)
		{
			try {
				wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;//check later//TODO 4
			}//check later
			System.out.println("waiting for table " + i + " to be created....");
		}
		
		while(atomicBoolean.compareAndSet(false,true)) {};
		instance.getTableById(i).addDish();
		atomicBoolean.set(false);
	}
	
	public void openTable(int i) 
	{
		while(atomicBoolean.compareAndSet(false,true)) {};
		tables.add(new Table(i));
		atomicBoolean.set(false);
		// you must not assume thread-safety adding elements to a set.
	}
	
	/*
	 * a corresponding table is guaranteed to exist.
	 * this implementation is good under SERIAL PROGRAM,
	 * however, in this assignment you might get ConcurrentModificationException -
	 * avoid it by LOCK-FREE IMPLEMENTATION
	 */
	public void closeTable(int i) {
		
		Table t = null;
		for (Table tmp: tables) {
			if (tmp.getId() == i) {
				t = tmp;
				break;
			}
		}
		
		tables.remove(t);
		balance.addAndGet(t.numOfDishes());//TODO
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
