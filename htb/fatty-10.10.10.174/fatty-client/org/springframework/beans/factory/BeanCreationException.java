/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.core.NestedRuntimeException;
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
/*     */ public class BeanCreationException
/*     */   extends FatalBeanException
/*     */ {
/*     */   @Nullable
/*     */   private final String beanName;
/*     */   @Nullable
/*     */   private final String resourceDescription;
/*     */   @Nullable
/*     */   private List<Throwable> relatedCauses;
/*     */   
/*     */   public BeanCreationException(String msg) {
/*  52 */     super(msg);
/*  53 */     this.beanName = null;
/*  54 */     this.resourceDescription = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(String msg, Throwable cause) {
/*  63 */     super(msg, cause);
/*  64 */     this.beanName = null;
/*  65 */     this.resourceDescription = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(String beanName, String msg) {
/*  74 */     super("Error creating bean with name '" + beanName + "': " + msg);
/*  75 */     this.beanName = beanName;
/*  76 */     this.resourceDescription = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(String beanName, String msg, Throwable cause) {
/*  86 */     this(beanName, msg);
/*  87 */     initCause(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(@Nullable String resourceDescription, @Nullable String beanName, String msg) {
/*  98 */     super("Error creating bean with name '" + beanName + "'" + ((resourceDescription != null) ? (" defined in " + resourceDescription) : "") + ": " + msg);
/*     */     
/* 100 */     this.resourceDescription = resourceDescription;
/* 101 */     this.beanName = beanName;
/* 102 */     this.relatedCauses = null;
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
/*     */   public BeanCreationException(@Nullable String resourceDescription, String beanName, String msg, Throwable cause) {
/* 114 */     this(resourceDescription, beanName, msg);
/* 115 */     initCause(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getResourceDescription() {
/* 125 */     return this.resourceDescription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBeanName() {
/* 133 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRelatedCause(Throwable ex) {
/* 143 */     if (this.relatedCauses == null) {
/* 144 */       this.relatedCauses = new ArrayList<>();
/*     */     }
/* 146 */     this.relatedCauses.add(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable[] getRelatedCauses() {
/* 155 */     if (this.relatedCauses == null) {
/* 156 */       return null;
/*     */     }
/* 158 */     return this.relatedCauses.<Throwable>toArray(new Throwable[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     StringBuilder sb = new StringBuilder(super.toString());
/* 165 */     if (this.relatedCauses != null) {
/* 166 */       for (Throwable relatedCause : this.relatedCauses) {
/* 167 */         sb.append("\nRelated cause: ");
/* 168 */         sb.append(relatedCause);
/*     */       } 
/*     */     }
/* 171 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream ps) {
/* 176 */     synchronized (ps) {
/* 177 */       super.printStackTrace(ps);
/* 178 */       if (this.relatedCauses != null) {
/* 179 */         for (Throwable relatedCause : this.relatedCauses) {
/* 180 */           ps.println("Related cause:");
/* 181 */           relatedCause.printStackTrace(ps);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter pw) {
/* 189 */     synchronized (pw) {
/* 190 */       super.printStackTrace(pw);
/* 191 */       if (this.relatedCauses != null) {
/* 192 */         for (Throwable relatedCause : this.relatedCauses) {
/* 193 */           pw.println("Related cause:");
/* 194 */           relatedCause.printStackTrace(pw);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Class<?> exClass) {
/* 202 */     if (super.contains(exClass)) {
/* 203 */       return true;
/*     */     }
/* 205 */     if (this.relatedCauses != null) {
/* 206 */       for (Throwable relatedCause : this.relatedCauses) {
/* 207 */         if (relatedCause instanceof NestedRuntimeException && ((NestedRuntimeException)relatedCause)
/* 208 */           .contains(exClass)) {
/* 209 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 213 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanCreationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */