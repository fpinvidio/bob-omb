package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.Page;

import controllers.routes;

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
	@JoinColumn(name = "id_position", referencedColumnName = "id_position")
	public Position position;
	@ManyToOne
	@JoinColumn(name = "id_product")
	public Product product;

	public Image() {
		super();
		this.trained = false;
	}

	public String getSitePath() {
		if (path != null) {
			int start_index = path.indexOf("/uploads/");
			String uploads_path = path.substring(start_index);
			return routes.Assets.at(uploads_path).url();
		}
		return "";
	}

	public static Finder<Long, Image> find = new Finder<Long, Image>(
			Long.class, Image.class);

	public static Page<Image> page(int page, int pageSize, String sortBy,
			String order, String filter, Product product) {
		return find.where().join("product").where()
				.eq("t0.id_product", product.id)
				.ilike("name", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

}
