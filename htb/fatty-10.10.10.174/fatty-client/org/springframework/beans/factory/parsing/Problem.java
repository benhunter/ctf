/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ public class Problem
/*     */ {
/*     */   private final String message;
/*     */   private final Location location;
/*     */   @Nullable
/*     */   private final ParseState parseState;
/*     */   @Nullable
/*     */   private final Throwable rootCause;
/*     */   
/*     */   public Problem(String message, Location location) {
/*  52 */     this(message, location, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Problem(String message, Location location, ParseState parseState) {
/*  62 */     this(message, location, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Problem(String message, Location location, @Nullable ParseState parseState, @Nullable Throwable rootCause) {
/*  73 */     Assert.notNull(message, "Message must not be null");
/*  74 */     Assert.notNull(location, "Location must not be null");
/*  75 */     this.message = message;
/*  76 */     this.location = location;
/*  77 */     this.parseState = parseState;
/*  78 */     this.rootCause = rootCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  86 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/*  93 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResourceDescription() {
/* 102 */     return getLocation().getResource().getDescription();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ParseState getParseState() {
/* 110 */     return this.parseState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getRootCause() {
/* 118 */     return this.rootCause;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     StringBuilder sb = new StringBuilder();
/* 125 */     sb.append("Configuration problem: ");
/* 126 */     sb.append(getMessage());
/* 127 */     sb.append("\nOffending resource: ").append(getResourceDescription());
/* 128 */     if (getParseState() != null) {
/* 129 */       sb.append('\n').append(getParseState());
/*     */     }
/* 131 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/Problem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */