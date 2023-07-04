// you must not modify this file!!!
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Thread t1 = new Thread(new Host(args[0]));
		Thread t2 = new Thread(new Host(args[1]));
		Thread t3 = new Thread(new Waiter(args[2]));
		Thread t4 = new Thread(new Waiter(args[3]));
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try {
			Thread.sleep(Integer.parseInt(args[4]));
		} catch (NumberFormatException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t1.interrupt();
		t2.interrupt();
		t3.interrupt();
		t4.interrupt();
		
		Restaurant restaurant = Restaurant.getInstance();
		restaurant.print();
	}
}