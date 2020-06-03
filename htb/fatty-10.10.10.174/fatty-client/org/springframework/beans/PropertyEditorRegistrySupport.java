/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Path;
/*     */ import java.time.ZoneId;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Currency;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
/*     */ import org.springframework.beans.propertyeditors.CharArrayPropertyEditor;
/*     */ import org.springframework.beans.propertyeditors.CharacterEditor;
/*     */ import org.springframework.beans.propertyeditors.CharsetEditor;
/*     */ import org.springframework.beans.propertyeditors.ClassArrayEditor;
/*     */ import org.springframework.beans.propertyeditors.ClassEditor;
/*     */ import org.springframework.beans.propertyeditors.CurrencyEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomBooleanEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomCollectionEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomMapEditor;
/*     */ import org.springframework.beans.propertyeditors.CustomNumberEditor;
/*     */ import org.springframework.beans.propertyeditors.FileEditor;
/*     */ import org.springframework.beans.propertyeditors.InputSourceEditor;
/*     */ import org.springframework.beans.propertyeditors.InputStreamEditor;
/*     */ import org.springframework.beans.propertyeditors.LocaleEditor;
/*     */ import org.springframework.beans.propertyeditors.PathEditor;
/*     */ import org.springframework.beans.propertyeditors.PatternEditor;
/*     */ import org.springframework.beans.propertyeditors.PropertiesEditor;
/*     */ import org.springframework.beans.propertyeditors.ReaderEditor;
/*     */ import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
/*     */ import org.springframework.beans.propertyeditors.TimeZoneEditor;
/*     */ import org.springframework.beans.propertyeditors.URIEditor;
/*     */ import org.springframework.beans.propertyeditors.URLEditor;
/*     */ import org.springframework.beans.propertyeditors.UUIDEditor;
/*     */ import org.springframework.beans.propertyeditors.ZoneIdEditor;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourceArrayPropertyEditor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyEditorRegistrySupport
/*     */   implements PropertyEditorRegistry
/*     */ {
/*     */   @Nullable
/*     */   private ConversionService conversionService;
/*     */   private boolean defaultEditorsActive = false;
/*     */   private boolean configValueEditorsActive = false;
/*     */   @Nullable
/*     */   private Map<Class<?>, PropertyEditor> defaultEditors;
/*     */   @Nullable
/*     */   private Map<Class<?>, PropertyEditor> overriddenDefaultEditors;
/*     */   @Nullable
/*     */   private Map<Class<?>, PropertyEditor> customEditors;
/*     */   @Nullable
/*     */   private Map<String, CustomEditorHolder> customEditorsForPath;
/*     */   @Nullable
/*     */   private Map<Class<?>, PropertyEditor> customEditorCache;
/*     */   
/*     */   public void setConversionService(@Nullable ConversionService conversionService) {
/* 122 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ConversionService getConversionService() {
/* 130 */     return this.conversionService;
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
/*     */   protected void registerDefaultEditors() {
/* 143 */     this.defaultEditorsActive = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void useConfigValueEditors() {
/* 154 */     this.configValueEditorsActive = true;
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
/*     */   public void overrideDefaultEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/* 167 */     if (this.overriddenDefaultEditors == null) {
/* 168 */       this.overriddenDefaultEditors = new HashMap<>();
/*     */     }
/* 170 */     this.overriddenDefaultEditors.put(requiredType, propertyEditor);
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
/*     */   public PropertyEditor getDefaultEditor(Class<?> requiredType) {
/* 182 */     if (!this.defaultEditorsActive) {
/* 183 */       return null;
/*     */     }
/* 185 */     if (this.overriddenDefaultEditors != null) {
/* 186 */       PropertyEditor editor = this.overriddenDefaultEditors.get(requiredType);
/* 187 */       if (editor != null) {
/* 188 */         return editor;
/*     */       }
/*     */     } 
/* 191 */     if (this.defaultEditors == null) {
/* 192 */       createDefaultEditors();
/*     */     }
/* 194 */     return this.defaultEditors.get(requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createDefaultEditors() {
/* 201 */     this.defaultEditors = new HashMap<>(64);
/*     */ 
/*     */ 
/*     */     
/* 205 */     this.defaultEditors.put(Charset.class, new CharsetEditor());
/* 206 */     this.defaultEditors.put(Class.class, new ClassEditor());
/* 207 */     this.defaultEditors.put(Class[].class, new ClassArrayEditor());
/* 208 */     this.defaultEditors.put(Currency.class, new CurrencyEditor());
/* 209 */     this.defaultEditors.put(File.class, new FileEditor());
/* 210 */     this.defaultEditors.put(InputStream.class, new InputStreamEditor());
/* 211 */     this.defaultEditors.put(InputSource.class, new InputSourceEditor());
/* 212 */     this.defaultEditors.put(Locale.class, new LocaleEditor());
/* 213 */     this.defaultEditors.put(Path.class, new PathEditor());
/* 214 */     this.defaultEditors.put(Pattern.class, new PatternEditor());
/* 215 */     this.defaultEditors.put(Properties.class, new PropertiesEditor());
/* 216 */     this.defaultEditors.put(Reader.class, new ReaderEditor());
/* 217 */     this.defaultEditors.put(Resource[].class, new ResourceArrayPropertyEditor());
/* 218 */     this.defaultEditors.put(TimeZone.class, new TimeZoneEditor());
/* 219 */     this.defaultEditors.put(URI.class, new URIEditor());
/* 220 */     this.defaultEditors.put(URL.class, new URLEditor());
/* 221 */     this.defaultEditors.put(UUID.class, new UUIDEditor());
/* 222 */     this.defaultEditors.put(ZoneId.class, new ZoneIdEditor());
/*     */ 
/*     */ 
/*     */     
/* 226 */     this.defaultEditors.put(Collection.class, new CustomCollectionEditor(Collection.class));
/* 227 */     this.defaultEditors.put(Set.class, new CustomCollectionEditor(Set.class));
/* 228 */     this.defaultEditors.put(SortedSet.class, new CustomCollectionEditor(SortedSet.class));
/* 229 */     this.defaultEditors.put(List.class, new CustomCollectionEditor(List.class));
/* 230 */     this.defaultEditors.put(SortedMap.class, new CustomMapEditor(SortedMap.class));
/*     */ 
/*     */     
/* 233 */     this.defaultEditors.put(byte[].class, new ByteArrayPropertyEditor());
/* 234 */     this.defaultEditors.put(char[].class, new CharArrayPropertyEditor());
/*     */ 
/*     */     
/* 237 */     this.defaultEditors.put(char.class, new CharacterEditor(false));
/* 238 */     this.defaultEditors.put(Character.class, new CharacterEditor(true));
/*     */ 
/*     */     
/* 241 */     this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
/* 242 */     this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));
/*     */ 
/*     */ 
/*     */     
/* 246 */     this.defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class, false));
/* 247 */     this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
/* 248 */     this.defaultEditors.put(short.class, new CustomNumberEditor(Short.class, false));
/* 249 */     this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));
/* 250 */     this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
/* 251 */     this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
/* 252 */     this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
/* 253 */     this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
/* 254 */     this.defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
/* 255 */     this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
/* 256 */     this.defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
/* 257 */     this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
/* 258 */     this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
/* 259 */     this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
/*     */ 
/*     */     
/* 262 */     if (this.configValueEditorsActive) {
/* 263 */       StringArrayPropertyEditor sae = new StringArrayPropertyEditor();
/* 264 */       this.defaultEditors.put(String[].class, sae);
/* 265 */       this.defaultEditors.put(short[].class, sae);
/* 266 */       this.defaultEditors.put(int[].class, sae);
/* 267 */       this.defaultEditors.put(long[].class, sae);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyDefaultEditorsTo(PropertyEditorRegistrySupport target) {
/* 276 */     target.defaultEditorsActive = this.defaultEditorsActive;
/* 277 */     target.configValueEditorsActive = this.configValueEditorsActive;
/* 278 */     target.defaultEditors = this.defaultEditors;
/* 279 */     target.overriddenDefaultEditors = this.overriddenDefaultEditors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/* 289 */     registerCustomEditor(requiredType, null, propertyEditor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(@Nullable Class<?> requiredType, @Nullable String propertyPath, PropertyEditor propertyEditor) {
/* 294 */     if (requiredType == null && propertyPath == null) {
/* 295 */       throw new IllegalArgumentException("Either requiredType or propertyPath is required");
/*     */     }
/* 297 */     if (propertyPath != null) {
/* 298 */       if (this.customEditorsForPath == null) {
/* 299 */         this.customEditorsForPath = new LinkedHashMap<>(16);
/*     */       }
/* 301 */       this.customEditorsForPath.put(propertyPath, new CustomEditorHolder(propertyEditor, requiredType));
/*     */     } else {
/*     */       
/* 304 */       if (this.customEditors == null) {
/* 305 */         this.customEditors = new LinkedHashMap<>(16);
/*     */       }
/* 307 */       this.customEditors.put(requiredType, propertyEditor);
/* 308 */       this.customEditorCache = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor findCustomEditor(@Nullable Class<?> requiredType, @Nullable String propertyPath) {
/* 315 */     Class<?> requiredTypeToUse = requiredType;
/* 316 */     if (propertyPath != null) {
/* 317 */       if (this.customEditorsForPath != null) {
/*     */         
/* 319 */         PropertyEditor editor = getCustomEditor(propertyPath, requiredType);
/* 320 */         if (editor == null) {
/* 321 */           List<String> strippedPaths = new ArrayList<>();
/* 322 */           addStrippedPropertyPaths(strippedPaths, "", propertyPath);
/* 323 */           for (Iterator<String> it = strippedPaths.iterator(); it.hasNext() && editor == null; ) {
/* 324 */             String strippedPath = it.next();
/* 325 */             editor = getCustomEditor(strippedPath, requiredType);
/*     */           } 
/*     */         } 
/* 328 */         if (editor != null) {
/* 329 */           return editor;
/*     */         }
/*     */       } 
/* 332 */       if (requiredType == null) {
/* 333 */         requiredTypeToUse = getPropertyType(propertyPath);
/*     */       }
/*     */     } 
/*     */     
/* 337 */     return getCustomEditor(requiredTypeToUse);
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
/*     */   public boolean hasCustomEditorForElement(@Nullable Class<?> elementType, @Nullable String propertyPath) {
/* 350 */     if (propertyPath != null && this.customEditorsForPath != null) {
/* 351 */       for (Map.Entry<String, CustomEditorHolder> entry : this.customEditorsForPath.entrySet()) {
/* 352 */         if (PropertyAccessorUtils.matchesProperty(entry.getKey(), propertyPath) && ((CustomEditorHolder)entry
/* 353 */           .getValue()).getPropertyEditor(elementType) != null) {
/* 354 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 359 */     return (elementType != null && this.customEditors != null && this.customEditors.containsKey(elementType));
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
/*     */   @Nullable
/*     */   protected Class<?> getPropertyType(String propertyPath) {
/* 375 */     return null;
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
/*     */   private PropertyEditor getCustomEditor(String propertyName, @Nullable Class<?> requiredType) {
/* 387 */     CustomEditorHolder holder = (this.customEditorsForPath != null) ? this.customEditorsForPath.get(propertyName) : null;
/* 388 */     return (holder != null) ? holder.getPropertyEditor(requiredType) : null;
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
/*     */   @Nullable
/*     */   private PropertyEditor getCustomEditor(@Nullable Class<?> requiredType) {
/* 401 */     if (requiredType == null || this.customEditors == null) {
/* 402 */       return null;
/*     */     }
/*     */     
/* 405 */     PropertyEditor editor = this.customEditors.get(requiredType);
/* 406 */     if (editor == null) {
/*     */       
/* 408 */       if (this.customEditorCache != null) {
/* 409 */         editor = this.customEditorCache.get(requiredType);
/*     */       }
/* 411 */       if (editor == null)
/*     */       {
/* 413 */         for (Iterator<Class<?>> it = this.customEditors.keySet().iterator(); it.hasNext() && editor == null; ) {
/* 414 */           Class<?> key = it.next();
/* 415 */           if (key.isAssignableFrom(requiredType)) {
/* 416 */             editor = this.customEditors.get(key);
/*     */ 
/*     */             
/* 419 */             if (this.customEditorCache == null) {
/* 420 */               this.customEditorCache = new HashMap<>();
/*     */             }
/* 422 */             this.customEditorCache.put(requiredType, editor);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 427 */     return editor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Class<?> guessPropertyTypeFromEditors(String propertyName) {
/* 438 */     if (this.customEditorsForPath != null) {
/* 439 */       CustomEditorHolder editorHolder = this.customEditorsForPath.get(propertyName);
/* 440 */       if (editorHolder == null) {
/* 441 */         List<String> strippedPaths = new ArrayList<>();
/* 442 */         addStrippedPropertyPaths(strippedPaths, "", propertyName);
/* 443 */         for (Iterator<String> it = strippedPaths.iterator(); it.hasNext() && editorHolder == null; ) {
/* 444 */           String strippedName = it.next();
/* 445 */           editorHolder = this.customEditorsForPath.get(strippedName);
/*     */         } 
/*     */       } 
/* 448 */       if (editorHolder != null) {
/* 449 */         return editorHolder.getRegisteredType();
/*     */       }
/*     */     } 
/* 452 */     return null;
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
/*     */   protected void copyCustomEditorsTo(PropertyEditorRegistry target, @Nullable String nestedProperty) {
/* 464 */     String actualPropertyName = (nestedProperty != null) ? PropertyAccessorUtils.getPropertyName(nestedProperty) : null;
/* 465 */     if (this.customEditors != null) {
/* 466 */       this.customEditors.forEach(target::registerCustomEditor);
/*     */     }
/* 468 */     if (this.customEditorsForPath != null) {
/* 469 */       this.customEditorsForPath.forEach((editorPath, editorHolder) -> {
/*     */             if (nestedProperty != null) {
/*     */               int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(editorPath);
/*     */               if (pos != -1) {
/*     */                 String editorNestedProperty = editorPath.substring(0, pos);
/*     */                 String editorNestedPath = editorPath.substring(pos + 1);
/*     */                 if (editorNestedProperty.equals(nestedProperty) || editorNestedProperty.equals(actualPropertyName)) {
/*     */                   target.registerCustomEditor(editorHolder.getRegisteredType(), editorNestedPath, editorHolder.getPropertyEditor());
/*     */                 }
/*     */               } 
/*     */             } else {
/*     */               target.registerCustomEditor(editorHolder.getRegisteredType(), editorPath, editorHolder.getPropertyEditor());
/*     */             } 
/*     */           });
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void addStrippedPropertyPaths(List<String> strippedPaths, String nestedPath, String propertyPath) {
/* 498 */     int startIndex = propertyPath.indexOf('[');
/* 499 */     if (startIndex != -1) {
/* 500 */       int endIndex = propertyPath.indexOf(']');
/* 501 */       if (endIndex != -1) {
/* 502 */         String prefix = propertyPath.substring(0, startIndex);
/* 503 */         String key = propertyPath.substring(startIndex, endIndex + 1);
/* 504 */         String suffix = propertyPath.substring(endIndex + 1, propertyPath.length());
/*     */         
/* 506 */         strippedPaths.add(nestedPath + prefix + suffix);
/*     */         
/* 508 */         addStrippedPropertyPaths(strippedPaths, nestedPath + prefix, suffix);
/*     */         
/* 510 */         addStrippedPropertyPaths(strippedPaths, nestedPath + prefix + key, suffix);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class CustomEditorHolder
/*     */   {
/*     */     private final PropertyEditor propertyEditor;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final Class<?> registeredType;
/*     */ 
/*     */ 
/*     */     
/*     */     private CustomEditorHolder(PropertyEditor propertyEditor, @Nullable Class<?> registeredType) {
/* 528 */       this.propertyEditor = propertyEditor;
/* 529 */       this.registeredType = registeredType;
/*     */     }
/*     */     
/*     */     private PropertyEditor getPropertyEditor() {
/* 533 */       return this.propertyEditor;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private Class<?> getRegisteredType() {
/* 538 */       return this.registeredType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private PropertyEditor getPropertyEditor(@Nullable Class<?> requiredType) {
/* 549 */       if (this.registeredType == null || (requiredType != null && (
/*     */         
/* 551 */         ClassUtils.isAssignable(this.registeredType, requiredType) || 
/* 552 */         ClassUtils.isAssignable(requiredType, this.registeredType))) || (requiredType == null && 
/*     */         
/* 554 */         !Collection.class.isAssignableFrom(this.registeredType) && !this.registeredType.isArray())) {
/* 555 */         return this.propertyEditor;
/*     */       }
/*     */       
/* 558 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyEditorRegistrySupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */