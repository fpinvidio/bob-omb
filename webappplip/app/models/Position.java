package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "position")
public class Position extends Model{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_position")
	public Long id;
	@Required
	public int face;
	@Required
	public int angle;
	
	public static Finder<Long, Position> find = new Finder<Long, Position>(Long.class,
			Position.class);

}
