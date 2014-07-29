package sk.apupo.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

public class FileUtil {

	public FileUtil() {
	}
	
	public static boolean copyDatabaseFromAssets(Context context, String fileName, String destinationPath) {
		try {
			InputStream is = context.getAssets().open(fileName);
			String outPath = destinationPath + fileName;
			
			File dbFile = new File(outPath);
			if(!dbFile.exists()) {
				dbFile.mkdir();
			}
			
			OutputStream os = new FileOutputStream(outPath);
			byte[] buffer = new byte[1024];
			int length;
			while((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			
			os.flush();
			os.close();
			is.close();
			
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	public static boolean copyDatabaseFromAssetsPartByPart(Context context, String fileName, String outPath, String partname) {
		if(partname == null) partname = "part";
        AssetManager am = context.getAssets();
        OutputStream os;
        String[] assetFiles;
		try {
			os = new FileOutputStream(outPath + fileName);
			assetFiles = am.list("");
			
	        byte[] buffer = new byte[1024];
	        
	        Arrays.sort(assetFiles);
	        int r;
	        List<String> parts = new ArrayList<String>();
	        for (String string : assetFiles) {
				if(string.contains(partname)) {
					parts.add(string);
				}
			}
	        
	        for (int i = 0; i < parts.size(); i++) {
	            InputStream is = am.open(partname + i);
	            while ((r = is.read(buffer)) != -1) {
	                os.write(buffer, 0, r);
	            }
	            is.close();
	        }
	        
	        os.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
        
        return true;
    }	
	
	

}
