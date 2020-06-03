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
/*     */ public class ParamTag
/*     */   extends BodyTagSupport
/*     */ {
/*  65 */   private String name = "";
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String value;
/*     */ 
/*     */   
/*     */   private boolean valueSet;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  77 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  84 */     this.value = value;
/*  85 */     this.valueSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() throws JspException {
/*  91 */     Param param = new Param();
/*  92 */     param.setName(this.name);
/*  93 */     if (this.valueSet) {
/*  94 */       param.setValue(this.value);
/*     */     }
/*  96 */     else if (getBodyContent() != null) {
/*     */       
/*  98 */       param.setValue(getBodyContent().getString().trim());
/*     */     } 
/*     */ 
/*     */     
/* 102 */     ParamAware paramAwareTag = (ParamAware)findAncestorWithClass((Tag)this, ParamAware.class);
/* 103 */     if (paramAwareTag == null) {
/* 104 */       throw new JspException("The param tag must be a descendant of a tag that supports parameters");
/*     */     }
/*     */     
/* 107 */     paramAwareTag.addParam(param);
/*     */     
/* 109 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public void release() {
/* 114 */     super.release();
/* 115 */     this.name = "";
/* 116 */     this.value = null;
/* 117 */     this.valueSet = false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/ParamTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */