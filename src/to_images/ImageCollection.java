package to_images;

import java.awt.Image;
import java.io.*;
import java.util.*;

public class ImageCollection {
	
	ArrayList<ImageNode> listOfImagesInFolder;
	PriorityQueue<ImageNode> pQueue;
	int size;
	
	public ImageCollection(String folderPath) {
		listOfImagesInFolder = new ArrayList<ImageNode>();
		pQueue = new PriorityQueue<ImageNode>((ImageNode a, ImageNode b) -> a.avgCol - b.avgCol);
		File folder = new File(folderPath);
		File[] imgList = folder.listFiles();
		for(File f : imgList) {
	//	System.out.println("path = " + f.getPath());
			if(!f.getPath().contains(".ini")) {
			listOfImagesInFolder.add(new ImageNode(f.getPath()));
			}
		}
		size = listOfImagesInFolder.size();
		listOfImagesInFolder.sort((ImageNode n1, ImageNode n2) -> n1.avgCol - n2.avgCol);
	}
	
	
	

	

	
}
