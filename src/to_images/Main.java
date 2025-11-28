package to_images;

import java.awt.image.*;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	//	ImageNode n1 = new ImageNode("/Users/maksla/Desktop/ZiemassvetkuKarte/MI_atteli/MIatteli/mi8wymq248d2kz.jpeg");
		
		ImageCollection ic = new ImageCollection("/Users/maksla/Desktop/ZiemassvetkuKarte/MI_atteli/MIatteli");
		
	//	for(ImageNode iN : ic.listOfImagesInFolder) {
			
	//		System.out.println("The avgCol = " + iN.avgCol);
			
	//	}
		
	//	ic.sort((ImageNode n1, ImageNode n2) -> n1.avgCol > n2.avgCol);

		ImageToEdit iTE = new ImageToEdit("/Users/maksla/Desktop/KalendaraAtteli/IMG_6615 copy 2.jpg");
		iTE.imageCollection = ic;
		iTE.oneStrip(iTE.listStripsSource.get(0), 0);
		
		iTE.renderImage("NewImg");
	}

}
