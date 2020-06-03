/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringValueResolver;
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
/*    */ 
/*    */ 
/*    */ public class EmbeddedValueResolver
/*    */   implements StringValueResolver
/*    */ {
/*    */   private final BeanExpressionContext exprContext;
/*    */   @Nullable
/*    */   private final BeanExpressionResolver exprResolver;
/*    */   
/*    */   public EmbeddedValueResolver(ConfigurableBeanFactory beanFactory) {
/* 46 */     this.exprContext = new BeanExpressionContext(beanFactory, null);
/* 47 */     this.exprResolver = beanFactory.getBeanExpressionResolver();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String resolveStringValue(String strVal) {
/* 54 */     String value = this.exprContext.getBeanFactory().resolveEmbeddedValue(strVal);
/* 55 */     if (this.exprResolver != null && value != null) {
/* 56 */       Object evaluated = this.exprResolver.evaluate(value, this.exprContext);
/* 57 */       value = (evaluated != null) ? evaluated.toString() : null;
/*    */     } 
/* 59 */     return value;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/EmbeddedValueResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */