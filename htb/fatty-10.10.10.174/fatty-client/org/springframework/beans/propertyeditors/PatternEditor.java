/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.regex.Pattern;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PatternEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final int flags;
/*    */   
/*    */   public PatternEditor() {
/* 42 */     this.flags = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PatternEditor(int flags) {
/* 56 */     this.flags = flags;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(@Nullable String text) {
/* 62 */     setValue((text != null) ? Pattern.compile(text, this.flags) : null);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 67 */     Pattern value = (Pattern)getValue();
/* 68 */     return (value != null) ? value.pattern() : "";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/PatternEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */