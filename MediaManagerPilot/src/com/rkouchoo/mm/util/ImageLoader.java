package com.rkouchoo.mm.util;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.rkouchoo.mm.MediaManager;

public class ImageLoader {

	static public ArrayList<Image> loadIconImages(MediaManager fileManager, String path) throws Exception {
	
		URL urlBig = fileManager.getClass().getResource(path);
		URL urlSmall = fileManager.getClass().getResource(path);
		ArrayList<Image> images = new ArrayList<Image>();
		
		images.add(ImageIO.read(urlBig));
		images.add(ImageIO.read(urlSmall));
		
		return images;
	}

}
