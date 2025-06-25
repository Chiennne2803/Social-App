from rest_framework import serializers
from .models import Post, Comment, Like, PostShare


class CommentSerializer(serializers.ModelSerializer):
    """Serializer for Comment model."""
    author_id = serializers.CharField(read_only=True)
    replies = serializers.SerializerMethodField()
    
    class Meta:
        model = Comment
        fields = ['id', 'post', 'author_id', 'content', 'parent_comment', 'replies', 'created_at', 'updated_at']
        read_only_fields = ['id', 'author_id', 'created_at', 'updated_at']
    
    def get_replies(self, obj):
        """Get nested replies for a comment."""
        replies = obj.replies.filter(is_deleted=False)
        return CommentSerializer(replies, many=True).data


class LikeSerializer(serializers.ModelSerializer):
    """Serializer for Like model."""
    user_id = serializers.CharField(read_only=True)
    
    class Meta:
        model = Like
        fields = ['id', 'post', 'user_id', 'created_at', 'is_active']
        read_only_fields = ['id', 'user_id', 'created_at']


class PostShareSerializer(serializers.ModelSerializer):
    """Serializer for PostShare model."""
    shared_by = serializers.CharField(read_only=True)
    
    class Meta:
        model = PostShare
        fields = ['id', 'original_post', 'shared_by', 'share_message', 'shared_at']
        read_only_fields = ['id', 'shared_by', 'shared_at']


class PostSerializer(serializers.ModelSerializer):
    """Serializer for Post model."""
    author_id = serializers.CharField(read_only=True)
    comments = CommentSerializer(many=True, read_only=True)
    likes = LikeSerializer(many=True, read_only=True)
    shares = PostShareSerializer(many=True, read_only=True)
    likes_count = serializers.ReadOnlyField()
    comments_count = serializers.ReadOnlyField()
    is_liked_by_user = serializers.SerializerMethodField()
    
    class Meta:
        model = Post
        fields = [
            'id', 'author_id', 'content', 'image', 'created_at', 'updated_at',
            'comments', 'likes', 'shares', 'likes_count', 'comments_count',
            'is_liked_by_user'
        ]
        read_only_fields = ['id', 'author_id', 'created_at', 'updated_at', 'likes_count', 'comments_count']
    
    def get_is_liked_by_user(self, obj):
        """Check if the current user has liked this post."""
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            user_id = getattr(request.user, 'id', None)
            if user_id:
                return obj.likes.filter(user_id=user_id, is_active=True).exists()
        return False


class PostCreateSerializer(serializers.ModelSerializer):
    """Serializer for creating posts."""
    author_id = serializers.CharField(read_only=True)
    
    class Meta:
        model = Post
        fields = ['content', 'image', 'author_id']
        read_only_fields = ['author_id']


class PostUpdateSerializer(serializers.ModelSerializer):
    """Serializer for updating posts."""
    
    class Meta:
        model = Post
        fields = ['content', 'image']


class CommentCreateSerializer(serializers.ModelSerializer):
    """Serializer for creating comments."""
    author_id = serializers.CharField(read_only=True)
    
    class Meta:
        model = Comment
        fields = ['post', 'content', 'parent_comment', 'author_id']
        read_only_fields = ['author_id']


class CommentUpdateSerializer(serializers.ModelSerializer):
    """Serializer for updating comments."""
    
    class Meta:
        model = Comment
        fields = ['content'] 