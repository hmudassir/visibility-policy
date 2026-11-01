package dev.portfolio.visibility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import dev.portfolio.visibility.VisibilityPolicy.Caller;
import dev.portfolio.visibility.VisibilityPolicy.CollectionStatus;
import dev.portfolio.visibility.VisibilityPolicy.DocumentStatus;
import dev.portfolio.visibility.VisibilityPolicy.Visibility;

@SpringBootTest
@AutoConfigureMockMvc
class VisibilityTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void policyAllowsStaffAndHidesDraftsFromMembers() {
		assertThat(VisibilityPolicy.canView(
				CollectionStatus.DRAFT, DocumentStatus.DRAFT, Visibility.PRIVATE, Caller.asStaff(), false)).isTrue();
		assertThat(VisibilityPolicy.canView(
				CollectionStatus.PUBLISHED, DocumentStatus.PUBLISHED, Visibility.PUBLIC, Caller.anonymous(), false)).isTrue();
		assertThat(VisibilityPolicy.canView(
				CollectionStatus.PUBLISHED, DocumentStatus.PUBLISHED, Visibility.PRIVATE, Caller.member("ada"), true)).isTrue();
		assertThat(VisibilityPolicy.canView(
				CollectionStatus.PUBLISHED, DocumentStatus.PUBLISHED, Visibility.PRIVATE, Caller.member("ada"), false)).isFalse();
		assertThat(VisibilityPolicy.canView(
				CollectionStatus.PUBLISHED, DocumentStatus.DRAFT, Visibility.PUBLIC, Caller.anonymous(), false)).isFalse();
		assertThat(VisibilityPolicy.canView(
				CollectionStatus.DRAFT, DocumentStatus.PUBLISHED, Visibility.PUBLIC, Caller.member("ada"), true)).isFalse();
	}

	@Test
	void publicDocumentIsReadableAndHiddenDocumentsDoNotLeakTheTitle() throws Exception {
		mvc.perform(get("/api/documents/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Public note"));

		mvc.perform(get("/api/documents/2"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.title").value("Not Found"))
				.andExpect(jsonPath("$.detail").value("Document was not found"));

		mvc.perform(get("/api/documents/2").header("X-Actor", "member:ada"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Private note"));

		mvc.perform(get("/api/documents/2").header("X-Actor", "member:bea"))
				.andExpect(status().isNotFound());

		mvc.perform(get("/api/documents/3"))
				.andExpect(status().isNotFound());

		mvc.perform(get("/api/documents/3").header("X-Actor", "staff"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Draft note"));

		mvc.perform(get("/api/documents/4"))
				.andExpect(status().isNotFound());

		mvc.perform(get("/api/documents/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail").value("Document was not found"));
	}
}
