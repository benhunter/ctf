/*    */ package org.springframework.beans.factory.support;
/*    */ 
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
/*    */ public class ManagedArray
/*    */   extends ManagedList<Object>
/*    */ {
/*    */   @Nullable
/*    */   volatile Class<?> resolvedElementType;
/*    */   
/*    */   public ManagedArray(String elementTypeName, int size) {
/* 43 */     super(size);
/* 44 */     Assert.notNull(elementTypeName, "elementTypeName must not be null");
/* 45 */     setElementTypeName(elementTypeName);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ManagedArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */