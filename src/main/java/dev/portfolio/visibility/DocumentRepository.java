package dev.portfolio.visibility;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentRecord, Long> {
}
