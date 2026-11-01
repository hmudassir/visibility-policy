package dev.portfolio.visibility;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "access_grants")
public class AccessGrant {

	@Id
	private Long id;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "member_name", nullable = false, length = 80)
	private String memberName;
}
