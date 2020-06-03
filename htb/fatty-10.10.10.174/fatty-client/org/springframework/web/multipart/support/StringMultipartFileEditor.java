/*    */ package org.springframework.web.multipart.support;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.IOException;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.multipart.MultipartFile;
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
/*    */ public class StringMultipartFileEditor
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   @Nullable
/*    */   private final String charsetName;
/*    */   
/*    */   public StringMultipartFileEditor() {
/* 44 */     this.charsetName = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StringMultipartFileEditor(String charsetName) {
/* 53 */     this.charsetName = charsetName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) {
/* 59 */     setValue(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(Object value) {
/* 64 */     if (value instanceof MultipartFile) {
/* 65 */       MultipartFile multipartFile = (MultipartFile)value;
/*    */       try {
/* 67 */         super.setValue((this.charsetName != null) ? new String(multipartFile
/* 68 */               .getBytes(), this.charsetName) : new String(multipartFile
/* 69 */               .getBytes()));
/*    */       }
/* 71 */       catch (IOException ex) {
/* 72 */         throw new IllegalArgumentException("Cannot read contents of multipart file", ex);
/*    */       } 
/*    */     } else {
/*    */       
/* 76 */       super.setValue(value);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/StringMultipartFileEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */