/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.springframework.context.HierarchicalMessageSource;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMessageSource
/*     */   extends MessageSourceSupport
/*     */   implements HierarchicalMessageSource
/*     */ {
/*     */   @Nullable
/*     */   private MessageSource parentMessageSource;
/*     */   @Nullable
/*     */   private Properties commonMessages;
/*     */   private boolean useCodeAsDefaultMessage = false;
/*     */   
/*     */   public void setParentMessageSource(@Nullable MessageSource parent) {
/*  78 */     this.parentMessageSource = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MessageSource getParentMessageSource() {
/*  84 */     return this.parentMessageSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommonMessages(@Nullable Properties commonMessages) {
/*  94 */     this.commonMessages = commonMessages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Properties getCommonMessages() {
/* 102 */     return this.commonMessages;
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
/*     */   public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
/* 123 */     this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
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
/*     */   protected boolean isUseCodeAsDefaultMessage() {
/* 135 */     return this.useCodeAsDefaultMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale) {
/* 141 */     String msg = getMessageInternal(code, args, locale);
/* 142 */     if (msg != null) {
/* 143 */       return msg;
/*     */     }
/* 145 */     if (defaultMessage == null) {
/* 146 */       return getDefaultMessage(code);
/*     */     }
/* 148 */     return renderDefaultMessage(defaultMessage, args, locale);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
/* 153 */     String msg = getMessageInternal(code, args, locale);
/* 154 */     if (msg != null) {
/* 155 */       return msg;
/*     */     }
/* 157 */     String fallback = getDefaultMessage(code);
/* 158 */     if (fallback != null) {
/* 159 */       return fallback;
/*     */     }
/* 161 */     throw new NoSuchMessageException(code, locale);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 166 */     String[] codes = resolvable.getCodes();
/* 167 */     if (codes != null) {
/* 168 */       for (String code : codes) {
/* 169 */         String message = getMessageInternal(code, resolvable.getArguments(), locale);
/* 170 */         if (message != null) {
/* 171 */           return message;
/*     */         }
/*     */       } 
/*     */     }
/* 175 */     String defaultMessage = getDefaultMessage(resolvable, locale);
/* 176 */     if (defaultMessage != null) {
/* 177 */       return defaultMessage;
/*     */     }
/* 179 */     throw new NoSuchMessageException(!ObjectUtils.isEmpty(codes) ? codes[codes.length - 1] : "", locale);
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
/*     */   @Nullable
/*     */   protected String getMessageInternal(@Nullable String code, @Nullable Object[] args, @Nullable Locale locale) {
/* 199 */     if (code == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     if (locale == null) {
/* 203 */       locale = Locale.getDefault();
/*     */     }
/* 205 */     Object[] argsToUse = args;
/*     */     
/* 207 */     if (!isAlwaysUseMessageFormat() && ObjectUtils.isEmpty(args)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 212 */       String message = resolveCodeWithoutArguments(code, locale);
/* 213 */       if (message != null) {
/* 214 */         return message;
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 222 */       argsToUse = resolveArguments(args, locale);
/*     */       
/* 224 */       MessageFormat messageFormat = resolveCode(code, locale);
/* 225 */       if (messageFormat != null) {
/* 226 */         synchronized (messageFormat) {
/* 227 */           return messageFormat.format(argsToUse);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 233 */     Properties commonMessages = getCommonMessages();
/* 234 */     if (commonMessages != null) {
/* 235 */       String commonMessage = commonMessages.getProperty(code);
/* 236 */       if (commonMessage != null) {
/* 237 */         return formatMessage(commonMessage, args, locale);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 242 */     return getMessageFromParent(code, argsToUse, locale);
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
/*     */   @Nullable
/*     */   protected String getMessageFromParent(String code, @Nullable Object[] args, Locale locale) {
/* 256 */     MessageSource parent = getParentMessageSource();
/* 257 */     if (parent != null) {
/* 258 */       if (parent instanceof AbstractMessageSource)
/*     */       {
/*     */         
/* 261 */         return ((AbstractMessageSource)parent).getMessageInternal(code, args, locale);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 266 */       return parent.getMessage(code, args, null, locale);
/*     */     } 
/*     */ 
/*     */     
/* 270 */     return null;
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
/*     */   @Nullable
/*     */   protected String getDefaultMessage(MessageSourceResolvable resolvable, Locale locale) {
/* 287 */     String defaultMessage = resolvable.getDefaultMessage();
/* 288 */     String[] codes = resolvable.getCodes();
/* 289 */     if (defaultMessage != null) {
/* 290 */       if (resolvable instanceof DefaultMessageSourceResolvable && 
/* 291 */         !((DefaultMessageSourceResolvable)resolvable).shouldRenderDefaultMessage())
/*     */       {
/*     */         
/* 294 */         return defaultMessage;
/*     */       }
/* 296 */       if (!ObjectUtils.isEmpty((Object[])codes) && defaultMessage.equals(codes[0]))
/*     */       {
/* 298 */         return defaultMessage;
/*     */       }
/* 300 */       return renderDefaultMessage(defaultMessage, resolvable.getArguments(), locale);
/*     */     } 
/* 302 */     return !ObjectUtils.isEmpty((Object[])codes) ? getDefaultMessage(codes[0]) : null;
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
/*     */   protected String getDefaultMessage(String code) {
/* 317 */     if (isUseCodeAsDefaultMessage()) {
/* 318 */       return code;
/*     */     }
/* 320 */     return null;
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
/*     */   protected Object[] resolveArguments(@Nullable Object[] args, Locale locale) {
/* 334 */     if (ObjectUtils.isEmpty(args)) {
/* 335 */       return super.resolveArguments(args, locale);
/*     */     }
/* 337 */     List<Object> resolvedArgs = new ArrayList(args.length);
/* 338 */     for (Object arg : args) {
/* 339 */       if (arg instanceof MessageSourceResolvable) {
/* 340 */         resolvedArgs.add(getMessage((MessageSourceResolvable)arg, locale));
/*     */       } else {
/*     */         
/* 343 */         resolvedArgs.add(arg);
/*     */       } 
/*     */     } 
/* 346 */     return resolvedArgs.toArray();
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
/*     */   @Nullable
/*     */   protected String resolveCodeWithoutArguments(String code, Locale locale) {
/* 368 */     MessageFormat messageFormat = resolveCode(code, locale);
/* 369 */     if (messageFormat != null) {
/* 370 */       synchronized (messageFormat) {
/* 371 */         return messageFormat.format(new Object[0]);
/*     */       } 
/*     */     }
/* 374 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract MessageFormat resolveCode(String paramString, Locale paramLocale);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/AbstractMessageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */