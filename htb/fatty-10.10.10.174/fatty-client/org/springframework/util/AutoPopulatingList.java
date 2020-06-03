/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ public class AutoPopulatingList<E>
/*     */   implements List<E>, Serializable
/*     */ {
/*     */   private final List<E> backingList;
/*     */   private final ElementFactory<E> elementFactory;
/*     */   
/*     */   public AutoPopulatingList(Class<? extends E> elementClass) {
/*  65 */     this(new ArrayList<>(), elementClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(List<E> backingList, Class<? extends E> elementClass) {
/*  74 */     this(backingList, new ReflectiveElementFactory<>(elementClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(ElementFactory<E> elementFactory) {
/*  82 */     this(new ArrayList<>(), elementFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoPopulatingList(List<E> backingList, ElementFactory<E> elementFactory) {
/*  90 */     Assert.notNull(backingList, "Backing List must not be null");
/*  91 */     Assert.notNull(elementFactory, "Element factory must not be null");
/*  92 */     this.backingList = backingList;
/*  93 */     this.elementFactory = elementFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, E element) {
/*  99 */     this.backingList.add(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E o) {
/* 104 */     return this.backingList.add(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c) {
/* 109 */     return this.backingList.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> c) {
/* 114 */     return this.backingList.addAll(index, c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 119 */     this.backingList.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 124 */     return this.backingList.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 129 */     return this.backingList.containsAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 138 */     int backingListSize = this.backingList.size();
/* 139 */     E element = null;
/* 140 */     if (index < backingListSize) {
/* 141 */       element = this.backingList.get(index);
/* 142 */       if (element == null) {
/* 143 */         element = this.elementFactory.createElement(index);
/* 144 */         this.backingList.set(index, element);
/*     */       } 
/*     */     } else {
/*     */       
/* 148 */       for (int x = backingListSize; x < index; x++) {
/* 149 */         this.backingList.add(null);
/*     */       }
/* 151 */       element = this.elementFactory.createElement(index);
/* 152 */       this.backingList.add(element);
/*     */     } 
/* 154 */     return element;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 159 */     return this.backingList.indexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 164 */     return this.backingList.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 169 */     return this.backingList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 174 */     return this.backingList.lastIndexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 179 */     return this.backingList.listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 184 */     return this.backingList.listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 189 */     return this.backingList.remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 194 */     return this.backingList.remove(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 199 */     return this.backingList.removeAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 204 */     return this.backingList.retainAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public E set(int index, E element) {
/* 209 */     return this.backingList.set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 214 */     return this.backingList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 219 */     return this.backingList.subList(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 224 */     return this.backingList.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 229 */     return this.backingList.toArray(a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 235 */     return this.backingList.equals(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 240 */     return this.backingList.hashCode();
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
/*     */   @FunctionalInterface
/*     */   public static interface ElementFactory<E>
/*     */   {
/*     */     E createElement(int param1Int) throws AutoPopulatingList.ElementInstantiationException;
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
/*     */   public static class ElementInstantiationException
/*     */     extends RuntimeException
/*     */   {
/*     */     public ElementInstantiationException(String msg) {
/* 269 */       super(msg);
/*     */     }
/*     */     
/*     */     public ElementInstantiationException(String message, Throwable cause) {
/* 273 */       super(message, cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReflectiveElementFactory<E>
/*     */     implements ElementFactory<E>, Serializable
/*     */   {
/*     */     private final Class<? extends E> elementClass;
/*     */ 
/*     */ 
/*     */     
/*     */     public ReflectiveElementFactory(Class<? extends E> elementClass) {
/* 287 */       Assert.notNull(elementClass, "Element class must not be null");
/* 288 */       Assert.isTrue(!elementClass.isInterface(), "Element class must not be an interface type");
/* 289 */       Assert.isTrue(!Modifier.isAbstract(elementClass.getModifiers()), "Element class cannot be an abstract class");
/* 290 */       this.elementClass = elementClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public E createElement(int index) {
/*     */       try {
/* 296 */         return ReflectionUtils.<E>accessibleConstructor((Class)this.elementClass, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 298 */       catch (NoSuchMethodException ex) {
/* 299 */         throw new AutoPopulatingList.ElementInstantiationException("No default constructor on element class: " + this.elementClass
/* 300 */             .getName(), ex);
/*     */       }
/* 302 */       catch (InstantiationException ex) {
/* 303 */         throw new AutoPopulatingList.ElementInstantiationException("Unable to instantiate element class: " + this.elementClass
/* 304 */             .getName(), ex);
/*     */       }
/* 306 */       catch (IllegalAccessException ex) {
/* 307 */         throw new AutoPopulatingList.ElementInstantiationException("Could not access element constructor: " + this.elementClass
/* 308 */             .getName(), ex);
/*     */       }
/* 310 */       catch (InvocationTargetException ex) {
/* 311 */         throw new AutoPopulatingList.ElementInstantiationException("Failed to invoke element constructor: " + this.elementClass
/* 312 */             .getName(), ex.getTargetException());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/AutoPopulatingList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */