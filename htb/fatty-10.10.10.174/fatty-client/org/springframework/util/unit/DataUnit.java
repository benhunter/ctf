/*    */ package org.springframework.util.unit;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ public enum DataUnit
/*    */ {
/* 32 */   BYTES("B", DataSize.ofBytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   KILOBYTES("KB", DataSize.ofKilobytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   MEGABYTES("MB", DataSize.ofMegabytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   GIGABYTES("GB", DataSize.ofGigabytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   TERABYTES("TB", DataSize.ofTerabytes(1L));
/*    */ 
/*    */   
/*    */   private final String suffix;
/*    */   
/*    */   private final DataSize size;
/*    */ 
/*    */   
/*    */   DataUnit(String suffix, DataSize size) {
/* 61 */     this.suffix = suffix;
/* 62 */     this.size = size;
/*    */   }
/*    */   
/*    */   DataSize size() {
/* 66 */     return this.size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataUnit fromSuffix(String suffix) {
/* 77 */     for (DataUnit candidate : values()) {
/* 78 */       if (Objects.equals(candidate.suffix, suffix)) {
/* 79 */         return candidate;
/*    */       }
/*    */     } 
/* 82 */     throw new IllegalArgumentException("Unknown unit '" + suffix + "'");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/unit/DataUnit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */