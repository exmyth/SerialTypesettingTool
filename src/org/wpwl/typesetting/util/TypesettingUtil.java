package org.wpwl.typesetting.util;

import java.io.File;

public class TypesettingUtil {

	public static class Size {
		public float width = 0;
		public float height = 0;
		public Size() {
			super();
		}
		public Size(float width, float height) {
			super();
			this.width = width;
			this.height = height;
		}
		@Override
		public String toString() {
			return "Size [width=" + width + ", height=" + height + "]";
		}
	}

	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean 
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

	public static Size measure(String str, float w, float h) {
		Size size = new Size();
		if(str == null || "".equals(str)){
			return size;
		}
		int width = 0;
		if(str.length() == 3){
			width = 68;
		}
		else if(str.length() == 4){
			int count = getNumCount(str);
			if(count == 4){
				width = 57;
			}
			else if(count == 79){
				width = 79;
			}
			else{
				width = 68;
			}
		}
		size.width = width;
		size.height = h * width/w;
		return size;
	}

	private static int getNumCount(String str) {
		int count = 0;
		for(int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if(c >= 48 && c <= 57){
				++count;
			}
		}
		return count;
	}
	
	public static void main(String[] args) {
		System.out.println(measure("1aaa",14f,3.5f));
	}
}
