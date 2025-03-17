
CREATE TABLE "users" (
  "user_id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "user_guid" UUID  NOT NULL,
  "created_at" TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE TABLE "languages" (
  "language_id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "language_name" VARCHAR(256) NOT NULL
);

CREATE TABLE "snippets" (
  "snippet_id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "user_id" INT NOT NULL,
  "title" VARCHAR(256) NOT NULL,
  "description" VARCHAR(1000) NOT NULL,
  "language_id" INT NOT NULL,
  "is_deleted" BOOLEAN  NOT NULL DEFAULT FALSE,
  "created_at" TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE "versions" (
  "version_id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "snippet_id" INT NOT NULL,
  "version" INT NOT NULL,
  "code" VARCHAR(10000) NOT NULL,
  "created_at" TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE "tags" (
  "tag_id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "tag_name" VARCHAR(256) NOT NULL
);

CREATE TABLE "snippettags" (
  "snippet_id" INT NOT NULL,
  "tag_id" INT NOT NULL
);

CREATE TABLE "ratings" (
  "rating_id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "snippet_id" INT NOT NULL,
  "user_id" INT NOT NULL,
  "rating" INT NOT NULL,
  "created_at" TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE "comments" (
  "comment_id" INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "snippet_id" INT NOT NULL,
  "user_id" INT NOT NULL,
  "comment" VARCHAR(256) NOT NULL,
  "created_at" TIMESTAMP NOT NULL DEFAULT NOW()
);

ALTER TABLE "snippets" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "snippets" ADD FOREIGN KEY ("language_id") REFERENCES "languages" ("language_id");

ALTER TABLE "versions" ADD FOREIGN KEY ("snippet_id") REFERENCES "snippets" ("snippet_id");

ALTER TABLE "snippettags" ADD FOREIGN KEY ("snippet_id") REFERENCES "snippets" ("snippet_id");

ALTER TABLE "snippettags" ADD FOREIGN KEY ("tag_id") REFERENCES "tags" ("tag_id");

ALTER TABLE "ratings" ADD FOREIGN KEY ("snippet_id") REFERENCES "snippets" ("snippet_id");

ALTER TABLE "ratings" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "comments" ADD FOREIGN KEY ("snippet_id") REFERENCES "snippets" ("snippet_id");

ALTER TABLE "comments" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");