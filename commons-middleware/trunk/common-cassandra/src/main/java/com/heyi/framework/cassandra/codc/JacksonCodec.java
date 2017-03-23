package com.heyi.framework.cassandra.codc;

import java.nio.ByteBuffer;

import com.datastax.driver.core.utils.Bytes;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class JacksonCodec {
	private static JacksonCodec instance = null;
	
	
    public static JacksonCodec getInstance() {
    	if(instance==null)
    		instance = new JacksonCodec();
		return instance;
	}

	private final ObjectMapper objectMapper = new ObjectMapper();
    private ObjectMapper mapObjectMapper = new ObjectMapper();

    private JacksonCodec() {
        createObjectMapper(objectMapper);
        TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(DefaultTyping.NON_FINAL);
        typer.init(JsonTypeInfo.Id.CLASS, null);
        typer.inclusion(JsonTypeInfo.As.PROPERTY);
        objectMapper.setDefaultTyping(typer);

        createObjectMapper(mapObjectMapper);
        // type info inclusion
        TypeResolverBuilder<?> mapTyper = new DefaultTypeResolverBuilder(DefaultTyping.NON_FINAL) {
            public boolean useForType(JavaType t)
            {
                switch (_appliesFor) {
                case NON_CONCRETE_AND_ARRAYS:
                    while (t.isArrayType()) {
                        t = t.getContentType();
                    }
                    // fall through
                case OBJECT_AND_NON_CONCRETE:
                    return (t.getRawClass() == Object.class) || !t.isConcrete();
                case NON_FINAL:
                    while (t.isArrayType()) {
                        t = t.getContentType();
                    }
                    // to fix problem with wrong long to int conversion
                    if (t.getRawClass() == Long.class) {
                        return true;
                    }
                    return !t.isFinal(); // includes Object.class
                default:
                //case JAVA_LANG_OBJECT:
                    return (t.getRawClass() == Object.class);
                }
            }
        };
        mapTyper.init(JsonTypeInfo.Id.CLASS, null);
        mapTyper.inclusion(JsonTypeInfo.As.PROPERTY);
        mapObjectMapper.setDefaultTyping(mapTyper);
    }

    private void createObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setVisibilityChecker(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                                            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                                            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                                            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                                            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
    }


    public Object decodeObject(ByteBuffer bb) {
    	if(bb==null) return null;
    	byte[] bytes  = Bytes.getArray(bb);
        try {
        	return  objectMapper.readValue(bytes, Object.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public ByteBuffer encode(Object value) {
    	if(value==null) return null;
    	
        try {
        	byte[] bytes =  objectMapper.writeValueAsBytes(value);
        	if(bytes != null){
        		return ByteBuffer.wrap(bytes);
        	}
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return null;
    }
    
    public byte[] encodeAsByteArray(Object value) {
    	if(value==null) return null;
    	
        try {
        	byte[] bytes =  objectMapper.writeValueAsBytes(value);
        	if(bytes != null){
        		return bytes;
        	}
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return null;
    }
    
    public Object decodeObjectFromByteArray(byte[] bytes) {
    	if(bytes==null) return null;
        try {
        	return  objectMapper.readValue(bytes, Object.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
