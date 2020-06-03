/*     */ package org.springframework.jmx.export.metadata;
/*     */ 
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
/*     */ public class ManagedResource
/*     */   extends AbstractJmxAttribute
/*     */ {
/*     */   @Nullable
/*     */   private String objectName;
/*     */   private boolean log = false;
/*     */   @Nullable
/*     */   private String logFile;
/*     */   @Nullable
/*     */   private String persistPolicy;
/*  45 */   private int persistPeriod = -1;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String persistName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String persistLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectName(@Nullable String objectName) {
/*  58 */     this.objectName = objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getObjectName() {
/*  66 */     return this.objectName;
/*     */   }
/*     */   
/*     */   public void setLog(boolean log) {
/*  70 */     this.log = log;
/*     */   }
/*     */   
/*     */   public boolean isLog() {
/*  74 */     return this.log;
/*     */   }
/*     */   
/*     */   public void setLogFile(@Nullable String logFile) {
/*  78 */     this.logFile = logFile;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getLogFile() {
/*  83 */     return this.logFile;
/*     */   }
/*     */   
/*     */   public void setPersistPolicy(@Nullable String persistPolicy) {
/*  87 */     this.persistPolicy = persistPolicy;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getPersistPolicy() {
/*  92 */     return this.persistPolicy;
/*     */   }
/*     */   
/*     */   public void setPersistPeriod(int persistPeriod) {
/*  96 */     this.persistPeriod = persistPeriod;
/*     */   }
/*     */   
/*     */   public int getPersistPeriod() {
/* 100 */     return this.persistPeriod;
/*     */   }
/*     */   
/*     */   public void setPersistName(@Nullable String persistName) {
/* 104 */     this.persistName = persistName;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getPersistName() {
/* 109 */     return this.persistName;
/*     */   }
/*     */   
/*     */   public void setPersistLocation(@Nullable String persistLocation) {
/* 113 */     this.persistLocation = persistLocation;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getPersistLocation() {
/* 118 */     return this.persistLocation;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/metadata/ManagedResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */