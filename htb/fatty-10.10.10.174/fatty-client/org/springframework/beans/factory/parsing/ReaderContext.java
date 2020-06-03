/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import org.springframework.core.io.Resource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReaderContext
/*     */ {
/*     */   private final Resource resource;
/*     */   private final ProblemReporter problemReporter;
/*     */   private final ReaderEventListener eventListener;
/*     */   private final SourceExtractor sourceExtractor;
/*     */   
/*     */   public ReaderContext(Resource resource, ProblemReporter problemReporter, ReaderEventListener eventListener, SourceExtractor sourceExtractor) {
/*  51 */     this.resource = resource;
/*  52 */     this.problemReporter = problemReporter;
/*  53 */     this.eventListener = eventListener;
/*  54 */     this.sourceExtractor = sourceExtractor;
/*     */   }
/*     */   
/*     */   public final Resource getResource() {
/*  58 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, @Nullable Object source) {
/*  68 */     fatal(message, source, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, @Nullable Object source, @Nullable Throwable cause) {
/*  75 */     fatal(message, source, null, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, @Nullable Object source, @Nullable ParseState parseState) {
/*  82 */     fatal(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatal(String message, @Nullable Object source, @Nullable ParseState parseState, @Nullable Throwable cause) {
/*  89 */     Location location = new Location(getResource(), source);
/*  90 */     this.problemReporter.fatal(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, @Nullable Object source) {
/*  97 */     error(message, source, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, @Nullable Object source, @Nullable Throwable cause) {
/* 104 */     error(message, source, null, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, @Nullable Object source, @Nullable ParseState parseState) {
/* 111 */     error(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, @Nullable Object source, @Nullable ParseState parseState, @Nullable Throwable cause) {
/* 118 */     Location location = new Location(getResource(), source);
/* 119 */     this.problemReporter.error(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, @Nullable Object source) {
/* 126 */     warning(message, source, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, @Nullable Object source, @Nullable Throwable cause) {
/* 133 */     warning(message, source, null, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, @Nullable Object source, @Nullable ParseState parseState) {
/* 140 */     warning(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, @Nullable Object source, @Nullable ParseState parseState, @Nullable Throwable cause) {
/* 147 */     Location location = new Location(getResource(), source);
/* 148 */     this.problemReporter.warning(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireDefaultsRegistered(DefaultsDefinition defaultsDefinition) {
/* 158 */     this.eventListener.defaultsRegistered(defaultsDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireComponentRegistered(ComponentDefinition componentDefinition) {
/* 165 */     this.eventListener.componentRegistered(componentDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireAliasRegistered(String beanName, String alias, @Nullable Object source) {
/* 172 */     this.eventListener.aliasRegistered(new AliasDefinition(beanName, alias, source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireImportProcessed(String importedResource, @Nullable Object source) {
/* 179 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireImportProcessed(String importedResource, Resource[] actualResources, @Nullable Object source) {
/* 186 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, actualResources, source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceExtractor getSourceExtractor() {
/* 196 */     return this.sourceExtractor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object extractSource(Object sourceCandidate) {
/* 208 */     return this.sourceExtractor.extractSource(sourceCandidate, this.resource);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/ReaderContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */