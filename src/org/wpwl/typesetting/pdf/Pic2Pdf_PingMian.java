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

import org.wpwl.typesetting.util.TypesettingUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author WPWL
 *
 */
public class Pic2Pdf_PingMian {
	public static void main(String[] args) {
		try {
			if( args.length <4) {
				System.out.println("使用方式: 命令  图片目录  PDF目录  图片总数");
				System.out.println("举例: MatrixPdf d:\\outputpic d:\\outputpdf 30 31111110000000001");
				return;
			}
			
			String picPath = args[0];
			String pdfPath = args[1];
			String picNum  = args[2];
			String startId = args[3];
			
			File pdfDir = new File(pdfPath);
			// 清空之前保存的pdf文件
			if (pdfDir.exists()) {
				TypesettingUtil.deleteDir(pdfDir);
			}
			// 重建目录
			pdfDir.mkdirs();
			
			int rowSize = 10;	//行数
			int colSize = 2;	//列数
			int pageSize = rowSize * colSize;	//页大小
			
			int width = 110;		//尺寸-宽(mm)
			int heigh = 290;		//尺寸-高(mm)
			float scale = 2.835f;	//1mm=2.835px
			float urx = width * scale;	//尺寸-宽(px)
			float ury = heigh * scale;	//尺寸-高(px)
			
			Long startIndex = Long.parseLong(startId);
			int pageCount = (Integer.parseInt(picNum))/pageSize;//页数
			int num = (Integer.parseInt(picNum))%pageSize;
			if(num >0){
				pageCount = pageCount + 1;
			}
			for(int k = 1; k <= pageCount; k++) {
				Document document = new Document(new Rectangle(urx,ury));
				String pdfFileName = String.format("%06d", k) + ".pdf";
				PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream( pdfPath + "\\" + pdfFileName));
				document.open();
				//标签id文字设置
				PdfContentByte serialText = pdfWriter.getDirectContent();
//				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.CP1252, BaseFont.NOT_EMBEDDED);  					
//				BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
				
				BaseFont bf = BaseFont.createFont("msyh.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				
				serialText.setFontAndSize(bf, 5);
				serialText.beginText();
				
				float absoluteX,absoluteY;	//二维码坐标,值越大越往上移动，值越小越往下移动
				float x,y;					//文字x,y坐标
				Image png;					//png图片对象
				int index;					//png图片索引1...picNum
				for(int i = 1; i <= rowSize ; i++) {
					for(int j = 1; j <=colSize ; j++) {
						index = (k-1)*pageSize+(i-1)*colSize + j;
						if(index>Integer.parseInt(picNum)){
							break;//结束分页排版
						}
						png = Image.getInstance(picPath+"\\"+ index + ".png");
						absoluteX = (4 + j *(2.5f) + (j - 1)*(18+3f+24+2.5f+2))*scale;
						absoluteY = ury - (2 + i *(3.5f+18) + (i-1)*(3.5f+4))*scale;
						
						png.scalePercent(18*scale/148*100);//二维码尺寸是148x148,底板是24mm,伸缩百分比就是24*scale/148*100
						png.setAbsolutePosition(absoluteX,absoluteY);
						document.add(png);
						
						if(i == 1 && j == 1){
							System.out.println("--------------------------------");
						}
						System.out.println("absoluteX:"+absoluteX+",absoluteY:"+absoluteY);
						
						x = absoluteX+0.5f*scale + 24;//文字比二维码偏移值是24,这个值是测试出来的
						y = absoluteY-(1+1.4f)*scale;
//						
//						//底下增加标签ID
						serialText.showTextAligned(PdfContentByte.ALIGN_CENTER, startIndex + (i-1) * colSize + j - 1 + "",x,y,0);
					}
				}
				
				serialText.endText();
				startIndex += pageSize; //增加一页，为下一页的开始
				//一页PDF完成，保存文件
				document.close();
			}
			
			Runtime.getRuntime().exec("cmd.exe /c start " + pdfPath);
			System.out.println("PDF 文件生成Ok");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
