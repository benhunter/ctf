/*     */ package org.springframework.core.style;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ public class DefaultValueStyler
/*     */   implements ValueStyler
/*     */ {
/*     */   private static final String EMPTY = "[empty]";
/*     */   private static final String NULL = "[null]";
/*     */   private static final String COLLECTION = "collection";
/*     */   private static final String SET = "set";
/*     */   private static final String LIST = "list";
/*     */   private static final String MAP = "map";
/*     */   private static final String ARRAY = "array";
/*     */   
/*     */   public String style(@Nullable Object value) {
/*  54 */     if (value == null) {
/*  55 */       return "[null]";
/*     */     }
/*  57 */     if (value instanceof String) {
/*  58 */       return "'" + value + "'";
/*     */     }
/*  60 */     if (value instanceof Class) {
/*  61 */       return ClassUtils.getShortName((Class)value);
/*     */     }
/*  63 */     if (value instanceof Method) {
/*  64 */       Method method = (Method)value;
/*  65 */       return method.getName() + "@" + ClassUtils.getShortName(method.getDeclaringClass());
/*     */     } 
/*  67 */     if (value instanceof Map) {
/*  68 */       return style((Map<?, ?>)value);
/*     */     }
/*  70 */     if (value instanceof Map.Entry) {
/*  71 */       return style((Map.Entry<?, ?>)value);
/*     */     }
/*  73 */     if (value instanceof Collection) {
/*  74 */       return style((Collection)value);
/*     */     }
/*  76 */     if (value.getClass().isArray()) {
/*  77 */       return styleArray(ObjectUtils.toObjectArray(value));
/*     */     }
/*     */     
/*  80 */     return String.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   private <K, V> String style(Map<K, V> value) {
/*  85 */     StringBuilder result = new StringBuilder(value.size() * 8 + 16);
/*  86 */     result.append("map[");
/*  87 */     for (Iterator<Map.Entry<K, V>> it = value.entrySet().iterator(); it.hasNext(); ) {
/*  88 */       Map.Entry<K, V> entry = it.next();
/*  89 */       result.append(style(entry));
/*  90 */       if (it.hasNext()) {
/*  91 */         result.append(',').append(' ');
/*     */       }
/*     */     } 
/*  94 */     if (value.isEmpty()) {
/*  95 */       result.append("[empty]");
/*     */     }
/*  97 */     result.append("]");
/*  98 */     return result.toString();
/*     */   }
/*     */   
/*     */   private String style(Map.Entry<?, ?> value) {
/* 102 */     return style(value.getKey()) + " -> " + style(value.getValue());
/*     */   }
/*     */   
/*     */   private String style(Collection<?> value) {
/* 106 */     StringBuilder result = new StringBuilder(value.size() * 8 + 16);
/* 107 */     result.append(getCollectionTypeString(value)).append('[');
/* 108 */     for (Iterator<?> i = value.iterator(); i.hasNext(); ) {
/* 109 */       result.append(style(i.next()));
/* 110 */       if (i.hasNext()) {
/* 111 */         result.append(',').append(' ');
/*     */       }
/*     */     } 
/* 114 */     if (value.isEmpty()) {
/* 115 */       result.append("[empty]");
/*     */     }
/* 117 */     result.append("]");
/* 118 */     return result.toString();
/*     */   }
/*     */   
/*     */   private String getCollectionTypeString(Collection<?> value) {
/* 122 */     if (value instanceof java.util.List) {
/* 123 */       return "list";
/*     */     }
/* 125 */     if (value instanceof java.util.Set) {
/* 126 */       return "set";
/*     */     }
/*     */     
/* 129 */     return "collection";
/*     */   }
/*     */ 
/*     */   
/*     */   private String styleArray(Object[] array) {
/* 134 */     StringBuilder result = new StringBuilder(array.length * 8 + 16);
/* 135 */     result.append("array<").append(ClassUtils.getShortName(array.getClass().getComponentType())).append(">[");
/* 136 */     for (int i = 0; i < array.length - 1; i++) {
/* 137 */       result.append(style(array[i]));
/* 138 */       result.append(',').append(' ');
/*     */     } 
/* 140 */     if (array.length > 0) {
/* 141 */       result.append(style(array[array.length - 1]));
/*     */     } else {
/*     */       
/* 144 */       result.append("[empty]");
/*     */     } 
/* 146 */     result.append("]");
/* 147 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/style/DefaultValueStyler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */