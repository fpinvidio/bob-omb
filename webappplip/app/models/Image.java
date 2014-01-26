package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "image")
public class Image extends Model {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_image")
	public Long id;
	@Required
	public String path;
	public String name;
	public boolean trained;
	@ManyToOne
	@JoinColumn(name="id_position", referencedColumnName="id_position")
	public Position position;
	
	public Image() {
		super();
		this.trained = false;
	}

}
