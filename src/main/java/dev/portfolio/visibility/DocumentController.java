package dev.portfolio.visibility;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController {

	private final DocumentRepository documents;

	private final CollectionRepository collections;

	private final AccessGrantRepository grants;

	public DocumentController(
			DocumentRepository documents,
			CollectionRepository collections,
			AccessGrantRepository grants) {
		this.documents = documents;
		this.collections = collections;
		this.grants = grants;
	}

	@GetMapping("/api/documents/{id}")
	public DocumentView read(
			@PathVariable Long id,
			@RequestHeader(value = "X-Actor", required = false) String actorHeader) {
		DocumentRecord document = documents.findById(id).orElseThrow(HiddenDocumentException::new);
		CollectionRecord collection = collections.findById(document.getCollectionId())
				.orElseThrow(HiddenDocumentException::new);
		VisibilityPolicy.Caller caller = VisibilityPolicy.Caller.parse(actorHeader);
		boolean approved = caller.member()
				&& grants.existsByDocumentIdAndMemberName(document.getId(), caller.memberName());
		boolean allowed = VisibilityPolicy.canView(
				collection.getStatus(),
				document.getStatus(),
				document.getVisibility(),
				caller,
				approved);
		if (!allowed) {
			throw new HiddenDocumentException();
		}
		return new DocumentView(document.getId(), document.getTitle());
	}

	public record DocumentView(Long id, String title) {
	}
}
