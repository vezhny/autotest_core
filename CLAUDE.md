# Test-writing conventions

These rules apply to every JUnit test in this repo (not just the file they were first raised on).

## 1. Test method bodies must be fully linear - no loops or conditionals

A `@Test`/`@ParameterizedTest` method body must read as a flat, top-to-bottom sequence of
statements: no `if`/`else`, no `for`/`while`/`do-while`, no `try`/`catch`/`finally` directly
inside it. If a scenario genuinely needs a loop, a branch, or guaranteed cleanup (a pagination
sweep, "search until a matching value is found", change-then-restore-on-failure, etc.),
extract that logic into a separate, named `private` helper method and call it as one linear
step from the test.

## 2. Cache expensive/repeated helper calls once instead of calling them per-test

If a helper method backed by a live API call is needed by multiple test methods in the same
class, fetch it once - typically in `@BeforeAll` (test classes run with
`TestInstance.Lifecycle.PER_CLASS`, see `TestCore`) - and store the result (and any derived
value like `.getId()`) in an instance field. Test methods then read the field instead of
re-invoking the helper.

## 3. Don't spin up a separate test for checks that belong on an existing smoke test

If a new assertion is a basic, always-true-for-a-healthy-response shape check (e.g. "every
returned entry has a unique, non-null id and name"), add it to the existing smoke test for
that endpoint instead of writing a new standalone `@Test` method for it. Reserve a dedicated
test method for a distinct *scenario* (a specific filter, an edge case, a permission
boundary), not for every individual assertion that could just as well ride along on the
smoke test that already fetches the same data.

## 4. Lookup helpers belong in the helper class; matcher methods should own response-handling

- If test code needs to find an entity by id/name within a collection, reuse (or add) that
  lookup on the relevant `*Helper` class (extends `CoreHelper`) - don't reinvent it as a
  private method in the test file.
- For assertions that need to inspect a specific field or entity within an API response,
  add a dedicated method on the relevant `*Matcher` class (extends `MatcherCore`) that takes
  the raw `Response` and does `assertSuccessResponse(response)` + parsing + the lookup + the
  actual assertion *inside that one matcher method*, covered with `@Step`. The test method
  should then just be: call the bare `xxxApi.someMethod()`, then call one matcher method with
  the response.

Example: `ExampleMatcher.assertSuccessResponse` / `assertExampleModels`, used from
`api/example/ExampleTest`.

## 5. Use the core tools instead of hand-rolled equivalents

- A known bug is marked with `@Bug("<ticket id>")` (`core.annotation.Bug`), not
  `@Link(type = "issue", ...)`.
- A request as another user goes through `tokenStore.runAs(token, () -> ...)`, never by
  replacing the token and restoring it by hand. `tokenStore.reset()` forces a new token.
- Passwords, tokens and api keys are masked in logs, Allure attachments and step parameters
  by `LogMasker`. When a new kind of
  secret has to be hidden, extend `LogMasker` rather than masking it at the call site.

# Workflow conventions

## 1. Register every new file in git as soon as it is created

Right after creating a new file - a class, a page object, a matcher, a config, a resource -
run `git add <path>` on it. Don't wait for the commit and don't leave it untracked while the
work continues.

An untracked file is invisible to `git diff`, is skipped by `git stash`, is silently missing
from a `git commit -a`, and is wiped by `git clean`. Staging it immediately means the new class
shows up in the diff that is being reviewed and discussed.

Staging is not committing: `git commit` and `git push` still happen only when explicitly asked
for.

# Bug reports

## 1. Write every bug in the Steps / Expected / Actual shape

When a test uncovers wrong behaviour, the test itself gets `@Disabled` + `@Bug("<ticket id>")`
and asserts the *correct* behaviour, and the ticket describing it is written in English, in
this exact shape - so it can be pasted into the bug tracker as is:

````markdown
## API -> <Area> : <endpoint or subject> <what is wrong>

**Steps:**

1. <one short sentence saying what is being done>

POST `https://<stand url>/<endpoint>`

```json
{
  "name": "No start",
  "end": 1790236800000
}
```

**Expected result:**

- Response code 400

```json
{"status":"error","message":"ERR_INVALID_DATE"}
```

**Actual result:**

- Response code 200
- <further observed facts, one bullet each>
````

Rules for filling it in:

- **Summary**: `API -> <Area> : <subject> <problem>`. For a UI bug the same shape without the
  `API` prefix: `<Page> -> <Section> : <problem>`.
- **Steps** are numbered sentences. Under each one put the real request that reproduces it: the
  method, the full URL against the stand it was measured on (real ids, not placeholders), and the
  exact request body in a `json` block. A step whose response matters for the next step carries
  that response right under it.
- **Expected result** and **Actual result** are bullet lists. The response code is always its own
  first bullet, and the response body goes in a `json` block under it. Anything else worth
  recording - a `GET` that gives the bad value back, a neighbouring case that behaves correctly and
  shows where the line is - is a further bullet.
- Keep the values concrete: timestamps, ids and names are the ones actually seen in the run.
- Never paste real passwords or tokens into a report - replace them with `*****`.

## 2. Don't create bug tracker issues without being asked

Write the drafts in the chat and let them be reviewed. Creating, editing or transitioning an
issue happens only on an explicit go-ahead, and then the keys go into the `@Bug(...)`
annotations that were left as placeholders.
