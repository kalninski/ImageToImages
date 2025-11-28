package to_images;

import java.awt.Image;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.io.*;

public class ImageNode {
	
	int xRed;
	int yGreen;
	int zBlue;
	
	int sum;
	
	double xRedN;
	double yGreenN;
	double zBlueN;
	
	int avgCol;
		
	BufferedImage img;
	BufferedImage cell;
	
	public ImageNode(String path) {
		
		try {
			img = ImageIO.read(new File(path));
			int h = img.getHeight();
			int w = img.getWidth();

			Image oneCell = img.getScaledInstance(30, 56, Image.SCALE_DEFAULT);
			Image avgColorPix = img.getScaledInstance(1, 1, Image.SCALE_SMOOTH);

			cell = new BufferedImage(30, 56, BufferedImage.TYPE_INT_RGB);
			BufferedImage pix = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			
			cell.getGraphics().drawImage(oneCell, 0, 0, null);
			pix.getGraphics().drawImage(avgColorPix, 0, 0, null);
			
			int color = pix.getRGB(0,0);
			xRed = (color >> 16) & 0xFF;
			yGreen = (color >> 8) & 0xFF;
			zBlue = (color) & 0xFF;
			avgCol = (xRed + yGreen + zBlue)/3;
			
			normalize();
			
			System.out.println(String.format("avgRed = %d , avgGreen = %d , avgBlue =  %d ", xRed, yGreen ,zBlue));
		}catch(IOException ex) {
		
			System.out.println("The file was not found for some reason!");
			
		}
	}
	
	public void normalize() {
		
		double xRedD = (double) xRed;
		double yGreenD = (double) yGreen;
		double zBlueD =  (double) zBlue;
		
		double sum = Math.sqrt(Math.pow(xRedD, 2) + Math.pow(yGreenD, 2) + Math.pow(zBlueD, 2));
		
		xRedN = xRedD / sum;
		yGreenN = yGreenD / sum;
		zBlueN = zBlueD / sum;
		
	}
	
	
	
}
