package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "page")
public class Page extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_page")
	public Long id;
	@ManyToOne
	@JoinColumn(name="id_order", referencedColumnName="id_order")
	public Order order;
	public Long product_quantity;
	@OneToOne
	@JoinColumn(name="id_page_image")
	public PageImage page_image;
	@Required
	public Long page_number;
}
