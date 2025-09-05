package com.itmo.nxzage.common.util.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtil {
    static {
        var filter = ObjectInputFilter.Config.createFilter("com.itmo.nxzage.*");
        ObjectInputFilter.Config.setSerialFilter(filter);
    }

    public static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.toByteArray();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> type) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis)) {
            return type.cast(in.readObject());
        }
    }
}