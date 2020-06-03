/*    */ package org.springframework.validation.support;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.ui.ExtendedModelMap;
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
/*    */ public class BindingAwareModelMap
/*    */   extends ExtendedModelMap
/*    */ {
/*    */   public Object put(String key, Object value) {
/* 43 */     removeBindingResultIfNecessary(key, value);
/* 44 */     return super.put(key, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void putAll(Map<? extends String, ?> map) {
/* 49 */     map.forEach(this::removeBindingResultIfNecessary);
/* 50 */     super.putAll(map);
/*    */   }
/*    */   
/*    */   private void removeBindingResultIfNecessary(Object key, Object value) {
/* 54 */     if (key instanceof String) {
/* 55 */       String attributeName = (String)key;
/* 56 */       if (!attributeName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 57 */         String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attributeName;
/* 58 */         BindingResult bindingResult = (BindingResult)get(bindingResultKey);
/* 59 */         if (bindingResult != null && bindingResult.getTarget() != value)
/* 60 */           remove(bindingResultKey); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/support/BindingAwareModelMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */