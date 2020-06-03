/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Arrays;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class SimpleKey
/*    */   implements Serializable
/*    */ {
/* 36 */   public static final SimpleKey EMPTY = new SimpleKey(new Object[0]);
/*    */ 
/*    */ 
/*    */   
/*    */   private final Object[] params;
/*    */ 
/*    */ 
/*    */   
/*    */   private final int hashCode;
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleKey(Object... elements) {
/* 49 */     Assert.notNull(elements, "Elements must not be null");
/* 50 */     this.params = new Object[elements.length];
/* 51 */     System.arraycopy(elements, 0, this.params, 0, elements.length);
/* 52 */     this.hashCode = Arrays.deepHashCode(this.params);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 58 */     return (this == other || (other instanceof SimpleKey && 
/* 59 */       Arrays.deepEquals(this.params, ((SimpleKey)other).params)));
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 64 */     return this.hashCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/SimpleKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */