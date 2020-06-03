/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.BodyTagSupport;
/*     */ import javax.servlet.jsp.tagext.Tag;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ArgumentTag
/*     */   extends BodyTagSupport
/*     */ {
/*     */   @Nullable
/*     */   private Object value;
/*     */   private boolean valueSet;
/*     */   
/*     */   public void setValue(Object value) {
/*  70 */     this.value = value;
/*  71 */     this.valueSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() throws JspException {
/*  77 */     Object argument = null;
/*  78 */     if (this.valueSet) {
/*  79 */       argument = this.value;
/*     */     }
/*  81 */     else if (getBodyContent() != null) {
/*     */       
/*  83 */       argument = getBodyContent().getString().trim();
/*     */     } 
/*     */ 
/*     */     
/*  87 */     ArgumentAware argumentAwareTag = (ArgumentAware)findAncestorWithClass((Tag)this, ArgumentAware.class);
/*  88 */     if (argumentAwareTag == null) {
/*  89 */       throw new JspException("The argument tag must be a descendant of a tag that supports arguments");
/*     */     }
/*  91 */     argumentAwareTag.addArgument(argument);
/*     */     
/*  93 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public void release() {
/*  98 */     super.release();
/*  99 */     this.value = null;
/* 100 */     this.valueSet = false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/ArgumentTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */