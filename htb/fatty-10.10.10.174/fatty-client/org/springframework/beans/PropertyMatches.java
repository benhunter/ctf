/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertyMatches
/*     */ {
/*     */   public static final int DEFAULT_MAX_DISTANCE = 2;
/*     */   private final String propertyName;
/*     */   private final String[] possibleMatches;
/*     */   
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass) {
/*  57 */     return forProperty(propertyName, beanClass, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass, int maxDistance) {
/*  67 */     return new BeanPropertyMatches(propertyName, beanClass, maxDistance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass) {
/*  76 */     return forField(propertyName, beanClass, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass, int maxDistance) {
/*  86 */     return new FieldPropertyMatches(propertyName, beanClass, maxDistance);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private PropertyMatches(String propertyName, String[] possibleMatches) {
/* 101 */     this.propertyName = propertyName;
/* 102 */     this.possibleMatches = possibleMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPropertyName() {
/* 110 */     return this.propertyName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getPossibleMatches() {
/* 117 */     return this.possibleMatches;
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
/*     */   
/*     */   protected void appendHintMessage(StringBuilder msg) {
/* 130 */     msg.append("Did you mean ");
/* 131 */     for (int i = 0; i < this.possibleMatches.length; i++) {
/* 132 */       msg.append('\'');
/* 133 */       msg.append(this.possibleMatches[i]);
/* 134 */       if (i < this.possibleMatches.length - 2) {
/* 135 */         msg.append("', ");
/*     */       }
/* 137 */       else if (i == this.possibleMatches.length - 2) {
/* 138 */         msg.append("', or ");
/*     */       } 
/*     */     } 
/* 141 */     msg.append("'?");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int calculateStringDistance(String s1, String s2) {
/* 152 */     if (s1.isEmpty()) {
/* 153 */       return s2.length();
/*     */     }
/* 155 */     if (s2.isEmpty()) {
/* 156 */       return s1.length();
/*     */     }
/*     */     
/* 159 */     int[][] d = new int[s1.length() + 1][s2.length() + 1];
/* 160 */     for (int k = 0; k <= s1.length(); k++) {
/* 161 */       d[k][0] = k;
/*     */     }
/* 163 */     for (int j = 0; j <= s2.length(); j++) {
/* 164 */       d[0][j] = j;
/*     */     }
/*     */     
/* 167 */     for (int i = 1; i <= s1.length(); i++) {
/* 168 */       char c1 = s1.charAt(i - 1);
/* 169 */       for (int m = 1; m <= s2.length(); m++) {
/*     */         int cost;
/* 171 */         char c2 = s2.charAt(m - 1);
/* 172 */         if (c1 == c2) {
/* 173 */           cost = 0;
/*     */         } else {
/*     */           
/* 176 */           cost = 1;
/*     */         } 
/* 178 */         d[i][m] = Math.min(Math.min(d[i - 1][m] + 1, d[i][m - 1] + 1), d[i - 1][m - 1] + cost);
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     return d[s1.length()][s2.length()];
/*     */   }
/*     */   
/*     */   public abstract String buildErrorMessage();
/*     */   
/*     */   private static class BeanPropertyMatches
/*     */     extends PropertyMatches
/*     */   {
/*     */     public BeanPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance) {
/* 191 */       super(propertyName, 
/* 192 */           calculateMatches(propertyName, BeanUtils.getPropertyDescriptors(beanClass), maxDistance));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static String[] calculateMatches(String name, PropertyDescriptor[] descriptors, int maxDistance) {
/* 203 */       List<String> candidates = new ArrayList<>();
/* 204 */       for (PropertyDescriptor pd : descriptors) {
/* 205 */         if (pd.getWriteMethod() != null) {
/* 206 */           String possibleAlternative = pd.getName();
/* 207 */           if (PropertyMatches.calculateStringDistance(name, possibleAlternative) <= maxDistance) {
/* 208 */             candidates.add(possibleAlternative);
/*     */           }
/*     */         } 
/*     */       } 
/* 212 */       Collections.sort(candidates);
/* 213 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */ 
/*     */     
/*     */     public String buildErrorMessage() {
/* 218 */       StringBuilder msg = new StringBuilder(160);
/* 219 */       msg.append("Bean property '").append(getPropertyName()).append("' is not writable or has an invalid setter method. ");
/*     */       
/* 221 */       if (!ObjectUtils.isEmpty((Object[])getPossibleMatches())) {
/* 222 */         appendHintMessage(msg);
/*     */       } else {
/*     */         
/* 225 */         msg.append("Does the parameter type of the setter match the return type of the getter?");
/*     */       } 
/* 227 */       return msg.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FieldPropertyMatches
/*     */     extends PropertyMatches
/*     */   {
/*     */     public FieldPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance) {
/* 235 */       super(propertyName, calculateMatches(propertyName, beanClass, maxDistance));
/*     */     }
/*     */     
/*     */     private static String[] calculateMatches(String name, Class<?> clazz, int maxDistance) {
/* 239 */       List<String> candidates = new ArrayList<>();
/* 240 */       ReflectionUtils.doWithFields(clazz, field -> {
/*     */             String possibleAlternative = field.getName();
/*     */             if (PropertyMatches.calculateStringDistance(name, possibleAlternative) <= maxDistance) {
/*     */               candidates.add(possibleAlternative);
/*     */             }
/*     */           });
/* 246 */       Collections.sort(candidates);
/* 247 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */ 
/*     */     
/*     */     public String buildErrorMessage() {
/* 252 */       StringBuilder msg = new StringBuilder(80);
/* 253 */       msg.append("Bean property '").append(getPropertyName()).append("' has no matching field.");
/* 254 */       if (!ObjectUtils.isEmpty((Object[])getPossibleMatches())) {
/* 255 */         msg.append(' ');
/* 256 */         appendHintMessage(msg);
/*     */       } 
/* 258 */       return msg.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyMatches.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */