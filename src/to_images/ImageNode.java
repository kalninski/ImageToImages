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
	
	int avgRed;
	int avgGreen;
	int avgBlue;
	
	int divisor;
	
	String pathImg = "";

	
	
	double angleOfEdge;
	
	boolean isEdge;
	
	int avgCol;
	double avgColN; 
		
	BufferedImage img;
	BufferedImage cell;
	
	public ImageNode(String path) {
		
		try {
			img = ImageIO.read(new File(path));
			int h = img.getHeight();
			int w = img.getWidth();

			Image oneCell = img.getScaledInstance(30, 46, Image.SCALE_DEFAULT);
			Image avgColorPix = img.getScaledInstance(5, 5, Image.SCALE_SMOOTH);

			cell = new BufferedImage(30, 46, BufferedImage.TYPE_INT_RGB);
			BufferedImage pix = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
			pathImg = path;
			img = null;
			cell.getGraphics().drawImage(oneCell, 0, 0, null);
			pix.getGraphics().drawImage(avgColorPix, 0, 0, null);
			
			for(int x = 0; x < 5; x++) {
				for(int y = 0; y < 5; y++) {
					int color = pix.getRGB(x,y);
					xRed += (color >> 16) & 0xFF;
					yGreen += (color >> 8) & 0xFF;
					zBlue += (color) & 0xFF;
				}
			}
			
			xRed = xRed / 25;
			yGreen = yGreen / 25;
			zBlue = zBlue / 25;

			
			avgCol = (xRed + yGreen + zBlue)/3;
			
			avgColN = ((double) avgCol) /255.0;
			
			
			
			getGradient();

			
			normalize();
			
			System.out.println(String.format("avgRed = %d , avgGreen = %d , avgBlue =  %d ", xRed, yGreen ,zBlue));
		}catch(IOException ex) {
		
			System.out.println("The file was not found for some reason!");
			
		}
	}
	void getGradient() {
		
		int color = 0;
		
		int sub1 = 0;
		int sub2 = 0;
		
		int w = cell.getWidth();
		int h = cell.getHeight();
		
		int avgDX = 0;
		int avgDY = 0;
		
		int i = 0;
		int j = 0;
		
		int m = 0;
		int n = 0;
		
		
		
		for(i = 0; i < h; i ++) {
			
			color = cell.getRGB(0, i);
			
			int red = (color >> 16) & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = (color) & 0xFF;
			

			
			sub1 = (red + green + blue) / 3;

			color = cell.getRGB(w - 1, i);
			
			red = (color >> 16) & 0xFF;
			green = (color >> 8) & 0xFF;
			blue = (color) & 0xFF;
			

			sub2 = (red + green + blue) / 3;
			
			avgDX += (sub2 - sub1);
			
			m++;
		}
		
		for(j = 0; j < w; j++) {
			
			color = cell.getRGB(j, 0);
			
			int red = (color >> 16) & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = (color) & 0xFF;
						
			sub1 = (red + green + blue) / 3;
			

			
			color = cell.getRGB(j, h - 1);
			
			red = (color >> 16) & 0xFF;
			green = (color >> 8) & 0xFF;
			blue = (color) & 0xFF;
			

			
			sub2 = (red + green + blue) / 3;
			
			avgDY += (sub2 - sub1);
			
			n++;
		}
		
		double avgDiffX = ((double) avgDX) / (m + 1) ;
		double avgDiffY = ((double) avgDY) / (n + 1);
		
		
		

		
		double lenOfGrad = Math.sqrt(Math.pow(avgDiffY,2) + Math.pow(avgDiffX, 2));
				System.out.println("The avgDiffX = " + avgDiffX + " avgDiffY = " + avgDiffY + " angle = " + angleOfEdge);	
		if(lenOfGrad > 50) {
			isEdge = true;
			angleOfEdge = (Math.atan(avgDiffY/avgDiffX)) * (180/Math.PI);
			System.out.println("The avgDiffX = " + avgDiffX + " avgDiffY = " + avgDiffY + " angle = " + angleOfEdge);
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
