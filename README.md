# Visibility policy

Who can read a document is a **pure function**. It takes the collection status, the document status, the visibility, and the caller. It does not load the database and it does not know about HTTP.

The API asks that function and then responds:

- Allowed: `200` and the title.
- Hidden: `404`. The body does not contain the title, and it does not say whether the document is private, draft, or missing.

Rules in this sample:

- Staff can read every document, including drafts.
- Anyone can read a published document in a published collection when the document is public.
- A private document is readable by a member who has an approved grant.
- A draft document, or a document in a draft collection, is hidden from everyone except staff.

`X-Actor` stands in for the signed-in user so this sample can stay focused on the policy. The session sample shows how a real principal is established.

## Run

Java 21.

```sh
./mvnw spring-boot:run
```

```sh
curl -sS http://127.0.0.1:8080/api/documents/1
curl -sS -H 'X-Actor: member:ada' http://127.0.0.1:8080/api/documents/2
curl -sS http://127.0.0.1:8080/api/documents/2
```

Document 1 is public. Document 2 is private. Document 3 is a draft. Document 4 sits in a draft collection.

## Tests

```sh
./mvnw test
```

## What this is

A portfolio proof of concept. It is not a production access-control system.
