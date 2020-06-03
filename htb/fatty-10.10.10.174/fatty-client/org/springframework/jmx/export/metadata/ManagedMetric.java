/*     */ package org.springframework.jmx.export.metadata;
/*     */ 
/*     */ import org.springframework.jmx.support.MetricType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ManagedMetric
/*     */   extends AbstractJmxAttribute
/*     */ {
/*     */   @Nullable
/*     */   private String category;
/*     */   @Nullable
/*     */   private String displayName;
/*  40 */   private MetricType metricType = MetricType.GAUGE;
/*     */   
/*  42 */   private int persistPeriod = -1;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String persistPolicy;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String unit;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCategory(@Nullable String category) {
/*  55 */     this.category = category;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCategory() {
/*  63 */     return this.category;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisplayName(@Nullable String displayName) {
/*  70 */     this.displayName = displayName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDisplayName() {
/*  78 */     return this.displayName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricType(MetricType metricType) {
/*  85 */     Assert.notNull(metricType, "MetricType must not be null");
/*  86 */     this.metricType = metricType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetricType getMetricType() {
/*  93 */     return this.metricType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPersistPeriod(int persistPeriod) {
/* 100 */     this.persistPeriod = persistPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPersistPeriod() {
/* 107 */     return this.persistPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPersistPolicy(@Nullable String persistPolicy) {
/* 114 */     this.persistPolicy = persistPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPersistPolicy() {
/* 122 */     return this.persistPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnit(@Nullable String unit) {
/* 129 */     this.unit = unit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getUnit() {
/* 137 */     return this.unit;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/metadata/ManagedMetric.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */