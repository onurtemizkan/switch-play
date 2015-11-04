package com.fasterxml.jackson.datatype.jdk8;

import java.io.IOException;
import java.util.OptionalInt;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class OptionalIntDeserializer extends StdDeserializer<OptionalInt>
{
    private static final long serialVersionUID = 1L;

    static final OptionalIntDeserializer INSTANCE = new OptionalIntDeserializer();

    public OptionalIntDeserializer() {
        super(OptionalInt.class);
    }
    
    @Override
    public OptionalInt getNullValue() {
        return OptionalInt.empty();
    }

    @Override
    public OptionalInt deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
    {
        return OptionalInt.of(jp.getValueAsInt());
    }
}
