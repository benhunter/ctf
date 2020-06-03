/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import org.springframework.context.annotation.AdviceMode;
/*    */ import org.springframework.context.annotation.AdviceModeImportSelector;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncConfigurationSelector
/*    */   extends AdviceModeImportSelector<EnableAsync>
/*    */ {
/*    */   private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";
/*    */   
/*    */   @Nullable
/*    */   public String[] selectImports(AdviceMode adviceMode) {
/* 48 */     switch (adviceMode) {
/*    */       case PROXY:
/* 50 */         return new String[] { ProxyAsyncConfiguration.class.getName() };
/*    */       case ASPECTJ:
/* 52 */         return new String[] { "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration" };
/*    */     } 
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/AsyncConfigurationSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */