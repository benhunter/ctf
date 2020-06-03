/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ public class BeanDefinitionVisitor
/*     */ {
/*     */   @Nullable
/*     */   private StringValueResolver valueResolver;
/*     */   
/*     */   public BeanDefinitionVisitor(StringValueResolver valueResolver) {
/*  60 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  61 */     this.valueResolver = valueResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanDefinitionVisitor() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitBeanDefinition(BeanDefinition beanDefinition) {
/*  79 */     visitParentName(beanDefinition);
/*  80 */     visitBeanClassName(beanDefinition);
/*  81 */     visitFactoryBeanName(beanDefinition);
/*  82 */     visitFactoryMethodName(beanDefinition);
/*  83 */     visitScope(beanDefinition);
/*  84 */     if (beanDefinition.hasPropertyValues()) {
/*  85 */       visitPropertyValues(beanDefinition.getPropertyValues());
/*     */     }
/*  87 */     if (beanDefinition.hasConstructorArgumentValues()) {
/*  88 */       ConstructorArgumentValues cas = beanDefinition.getConstructorArgumentValues();
/*  89 */       visitIndexedArgumentValues(cas.getIndexedArgumentValues());
/*  90 */       visitGenericArgumentValues(cas.getGenericArgumentValues());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitParentName(BeanDefinition beanDefinition) {
/*  95 */     String parentName = beanDefinition.getParentName();
/*  96 */     if (parentName != null) {
/*  97 */       String resolvedName = resolveStringValue(parentName);
/*  98 */       if (!parentName.equals(resolvedName)) {
/*  99 */         beanDefinition.setParentName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitBeanClassName(BeanDefinition beanDefinition) {
/* 105 */     String beanClassName = beanDefinition.getBeanClassName();
/* 106 */     if (beanClassName != null) {
/* 107 */       String resolvedName = resolveStringValue(beanClassName);
/* 108 */       if (!beanClassName.equals(resolvedName)) {
/* 109 */         beanDefinition.setBeanClassName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitFactoryBeanName(BeanDefinition beanDefinition) {
/* 115 */     String factoryBeanName = beanDefinition.getFactoryBeanName();
/* 116 */     if (factoryBeanName != null) {
/* 117 */       String resolvedName = resolveStringValue(factoryBeanName);
/* 118 */       if (!factoryBeanName.equals(resolvedName)) {
/* 119 */         beanDefinition.setFactoryBeanName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitFactoryMethodName(BeanDefinition beanDefinition) {
/* 125 */     String factoryMethodName = beanDefinition.getFactoryMethodName();
/* 126 */     if (factoryMethodName != null) {
/* 127 */       String resolvedName = resolveStringValue(factoryMethodName);
/* 128 */       if (!factoryMethodName.equals(resolvedName)) {
/* 129 */         beanDefinition.setFactoryMethodName(resolvedName);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitScope(BeanDefinition beanDefinition) {
/* 135 */     String scope = beanDefinition.getScope();
/* 136 */     if (scope != null) {
/* 137 */       String resolvedScope = resolveStringValue(scope);
/* 138 */       if (!scope.equals(resolvedScope)) {
/* 139 */         beanDefinition.setScope(resolvedScope);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitPropertyValues(MutablePropertyValues pvs) {
/* 145 */     PropertyValue[] pvArray = pvs.getPropertyValues();
/* 146 */     for (PropertyValue pv : pvArray) {
/* 147 */       Object newVal = resolveValue(pv.getValue());
/* 148 */       if (!ObjectUtils.nullSafeEquals(newVal, pv.getValue())) {
/* 149 */         pvs.add(pv.getName(), newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitIndexedArgumentValues(Map<Integer, ConstructorArgumentValues.ValueHolder> ias) {
/* 155 */     for (ConstructorArgumentValues.ValueHolder valueHolder : ias.values()) {
/* 156 */       Object newVal = resolveValue(valueHolder.getValue());
/* 157 */       if (!ObjectUtils.nullSafeEquals(newVal, valueHolder.getValue())) {
/* 158 */         valueHolder.setValue(newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void visitGenericArgumentValues(List<ConstructorArgumentValues.ValueHolder> gas) {
/* 164 */     for (ConstructorArgumentValues.ValueHolder valueHolder : gas) {
/* 165 */       Object newVal = resolveValue(valueHolder.getValue());
/* 166 */       if (!ObjectUtils.nullSafeEquals(newVal, valueHolder.getValue())) {
/* 167 */         valueHolder.setValue(newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object resolveValue(@Nullable Object value) {
/* 175 */     if (value instanceof BeanDefinition) {
/* 176 */       visitBeanDefinition((BeanDefinition)value);
/*     */     }
/* 178 */     else if (value instanceof BeanDefinitionHolder) {
/* 179 */       visitBeanDefinition(((BeanDefinitionHolder)value).getBeanDefinition());
/*     */     }
/* 181 */     else if (value instanceof RuntimeBeanReference) {
/* 182 */       RuntimeBeanReference ref = (RuntimeBeanReference)value;
/* 183 */       String newBeanName = resolveStringValue(ref.getBeanName());
/* 184 */       if (newBeanName == null) {
/* 185 */         return null;
/*     */       }
/* 187 */       if (!newBeanName.equals(ref.getBeanName())) {
/* 188 */         return new RuntimeBeanReference(newBeanName);
/*     */       }
/*     */     }
/* 191 */     else if (value instanceof RuntimeBeanNameReference) {
/* 192 */       RuntimeBeanNameReference ref = (RuntimeBeanNameReference)value;
/* 193 */       String newBeanName = resolveStringValue(ref.getBeanName());
/* 194 */       if (newBeanName == null) {
/* 195 */         return null;
/*     */       }
/* 197 */       if (!newBeanName.equals(ref.getBeanName())) {
/* 198 */         return new RuntimeBeanNameReference(newBeanName);
/*     */       }
/*     */     }
/* 201 */     else if (value instanceof Object[]) {
/* 202 */       visitArray((Object[])value);
/*     */     }
/* 204 */     else if (value instanceof List) {
/* 205 */       visitList((List)value);
/*     */     }
/* 207 */     else if (value instanceof Set) {
/* 208 */       visitSet((Set)value);
/*     */     }
/* 210 */     else if (value instanceof Map) {
/* 211 */       visitMap((Map<?, ?>)value);
/*     */     }
/* 213 */     else if (value instanceof TypedStringValue) {
/* 214 */       TypedStringValue typedStringValue = (TypedStringValue)value;
/* 215 */       String stringValue = typedStringValue.getValue();
/* 216 */       if (stringValue != null) {
/* 217 */         String visitedString = resolveStringValue(stringValue);
/* 218 */         typedStringValue.setValue(visitedString);
/*     */       }
/*     */     
/* 221 */     } else if (value instanceof String) {
/* 222 */       return resolveStringValue((String)value);
/*     */     } 
/* 224 */     return value;
/*     */   }
/*     */   
/*     */   protected void visitArray(Object[] arrayVal) {
/* 228 */     for (int i = 0; i < arrayVal.length; i++) {
/* 229 */       Object elem = arrayVal[i];
/* 230 */       Object newVal = resolveValue(elem);
/* 231 */       if (!ObjectUtils.nullSafeEquals(newVal, elem)) {
/* 232 */         arrayVal[i] = newVal;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitList(List<Object> listVal) {
/* 239 */     for (int i = 0; i < listVal.size(); i++) {
/* 240 */       Object elem = listVal.get(i);
/* 241 */       Object newVal = resolveValue(elem);
/* 242 */       if (!ObjectUtils.nullSafeEquals(newVal, elem)) {
/* 243 */         listVal.set(i, newVal);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitSet(Set<Object> setVal) {
/* 250 */     Set<Object> newContent = new LinkedHashSet();
/* 251 */     boolean entriesModified = false;
/* 252 */     for (Object elem : setVal) {
/* 253 */       int elemHash = (elem != null) ? elem.hashCode() : 0;
/* 254 */       Object newVal = resolveValue(elem);
/* 255 */       int newValHash = (newVal != null) ? newVal.hashCode() : 0;
/* 256 */       newContent.add(newVal);
/* 257 */       entriesModified = (entriesModified || newVal != elem || newValHash != elemHash);
/*     */     } 
/* 259 */     if (entriesModified) {
/* 260 */       setVal.clear();
/* 261 */       setVal.addAll(newContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitMap(Map<?, ?> mapVal) {
/* 267 */     Map<Object, Object> newContent = new LinkedHashMap<>();
/* 268 */     boolean entriesModified = false;
/* 269 */     for (Map.Entry<?, ?> entry : mapVal.entrySet()) {
/* 270 */       Object key = entry.getKey();
/* 271 */       int keyHash = (key != null) ? key.hashCode() : 0;
/* 272 */       Object newKey = resolveValue(key);
/* 273 */       int newKeyHash = (newKey != null) ? newKey.hashCode() : 0;
/* 274 */       Object val = entry.getValue();
/* 275 */       Object newVal = resolveValue(val);
/* 276 */       newContent.put(newKey, newVal);
/* 277 */       entriesModified = (entriesModified || newVal != val || newKey != key || newKeyHash != keyHash);
/*     */     } 
/* 279 */     if (entriesModified) {
/* 280 */       mapVal.clear();
/* 281 */       mapVal.putAll(newContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String resolveStringValue(String strVal) {
/* 292 */     if (this.valueResolver == null) {
/* 293 */       throw new IllegalStateException("No StringValueResolver specified - pass a resolver object into the constructor or override the 'resolveStringValue' method");
/*     */     }
/*     */     
/* 296 */     String resolvedValue = this.valueResolver.resolveStringValue(strVal);
/*     */     
/* 298 */     return strVal.equals(resolvedValue) ? strVal : resolvedValue;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/BeanDefinitionVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */