/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.NamespaceContext;
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
/*     */ public class SimpleNamespaceContext
/*     */   implements NamespaceContext
/*     */ {
/*  42 */   private final Map<String, String> prefixToNamespaceUri = new HashMap<>();
/*     */   
/*  44 */   private final Map<String, Set<String>> namespaceUriToPrefixes = new HashMap<>();
/*     */   
/*  46 */   private String defaultNamespaceUri = "";
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  51 */     Assert.notNull(prefix, "No prefix given");
/*  52 */     if ("xml".equals(prefix)) {
/*  53 */       return "http://www.w3.org/XML/1998/namespace";
/*     */     }
/*  55 */     if ("xmlns".equals(prefix)) {
/*  56 */       return "http://www.w3.org/2000/xmlns/";
/*     */     }
/*  58 */     if ("".equals(prefix)) {
/*  59 */       return this.defaultNamespaceUri;
/*     */     }
/*  61 */     if (this.prefixToNamespaceUri.containsKey(prefix)) {
/*  62 */       return this.prefixToNamespaceUri.get(prefix);
/*     */     }
/*  64 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPrefix(String namespaceUri) {
/*  70 */     Set<String> prefixes = getPrefixesSet(namespaceUri);
/*  71 */     return !prefixes.isEmpty() ? prefixes.iterator().next() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getPrefixes(String namespaceUri) {
/*  76 */     return getPrefixesSet(namespaceUri).iterator();
/*     */   }
/*     */   
/*     */   private Set<String> getPrefixesSet(String namespaceUri) {
/*  80 */     Assert.notNull(namespaceUri, "No namespaceUri given");
/*  81 */     if (this.defaultNamespaceUri.equals(namespaceUri)) {
/*  82 */       return Collections.singleton("");
/*     */     }
/*  84 */     if ("http://www.w3.org/XML/1998/namespace".equals(namespaceUri)) {
/*  85 */       return Collections.singleton("xml");
/*     */     }
/*  87 */     if ("http://www.w3.org/2000/xmlns/".equals(namespaceUri)) {
/*  88 */       return Collections.singleton("xmlns");
/*     */     }
/*     */     
/*  91 */     Set<String> prefixes = this.namespaceUriToPrefixes.get(namespaceUri);
/*  92 */     return (prefixes != null) ? Collections.<String>unmodifiableSet(prefixes) : Collections.<String>emptySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindings(Map<String, String> bindings) {
/* 102 */     bindings.forEach(this::bindNamespaceUri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindDefaultNamespaceUri(String namespaceUri) {
/* 110 */     bindNamespaceUri("", namespaceUri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindNamespaceUri(String prefix, String namespaceUri) {
/* 119 */     Assert.notNull(prefix, "No prefix given");
/* 120 */     Assert.notNull(namespaceUri, "No namespaceUri given");
/* 121 */     if ("".equals(prefix)) {
/* 122 */       this.defaultNamespaceUri = namespaceUri;
/*     */     } else {
/*     */       
/* 125 */       this.prefixToNamespaceUri.put(prefix, namespaceUri);
/*     */       
/* 127 */       Set<String> prefixes = this.namespaceUriToPrefixes.computeIfAbsent(namespaceUri, k -> new LinkedHashSet());
/* 128 */       prefixes.add(prefix);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBinding(@Nullable String prefix) {
/* 137 */     if ("".equals(prefix)) {
/* 138 */       this.defaultNamespaceUri = "";
/*     */     }
/* 140 */     else if (prefix != null) {
/* 141 */       String namespaceUri = this.prefixToNamespaceUri.remove(prefix);
/* 142 */       if (namespaceUri != null) {
/* 143 */         Set<String> prefixes = this.namespaceUriToPrefixes.get(namespaceUri);
/* 144 */         if (prefixes != null) {
/* 145 */           prefixes.remove(prefix);
/* 146 */           if (prefixes.isEmpty()) {
/* 147 */             this.namespaceUriToPrefixes.remove(namespaceUri);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 158 */     this.prefixToNamespaceUri.clear();
/* 159 */     this.namespaceUriToPrefixes.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<String> getBoundPrefixes() {
/* 166 */     return this.prefixToNamespaceUri.keySet().iterator();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/SimpleNamespaceContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */