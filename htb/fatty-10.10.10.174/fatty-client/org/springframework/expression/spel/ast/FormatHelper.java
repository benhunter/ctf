/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ abstract class FormatHelper
/*    */ {
/*    */   public static String formatMethodForMessage(String name, List<TypeDescriptor> argumentTypes) {
/* 39 */     StringBuilder sb = new StringBuilder(name);
/* 40 */     sb.append("(");
/* 41 */     for (int i = 0; i < argumentTypes.size(); i++) {
/* 42 */       if (i > 0) {
/* 43 */         sb.append(",");
/*    */       }
/* 45 */       TypeDescriptor typeDescriptor = argumentTypes.get(i);
/* 46 */       if (typeDescriptor != null) {
/* 47 */         sb.append(formatClassNameForMessage(typeDescriptor.getType()));
/*    */       } else {
/*    */         
/* 50 */         sb.append(formatClassNameForMessage(null));
/*    */       } 
/*    */     } 
/* 53 */     sb.append(")");
/* 54 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String formatClassNameForMessage(@Nullable Class<?> clazz) {
/* 65 */     return (clazz != null) ? ClassUtils.getQualifiedName(clazz) : "null";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/ast/FormatHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */