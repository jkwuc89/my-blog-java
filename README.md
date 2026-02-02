# My Blog (Java/Spring Boot)

A personal blog website built with Spring Boot 4.0.2, featuring blog posts, presentations, and an about page with admin interface for content management. This is a parallel implementation of the Rails version, built to refamiliarize with the Java/Spring ecosystem.

## Purpose

This project is a Java/Spring Boot implementation of a personal blog site, providing:
- Blog posts written in Markdown (stored in `src/main/resources/static/blog_posts/`)
- Presentations with slide decks (stored in `src/main/resources/static/presentations/`)
- About page with bio and resume link
- Admin interface for managing all content
- Sticky header and footer with social media links

## Technology Stack

- **Framework:** Spring Boot 4.0.2
- **Language:** Java 25
- **Build Tool:** Gradle 9.3
- **Database:** SQLite3 (development and production)
- **Frontend:** 
  - Tailwind CSS for styling
  - Thymeleaf for server-rendered HTML templates
- **Markdown:** CommonMark for rendering Markdown content
- **Icons:** SVG icons for social media links
- **Authentication:** Spring Security with session-based authentication
- **ORM:** Spring Data JPA with Hibernate
- **Migrations:** Flyway
- **Deployment:** Kamal (see [Deployment Guide](#deployment))

## Prerequisites

- Java 25 (Temurin distribution recommended)
- Gradle 9.3
- SQLite3
- Node.js (for Tailwind CSS compilation, if needed)

## Local Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd my-blog-java
   ```

2. **Build the project:**
   ```bash
   ./gradlew build
   ```

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```
   
   Or run the JAR directly:
   ```bash
   java -jar build/libs/my-blog-java-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application:**
   - Public site: http://localhost:8080
   - Admin interface: http://localhost:8080/admin (requires login)

## Creating an Admin User

1. Start the application and access the database:
   ```bash
   sqlite3 storage/java_development.sqlite3
   ```

2. Create a user (password will be BCrypt hashed):
   
   **Using Gradle task (recommended):**
   ```bash
   ./gradlew hashPassword -Ppassword=your-password
   ```
   This will output the hash and a ready-to-use SQL INSERT statement.
   
   **Using jshell:**
   ```bash
   ./gradlew compileJava
   jshell --class-path "$(./gradlew -q dependencies --configuration runtimeClasspath | grep spring-security-crypto | head -1 | awk '{print $NF}')"
   ```
   Then in jshell:
   ```java
   import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
   System.out.println(encoder.encode("your-password"));
   ```
   
   Then insert the user into the database:
   ```sql
   INSERT INTO users (email_address, password_digest, created_at, updated_at)
   VALUES ('your-email@example.com', '<hash-from-above>', datetime('now'), datetime('now'));
   ```

3. You can now log in at http://localhost:8080/session/new

## Development Workflow

- **Run migrations:** Flyway runs automatically on application startup
- **View database:** Use SQLite browser or `sqlite3 storage/java_development.sqlite3`
- **Run tests:** `./gradlew test`
- **Build JAR:** `./gradlew build`
- **Run application:** `./gradlew bootRun`

## Project Structure

```
src/
  main/
    java/com/kwedinger/blog/
      config/                      # Spring configuration classes
        SecurityConfig.java        # Spring Security configuration
        ThymeleafConfig.java       # Thymeleaf configuration
        ViewHelper.java            # View helper utilities
      controller/                  # MVC controllers
        BlogPostsController.java   # Public blog posts
        PresentationsController.java # Public presentations
        PagesController.java       # About page
        SessionsController.java    # Login/logout
        admin/                     # Admin controllers
      model/                       # JPA entities
        User.java                  # User entity
        Session.java               # Session entity
        Bio.java                   # Bio content (singleton)
        ContactInfo.java           # Contact info (singleton)
        BlogPost.java              # Blog posts
        Presentation.java          # Presentations
        Conference.java            # Conferences
        ConferencePresentation.java # Conference associations
      repository/                  # Spring Data JPA repositories
      service/                     # Business logic services
        BlogPostFileReader.java    # Reads blog post markdown files
        MarkdownService.java       # Markdown rendering
        FileService.java           # File listing utilities
        BioService.java            # Bio singleton service
        ContactInfoService.java    # Contact info singleton service
      security/                    # Security-related classes
        CustomAuthenticationProvider.java # Custom auth provider
    resources/
      db/migration/                # Flyway migration scripts
        V1__initial_schema.sql     # Initial database schema
      static/                      # Static assets
        blog_posts/                # Markdown blog post files
        presentations/             # PowerPoint presentation files
        documents/                 # Resume and other documents
      templates/                   # Thymeleaf templates
        layouts/                   # Layout templates
        blog_posts/                # Blog post views
        presentations/             # Presentation views
        pages/                     # About page
        sessions/                  # Login page
        admin/                     # Admin interface views
      application.properties       # Spring Boot configuration
```

## Features

### Content Management
- **File-Based Blog Posts:** Blog posts are stored as Markdown files in `src/main/resources/static/blog_posts/`. Admin interface provides a dropdown to select from available files.
- **File-Based Presentations:** Presentation slide decks are stored in `src/main/resources/static/presentations/`. Admin interface provides a dropdown to select from available `.pptx` files.
- **GitHub Integration:** Presentations can include GitHub repository links that are displayed alongside slide deck downloads.

### User Interface
- **Sticky Header:** Fixed header with profile photo (with border), name, bio tagline, and navigation
- **Sticky Footer:** Fixed footer with social media icon links (Email, GitHub, LinkedIn, Twitter/X, Untappd) with hover effects
- **Responsive Design:** Mobile-friendly layout using Tailwind CSS
- **Hover Effects:** Footer icons feature border/outline highlight on hover

### Content Features
- **Blog:** Markdown-based blog posts with automatic excerpt generation
- **Presentations:** List of presentations with conference associations, slide deck downloads, and GitHub repository links
- **About Page:** Bio content (Markdown), resume download link, and contact information
- **Markdown Support:** CommonMark for rendering Markdown in blog posts and bio content

### Admin Interface
- **Full CRUD:** Complete create, read, update, delete interface for all content types
- **File Selection:** Dropdown selectors for blog posts and presentation files
- **Content Management:** Manage bio, contact info, presentations, conferences, and blog posts

## Database

The application uses SQLite3 for both development and production. The database is stored in:
- **Development:** `storage/java_development.sqlite3`
- **Production:** `/app/storage/java_production.sqlite3` (inside container)

Flyway migrations are located in `src/main/resources/db/migration/` and run automatically on application startup.

## Deployment

The application is deployed using Kamal to a DigitalOcean droplet. The Java version is accessible at `https://jkwuc89.com/java` while the Rails version remains at `https://jkwuc89.com`.

### Deployment Configuration

- **Kamal config:** `config/deploy.yml`
- **Dockerfile:** Multi-stage build using Gradle 9.3 and JDK 25
- **Registry:** GitHub Container Registry (ghcr.io)
- **Deployment script:** `bin/deploy`

### Deployment Commands

- **Deploy:** `bin/deploy`
- **View logs:** `bin/kamal app logs -f`
- **Access shell:** `bin/kamal app exec --interactive --reuse "bash"`
- **Database console:** `bin/kamal app exec --interactive --reuse "sqlite3 /app/storage/java_production.sqlite3"`

## CI/CD

GitHub Actions workflow (`.github/workflows/ci.yml`) runs on:
- Pull requests
- Pushes to `main` branch

The workflow:
- Builds the application with Gradle
- Runs all tests
- Uploads test results and build artifacts

## Differences from Rails Version

- **Database:** Separate SQLite database (`java_development.sqlite3` vs `development.sqlite3`)
- **Static Assets:** Copied from Rails project to `src/main/resources/static/`
- **Templating:** Thymeleaf instead of ERB
- **Markdown:** CommonMark instead of Kramdown
- **Authentication:** Spring Security instead of Rails authentication
- **ORM:** Spring Data JPA instead of ActiveRecord
- **Migrations:** Flyway instead of Rails migrations

## License

Private project - All rights reserved
