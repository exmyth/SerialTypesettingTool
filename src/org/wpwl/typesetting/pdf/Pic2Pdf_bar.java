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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author 姜建成
 * @version ：1.0 Version
 * description:
 * create time：2015年10月15日 
 * 目前在生产的标签，老的，带条码
 *
 */
public class Pic2Pdf_bar {

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
				System.out.println("举例: MatrixPdf e:\\test e:\\pdf 120");
				return;
			}
			
			String picPath = args[0];
			String pdfPath = args[1];
			String picNum  = args[2];
			
			float left = 35.9f;
			float top = 743f;
			float widthStep = 119;
			float heightStep = 105;
			int pageSize = 40;
			int rowSize = 5;
			float step = 116.75f;
			float vStep = 101.85f;
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = Pic2Pdf_bar.class.getClassLoader();
            }
            InputStream fileStream = null;
            fileStream = classLoader.getResourceAsStream("04.jpg");
            File so = File.createTempFile("back02", ".jpg");
            if (fileStream != null) {
                
                FileOutputStream out = new FileOutputStream(so);

                int i;
                byte[] buf = new byte[1024];
                while ((i = fileStream.read(buf)) != -1) {
                    out.write(buf, 0, i);
                }
                fileStream.close();
                out.close();
                so.deleteOnExit();
            }    
            Image img = Image.getInstance(so.getPath());
			img.setAlignment(Image.UNDERLYING);
//			img.scalePercent(23.9f);
			img.scalePercent(24.2f);
//			img.sc
			img.setAbsolutePosition(-10f, -8f);
//			img.setAbsolutePosition(0, 0);
			
//			document.add(new Paragraph("Hello World"));			
			for(int k = 1; k <= (Integer.parseInt(picNum))/ pageSize; k++) {
				Rectangle rect = new Rectangle(609,842);
//				Document document = new Document();
				Document document = new Document(rect);
				PdfWriter.getInstance(document, new FileOutputStream( pdfPath + "\\" + String.format("%06d", k) + ".pdf"));
				document.open();
				//document.add(img);
				for(int i = 1; i <= 8 ; i++) {
					for(int j = 1; j <=5 ; j++) {
						Image png = Image.getInstance(picPath+"\\"+ ((k-1)*pageSize+(i-1)*rowSize + j) + ".png");
						float leftPos,toPos;
						leftPos = 0.0f;
						toPos = 0.0f;
						if( i == 1) {
							toPos = top + 0.61f + 7f;
						}
						if( i == 2) {
							toPos = top -vStep + 0.37f+ 7f;
						}
						if( i == 3) {
							toPos = top -vStep * 2f+ 7f;
						}
						if( i == 4) {
							toPos = top -vStep * 3f+ 7f;
						}
						if( i == 5) {
							toPos = top -vStep * 4f+ 7f;
						}
						if( i == 6) {
							toPos = top -vStep * 5f - 0.38f+ 7f;
						}
						if( i == 7) {
							toPos = top -vStep * 6f - 0.64f+ 7f;
						}
						if( i == 8) {
							toPos = top -vStep * 7f - 0.82f+ 7f;
						}
						/*
						if( i == 1) {
							toPos = top - heightStep*(i-1)+4.8f-2.2f;
						}
						if( i == 2) {
							toPos = top - heightStep*(i-1)+8.3f-1.2f;
						}	
						if( i == 3) {
							toPos = top - heightStep*(i-1)+12.3f-0.2f;
						}						
//						png.setAbsolutePosition(left + step * (j-1), top - step*(i-1));
						if( i == 4 ) {
							toPos = top - heightStep*(i-1)+15.95f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-1);
						}
						if( i == 5 ) {
							toPos = top - heightStep*(i-1)+20f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-1);
						}	
						if( i == 6 ) {
							toPos = top - heightStep*(i-1)+23.65f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-2);
						}
						if( i == 7 ) {
							toPos = top - heightStep*(i-1)+28;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-2);
						}	
						if( i == 8 ) {
							toPos = top - heightStep*(i-1)+30.1f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-2);
						} 
						/*
						if( j == 1 ) {
							leftPos = left + (j-1) * widthStep-0.08f + 6.6f; 
						}
						if( j == 2 ) {
							leftPos = left + (j-1) * widthStep-2.3f + 6.5f; 
						}
						
						if( j == 3 ) {
							leftPos = left + (j-1) * widthStep-5.2f + 6.6f; 
						}
						if( j == 4 ) {
							leftPos = left + (j-1) * widthStep-7.5f + 6.6f; 
						}
						if( j == 5 ) {
							leftPos = left + (j-1) * widthStep-10f + 6.6f; 
						}
						*/
						if( j == 1 ) {
							leftPos = left + 1.5f;//12.356f;
						}
						
						if( j == 2) {
							leftPos = left + step + 0.87f; //
						}
						if( j == 3 ) {
							leftPos = left + step * 2 + 0.8f ;
						}
						
						if( j == 4) {
							leftPos = left + step * 3 +0.2f;
						}
						if( j == 5 ) {
							leftPos = left + step * 4 - 0.5f;
						}
						png.setAbsolutePosition(leftPos,toPos);
//						png.scalePercent(45.5f);
						png.scalePercent(46.05f);
						document.add(png);
						
						//底下增加条码
						Image barPng = Image.getInstance(picPath+"\\"+ ((k-1)*pageSize+(i-1)*rowSize + j) + "_bar.png");
						float barLeft,barTop;
						barLeft = 0.0f;
						barTop = 0.0f;
						if( i == 1) {
							barTop = top - heightStep*(i-1)+4.8f - 25f+ 7f;
						}
						if( i == 2) {
							barTop = top - heightStep*(i-1)+8.3f - 25f+ 7f;
						}	
						if( i == 3) {
							barTop = top - heightStep*(i-1)+12.3f - 26f+ 7f;
						}						
//						png.setAbsolutePosition(left + step * (j-1), top - step*(i-1));
						if( i == 4 ) {
							barTop = top - heightStep*(i-1)+15.95f - 27f+ 7f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-1);
						}
						if( i == 5 ) {
							barTop = top - heightStep*(i-1)+20f - 28f+ 7f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-1);
						}	
						if( i == 6 ) {
							barTop = top - heightStep*(i-1)+23.65f - 28.5f+ 7f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-2);
						}
						if( i == 7 ) {
							barTop = top - heightStep*(i-1)+28 - 30f+ 7f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-2);
						}	
						if( i == 8 ) {
							barTop = top - heightStep*(i-1)+30.1f - 29f+ 7f;
//							png.setAbsolutePosition(left + step * (j-1), top - step*(i-1)-2);
						} 
						if( j == 1 ) {
							barLeft = left + (j-1) * widthStep-5.5f ; 
						}
						if( j == 2 ) {
							barLeft = left + (j-1) * widthStep-1.9f -6.1f; 
						}
						if( j == 3 ) {
							barLeft = left + (j-1) * widthStep+2.2f -13.1f;   
						}
						if( j == 4 ) {
							barLeft = left + (j-1) * widthStep+5.7f -18.1f; 
						}
						if( j == 5 ) {
							barLeft = left + (j-1) * widthStep+10.5f -27.1f; 
						}						
//						if( j == 1 ) {
//							barLeft = left + (j-1) * widthStep-15.5f; 
//						}
//						if( j == 2 ) {
//							barLeft = left + (j-1) * widthStep-2.3f; 
//						}
//						if( j == 3 ) {
//							barLeft = left + (j-1) * widthStep-5.2f; 
//						}
//						if( j == 4 ) {
//							barLeft = left + (j-1) * widthStep-7.5f; 
//						}
//						if( j == 5 ) {
//							barLeft = left + (j-1) * widthStep-11f; 
//						}
						barPng.setAbsolutePosition(barLeft,barTop);
//						png.scalePercent(45.5f);
						barPng.scalePercent(84f,55);
						document.add(barPng);	
					}
				}
				//一页PDF完成，保存文件
				document.close();
			}
//			Image png = Image.getInstance("e:\\test\\1.png");
//			png.scaleAbsolute(145, 145);
//			scalePercent(int percent)
//			png.setBorder(Image.BOX);
//			png.setBorderWidth(1);
//			png.setAbsolutePosition(66, 737);
//			png.scalePercent(45.5f);
//			Image img = Image.getInstance("e:\\test\\2.png");
//			img.setAbsolutePosition(165, 737);
//			img.scalePercent(45.5f);
//			document.add(img);
//			document.add(png);
//			document.add(img);
			
//			Graphics grx = new Graphic();
//			grx.rectangle(100, 700, 100, 100);
//			Chunk chunk1 = new Chunk("下载APP验伪 沃朴物联",FontFactory.getFont(FontFactory.HELVETICA, 12, Font.UNDERLINE)); 
//			document.add(chunk1);
			System.out.println("PDF 文件生成Ok");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
