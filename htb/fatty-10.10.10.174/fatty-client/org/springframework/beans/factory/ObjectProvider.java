/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import org.springframework.beans.BeansException;
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
/*     */ public interface ObjectProvider<T>
/*     */   extends ObjectFactory<T>, Iterable<T>
/*     */ {
/*     */   T getObject(Object... paramVarArgs) throws BeansException;
/*     */   
/*     */   @Nullable
/*     */   T getIfAvailable() throws BeansException;
/*     */   
/*     */   default T getIfAvailable(Supplier<T> defaultSupplier) throws BeansException {
/*  77 */     T dependency = getIfAvailable();
/*  78 */     return (dependency != null) ? dependency : defaultSupplier.get();
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
/*     */   default void ifAvailable(Consumer<T> dependencyConsumer) throws BeansException {
/*  91 */     T dependency = getIfAvailable();
/*  92 */     if (dependency != null) {
/*  93 */       dependencyConsumer.accept(dependency);
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   T getIfUnique() throws BeansException;
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
/*     */   default T getIfUnique(Supplier<T> defaultSupplier) throws BeansException {
/* 121 */     T dependency = getIfUnique();
/* 122 */     return (dependency != null) ? dependency : defaultSupplier.get();
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
/*     */   default void ifUnique(Consumer<T> dependencyConsumer) throws BeansException {
/* 135 */     T dependency = getIfUnique();
/* 136 */     if (dependency != null) {
/* 137 */       dependencyConsumer.accept(dependency);
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
/*     */   default Iterator<T> iterator() {
/* 149 */     return stream().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Stream<T> stream() {
/* 160 */     throw new UnsupportedOperationException("Multi element access not supported");
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
/*     */   default Stream<T> orderedStream() {
/* 176 */     throw new UnsupportedOperationException("Ordered element access not supported");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/ObjectProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */