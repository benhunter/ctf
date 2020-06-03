/*    */ package org.springframework.web.bind.support;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorFactory;
/*    */ import org.springframework.web.context.ContextLoader;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpringWebConstraintValidatorFactory
/*    */   implements ConstraintValidatorFactory
/*    */ {
/*    */   public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
/* 44 */     return (T)getWebApplicationContext().getAutowireCapableBeanFactory().createBean(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void releaseInstance(ConstraintValidator<?, ?> instance) {
/* 49 */     getWebApplicationContext().getAutowireCapableBeanFactory().destroyBean(instance);
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
/*    */   protected WebApplicationContext getWebApplicationContext() {
/* 61 */     WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
/* 62 */     if (wac == null) {
/* 63 */       throw new IllegalStateException("No WebApplicationContext registered for current thread - consider overriding SpringWebConstraintValidatorFactory.getWebApplicationContext()");
/*    */     }
/*    */     
/* 66 */     return wac;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/SpringWebConstraintValidatorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */