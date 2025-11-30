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
		
	BufferedImage img;
	BufferedImage cell;
	
	public ImageNode(String path) {
		
		try {
			img = ImageIO.read(new File(path));
			int h = img.getHeight();
			int w = img.getWidth();

			Image oneCell = img.getScaledInstance(30, 46, Image.SCALE_DEFAULT);
			Image avgColorPix = img.getScaledInstance(1, 1, Image.SCALE_SMOOTH);

			cell = new BufferedImage(30, 46, BufferedImage.TYPE_INT_RGB);
			BufferedImage pix = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			pathImg = path;
			img = null;
			cell.getGraphics().drawImage(oneCell, 0, 0, null);
			pix.getGraphics().drawImage(avgColorPix, 0, 0, null);
			
			int color = pix.getRGB(0,0);
			xRed = (color >> 16) & 0xFF;
			yGreen = (color >> 8) & 0xFF;
			zBlue = (color) & 0xFF;
			avgCol = (xRed + yGreen + zBlue)/3;
			getGradient();
			setAvgPixel();
			
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
		
		
		
		for(i = 0; i < h; i +=2) {
			
			color = cell.getRGB(0, i);
			
			int red = (color >> 16) & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = (color) & 0xFF;
			
			avgRed += red;
			avgGreen += green;
			avgBlue += blue;
			
			sub1 = (red + green + blue) / 3;
			m++;
			color = cell.getRGB(w - 1, i);
			
			red = (color >> 16) & 0xFF;
			green = (color >> 8) & 0xFF;
			blue = (color) & 0xFF;
			
			avgRed += red;
			avgGreen += green;
			avgBlue += blue;
			
			sub2 = (red + green + blue) / 3;
			
			avgDX += (sub1 - sub2);
			
			m++;
		}
		
		for(j = 0; j < w; j+=2) {
			
			color = cell.getRGB(j, 0);
			
			int red = (color >> 16) & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = (color) & 0xFF;
			
			avgRed += red;
			avgGreen += green;
			avgBlue += blue;
			
			sub1 = (red + green + blue) / 3;
			n++;
			
			color = cell.getRGB(j, h - 1);
			
			red = (color >> 16) & 0xFF;
			green = (color >> 8) & 0xFF;
			blue = (color) & 0xFF;
			
			avgRed += red;
			avgGreen += green;
			avgBlue += blue;
			
			sub2 = (red + green + blue) / 3;
			
			avgDY += (sub2 - sub1);
			
			n++;
		}
		
		double avgDiffX = ((double) avgDX) / m ;
		double avgDiffY = ((double) avgDY) / n;
		
		
		
		divisor = m + n;
		
		double lenOfGrad = Math.sqrt(Math.pow(avgDiffY,2) + Math.pow(avgDiffX, 2));
				System.out.println("The avgDiffX = " + avgDiffX + " avgDiffY = " + avgDiffY + " angle = " + angleOfEdge);	
		if(lenOfGrad > 60) {
			isEdge = true;
			angleOfEdge = (Math.atan(avgDiffY/avgDiffX)) * (180/Math.PI);
			System.out.println("The avgDiffX = " + avgDiffX + " avgDiffY = " + avgDiffY + " angle = " + angleOfEdge);
		}
		
	}
	
	void setAvgPixel() {
		int j = 0;
		for(int i = 1; i < 16; i++) {
			j++;
			divisor ++;

			int color = cell.getRGB(i, j);
			
			avgRed += (color >> 16) & 0xFF;
			avgGreen += (color >> 8) & 0xFF;
			avgBlue += (color) & 0xFF;
		}
		
		avgRed /= divisor;
		avgGreen /= divisor;
		avgBlue /= divisor;
		
			
		xRedN = avgRed;
		yGreenN = avgGreen;
		zBlueN = avgBlue;
		
		
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
