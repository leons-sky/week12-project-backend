package us.group14.backend.annotations.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import us.group14.backend.annotations.ApiMapping;

import java.lang.reflect.Method;

@Configuration
public class WebMvcRegistrationsConfig {

    @Bean
    public WebMvcRegistrations webMvcRegistrations() {
        return new WebMvcRegistrations(){
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping(){
                    @Override
                    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType){
                        RequestMappingInfo defaultRequestMappingInfo = super.getMappingForMethod(method, handlerType);
                        if (defaultRequestMappingInfo == null) {
                            return null;
                        }

                        final Class<?> clazz = method.getDeclaringClass();

                        ApiMapping apiMapping;
                        if (method.isAnnotationPresent(ApiMapping.class)) {
                            apiMapping = method.getAnnotation(ApiMapping.class);
                        } else if (clazz.isAnnotationPresent(ApiMapping.class)) {
                            apiMapping = clazz.getAnnotation(ApiMapping.class);
                        } else {
                            return defaultRequestMappingInfo;
                        }

                        String pathPrefix;
                        if (apiMapping != null) {
                            String version = apiMapping.version();
                            pathPrefix = String.format("/api/v%s", version);
                        } else {
                            return defaultRequestMappingInfo;
                        }

                        //extend path by mutating configured request mapping info
                        RequestMappingInfo.Builder mutateBuilder = defaultRequestMappingInfo.mutate();
                        mutateBuilder.paths(
                                defaultRequestMappingInfo.getPatternValues().stream()
                                        .map(path -> pathPrefix + path)
                                        .toArray(String[]::new)
                        );
                        return mutateBuilder.build();
                    }
                };
            }
        };
    }

}
