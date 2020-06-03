/*     */ package org.springframework.web.servlet.view.document;
/*     */ 
/*     */ import com.lowagie.text.Document;
/*     */ import com.lowagie.text.DocumentException;
/*     */ import com.lowagie.text.PageSize;
/*     */ import com.lowagie.text.pdf.PdfWriter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public abstract class AbstractPdfView
/*     */   extends AbstractView
/*     */ {
/*     */   public AbstractPdfView() {
/*  60 */     setContentType("application/pdf");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean generatesDownloadContent() {
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  74 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/*     */ 
/*     */     
/*  77 */     Document document = newDocument();
/*  78 */     PdfWriter writer = newWriter(document, baos);
/*  79 */     prepareWriter(model, writer, request);
/*  80 */     buildPdfMetadata(model, document, request);
/*     */ 
/*     */     
/*  83 */     document.open();
/*  84 */     buildPdfDocument(model, document, writer, request, response);
/*  85 */     document.close();
/*     */ 
/*     */     
/*  88 */     writeToResponse(response, baos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Document newDocument() {
/*  99 */     return new Document(PageSize.A4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
/* 110 */     return PdfWriter.getInstance(document, os);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request) throws DocumentException {
/* 131 */     writer.setViewerPreferences(getViewerPreferences());
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
/*     */   protected int getViewerPreferences() {
/* 145 */     return 2053;
/*     */   }
/*     */   
/*     */   protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {}
/*     */   
/*     */   protected abstract void buildPdfDocument(Map<String, Object> paramMap, Document paramDocument, PdfWriter paramPdfWriter, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/document/AbstractPdfView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */