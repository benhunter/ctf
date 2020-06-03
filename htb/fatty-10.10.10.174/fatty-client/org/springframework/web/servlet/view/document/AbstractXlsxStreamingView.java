/*    */ package org.springframework.web.servlet.view.document;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.apache.poi.ss.usermodel.Workbook;
/*    */ import org.apache.poi.xssf.streaming.SXSSFWorkbook;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractXlsxStreamingView
/*    */   extends AbstractXlsxView
/*    */ {
/*    */   protected SXSSFWorkbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {
/* 44 */     return new SXSSFWorkbook();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void renderWorkbook(Workbook workbook, HttpServletResponse response) throws IOException {
/* 53 */     super.renderWorkbook(workbook, response);
/*    */ 
/*    */     
/* 56 */     ((SXSSFWorkbook)workbook).dispose();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/document/AbstractXlsxStreamingView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */