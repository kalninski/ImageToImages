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
	BufferedImage edgeSource;
	
	ImageCollection imageCollection;
	
	List<BufferedImage> listStripsDest = Collections.synchronizedList(new ArrayList<BufferedImage>());
	List<BufferedImage> listStripsSource = Collections.synchronizedList(new ArrayList<BufferedImage>());
	List<BufferedImage> listStripsEdgeSource = Collections.synchronizedList(new ArrayList<BufferedImage>());
	public ImageToEdit(String path) {
		
		try {
			img = ImageIO.read(new File(path));
			int h = img.getHeight();
			int w = img.getWidth();
			h /= 46;// scale down by avgWidth of a book
			w /= 30; //scale down by the avgHeight of the book
			/*stretching the book disproportionallly cause there to be greater loss of information on one direction than the other =>
			scaling it back to the original size there fore means that the part the was scaled down the most
			 to have to compensate the most => to have vertical "pixels", must scale down height by width and width by height of the avg book
			 dimensions */

			BufferedImage scaledImgTemp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = scaledImgTemp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.drawImage(img, 0, 0, w, h, null);
			img = null;
			h *= 46;
			w *= 30;
			g2.dispose();
			
			scaledImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			outputImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			
			g2 = scaledImg.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.drawImage(scaledImgTemp, 0, 0, w, h, null);
	//		g2.drawImage(img, 0, 0, w, h, null);
			g2.dispose();
			
			BufferedImage imgEdges = ImageIO.read(new File(path));
			edgeSource = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			g2 = edgeSource.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.drawImage(imgEdges, 0, 0, w, h, null);
			
			g2.dispose();
//			try {
//				ImageIO.write(edgeSource, "jpg", new File("C:\\Users\\Toms\\Desktop\\Foto_Prezidents_Edgars_Rinkēvičs\\Foto_Prezidents_Edgars_Rinke╠ävic╠īs\\EDGE_SOURCE.jpg"));
//			}catch(IOException ex) {
//				System.out.println("Something was not right");
//			}
			System.out.println(String.format("avgRed = %d , avgGreen = %d , avgBlue =  %d ", xRed, yGreen ,zBlue));
		//	 ImageIO.write(scaledImg, "jpeg", new File("/Users/maksla/Desktop/ZiemassvetkuKarte/MI_atteli/MIatteli/SCALED.jpeg"));
			 
			 splitIntoStrips();
			
		}catch(IOException ex) {
			System.out.println("The file (ImageToEdit) was not found!");
		}
	}
	

	
	public void splitIntoStrips() {
		
		for(int y = 0; y < scaledImg.getHeight(); y += 46) {
			
			BufferedImage stripD = new BufferedImage(scaledImg.getWidth(), 46, BufferedImage.TYPE_INT_RGB);
			BufferedImage stripS = scaledImg.getSubimage(0, y, scaledImg.getWidth(), 46);
			BufferedImage stripE = edgeSource.getSubimage(0, y, scaledImg.getWidth(), 46);
			
			listStripsDest.add(stripD);
			listStripsSource.add(stripS);
			listStripsEdgeSource.add(stripE);
			
		}
		
	}
	
	public double dotProduct(ImageNode imgNode, Vector sourceColorVec) {
		return ((imgNode.xRedN * sourceColorVec.xRedN) + (imgNode.yGreenN * sourceColorVec.yGreenN) + (imgNode.zBlueN * sourceColorVec.zBlueN));
	}
	
	public double getAngleEdge(BufferedImage strip, int x) {
		
		int color = 0;
		
		int sub1 = 0;
		int sub2 = 0;
		
		
		int avgDX = 0;
		int avgDY = 0;
		
		int i = 0;
		int j = 0;
		
		int m = 0;
		int n = 0;
		
		
		
		for(i = 0; i < 46; i ++) {
			
			color = strip.getRGB(x + 0, i);
			
			int red = (color >> 16) & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = (color) & 0xFF;
			
			
			sub1 = (red + green + blue) / 3;

			color = strip.getRGB(x + 29 , i);
			
			red = (color >> 16) & 0xFF;
			green = (color >> 8) & 0xFF;
			blue = (color) & 0xFF;
			
			
			sub2 = (red + green + blue) / 3;
			
			avgDX += (sub1 - sub2);
			m++;

		}
		
		for(j = 0; j < 30; j++) {
			
			color = strip.getRGB( x + j, 0);
			
			int red = (color >> 16) & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = (color) & 0xFF;
			

			
			sub1 = (red + green + blue) / 3;
			

			color = strip.getRGB(x + j, 45);
			
			red = (color >> 16) & 0xFF;
			green = (color >> 8) & 0xFF;
			blue = (color) & 0xFF;
			
			
			sub2 = (red + green + blue) / 3;
			
			avgDY += (sub2 - sub1);
		
			n++;
		}
		
		double avgDiffX = ((double) avgDX) / (m + 1) ;
		double avgDiffY = ((double) avgDY) / (n + 1); 
		
		double lenGrad = Math.sqrt(Math.pow(avgDiffY, 2) + Math.pow(avgDiffX, 2));
		
		if(lenGrad > 60) {
			return Math.atan(avgDiffY/avgDiffX);
		}
		return 180.0;
		
	}
	
	public void oneStrip(BufferedImage subimg, int indexOf ) {
		BufferedImage subimgOut = listStripsDest.get(indexOf);
		Graphics gOut = subimgOut.getGraphics();
		Graphics2D g2dOut = (Graphics2D) gOut;
		
		ImageNode nodeToUse = null;
		for(int x = 0; x < subimg.getWidth(); x += 30) {
			Vector v = new Vector(subimg, x, 0, imageCollection.size);
			Vector image = new Vector();
			int i = 0;
			double minDotProd = 0.997;
			
			boolean imgFound = false;
	//		synchronized (imageCollection) {// !!!
			for(ImageNode in : imageCollection.listOfImagesInFolder) {//<-- synchronize imageCollection ?
				
				double angle = getAngleEdge(listStripsEdgeSource.get(indexOf), x);
				angle  = angle * (180 / Math.PI);
//				System.out.println("angle  = " + angle);
				
				if(angle != 180 && Math.abs((angle - in.angleOfEdge)) < 10 && Math.abs((angle - in.angleOfEdge)) > 0 &&  dotProduct(in, v) > 0.98 && in.isEdge && Math.abs(in.avgColN - v.avgColN) < 0.1) {
					System.out.println("Angle  = " + angle + " for image : " + in.pathImg);
					nodeToUse = in;
					minDotProd = 1.1;
				}
				
				if(dotProduct(in, v) > minDotProd &&  Math.abs(in.avgColN - v.avgColN) < 0.1) {
					nodeToUse = in;
					minDotProd = dotProduct(in, v);
					i++;
	//		System.out.println("the loop : dot prod improved " + i +  " 'th time" + " row = " + indexOf + " the cell =  " + x);
				}
				
			}
//			}
			if(nodeToUse != null) {
					g2dOut.drawImage(nodeToUse.cell, x, 0, null);
			}else {
				Rectangle2D r2d = new Rectangle2D.Double(x, 0, 30, 46);
				g2dOut.setColor(new Color(v.xOrig, v.yOrig, v.zOrig));
			//	g2dOut.draw(r2d);
				g2dOut.fillRect(x, 0, 30, 46);
			//	imgFound = true;
			}

		}
		//try {
		//	ImageIO.write(subimgOut, "jpeg", new File("/Users/maksla/Desktop/ZiemassvetkuKarte/MI_atteli/MIatteli/SCALED.jpeg"));
		//}catch(IOException ex) {
		//	System.out.println("The image could not we saved!");
		//}
	}
	
	public void renderImage(String newName, String extention) {
		
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
			y += 46;
		}
		
		File output = new File("C:\\Users\\Toms\\Downloads\\" + newName + "." + extention);
		
		try {
			ImageIO.write(outputImg, "jpeg", output);
		}catch(IOException ex) {
			System.out.println( "The image could not be written ! \n" + ex.getMessage());
		}
		
	}
	
	
	
	
}
