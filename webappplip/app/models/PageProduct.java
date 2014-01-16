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
@Table(name = "page_product")
public class PageProduct extends Model{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_page_product")
	public Long id;
	@ManyToOne
	@JoinColumn(name="id_page")
	public Page page;
	@ManyToOne
	@JoinColumn(name="id_product")
	public Product product;
	@Required
	public Long quantity;
	
	public static void create(PageProduct page_product) {
		page_product.save();
	}

}
