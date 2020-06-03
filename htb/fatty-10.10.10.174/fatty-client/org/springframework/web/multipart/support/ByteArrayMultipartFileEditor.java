/*    */ package org.springframework.web.multipart.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
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
/*    */ public class ByteArrayMultipartFileEditor
/*    */   extends ByteArrayPropertyEditor
/*    */ {
/*    */   public void setValue(@Nullable Object value) {
/* 36 */     if (value instanceof MultipartFile) {
/* 37 */       MultipartFile multipartFile = (MultipartFile)value;
/*    */       try {
/* 39 */         super.setValue(multipartFile.getBytes());
/*    */       }
/* 41 */       catch (IOException ex) {
/* 42 */         throw new IllegalArgumentException("Cannot read contents of multipart file", ex);
/*    */       }
/*    */     
/* 45 */     } else if (value instanceof byte[]) {
/* 46 */       super.setValue(value);
/*    */     } else {
/*    */       
/* 49 */       super.setValue((value != null) ? value.toString().getBytes() : null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 55 */     byte[] value = (byte[])getValue();
/* 56 */     return (value != null) ? new String(value) : "";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/ByteArrayMultipartFileEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */