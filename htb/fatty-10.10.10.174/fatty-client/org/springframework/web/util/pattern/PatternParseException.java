/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternParseException
/*     */   extends IllegalArgumentException
/*     */ {
/*     */   private final int position;
/*     */   private final char[] pattern;
/*     */   private final PatternMessage messageType;
/*     */   private final Object[] inserts;
/*     */   
/*     */   PatternParseException(int pos, char[] pattern, PatternMessage messageType, Object... inserts) {
/*  40 */     super(messageType.formatMessage(inserts));
/*  41 */     this.position = pos;
/*  42 */     this.pattern = pattern;
/*  43 */     this.messageType = messageType;
/*  44 */     this.inserts = inserts;
/*     */   }
/*     */   
/*     */   PatternParseException(Throwable cause, int pos, char[] pattern, PatternMessage messageType, Object... inserts) {
/*  48 */     super(messageType.formatMessage(inserts), cause);
/*  49 */     this.position = pos;
/*  50 */     this.pattern = pattern;
/*  51 */     this.messageType = messageType;
/*  52 */     this.inserts = inserts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  61 */     return this.messageType.formatMessage(this.inserts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toDetailedString() {
/*  69 */     StringBuilder buf = new StringBuilder();
/*  70 */     buf.append(this.pattern).append('\n');
/*  71 */     for (int i = 0; i < this.position; i++) {
/*  72 */       buf.append(' ');
/*     */     }
/*  74 */     buf.append("^\n");
/*  75 */     buf.append(getMessage());
/*  76 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public int getPosition() {
/*  80 */     return this.position;
/*     */   }
/*     */   
/*     */   public PatternMessage getMessageType() {
/*  84 */     return this.messageType;
/*     */   }
/*     */   
/*     */   public Object[] getInserts() {
/*  88 */     return this.inserts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum PatternMessage
/*     */   {
/*  97 */     MISSING_CLOSE_CAPTURE("Expected close capture character after variable name '}'"),
/*  98 */     MISSING_OPEN_CAPTURE("Missing preceding open capture character before variable name'{'"),
/*  99 */     ILLEGAL_NESTED_CAPTURE("Not allowed to nest variable captures"),
/* 100 */     CANNOT_HAVE_ADJACENT_CAPTURES("Adjacent captures are not allowed"),
/* 101 */     ILLEGAL_CHARACTER_AT_START_OF_CAPTURE_DESCRIPTOR("Char ''{0}'' not allowed at start of captured variable name"),
/* 102 */     ILLEGAL_CHARACTER_IN_CAPTURE_DESCRIPTOR("Char ''{0}'' is not allowed in a captured variable name"),
/* 103 */     NO_MORE_DATA_EXPECTED_AFTER_CAPTURE_THE_REST("No more pattern data allowed after '{*...}' pattern element"),
/* 104 */     BADLY_FORMED_CAPTURE_THE_REST("Expected form when capturing the rest of the path is simply '{*...}'"),
/* 105 */     MISSING_REGEX_CONSTRAINT("Missing regex constraint on capture"),
/* 106 */     ILLEGAL_DOUBLE_CAPTURE("Not allowed to capture ''{0}'' twice in the same pattern"),
/* 107 */     REGEX_PATTERN_SYNTAX_EXCEPTION("Exception occurred in regex pattern compilation"),
/* 108 */     CAPTURE_ALL_IS_STANDALONE_CONSTRUCT("'{*...}' can only be preceded by a path separator");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     PatternMessage(String message) {
/* 113 */       this.message = message;
/*     */     }
/*     */     
/*     */     public String formatMessage(Object... inserts) {
/* 117 */       return MessageFormat.format(this.message, inserts);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/PatternParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */