/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.validation.Errors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindErrorsTag
/*     */   extends HtmlEscapingAwareTag
/*     */ {
/*     */   public static final String ERRORS_VARIABLE_NAME = "errors";
/*  75 */   private String name = "";
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Errors errors;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  85 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  92 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int doStartTagInternal() throws ServletException, JspException {
/*  98 */     this.errors = getRequestContext().getErrors(this.name, isHtmlEscape());
/*  99 */     if (this.errors != null && this.errors.hasErrors()) {
/* 100 */       this.pageContext.setAttribute("errors", this.errors, 2);
/* 101 */       return 1;
/*     */     } 
/*     */     
/* 104 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() {
/* 110 */     this.pageContext.removeAttribute("errors", 2);
/* 111 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Errors getErrors() {
/* 120 */     return this.errors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFinally() {
/* 126 */     super.doFinally();
/* 127 */     this.errors = null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/BindErrorsTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */