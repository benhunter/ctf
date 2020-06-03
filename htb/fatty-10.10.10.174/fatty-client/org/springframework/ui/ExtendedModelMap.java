/*    */ package org.springframework.ui;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
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
/*    */ public class ExtendedModelMap
/*    */   extends ModelMap
/*    */   implements Model
/*    */ {
/*    */   public ExtendedModelMap addAttribute(String attributeName, @Nullable Object attributeValue) {
/* 41 */     super.addAttribute(attributeName, attributeValue);
/* 42 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExtendedModelMap addAttribute(Object attributeValue) {
/* 47 */     super.addAttribute(attributeValue);
/* 48 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExtendedModelMap addAllAttributes(@Nullable Collection<?> attributeValues) {
/* 53 */     super.addAllAttributes(attributeValues);
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExtendedModelMap addAllAttributes(@Nullable Map<String, ?> attributes) {
/* 59 */     super.addAllAttributes(attributes);
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExtendedModelMap mergeAttributes(@Nullable Map<String, ?> attributes) {
/* 65 */     super.mergeAttributes(attributes);
/* 66 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Object> asMap() {
/* 71 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/ExtendedModelMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */