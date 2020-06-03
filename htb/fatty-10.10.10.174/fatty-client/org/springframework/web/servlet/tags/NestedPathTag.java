/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.TagSupport;
/*     */ import javax.servlet.jsp.tagext.TryCatchFinally;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NestedPathTag
/*     */   extends TagSupport
/*     */   implements TryCatchFinally
/*     */ {
/*     */   public static final String NESTED_PATH_VARIABLE_NAME = "nestedPath";
/*     */   @Nullable
/*     */   private String path;
/*     */   @Nullable
/*     */   private String previousNestedPath;
/*     */   
/*     */   public void setPath(@Nullable String path) {
/*  86 */     if (path == null) {
/*  87 */       path = "";
/*     */     }
/*  89 */     if (path.length() > 0 && !path.endsWith(".")) {
/*  90 */       path = path + ".";
/*     */     }
/*  92 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPath() {
/* 100 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doStartTag() throws JspException {
/* 108 */     this
/* 109 */       .previousNestedPath = (String)this.pageContext.getAttribute("nestedPath", 2);
/*     */     
/* 111 */     String nestedPath = (this.previousNestedPath != null) ? (this.previousNestedPath + getPath()) : getPath();
/* 112 */     this.pageContext.setAttribute("nestedPath", nestedPath, 2);
/*     */     
/* 114 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() {
/* 122 */     if (this.previousNestedPath != null) {
/*     */       
/* 124 */       this.pageContext.setAttribute("nestedPath", this.previousNestedPath, 2);
/*     */     }
/*     */     else {
/*     */       
/* 128 */       this.pageContext.removeAttribute("nestedPath", 2);
/*     */     } 
/*     */     
/* 131 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doCatch(Throwable throwable) throws Throwable {
/* 136 */     throw throwable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doFinally() {
/* 141 */     this.previousNestedPath = null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/NestedPathTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */