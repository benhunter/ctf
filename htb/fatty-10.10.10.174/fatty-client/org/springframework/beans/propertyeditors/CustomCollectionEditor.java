/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class CustomCollectionEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final Class<? extends Collection> collectionType;
/*     */   private final boolean nullAsEmptyCollection;
/*     */   
/*     */   public CustomCollectionEditor(Class<? extends Collection> collectionType) {
/*  67 */     this(collectionType, false);
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
/*     */   public CustomCollectionEditor(Class<? extends Collection> collectionType, boolean nullAsEmptyCollection) {
/*  90 */     Assert.notNull(collectionType, "Collection type is required");
/*  91 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/*  92 */       throw new IllegalArgumentException("Collection type [" + collectionType
/*  93 */           .getName() + "] does not implement [java.util.Collection]");
/*     */     }
/*  95 */     this.collectionType = collectionType;
/*  96 */     this.nullAsEmptyCollection = nullAsEmptyCollection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/* 105 */     setValue(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable Object value) {
/* 113 */     if (value == null && this.nullAsEmptyCollection) {
/* 114 */       super.setValue(createCollection(this.collectionType, 0));
/*     */     }
/* 116 */     else if (value == null || (this.collectionType.isInstance(value) && !alwaysCreateNewCollection())) {
/*     */       
/* 118 */       super.setValue(value);
/*     */     }
/* 120 */     else if (value instanceof Collection) {
/*     */       
/* 122 */       Collection<?> source = (Collection)value;
/* 123 */       Collection<Object> target = createCollection(this.collectionType, source.size());
/* 124 */       for (Object elem : source) {
/* 125 */         target.add(convertElement(elem));
/*     */       }
/* 127 */       super.setValue(target);
/*     */     }
/* 129 */     else if (value.getClass().isArray()) {
/*     */       
/* 131 */       int length = Array.getLength(value);
/* 132 */       Collection<Object> target = createCollection(this.collectionType, length);
/* 133 */       for (int i = 0; i < length; i++) {
/* 134 */         target.add(convertElement(Array.get(value, i)));
/*     */       }
/* 136 */       super.setValue(target);
/*     */     }
/*     */     else {
/*     */       
/* 140 */       Collection<Object> target = createCollection(this.collectionType, 1);
/* 141 */       target.add(convertElement(value));
/* 142 */       super.setValue(target);
/*     */     } 
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
/*     */   protected Collection<Object> createCollection(Class<? extends Collection> collectionType, int initialCapacity) {
/* 155 */     if (!collectionType.isInterface()) {
/*     */       try {
/* 157 */         return ReflectionUtils.accessibleConstructor(collectionType, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 159 */       catch (Throwable ex) {
/* 160 */         throw new IllegalArgumentException("Could not instantiate collection class: " + collectionType
/* 161 */             .getName(), ex);
/*     */       } 
/*     */     }
/* 164 */     if (List.class == collectionType) {
/* 165 */       return new ArrayList(initialCapacity);
/*     */     }
/* 167 */     if (SortedSet.class == collectionType) {
/* 168 */       return new TreeSet();
/*     */     }
/*     */     
/* 171 */     return new LinkedHashSet(initialCapacity);
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
/*     */   protected boolean alwaysCreateNewCollection() {
/* 183 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object convertElement(Object element) {
/* 201 */     return element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAsText() {
/* 212 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/CustomCollectionEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */