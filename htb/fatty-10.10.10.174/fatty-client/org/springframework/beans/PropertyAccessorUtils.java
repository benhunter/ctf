/*     */ package org.springframework.beans;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertyAccessorUtils
/*     */ {
/*     */   public static String getPropertyName(String propertyPath) {
/*  38 */     int separatorIndex = propertyPath.endsWith("]") ? propertyPath.indexOf('[') : -1;
/*  39 */     return (separatorIndex != -1) ? propertyPath.substring(0, separatorIndex) : propertyPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNestedOrIndexedProperty(@Nullable String propertyPath) {
/*  48 */     if (propertyPath == null) {
/*  49 */       return false;
/*     */     }
/*  51 */     for (int i = 0; i < propertyPath.length(); i++) {
/*  52 */       char ch = propertyPath.charAt(i);
/*  53 */       if (ch == '.' || ch == '[')
/*     */       {
/*  55 */         return true;
/*     */       }
/*     */     } 
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getFirstNestedPropertySeparatorIndex(String propertyPath) {
/*  68 */     return getNestedPropertySeparatorIndex(propertyPath, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getLastNestedPropertySeparatorIndex(String propertyPath) {
/*  78 */     return getNestedPropertySeparatorIndex(propertyPath, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getNestedPropertySeparatorIndex(String propertyPath, boolean last) {
/*  89 */     boolean inKey = false;
/*  90 */     int length = propertyPath.length();
/*  91 */     int i = last ? (length - 1) : 0;
/*  92 */     while (last ? (i >= 0) : (i < length)) {
/*  93 */       switch (propertyPath.charAt(i)) {
/*     */         case '[':
/*     */         case ']':
/*  96 */           inKey = !inKey;
/*     */           break;
/*     */         case '.':
/*  99 */           if (!inKey)
/* 100 */             return i; 
/*     */           break;
/*     */       } 
/* 103 */       if (last) {
/* 104 */         i--;
/*     */         continue;
/*     */       } 
/* 107 */       i++;
/*     */     } 
/*     */     
/* 110 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchesProperty(String registeredPath, String propertyPath) {
/* 121 */     if (!registeredPath.startsWith(propertyPath)) {
/* 122 */       return false;
/*     */     }
/* 124 */     if (registeredPath.length() == propertyPath.length()) {
/* 125 */       return true;
/*     */     }
/* 127 */     if (registeredPath.charAt(propertyPath.length()) != '[') {
/* 128 */       return false;
/*     */     }
/* 130 */     return 
/* 131 */       (registeredPath.indexOf(']', propertyPath.length() + 1) == registeredPath.length() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String canonicalPropertyName(@Nullable String propertyName) {
/* 143 */     if (propertyName == null) {
/* 144 */       return "";
/*     */     }
/*     */     
/* 147 */     StringBuilder sb = new StringBuilder(propertyName);
/* 148 */     int searchIndex = 0;
/* 149 */     while (searchIndex != -1) {
/* 150 */       int keyStart = sb.indexOf("[", searchIndex);
/* 151 */       searchIndex = -1;
/* 152 */       if (keyStart != -1) {
/* 153 */         int keyEnd = sb.indexOf("]", keyStart + "["
/* 154 */             .length());
/* 155 */         if (keyEnd != -1) {
/* 156 */           String key = sb.substring(keyStart + "[".length(), keyEnd);
/* 157 */           if ((key.startsWith("'") && key.endsWith("'")) || (key.startsWith("\"") && key.endsWith("\""))) {
/* 158 */             sb.delete(keyStart + 1, keyStart + 2);
/* 159 */             sb.delete(keyEnd - 2, keyEnd - 1);
/* 160 */             keyEnd -= 2;
/*     */           } 
/* 162 */           searchIndex = keyEnd + "]".length();
/*     */         } 
/*     */       } 
/*     */     } 
/* 166 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String[] canonicalPropertyNames(@Nullable String[] propertyNames) {
/* 178 */     if (propertyNames == null) {
/* 179 */       return null;
/*     */     }
/* 181 */     String[] result = new String[propertyNames.length];
/* 182 */     for (int i = 0; i < propertyNames.length; i++) {
/* 183 */       result[i] = canonicalPropertyName(propertyNames[i]);
/*     */     }
/* 185 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyAccessorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */