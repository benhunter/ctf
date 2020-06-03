/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public class TimeoutDeferredResultProcessingInterceptor
/*    */   implements DeferredResultProcessingInterceptor
/*    */ {
/*    */   public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> result) throws Exception {
/* 42 */     result.setErrorResult(new AsyncRequestTimeoutException());
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/TimeoutDeferredResultProcessingInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */