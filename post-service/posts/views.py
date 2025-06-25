from rest_framework import viewsets, status, permissions
from rest_framework.decorators import action
from rest_framework.response import Response
from django_filters.rest_framework import DjangoFilterBackend
from rest_framework.filters import SearchFilter, OrderingFilter
from django.shortcuts import get_object_or_404
from django.db.models import Q

from .models import Post, Comment, Like, PostShare
from .serializers import (
    PostSerializer, PostCreateSerializer, PostUpdateSerializer,
    CommentSerializer, CommentCreateSerializer, CommentUpdateSerializer,
    LikeSerializer, PostShareSerializer
)


class PostViewSet(viewsets.ModelViewSet):
    """ViewSet for Post model."""
    queryset = Post.objects.filter(is_deleted=False)
    filter_backends = [DjangoFilterBackend, SearchFilter, OrderingFilter]
    filterset_fields = ['author_id', 'created_at']
    search_fields = ['content']
    ordering_fields = ['created_at', 'likes_count', 'comments_count']
    ordering = ['-created_at']
    
    def get_serializer_class(self):
        if self.action == 'create':
            return PostCreateSerializer
        elif self.action in ['update', 'partial_update']:
            return PostUpdateSerializer
        return PostSerializer
    
    def get_queryset(self):
        """Filter posts based on query parameters."""
        queryset = super().get_queryset()
        
        # Filter by author
        author_id = self.request.query_params.get('author_id', None)
        if author_id:
            queryset = queryset.filter(author_id=author_id)
        
        # Filter by date range
        date_from = self.request.query_params.get('date_from', None)
        date_to = self.request.query_params.get('date_to', None)
        if date_from:
            queryset = queryset.filter(created_at__gte=date_from)
        if date_to:
            queryset = queryset.filter(created_at__lte=date_to)
        
        return queryset
    
    def perform_create(self, serializer):
        """Set the author_id when creating a post."""
        # In a real application, this would come from the authenticated user
        # For now, we'll use a placeholder or get it from request
        author_id = getattr(self.request.user, 'id', 'default_user_id')
        serializer.save(author_id=author_id)
    
    def perform_destroy(self, instance):
        """Soft delete the post."""
        instance.is_deleted = True
        instance.save()
    
    @action(detail=True, methods=['post'])
    def like(self, request, pk=None):
        """Like or unlike a post."""
        post = self.get_object()
        user_id = getattr(request.user, 'id', 'default_user_id')
        
        like, created = Like.objects.get_or_create(
            post=post,
            user_id=user_id,
            defaults={'is_active': True}
        )
        
        if not created:
            # Toggle like status
            like.is_active = not like.is_active
            like.save()
        
        return Response({
            'liked': like.is_active,
            'likes_count': post.likes_count
        })
    
    @action(detail=True, methods=['post'])
    def share(self, request, pk=None):
        """Share a post."""
        post = self.get_object()
        user_id = getattr(request.user, 'id', 'default_user_id')
        share_message = request.data.get('share_message', '')
        
        share = PostShare.objects.create(
            original_post=post,
            shared_by=user_id,
            share_message=share_message
        )
        
        serializer = PostShareSerializer(share)
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    
    @action(detail=False, methods=['get'])
    def feed(self, request):
        """Get a feed of posts (could be personalized based on user)."""
        # This is a simple implementation - in a real app, you'd implement
        # more sophisticated feed algorithms
        posts = self.get_queryset()
        page = self.paginate_queryset(posts)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)
        
        serializer = self.get_serializer(posts, many=True)
        return Response(serializer.data)


class CommentViewSet(viewsets.ModelViewSet):
    """ViewSet for Comment model."""
    queryset = Comment.objects.filter(is_deleted=False)
    filter_backends = [DjangoFilterBackend, OrderingFilter]
    filterset_fields = ['post', 'author_id', 'parent_comment']
    ordering_fields = ['created_at']
    ordering = ['created_at']
    
    def get_serializer_class(self):
        if self.action == 'create':
            return CommentCreateSerializer
        elif self.action in ['update', 'partial_update']:
            return CommentUpdateSerializer
        return CommentSerializer
    
    def get_queryset(self):
        """Filter comments based on query parameters."""
        queryset = super().get_queryset()
        
        # Filter by post
        post_id = self.request.query_params.get('post_id', None)
        if post_id:
            queryset = queryset.filter(post_id=post_id)
        
        # Filter by author
        author_id = self.request.query_params.get('author_id', None)
        if author_id:
            queryset = queryset.filter(author_id=author_id)
        
        return queryset
    
    def perform_create(self, serializer):
        """Set the author_id when creating a comment."""
        author_id = getattr(self.request.user, 'id', 'default_user_id')
        serializer.save(author_id=author_id)
    
    def perform_destroy(self, instance):
        """Soft delete the comment."""
        instance.is_deleted = True
        instance.save()
    
    @action(detail=True, methods=['get'])
    def replies(self, request, pk=None):
        """Get replies to a specific comment."""
        comment = self.get_object()
        replies = comment.replies.filter(is_deleted=False)
        serializer = self.get_serializer(replies, many=True)
        return Response(serializer.data)


class LikeViewSet(viewsets.ReadOnlyModelViewSet):
    """ViewSet for Like model (read-only)."""
    queryset = Like.objects.filter(is_active=True)
    serializer_class = LikeSerializer
    filter_backends = [DjangoFilterBackend]
    filterset_fields = ['post', 'user_id']
    
    @action(detail=False, methods=['get'])
    def user_likes(self, request):
        """Get all posts liked by the current user."""
        user_id = getattr(request.user, 'id', 'default_user_id')
        likes = self.queryset.filter(user_id=user_id)
        serializer = self.get_serializer(likes, many=True)
        return Response(serializer.data)


class PostShareViewSet(viewsets.ReadOnlyModelViewSet):
    """ViewSet for PostShare model (read-only)."""
    queryset = PostShare.objects.all()
    serializer_class = PostShareSerializer
    filter_backends = [DjangoFilterBackend]
    filterset_fields = ['original_post', 'shared_by'] 