/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.serde;

import com.phonepe.columbus.type.EntityType;

/**
 * A utility class to help serialize and deserialize Java objects into other formats.
 */
public interface SerDe {

    /**
     * A unique string indicating the format of the serialized data.
     *
     * @return the data format.
     */
    String format();

    /**
     * Deserialize the data from a string to the expected type.
     *
     * @param data the input to be parsed.
     * @param type the type of the object being read.
     * @param <T>  the type of the result.
     * @return the parsed object of type {@code T}.
     * @throws IllegalArgumentException if the {@code type} is invalid or if the {@code data} could not be deserialized.
     */
    <T> T fromString(String data, EntityType<T> type);

    /**
     * Deserialize the data from a byte array to the expected type.
     *
     * @param data the input to be parsed.
     * @param type the type of the object being read.
     * @param <T>  the type of the result.
     * @return the parsed object of type {@code T}.
     * @throws IllegalArgumentException if the {@code type} is invalid or if the {@code data} could not be deserialized.
     */
    <T> T fromBytes(byte[] data, EntityType<T> type);

    /**
     * Serialize the given data and deserialize to the expected type. Some implementations can directly transform
     * from the input to the given type.
     *
     * @param data the input to be converted.
     * @param type the expected type of the object.
     * @param <T>  the type of the result.
     * @return the new object of type {@code T}.
     * @throws IllegalArgumentException if the {@code type} is invalid or if the {@code data} could not be
     *                                  serialized, deserialized or converted.
     */
    <T> T convert(Object data, EntityType<T> type);

    /**
     * Serialize the data to a string.
     *
     * @param data the data to be written.
     * @return a string representing the serialized input.
     * @throws IllegalArgumentException if the {@code data} could not be serialized.
     */
    String toString(Object data);

    /**
     * Serialize the data to a byte array.
     *
     * @param data the data to be written.
     * @return a byte array representing the serialized input.
     * @throws IllegalArgumentException if the {@code data} could not be serialized.
     */
    byte[] toBytes(Object data);

    /**
     * Create a human-readable representation of serialized data.
     *
     * @param data the data to be written.
     * @return the human-readable string.
     * @throws IllegalArgumentException if the {@code data} could not be serialized.
     */
    String humanize(Object data);

    /**
     * The media type representing the format of serialized data. This is usually used while sending the data over
     * the network.
     *
     * @return the media type of the serialized data.
     */
    String mediaType();
}
