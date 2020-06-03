package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {
  @AliasFor("basePackages")
  String[] value() default {};
  
  @AliasFor("value")
  String[] basePackages() default {};
  
  Class<?>[] basePackageClasses() default {};
  
  Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;
  
  Class<? extends ScopeMetadataResolver> scopeResolver() default AnnotationScopeMetadataResolver.class;
  
  ScopedProxyMode scopedProxy() default ScopedProxyMode.DEFAULT;
  
  String resourcePattern() default "**/*.class";
  
  boolean useDefaultFilters() default true;
  
  Filter[] includeFilters() default {};
  
  Filter[] excludeFilters() default {};
  
  boolean lazyInit() default false;
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({})
  public static @interface Filter {
    FilterType type() default FilterType.ANNOTATION;
    
    @AliasFor("classes")
    Class<?>[] value() default {};
    
    @AliasFor("value")
    Class<?>[] classes() default {};
    
    String[] pattern() default {};
  }
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ComponentScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */