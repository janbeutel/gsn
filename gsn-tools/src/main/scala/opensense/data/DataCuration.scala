package opensense.data

import com.mchange.v2.c3p0.C3P0Registry
import scala.slick.jdbc.JdbcBackend.Database
import org.slf4j.LoggerFactory
import gsn.data.DataStore
import com.typesafe.config.ConfigFactory
import gsn.config.GsnConf
import gsn.config.StorageConf
import scala.slick.jdbc.JdbcBackend
import scala.collection.mutable.ArrayBuffer

object DataCuration {
  val data=new ArrayBuffer[Array[Any]]()
  
  val log=LoggerFactory.getLogger(DataCuration.getClass)

  val geo=StorageConf("org.postgresql.Driver","jdbc:postgresql://localhost:5433/pg_test",
        "postgres","password",Some("geo"))
            lazy val conf=ConfigFactory.load
  val gsnConf=GsnConf.load(conf.getString("gsn.config"))
  val ds=new DataStore(gsnConf)

  val d=ds.datasource(geo.url, geo)    

  private def vsDB(dsName:Option[String])={
	  Database.forDataSource(C3P0Registry.pooledDataSourceByName(dsName.get))
	}
  
  def point(x:Double,y:Double)=s"st_setsrid(st_makepoint($x, $y),4326)"
	  
  def insertClean(time:Long,station:Int,co:Int,no2:Int,co2:Int,
      segId:Int,x:Double,y:Double)(implicit session:JdbcBackend.Session)={
    val sql=s"""INSERT INTO geo_osanm (timed,station,co,no2,co2,geom,seg) 
                VALUES ($time,$station,$co,$no2,$co2,${point(x,y)},$segId);"""
    val stmt=session.conn.createStatement
    val rs=stmt executeUpdate(sql)        
  }
  
  def insertClean()(implicit session:JdbcBackend.Session)={
    val sql=s"""INSERT INTO geo_osanm (timed,station,co,no2,co2,geom,seg) 
                VALUES ${data.map(d=>"("+d.mkString(",")+")").mkString(",")}"""
    val stmt=session.conn.createStatement
    val rs=stmt executeUpdate(sql)        
  }
  def closePoint(gid:Int,x:Double,y:Double)(implicit session:JdbcBackend.Session)={
    val q=s"SELECT ST_AsText(ST_ClosestPoint(geom,${point(x,y)})) AS point FROM tl where gid=$gid"
    val stmt=session.conn.createStatement
    val rs=stmt executeQuery q
        println("execute close "+System.currentTimeMillis)
    rs.next
    rs getObject "point"
  }
  
  def distance(time:Long,station:Int,co:Int,no2:Int,co2:Int,
      x:Double,y:Double)={
    val q=s"""SELECT (geom::geography <-> ${point(x,y)}::geography) as dist,
                     gid,id,line,geom
              FROM tl 
              ORDER BY dist LIMIT 10;"""
    var pip=0d
    println("before distance "+System.currentTimeMillis)
    vsDB(Some(geo.url)).withSession {implicit session=>
      val stmt=session.conn.createStatement
      val rs=stmt executeQuery q
      println("after distance "+System.currentTimeMillis)
	    rs.next
      pip=rs getDouble "dist"
      if (pip<20){
        val gid =rs getInt "gid"
        val segId=rs getInt "id"
        val coords=closePoint(gid,x,y).toString.replace("POINT(","").replace(")","").split(" ")
        println("after close point "+System.currentTimeMillis)
        data+=Array(time, station, co, no2, co2, point(coords(0).toDouble, coords(1).toDouble),segId)
      }
      //else println(s"$x,$y")
     
      if (data.size>100){
        //insertClean
        data.clear
      }
    }
  
    pip    
  }
  
  def query(offset:Long)={
    val open=StorageConf("org.postgresql.Driver","jdbc:postgresql://opensense.epfl.ch:5433/opensense",
        "gsn","opensense",Some("opensense"))
    val op_d=ds.datasource(open.url, open)    
    
    val q=s"select * from geo_osanm where latitude is not null and station<100 order by timed offset $offset limit 100000"
    vsDB(Some(open.url)).withSession {implicit session=>
      val stmt=session.conn.createStatement
      val rs=stmt executeQuery q
	    log debug "Query: "+q
      while (rs.next) {
        val x=rs getDouble "longitude"
        val y=rs getDouble "latitude"
        val station=rs getInt "station"
        val time=rs getLong "timed"
        val co=rs getInt "co"
        val no2=rs getInt "no2"
        val co2=rs getInt "co2"
        val dis=distance(time,station,co,no2,co2,x,y)
        //if (dis<100)
          //println(s"$x,$y,$station")                 
      }
	  } 
  }
  
  def main(args:Array[String]):Unit={
    query(1600000)
  }
}