from django.contrib import admin
from .models import Post, Comment, Like, PostShare


@admin.register(Post)
class PostAdmin(admin.ModelAdmin):
    list_display = ['id', 'author_id', 'content', 'created_at', 'is_deleted']
    list_filter = ['created_at', 'is_deleted']
    search_fields = ['content', 'author_id']
    readonly_fields = ['id', 'created_at', 'updated_at']
    list_per_page = 20


@admin.register(Comment)
class CommentAdmin(admin.ModelAdmin):
    list_display = ['id', 'post', 'author_id', 'content', 'parent_comment', 'created_at', 'is_deleted']
    list_filter = ['created_at', 'is_deleted']
    search_fields = ['content', 'author_id']
    readonly_fields = ['id', 'created_at', 'updated_at']
    list_per_page = 20


@admin.register(Like)
class LikeAdmin(admin.ModelAdmin):
    list_display = ['id', 'post', 'user_id', 'created_at', 'is_active']
    list_filter = ['created_at', 'is_active']
    search_fields = ['user_id']
    readonly_fields = ['id', 'created_at']
    list_per_page = 20


@admin.register(PostShare)
class PostShareAdmin(admin.ModelAdmin):
    list_display = ['id', 'original_post', 'shared_by', 'share_message', 'shared_at']
    list_filter = ['shared_at']
    search_fields = ['shared_by', 'share_message']
    readonly_fields = ['id', 'shared_at']
    list_per_page = 20 