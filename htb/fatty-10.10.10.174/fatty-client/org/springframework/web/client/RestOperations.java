package org.springframework.web.client;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public interface RestOperations {
  @Nullable
  <T> T getForObject(String paramString, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  @Nullable
  <T> T getForObject(String paramString, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  @Nullable
  <T> T getForObject(URI paramURI, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> getForEntity(String paramString, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> getForEntity(String paramString, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> getForEntity(URI paramURI, Class<T> paramClass) throws RestClientException;
  
  HttpHeaders headForHeaders(String paramString, Object... paramVarArgs) throws RestClientException;
  
  HttpHeaders headForHeaders(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  HttpHeaders headForHeaders(URI paramURI) throws RestClientException;
  
  @Nullable
  URI postForLocation(String paramString, @Nullable Object paramObject, Object... paramVarArgs) throws RestClientException;
  
  @Nullable
  URI postForLocation(String paramString, @Nullable Object paramObject, Map<String, ?> paramMap) throws RestClientException;
  
  @Nullable
  URI postForLocation(URI paramURI, @Nullable Object paramObject) throws RestClientException;
  
  @Nullable
  <T> T postForObject(String paramString, @Nullable Object paramObject, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  @Nullable
  <T> T postForObject(String paramString, @Nullable Object paramObject, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  @Nullable
  <T> T postForObject(URI paramURI, @Nullable Object paramObject, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> postForEntity(String paramString, @Nullable Object paramObject, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> postForEntity(String paramString, @Nullable Object paramObject, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> postForEntity(URI paramURI, @Nullable Object paramObject, Class<T> paramClass) throws RestClientException;
  
  void put(String paramString, @Nullable Object paramObject, Object... paramVarArgs) throws RestClientException;
  
  void put(String paramString, @Nullable Object paramObject, Map<String, ?> paramMap) throws RestClientException;
  
  void put(URI paramURI, @Nullable Object paramObject) throws RestClientException;
  
  @Nullable
  <T> T patchForObject(String paramString, @Nullable Object paramObject, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  @Nullable
  <T> T patchForObject(String paramString, @Nullable Object paramObject, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  @Nullable
  <T> T patchForObject(URI paramURI, @Nullable Object paramObject, Class<T> paramClass) throws RestClientException;
  
  void delete(String paramString, Object... paramVarArgs) throws RestClientException;
  
  void delete(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  void delete(URI paramURI) throws RestClientException;
  
  Set<HttpMethod> optionsForAllow(String paramString, Object... paramVarArgs) throws RestClientException;
  
  Set<HttpMethod> optionsForAllow(String paramString, Map<String, ?> paramMap) throws RestClientException;
  
  Set<HttpMethod> optionsForAllow(URI paramURI) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, @Nullable HttpEntity<?> paramHttpEntity, Class<T> paramClass, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, @Nullable HttpEntity<?> paramHttpEntity, Class<T> paramClass, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(URI paramURI, HttpMethod paramHttpMethod, @Nullable HttpEntity<?> paramHttpEntity, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, @Nullable HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference, Object... paramVarArgs) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(String paramString, HttpMethod paramHttpMethod, @Nullable HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference, Map<String, ?> paramMap) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(URI paramURI, HttpMethod paramHttpMethod, @Nullable HttpEntity<?> paramHttpEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(RequestEntity<?> paramRequestEntity, Class<T> paramClass) throws RestClientException;
  
  <T> ResponseEntity<T> exchange(RequestEntity<?> paramRequestEntity, ParameterizedTypeReference<T> paramParameterizedTypeReference) throws RestClientException;
  
  @Nullable
  <T> T execute(String paramString, HttpMethod paramHttpMethod, @Nullable RequestCallback paramRequestCallback, @Nullable ResponseExtractor<T> paramResponseExtractor, Object... paramVarArgs) throws RestClientException;
  
  @Nullable
  <T> T execute(String paramString, HttpMethod paramHttpMethod, @Nullable RequestCallback paramRequestCallback, @Nullable ResponseExtractor<T> paramResponseExtractor, Map<String, ?> paramMap) throws RestClientException;
  
  @Nullable
  <T> T execute(URI paramURI, HttpMethod paramHttpMethod, @Nullable RequestCallback paramRequestCallback, @Nullable ResponseExtractor<T> paramResponseExtractor) throws RestClientException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/RestOperations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */