package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tray")
public class Tray extends Model {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_tray")
	public Long id;
	@Required
	public String code;
	@OneToOne
	@JoinColumn(name = "id_page")
	public models.Page page;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_tray", referencedColumnName = "id_tray")
	public List<TrayStatus> tray_status;
	
	public static Finder<Long, Tray> find = new Finder<Long, Tray>(Long.class,
			Tray.class);
	//FALTA FILTRAR POR ORDER
	public static Page<Tray> page(int page, int pageSize, String sortBy, String order, String filter, Order plip_order) {
		 
       Page<Tray> res_page =    find
            	.where()
            	.join("page.order")
            	.where()
            	.eq("t2.id_order", plip_order.id)
                .ilike("code", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(pageSize)
                .getPage(page);
//                .setFirstRow(page*pageSize)
//                .setMaxRows(pageSize)
//                .findList();
       return res_page;
    }
}
