package dev.portfolio.visibility;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<CollectionRecord, Long> {
}
