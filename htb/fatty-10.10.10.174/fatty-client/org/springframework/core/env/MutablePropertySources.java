/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.stream.Stream;
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
/*     */ public class MutablePropertySources
/*     */   implements PropertySources
/*     */ {
/*  44 */   private final List<PropertySource<?>> propertySourceList = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertySources() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertySources(PropertySources propertySources) {
/*  58 */     this();
/*  59 */     for (PropertySource<?> propertySource : (Iterable<PropertySource<?>>)propertySources) {
/*  60 */       addLast(propertySource);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<PropertySource<?>> iterator() {
/*  67 */     return this.propertySourceList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<PropertySource<?>> spliterator() {
/*  72 */     return Spliterators.spliterator(this.propertySourceList, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<PropertySource<?>> stream() {
/*  77 */     return this.propertySourceList.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String name) {
/*  82 */     return this.propertySourceList.contains(PropertySource.named(name));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertySource<?> get(String name) {
/*  88 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/*  89 */     return (index != -1) ? this.propertySourceList.get(index) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(PropertySource<?> propertySource) {
/*  97 */     removeIfPresent(propertySource);
/*  98 */     this.propertySourceList.add(0, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLast(PropertySource<?> propertySource) {
/* 105 */     removeIfPresent(propertySource);
/* 106 */     this.propertySourceList.add(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 114 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/* 115 */     removeIfPresent(propertySource);
/* 116 */     int index = assertPresentAndGetIndex(relativePropertySourceName);
/* 117 */     addAtIndex(index, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 125 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/* 126 */     removeIfPresent(propertySource);
/* 127 */     int index = assertPresentAndGetIndex(relativePropertySourceName);
/* 128 */     addAtIndex(index + 1, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int precedenceOf(PropertySource<?> propertySource) {
/* 135 */     return this.propertySourceList.indexOf(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertySource<?> remove(String name) {
/* 144 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/* 145 */     return (index != -1) ? this.propertySourceList.remove(index) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(String name, PropertySource<?> propertySource) {
/* 156 */     int index = assertPresentAndGetIndex(name);
/* 157 */     this.propertySourceList.set(index, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 164 */     return this.propertySourceList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 169 */     return this.propertySourceList.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
/* 176 */     String newPropertySourceName = propertySource.getName();
/* 177 */     if (relativePropertySourceName.equals(newPropertySourceName)) {
/* 178 */       throw new IllegalArgumentException("PropertySource named '" + newPropertySourceName + "' cannot be added relative to itself");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeIfPresent(PropertySource<?> propertySource) {
/* 187 */     this.propertySourceList.remove(propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAtIndex(int index, PropertySource<?> propertySource) {
/* 194 */     removeIfPresent(propertySource);
/* 195 */     this.propertySourceList.add(index, propertySource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int assertPresentAndGetIndex(String name) {
/* 204 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/* 205 */     if (index == -1) {
/* 206 */       throw new IllegalArgumentException("PropertySource named '" + name + "' does not exist");
/*     */     }
/* 208 */     return index;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/MutablePropertySources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */