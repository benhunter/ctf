/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
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
/*     */ public class MessageSourceAccessor
/*     */ {
/*     */   private final MessageSource messageSource;
/*     */   @Nullable
/*     */   private final Locale defaultLocale;
/*     */   
/*     */   public MessageSourceAccessor(MessageSource messageSource) {
/*  53 */     this.messageSource = messageSource;
/*  54 */     this.defaultLocale = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageSourceAccessor(MessageSource messageSource, Locale defaultLocale) {
/*  63 */     this.messageSource = messageSource;
/*  64 */     this.defaultLocale = defaultLocale;
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
/*     */   protected Locale getDefaultLocale() {
/*  77 */     return (this.defaultLocale != null) ? this.defaultLocale : LocaleContextHolder.getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, String defaultMessage) {
/*  87 */     String msg = this.messageSource.getMessage(code, null, defaultMessage, getDefaultLocale());
/*  88 */     return (msg != null) ? msg : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, String defaultMessage, Locale locale) {
/*  99 */     String msg = this.messageSource.getMessage(code, null, defaultMessage, locale);
/* 100 */     return (msg != null) ? msg : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, @Nullable Object[] args, String defaultMessage) {
/* 111 */     String msg = this.messageSource.getMessage(code, args, defaultMessage, getDefaultLocale());
/* 112 */     return (msg != null) ? msg : "";
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
/*     */   public String getMessage(String code, @Nullable Object[] args, String defaultMessage, Locale locale) {
/* 124 */     String msg = this.messageSource.getMessage(code, args, defaultMessage, locale);
/* 125 */     return (msg != null) ? msg : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code) throws NoSuchMessageException {
/* 135 */     return this.messageSource.getMessage(code, null, getDefaultLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, Locale locale) throws NoSuchMessageException {
/* 146 */     return this.messageSource.getMessage(code, null, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
/* 157 */     return this.messageSource.getMessage(code, args, getDefaultLocale());
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
/*     */   public String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
/* 169 */     return this.messageSource.getMessage(code, args, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
/* 180 */     return this.messageSource.getMessage(resolvable, getDefaultLocale());
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
/*     */   public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 192 */     return this.messageSource.getMessage(resolvable, locale);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/MessageSourceAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */