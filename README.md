# DRY Code Snippets Repository API

## Purpose

The DRY Code Snippets Repository API helps developers store, share, and retrieve reusable code snippets. By promoting the **DRY (Don't Repeat Yourself)** principle, this API saves time and effort, enhances maintainability, and encourages better code practices. Developers can easily find and contribute high-quality code snippets for common tasks, reducing code duplication across projects.

## API Documentation

This API follows RESTful principles and offers the following endpoints:

### Authentication
- **POST /api/login**: 
    - Log in and get a JWT token.

### Snippet Management
- **GET /api/snippets?tags={tags}&language={langauge}**: 
    - Retrieve a list of all code snippets with optional filters (tags ( ; separated), language)
- **GET /api/snippets/{id}/explain**: 
    - Uses AI to explain the code snippet
- **POST /api/snippets**: 
    - Create a new code snippet.
    - Body: {"code":"This is code","language":"This is a language","description":"This is a description","title":"This is a title","tags":["Tag 1","Tag 2"]}
- **GET /api/snippets/{id}**: 
    - Retrieve details of a specific snippet.
- **PUT /api/snippets/{id}**: 
    - Update an existing snippet (Add a new version).
- **DELETE /api/snippets/{id}**: 
    - Delete a snippet.

### Snippet Versioning
- **GET /api/snippets/versions?snippetId={id}**: 
    - Retrieve all versions of a specific snippet.
- **GET /api/snippets/versions/byVersion?snippetId={id}&version={version}**: 
    - View a specific version of a snippet.

### Comments
- **GET /api/snippets/comments?snippetId={id}**: 
    - Gets all comments for a specific snippet
- **POST /api/snippets/comments?snippetId={id}**: 
    - Add a comment to a snippet
    - body: comment

### Snippet Rating
- **GET /api/snippets/ratings?snippetId={id}**: 
    - Gets the ratings for a snippet
- **POST /api/snippets/ratings?snippetId={id}**: 
     - Adds a rating to a snippet
     - body: rating(Integer between 0 and 10)



## CLI Documentation

The CLI provides an interface to interact with the api using picocli.

### Authentication
- **login**: Login with Google.

### Snippet Management
- **list-snippets**: Lists all code snippets that match the filters
- **get-snippet**: Displays a code snippet
- **explain-snippet**: Explains code snippet using AI
- **add-snippet**: Adds a new code snippet
- **delete-snippet**: Delete a snippet

### Snippet Versioning
- **list-snippet-versions**: Lists all versions of the code snippet specified by id
- **get-version**: Displays a version of a snippet
- **add-version**: Adds new version to a snippet

### Comments
- **list-snippet-comments**: Lists all comments of a snippet
- **add-comment**: Comment on a code snippet

### Snippet Rating
- **get-ratings**: Lists all ratings of the snippet
- **add-rating**: Rate a code snippet