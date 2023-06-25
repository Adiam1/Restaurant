
// you must not modify this file!!!
public class Host implements Runnable {

	private String s;
	
	public Host(String s) {
		this.s = s;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		Restaurant restaurant = Restaurant.getInstance();
		
		int i = 0;
		while (i < s.length() && !Thread.currentThread().isInterrupted()) {
			
			switch(s.charAt(i)) {
			
				case '0':
				{
					i++;
					break;
				}
				
				case '1':
				{
					restaurant.openTable(Character.getNumericValue(s.charAt(i + 1)));
					i += 2;
					break;
				}
				
				case '3':
				{
					restaurant.closeTable(Character.getNumericValue(s.charAt(i + 1)));
					i += 2;
					break;
				}
			}
			
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
