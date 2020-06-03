/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterizableViewController
/*     */   extends AbstractController
/*     */ {
/*     */   @Nullable
/*     */   private Object view;
/*     */   @Nullable
/*     */   private HttpStatus statusCode;
/*     */   private boolean statusOnly;
/*     */   
/*     */   public ParameterizableViewController() {
/*  51 */     super(false);
/*  52 */     setSupportedMethods(new String[] { HttpMethod.GET.name(), HttpMethod.HEAD.name() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewName(@Nullable String viewName) {
/*  61 */     this.view = viewName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getViewName() {
/*  70 */     if (this.view instanceof String) {
/*  71 */       String viewName = (String)this.view;
/*  72 */       if (getStatusCode() != null && getStatusCode().is3xxRedirection()) {
/*  73 */         return viewName.startsWith("redirect:") ? viewName : ("redirect:" + viewName);
/*     */       }
/*     */       
/*  76 */       return viewName;
/*     */     } 
/*     */     
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setView(View view) {
/*  88 */     this.view = view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public View getView() {
/*  98 */     return (this.view instanceof View) ? (View)this.view : null;
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
/*     */   public void setStatusCode(@Nullable HttpStatus statusCode) {
/* 113 */     this.statusCode = statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpStatus getStatusCode() {
/* 122 */     return this.statusCode;
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
/*     */   public void setStatusOnly(boolean statusOnly) {
/* 134 */     this.statusOnly = statusOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatusOnly() {
/* 141 */     return this.statusOnly;
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
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 155 */     String viewName = getViewName();
/*     */     
/* 157 */     if (getStatusCode() != null) {
/* 158 */       if (getStatusCode().is3xxRedirection()) {
/* 159 */         request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, getStatusCode());
/*     */       } else {
/*     */         
/* 162 */         response.setStatus(getStatusCode().value());
/* 163 */         if (getStatusCode().equals(HttpStatus.NO_CONTENT) && viewName == null) {
/* 164 */           return null;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 169 */     if (isStatusOnly()) {
/* 170 */       return null;
/*     */     }
/*     */     
/* 173 */     ModelAndView modelAndView = new ModelAndView();
/* 174 */     modelAndView.addAllObjects(RequestContextUtils.getInputFlashMap(request));
/* 175 */     if (viewName != null) {
/* 176 */       modelAndView.setViewName(viewName);
/*     */     } else {
/*     */       
/* 179 */       modelAndView.setView(getView());
/*     */     } 
/* 181 */     return modelAndView;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 186 */     return "ParameterizableViewController [" + formatStatusAndView() + "]";
/*     */   }
/*     */   
/*     */   private String formatStatusAndView() {
/* 190 */     StringBuilder sb = new StringBuilder();
/* 191 */     if (this.statusCode != null) {
/* 192 */       sb.append("status=").append(this.statusCode);
/*     */     }
/* 194 */     if (this.view != null) {
/* 195 */       sb.append((sb.length() != 0) ? ", " : "");
/* 196 */       String viewName = getViewName();
/* 197 */       sb.append("view=").append((viewName != null) ? ("\"" + viewName + "\"") : this.view);
/*     */     } 
/* 199 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/ParameterizableViewController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */