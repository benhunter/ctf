/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultMessageCodesResolver
/*     */   implements MessageCodesResolver, Serializable
/*     */ {
/*     */   public static final String CODE_SEPARATOR = ".";
/*  99 */   private static final MessageCodeFormatter DEFAULT_FORMATTER = Format.PREFIX_ERROR_CODE;
/*     */ 
/*     */   
/* 102 */   private String prefix = "";
/*     */   
/* 104 */   private MessageCodeFormatter formatter = DEFAULT_FORMATTER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(@Nullable String prefix) {
/* 113 */     this.prefix = (prefix != null) ? prefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/* 121 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageCodeFormatter(@Nullable MessageCodeFormatter formatter) {
/* 131 */     this.formatter = (formatter != null) ? formatter : DEFAULT_FORMATTER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, String objectName) {
/* 137 */     return resolveMessageCodes(errorCode, objectName, "", null);
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
/*     */   public String[] resolveMessageCodes(String errorCode, String objectName, String field, @Nullable Class<?> fieldType) {
/* 151 */     Set<String> codeList = new LinkedHashSet<>();
/* 152 */     List<String> fieldList = new ArrayList<>();
/* 153 */     buildFieldList(field, fieldList);
/* 154 */     addCodes(codeList, errorCode, objectName, fieldList);
/* 155 */     int dotIndex = field.lastIndexOf('.');
/* 156 */     if (dotIndex != -1) {
/* 157 */       buildFieldList(field.substring(dotIndex + 1), fieldList);
/*     */     }
/* 159 */     addCodes(codeList, errorCode, null, fieldList);
/* 160 */     if (fieldType != null) {
/* 161 */       addCode(codeList, errorCode, null, fieldType.getName());
/*     */     }
/* 163 */     addCode(codeList, errorCode, null, null);
/* 164 */     return StringUtils.toStringArray(codeList);
/*     */   }
/*     */   
/*     */   private void addCodes(Collection<String> codeList, String errorCode, @Nullable String objectName, Iterable<String> fields) {
/* 168 */     for (String field : fields) {
/* 169 */       addCode(codeList, errorCode, objectName, field);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addCode(Collection<String> codeList, String errorCode, @Nullable String objectName, @Nullable String field) {
/* 174 */     codeList.add(postProcessMessageCode(this.formatter.format(errorCode, objectName, field)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildFieldList(String field, List<String> fieldList) {
/* 182 */     fieldList.add(field);
/* 183 */     String plainField = field;
/* 184 */     int keyIndex = plainField.lastIndexOf('[');
/* 185 */     while (keyIndex != -1) {
/* 186 */       int endKeyIndex = plainField.indexOf(']', keyIndex);
/* 187 */       if (endKeyIndex != -1) {
/* 188 */         plainField = plainField.substring(0, keyIndex) + plainField.substring(endKeyIndex + 1);
/* 189 */         fieldList.add(plainField);
/* 190 */         keyIndex = plainField.lastIndexOf('[');
/*     */         continue;
/*     */       } 
/* 193 */       keyIndex = -1;
/*     */     } 
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
/*     */   protected String postProcessMessageCode(String code) {
/* 206 */     return getPrefix() + code;
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
/*     */   public enum Format
/*     */     implements MessageCodeFormatter
/*     */   {
/* 221 */     PREFIX_ERROR_CODE
/*     */     {
/*     */       public String format(String errorCode, @Nullable String objectName, @Nullable String field) {
/* 224 */         return null.toDelimitedString(new String[] { errorCode, objectName, field
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             });
/*     */       }
/*     */     },
/* 232 */     POSTFIX_ERROR_CODE
/*     */     {
/*     */       public String format(String errorCode, @Nullable String objectName, @Nullable String field) {
/* 235 */         return null.toDelimitedString(new String[] { objectName, field, errorCode });
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static String toDelimitedString(String... elements) {
/* 245 */       StringBuilder rtn = new StringBuilder();
/* 246 */       for (String element : elements) {
/* 247 */         if (StringUtils.hasLength(element)) {
/* 248 */           rtn.append((rtn.length() == 0) ? "" : ".");
/* 249 */           rtn.append(element);
/*     */         } 
/*     */       } 
/* 252 */       return rtn.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/DefaultMessageCodesResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */