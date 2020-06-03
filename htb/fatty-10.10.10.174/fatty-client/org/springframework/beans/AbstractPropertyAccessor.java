/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class AbstractPropertyAccessor
/*     */   extends TypeConverterSupport
/*     */   implements ConfigurablePropertyAccessor
/*     */ {
/*     */   private boolean extractOldValueForEditor = false;
/*     */   private boolean autoGrowNestedPaths = false;
/*     */   
/*     */   public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
/*  46 */     this.extractOldValueForEditor = extractOldValueForEditor;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExtractOldValueForEditor() {
/*  51 */     return this.extractOldValueForEditor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
/*  56 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNestedPaths() {
/*  61 */     return this.autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyValue(PropertyValue pv) throws BeansException {
/*  67 */     setPropertyValue(pv.getName(), pv.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyValues(Map<?, ?> map) throws BeansException {
/*  72 */     setPropertyValues(new MutablePropertyValues(map));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyValues(PropertyValues pvs) throws BeansException {
/*  77 */     setPropertyValues(pvs, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {
/*  82 */     setPropertyValues(pvs, ignoreUnknown, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid) throws BeansException {
/*  89 */     List<PropertyAccessException> propertyAccessExceptions = null;
/*     */     
/*  91 */     List<PropertyValue> propertyValues = (pvs instanceof MutablePropertyValues) ? ((MutablePropertyValues)pvs).getPropertyValueList() : Arrays.<PropertyValue>asList(pvs.getPropertyValues());
/*  92 */     for (PropertyValue pv : propertyValues) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  97 */         setPropertyValue(pv);
/*     */       }
/*  99 */       catch (NotWritablePropertyException ex) {
/* 100 */         if (!ignoreUnknown) {
/* 101 */           throw ex;
/*     */         
/*     */         }
/*     */       }
/* 105 */       catch (NullValueInNestedPathException ex) {
/* 106 */         if (!ignoreInvalid) {
/* 107 */           throw ex;
/*     */         
/*     */         }
/*     */       }
/* 111 */       catch (PropertyAccessException ex) {
/* 112 */         if (propertyAccessExceptions == null) {
/* 113 */           propertyAccessExceptions = new ArrayList<>();
/*     */         }
/* 115 */         propertyAccessExceptions.add(ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 120 */     if (propertyAccessExceptions != null) {
/* 121 */       PropertyAccessException[] paeArray = propertyAccessExceptions.<PropertyAccessException>toArray(new PropertyAccessException[0]);
/* 122 */       throw new PropertyBatchUpdateException(paeArray);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getPropertyType(String propertyPath) {
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public abstract Object getPropertyValue(String paramString) throws BeansException;
/*     */   
/*     */   public abstract void setPropertyValue(String paramString, @Nullable Object paramObject) throws BeansException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/AbstractPropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */