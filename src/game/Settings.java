package game;

public class Settings {
	
	public static float versionNumber = 1.0f;
	public static String version = "1.2B";
	
	private static int dimx =800;
	private static int dimy = 800;
	
	public static int update_rate = 120;
	public static int update_factor = 120/update_rate;
	
	public static Map map;// = new Map("Empty");

	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	public static void setMap(Map m){
		map = m;
	}
	
	public static int getDimx(){
		return dimx;
	}
	
	public static int getDimy(){
		return dimy;
	}
}
