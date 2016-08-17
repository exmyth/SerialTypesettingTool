/**
 * 版权所有@: 杭州沃朴物联科技有限公司
 * 创建时间: 2015年10月15日下午4:59:00
 * 注意：本内容仅限于杭州沃朴物联科技有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2015 Hangzhou wopuwulian Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by wopuwulian Technology Co.,Ltd. Others are forbidden. 
 * Created on: 2015年10月15日下午4:59:00
 */
package org.wpwl.typesetting.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author 姜建成
 * @version ：1.0 Version
 * description:
 * create time：2015年10月15日 
 * 抗菌水圆形标签(图片生成pdf)
 *
 */
public class BarPic2Pdf_Water3 {

	/**
	 * @authoer：jason
	 * @param args
	 * description:
	 * create time： 2015年10月15日
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			if( args.length <3) {
				System.out.println("使用方式: 命令  图片目录  PDF目录  图片总数");
				System.out.println("举例: BarPic2Pdf d:\\PicWater3 d:\\DdfWater3 30");
				return;
			}
			
			String picPath = args[0];
			String pdfPath = args[1];
			String picNum  = args[2];
			
			File saveDir = new File(pdfPath);
			if(!saveDir.exists()){
				saveDir.mkdirs();
			}
			
			//需要调整的坐标阈值
			float left = 56.8f;		//第一列的左边距
			float top = 732f;		//第一行的上边距
			int pageSize = 30;		//打印一张的标签数量
			int rowSize = 6;		//一张纸的标签行数
			int colSize = 5;		//一张纸的标签列数
			float step = 114f;		//第二行距第一行的间隔(step值越大,left越大,越向右)
			float vStep = 140.4f;	//第二列距第一列的间隔(vStep值越大,top越小,越向下)
			
			
			BufferedReader br = new BufferedReader(new FileReader(new File(picPath,"BarCode2Pic.txt")));
			int pageCount = (Integer.parseInt(picNum))/pageSize;
			int num = (Integer.parseInt(picNum))%pageSize;
			if(num >0){
				pageCount = pageCount + 1;
			}
			for(int k = 1; k <= pageCount; k++) {
				Rectangle rect = new Rectangle(609,842);
				Document document = new Document(rect);
				String pdfFileName = String.format("%06d", k) + ".pdf";
				PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream( pdfPath + "\\" + pdfFileName));

				
				document.open();
				
				//底下增加标签ID
				PdfContentByte serialText = pdfWriter.getDirectContent();
				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
//				BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
				serialText.setFontAndSize(bf, 8);
				serialText.beginText();
				
				float absoluteX = 0.0f;
				for(int i = 1; i <= rowSize; i++) {
					for(int j = 1; j <=colSize; j++) {
						if(((k-1)*pageSize+(i-1)*colSize + j)>Integer.parseInt(picNum)){
							break;
						}
						int index = (k-1)*pageSize+(i-1)*colSize + j;
						
						Image png1 = Image.getInstance(picPath+"\\"+ index + "_1_bar.png");
						Image png2 = Image.getInstance(picPath+"\\"+ index + "_2_bar.png");
						
						int mod = index % 36;
						
						float leftPos = 0.0f;
						float toPos = 0.0f;
						float scale = 62.5f;
						
						//--------------------行图标top调整(第三个开始需要调整偏差)-----------------------
						if( i == 1) {
							toPos = top;
						}
						if( i == 2) {
							toPos = top -vStep;
						}
						if( i == 3) {
							toPos = top -vStep * 2f + 0.4f;	
						}
						if( i == 4) {
							toPos = top -vStep * 3f + 0.1f;
						}
						if( i == 5) {
							toPos = top -vStep * 4f + 0.2f;
						}
						if( i == 6) {
							toPos = top -vStep * 5f + 0.2f;
						}
						//--------------------列图标left调整(第三个开始需要调整偏差)-----------------------
						if( j == 1 ) {
							leftPos = left;
						}
						if( j == 2) {
							leftPos = left + step + 0.2f;
						}
						if( j == 3 ) {
							leftPos = left + step * 2 + 0.2f;
						}
						if( j == 4) {
							leftPos = left + step * 3 + 0.3f;
						}
						if( j == 5 ) {
							leftPos = left + step * 4 + 0.4f;
						}
						
						/*
						if(mod<10){
							absoluteX = leftPos;
						}
						else{
							absoluteX = leftPos-4;
						}
						*/
						
						png1.setAbsolutePosition(leftPos,toPos+80);//leftPos越大,越靠上;toPos越小,越靠左
						png1.scalePercent(scale);
						document.add(png1);
						
						png2.setAbsolutePosition(leftPos,toPos);//leftPos越大,越靠上;toPos越小,越靠左
						png2.scalePercent(scale);
						document.add(png2);
						serialText.showTextAligned(PdfContentByte.ALIGN_CENTER, br.readLine(),leftPos + 20, toPos-20 , 0);
					}
				}
				
				//一页PDF完成，保存文件
				serialText.endText();
				document.close();
				System.out.println(pdfFileName+"文件生成Ok");
			}
			br.close();
			Runtime.getRuntime().exec("cmd.exe /c start " + pdfPath);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
