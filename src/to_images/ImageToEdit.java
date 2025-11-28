package to_images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.imageio.ImageIO;



public class ImageToEdit {
	
	int xRed;
	int yGreen;
	int zBlue;
	
	double xRedN;
	double yGreenN;
	double zBlueN;
	
	
	int avgBookHeight;
	int avgBookWidth;
	
	int sum;
	
	BufferedImage img;
	BufferedImage scaledImg;
	BufferedImage outputImg;
	ImageCollection imageCollection;
	
	List<BufferedImage> listStripsDest = Collections.synchronizedList(new ArrayList<BufferedImage>());
	List<BufferedImage> listStripsSource = Collections.synchronizedList(new ArrayList<BufferedImage>());
	
	public ImageToEdit(String path) {
		
		try {
			img = ImageIO.read(new File(path));
			int h = img.getHeight();
			int w = img.getWidth();
			h /= 56;// scale down by avgWidth of a book
			w /= 30; //scale down by the avgHeight of the book
			/*stretching the book disproportionallly cause there to be greater loss of information on one direction than the other =>
			scaling it back to the original size there fore means that the part the was scaled down the most
			 to have to compensate the most => to have vertical "pixels", must scale down height by width and width by height of the avg book
			 dimensions */

			BufferedImage scaledImgTemp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = scaledImgTemp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.drawImage(img, 0, 0, w, h, null);
			
			h *= 56;
			w *= 30;
			g2.dispose();
			
			scaledImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			outputImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			g2 = scaledImg.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.drawImage(scaledImgTemp, 0, 0, w, h, null);
	//		g2.drawImage(img, 0, 0, w, h, null);
			g2.dispose();

			System.out.println(String.format("avgRed = %d , avgGreen = %d , avgBlue =  %d ", xRed, yGreen ,zBlue));
		//	 ImageIO.write(scaledImg, "jpeg", new File("/Users/maksla/Desktop/ZiemassvetkuKarte/MI_atteli/MIatteli/SCALED.jpeg"));
			 
			 splitIntoStrips();
			
		}catch(IOException ex) {
			System.out.println("The file (ImageToEdit) was not found!");
		}
	}
	

	
	public void splitIntoStrips() {
		
		for(int y = 0; y < scaledImg.getHeight(); y += 56) {
			
			BufferedImage stripD = new BufferedImage(scaledImg.getWidth(), 56, BufferedImage.TYPE_INT_RGB);
			BufferedImage stripS = scaledImg.getSubimage(0, y, scaledImg.getWidth(), 56);
			
			listStripsDest.add(stripD);
			listStripsSource.add(stripS);
			
		}
		
	}
	
	public double dotProduct(ImageNode imgNode, Vector sourceColorVec) {
		return ((imgNode.xRedN * sourceColorVec.xRedN) + (imgNode.yGreenN * sourceColorVec.yGreenN) + (imgNode.zBlueN * sourceColorVec.zBlueN));
	}
	
	public void oneStrip(BufferedImage subimg, int indexOf ) {
		BufferedImage subimgOut = listStripsDest.get(indexOf);
		Graphics gOut = subimgOut.getGraphics();
		Graphics2D g2dOut = (Graphics2D) gOut;
		
		
		for(int x = 0; x < subimg.getWidth(); x += 30) {
			Vector v = new Vector(subimg, x, 0, imageCollection.size);
			Vector image = new Vector();
			int i = 0;
			boolean imgFound = false;
			for(ImageNode in : imageCollection.listOfImagesInFolder) {
				
				if(dotProduct(in, v) > 0.99) {
					g2dOut.drawImage(in.cell, x, 0, null);
					imgFound = true;

				}
				i++;
			}
			if(!imgFound) {
				Rectangle2D r2d = new Rectangle2D.Double(x, 0, 30, 56);
				g2dOut.setColor(new Color(v.xOrig, v.yOrig, v.zOrig));
			//	g2dOut.draw(r2d);
				g2dOut.fillRect(x, 0, 30, 56);
			//	imgFound = true;
			}

		}
		//try {
		//	ImageIO.write(subimgOut, "jpeg", new File("/Users/maksla/Desktop/ZiemassvetkuKarte/MI_atteli/MIatteli/SCALED.jpeg"));
		//}catch(IOException ex) {
		//	System.out.println("The image could not we saved!");
		//}
	}
	
	public void renderImage(String newName) {
		
		ArrayList<Thread> listOfThreads = new ArrayList<Thread>();
		
		for(int i = 0; i < listStripsSource.size(); i++) {
			
			final int ind = i;
			final BufferedImage subimg = listStripsSource.get(i);
			
			Thread t = Thread.ofVirtual().start(() -> {
				oneStrip(subimg, ind);
			});
			listOfThreads.add(t);
			
		}
		
		for(Thread th : listOfThreads) {
			try {
				th.join();
			}catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		int y = 0;
		for(BufferedImage subiOut : listStripsDest) {
			outputImg.getGraphics().drawImage(subiOut, 0, y, null);
			y += 56;
		}
		
		File output = new File("/Users/maksla/Desktop/ZiemassvetkuKarte/MI_atteli/MIatteli/NAHUJ.jpeg");
		
		try {
			ImageIO.write(outputImg, "jpeg", output);
		}catch(IOException ex) {
			System.out.println( "The image could not be written ! \n" + ex.getMessage());
		}
		
	}
	
	
	
	
}
