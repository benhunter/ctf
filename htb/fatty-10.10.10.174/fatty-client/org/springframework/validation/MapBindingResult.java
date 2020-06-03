/*    */ package org.springframework.validation;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapBindingResult
/*    */   extends AbstractBindingResult
/*    */   implements Serializable
/*    */ {
/*    */   private final Map<?, ?> target;
/*    */   
/*    */   public MapBindingResult(Map<?, ?> target, String objectName) {
/* 49 */     super(objectName);
/* 50 */     Assert.notNull(target, "Target Map must not be null");
/* 51 */     this.target = target;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Map<?, ?> getTargetMap() {
/* 56 */     return this.target;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Object getTarget() {
/* 61 */     return this.target;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object getActualFieldValue(String field) {
/* 67 */     return this.target.get(field);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/MapBindingResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */