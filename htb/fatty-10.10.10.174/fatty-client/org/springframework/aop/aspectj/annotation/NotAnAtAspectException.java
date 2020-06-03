/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import org.springframework.aop.framework.AopConfigException;
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
/*    */ public class NotAnAtAspectException
/*    */   extends AopConfigException
/*    */ {
/*    */   private final Class<?> nonAspectClass;
/*    */   
/*    */   public NotAnAtAspectException(Class<?> nonAspectClass) {
/* 40 */     super(nonAspectClass.getName() + " is not an @AspectJ aspect");
/* 41 */     this.nonAspectClass = nonAspectClass;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getNonAspectClass() {
/* 48 */     return this.nonAspectClass;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/NotAnAtAspectException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */