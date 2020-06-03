/*    */ package org.springframework.http.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.util.MultiValueMap;
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
/*    */ public interface PathContainer
/*    */ {
/*    */   String value();
/*    */   
/*    */   List<Element> elements();
/*    */   
/*    */   default PathContainer subPath(int index) {
/* 54 */     return subPath(index, elements().size());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default PathContainer subPath(int startIndex, int endIndex) {
/* 65 */     return DefaultPathContainer.subPath(this, startIndex, endIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static PathContainer parsePath(String path) {
/* 76 */     return DefaultPathContainer.createFromUrlPath(path);
/*    */   }
/*    */   
/*    */   public static interface PathSegment extends Element {
/*    */     String valueToMatch();
/*    */     
/*    */     char[] valueToMatchAsChars();
/*    */     
/*    */     MultiValueMap<String, String> parameters();
/*    */   }
/*    */   
/*    */   public static interface Separator extends Element {}
/*    */   
/*    */   public static interface Element {
/*    */     String value();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/PathContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */