/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultMessageSourceResolvable
/*     */   implements MessageSourceResolvable, Serializable
/*     */ {
/*     */   @Nullable
/*     */   private final String[] codes;
/*     */   @Nullable
/*     */   private final Object[] arguments;
/*     */   @Nullable
/*     */   private final String defaultMessage;
/*     */   
/*     */   public DefaultMessageSourceResolvable(String code) {
/*  53 */     this(new String[] { code }, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(String[] codes) {
/*  61 */     this(codes, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(String[] codes, String defaultMessage) {
/*  70 */     this(codes, null, defaultMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(String[] codes, Object[] arguments) {
/*  79 */     this(codes, arguments, null);
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
/*     */   public DefaultMessageSourceResolvable(@Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage) {
/*  91 */     this.codes = codes;
/*  92 */     this.arguments = arguments;
/*  93 */     this.defaultMessage = defaultMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(MessageSourceResolvable resolvable) {
/* 101 */     this(resolvable.getCodes(), resolvable.getArguments(), resolvable.getDefaultMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCode() {
/* 111 */     return (this.codes != null && this.codes.length > 0) ? this.codes[this.codes.length - 1] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getCodes() {
/* 117 */     return this.codes;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object[] getArguments() {
/* 123 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDefaultMessage() {
/* 129 */     return this.defaultMessage;
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
/*     */   public boolean shouldRenderDefaultMessage() {
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String resolvableToString() {
/* 153 */     StringBuilder result = new StringBuilder(64);
/* 154 */     result.append("codes [").append(StringUtils.arrayToDelimitedString((Object[])this.codes, ","));
/* 155 */     result.append("]; arguments [").append(StringUtils.arrayToDelimitedString(this.arguments, ","));
/* 156 */     result.append("]; default message [").append(this.defaultMessage).append(']');
/* 157 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     return getClass().getName() + ": " + resolvableToString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 174 */     if (this == other) {
/* 175 */       return true;
/*     */     }
/* 177 */     if (!(other instanceof MessageSourceResolvable)) {
/* 178 */       return false;
/*     */     }
/* 180 */     MessageSourceResolvable otherResolvable = (MessageSourceResolvable)other;
/* 181 */     return (ObjectUtils.nullSafeEquals(getCodes(), otherResolvable.getCodes()) && 
/* 182 */       ObjectUtils.nullSafeEquals(getArguments(), otherResolvable.getArguments()) && 
/* 183 */       ObjectUtils.nullSafeEquals(getDefaultMessage(), otherResolvable.getDefaultMessage()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 188 */     int hashCode = ObjectUtils.nullSafeHashCode((Object[])getCodes());
/* 189 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getArguments());
/* 190 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getDefaultMessage());
/* 191 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/DefaultMessageSourceResolvable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */