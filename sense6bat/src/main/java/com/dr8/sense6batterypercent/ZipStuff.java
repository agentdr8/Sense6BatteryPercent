package com.dr8.sense6batterypercent;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressLint("DefaultLocale")
public class ZipStuff {

	public static boolean unpackZip(String outpath, String zipname) {
		InputStream is;
		ZipInputStream zis;
		try {
			String filename;
			is = new FileInputStream(zipname);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[1024];
			int count;
			while ((ze = zis.getNextEntry()) != null) {
				filename = ze.getName();
				if (ze.isDirectory()) {
					File fmd = new File(outpath + filename);
					fmd.mkdirs();
					fmd.setWritable(true, true);
					fmd.setExecutable(true, false);
					fmd.setReadable(true, false);
					continue;
				}
				FileOutputStream fout = new FileOutputStream(outpath + filename);
				while ((count = zis.read(buffer)) != -1) {
					fout.write(buffer, 0, count);             
				}
				fout.close();               
				zis.closeEntry();
			}
			zis.close();
			File nomedia = new File(outpath + ".nomedia");
			nomedia.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Bitmap getBitmap(final String FilePath, final String imageFile) {

		Bitmap result = null;
		InputStream fis = null;
		try {
			try {
				fis = new FileInputStream(FilePath + imageFile);
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
                e.printStackTrace();
            }
            result = BitmapFactory.decodeStream(fis);
			result.setDensity(Bitmap.DENSITY_NONE);
			//        	result.setDensity(320);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		} 	
		return result;
	}

}
