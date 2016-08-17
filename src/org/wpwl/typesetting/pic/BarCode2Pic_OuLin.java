package org.wpwl.typesetting.pic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Map;

import org.wpwl.typesetting.util.MatrixToImageWriter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * 
 * @author wpwl-hfq
 * 欧林
 * 抗菌水圆形标签(生成图片)
 *
 */
public class BarCode2Pic_OuLin {
	public static void main(String[] args) {
		try {
			 if( args.length < 4) {
				 System.out.println("使用方式: 命令  序号前半部分 序号后半部分 标签总数 保存目录");
				 System.out.println("举例: BarCode2Pic B000 000001 18000 d:\\PicOuLin");
			 }
			 else {
					int width = 120; 
					int height = 26;
					String format = "png";
					
					String serialStart = args[0];
					String serialEnd = args[1];
					String serialNum = args[2];
					String savePath = args[3];
					
					long start1 = Long.parseLong(serialEnd);
					int num = Integer.parseInt(serialNum);
					if(start1 + num > 999999){
						System.out.println("序号集合存在超过999999，条码图片生成失败");
						return;
					}
					
					File saveDir = new File(savePath);
					//清空之前的条码图片
					if(saveDir.exists()){
						deleteDir(saveDir);
					}
					saveDir.mkdirs();
					
					ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
					Map<EncodeHintType,Object> hints= new Hashtable<EncodeHintType,Object>();
					hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
					hints.put(EncodeHintType.ERROR_CORRECTION, level); //容错率
					hints.put(EncodeHintType.MARGIN,0);  //二维码边框宽度，这
					
					File barcode = new File(savePath,"BarCode2Pic.txt");
					//清空之前的条码记录
					if(barcode.exists()){
						barcode.delete();
					}
					barcode.createNewFile();
					
					FileWriter fw = new FileWriter(barcode);
					BufferedWriter bw = new BufferedWriter(fw);
					for(int i = 0; i < num; i ++) {
						long serialNo = start1 + i;
						// 文字编码 ,生成条码 
//			            Hashtable<EncodeHintType, String> barCodeHints = new Hashtable<EncodeHintType, String>();  
//			            barCodeHints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
			            
			            String serialStr = String.format("%06d", serialNo);
			            //老的标签，采用10位
//			            BitMatrix barMatrix1 = new MultiFormatWriter().encode(serialStart,BarcodeFormat.CODE_128, width, height, hints);
//			            File outputFile1 = new File(savePath + "\\" + (i+1) + "_1_bar.png"); 
//						MatrixToImageWriter.writeToFile(barMatrix1, format, outputFile1); 
			            
			            
			            BitMatrix barMatrix2 = new MultiFormatWriter().encode(serialStart+serialStr,BarcodeFormat.CODE_128, width, height, hints);
//						File outputFile2 = new File(savePath + "\\" + serialStart+serialStr+"_bar.png"); 
			            File outputFile2 = new File(savePath + "\\" + (i+1) + "_bar.png");
						MatrixToImageWriter.writeToFile(barMatrix2, format, outputFile2); 
						bw.write(serialStart+serialStr);
						bw.newLine();
					}
					bw.flush();
					bw.close();
					System.out.println("条码图片生成成功");
					Runtime.getRuntime().exec("cmd.exe /c start " + savePath);
			 }
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
	}
	
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
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
}
