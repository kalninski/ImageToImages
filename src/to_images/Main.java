package to_images;

import java.awt.image.*;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	//	ImageNode n1 = new ImageNode("C:\\Users\\Toms\\Desktop\\kaut_kas_ko_zimet\\Zvaigz╠īn╠¦u da╠äva╠äta╠äja.jpg");
		
		ImageCollection ic = new ImageCollection("C:\\Users\\Toms\\Desktop\\kaut_kas_ko_zimet");
		
	//	for(ImageNode iN : ic.listOfImagesInFolder) {
			
	//		System.out.println("The avgCol = " + iN.avgCol);
			
	//	}
		
	//	ic.sort((ImageNode n1, ImageNode n2) -> n1.avgCol > n2.avgCol);

		ImageToEdit iTE = new ImageToEdit("C:\\Users\\Toms\\Downloads\\coffee_upscayl_6x_ultramix-balanced-4x_upscayl_4x_ultramix-balanced-4x.png");
		
		iTE.imageCollection = ic;
		iTE.oneStrip(iTE.listStripsSource.get(0), 0);
		
		iTE.renderImage("NewImg");
	}

}
