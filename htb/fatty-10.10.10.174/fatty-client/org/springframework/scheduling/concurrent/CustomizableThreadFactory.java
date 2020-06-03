/*    */ package org.springframework.scheduling.concurrent;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import org.springframework.util.CustomizableThreadCreator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomizableThreadFactory
/*    */   extends CustomizableThreadCreator
/*    */   implements ThreadFactory
/*    */ {
/*    */   public CustomizableThreadFactory() {}
/*    */   
/*    */   public CustomizableThreadFactory(String threadNamePrefix) {
/* 50 */     super(threadNamePrefix);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable runnable) {
/* 56 */     return createThread(runnable);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/CustomizableThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */