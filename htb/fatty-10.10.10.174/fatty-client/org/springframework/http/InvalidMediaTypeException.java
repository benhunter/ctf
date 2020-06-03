/*    */ package org.springframework.http;
/*    */ 
/*    */ import org.springframework.util.InvalidMimeTypeException;
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
/*    */ public class InvalidMediaTypeException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   private final String mediaType;
/*    */   
/*    */   public InvalidMediaTypeException(String mediaType, String message) {
/* 40 */     super("Invalid media type \"" + mediaType + "\": " + message);
/* 41 */     this.mediaType = mediaType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   InvalidMediaTypeException(InvalidMimeTypeException ex) {
/* 48 */     super(ex.getMessage(), (Throwable)ex);
/* 49 */     this.mediaType = ex.getMimeType();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMediaType() {
/* 57 */     return this.mediaType;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/InvalidMediaTypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */