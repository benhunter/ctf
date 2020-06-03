/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.tiles.TilesException;
/*    */ import org.apache.tiles.preparer.PreparerException;
/*    */ import org.apache.tiles.preparer.ViewPreparer;
/*    */ import org.apache.tiles.preparer.factory.NoSuchPreparerException;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class SimpleSpringPreparerFactory
/*    */   extends AbstractSpringPreparerFactory
/*    */ {
/* 43 */   private final Map<String, ViewPreparer> sharedPreparers = new ConcurrentHashMap<>(16);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ViewPreparer getPreparer(String name, WebApplicationContext context) throws TilesException {
/* 49 */     ViewPreparer preparer = this.sharedPreparers.get(name);
/* 50 */     if (preparer == null) {
/* 51 */       synchronized (this.sharedPreparers) {
/* 52 */         preparer = this.sharedPreparers.get(name);
/* 53 */         if (preparer == null) {
/*    */           try {
/* 55 */             Class<?> beanClass = ClassUtils.forName(name, context.getClassLoader());
/* 56 */             if (!ViewPreparer.class.isAssignableFrom(beanClass)) {
/* 57 */               throw new PreparerException("Invalid preparer class [" + name + "]: does not implement ViewPreparer interface");
/*    */             }
/*    */             
/* 60 */             preparer = (ViewPreparer)context.getAutowireCapableBeanFactory().createBean(beanClass);
/* 61 */             this.sharedPreparers.put(name, preparer);
/*    */           }
/* 63 */           catch (ClassNotFoundException ex) {
/* 64 */             throw new NoSuchPreparerException("Preparer class [" + name + "] not found", ex);
/*    */           } 
/*    */         }
/*    */       } 
/*    */     }
/* 69 */     return preparer;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/SimpleSpringPreparerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */