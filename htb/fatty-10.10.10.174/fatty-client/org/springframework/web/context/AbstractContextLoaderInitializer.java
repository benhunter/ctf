/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import java.util.EventListener;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.context.ApplicationContextInitializer;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.WebApplicationInitializer;
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
/*    */ 
/*    */ 
/*    */ public abstract class AbstractContextLoaderInitializer
/*    */   implements WebApplicationInitializer
/*    */ {
/* 45 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */   
/*    */   public void onStartup(ServletContext servletContext) throws ServletException {
/* 50 */     registerContextLoaderListener(servletContext);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void registerContextLoaderListener(ServletContext servletContext) {
/* 60 */     WebApplicationContext rootAppContext = createRootApplicationContext();
/* 61 */     if (rootAppContext != null) {
/* 62 */       ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
/* 63 */       listener.setContextInitializers(getRootApplicationContextInitializers());
/* 64 */       servletContext.addListener((EventListener)listener);
/*    */     } else {
/*    */       
/* 67 */       this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
/*    */     } 
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
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected abstract WebApplicationContext createRootApplicationContext();
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
/*    */   @Nullable
/*    */   protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
/* 95 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/AbstractContextLoaderInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */