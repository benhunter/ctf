/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyBatchUpdateException
/*     */   extends BeansException
/*     */ {
/*     */   private final PropertyAccessException[] propertyAccessExceptions;
/*     */   
/*     */   public PropertyBatchUpdateException(PropertyAccessException[] propertyAccessExceptions) {
/*  51 */     super((String)null, (Throwable)null);
/*  52 */     Assert.notEmpty((Object[])propertyAccessExceptions, "At least 1 PropertyAccessException required");
/*  53 */     this.propertyAccessExceptions = propertyAccessExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getExceptionCount() {
/*  61 */     return this.propertyAccessExceptions.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PropertyAccessException[] getPropertyAccessExceptions() {
/*  69 */     return this.propertyAccessExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyAccessException getPropertyAccessException(String propertyName) {
/*  77 */     for (PropertyAccessException pae : this.propertyAccessExceptions) {
/*  78 */       if (ObjectUtils.nullSafeEquals(propertyName, pae.getPropertyName())) {
/*  79 */         return pae;
/*     */       }
/*     */     } 
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  88 */     StringBuilder sb = new StringBuilder("Failed properties: ");
/*  89 */     for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/*  90 */       sb.append(this.propertyAccessExceptions[i].getMessage());
/*  91 */       if (i < this.propertyAccessExceptions.length - 1) {
/*  92 */         sb.append("; ");
/*     */       }
/*     */     } 
/*  95 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     StringBuilder sb = new StringBuilder();
/* 101 */     sb.append(getClass().getName()).append("; nested PropertyAccessExceptions (");
/* 102 */     sb.append(getExceptionCount()).append(") are:");
/* 103 */     for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/* 104 */       sb.append('\n').append("PropertyAccessException ").append(i + 1).append(": ");
/* 105 */       sb.append(this.propertyAccessExceptions[i]);
/*     */     } 
/* 107 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream ps) {
/* 112 */     synchronized (ps) {
/* 113 */       ps.println(getClass().getName() + "; nested PropertyAccessException details (" + 
/* 114 */           getExceptionCount() + ") are:");
/* 115 */       for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/* 116 */         ps.println("PropertyAccessException " + (i + 1) + ":");
/* 117 */         this.propertyAccessExceptions[i].printStackTrace(ps);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter pw) {
/* 124 */     synchronized (pw) {
/* 125 */       pw.println(getClass().getName() + "; nested PropertyAccessException details (" + 
/* 126 */           getExceptionCount() + ") are:");
/* 127 */       for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
/* 128 */         pw.println("PropertyAccessException " + (i + 1) + ":");
/* 129 */         this.propertyAccessExceptions[i].printStackTrace(pw);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Class<?> exType) {
/* 136 */     if (exType == null) {
/* 137 */       return false;
/*     */     }
/* 139 */     if (exType.isInstance(this)) {
/* 140 */       return true;
/*     */     }
/* 142 */     for (PropertyAccessException pae : this.propertyAccessExceptions) {
/* 143 */       if (pae.contains(exType)) {
/* 144 */         return true;
/*     */       }
/*     */     } 
/* 147 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyBatchUpdateException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */