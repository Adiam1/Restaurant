import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Table {
	
	private Set<Dish> dishes = new HashSet<Dish>();
	private int id;
	private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

	public Table(int id) {
		this.id = id;
	}
	
	public void addDish() 
	{
		while(atomicBoolean.compareAndSet(false,true)) {};
		dishes.add(new Dish(dishes.size()));
		atomicBoolean.set(false);
	}
	
	public int getId() {
		return id;
	}

	public int numOfDishes() 
	{
		return dishes.size();
	}
	
	// do not modify
	@Override
    public boolean equals(Object obj) {
		return id == ((Table) obj).getId();
    }
	
	// do not modify
	@Override
    public int hashCode() {
        return id;
    }
}
