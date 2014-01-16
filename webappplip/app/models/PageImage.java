package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "page_image")
public class PageImage extends Model{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_page_image")
	public Long id;
	@Required
	public String path;
	
	public static void create(PageImage page_image) {
		page_image.save();
	}

	public static Finder<Long, PageImage> find = new Finder<Long, PageImage>(Long.class,
			PageImage.class);

}
