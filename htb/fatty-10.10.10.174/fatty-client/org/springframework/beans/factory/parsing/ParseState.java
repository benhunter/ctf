/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import java.util.LinkedList;
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
/*     */ public final class ParseState
/*     */ {
/*     */   private static final char TAB = '\t';
/*     */   private final LinkedList<Entry> state;
/*     */   
/*     */   public ParseState() {
/*  52 */     this.state = new LinkedList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParseState(ParseState other) {
/*  61 */     this.state = (LinkedList<Entry>)other.state.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(Entry entry) {
/*  69 */     this.state.push(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pop() {
/*  76 */     this.state.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entry peek() {
/*  85 */     return this.state.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseState snapshot() {
/*  93 */     return new ParseState(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     StringBuilder sb = new StringBuilder();
/* 103 */     for (int x = 0; x < this.state.size(); x++) {
/* 104 */       if (x > 0) {
/* 105 */         sb.append('\n');
/* 106 */         for (int y = 0; y < x; y++) {
/* 107 */           sb.append('\t');
/*     */         }
/* 109 */         sb.append("-> ");
/*     */       } 
/* 111 */       sb.append(this.state.get(x));
/*     */     } 
/* 113 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static interface Entry {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/ParseState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */