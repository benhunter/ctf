/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.BodyContent;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.util.JavaScriptUtils;
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
/*     */ public class EscapeBodyTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements BodyTag
/*     */ {
/*     */   private boolean javaScriptEscape = false;
/*     */   @Nullable
/*     */   private BodyContent bodyContent;
/*     */   
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {
/*  87 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int doStartTagInternal() {
/*  94 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doInitBody() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBodyContent(BodyContent bodyContent) {
/* 104 */     this.bodyContent = bodyContent;
/*     */   }
/*     */ 
/*     */   
/*     */   public int doAfterBody() throws JspException {
/*     */     try {
/* 110 */       String content = readBodyContent();
/*     */       
/* 112 */       content = htmlEscape(content);
/* 113 */       content = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(content) : content;
/* 114 */       writeBodyContent(content);
/*     */     }
/* 116 */     catch (IOException ex) {
/* 117 */       throw new JspException("Could not write escaped body", ex);
/*     */     } 
/* 119 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String readBodyContent() throws IOException {
/* 128 */     Assert.state((this.bodyContent != null), "No BodyContent set");
/* 129 */     return this.bodyContent.getString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBodyContent(String content) throws IOException {
/* 139 */     Assert.state((this.bodyContent != null), "No BodyContent set");
/* 140 */     this.bodyContent.getEnclosingWriter().print(content);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/EscapeBodyTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */