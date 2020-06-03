/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LazySingletonAspectInstanceFactoryDecorator
/*     */   implements MetadataAwareAspectInstanceFactory, Serializable
/*     */ {
/*     */   private final MetadataAwareAspectInstanceFactory maaif;
/*     */   @Nullable
/*     */   private volatile Object materialized;
/*     */   
/*     */   public LazySingletonAspectInstanceFactoryDecorator(MetadataAwareAspectInstanceFactory maaif) {
/*  45 */     Assert.notNull(maaif, "AspectInstanceFactory must not be null");
/*  46 */     this.maaif = maaif;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAspectInstance() {
/*  52 */     Object aspectInstance = this.materialized;
/*  53 */     if (aspectInstance == null) {
/*  54 */       Object mutex = this.maaif.getAspectCreationMutex();
/*  55 */       if (mutex == null) {
/*  56 */         aspectInstance = this.maaif.getAspectInstance();
/*  57 */         this.materialized = aspectInstance;
/*     */       } else {
/*     */         
/*  60 */         synchronized (mutex) {
/*  61 */           aspectInstance = this.materialized;
/*  62 */           if (aspectInstance == null) {
/*  63 */             aspectInstance = this.maaif.getAspectInstance();
/*  64 */             this.materialized = aspectInstance;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  69 */     return aspectInstance;
/*     */   }
/*     */   
/*     */   public boolean isMaterialized() {
/*  73 */     return (this.materialized != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getAspectClassLoader() {
/*  79 */     return this.maaif.getAspectClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public AspectMetadata getAspectMetadata() {
/*  84 */     return this.maaif.getAspectMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getAspectCreationMutex() {
/*  90 */     return this.maaif.getAspectCreationMutex();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  95 */     return this.maaif.getOrder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return "LazySingletonAspectInstanceFactoryDecorator: decorating " + this.maaif;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/LazySingletonAspectInstanceFactoryDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */