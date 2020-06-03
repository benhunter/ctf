/*    */ package org.springframework.http;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class HttpCookie
/*    */ {
/*    */   private final String name;
/*    */   private final String value;
/*    */   
/*    */   public HttpCookie(String name, @Nullable String value) {
/* 39 */     Assert.hasLength(name, "'name' is required and must not be empty.");
/* 40 */     this.name = name;
/* 41 */     this.value = (value != null) ? value : "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     return this.name.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 66 */     if (this == other) {
/* 67 */       return true;
/*    */     }
/* 69 */     if (!(other instanceof HttpCookie)) {
/* 70 */       return false;
/*    */     }
/* 72 */     HttpCookie otherCookie = (HttpCookie)other;
/* 73 */     return this.name.equalsIgnoreCase(otherCookie.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return this.name + '=' + this.value;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpCookie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */