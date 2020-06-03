/*    */ package org.springframework.jmx.export.metadata;
/*    */ 
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
/*    */ public class ManagedAttribute
/*    */   extends AbstractJmxAttribute
/*    */ {
/* 35 */   public static final ManagedAttribute EMPTY = new ManagedAttribute();
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private Object defaultValue;
/*    */   
/*    */   @Nullable
/*    */   private String persistPolicy;
/*    */   
/* 44 */   private int persistPeriod = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefaultValue(@Nullable Object defaultValue) {
/* 51 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getDefaultValue() {
/* 59 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */   public void setPersistPolicy(@Nullable String persistPolicy) {
/* 63 */     this.persistPolicy = persistPolicy;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getPersistPolicy() {
/* 68 */     return this.persistPolicy;
/*    */   }
/*    */   
/*    */   public void setPersistPeriod(int persistPeriod) {
/* 72 */     this.persistPeriod = persistPeriod;
/*    */   }
/*    */   
/*    */   public int getPersistPeriod() {
/* 76 */     return this.persistPeriod;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/metadata/ManagedAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */