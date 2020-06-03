/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.BodyContent;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class AbstractHtmlElementBodyTag
/*     */   extends AbstractHtmlElementTag
/*     */   implements BodyTag
/*     */ {
/*     */   @Nullable
/*     */   private BodyContent bodyContent;
/*     */   @Nullable
/*     */   private TagWriter tagWriter;
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/*  49 */     onWriteTagContent();
/*  50 */     this.tagWriter = tagWriter;
/*  51 */     if (shouldRender()) {
/*  52 */       exposeAttributes();
/*  53 */       return 2;
/*     */     } 
/*     */     
/*  56 */     return 0;
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
/*     */   public int doEndTag() throws JspException {
/*  68 */     if (shouldRender()) {
/*  69 */       Assert.state((this.tagWriter != null), "No TagWriter set");
/*  70 */       if (this.bodyContent != null && StringUtils.hasText(this.bodyContent.getString())) {
/*  71 */         renderFromBodyContent(this.bodyContent, this.tagWriter);
/*     */       } else {
/*     */         
/*  74 */         renderDefaultContent(this.tagWriter);
/*     */       } 
/*     */     } 
/*  77 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderFromBodyContent(BodyContent bodyContent, TagWriter tagWriter) throws JspException {
/*  87 */     flushBufferedBodyContent(bodyContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFinally() {
/*  95 */     super.doFinally();
/*  96 */     removeAttributes();
/*  97 */     this.tagWriter = null;
/*  98 */     this.bodyContent = null;
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
/*     */   protected void onWriteTagContent() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldRender() throws JspException {
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void exposeAttributes() throws JspException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeAttributes() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flushBufferedBodyContent(BodyContent bodyContent) throws JspException {
/*     */     try {
/* 142 */       bodyContent.writeOut((Writer)bodyContent.getEnclosingWriter());
/*     */     }
/* 144 */     catch (IOException ex) {
/* 145 */       throw new JspException("Unable to write buffered body content.", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void renderDefaultContent(TagWriter paramTagWriter) throws JspException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doInitBody() throws JspException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBodyContent(BodyContent bodyContent) {
/* 163 */     this.bodyContent = bodyContent;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractHtmlElementBodyTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */