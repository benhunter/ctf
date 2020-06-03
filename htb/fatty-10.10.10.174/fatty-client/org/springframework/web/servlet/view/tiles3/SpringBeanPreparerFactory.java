/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import org.apache.tiles.TilesException;
/*    */ import org.apache.tiles.preparer.ViewPreparer;
/*    */ import org.springframework.web.context.WebApplicationContext;
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
/*    */ public class SpringBeanPreparerFactory
/*    */   extends AbstractSpringPreparerFactory
/*    */ {
/*    */   protected ViewPreparer getPreparer(String name, WebApplicationContext context) throws TilesException {
/* 39 */     return (ViewPreparer)context.getBean(name, ViewPreparer.class);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/SpringBeanPreparerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */