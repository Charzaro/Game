package game;

public class Settings {
	
	private static int dimx =800;
	private static int dimy = 800;
	
	public static int update_rate = 120;
	public static int update_factor = 120/update_rate;

	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	public static int getDimx(){
		return dimx;
	}
	
	public static int getDimy(){
		return dimy;
	}
}
