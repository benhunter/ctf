/*     */ package org.springframework.web.servlet.view.document;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.ss.usermodel.Workbook;
/*     */ import org.springframework.web.servlet.view.AbstractView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractXlsView
/*     */   extends AbstractView
/*     */ {
/*     */   public AbstractXlsView() {
/*  47 */     setContentType("application/vnd.ms-excel");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean generatesDownloadContent() {
/*  53 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  64 */     Workbook workbook = createWorkbook(model, request);
/*     */ 
/*     */     
/*  67 */     buildExcelDocument(model, workbook, request, response);
/*     */ 
/*     */     
/*  70 */     response.setContentType(getContentType());
/*     */ 
/*     */     
/*  73 */     renderWorkbook(workbook, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {
/*  87 */     return (Workbook)new HSSFWorkbook();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderWorkbook(Workbook workbook, HttpServletResponse response) throws IOException {
/*  98 */     ServletOutputStream out = response.getOutputStream();
/*  99 */     workbook.write((OutputStream)out);
/* 100 */     workbook.close();
/*     */   }
/*     */   
/*     */   protected abstract void buildExcelDocument(Map<String, Object> paramMap, Workbook paramWorkbook, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/document/AbstractXlsView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */