
// you must not modify this file!!!
public class Dish {

	private int id;

	public Dish(int id) {
		this.id = id;
	}

	@Override
    public boolean equals(Object obj) {
		return this != obj;
    }
	
	@Override
    public int hashCode() {
        return this.id;
    }
}
