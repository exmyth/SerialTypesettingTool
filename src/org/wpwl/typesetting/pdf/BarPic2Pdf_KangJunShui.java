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

import org.wpwl.typesetting.util.StringUtil;
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
public class BarPic2Pdf_KangJunShui {
	public static void main(String[] args) {
		try {
			if( args.length <3) {
				System.out.println("使用方式: 命令  图片目录  PDF目录  图片总数");
				System.out.println("举例: BarPic2Pdf D:\\Test\\PicWater D:\\Test\\PdfWater 1500");
				return;
			}
			
			String picPath = args[0];
			String pdfPath = args[1];
			String picNum  = args[2];
			
			File pdfDir = new File(pdfPath);
			// 清空之前保存的pdf文件
			if (pdfDir.exists()) {
				TypesettingUtil.deleteDir(pdfDir);
			}
			// 重建目录
			pdfDir.mkdirs();
			
			int rowSize = 3;	//行数
			int colSize = 3;	//列数
			int pageSize = rowSize * colSize;	//页大小
			
			int width = 120;		//尺寸-宽(mm)
			int heigh = 124;		//尺寸-高(mm)
			float w = 14f;		//尺寸-宽(mm)
			float h = 3.5f;		//尺寸-高(mm)
			float scale = 2.835f;	//1mm=2.835px
			float urx = width * scale;	//尺寸-宽(px)
			float ury = heigh * scale;	//尺寸-高(px)
			
			BufferedReader br = new BufferedReader(new FileReader(new File(picPath,"BarCode2Pic.txt")));
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
				
				serialText.setFontAndSize(bf, 6);
				serialText.beginText();
				
				float absoluteX,absoluteY;	//二维码坐标,值越大越往上移动，值越小越往下移动
				Image png1;					//png1图片对象
				Image png2;					//png2图片对象
				int index;					//png图片索引1...picNum
				float newWidth = w * scale;
				float newHeight = h * scale;
				for(int i = 1; i <= rowSize ; i++) {
					for(int j = 1; j <=colSize; j++) {
						if(((k-1)*pageSize+(i-1)*colSize + j)>Integer.parseInt(picNum)){
							break;
						}
						index = (k-1)*pageSize+(i-1)*colSize + j;
						String serialNo = br.readLine();
						String number = StringUtil.formatString("0", 6, StringUtil.compressNumber(Long.valueOf(serialNo.substring(1)), 5));
						
						png1 = Image.getInstance(picPath+"\\"+ index + "_1_bar.png");
						png2 = Image.getInstance(picPath+"\\"+ index + "_2_bar.png");
						absoluteX = (16 + (i - 1)*(53 - 16))*scale;
						absoluteY = ury - (14.55f + (j - 1)*(38.95f+9.1f+3.5f-14.55f))*scale;
						png1.scalePercent(14*scale/TypesettingUtil.measure("A"+number.substring(0, 2), w, h).width*100);
						png1.setAbsolutePosition(absoluteX, absoluteY);
						png1.setScaleToFitHeight(true);
						document.add(png1);
						
						absoluteY = absoluteY - (38.95f-14.55f)*scale;
						png2.scalePercent(14*scale/TypesettingUtil.measure(number.substring(3), w, h).width*100);
						png2.setAbsolutePosition(absoluteX, absoluteY);
						png2.scaleAbsoluteWidth(newWidth);
						png2.scaleAbsoluteHeight(newHeight);
						png2.setScaleToFitHeight(true);
						document.add(png2);
						//标签底部增加文字
						serialText.showTextAligned(PdfContentByte.ALIGN_CENTER, serialNo ,absoluteX + 20, absoluteY-15 , 0);
					}
				}
				serialText.endText();
				//一页PDF完成，保存文件
				document.close();
			}
			br.close();
			Runtime.getRuntime().exec("cmd.exe /c start " + pdfPath);
			System.out.println("PDF 文件生成Ok");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
