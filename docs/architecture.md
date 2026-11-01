# Architecture

The visibility rule is a pure function. Loading rows and speaking HTTP sit outside it. A hidden document and a missing document produce the same response.

## Boundaries

```text
GET /api/documents/{id}
  X-Actor header          stands in for the signed-in caller
        |
        v
DocumentController
  loads the document, its collection, and whether this member has a grant
        |
        v
VisibilityPolicy.canView
  no repository
  no HTTP
  returns true or false
        |
        +--> true   200 and the title
        +--> false  404, title omitted, same body as a missing id
```

`X-Actor` is a stand-in so this sample can show the policy without a second authentication design. The session sample is where a real principal comes from. The policy still receives a caller: anonymous, a named member, or staff.

## Rule

- Staff can read every document, including drafts and documents in a draft collection.
- Anyone can read a published public document in a published collection.
- A published private document is readable by a member with an approved grant.
- Anything else is hidden, including a draft and a document whose collection is still draft.

The controller loads those facts and passes them in. The policy does not query.

## Failure

A denied caller and a caller who asks for an id that does not exist both receive `404` and `"Document was not found"`. The body does not contain the title and does not say whether the row is private, draft, or absent.

## What this sample leaves out

The grant is a row for one member and one document. A larger model would keep the same policy shape: callers load the current facts, including a role read on that request, and ask the function again. The function would still return yes or no.
