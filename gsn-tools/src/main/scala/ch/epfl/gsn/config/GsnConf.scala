/**
* Global Sensor Networks (GSN) Source Code
* Copyright (c) 2006-2016, Ecole Polytechnique Federale de Lausanne (EPFL)
* Copyright (c) 2020-2023, University of Innsbruck
* 
* This file is part of GSN.
* 
* GSN is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* GSN is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with GSN.  If not, see <http://www.gnu.org/licenses/>.
* 
* File: src/ch/epfl/gsn/config/GsnConf.scala
*
* @author Jean-Paul Calbimonte
* @author Julien Eberle
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/
package ch.epfl.gsn.config

import xml._
import com.typesafe.config.ConfigFactory
import play.api.libs.json._


case class GsnConf(monitorPort:Int,timeFormat:String,
    zmqConf:ZmqConf,
    storageConf:StorageConf,slidingConf:Option[StorageConf],
    maxDBConnections: Int, maxSlidingDBConnections: Int, backlogCommandsConf: BacklogCommandsConf)
    
object GsnConf extends Conf {
  def create(xml:Elem)=GsnConf(
    takeInt(xml \ "monitor-port").getOrElse(defaultGsn.monitorPort ),    
    take(xml \ "time-format").getOrElse(defaultGsn.timeFormat ),
    ZmqConf.create(xml),
    StorageConf.create((xml \ "storage").head),
    (xml \ "sliding").headOption.map(a=>StorageConf.create((a \ "storage").head)),
    takeInt(xml \ "max-db-connections").getOrElse(defaultGsn.maxDBConnections),
    takeInt(xml \ "max-sliding-db-connections").getOrElse(defaultGsn.maxSlidingDBConnections),
    BacklogCommandsConf.create(xml),
  )
  def load(path:String)=create(XML.load(path))
}
 
case class ZmqConf(enabled:Boolean,proxyPort:Int,metaPort:Int) 
object ZmqConf extends Conf{
  def create(xml:scala.xml.Elem)=ZmqConf(
    takeBool(xml \ "zmq-enable").getOrElse(defaultZmq.enabled),
    takeInt(xml \ "zmqproxy").getOrElse(defaultZmq .proxyPort),
    takeInt(xml \ "zmqmeta").getOrElse(defaultZmq.metaPort ) )  
}

case class BacklogCommandsConf(enabled:Boolean,backlogCommandsPort:Int) 
object BacklogCommandsConf extends Conf{
  def create(xml:scala.xml.Elem)=BacklogCommandsConf(
    takeBool(xml \ "backlog-commands-enable").getOrElse(defaultBacklogCommands.enabled),
    takeInt(xml \ "backlog-commands-port").getOrElse(defaultBacklogCommands.backlogCommandsPort))  
}

case class StorageConf(driver:String,url:String,
    user:String,pass:String,identifier:Option[String]) 
object StorageConf extends Conf{
  implicit val storageConfWrites: Writes[StorageConf] = Json.writes[StorageConf]
  implicit val storageConfReads: Reads[StorageConf] = Json.reads[StorageConf]
  def create(xml:Node)=StorageConf(
    xml \@ "driver",
    xml \@ "url",
    xml \@ "user",
    xml \@ "password",
    xml.attribute("identifier").map(_.toString))  
}

