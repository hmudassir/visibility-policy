package dev.portfolio.visibility;

public final class VisibilityPolicy {

	private VisibilityPolicy() {
	}

	public static boolean canView(
			CollectionStatus collectionStatus,
			DocumentStatus documentStatus,
			Visibility visibility,
			Caller caller,
			boolean approvedMember) {
		if (caller.staff()) {
			return true;
		}
		if (collectionStatus != CollectionStatus.PUBLISHED || documentStatus != DocumentStatus.PUBLISHED) {
			return false;
		}
		if (visibility == Visibility.PUBLIC) {
			return true;
		}
		return caller.member() && approvedMember;
	}

	public enum CollectionStatus {
		DRAFT,
		PUBLISHED
	}

	public enum DocumentStatus {
		DRAFT,
		PUBLISHED
	}

	public enum Visibility {
		PUBLIC,
		PRIVATE
	}

	public record Caller(String memberName, boolean staff) {

		public static Caller anonymous() {
			return new Caller(null, false);
		}

		public static Caller member(String name) {
			return new Caller(name, false);
		}

		public static Caller asStaff() {
			return new Caller(null, true);
		}

		public boolean member() {
			return memberName != null;
		}

		public static Caller parse(String header) {
			if (header == null || header.isBlank() || "anonymous".equals(header)) {
				return anonymous();
			}
			if ("staff".equals(header)) {
				return asStaff();
			}
			if (header.startsWith("member:")) {
				String name = header.substring("member:".length()).trim();
				if (!name.isEmpty()) {
					return member(name);
				}
			}
			return anonymous();
		}
	}
}
