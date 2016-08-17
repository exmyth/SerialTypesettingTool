package org.wpwl.typesetting.pic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Map;

import org.wpwl.typesetting.util.MatrixToImageWriter;
import org.wpwl.typesetting.util.StringUtil;
import org.wpwl.typesetting.util.TypesettingUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 版权所有@: 杭州沃朴物联科技有限公司
 * 创建时间: 2015年12月15日下午4:41:30
 * 注意：本内容仅限于杭州沃朴物联科技有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2015 Hangzhou wopuwulian Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by wopuwulian Technology Co.,Ltd. Others are forbidden. 
 * Created on: 2015年12月15日下午4:41:30
 * author WPWL
 */

public class BarCode2Pic_KangJunShui {
	public static void main(String[] args) {
		try {
			 if( args.length < 4) {
				 System.out.println("使用方式: 命令  序号前半部分 序号后半部分 标签总数 保存目录");
				 System.out.println("举例: BarCode2Pic A 000000001 1500 D:\\Test\\PicWater");
			 }
			 else {
				 	/**
				 	 * radix = 4:16进制
				 	 * radix = 5:32进制
				 	 * radix = 6:64进制
				 	 */
				 	final int radix = 5;	//进制
				 	final int radixLength = 6;	//进制字符串的长度
				    float w = 14f;			//上条码图片宽度 
				    float h = 3.5f;			//上条码图片高度 
				 	int width1 = 68;			//上条码图片宽度 
				 	int height1 = 17;			//上条码图片宽度 
					int width2 = 0;			//下条码图片宽度
					int height2 = 35;			//上条码图片宽度 
					//float scale = 2.835f;	//1mm=2.835px
					int height = 20;		//Math.round(h * scale * 10);
					String format = "png";	//条码格式
					
					String serialHeader = args[0];
					String serialString = args[1];
					String serialNum = args[2];
					String savePath = args[3];
					
					long start1 = Long.parseLong(serialString);
					int num = Integer.parseInt(serialNum);
					if(start1 + num > 999999999){
						System.out.println("序号集合存在超过999999，条码图片生成失败");
						return;
					}
					
					File saveDir = new File(savePath);
					//清空之前保存的条码图片
					if(saveDir.exists()){
						TypesettingUtil.deleteDir(saveDir);
					}
					//重建目录
					saveDir.mkdirs();
					//----------------------设置条码参数----------------------
					ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
					Map<EncodeHintType,Object> hints= new Hashtable<EncodeHintType,Object>();
					hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
					hints.put(EncodeHintType.ERROR_CORRECTION, level); //容错率
					hints.put(EncodeHintType.MARGIN,0);  //二维码边框宽度，这
					//BarCode2Pic.txt为保存标签编号的文件,需要设置标签号放在条码下，可以读取该文件每一行，依次放置
					File barcode = new File(savePath,"BarCode2Pic.txt");
					//清空之前的条码记录
					if(barcode.exists()){
						barcode.delete();
					}
					//重建新文件
					barcode.createNewFile();
					
					FileWriter fw = new FileWriter(barcode);
					BufferedWriter bw = new BufferedWriter(fw);
					
					//采用10位
//		            BitMatrix barMatrix = new MultiFormatWriter().encode("AAA",BarcodeFormat.CODE_128, width1, height, hints);
//		            File outputFile = new File(savePath + "\\" + "000_1_bar.png"); 
//					MatrixToImageWriter.writeToFile(barMatrix, format, outputFile);
					
					String serialBottom = null;
					for(int i = 0; i < num; i ++) {
						long serialNo = start1 + i;
						// 文字编码 ,生成条码 
//			            Hashtable<EncodeHintType, String> barCodeHints = new Hashtable<EncodeHintType, String>();  
//			            barCodeHints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
			            
//						String radixStr = Long.toString(serialNo, radix);
						String radixStr = StringUtil.compressNumber(serialNo, radix);
			            String serialStr = StringUtil.formatString("0", radixLength, radixStr);
			            
			            String serialTop = serialHeader + serialStr.substring(0, 2);
//			            Size size1 = TypesettingUtil.measure(serialTop, width, height);
			            //采用10位
			            BitMatrix barMatrix1 = new MultiFormatWriter().encode(serialTop,BarcodeFormat.CODE_128, width1, height1, hints);
			            File outputFile1 = new File(savePath + "\\" + (i+1) + "_1_bar.png"); 
						MatrixToImageWriter.writeToFile(barMatrix1, format, outputFile1); 
			            
			            serialBottom = serialStr.substring(2);
//			            Size size2 = TypesettingUtil.measure(serialBottom, width, height);
			            BitMatrix barMatrix2 = new MultiFormatWriter().encode(serialBottom,BarcodeFormat.CODE_128, width2, height2, hints);
			            File outputFile2 = new File(savePath + "\\" + (i+1) + "_2_bar.png");
						MatrixToImageWriter.writeToFile(barMatrix2, format, outputFile2); 
						
						bw.write(serialHeader + String.format("%09d", serialNo));
						bw.newLine();
					}
					bw.flush();
					bw.close();
					System.out.println("条码图片生成成功");
					//打开条码图片生成目录
					Runtime.getRuntime().exec("cmd.exe /c start " + savePath);
			 }
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
	}

}
