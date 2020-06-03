/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.util.HtmlUtils;
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
/*     */ public abstract class HtmlEscapingAwareTag
/*     */   extends RequestContextAwareTag
/*     */ {
/*     */   @Nullable
/*     */   private Boolean htmlEscape;
/*     */   
/*     */   public void setHtmlEscape(boolean htmlEscape) throws JspException {
/*  54 */     this.htmlEscape = Boolean.valueOf(htmlEscape);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isHtmlEscape() {
/*  63 */     if (this.htmlEscape != null) {
/*  64 */       return this.htmlEscape.booleanValue();
/*     */     }
/*     */     
/*  67 */     return isDefaultHtmlEscape();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isDefaultHtmlEscape() {
/*  78 */     return getRequestContext().isDefaultHtmlEscape();
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
/*     */   protected boolean isResponseEncodedHtmlEscape() {
/*  90 */     return getRequestContext().isResponseEncodedHtmlEscape();
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
/*     */   protected String htmlEscape(String content) {
/* 104 */     String out = content;
/* 105 */     if (isHtmlEscape()) {
/* 106 */       if (isResponseEncodedHtmlEscape()) {
/* 107 */         out = HtmlUtils.htmlEscape(content, this.pageContext.getResponse().getCharacterEncoding());
/*     */       } else {
/*     */         
/* 110 */         out = HtmlUtils.htmlEscape(content);
/*     */       } 
/*     */     }
/* 113 */     return out;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/HtmlEscapingAwareTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */