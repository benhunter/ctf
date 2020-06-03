/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import org.springframework.cache.Cache;
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
/*    */ public class SimpleValueWrapper
/*    */   implements Cache.ValueWrapper
/*    */ {
/*    */   @Nullable
/*    */   private final Object value;
/*    */   
/*    */   public SimpleValueWrapper(@Nullable Object value) {
/* 40 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object get() {
/* 50 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/SimpleValueWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */