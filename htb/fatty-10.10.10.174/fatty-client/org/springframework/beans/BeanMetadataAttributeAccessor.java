/*    */ package org.springframework.beans;
/*    */ 
/*    */ import org.springframework.core.AttributeAccessorSupport;
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
/*    */ public class BeanMetadataAttributeAccessor
/*    */   extends AttributeAccessorSupport
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   @Nullable
/*    */   private Object source;
/*    */   
/*    */   public void setSource(@Nullable Object source) {
/* 42 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getSource() {
/* 48 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addMetadataAttribute(BeanMetadataAttribute attribute) {
/* 57 */     super.setAttribute(attribute.getName(), attribute);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BeanMetadataAttribute getMetadataAttribute(String name) {
/* 68 */     return (BeanMetadataAttribute)super.getAttribute(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setAttribute(String name, @Nullable Object value) {
/* 73 */     super.setAttribute(name, new BeanMetadataAttribute(name, value));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getAttribute(String name) {
/* 79 */     BeanMetadataAttribute attribute = (BeanMetadataAttribute)super.getAttribute(name);
/* 80 */     return (attribute != null) ? attribute.getValue() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object removeAttribute(String name) {
/* 86 */     BeanMetadataAttribute attribute = (BeanMetadataAttribute)super.removeAttribute(name);
/* 87 */     return (attribute != null) ? attribute.getValue() : null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/BeanMetadataAttributeAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */