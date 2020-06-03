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
/*    */ 
/*    */ public abstract class StaticMethodMatcherPointcut
/*    */   extends StaticMethodMatcher
/*    */   implements Pointcut
/*    */ {
/* 35 */   private ClassFilter classFilter = ClassFilter.TRUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassFilter(ClassFilter classFilter) {
/* 43 */     this.classFilter = classFilter;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassFilter getClassFilter() {
/* 48 */     return this.classFilter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final MethodMatcher getMethodMatcher() {
/* 54 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/StaticMethodMatcherPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */