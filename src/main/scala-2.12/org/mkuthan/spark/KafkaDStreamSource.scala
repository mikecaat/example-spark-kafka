// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.mkuthan.spark

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

class KafkaDStreamSource(config: Map[String, String]) {

  def createSource(ssc: StreamingContext, topic: String): DStream[KafkaPayload] = {
    val kafkaParams = config
    val kafkaTopics = Set(topic)

    KafkaUtils.
      createDirectStream[Array[Byte], Array[Byte]](
      ssc,
      PreferConsistent,
      Subscribe[Array[Byte], Array[Byte]](kafkaTopics, kafkaParams)).
      map(dstream => KafkaPayload(Option(dstream.key), dstream.value))
  }

}

object KafkaDStreamSource {
  def apply(config: Map[String, String]): KafkaDStreamSource = new KafkaDStreamSource(config)
}