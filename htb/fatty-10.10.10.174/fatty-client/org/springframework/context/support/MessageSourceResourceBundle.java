/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.NoSuchMessageException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageSourceResourceBundle
/*     */   extends ResourceBundle
/*     */ {
/*     */   private final MessageSource messageSource;
/*     */   private final Locale locale;
/*     */   
/*     */   public MessageSourceResourceBundle(MessageSource source, Locale locale) {
/*  52 */     Assert.notNull(source, "MessageSource must not be null");
/*  53 */     this.messageSource = source;
/*  54 */     this.locale = locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageSourceResourceBundle(MessageSource source, Locale locale, ResourceBundle parent) {
/*  64 */     this(source, locale);
/*  65 */     setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object handleGetObject(String key) {
/*     */     try {
/*  77 */       return this.messageSource.getMessage(key, null, this.locale);
/*     */     }
/*  79 */     catch (NoSuchMessageException ex) {
/*  80 */       return null;
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
/*     */   public boolean containsKey(String key) {
/*     */     try {
/*  93 */       this.messageSource.getMessage(key, null, this.locale);
/*  94 */       return true;
/*     */     }
/*  96 */     catch (NoSuchMessageException ex) {
/*  97 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getKeys() {
/* 107 */     throw new UnsupportedOperationException("MessageSourceResourceBundle does not support enumerating its keys");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 116 */     return this.locale;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/MessageSourceResourceBundle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */