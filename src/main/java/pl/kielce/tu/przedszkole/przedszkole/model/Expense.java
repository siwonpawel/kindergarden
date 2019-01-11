package pl.kielce.tu.przedszkole.przedszkole.model;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EXPENSE database table.
 * 
 */
@Data
@Entity
public class Expense implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private BigDecimal cost;

	private String description;

	private String name;

	@Temporal(TemporalType.DATE)
	private Date term;

	//bi-directional many-to-one association to Payment
	@OneToMany(mappedBy="expense")
	private List<Payment> payments;

	public Expense() {
	}

	public Payment addPayment(Payment payment) {
		getPayments().add(payment);
		payment.setExpense(this);

		return payment;
	}

	public Payment removePayment(Payment payment) {
		getPayments().remove(payment);
		payment.setExpense(null);

		return payment;
	}

}