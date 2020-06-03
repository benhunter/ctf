/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import org.apache.tiles.TilesException;
/*    */ import org.apache.tiles.preparer.ViewPreparer;
/*    */ import org.apache.tiles.preparer.factory.PreparerFactory;
/*    */ import org.apache.tiles.request.Request;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ import org.springframework.web.servlet.DispatcherServlet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSpringPreparerFactory
/*    */   implements PreparerFactory
/*    */ {
/*    */   public ViewPreparer getPreparer(String name, Request context) {
/* 42 */     WebApplicationContext webApplicationContext = (WebApplicationContext)context.getContext("request").get(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*    */     
/* 44 */     if (webApplicationContext == null) {
/* 45 */       webApplicationContext = (WebApplicationContext)context.getContext("application").get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*    */       
/* 47 */       if (webApplicationContext == null) {
/* 48 */         throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*    */       }
/*    */     } 
/* 51 */     return getPreparer(name, webApplicationContext);
/*    */   }
/*    */   
/*    */   protected abstract ViewPreparer getPreparer(String paramString, WebApplicationContext paramWebApplicationContext) throws TilesException;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/AbstractSpringPreparerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */