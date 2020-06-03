/*    */ package org.springframework.web.servlet.view.document;
/*    */ 
/*    */ import com.lowagie.text.pdf.PdfReader;
/*    */ import com.lowagie.text.pdf.PdfStamper;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractPdfStamperView
/*    */   extends AbstractUrlBasedView
/*    */ {
/*    */   public AbstractPdfStamperView() {
/* 52 */     setContentType("application/pdf");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean generatesDownloadContent() {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 66 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/*    */     
/* 68 */     PdfReader reader = readPdfResource();
/* 69 */     PdfStamper stamper = new PdfStamper(reader, baos);
/* 70 */     mergePdfDocument(model, stamper, request, response);
/* 71 */     stamper.close();
/*    */ 
/*    */     
/* 74 */     writeToResponse(response, baos);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected PdfReader readPdfResource() throws IOException {
/* 86 */     String url = getUrl();
/* 87 */     Assert.state((url != null), "'url' not set");
/* 88 */     return new PdfReader(obtainApplicationContext().getResource(url).getInputStream());
/*    */   }
/*    */   
/*    */   protected abstract void mergePdfDocument(Map<String, Object> paramMap, PdfStamper paramPdfStamper, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/document/AbstractPdfStamperView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */