package com.libray.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;

/**
 * author :sunji
 * 
 * create time : 2012-8-6
 * 
 * function : sdcard
 * */
public class SDCard {

	private static SDCard mySdcard=null;
	private SDCard()
	{
		
	}
	public  static SDCard getInstance()
	{
		if(mySdcard==null)
			mySdcard=new SDCard();
		return mySdcard;
	}
	/**
	 * 2012-8-6 下午4:27:59 sdcard是否存在
	 * 
	 * @return true
	 */
	public static boolean SDCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 2012-8-6 下午4:28:20 获取sdcard路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		String sdPath = null;
		if (SDCardExist())
			sdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator;
		return sdPath;
	}

	public static String getSDCardPathNoSp() {
		String sdPath = null;
		if (SDCardExist())
			sdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		return sdPath;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File dir = new File(getSDCardPath() + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件是否存�?
	 * 
	 * @param fileName
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(String path, String fileName) {
		File file = new File(getSDCardPath() + path + fileName);
		return file.exists();
	}

	/**
	 * 判断SD卡上的文件是否存�?
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(getSDCardPath() + fileName);
		return file.exists();
	}

	/**
	 * 2012-8-6 下午4:28:44 获取sdcard总大�?
	 * 
	 * @return 返回-1表示sdCard不存�?反之则返回实际大�?
	 */
	public long getTotalSize() {
		if (SDCardExist()) {
			StatFs fs = new StatFs(getSDCardPath());
			// 获取BLOCK数量
			long blocSize = fs.getBlockSize();
			// 总共的Block的数�?
			long totalBlocks = fs.getBlockCount();
			long totalSize = totalBlocks * blocSize;
			return totalSize / 1024;// KB
		}
		return -1;
	}

	/**
	 * 2012-8-6 下午4:29:26 获取sdcard�?��可用大小,单位kb
	 * 
	 * @return 返回-1则表示sdCard不存�?反正则返回实际大�?
	 */
	public long getLeftSize() {
		if (SDCardExist()) {
			StatFs fs = new StatFs(getSDCardPath());
			// 获取BLOCK数量
			long blocSize = fs.getBlockSize();
			// 可使用的Block的数�?
			long availaBlock = fs.getAvailableBlocks();
			long availableSpare = availaBlock * blocSize;
			return availableSpare / 1024;// KB
		} else
			return -1;// SDCATD NOT EXIST
	}

	/**
	 * 2012-8-6 下午4:29:26 删除�?��文件
	 * 
	 * @param path
	 *            文件的相对路�?
	 * @return 返回true为删除成�?反之则false
	 */
	public boolean isDeleteFileSuccess(String path) {
		File file = new File(getSDCardPath() + path);
		if (!file.exists() || file.isDirectory())
			return false;
		if (file.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件夹及其其中的内容
	 * 
	 * @param dir
	 *            文件夹的绝对路径
	 * @return 返回true则返回成功，反之则false
	 * 
	 * **/
	public static boolean delDir(File dir) {
		if (dir == null || !dir.exists() || dir.isFile()) {
			return false;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				delDir(file);// 递归
			}
		}
		dir.delete();
		return true;
	}
	public static void deleteCache(File cache ) {
		if (cache.isFile()) { // 判断是否是文�?
			cache.delete(); // delete()方法 你应该知�?是删除的意�?;
		} else if (cache.isDirectory()) { // 否则如果它是�?��目录
			File files[] = cache.listFiles(); // 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				files[i].delete(); // 把每个文�?用这个方法进行迭�?
			}
			cache.delete();
		}
	}
	/**
	 * 2012-8-7 下午3:10:20 添加空加密串，构成正确的请求信息
	 * 
	 * @return 空串先转换为json再des加密生成的字符串
	 */
	public static void createDataRam(String userID,String cacheType) {
		File file = new File(getDirPath(userID, cacheType));
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 2012-8-7 下午3:10:20 添加空加密串，构成正确的请求信息
	 * 
	 * @return
	 */
	public static void storeInSD(Bitmap bitmap, String name,String userID,String cacheType) {
		File file = new File(getDirPath(userID, cacheType));
		if (!file.exists()) {
			file.mkdirs();
		}
		File imageFile = new File(file, name);
		try {
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bitmap.compress(CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @return
	 */
	public static String getDirPath(String userID,String cacheType) {
		if(userID==null) return "";
		return SDCard.getSDCardPathNoSp() +cacheType;
	}

	/**
	 * 
	 * @return
	 */
	public static String getFilePath(String userID,String cacheType, String fileName) {

		return SDCard.getSDCardPathNoSp() + cacheType + fileName;

	}

}
