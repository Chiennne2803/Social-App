from django.db import models
from django_mongodb_backend.fields import EmbeddedModelArrayField, ArrayField
from django_mongodb_backend.models import EmbeddedModel

class SubComment(EmbeddedModel):
    userId = models.CharField(max_length=255)
    content = models.TextField()
    time = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"SubComment by {self.userId}: {self.content[:30]}..."

class Interaction(EmbeddedModel):
    class InteractionTypes(models.TextChoices):
        LIKE = 'like', 'Like'
        SHARE = 'share', 'Share'
    userId = models.CharField(max_length=255)
    type = models.CharField(max_length=10, choices=InteractionTypes.choices)
    time = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.userId} {self.type}"

class Comment(EmbeddedModel):
    userId = models.CharField(max_length=255)
    content = models.TextField()
    time = models.DateTimeField(auto_now_add=True)
    interactions = EmbeddedModelArrayField(Interaction, default=list)
    subComments = EmbeddedModelArrayField(SubComment, default=list)

    def __str__(self):
        return f"Comment by {self.userId}: {self.content[:30]}..."

class Post(models.Model):
    class AccessLevel(models.TextChoices):
        PUBLIC = 'public', 'Public'
        FRIENDS = 'friends', 'Friends'
        PRIVATE = 'private', 'Private'
    userId = models.CharField(max_length=255)
    content = models.TextField()
    images = ArrayField(models.CharField(max_length=255), default=list)
    access = models.CharField(max_length=10, choices=AccessLevel.choices, default=AccessLevel.PUBLIC)
    interactions = EmbeddedModelArrayField(Interaction, default=list)
    comments = EmbeddedModelArrayField(Comment, default=list)
    saved = models.BooleanField(default=False)
    pinned = models.BooleanField(default=False)
    createdAt = models.DateTimeField(auto_now_add=True)
    updatedAt = models.DateTimeField(auto_now=True)

    class Meta:
        ordering = ['-createdAt']

    def __str__(self):
        return f"Post by {self.userId}: {self.content[:50]}..."
