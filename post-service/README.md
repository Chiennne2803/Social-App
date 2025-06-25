# Post Service

A Django-based microservice for handling social media posts, comments, likes, and shares in the Social App.

## Features

- **Posts Management**: Create, read, update, and delete social media posts
- **Comments System**: Add comments to posts with nested replies support
- **Likes System**: Like and unlike posts
- **Sharing**: Share posts with optional messages
- **RESTful API**: Full REST API with filtering, searching, and pagination
- **Image Support**: Upload and store images with posts
- **Soft Deletes**: Posts and comments are soft deleted for data integrity

## Technology Stack

- **Framework**: Django 5.0.2
- **API**: Django REST Framework 3.14.0
- **Database**: PostgreSQL
- **Task Queue**: Celery with Redis
- **Image Processing**: Pillow
- **Testing**: pytest with pytest-django

## Project Structure

```
post-service/
├── post_service/          # Main Django project
│   ├── __init__.py
│   ├── settings.py        # Django settings
│   ├── urls.py           # Main URL configuration
│   ├── wsgi.py           # WSGI configuration
│   └── asgi.py           # ASGI configuration
├── posts/                # Posts app
│   ├── __init__.py
│   ├── admin.py          # Django admin configuration
│   ├── apps.py           # App configuration
│   ├── models.py         # Database models
│   ├── serializers.py    # DRF serializers
│   ├── urls.py           # App URL patterns
│   ├── views.py          # API views
│   └── tests.py          # Test cases
├── manage.py             # Django management script
├── requirements.txt      # Python dependencies
├── env.example           # Environment variables example
└── README.md            # This file
```

## Models

### Post
- `id`: UUID primary key
- `author_id`: User ID from identity service
- `content`: Post text content
- `image`: Optional image file
- `created_at`: Creation timestamp
- `updated_at`: Last update timestamp
- `is_deleted`: Soft delete flag

### Comment
- `id`: UUID primary key
- `post`: Foreign key to Post
- `author_id`: User ID from identity service
- `content`: Comment text
- `parent_comment`: Self-referencing for nested comments
- `created_at`: Creation timestamp
- `updated_at`: Last update timestamp
- `is_deleted`: Soft delete flag

### Like
- `id`: UUID primary key
- `post`: Foreign key to Post
- `user_id`: User ID from identity service
- `created_at`: Creation timestamp
- `is_active`: Like status (for toggling)

### PostShare
- `id`: UUID primary key
- `original_post`: Foreign key to Post
- `shared_by`: User ID from identity service
- `share_message`: Optional message when sharing
- `shared_at`: Share timestamp

## API Endpoints

### Posts
- `GET /api/v1/posts/` - List all posts
- `POST /api/v1/posts/` - Create a new post
- `GET /api/v1/posts/{id}/` - Get a specific post
- `PUT /api/v1/posts/{id}/` - Update a post
- `PATCH /api/v1/posts/{id}/` - Partially update a post
- `DELETE /api/v1/posts/{id}/` - Soft delete a post
- `POST /api/v1/posts/{id}/like/` - Like/unlike a post
- `POST /api/v1/posts/{id}/share/` - Share a post
- `GET /api/v1/posts/feed/` - Get posts feed

### Comments
- `GET /api/v1/comments/` - List all comments
- `POST /api/v1/comments/` - Create a new comment
- `GET /api/v1/comments/{id}/` - Get a specific comment
- `PUT /api/v1/comments/{id}/` - Update a comment
- `PATCH /api/v1/comments/{id}/` - Partially update a comment
- `DELETE /api/v1/comments/{id}/` - Soft delete a comment
- `GET /api/v1/comments/{id}/replies/` - Get replies to a comment

### Likes
- `GET /api/v1/likes/` - List all likes
- `GET /api/v1/likes/{id}/` - Get a specific like
- `GET /api/v1/likes/user_likes/` - Get user's likes

### Shares
- `GET /api/v1/shares/` - List all shares
- `GET /api/v1/shares/{id}/` - Get a specific share

## Setup Instructions

### Prerequisites
- Python 3.8+
- PostgreSQL
- Redis (for Celery)

### Installation

1. **Clone the repository**
   ```bash
   cd post-service
   ```

2. **Create virtual environment**
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

4. **Set up environment variables**
   ```bash
   cp env.example .env
   # Edit .env with your configuration
   ```

5. **Set up database**
   ```bash
   # Create PostgreSQL database
   createdb post_service
   
   # Run migrations
   python manage.py makemigrations
   python manage.py migrate
   ```

6. **Create superuser (optional)**
   ```bash
   python manage.py createsuperuser
   ```

7. **Run the development server**
   ```bash
   python manage.py runserver 8082
   ```

### Running Tests
```bash
python manage.py test
```

### Running with Docker (optional)
```bash
# Build and run with docker-compose
docker-compose up --build
```

## Configuration

### Environment Variables
- `SECRET_KEY`: Django secret key
- `DEBUG`: Debug mode (True/False)
- `ALLOWED_HOSTS`: Comma-separated list of allowed hosts
- `DB_NAME`: PostgreSQL database name
- `DB_USER`: PostgreSQL username
- `DB_PASSWORD`: PostgreSQL password
- `DB_HOST`: PostgreSQL host
- `DB_PORT`: PostgreSQL port
- `CELERY_BROKER_URL`: Redis URL for Celery
- `CELERY_RESULT_BACKEND`: Redis URL for Celery results

### Database Configuration
The service uses PostgreSQL by default. You can modify the database settings in `post_service/settings.py`.

### CORS Configuration
CORS is configured to allow requests from the frontend application. Update `CORS_ALLOWED_ORIGINS` in settings for production.

## API Usage Examples

### Create a Post
```bash
curl -X POST http://localhost:8082/api/v1/posts/ \
  -H "Content-Type: application/json" \
  -d '{"content": "Hello, world!"}'
```

### Get Posts Feed
```bash
curl http://localhost:8082/api/v1/posts/feed/
```

### Like a Post
```bash
curl -X POST http://localhost:8082/api/v1/posts/{post_id}/like/
```

### Add a Comment
```bash
curl -X POST http://localhost:8082/api/v1/comments/ \
  -H "Content-Type: application/json" \
  -d '{"post": "post_id", "content": "Great post!"}'
```

## Integration with Other Services

This post service is designed to work with:
- **Identity Service**: For user authentication and authorization
- **Profile Service**: For user profile information
- **API Gateway**: For routing and load balancing

The service uses user IDs from the identity service and can be extended to fetch user profile information from the profile service as needed.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Run the test suite
6. Submit a pull request

## License

This project is part of the Social App microservices architecture. 