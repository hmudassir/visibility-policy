package dev.portfolio.visibility;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class DocumentRecord {

	@Id
	private Long id;

	@Column(name = "collection_id", nullable = false)
	private Long collectionId;

	@Column(nullable = false, length = 200)
	private String title;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private VisibilityPolicy.DocumentStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private VisibilityPolicy.Visibility visibility;

	public Long getId() {
		return id;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public String getTitle() {
		return title;
	}

	public VisibilityPolicy.DocumentStatus getStatus() {
		return status;
	}

	public VisibilityPolicy.Visibility getVisibility() {
		return visibility;
	}
}
