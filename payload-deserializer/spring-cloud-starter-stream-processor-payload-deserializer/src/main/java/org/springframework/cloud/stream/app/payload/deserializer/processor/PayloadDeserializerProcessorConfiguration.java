/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.payload.deserializer.processor;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.serializer.Deserializer;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.transformer.PayloadDeserializingTransformer;
import org.springframework.util.StreamUtils;

/**
 * @author Soby Chacko
 */
@EnableBinding(Processor.class)
public class PayloadDeserializerProcessorConfiguration {

	@Autowired
	private Deserializer<Object> deserializer;

	@Bean
	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public PayloadDeserializingTransformer payloadDeserializingTransformer() {
		PayloadDeserializingTransformer pdt = new PayloadDeserializingTransformer();
		pdt.setDeserializer(deserializer);
		return pdt;
	}

	@Bean
	@ConditionalOnMissingBean(Deserializer.class)
	public Deserializer<Object> noOpDeserializer() {
		return new Deserializer<Object>() {
			@Override
			public Object deserialize(InputStream inputStream) throws IOException {
				return StreamUtils.copyToByteArray(inputStream);
			}
		};
	}

}
