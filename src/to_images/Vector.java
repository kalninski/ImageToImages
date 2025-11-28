package to_images;

import java.awt.image.*;
//Class to get the vector info from the source image 
public class Vector {
	
	int xOrig;
	int yOrig;
	int zOrig;
	
	//normalised
	double xRedN;
	double yGreenN;
	double zBlueN;
	
	int scaledColX;
	int scaledColY;
	int scaledColZ;
	
	int avgCol;
	
	int indexOfLightness;
	
	public Vector() {}
	
	public Vector(BufferedImage img, int x, int y, int numPictures) {
		normalize(img, x, y, numPictures);
	}
	
	public void normalize(BufferedImage img, int x, int y, int numPictures) {
		int color = img.getRGB(x, y);
		xOrig = (color >> 16) & 0xFF;
		yOrig = (color >> 8) & 0xFF;
		zOrig = (color) & 0xFF;
		
		double sum = Math.sqrt(Math.pow(xOrig, 2) + Math.pow(yOrig, 2) + Math.pow(zOrig, 2));
		
		xRedN = ((double) xOrig)/sum;
		yGreenN = ((double) yOrig)/sum;
		zBlueN = ((double) zOrig)/sum;
		
		avgCol = (xOrig + yOrig + zOrig)/3;
		
		double light = (double) avgCol/255.0f;
		indexOfLightness = (int) light * numPictures;//if no similar color vectors found, then pick the image that has same place in the image range
		
	}
	//0 = red, 1 = green, 2 = blue, numBooks = num. of available images returns the restricted color values.
	public int index(int col, int numBooks) {
		if(col == 0) {
			return ((int) xRedN * (numBooks));
		}
		if(col == 1) {
			return ((int) yGreenN * (numBooks));
		}
		if(col == 2) {
			return ((int) zBlueN * (numBooks));
		}
		return 0;
	}
	
}
