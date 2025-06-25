from django.db import models
from django.contrib.auth.models import User
from django.utils import timezone
import uuid


class Post(models.Model):
    """Model for social media posts."""
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    author_id = models.CharField(max_length=100)  # User ID from identity service
    content = models.TextField()
    image = models.ImageField(upload_to='posts/', blank=True, null=True)
    created_at = models.DateTimeField(default=timezone.now)
    updated_at = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    class Meta:
        ordering = ['-created_at']
        indexes = [
            models.Index(fields=['author_id']),
            models.Index(fields=['created_at']),
            models.Index(fields=['is_deleted']),
        ]
    
    def __str__(self):
        return f"Post by {self.author_id} - {self.created_at}"
    
    @property
    def likes_count(self):
        return self.likes.filter(is_active=True).count()
    
    @property
    def comments_count(self):
        return self.comments.filter(is_deleted=False).count()


class Comment(models.Model):
    """Model for comments on posts."""
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='comments')
    author_id = models.CharField(max_length=100)  # User ID from identity service
    content = models.TextField()
    parent_comment = models.ForeignKey('self', on_delete=models.CASCADE, null=True, blank=True, related_name='replies')
    created_at = models.DateTimeField(default=timezone.now)
    updated_at = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    class Meta:
        ordering = ['created_at']
        indexes = [
            models.Index(fields=['post']),
            models.Index(fields=['author_id']),
            models.Index(fields=['created_at']),
            models.Index(fields=['is_deleted']),
        ]
    
    def __str__(self):
        return f"Comment by {self.author_id} on {self.post.id}"


class Like(models.Model):
    """Model for likes on posts."""
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='likes')
    user_id = models.CharField(max_length=100)  # User ID from identity service
    created_at = models.DateTimeField(default=timezone.now)
    is_active = models.BooleanField(default=True)
    
    class Meta:
        unique_together = ['post', 'user_id']
        indexes = [
            models.Index(fields=['post']),
            models.Index(fields=['user_id']),
            models.Index(fields=['is_active']),
        ]
    
    def __str__(self):
        return f"Like by {self.user_id} on {self.post.id}"


class PostShare(models.Model):
    """Model for tracking post shares."""
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    original_post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='shares')
    shared_by = models.CharField(max_length=100)  # User ID from identity service
    shared_at = models.DateTimeField(default=timezone.now)
    share_message = models.TextField(blank=True)  # Optional message when sharing
    
    class Meta:
        ordering = ['-shared_at']
        indexes = [
            models.Index(fields=['original_post']),
            models.Index(fields=['shared_by']),
            models.Index(fields=['shared_at']),
        ]
    
    def __str__(self):
        return f"Share by {self.shared_by} of {self.original_post.id}" 