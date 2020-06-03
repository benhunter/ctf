/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public class RemoteInvocationResult
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2138555143707773549L;
/*     */   @Nullable
/*     */   private Object value;
/*     */   @Nullable
/*     */   private Throwable exception;
/*     */   
/*     */   public RemoteInvocationResult(@Nullable Object value) {
/*  57 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocationResult(@Nullable Throwable exception) {
/*  66 */     this.exception = exception;
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
/*     */   public RemoteInvocationResult() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable Object value) {
/*  87 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() {
/*  97 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setException(@Nullable Throwable exception) {
/* 108 */     this.exception = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getException() {
/* 118 */     return this.exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasException() {
/* 129 */     return (this.exception != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasInvocationTargetException() {
/* 138 */     return this.exception instanceof InvocationTargetException;
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
/*     */   @Nullable
/*     */   public Object recreate() throws Throwable {
/* 151 */     if (this.exception != null) {
/* 152 */       Throwable exToThrow = this.exception;
/* 153 */       if (this.exception instanceof InvocationTargetException) {
/* 154 */         exToThrow = ((InvocationTargetException)this.exception).getTargetException();
/*     */       }
/* 156 */       RemoteInvocationUtils.fillInClientStackTraceIfPossible(exToThrow);
/* 157 */       throw exToThrow;
/*     */     } 
/*     */     
/* 160 */     return this.value;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteInvocationResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */