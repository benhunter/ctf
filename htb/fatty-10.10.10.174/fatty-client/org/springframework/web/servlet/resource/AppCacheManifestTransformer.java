/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Scanner;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.DigestUtils;
/*     */ import org.springframework.util.FileCopyUtils;
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
/*     */ public class AppCacheManifestTransformer
/*     */   extends ResourceTransformerSupport
/*     */ {
/*  68 */   private static final Collection<String> MANIFEST_SECTION_HEADERS = Arrays.asList(new String[] { "CACHE MANIFEST", "NETWORK:", "FALLBACK:", "CACHE:" });
/*     */   
/*     */   private static final String MANIFEST_HEADER = "CACHE MANIFEST";
/*     */   
/*     */   private static final String CACHE_HEADER = "CACHE:";
/*     */   
/*  74 */   private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */   
/*  76 */   private static final Log logger = LogFactory.getLog(AppCacheManifestTransformer.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final String fileExtension;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppCacheManifestTransformer() {
/*  86 */     this("appcache");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppCacheManifestTransformer(String fileExtension) {
/*  94 */     this.fileExtension = fileExtension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain chain) throws IOException {
/* 102 */     resource = chain.transform(request, resource);
/* 103 */     if (!this.fileExtension.equals(StringUtils.getFilenameExtension(resource.getFilename()))) {
/* 104 */       return resource;
/*     */     }
/*     */     
/* 107 */     byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
/* 108 */     String content = new String(bytes, DEFAULT_CHARSET);
/*     */     
/* 110 */     if (!content.startsWith("CACHE MANIFEST")) {
/* 111 */       if (logger.isTraceEnabled()) {
/* 112 */         logger.trace("Skipping " + resource + ": Manifest does not start with 'CACHE MANIFEST'");
/*     */       }
/* 114 */       return resource;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     Scanner scanner = new Scanner(content);
/* 119 */     LineInfo previous = null;
/* 120 */     LineAggregator aggregator = new LineAggregator(resource, content);
/*     */     
/* 122 */     while (scanner.hasNext()) {
/* 123 */       String line = scanner.nextLine();
/* 124 */       LineInfo current = new LineInfo(line, previous);
/* 125 */       LineOutput lineOutput = processLine(current, request, resource, chain);
/* 126 */       aggregator.add(lineOutput);
/* 127 */       previous = current;
/*     */     } 
/*     */     
/* 130 */     return (Resource)aggregator.createResource();
/*     */   }
/*     */   
/*     */   private static byte[] getResourceBytes(Resource resource) throws IOException {
/* 134 */     return FileCopyUtils.copyToByteArray(resource.getInputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LineOutput processLine(LineInfo info, HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) {
/* 140 */     if (!info.isLink()) {
/* 141 */       return new LineOutput(info.getLine(), null);
/*     */     }
/*     */ 
/*     */     
/* 145 */     Resource appCacheResource = transformerChain.getResolverChain().resolveResource(null, info.getLine(), Collections.singletonList(resource));
/*     */     
/* 147 */     String path = info.getLine();
/* 148 */     String absolutePath = toAbsolutePath(path, request);
/* 149 */     String newPath = resolveUrlPath(absolutePath, request, resource, transformerChain);
/*     */     
/* 151 */     return new LineOutput((newPath != null) ? newPath : path, appCacheResource);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LineInfo
/*     */   {
/*     */     private final String line;
/*     */     
/*     */     private final boolean cacheSection;
/*     */     
/*     */     private final boolean link;
/*     */     
/*     */     public LineInfo(String line, @Nullable LineInfo previous) {
/* 164 */       this.line = line;
/* 165 */       this.cacheSection = initCacheSectionFlag(line, previous);
/* 166 */       this.link = iniLinkFlag(line, this.cacheSection);
/*     */     }
/*     */     
/*     */     private static boolean initCacheSectionFlag(String line, @Nullable LineInfo previousLine) {
/* 170 */       if (AppCacheManifestTransformer.MANIFEST_SECTION_HEADERS.contains(line.trim())) {
/* 171 */         return line.trim().equals("CACHE:");
/*     */       }
/* 173 */       if (previousLine != null) {
/* 174 */         return previousLine.isCacheSection();
/*     */       }
/* 176 */       throw new IllegalStateException("Manifest does not start with CACHE MANIFEST: " + line);
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean iniLinkFlag(String line, boolean isCacheSection) {
/* 181 */       return (isCacheSection && StringUtils.hasText(line) && !line.startsWith("#") && 
/* 182 */         !line.startsWith("//") && !hasScheme(line));
/*     */     }
/*     */     
/*     */     private static boolean hasScheme(String line) {
/* 186 */       int index = line.indexOf(':');
/* 187 */       return (line.startsWith("//") || (index > 0 && !line.substring(0, index).contains("/")));
/*     */     }
/*     */     
/*     */     public String getLine() {
/* 191 */       return this.line;
/*     */     }
/*     */     
/*     */     public boolean isCacheSection() {
/* 195 */       return this.cacheSection;
/*     */     }
/*     */     
/*     */     public boolean isLink() {
/* 199 */       return this.link;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LineOutput
/*     */   {
/*     */     private final String line;
/*     */     
/*     */     @Nullable
/*     */     private final Resource resource;
/*     */     
/*     */     public LineOutput(String line, @Nullable Resource resource) {
/* 212 */       this.line = line;
/* 213 */       this.resource = resource;
/*     */     }
/*     */     
/*     */     public String getLine() {
/* 217 */       return this.line;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Resource getResource() {
/* 222 */       return this.resource;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LineAggregator
/*     */   {
/* 229 */     private final StringWriter writer = new StringWriter();
/*     */     
/*     */     private final ByteArrayOutputStream baos;
/*     */     
/*     */     private final Resource resource;
/*     */     
/*     */     public LineAggregator(Resource resource, String content) {
/* 236 */       this.resource = resource;
/* 237 */       this.baos = new ByteArrayOutputStream(content.length());
/*     */     }
/*     */     
/*     */     public void add(AppCacheManifestTransformer.LineOutput lineOutput) throws IOException {
/* 241 */       this.writer.write(lineOutput.getLine() + "\n");
/*     */ 
/*     */       
/* 244 */       byte[] bytes = (lineOutput.getResource() != null) ? DigestUtils.md5Digest(AppCacheManifestTransformer.getResourceBytes(lineOutput.getResource())) : lineOutput.getLine().getBytes(AppCacheManifestTransformer.DEFAULT_CHARSET);
/* 245 */       this.baos.write(bytes);
/*     */     }
/*     */     
/*     */     public TransformedResource createResource() {
/* 249 */       String hash = DigestUtils.md5DigestAsHex(this.baos.toByteArray());
/* 250 */       this.writer.write("\n# Hash: " + hash);
/* 251 */       byte[] bytes = this.writer.toString().getBytes(AppCacheManifestTransformer.DEFAULT_CHARSET);
/* 252 */       return new TransformedResource(this.resource, bytes);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/AppCacheManifestTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */