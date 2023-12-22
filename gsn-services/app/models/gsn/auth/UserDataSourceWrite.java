/**
* Global Sensor Networks (GSN) Source Code
* Copyright (c) 2006-2016, Ecole Polytechnique Federale de Lausanne (EPFL)
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
* File: app/models/gsn/auth/UserDataSourceWrite.java
*
* @author Julien Eberle
*
*/
package models.gsn.auth;

import java.util.List;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.JoinColumn;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
@Entity
@Table(name="user_data_source_write",  uniqueConstraints={
		   @UniqueConstraint(columnNames={"user_id", "data_source_id"})
		})
public class UserDataSourceWrite extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
    
	@ManyToOne
	@JoinColumn(name = "user_id") 
	public User user;
	
	@ManyToOne
	@JoinColumn(name = "data_source_id")
	public DataSource data_source;
	
	public static final Finder<Long, UserDataSourceWrite> find = new Finder<>(UserDataSourceWrite.class);

	public static List<UserDataSourceWrite> findByUser(User value) {
		return find.query().where().eq("user", value).findList();
	}
	
	public static List<UserDataSourceWrite> findByDataSource(DataSource value) {
		return find.query().where().eq("data_source", value).findList();
	}
	
	public static UserDataSourceWrite findByBoth(User u, DataSource d) {
		return find.query().where().eq("user", u).eq("data_source", d).findOne();
	}

}
