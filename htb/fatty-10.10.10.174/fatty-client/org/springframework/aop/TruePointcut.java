/*    */ package org.springframework.aop;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ final class TruePointcut
/*    */   implements Pointcut, Serializable
/*    */ {
/* 29 */   public static final TruePointcut INSTANCE = new TruePointcut();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassFilter getClassFilter() {
/* 39 */     return ClassFilter.TRUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodMatcher getMethodMatcher() {
/* 44 */     return MethodMatcher.TRUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 53 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "Pointcut.TRUE";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/TruePointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */