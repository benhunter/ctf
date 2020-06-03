/*    */ package org.springframework.web.servlet.view.document;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.poi.ss.usermodel.Workbook;
/*    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
/*    */ 
/*    */ public abstract class AbstractXlsxView
/*    */   extends AbstractXlsView
/*    */ {
/*    */   public AbstractXlsxView() {
/* 43 */     setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {
/* 51 */     return (Workbook)new XSSFWorkbook();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/document/AbstractXlsxView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */