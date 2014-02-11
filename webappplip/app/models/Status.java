package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Status extends Model{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_status")
	public Long id;
	@Required
	public String description;

}
