/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
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
/*     */ abstract class SelectedValueComparator
/*     */ {
/*     */   public static boolean isSelected(BindStatus bindStatus, @Nullable Object candidateValue) {
/*  66 */     Object boundValue = bindStatus.getValue();
/*  67 */     if (ObjectUtils.nullSafeEquals(boundValue, candidateValue)) {
/*  68 */       return true;
/*     */     }
/*  70 */     Object actualValue = bindStatus.getActualValue();
/*  71 */     if (actualValue != null && actualValue != boundValue && 
/*  72 */       ObjectUtils.nullSafeEquals(actualValue, candidateValue)) {
/*  73 */       return true;
/*     */     }
/*  75 */     if (actualValue != null) {
/*  76 */       boundValue = actualValue;
/*     */     }
/*  78 */     else if (boundValue == null) {
/*  79 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  84 */     boolean selected = false;
/*  85 */     if (candidateValue != null) {
/*  86 */       if (boundValue.getClass().isArray()) {
/*  87 */         selected = collectionCompare(CollectionUtils.arrayToList(boundValue), candidateValue, bindStatus);
/*     */       }
/*  89 */       else if (boundValue instanceof Collection) {
/*  90 */         selected = collectionCompare((Collection)boundValue, candidateValue, bindStatus);
/*     */       }
/*  92 */       else if (boundValue instanceof Map) {
/*  93 */         selected = mapCompare((Map<?, ?>)boundValue, candidateValue, bindStatus);
/*     */       } 
/*     */     }
/*  96 */     if (!selected) {
/*  97 */       selected = exhaustiveCompare(boundValue, candidateValue, bindStatus.getEditor(), null);
/*     */     }
/*  99 */     return selected;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean collectionCompare(Collection<?> boundCollection, Object candidateValue, BindStatus bindStatus) {
/*     */     try {
/* 105 */       if (boundCollection.contains(candidateValue)) {
/* 106 */         return true;
/*     */       }
/*     */     }
/* 109 */     catch (ClassCastException classCastException) {}
/*     */ 
/*     */     
/* 112 */     return exhaustiveCollectionCompare(boundCollection, candidateValue, bindStatus);
/*     */   }
/*     */   
/*     */   private static boolean mapCompare(Map<?, ?> boundMap, Object candidateValue, BindStatus bindStatus) {
/*     */     try {
/* 117 */       if (boundMap.containsKey(candidateValue)) {
/* 118 */         return true;
/*     */       }
/*     */     }
/* 121 */     catch (ClassCastException classCastException) {}
/*     */ 
/*     */     
/* 124 */     return exhaustiveCollectionCompare(boundMap.keySet(), candidateValue, bindStatus);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean exhaustiveCollectionCompare(Collection<?> collection, Object candidateValue, BindStatus bindStatus) {
/* 130 */     Map<PropertyEditor, Object> convertedValueCache = new HashMap<>();
/* 131 */     PropertyEditor editor = null;
/* 132 */     boolean candidateIsString = candidateValue instanceof String;
/* 133 */     if (!candidateIsString) {
/* 134 */       editor = bindStatus.findEditor(candidateValue.getClass());
/*     */     }
/* 136 */     for (Object element : collection) {
/* 137 */       if (editor == null && element != null && candidateIsString) {
/* 138 */         editor = bindStatus.findEditor(element.getClass());
/*     */       }
/* 140 */       if (exhaustiveCompare(element, candidateValue, editor, convertedValueCache)) {
/* 141 */         return true;
/*     */       }
/*     */     } 
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean exhaustiveCompare(@Nullable Object boundValue, @Nullable Object candidate, @Nullable PropertyEditor editor, @Nullable Map<PropertyEditor, Object> convertedValueCache) {
/* 150 */     String candidateDisplayString = ValueFormatter.getDisplayString(candidate, editor, false);
/* 151 */     if (boundValue != null && boundValue.getClass().isEnum()) {
/* 152 */       Enum<?> boundEnum = (Enum)boundValue;
/* 153 */       String enumCodeAsString = ObjectUtils.getDisplayString(boundEnum.name());
/* 154 */       if (enumCodeAsString.equals(candidateDisplayString)) {
/* 155 */         return true;
/*     */       }
/* 157 */       String enumLabelAsString = ObjectUtils.getDisplayString(boundEnum.toString());
/* 158 */       if (enumLabelAsString.equals(candidateDisplayString)) {
/* 159 */         return true;
/*     */       }
/*     */     }
/* 162 */     else if (ObjectUtils.getDisplayString(boundValue).equals(candidateDisplayString)) {
/* 163 */       return true;
/*     */     } 
/*     */     
/* 166 */     if (editor != null && candidate instanceof String) {
/*     */       Object candidateAsValue;
/* 168 */       String candidateAsString = (String)candidate;
/*     */       
/* 170 */       if (convertedValueCache != null && convertedValueCache.containsKey(editor)) {
/* 171 */         candidateAsValue = convertedValueCache.get(editor);
/*     */       } else {
/*     */         
/* 174 */         editor.setAsText(candidateAsString);
/* 175 */         candidateAsValue = editor.getValue();
/* 176 */         if (convertedValueCache != null) {
/* 177 */           convertedValueCache.put(editor, candidateAsValue);
/*     */         }
/*     */       } 
/* 180 */       if (ObjectUtils.nullSafeEquals(boundValue, candidateAsValue)) {
/* 181 */         return true;
/*     */       }
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/SelectedValueComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */