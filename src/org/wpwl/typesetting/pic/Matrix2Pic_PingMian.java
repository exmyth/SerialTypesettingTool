package org.wpwl.typesetting.pic;

import java.io.File;
import java.util.Hashtable;

import org.wpwl.typesetting.util.MatrixToImageWriter;
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

public class Matrix2Pic_PingMian {
	public static void main(String[] args) {
		try {
			if (args.length < 4) {
				System.out.println("使用方式: 命令  字符串前缀  标签开始序号  标签总数 保存目录");
				System.out.println("举例: MatrixCode http://wpwl.org/d.htm 31111110000000001 30 d:\\outputpic");
			} else {
				/*
				float x = 24f;
				float y = 24f;
				float scale = 2.835f;	//1mm=2.835px
				int width = (int) (x * scale);
				int height = (int) (y * scale);
				*/
				int magnitude = 4;
				int unit = 37;
				int width = unit * magnitude;
				int height = unit * magnitude;
				String format = "png";
				String str = args[0];
				String serialStart = args[1];
				String serialNum = args[2];
				String savePath = args[3];

				File saveDir = new File(savePath);
				// 清空之前保存的条码图片
				if (saveDir.exists()) {
					TypesettingUtil.deleteDir(saveDir);
				}
				// 重建目录
				saveDir.mkdirs();

				long start1 = Long.parseLong(serialStart);
				ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
				Hashtable<EncodeHintType,Object> hints = new Hashtable<EncodeHintType,Object>();
				hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
				hints.put(EncodeHintType.ERROR_CORRECTION, level); // 容错率
				hints.put(EncodeHintType.MARGIN, 0); // 二维码边框宽度，这
				String temp = null;
				for (int i = 0; i < Integer.parseInt(serialNum); i++) {
					long serialNo = start1 + i;

					temp = str + "?" + serialNo;
					BitMatrix bitMatrix = new MultiFormatWriter().encode(temp, BarcodeFormat.QR_CODE, width, height,hints);
					// BitMatrix result = MatrixCode.updateBit(bitMatrix, 0);
					File outputFile = new File(savePath + "\\" + (i + 1) + ".png");
					MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
					bitMatrix.clear();
				}
				System.out.println("二维码图片生成成功");
				// 打开条码图片生成目录
				Runtime.getRuntime().exec("cmd.exe /c start " + savePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
