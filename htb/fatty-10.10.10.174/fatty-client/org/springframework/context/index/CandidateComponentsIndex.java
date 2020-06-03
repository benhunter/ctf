/*     */ package org.springframework.context.index;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class CandidateComponentsIndex
/*     */ {
/*  51 */   private static final AntPathMatcher pathMatcher = new AntPathMatcher(".");
/*     */   
/*     */   private final MultiValueMap<String, Entry> index;
/*     */ 
/*     */   
/*     */   CandidateComponentsIndex(List<Properties> content) {
/*  57 */     this.index = parseIndex(content);
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
/*     */   public Set<String> getCandidateTypes(String basePackage, String stereotype) {
/*  69 */     List<Entry> candidates = (List<Entry>)this.index.get(stereotype);
/*  70 */     if (candidates != null) {
/*  71 */       return (Set<String>)candidates.parallelStream()
/*  72 */         .filter(t -> t.match(basePackage))
/*  73 */         .map(t -> t.type)
/*  74 */         .collect(Collectors.toSet());
/*     */     }
/*  76 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   private static MultiValueMap<String, Entry> parseIndex(List<Properties> content) {
/*  80 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/*  81 */     for (Iterator<Properties> iterator = content.iterator(); iterator.hasNext(); ) { Properties entry = iterator.next();
/*  82 */       entry.forEach((type, values) -> {
/*     */             String[] stereotypes = ((String)values).split(",");
/*     */             for (String stereotype : stereotypes) {
/*     */               index.add(stereotype, new Entry((String)type));
/*     */             }
/*     */           }); }
/*     */     
/*  89 */     return (MultiValueMap<String, Entry>)linkedMultiValueMap;
/*     */   }
/*     */   
/*     */   private static class Entry {
/*     */     private final String type;
/*     */     private final String packageName;
/*     */     
/*     */     Entry(String type) {
/*  97 */       this.type = type;
/*  98 */       this.packageName = ClassUtils.getPackageName(type);
/*     */     }
/*     */     
/*     */     public boolean match(String basePackage) {
/* 102 */       if (CandidateComponentsIndex.pathMatcher.isPattern(basePackage)) {
/* 103 */         return CandidateComponentsIndex.pathMatcher.match(basePackage, this.packageName);
/*     */       }
/*     */       
/* 106 */       return this.type.startsWith(basePackage);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/index/CandidateComponentsIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */