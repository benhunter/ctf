/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   @Nullable
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public ClassEditor() {
/* 49 */     this((ClassLoader)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassEditor(@Nullable ClassLoader classLoader) {
/* 58 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 64 */     if (StringUtils.hasText(text)) {
/* 65 */       setValue(ClassUtils.resolveClassName(text.trim(), this.classLoader));
/*    */     } else {
/*    */       
/* 68 */       setValue(null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 74 */     Class<?> clazz = (Class)getValue();
/* 75 */     if (clazz != null) {
/* 76 */       return ClassUtils.getQualifiedName(clazz);
/*    */     }
/*    */     
/* 79 */     return "";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/ClassEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */