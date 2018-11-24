package menu;

public class RunThread implements Runnable{

	private boolean stop;
	   @SuppressWarnings("static-access")
	public void run () {
	      stop = true;
	      while (stop) {
	         System.out.print("\010$");
	     try {
	        Thread.currentThread().sleep(1);
	         } catch(InterruptedException ie) {
	            ie.printStackTrace();
	         }
	      }
	   }
	   public void stopMasking() {
	      this.stop = false;
	   }
	 
}
