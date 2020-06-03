/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
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
/*    */ 
/*    */ 
/*    */ public class ViewControllerRegistration
/*    */ {
/*    */   private final String urlPath;
/* 37 */   private final ParameterizableViewController controller = new ParameterizableViewController();
/*    */ 
/*    */   
/*    */   public ViewControllerRegistration(String urlPath) {
/* 41 */     Assert.notNull(urlPath, "'urlPath' is required.");
/* 42 */     this.urlPath = urlPath;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ViewControllerRegistration setStatusCode(HttpStatus statusCode) {
/* 51 */     this.controller.setStatusCode(statusCode);
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setViewName(String viewName) {
/* 64 */     this.controller.setViewName(viewName);
/*    */   }
/*    */   
/*    */   protected void setApplicationContext(@Nullable ApplicationContext applicationContext) {
/* 68 */     this.controller.setApplicationContext(applicationContext);
/*    */   }
/*    */   
/*    */   protected String getUrlPath() {
/* 72 */     return this.urlPath;
/*    */   }
/*    */   
/*    */   protected ParameterizableViewController getViewController() {
/* 76 */     return this.controller;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/ViewControllerRegistration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */