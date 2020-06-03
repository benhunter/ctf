/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import javax.servlet.jsp.JspTagException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements EditorAwareTag
/*     */ {
/*     */   public static final String STATUS_VARIABLE_NAME = "status";
/*  95 */   private String path = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ignoreNestedPath = false;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BindStatus status;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object previousPageStatus;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object previousRequestStatus;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(String path) {
/* 119 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 126 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreNestedPath(boolean ignoreNestedPath) {
/* 134 */     this.ignoreNestedPath = ignoreNestedPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoreNestedPath() {
/* 141 */     return this.ignoreNestedPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int doStartTagInternal() throws Exception {
/* 147 */     String resolvedPath = getPath();
/* 148 */     if (!isIgnoreNestedPath()) {
/* 149 */       String nestedPath = (String)this.pageContext.getAttribute("nestedPath", 2);
/*     */ 
/*     */       
/* 152 */       if (nestedPath != null && !resolvedPath.startsWith(nestedPath) && 
/* 153 */         !resolvedPath.equals(nestedPath.substring(0, nestedPath.length() - 1))) {
/* 154 */         resolvedPath = nestedPath + resolvedPath;
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 159 */       this.status = new BindStatus(getRequestContext(), resolvedPath, isHtmlEscape());
/*     */     }
/* 161 */     catch (IllegalStateException ex) {
/* 162 */       throw new JspTagException(ex.getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 166 */     this.previousPageStatus = this.pageContext.getAttribute("status", 1);
/* 167 */     this.previousRequestStatus = this.pageContext.getAttribute("status", 2);
/*     */ 
/*     */ 
/*     */     
/* 171 */     this.pageContext.removeAttribute("status", 1);
/* 172 */     this.pageContext.setAttribute("status", this.status, 2);
/*     */     
/* 174 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() {
/* 180 */     if (this.previousPageStatus != null) {
/* 181 */       this.pageContext.setAttribute("status", this.previousPageStatus, 1);
/*     */     }
/* 183 */     if (this.previousRequestStatus != null) {
/* 184 */       this.pageContext.setAttribute("status", this.previousRequestStatus, 2);
/*     */     } else {
/*     */       
/* 187 */       this.pageContext.removeAttribute("status", 2);
/*     */     } 
/* 189 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BindStatus getStatus() {
/* 197 */     Assert.state((this.status != null), "No current BindStatus");
/* 198 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String getProperty() {
/* 210 */     return getStatus().getExpression();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Errors getErrors() {
/* 220 */     return getStatus().getErrors();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final PropertyEditor getEditor() {
/* 226 */     return getStatus().getEditor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFinally() {
/* 232 */     super.doFinally();
/* 233 */     this.status = null;
/* 234 */     this.previousPageStatus = null;
/* 235 */     this.previousRequestStatus = null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/BindTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */