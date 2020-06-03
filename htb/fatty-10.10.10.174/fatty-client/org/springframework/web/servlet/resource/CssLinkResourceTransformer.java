/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class CssLinkResourceTransformer
/*     */   extends ResourceTransformerSupport
/*     */ {
/*  52 */   private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */   
/*  54 */   private static final Log logger = LogFactory.getLog(CssLinkResourceTransformer.class);
/*     */   
/*  56 */   private final List<LinkParser> linkParsers = new ArrayList<>(2);
/*     */ 
/*     */   
/*     */   public CssLinkResourceTransformer() {
/*  60 */     this.linkParsers.add(new ImportStatementLinkParser());
/*  61 */     this.linkParsers.add(new UrlFunctionLinkParser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
/*  70 */     resource = transformerChain.transform(request, resource);
/*     */     
/*  72 */     String filename = resource.getFilename();
/*  73 */     if (!"css".equals(StringUtils.getFilenameExtension(filename)) || resource instanceof EncodedResourceResolver.EncodedResource || resource instanceof GzipResourceResolver.GzippedResource)
/*     */     {
/*     */       
/*  76 */       return resource;
/*     */     }
/*     */     
/*  79 */     byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
/*  80 */     String content = new String(bytes, DEFAULT_CHARSET);
/*     */     
/*  82 */     SortedSet<ContentChunkInfo> links = new TreeSet<>();
/*  83 */     for (LinkParser parser : this.linkParsers) {
/*  84 */       parser.parse(content, links);
/*     */     }
/*     */     
/*  87 */     if (links.isEmpty()) {
/*  88 */       return resource;
/*     */     }
/*     */     
/*  91 */     int index = 0;
/*  92 */     StringWriter writer = new StringWriter();
/*  93 */     for (ContentChunkInfo linkContentChunkInfo : links) {
/*  94 */       writer.write(content.substring(index, linkContentChunkInfo.getStart()));
/*  95 */       String link = content.substring(linkContentChunkInfo.getStart(), linkContentChunkInfo.getEnd());
/*  96 */       String newLink = null;
/*  97 */       if (!hasScheme(link)) {
/*  98 */         String absolutePath = toAbsolutePath(link, request);
/*  99 */         newLink = resolveUrlPath(absolutePath, request, resource, transformerChain);
/*     */       } 
/* 101 */       writer.write((newLink != null) ? newLink : link);
/* 102 */       index = linkContentChunkInfo.getEnd();
/*     */     } 
/* 104 */     writer.write(content.substring(index));
/*     */     
/* 106 */     return (Resource)new TransformedResource(resource, writer.toString().getBytes(DEFAULT_CHARSET));
/*     */   }
/*     */   
/*     */   private boolean hasScheme(String link) {
/* 110 */     int schemeIndex = link.indexOf(':');
/* 111 */     return ((schemeIndex > 0 && !link.substring(0, schemeIndex).contains("/")) || link.indexOf("//") == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface LinkParser
/*     */   {
/*     */     void parse(String param1String, SortedSet<CssLinkResourceTransformer.ContentChunkInfo> param1SortedSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class AbstractLinkParser
/*     */     implements LinkParser
/*     */   {
/*     */     protected abstract String getKeyword();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void parse(String content, SortedSet<CssLinkResourceTransformer.ContentChunkInfo> result) {
/* 136 */       int position = 0;
/*     */       while (true) {
/* 138 */         position = content.indexOf(getKeyword(), position);
/* 139 */         if (position == -1) {
/*     */           return;
/*     */         }
/* 142 */         position += getKeyword().length();
/* 143 */         while (Character.isWhitespace(content.charAt(position))) {
/* 144 */           position++;
/*     */         }
/* 146 */         if (content.charAt(position) == '\'') {
/* 147 */           position = extractLink(position, "'", content, result); continue;
/*     */         } 
/* 149 */         if (content.charAt(position) == '"') {
/* 150 */           position = extractLink(position, "\"", content, result);
/*     */           continue;
/*     */         } 
/* 153 */         position = extractLink(position, content, result);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int extractLink(int index, String endKey, String content, SortedSet<CssLinkResourceTransformer.ContentChunkInfo> linksToAdd) {
/* 160 */       int start = index + 1;
/* 161 */       int end = content.indexOf(endKey, start);
/* 162 */       linksToAdd.add(new CssLinkResourceTransformer.ContentChunkInfo(start, end));
/* 163 */       return end + endKey.length();
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract int extractLink(int param1Int, String param1String, SortedSet<CssLinkResourceTransformer.ContentChunkInfo> param1SortedSet);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ImportStatementLinkParser
/*     */     extends AbstractLinkParser
/*     */   {
/*     */     private ImportStatementLinkParser() {}
/*     */ 
/*     */     
/*     */     protected String getKeyword() {
/* 178 */       return "@import";
/*     */     }
/*     */ 
/*     */     
/*     */     protected int extractLink(int index, String content, SortedSet<CssLinkResourceTransformer.ContentChunkInfo> linksToAdd) {
/* 183 */       if (!content.substring(index, index + 4).equals("url("))
/*     */       {
/*     */         
/* 186 */         if (CssLinkResourceTransformer.logger.isTraceEnabled())
/* 187 */           CssLinkResourceTransformer.logger.trace("Unexpected syntax for @import link at index " + index); 
/*     */       }
/* 189 */       return index;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UrlFunctionLinkParser
/*     */     extends AbstractLinkParser {
/*     */     private UrlFunctionLinkParser() {}
/*     */     
/*     */     protected String getKeyword() {
/* 198 */       return "url(";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int extractLink(int index, String content, SortedSet<CssLinkResourceTransformer.ContentChunkInfo> linksToAdd) {
/* 204 */       return extractLink(index - 1, ")", content, linksToAdd);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ContentChunkInfo
/*     */     implements Comparable<ContentChunkInfo>
/*     */   {
/*     */     private final int start;
/*     */     private final int end;
/*     */     
/*     */     ContentChunkInfo(int start, int end) {
/* 216 */       this.start = start;
/* 217 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int getStart() {
/* 221 */       return this.start;
/*     */     }
/*     */     
/*     */     public int getEnd() {
/* 225 */       return this.end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(ContentChunkInfo other) {
/* 230 */       return Integer.compare(this.start, other.start);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 235 */       if (this == other) {
/* 236 */         return true;
/*     */       }
/* 238 */       if (!(other instanceof ContentChunkInfo)) {
/* 239 */         return false;
/*     */       }
/* 241 */       ContentChunkInfo otherCci = (ContentChunkInfo)other;
/* 242 */       return (this.start == otherCci.start && this.end == otherCci.end);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 247 */       return this.start * 31 + this.end;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/CssLinkResourceTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */