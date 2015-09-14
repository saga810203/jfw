package org.jfw.util.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class TypeReference<T>
{
    protected final Type _type;
    
    protected TypeReference()
    {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) { // sanity check, should never happen
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() { return _type; }
}