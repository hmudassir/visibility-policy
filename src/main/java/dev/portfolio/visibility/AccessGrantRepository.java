package dev.portfolio.visibility;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessGrantRepository extends JpaRepository<AccessGrant, Long> {

	boolean existsByDocumentIdAndMemberName(Long documentId, String memberName);
}
