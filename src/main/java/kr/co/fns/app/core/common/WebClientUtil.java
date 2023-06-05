package kr.co.fns.app.core.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
public class WebClientUtil {

    @Autowired
    private WebClient webClient;
    @Autowired
    private ObjectMapper objectMapper;

    public <T> T getAsnc(URI url, Object paramData){
        return this._get(url, paramData, Map.class, true);
    }
    public <T> T get(URI url, Object paramData, Class responseClass){
        return this._get(url, paramData, responseClass, false);
    }
    public <T> T postAsnc(URI url, Object bodyData){
        return this._postAndPutAndDelete(url, HttpMethod.POST, bodyData, Map.class, true);
    }
    public <T> T post(URI url, Object bodyData, Class responseClass){
        return this._postAndPutAndDelete(url, HttpMethod.POST, bodyData, responseClass, false);
    }
    public <T> T putAsnc(URI url, Object bodyData){
        return this._postAndPutAndDelete(url, HttpMethod.PUT, bodyData, Map.class, true);
    }
    public <T> T put(URI url, Object bodyData, Class responseClass){
        return this._postAndPutAndDelete(url, HttpMethod.PUT, bodyData, responseClass, false);
    }
    public <T> T deleteAsnc(URI url, Object bodyData){
        return this._postAndPutAndDelete(url, HttpMethod.DELETE, bodyData, Map.class, true);
    }
    public <T> T delete(URI url, Object bodyData, Class responseClass){
        return this._postAndPutAndDelete(url, HttpMethod.DELETE, bodyData, responseClass, false);
    }


    private <T> T _get(URI url, Object paramData, Class responseClass, Boolean isAsnc){
        String uri = url.toString();
        if (ObjectUtils.isEmpty(paramData) != true) {
            Map<String, Object> param;
            if (paramData instanceof Map != true) {
                param = objectMapper.convertValue(paramData, Map.class);
            } else {
                param = (Map<String, Object>) paramData;
            }
            StringBuilder sb = new StringBuilder(uri);
            sb.append("?");
            param.keySet().stream().forEach(v -> sb.append(v).append("=").append(param.get(v)).append("&"));
            uri = sb.toString();
        }
        if(isAsnc == true) {
            return (T) webClient.method(HttpMethod.GET)
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(responseClass)
                    .subscribe();
        }else{
            return (T) webClient.method(HttpMethod.GET)
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(responseClass)
                    .block();
        }
    }

    private <T> T _postAndPutAndDelete(URI url, HttpMethod method, Object bodyData, Class responseClass, Boolean isAsnc){
        String uri = url.toString();
        Map<String, Object> body = null;
        if (ObjectUtils.isEmpty(bodyData) != true) {
            if (bodyData instanceof Map != true) {
                body = objectMapper.convertValue(bodyData, Map.class);
            } else {
                body = (Map<String, Object>) bodyData;
            }
        }
        if(isAsnc == true) {
            return (T) webClient.method(method)
                    .uri(uri)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(responseClass)
                    .subscribe();
        }else{
            return (T) webClient.method(method)
                    .uri(uri)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(responseClass)
                    .block();
        }
    }
}
