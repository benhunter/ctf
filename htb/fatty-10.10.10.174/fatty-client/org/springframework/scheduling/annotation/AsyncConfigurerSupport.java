/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class AsyncConfigurerSupport
/*    */   implements AsyncConfigurer
/*    */ {
/*    */   public Executor getAsyncExecutor() {
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
/* 42 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/AsyncConfigurerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */