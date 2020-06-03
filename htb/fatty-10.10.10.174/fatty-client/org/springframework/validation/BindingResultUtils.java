/*    */ package org.springframework.validation;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public abstract class BindingResultUtils
/*    */ {
/*    */   @Nullable
/*    */   public static BindingResult getBindingResult(Map<?, ?> model, String name) {
/* 42 */     Assert.notNull(model, "Model map must not be null");
/* 43 */     Assert.notNull(name, "Name must not be null");
/* 44 */     Object attr = model.get(BindingResult.MODEL_KEY_PREFIX + name);
/* 45 */     if (attr != null && !(attr instanceof BindingResult)) {
/* 46 */       throw new IllegalStateException("BindingResult attribute is not of type BindingResult: " + attr);
/*    */     }
/* 48 */     return (BindingResult)attr;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BindingResult getRequiredBindingResult(Map<?, ?> model, String name) {
/* 59 */     BindingResult bindingResult = getBindingResult(model, name);
/* 60 */     if (bindingResult == null) {
/* 61 */       throw new IllegalStateException("No BindingResult attribute found for name '" + name + "'- have you exposed the correct model?");
/*    */     }
/*    */     
/* 64 */     return bindingResult;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/BindingResultUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */