/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import javax.validation.ValidationException;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class OptionalValidatorFactoryBean
/*    */   extends LocalValidatorFactoryBean
/*    */ {
/*    */   public void afterPropertiesSet() {
/*    */     try {
/* 40 */       super.afterPropertiesSet();
/*    */     }
/* 42 */     catch (ValidationException ex) {
/* 43 */       LogFactory.getLog(getClass()).debug("Failed to set up a Bean Validation provider", (Throwable)ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/OptionalValidatorFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */