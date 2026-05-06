# ArtConnect Pro - Local Art Community Platform

## Overview
ArtConnect Pro is a JavaFX-based management system for local art communities. It allows managing artists, artworks, exhibitions, galleries, workshops, and community members.

This project is a skeleton designed for students to practice:
1. **Layered Architecture**: Presentation, Service, DAO, and Model layers.
2. **Database Persistence**: Implementing JDBC DAOs to connect to a MySQL database.
3. **JavaFX UI**: Working with FXML, TableViews, and Controllers.

## Project Structure
- `com.project.artconnect.MainApp`: Entry point.
- `com.project.artconnect.model`: Domain entities (POJOs/Stubs).
- `com.project.artconnect.dao`: Data Access Object interfaces.
- `com.project.artconnect.persistence`: JDBC implementations (TODO: Students implement these).
- `com.project.artconnect.service`: Business logic layer.
- `com.project.artconnect.ui`: JavaFX Controllers and FXML views.
- `com.project.artconnect.util`: Utility classes like `ConnectionManager` and `ServiceProvider`.

## How to Run
Requirement: Java 17+ and Maven installed.

```bash
mvn clean javafx:run
```

The application runs "out-of-the-box" using **In-Memory Services** (`InMemoryArtistService`, etc.) located in `com.project.artconnect.service.impl`. This allows immediate demonstration of the UI with dummy data.

## OOP-First Design (Object-Oriented Programming)
Unlike typical database-centric skeletons, ArtConnect Pro follows strict OOP best practices:
- **Explicit IDs in Models**: Model classes (`Artist`, `Artwork`, etc.) include a `Long id` field to map cleanly to relational primary keys.
- **Direct Object References**: Relationships are modeled using direct references. For example, an `Artwork` object holds a reference to an `Artist` object, not an `artistId`.
- **Bidirectional Links**: Many relationships are bidirectional (e.g., an `Artist` has a `List<Artwork>`, and each `Artwork` points back to its `Artist`).
- **No Junction Tables**: Many-to-Many relationships (like Exhibitions and Artworks) are modeled using simple collections (`List<Artwork>`) rather than separate junction classes.

## Student Tasks (The Challenge)
1. **ID Mapping**: Use the `id` field already present in Java models as the primary key mapping for JDBC (`findAll`, `save`, `update`, `delete`).
2. **Relational Mapping**: You must implement the logic to reconstruct the object graph from relational tables. When fetching an `Artwork`, you must also fetch/link the corresponding `Artist`.
3. **Database Setup**: Create the MySQL database and tables with IDs and foreign keys that map to the model references.
4. **JDBC Implementation**: Implement the `Jdbc` DAO classes in `com.project.artconnect.persistence`.
5. **Service Swap**: Update `ServiceProvider` to use your new `Jdbc` DAOs.

## Current API Compatibility Note
- Some service methods still expose legacy natural-key lookups (for example `getArtistByName` or `getArtworkByTitle`) to keep the current UI flow unchanged.
- For JDBC and database operations, prefer ID-based updates/deletes (`WHERE id = ?`) even when legacy lookup methods are still present.

## Architecture Diagram
```mermaid
graph TD
    UI[JavaFX Presentation Layer] --> Service[Service Layer]
    Service --> DAO[DAO Interfaces]
    DAO --> JDBC[JDBC Persistence Implementation]
    DAO --> InMemory[InMemory Mock Implementation]
    JDBC --> DB[(MySQL Database)]
```

## Testing Instructions
1. Launch the app and verify all 7 tabs show dummy data.
2. Search for an artist by name or filter by discipline in the Artists Tab.
3. View the "Discover" tab to see featured content dynamically generated.
4. Once you implement JDBC, swap the `ServiceProvider` to use your `JdbcArtistDao` and verify data is fetched from MySQL.
