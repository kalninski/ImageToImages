package to_images;

import java.awt.image.*;
import java.time.Clock;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Clock clock = Clock.systemDefaultZone();
		
		long start = clock.millis();
		
	//	ImageNode n1 = new ImageNode("C:\\Users\\Toms\\Desktop\\Foto_Prezidents_Edgars_Rinkēvičs\\Foto_Prezidents_Edgars_Rinke╠ävic╠īs\\New folder\\Ziema╠ä vajag sniegu!.jpg");
		
		ImageCollection ic = new ImageCollection("C:\\Users\\Toms\\Desktop\\kaut_kas_ko_zimet");
		
	//	for(ImageNode iN : ic.listOfImagesInFolder) {
			
	//		System.out.println("The avgCol = " + iN.avgCol);
			
	//	}
		
	//	ic.sort((ImageNode n1, ImageNode n2) -> n1.avgCol > n2.avgCol);

		ImageToEdit iTE = new ImageToEdit("C:\\Users\\Toms\\Downloads\\Larry_le_malicieux_Upscaled_4x.png");
	//	ImageToEdit iTE1 = new ImageToEdit("C:\\Users\\Toms\\Downloads\\Larry_small_upscayl_4x_ultramix-balanced-4x.png");
	//	iTE1.imageCollection = ic;
		iTE.imageCollection = ic;
	//	iTE.oneStrip(iTE.listStripsSource.get(0), 0);
		
		iTE.renderImage("LARRY_LE_MALICIEUX", "png");
//		iTE1.renderImage("Larriy_larry", "png");
		long end = clock.millis();
		System.out.println("Time it took to render = " + (end - start) + " milliseconds");
	}

}
