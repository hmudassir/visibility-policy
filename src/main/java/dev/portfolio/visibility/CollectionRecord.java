package dev.portfolio.visibility;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "collections")
public class CollectionRecord {

	@Id
	private Long id;

	@Column(nullable = false, length = 120)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private VisibilityPolicy.CollectionStatus status;

	public VisibilityPolicy.CollectionStatus getStatus() {
		return status;
	}
}
