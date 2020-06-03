/*     */ package org.springframework.web.multipart.commons;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.fileupload.FileItem;
/*     */ import org.apache.commons.fileupload.FileItemFactory;
/*     */ import org.apache.commons.fileupload.FileUpload;
/*     */ import org.apache.commons.fileupload.disk.DiskFileItemFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CommonsFileUploadSupport
/*     */ {
/*  59 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private final DiskFileItemFactory fileItemFactory;
/*     */ 
/*     */   
/*     */   private final FileUpload fileUpload;
/*     */ 
/*     */   
/*     */   private boolean uploadTempDirSpecified = false;
/*     */ 
/*     */   
/*     */   private boolean preserveFilename = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonsFileUploadSupport() {
/*  77 */     this.fileItemFactory = newFileItemFactory();
/*  78 */     this.fileUpload = newFileUpload((FileItemFactory)getFileItemFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DiskFileItemFactory getFileItemFactory() {
/*  88 */     return this.fileItemFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileUpload getFileUpload() {
/*  97 */     return this.fileUpload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxUploadSize(long maxUploadSize) {
/* 107 */     this.fileUpload.setSizeMax(maxUploadSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxUploadSizePerFile(long maxUploadSizePerFile) {
/* 118 */     this.fileUpload.setFileSizeMax(maxUploadSizePerFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxInMemorySize(int maxInMemorySize) {
/* 129 */     this.fileItemFactory.setSizeThreshold(maxInMemorySize);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultEncoding(String defaultEncoding) {
/* 147 */     this.fileUpload.setHeaderEncoding(defaultEncoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultEncoding() {
/* 155 */     String encoding = getFileUpload().getHeaderEncoding();
/* 156 */     if (encoding == null) {
/* 157 */       encoding = "ISO-8859-1";
/*     */     }
/* 159 */     return encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUploadTempDir(Resource uploadTempDir) throws IOException {
/* 168 */     if (!uploadTempDir.exists() && !uploadTempDir.getFile().mkdirs()) {
/* 169 */       throw new IllegalArgumentException("Given uploadTempDir [" + uploadTempDir + "] could not be created");
/*     */     }
/* 171 */     this.fileItemFactory.setRepository(uploadTempDir.getFile());
/* 172 */     this.uploadTempDirSpecified = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUploadTempDirSpecified() {
/* 180 */     return this.uploadTempDirSpecified;
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
/*     */   public void setPreserveFilename(boolean preserveFilename) {
/* 194 */     this.preserveFilename = preserveFilename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DiskFileItemFactory newFileItemFactory() {
/* 205 */     return new DiskFileItemFactory();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileUpload prepareFileUpload(@Nullable String encoding) {
/* 226 */     FileUpload fileUpload = getFileUpload();
/* 227 */     FileUpload actualFileUpload = fileUpload;
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (encoding != null && !encoding.equals(fileUpload.getHeaderEncoding())) {
/* 232 */       actualFileUpload = newFileUpload((FileItemFactory)getFileItemFactory());
/* 233 */       actualFileUpload.setSizeMax(fileUpload.getSizeMax());
/* 234 */       actualFileUpload.setFileSizeMax(fileUpload.getFileSizeMax());
/* 235 */       actualFileUpload.setHeaderEncoding(encoding);
/*     */     } 
/*     */     
/* 238 */     return actualFileUpload;
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
/*     */   protected MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding) {
/* 250 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 251 */     Map<String, String[]> multipartParameters = (Map)new HashMap<>();
/* 252 */     Map<String, String> multipartParameterContentTypes = new HashMap<>();
/*     */ 
/*     */     
/* 255 */     for (FileItem fileItem : fileItems) {
/* 256 */       if (fileItem.isFormField()) {
/*     */         
/* 258 */         String value, partEncoding = determineEncoding(fileItem.getContentType(), encoding);
/*     */         try {
/* 260 */           value = fileItem.getString(partEncoding);
/*     */         }
/* 262 */         catch (UnsupportedEncodingException ex) {
/* 263 */           if (this.logger.isWarnEnabled()) {
/* 264 */             this.logger.warn("Could not decode multipart item '" + fileItem.getFieldName() + "' with encoding '" + partEncoding + "': using platform default");
/*     */           }
/*     */           
/* 267 */           value = fileItem.getString();
/*     */         } 
/* 269 */         String[] curParam = multipartParameters.get(fileItem.getFieldName());
/* 270 */         if (curParam == null) {
/*     */           
/* 272 */           multipartParameters.put(fileItem.getFieldName(), new String[] { value });
/*     */         }
/*     */         else {
/*     */           
/* 276 */           String[] newParam = StringUtils.addStringToArray(curParam, value);
/* 277 */           multipartParameters.put(fileItem.getFieldName(), newParam);
/*     */         } 
/* 279 */         multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
/*     */         
/*     */         continue;
/*     */       } 
/* 283 */       CommonsMultipartFile file = createMultipartFile(fileItem);
/* 284 */       linkedMultiValueMap.add(file.getName(), file);
/* 285 */       LogFormatUtils.traceDebug(this.logger, traceOn -> "Part '" + file.getName() + "', size " + file.getSize() + " bytes, filename='" + file.getOriginalFilename() + "'" + (traceOn.booleanValue() ? (", storage=" + file.getStorageDescription()) : ""));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     return new MultipartParsingResult((MultiValueMap<String, MultipartFile>)linkedMultiValueMap, multipartParameters, multipartParameterContentTypes);
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
/*     */   protected CommonsMultipartFile createMultipartFile(FileItem fileItem) {
/* 304 */     CommonsMultipartFile multipartFile = new CommonsMultipartFile(fileItem);
/* 305 */     multipartFile.setPreserveFilename(this.preserveFilename);
/* 306 */     return multipartFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles) {
/* 317 */     for (List<MultipartFile> files : (Iterable<List<MultipartFile>>)multipartFiles.values()) {
/* 318 */       for (MultipartFile file : files) {
/* 319 */         if (file instanceof CommonsMultipartFile) {
/* 320 */           CommonsMultipartFile cmf = (CommonsMultipartFile)file;
/* 321 */           cmf.getFileItem().delete();
/* 322 */           LogFormatUtils.traceDebug(this.logger, traceOn -> "Cleaning up part '" + cmf.getName() + "', filename '" + cmf.getOriginalFilename() + "'" + (traceOn.booleanValue() ? (", stored " + cmf.getStorageDescription()) : ""));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String determineEncoding(String contentTypeHeader, String defaultEncoding) {
/* 332 */     if (!StringUtils.hasText(contentTypeHeader)) {
/* 333 */       return defaultEncoding;
/*     */     }
/* 335 */     MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
/* 336 */     Charset charset = contentType.getCharset();
/* 337 */     return (charset != null) ? charset.name() : defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract FileUpload newFileUpload(FileItemFactory paramFileItemFactory);
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class MultipartParsingResult
/*     */   {
/*     */     private final MultiValueMap<String, MultipartFile> multipartFiles;
/*     */     
/*     */     private final Map<String, String[]> multipartParameters;
/*     */     
/*     */     private final Map<String, String> multipartParameterContentTypes;
/*     */ 
/*     */     
/*     */     public MultipartParsingResult(MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {
/* 356 */       this.multipartFiles = mpFiles;
/* 357 */       this.multipartParameters = mpParams;
/* 358 */       this.multipartParameterContentTypes = mpParamContentTypes;
/*     */     }
/*     */     
/*     */     public MultiValueMap<String, MultipartFile> getMultipartFiles() {
/* 362 */       return this.multipartFiles;
/*     */     }
/*     */     
/*     */     public Map<String, String[]> getMultipartParameters() {
/* 366 */       return this.multipartParameters;
/*     */     }
/*     */     
/*     */     public Map<String, String> getMultipartParameterContentTypes() {
/* 370 */       return this.multipartParameterContentTypes;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/commons/CommonsFileUploadSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */