/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.servlet.View;
/*    */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
/*    */ import org.springframework.web.servlet.view.RedirectView;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RedirectViewControllerRegistration
/*    */ {
/*    */   private final String urlPath;
/*    */   private final RedirectView redirectView;
/* 38 */   private final ParameterizableViewController controller = new ParameterizableViewController();
/*    */ 
/*    */   
/*    */   public RedirectViewControllerRegistration(String urlPath, String redirectUrl) {
/* 42 */     Assert.notNull(urlPath, "'urlPath' is required.");
/* 43 */     Assert.notNull(redirectUrl, "'redirectUrl' is required.");
/* 44 */     this.urlPath = urlPath;
/* 45 */     this.redirectView = new RedirectView(redirectUrl);
/* 46 */     this.redirectView.setContextRelative(true);
/* 47 */     this.controller.setView((View)this.redirectView);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RedirectViewControllerRegistration setStatusCode(HttpStatus statusCode) {
/* 57 */     Assert.isTrue(statusCode.is3xxRedirection(), "Not a redirect status code");
/* 58 */     this.redirectView.setStatusCode(statusCode);
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RedirectViewControllerRegistration setContextRelative(boolean contextRelative) {
/* 69 */     this.redirectView.setContextRelative(contextRelative);
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RedirectViewControllerRegistration setKeepQueryParams(boolean propagate) {
/* 79 */     this.redirectView.setPropagateQueryParams(propagate);
/* 80 */     return this;
/*    */   }
/*    */   
/*    */   protected void setApplicationContext(@Nullable ApplicationContext applicationContext) {
/* 84 */     this.controller.setApplicationContext(applicationContext);
/* 85 */     this.redirectView.setApplicationContext(applicationContext);
/*    */   }
/*    */   
/*    */   protected String getUrlPath() {
/* 89 */     return this.urlPath;
/*    */   }
/*    */   
/*    */   protected ParameterizableViewController getViewController() {
/* 93 */     return this.controller;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/RedirectViewControllerRegistration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */