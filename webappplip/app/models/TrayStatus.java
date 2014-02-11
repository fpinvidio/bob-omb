package models;

import java.util.Date;

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
@Table(name = "tray_status")
public class TrayStatus extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_tray_status")
	public Long id;
	public Integer quantity;
	@Required
	public Date date;
	@Required
	@ManyToOne
	@JoinColumn(name = "id_tray")
	public Tray tray;
	@Required
	@OneToOne
	@JoinColumn(name = "id_status")
	public Status status;
	@OneToOne
	@JoinColumn(name = "id_product")
	public Product product;

	public static Finder<Long, TrayStatus> find = new Finder<Long, TrayStatus>(
			Long.class, TrayStatus.class);

	public String getStatusType() {
		String type = "success";
		switch (this.status.id.intValue()) {
		case 7:
			type = "warning";
			break;
		case 9:
			type = "danger";
			break;
		}
		return type;
	}
}
