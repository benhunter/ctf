/*     */ package org.springframework.web.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HtmlCharacterEntityDecoder
/*     */ {
/*     */   private static final int MAX_REFERENCE_SIZE = 10;
/*     */   private final HtmlCharacterEntityReferences characterEntityReferences;
/*     */   private final String originalMessage;
/*     */   private final StringBuilder decodedMessage;
/*  38 */   private int currentPosition = 0;
/*     */   
/*  40 */   private int nextPotentialReferencePosition = -1;
/*     */   
/*  42 */   private int nextSemicolonPosition = -2;
/*     */ 
/*     */   
/*     */   public HtmlCharacterEntityDecoder(HtmlCharacterEntityReferences characterEntityReferences, String original) {
/*  46 */     this.characterEntityReferences = characterEntityReferences;
/*  47 */     this.originalMessage = original;
/*  48 */     this.decodedMessage = new StringBuilder(original.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public String decode() {
/*  53 */     while (this.currentPosition < this.originalMessage.length()) {
/*  54 */       findNextPotentialReference(this.currentPosition);
/*  55 */       copyCharactersTillPotentialReference();
/*  56 */       processPossibleReference();
/*     */     } 
/*  58 */     return this.decodedMessage.toString();
/*     */   }
/*     */   
/*     */   private void findNextPotentialReference(int startPosition) {
/*  62 */     this.nextPotentialReferencePosition = Math.max(startPosition, this.nextSemicolonPosition - 10);
/*     */     
/*     */     do {
/*  65 */       this
/*  66 */         .nextPotentialReferencePosition = this.originalMessage.indexOf('&', this.nextPotentialReferencePosition);
/*     */       
/*  68 */       if (this.nextSemicolonPosition != -1 && this.nextSemicolonPosition < this.nextPotentialReferencePosition)
/*     */       {
/*  70 */         this.nextSemicolonPosition = this.originalMessage.indexOf(';', this.nextPotentialReferencePosition + 1);
/*     */       }
/*     */       
/*  73 */       boolean isPotentialReference = (this.nextPotentialReferencePosition != -1 && this.nextSemicolonPosition != -1 && this.nextPotentialReferencePosition - this.nextSemicolonPosition < 10);
/*     */ 
/*     */ 
/*     */       
/*  77 */       if (isPotentialReference) {
/*     */         break;
/*     */       }
/*  80 */       if (this.nextPotentialReferencePosition == -1) {
/*     */         break;
/*     */       }
/*  83 */       if (this.nextSemicolonPosition == -1) {
/*  84 */         this.nextPotentialReferencePosition = -1;
/*     */         
/*     */         break;
/*     */       } 
/*  88 */       this.nextPotentialReferencePosition++;
/*     */     }
/*  90 */     while (this.nextPotentialReferencePosition != -1);
/*     */   }
/*     */   
/*     */   private void copyCharactersTillPotentialReference() {
/*  94 */     if (this.nextPotentialReferencePosition != this.currentPosition) {
/*     */       
/*  96 */       int skipUntilIndex = (this.nextPotentialReferencePosition != -1) ? this.nextPotentialReferencePosition : this.originalMessage.length();
/*  97 */       if (skipUntilIndex - this.currentPosition > 3) {
/*  98 */         this.decodedMessage.append(this.originalMessage, this.currentPosition, skipUntilIndex);
/*  99 */         this.currentPosition = skipUntilIndex;
/*     */       } else {
/*     */         
/* 102 */         while (this.currentPosition < skipUntilIndex) {
/* 103 */           this.decodedMessage.append(this.originalMessage.charAt(this.currentPosition++));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processPossibleReference() {
/* 110 */     if (this.nextPotentialReferencePosition != -1) {
/* 111 */       boolean isNumberedReference = (this.originalMessage.charAt(this.currentPosition + 1) == '#');
/* 112 */       boolean wasProcessable = isNumberedReference ? processNumberedReference() : processNamedReference();
/* 113 */       if (wasProcessable) {
/* 114 */         this.currentPosition = this.nextSemicolonPosition + 1;
/*     */       } else {
/*     */         
/* 117 */         char currentChar = this.originalMessage.charAt(this.currentPosition);
/* 118 */         this.decodedMessage.append(currentChar);
/* 119 */         this.currentPosition++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean processNumberedReference() {
/* 125 */     char referenceChar = this.originalMessage.charAt(this.nextPotentialReferencePosition + 2);
/* 126 */     boolean isHexNumberedReference = (referenceChar == 'x' || referenceChar == 'X');
/*     */ 
/*     */     
/*     */     try {
/* 130 */       int value = !isHexNumberedReference ? Integer.parseInt(getReferenceSubstring(2)) : Integer.parseInt(getReferenceSubstring(3), 16);
/* 131 */       this.decodedMessage.append((char)value);
/* 132 */       return true;
/*     */     }
/* 134 */     catch (NumberFormatException ex) {
/* 135 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean processNamedReference() {
/* 140 */     String referenceName = getReferenceSubstring(1);
/* 141 */     char mappedCharacter = this.characterEntityReferences.convertToCharacter(referenceName);
/* 142 */     if (mappedCharacter != Character.MAX_VALUE) {
/* 143 */       this.decodedMessage.append(mappedCharacter);
/* 144 */       return true;
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   private String getReferenceSubstring(int referenceOffset) {
/* 150 */     return this.originalMessage.substring(this.nextPotentialReferencePosition + referenceOffset, this.nextSemicolonPosition);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/HtmlCharacterEntityDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */