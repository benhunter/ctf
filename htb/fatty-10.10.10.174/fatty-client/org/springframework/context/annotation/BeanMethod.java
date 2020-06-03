/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.Problem;
/*    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*    */ import org.springframework.core.type.MethodMetadata;
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
/*    */ final class BeanMethod
/*    */   extends ConfigurationMethod
/*    */ {
/*    */   public BeanMethod(MethodMetadata metadata, ConfigurationClass configurationClass) {
/* 37 */     super(metadata, configurationClass);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ProblemReporter problemReporter) {
/* 42 */     if (getMetadata().isStatic()) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 47 */     if (this.configurationClass.getMetadata().isAnnotated(Configuration.class.getName()) && 
/* 48 */       !getMetadata().isOverridable())
/*    */     {
/* 50 */       problemReporter.error(new NonOverridableMethodError());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private class NonOverridableMethodError
/*    */     extends Problem
/*    */   {
/*    */     public NonOverridableMethodError() {
/* 59 */       super(String.format("@Bean method '%s' must not be private or final; change the method's modifiers to continue", new Object[] { this$0
/* 60 */               .getMetadata().getMethodName() }), BeanMethod.this.getResourceLocation());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/BeanMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */