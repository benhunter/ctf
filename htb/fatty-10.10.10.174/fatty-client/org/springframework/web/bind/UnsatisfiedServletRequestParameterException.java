/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnsatisfiedServletRequestParameterException
/*     */   extends ServletRequestBindingException
/*     */ {
/*     */   private final List<String[]> paramConditions;
/*     */   private final Map<String, String[]> actualParams;
/*     */   
/*     */   public UnsatisfiedServletRequestParameterException(String[] paramConditions, Map<String, String[]> actualParams) {
/*  51 */     super("");
/*  52 */     this.paramConditions = Arrays.asList(new String[][] { paramConditions });
/*  53 */     this.actualParams = actualParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsatisfiedServletRequestParameterException(List<String[]> paramConditions, Map<String, String[]> actualParams) {
/*  65 */     super("");
/*  66 */     Assert.notEmpty(paramConditions, "Parameter conditions must not be empty");
/*  67 */     this.paramConditions = paramConditions;
/*  68 */     this.actualParams = actualParams;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  74 */     StringBuilder sb = new StringBuilder("Parameter conditions ");
/*  75 */     int i = 0;
/*  76 */     for (String[] conditions : this.paramConditions) {
/*  77 */       if (i > 0) {
/*  78 */         sb.append(" OR ");
/*     */       }
/*  80 */       sb.append("\"");
/*  81 */       sb.append(StringUtils.arrayToDelimitedString((Object[])conditions, ", "));
/*  82 */       sb.append("\"");
/*  83 */       i++;
/*     */     } 
/*  85 */     sb.append(" not met for actual request parameters: ");
/*  86 */     sb.append(requestParameterMapToString(this.actualParams));
/*  87 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String[] getParamConditions() {
/*  96 */     return this.paramConditions.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<String[]> getParamConditionGroups() {
/* 105 */     return this.paramConditions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Map<String, String[]> getActualParams() {
/* 113 */     return this.actualParams;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String requestParameterMapToString(Map<String, String[]> actualParams) {
/* 118 */     StringBuilder result = new StringBuilder();
/* 119 */     for (Iterator<Map.Entry<String, String[]>> it = actualParams.entrySet().iterator(); it.hasNext(); ) {
/* 120 */       Map.Entry<String, String[]> entry = it.next();
/* 121 */       result.append(entry.getKey()).append('=').append(ObjectUtils.nullSafeToString((Object[])entry.getValue()));
/* 122 */       if (it.hasNext()) {
/* 123 */         result.append(", ");
/*     */       }
/*     */     } 
/* 126 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/UnsatisfiedServletRequestParameterException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */