/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public class ClassArrayEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   @Nullable
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public ClassArrayEditor() {
/* 49 */     this((ClassLoader)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassArrayEditor(@Nullable ClassLoader classLoader) {
/* 59 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 65 */     if (StringUtils.hasText(text)) {
/* 66 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(text);
/* 67 */       Class<?>[] classes = new Class[classNames.length];
/* 68 */       for (int i = 0; i < classNames.length; i++) {
/* 69 */         String className = classNames[i].trim();
/* 70 */         classes[i] = ClassUtils.resolveClassName(className, this.classLoader);
/*    */       } 
/* 72 */       setValue(classes);
/*    */     } else {
/*    */       
/* 75 */       setValue(null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 81 */     Class<?>[] classes = (Class[])getValue();
/* 82 */     if (ObjectUtils.isEmpty((Object[])classes)) {
/* 83 */       return "";
/*    */     }
/* 85 */     StringBuilder sb = new StringBuilder();
/* 86 */     for (int i = 0; i < classes.length; i++) {
/* 87 */       if (i > 0) {
/* 88 */         sb.append(",");
/*    */       }
/* 90 */       sb.append(ClassUtils.getQualifiedName(classes[i]));
/*    */     } 
/* 92 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/ClassArrayEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */