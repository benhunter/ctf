/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import org.springframework.aop.ClassFilter;
/*    */ import org.springframework.aop.MethodMatcher;
/*    */ import org.springframework.aop.Pointcut;
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
/*    */ public abstract class DynamicMethodMatcherPointcut
/*    */   extends DynamicMethodMatcher
/*    */   implements Pointcut
/*    */ {
/*    */   public ClassFilter getClassFilter() {
/* 35 */     return ClassFilter.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public final MethodMatcher getMethodMatcher() {
/* 40 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/DynamicMethodMatcherPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */