/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
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
/*    */ public abstract class PropertyAccessException
/*    */   extends BeansException
/*    */ {
/*    */   @Nullable
/*    */   private final PropertyChangeEvent propertyChangeEvent;
/*    */   
/*    */   public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, @Nullable Throwable cause) {
/* 44 */     super(msg, cause);
/* 45 */     this.propertyChangeEvent = propertyChangeEvent;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyAccessException(String msg, @Nullable Throwable cause) {
/* 54 */     super(msg, cause);
/* 55 */     this.propertyChangeEvent = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public PropertyChangeEvent getPropertyChangeEvent() {
/* 66 */     return this.propertyChangeEvent;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getPropertyName() {
/* 74 */     return (this.propertyChangeEvent != null) ? this.propertyChangeEvent.getPropertyName() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getValue() {
/* 82 */     return (this.propertyChangeEvent != null) ? this.propertyChangeEvent.getNewValue() : null;
/*    */   }
/*    */   
/*    */   public abstract String getErrorCode();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyAccessException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */