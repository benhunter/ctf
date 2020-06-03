/*    */ package org.springframework.validation.support;
/*    */ 
/*    */ import org.springframework.ui.ConcurrentModel;
/*    */ import org.springframework.validation.BindingResult;
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
/*    */ public class BindingAwareConcurrentModel
/*    */   extends ConcurrentModel
/*    */ {
/*    */   public Object put(String key, Object value) {
/* 45 */     removeBindingResultIfNecessary(key, value);
/* 46 */     return super.put(key, value);
/*    */   }
/*    */   
/*    */   private void removeBindingResultIfNecessary(String key, Object value) {
/* 50 */     if (!key.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 51 */       String resultKey = BindingResult.MODEL_KEY_PREFIX + key;
/* 52 */       BindingResult result = (BindingResult)get(resultKey);
/* 53 */       if (result != null && result.getTarget() != value)
/* 54 */         remove(resultKey); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/support/BindingAwareConcurrentModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */