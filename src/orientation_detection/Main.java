package orientation_detection;

public class Main {

	public static void main(String[] args) {
		ComponentFinder compFinder;
			compFinder = new ComponentFinder();
		
		if(compFinder != null){
			compFinder.captureImageData();
			System.out.println("X: " + compFinder.getXCenter() + 
								"\nY: " + compFinder.getYCenter() +
								"\nOrientation: " + compFinder.getOrientation());
		}
	}
	
}
