/*
 * Copyright 2013 FasterXML.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package com.fasterxml.jackson.datatype.jsr310.ser;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * Serializer for Java 8 temporal {@link Instant}s, {@link OffsetDateTime}, and {@link ZonedDateTime}s.
 *
 * @author Nick Williams
 * @since 2.2.0
 */
public final class InstantSerializer extends InstantSerializerBase<Instant>
{
    private static final long serialVersionUID = 1L;

    public static final InstantSerializer INSTANCE = new InstantSerializer();

    @Deprecated // since 2.5, remove in 2.6
    public static final InstantSerializer INSTANT = INSTANCE;

    @Deprecated // since 2.5, remove in 2.6
    public static final OffsetDateTimeSerializer OFFSET_DATE_TIME = OffsetDateTimeSerializer.INSTANCE;

    @Deprecated // since 2.5, remove in 2.6
    public static final ZonedDateTimeSerializer ZONED_DATE_TIME = ZonedDateTimeSerializer.INSTANCE;
    
    protected InstantSerializer() {
        super(Instant.class, Instant::toEpochMilli, Instant::getEpochSecond, Instant::getNano);
    }
}
