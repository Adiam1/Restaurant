
// you must not modify this file!!!
public class Waiter implements Runnable {

	private String s;
	
	public Waiter(String s) {
		this.s = s;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		Restaurant restaurant = Restaurant.getInstance();
		
		int i = 0;
		while (i < s.length() && !Thread.currentThread().isInterrupted()) {

			//System.out.println("waiter");
			restaurant.order(Character.getNumericValue(s.charAt(i)));
			
			i++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
}